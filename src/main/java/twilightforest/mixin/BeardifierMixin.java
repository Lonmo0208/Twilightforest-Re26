package twilightforest.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.WorldgenHooks;
import twilightforest.world.components.structures.CustomDensitySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Mixin(Beardifier.class)
public class BeardifierMixin {

	/**
	 * Any Rigid (piece beard-box) whose minY is above this threshold is considered to
	 * belong to an airborne tower structure (Final Castle, Dark Tower, upper Lich Tower,
	 * etc.). Such rigids will be stripped from the beardifier because running BEARD_BOX
	 * / BEARD_THIN / BURY on boxes that sit in mid-air produces floating terrain discs
	 * that appear to "hold up" the structure in visually broken ways.
	 * <p>
	 * Twilight Forest's main terrain surface sits between Y60-Y90 so a cutoff of 100
	 * keeps all legitimate surface-piece beardification (e.g. Naga Courtyard terraces)
	 * intact while catching every piece of the elevated towers.
	 */
	@Unique
	private static final int TF_AIRBORNE_RIGID_THRESHOLD_Y = 100;

	/**
	 * Stores custom density functions keyed by the specific Beardifier instance used for
	 * that chunk. Since Beardifier.EMPTY is a JVM-wide singleton shared by all chunks with
	 * no terrain-adapting structures, we can NEVER use EMPTY itself as the map key or
	 * multiple concurrent chunks' custom densities will overwrite each other.
	 * <p>
	 * Instead, when we encounter EMPTY but have custom densities (e.g. Hollow Hill with
	 * TerrainAdjustment.NONE), we swap EMPTY for a freshly-constructed Beardifier clone
	 * with null affectedBox and empty rigids. This new instance behaves exactly like EMPTY
	 * for all vanilla purposes (compute() returns 0 immediately because affectedBox is null
	 * and rigids/junctions are empty) but gives us a unique per-chunk key in this map.
	 */
	@Unique
	private static final Map<Beardifier, ObjectList<DensitySampler>> TF_CUSTOM_DENSITIES = Collections.synchronizedMap(new WeakHashMap<>());

	/**
	 * CompileContext stub for compiling custom terrain-former densities at structure
	 * gather time. Our custom density functions are pure vertex functions (no noise
	 * sampling), so the noise/random factories are never invoked.
	 */
	@Unique
	private static final DensityFunction.CompileContext TF_NO_COMPILE_CONTEXT = new DensityFunction.CompileContext() {
		@Override
		public Noise createNoiseSampler(Holder<NormalNoise> noise) {
			throw new UnsupportedOperationException("Custom TF densities cannot sample noise");
		}

		@Override
		public RandomSource createRandom(Identifier id) {
			throw new UnsupportedOperationException("Custom TF densities cannot create random");
		}

		@Override
		public RandomSource createEndIslandRandom() {
			throw new UnsupportedOperationException("Custom TF densities cannot create random");
		}
	};

	@Inject(method = "forStructuresInChunk", at = @At("RETURN"), cancellable = true)
	private static void tf$gatherCustomDensitiesAndStripAirborneRigids(StructureManager structureManager, ChunkPos chunkPos, CallbackInfoReturnable<Beardifier> cir) {
		Beardifier original = cir.getReturnValue();

		// Both vanilla rigids (added from structure-level TerrainAdjustment) and custom
		// rigids (added later by addPieceBeardifierModifiers for PieceBeardifierModifier
		// surface pieces) need to be filtered when they belong to pieces that sit high
		// in the air. Doing this once here, on the already-assembled beardifier, covers
		// every source uniformly.
		Beardifier stripped = tf$stripAirborneRigids(original);

		// Surface pieces (<100) still get their piece-level terrain adjustments swapped in
		// (vanilla rigid removed + custom rigid added). Any accidentally-added custom
		// airborne rigids here are harmless because if they crossed the 100 threshold they
		// would have been caught by the strip pass above if called after. But we call
		// strip BEFORE modifiers so modifier-added surface rigids survive. Actually we
		// need strip AFTER modifiers... so re-run strip on the final result.
		Beardifier modified = WorldgenHooks.addPieceBeardifierModifiers(structureManager, chunkPos, stripped);
		if (modified != stripped) {
			// PieceBeardifierModifier produced a new beardifier; strip any airborne rigids
			// that may have slipped in (shouldn't happen because addPieceBeardifierModifiers
			// itself now short-circuits for airborne pieces, but belt-and-suspenders).
			modified = tf$stripAirborneRigids(modified);
		}

		ObjectList<DensitySampler> customDensities = new ObjectArrayList<>(4);
		for (StructureStart start : structureManager.startsForStructure(chunkPos.x(), chunkPos.z(), s -> s instanceof CustomDensitySource)) {
			if (start.getStructure() instanceof CustomDensitySource customDensitySource) {
				customDensities.add(customDensitySource.getStructureTerraformer(chunkPos, start).compileSampler(TF_NO_COMPILE_CONTEXT));
			}
		}

		if (!customDensities.isEmpty()) {
			Beardifier key = modified;
			if (key == Beardifier.EMPTY) {
				// Swap the shared EMPTY singleton for a unique instance with identical
				// vanilla behavior (null affectedBox + empty rigids) so each chunk's
				// custom densities don't overwrite each other via the shared-map key.
				key = BeardifierAccessor.tf$create(List.of(), List.of(), null);
			}
			TF_CUSTOM_DENSITIES.put(key, customDensities);
			cir.setReturnValue(key);
		} else {
			cir.setReturnValue(modified);
		}
	}

