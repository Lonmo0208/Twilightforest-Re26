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
import twilightforest.world.components.structures.minotaurmaze.MazeRuinsComponent;
import twilightforest.world.components.structures.util.ConfigurableSpawns;
import twilightforest.world.components.structures.util.ControlledSpawningStructure;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LabyrinthStructure extends ControlledSpawningStructure implements ConfigurableSpawns {
	public static final MapCodec<LabyrinthStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		controlledSpawningCodec(instance).apply(instance, LabyrinthStructure::new)
	);

	public LabyrinthStructure(ControlledSpawningConfig controlledSpawningConfig, AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, centerInChunk, structureIcon, structureSettings);
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new MazeRuinsComponent(0, x + 5, y, z + 5); // Offset centers labyrinth mound on intersection of 4 chunk boundaries
	}

	@Override
	public ControlledSpawningConfig getConfig() {
		return this.controlledSpawningConfig;
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.LABYRINTH;
	}

	public static LabyrinthStructure buildLabyrinthConfig(BootstrapContext<Structure> context) {
		return new LabyrinthStructure(
			ControlledSpawningConfig.firstIndexMonsters(WeightedList.<MobSpawnSettings.SpawnerData>builder()
				.add(new MobSpawnSettings.SpawnerData(TFEntities.MINOTAUR.get(), 2, 3), 20)
				.add(new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER, 1, 2), 10)
				.add(new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 2), 10)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.MAZE_SLIME.get(), 2, 4), 10)
				.add(new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 2), 1)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.FIRE_BEETLE.get(), 1, 2), 10)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.SLIME_BEETLE.get(), 1, 2), 10)
				.add(new MobSpawnSettings.SpawnerData(TFEntities.PINCH_BEETLE.get(), 1, 1), 10)
				.build()
			),
			new AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_lich"))),
			Optional.of(new HintConfig(HintConfig.book("labyrinth", 5), TFEntities.KOBOLD.get())),
			Optional.of(new DecorationConfig(3, true, false, false)),
			true, Optional.of(BuiltInRegistries.MAP_DECORATION_TYPE.wrapAsHolder(TFMapDecorations.LABYRINTH)),
			new StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_LABYRINTH_BIOMES),
				Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
				GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
				TerrainAdjustment.BURY
			)
		);
	}
}
