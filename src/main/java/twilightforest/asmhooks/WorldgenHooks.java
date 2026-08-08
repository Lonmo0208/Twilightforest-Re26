package twilightforest.asmhooks;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.mixin.BeardifierAccessor;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;
import twilightforest.world.components.structures.util.PieceBeardifierModifier;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused"})
public class WorldgenHooks {

	public static ObjectList<DensityFunction> gatherCustomTerrain(StructureManager structureManager, ChunkPos chunkPos) {
		ObjectArrayList<DensityFunction> customStructureTerraforms = new ObjectArrayList<>(10);

		for (StructureStart structureStart : structureManager.startsForStructure(chunkPos, s -> s instanceof CustomDensitySource))
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource)
				customStructureTerraforms.add(customDensitySource.getStructureTerraformer(chunkPos, structureStart));

		TwilightForestMod.LOGGER.debug("TF-WorldgenHooks: Gathered {} custom densities for chunk ({},{})", customStructureTerraforms.size(), chunkPos.x(), chunkPos.z());
		return customStructureTerraforms;
	}

	public static double getCustomDensity(double original, DensityFunction.FunctionContext context, ObjectList<DensityFunction> customDensities) {
		if (customDensities == null || customDensities.isEmpty())
			return original;

		double addedDensity = 0;

		for (DensityFunction customDensity : customDensities) {
			addedDensity += customDensity.compute(context);
		}

		return original + addedDensity;
	}

	/**
	 * Rebuilds the Beardifier with piece-specific terrain adjustments for PieceBeardifierModifier pieces.
	 * Removes the vanilla rigid for such pieces and replaces it with a custom rigid using the piece's
	 * getBeardifierBox() and getTerrainAdjustment().
	 * <p>
	 * Mirror's NeoForge's PieceBeardifierModifier behavior: piece-level terrain adjustment
	 * overrides structure-level adjustment.
	 */
	public static Beardifier addPieceBeardifierModifiers(StructureManager structureManager, ChunkPos chunkPos, Beardifier original) {
		List<Beardifier.Rigid> pieces = new ArrayList<>(((BeardifierAccessor) (Object) original).tf$getPieces());
		List<JigsawJunction> junctions = new ArrayList<>(((BeardifierAccessor) (Object) original).tf$getJunctions());
		boolean changed = false;

		for (StructureStart start : structureManager.startsForStructure(chunkPos, s -> true)) {
			TerrainAdjustment structureAdjustment = start.getStructure().terrainAdaptation();
			for (StructurePiece piece : start.getPieces()) {
				if (piece instanceof PieceBeardifierModifier modifier && piece.isCloseToChunk(chunkPos, 12)) {
					BoundingBox pieceBox = piece.getBoundingBox();
					boolean isAirborne = pieceBox.minY() >= 100;

					if (isAirborne) {
						// Classification: this piece belongs to an airborne tower structure
						// (Lich Tower, Dark Tower, Final Castle). Keep vanilla rigids and do
						// NOT insert custom PieceBeardifierModifier rigids. The custom rigid
						// boxes assume ground-level placement and produce floating terrain
						// discs when placed in mid-air.
						continue;
					}

					// Surface piece: remove vanilla rigid and replace with piece's custom one
					changed |= removeVanillaRigidForPiece(pieces, piece, structureAdjustment);

					TerrainAdjustment pieceAdjustment = modifier.getTerrainAdjustment();
					if (pieceAdjustment != TerrainAdjustment.NONE) {
						Beardifier.Rigid customRigid = new Beardifier.Rigid(
							modifier.getBeardifierBox(),
							pieceAdjustment,
							modifier.getGroundLevelDelta()
						);
						// Avoid adding duplicate rigids
						boolean alreadyPresent = false;
						for (Beardifier.Rigid existing : pieces) {
							if (sameBox(existing.box(), customRigid.box())
								&& existing.terrainAdjustment() == customRigid.terrainAdjustment()
								&& existing.groundLevelDelta() == customRigid.groundLevelDelta()) {
								alreadyPresent = true;
								break;
							}
						}
						if (!alreadyPresent) {
							pieces.add(customRigid);
							changed = true;
							TwilightForestMod.LOGGER.debug("TF-WorldgenHooks: Added custom rigid for {} with box [{},{}]-[{},{}]",
								piece.getClass().getSimpleName(),
								customRigid.box().minX(), customRigid.box().minY(),
								customRigid.box().maxX(), customRigid.box().maxY());
						}
					}
				}
			}
		}

		if (!changed) {
			return original;
		}

		// Recompute affectedBox from the updated pieces list.
		BoundingBox affectedBox = null;
		for (Beardifier.Rigid rigid : pieces) {
			affectedBox = includeBoundingBox(affectedBox, rigid.box());
		}
		for (JigsawJunction junction : junctions) {
			BoundingBox junctionBox = new BoundingBox(
				new BlockPos(junction.getSourceX(), junction.getSourceGroundY(), junction.getSourceZ())
			);
			affectedBox = includeBoundingBox(affectedBox, junctionBox);
		}
		if (affectedBox != null) {
			affectedBox = affectedBox.inflatedBy(24);
		}
		return BeardifierAccessor.tf$create(List.copyOf(pieces), List.copyOf(junctions), affectedBox);
	}

	/**
	 * Removes the vanilla rigid that was added for a specific piece.
	 * For non-pool pieces, vanilla creates a Rigid with piece.getBoundingBox(), structureAdjustment, groundLevelDelta=0.
	 * For pool pieces with RIGID projection, vanilla uses the pool piece's bounding box and ground delta.
	 */
	private static boolean removeVanillaRigidForPiece(List<Beardifier.Rigid> pieces, StructurePiece piece, TerrainAdjustment structureAdjustment) {
		if (structureAdjustment == TerrainAdjustment.NONE) {
			return false;
		}

		int vanillaGroundLevelDelta = 0;
		if (piece instanceof PoolElementStructurePiece poolPiece) {
			if (poolPiece.getElement().getProjection() != StructureTemplatePool.Projection.RIGID) {
				return false;
			}
			vanillaGroundLevelDelta = poolPiece.getGroundLevelDelta();
		}

		Beardifier.Rigid vanillaRigid = new Beardifier.Rigid(piece.getBoundingBox(), structureAdjustment, vanillaGroundLevelDelta);
		return pieces.remove(vanillaRigid);
	}

	/**
	 * Compares two BoundingBox instances by value (since BoundingBox uses reference equality for equals).
	 * Needed for duplicate detection when our custom box differs from the vanilla box.
	 */
	private static boolean sameBox(BoundingBox a, BoundingBox b) {
		return a.minX() == b.minX() && a.minY() == b.minY() && a.minZ() == b.minZ()
			&& a.maxX() == b.maxX() && a.maxY() == b.maxY() && a.maxZ() == b.maxZ();
	}

	private static BoundingBox includeBoundingBox(@Nullable BoundingBox encompassingBox, BoundingBox newBox) {
		return encompassingBox == null ? newBox : BoundingBox.encapsulating(encompassingBox, newBox);
	}

	public static void chunkBlanketing(ChunkAccess access, WorldGenRegion region) {
		ChunkBlanketProcessors.chunkBlanketing(access, region);
	}

	public static StructureStart loadStaticStart(StructureStart start, PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData s)
			return s.forDeserialization(start.getStructure(), start.getChunkPos(), start.getReferences(), piecesContainer, nbt);
		return start;
	}
}