	@Unique
	private static Beardifier tf$stripAirborneRigids(Beardifier beardifier) {
		if (beardifier == Beardifier.EMPTY) return beardifier;

		List<Beardifier.Rigid> originalPieces = ((BeardifierAccessor) (Object) beardifier).tf$getPieces();
		List<JigsawJunction> originalJunctions = ((BeardifierAccessor) (Object) beardifier).tf$getJunctions();
		BoundingBox originalBox = ((BeardifierAccessor) (Object) beardifier).tf$getAffectedBox();

		List<Beardifier.Rigid> filtered = new ArrayList<>(originalPieces.size());
		int removed = 0;
		for (Beardifier.Rigid rigid : originalPieces) {
			if (rigid.box().minY() >= TF_AIRBORNE_RIGID_THRESHOLD_Y) {
				removed++;
			} else {
				filtered.add(rigid);
			}
		}

		if (removed == 0) {
			// Nothing to strip; keep original instance to preserve identity for map lookups
			return beardifier;
		}

		// Rebuild affectedBox from the remaining surface-only rigids (plus junctions)
		BoundingBox affectedBox = null;
		for (Beardifier.Rigid rigid : filtered) {
			affectedBox = tf$include(affectedBox, rigid.box());
		}
		for (JigsawJunction junction : originalJunctions) {
			BoundingBox junctionBox = new BoundingBox(
				new BlockPos(junction.getSourceX(), junction.getSourceGroundY(), junction.getSourceZ())
			);
			affectedBox = tf$include(affectedBox, junctionBox);
		}
		if (affectedBox != null) affectedBox = affectedBox.inflatedBy(24);

		return BeardifierAccessor.tf$create(
			List.copyOf(filtered),
			List.copyOf(originalJunctions),
			affectedBox
		);
	}

	@Unique
	private static BoundingBox tf$include(@Nullable BoundingBox outer, BoundingBox inner) {
		return outer == null ? inner : BoundingBox.encapsulating(outer, inner);
	}

	@Inject(method = "sampleValue", at = @At("RETURN"), cancellable = true)
	private void tf$addCustomDensity(SamplerContext context, int x, int y, int z, CallbackInfoReturnable<Float> cir) {
		ObjectList<DensitySampler> densities = TF_CUSTOM_DENSITIES.get(this);
		if (densities != null && !densities.isEmpty()) {
			float original = cir.getReturnValue();
			float added = 0;
			for (int i = 0; i < densities.size(); i++) {
				added += densities.get(i).sampleValue(context, x, y, z);
			}
			cir.setReturnValue(original + added);
		}
	}

	/**
	 * Overrides EMPTY.sampleVolume()'s fast-path that skips computing entirely by leaving
	 * the output buffer untouched. When we have custom densities attached to this Beardifier
	 * instance, we force sampleVolumeNaive which iterates every cell and calls sampleValue(),
	 * letting our tf$addCustomDensity mixin above modify every noise cell's value.
	 */
	@Inject(method = "sampleVolume", at = @At("HEAD"), cancellable = true)
	private void tf$fillVolumeWithCustomDensities(SamplerContext context, DensityBuffer buffer, DensityVolume volume, CallbackInfo ci) {
		ObjectList<DensitySampler> densities = TF_CUSTOM_DENSITIES.get(this);
		if (densities != null && !densities.isEmpty()) {
			DensitySampler.sampleVolumeNaive(context, buffer, volume, (DensitySampler) (Object) this);
			ci.cancel();
		}
	}
}
