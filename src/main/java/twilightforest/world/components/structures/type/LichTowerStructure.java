package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMapDecorations;
import twilightforest.init.TFStructureTypes;
import twilightforest.tags.TFBiomeTags;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.BoxDensityFunction;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.lichtower.TowerMainComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LichTowerStructure extends ControlledSpawningStructure implements CustomDensitySource {
	public static final MapCodec<LichTowerStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		controlledSpawningCodec(instance).apply(instance, LichTowerStructure::new)
	);

	public LichTowerStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new TowerMainComponent(random, 0, x, y, z);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.LICH_TOWER;
	}

	@Override
	public DensityFunction getStructureTerraformer(ChunkPos chunkPosAt, StructureStart structurePieceSource) {
		BoundingBox mainPieceBox = structurePieceSource.getPieces().getFirst().getBoundingBox();

		int yBase = mainPieceBox.minY();

		DensityFunction activator = DensityFunctions.yClampedGradient(yBase - 2, yBase - 1, 1, 0);

		DensityFunction bury = BoxDensityFunction.make(mainPieceBox, -5, -5, TerrainAdjustment.BURY);

		return DensityFunctions.mul(activator, bury);
	}

	@Override
	public int adjustForTerrain(GenerationContext context, int x, int z) {
		return WorldUtil.adjustForTerrain(context, x, z, 32, 4);
	}

	@SuppressWarnings("unchecked")
	public static LichTowerStructure buildLichTowerConfig(BootstrapContext<Structure> context) {
		return new LichTowerStructure(
			ControlledSpawningConfig.firstIndexMonsters(
				WeightedList.<MobSpawnSettings.SpawnerData>builder()
					.add(new MobSpawnSettings.SpawnerData(EntityTypes.ZOMBIE, 1, 2), 10)
					.add(new MobSpawnSettings.SpawnerData(EntityTypes.SKELETON, 1, 2), 10)
					.add(new MobSpawnSettings.SpawnerData(EntityTypes.CREEPER, 1, 1), 1)
					.add(new MobSpawnSettings.SpawnerData(EntityTypes.ENDERMAN, 1, 2), 1)
					.add(new MobSpawnSettings.SpawnerData(TFEntities.DEATH_TOME.get(), 1, 2), 5)
					.add(new MobSpawnSettings.SpawnerData(EntityTypes.WITCH, 1, 1), 1)
					.build()
			),
			new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_naga"))),
			Optional.of(new HintConfig(HintConfig.book("lichtower", 4), TFEntities.KOBOLD.get())),
			Optional.of(new DecorationConfig(1, false, true, true)),
			true, Optional.of(BuiltInRegistries.MAP_DECORATION_TYPE.wrapAsHolder(TFMapDecorations.LICH_TOWER)),
			new StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_LICH_TOWER_BIOMES),
				Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))),
				GenerationStep.Decoration.SURFACE_STRUCTURES,
				TerrainAdjustment.BEARD_THIN
			)
		);
	}
}
