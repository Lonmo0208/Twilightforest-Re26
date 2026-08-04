package twilightforest.world.components.structures.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMapDecorations;
import twilightforest.init.TFStructureTypes;
import twilightforest.tags.TFBiomeTags;
import twilightforest.world.components.structures.lichtower.TowerMainComponent;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LichTowerStructure extends ControlledSpawningStructure {
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

	@SuppressWarnings("unchecked")
	public static LichTowerStructure buildLichTowerConfig(BootstrapContext<Structure> context) {
		return new LichTowerStructure(
			ControlledSpawningConfig.firstIndexMonsters(
				WeightedList.<MobSpawnSettings.SpawnerData>builder()
					.add(new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 1, 2), 10)
					.add(new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 1, 2), 10)
					.add(new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1), 1)
					.add(new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 2), 1)
					.add(new MobSpawnSettings.SpawnerData(TFEntities.DEATH_TOME.get(), 1, 2), 5)
					.add(new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1), 1)
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
