package twilightforest.world.components.structures.lichtowerrevamp;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;


import twilightforest.TFRegistries;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.util.woods.WoodPalette;
import twilightforest.world.components.processors.WoodMultiPaletteSwizzle;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LichTowerSpawnerBridge extends TwilightJigsawPiece  {
	private static final LichTowerUtil lichTowerUtil = new LichTowerUtil();

	private final boolean invertedPalette;

	public LichTowerSpawnerBridge(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_SPAWNER_BRIDGE, compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(lichTowerUtil.getCentralBridgeSpawnerProcessor()));

		this.invertedPalette = compoundTag.getBooleanOr("inverted", false);

		if (this.invertedPalette) {
			RegistryAccess registryAccess = ctx.registryAccess();
			addInvertedWoodProcessors(registryAccess, this.placeSettings);
		}
	}

	private static void addInvertedWoodProcessors(RegistryAccess registryAccess, StructurePlaceSettings placeSettings) {
		Optional<Registry<WoodPalette>> woodPalettes = registryAccess.lookup(TFRegistries.Keys.WOOD_PALETTES);

		if (woodPalettes.isPresent()) {
			Registry<WoodPalette> woodPaletteRegistry = woodPalettes.get();
			Holder<WoodPalette> twiOak = woodPaletteRegistry.getOrThrow(WoodPalettes.TWILIGHT_OAK);
			Holder<WoodPalette> canopy = woodPaletteRegistry.getOrThrow(WoodPalettes.CANOPY);
			placeSettings.addProcessor(new WoodMultiPaletteSwizzle(List.of(
				Pair.of(twiOak, canopy),
				Pair.of(canopy, twiOak)
			)));
		}
	}

	public LichTowerSpawnerBridge(int genDepth, StructureTemplateManager structureManager, Identifier templateLocation, JigsawPlaceContext jigsawContext, boolean invertedPalette) {
		super(TFStructurePieceTypes.LICH_SPAWNER_BRIDGE, genDepth, structureManager, templateLocation, jigsawContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(lichTowerUtil.getCentralBridgeSpawnerProcessor()));

		this.invertedPalette = invertedPalette;

		if (this.invertedPalette) {
			// RegistryAccess not available in live-construction constructor; palette inversion is handled via NBT serialization
		}
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putBoolean("inverted", this.invertedPalette);
	}

	@Override
	protected void processJigsaw(TwilightJigsawPiece parent, StructurePieceAccessor pieceAccessor, Structure.GenerationContext context, JigsawRecord connection, int jigsawIndex) {
	}
}
