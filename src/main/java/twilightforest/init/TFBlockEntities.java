package twilightforest.init;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.TwilightForestMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import twilightforest.block.entity.*;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.block.entity.spawner.*;

public class TFBlockEntities {

	public static final BlockEntityType<AntibuilderBlockEntity> ANTIBUILDER =
		FabricBlockEntityTypeBuilder.create(AntibuilderBlockEntity::new, TFBlocks.ANTIBUILDER).build();
	public static final BlockEntityType<CinderFurnaceBlockEntity> CINDER_FURNACE =
		FabricBlockEntityTypeBuilder.create(CinderFurnaceBlockEntity::new, TFBlocks.CINDER_FURNACE).build();
	public static final BlockEntityType<CarminiteReactorBlockEntity> CARMINITE_REACTOR =
		FabricBlockEntityTypeBuilder.create(CarminiteReactorBlockEntity::new, TFBlocks.CARMINITE_REACTOR).build();
	public static final BlockEntityType<ReactorDebrisBlockEntity> REACTOR_DEBRIS =
		FabricBlockEntityTypeBuilder.create(ReactorDebrisBlockEntity::new, TFBlocks.REACTOR_DEBRIS).build();
	public static final BlockEntityType<FireJetBlockEntity> FLAME_JET =
		FabricBlockEntityTypeBuilder.create(FireJetBlockEntity::new, TFBlocks.FIRE_JET, TFBlocks.ENCASED_FIRE_JET).build();
	public static final BlockEntityType<GhastTrapBlockEntity> GHAST_TRAP =
		FabricBlockEntityTypeBuilder.create(GhastTrapBlockEntity::new, TFBlocks.GHAST_TRAP).build();
	public static final BlockEntityType<TFSmokerBlockEntity> SMOKER =
		FabricBlockEntityTypeBuilder.create(TFSmokerBlockEntity::new, TFBlocks.SMOKER, TFBlocks.ENCASED_SMOKER).build();
	public static final BlockEntityType<CarminiteBuilderBlockEntity> TOWER_BUILDER =
		FabricBlockEntityTypeBuilder.create(CarminiteBuilderBlockEntity::new, TFBlocks.CARMINITE_BUILDER).build();
	public static final BlockEntityType<TrophyBlockEntity> TROPHY =
		FabricBlockEntityTypeBuilder.create(TrophyBlockEntity::new, TFBlocks.NAGA_TROPHY, TFBlocks.LICH_TROPHY, TFBlocks.MINOSHROOM_TROPHY,
			TFBlocks.HYDRA_TROPHY, TFBlocks.KNIGHT_PHANTOM_TROPHY, TFBlocks.UR_GHAST_TROPHY, TFBlocks.ALPHA_YETI_TROPHY,
			TFBlocks.SNOW_QUEEN_TROPHY, TFBlocks.QUEST_RAM_TROPHY, TFBlocks.NAGA_WALL_TROPHY, TFBlocks.LICH_WALL_TROPHY,
			TFBlocks.MINOSHROOM_WALL_TROPHY, TFBlocks.HYDRA_WALL_TROPHY, TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY, TFBlocks.UR_GHAST_WALL_TROPHY,
			TFBlocks.ALPHA_YETI_WALL_TROPHY, TFBlocks.SNOW_QUEEN_WALL_TROPHY, TFBlocks.QUEST_RAM_WALL_TROPHY).build();
	public static final BlockEntityType<AlphaYetiSpawnerBlockEntity> ALPHA_YETI_SPAWNER =
		FabricBlockEntityTypeBuilder.create(AlphaYetiSpawnerBlockEntity::new, TFBlocks.ALPHA_YETI_BOSS_SPAWNER).build();
	public static final BlockEntityType<FinalBossSpawnerBlockEntity> FINAL_BOSS_SPAWNER =
		FabricBlockEntityTypeBuilder.create(FinalBossSpawnerBlockEntity::new, TFBlocks.FINAL_BOSS_BOSS_SPAWNER).build();
	public static final BlockEntityType<HydraSpawnerBlockEntity> HYDRA_SPAWNER =
		FabricBlockEntityTypeBuilder.create(HydraSpawnerBlockEntity::new, TFBlocks.HYDRA_BOSS_SPAWNER).build();
	public static final BlockEntityType<KnightPhantomSpawnerBlockEntity> KNIGHT_PHANTOM_SPAWNER =
		FabricBlockEntityTypeBuilder.create(KnightPhantomSpawnerBlockEntity::new, TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER).build();
	public static final BlockEntityType<LichSpawnerBlockEntity> LICH_SPAWNER =
		FabricBlockEntityTypeBuilder.create(LichSpawnerBlockEntity::new, TFBlocks.LICH_BOSS_SPAWNER).build();
	public static final BlockEntityType<MinoshroomSpawnerBlockEntity> MINOSHROOM_SPAWNER =
		FabricBlockEntityTypeBuilder.create(MinoshroomSpawnerBlockEntity::new, TFBlocks.MINOSHROOM_BOSS_SPAWNER).build();
	public static final BlockEntityType<NagaSpawnerBlockEntity> NAGA_SPAWNER =
		FabricBlockEntityTypeBuilder.create(NagaSpawnerBlockEntity::new, TFBlocks.NAGA_BOSS_SPAWNER).build();
	public static final BlockEntityType<SnowQueenSpawnerBlockEntity> SNOW_QUEEN_SPAWNER =
		FabricBlockEntityTypeBuilder.create(SnowQueenSpawnerBlockEntity::new, TFBlocks.SNOW_QUEEN_BOSS_SPAWNER).build();
	public static final BlockEntityType<UrGhastSpawnerBlockEntity> UR_GHAST_SPAWNER =
		FabricBlockEntityTypeBuilder.create(UrGhastSpawnerBlockEntity::new, TFBlocks.UR_GHAST_BOSS_SPAWNER).build();

	public static final BlockEntityType<CicadaBlockEntity> CICADA =
		FabricBlockEntityTypeBuilder.create(CicadaBlockEntity::new, TFBlocks.CICADA).build();
	public static final BlockEntityType<FireflyBlockEntity> FIREFLY =
		FabricBlockEntityTypeBuilder.create(FireflyBlockEntity::new, TFBlocks.FIREFLY).build();
	public static final BlockEntityType<MoonwormBlockEntity> MOONWORM =
		FabricBlockEntityTypeBuilder.create(MoonwormBlockEntity::new, TFBlocks.MOONWORM).build();

	public static final BlockEntityType<SkullChestBlockEntity> SKULL_CHEST =
		FabricBlockEntityTypeBuilder.create(SkullChestBlockEntity::new, TFBlocks.SKULL_CHEST).build();
	public static final BlockEntityType<KeepsakeCasketBlockEntity> KEEPSAKE_CASKET =
		FabricBlockEntityTypeBuilder.create(KeepsakeCasketBlockEntity::new, TFBlocks.KEEPSAKE_CASKET).build();
	public static final BlockEntityType<BrazierBlockEntity> BRAZIER =
		FabricBlockEntityTypeBuilder.create(BrazierBlockEntity::new, TFBlocks.BRAZIER).build();

	public static final BlockEntityType<TFChestBlockEntity> TF_CHEST =
		FabricBlockEntityTypeBuilder.create(TFChestBlockEntity::new,
			TFBlocks.TWILIGHT_OAK_CHEST, TFBlocks.CANOPY_CHEST, TFBlocks.MANGROVE_CHEST,
			TFBlocks.DARK_CHEST, TFBlocks.TIME_CHEST, TFBlocks.TRANSFORMATION_CHEST,
			TFBlocks.MINING_CHEST, TFBlocks.SORTING_CHEST).build();

	public static final BlockEntityType<TFTrappedChestBlockEntity> TF_TRAPPED_CHEST =
		FabricBlockEntityTypeBuilder.create(TFTrappedChestBlockEntity::new,
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, TFBlocks.CANOPY_TRAPPED_CHEST, TFBlocks.MANGROVE_TRAPPED_CHEST,
			TFBlocks.DARK_TRAPPED_CHEST, TFBlocks.TIME_TRAPPED_CHEST, TFBlocks.TRANSFORMATION_TRAPPED_CHEST,
			TFBlocks.MINING_TRAPPED_CHEST, TFBlocks.SORTING_TRAPPED_CHEST).build();

	public static final BlockEntityType<SkullCandleBlockEntity> SKULL_CANDLE =
		FabricBlockEntityTypeBuilder.create(SkullCandleBlockEntity::new,
			TFBlocks.ZOMBIE_SKULL_CANDLE, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE,
			TFBlocks.SKELETON_SKULL_CANDLE, TFBlocks.SKELETON_WALL_SKULL_CANDLE,
			TFBlocks.WITHER_SKELE_SKULL_CANDLE, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE,
			TFBlocks.CREEPER_SKULL_CANDLE, TFBlocks.CREEPER_WALL_SKULL_CANDLE,
			TFBlocks.PLAYER_SKULL_CANDLE, TFBlocks.PLAYER_WALL_SKULL_CANDLE,
			TFBlocks.PIGLIN_SKULL_CANDLE, TFBlocks.PIGLIN_WALL_SKULL_CANDLE).build();

	public static final BlockEntityType<ChiseledCanopyShelfBlockEntity> CHISELED_CANOPY_BOOKSHELF =
		FabricBlockEntityTypeBuilder.create(ChiseledCanopyShelfBlockEntity::new, TFBlocks.CHISELED_CANOPY_BOOKSHELF).build();

	public static final BlockEntityType<GrowingBeanstalkBlockEntity> BEANSTALK_GROWER =
		FabricBlockEntityTypeBuilder.create(GrowingBeanstalkBlockEntity::new, TFBlocks.BEANSTALK_GROWER).build();

	public static final BlockEntityType<RedThreadBlockEntity> RED_THREAD =
		FabricBlockEntityTypeBuilder.create(RedThreadBlockEntity::new, TFBlocks.RED_THREAD).build();

	public static final BlockEntityType<CandelabraBlockEntity> CANDELABRA =
		FabricBlockEntityTypeBuilder.create(CandelabraBlockEntity::new, TFBlocks.CANDELABRA).build();

	public static final BlockEntityType<JarBlockEntity> JAR =
		FabricBlockEntityTypeBuilder.create(JarBlockEntity::new, TFBlocks.FIREFLY_JAR, TFBlocks.CICADA_JAR).build();

	public static final BlockEntityType<MasonJarBlockEntity> MASON_JAR =
		FabricBlockEntityTypeBuilder.create(MasonJarBlockEntity::new, TFBlocks.MASON_JAR).build();

	public static final BlockEntityType<SinisterSpawnerBlockEntity> SINISTER_SPAWNER =
		FabricBlockEntityTypeBuilder.create(SinisterSpawnerBlockEntity::new, TFBlocks.SINISTER_SPAWNER).build();

	public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACK =
		FabricBlockEntityTypeBuilder.create(DryingRackBlockEntity::new,
			TFBlocks.OAK_DRYING_RACK, TFBlocks.SPRUCE_DRYING_RACK,
			TFBlocks.BIRCH_DRYING_RACK, TFBlocks.JUNGLE_DRYING_RACK,
			TFBlocks.ACACIA_DRYING_RACK, TFBlocks.DARK_OAK_DRYING_RACK,
			TFBlocks.CRIMSON_DRYING_RACK, TFBlocks.WARPED_DRYING_RACK,
			TFBlocks.VANGROVE_DRYING_RACK, TFBlocks.BAMBOO_DRYING_RACK,
			TFBlocks.CHERRY_DRYING_RACK, TFBlocks.PALE_OAK_DRYING_RACK,
			TFBlocks.TWILIGHT_OAK_DRYING_RACK, TFBlocks.CANOPY_DRYING_RACK,
			TFBlocks.MANGROVE_DRYING_RACK, TFBlocks.DARK_DRYING_RACK,
			TFBlocks.TIME_DRYING_RACK, TFBlocks.TRANSFORMATION_DRYING_RACK,
			TFBlocks.MINING_DRYING_RACK, TFBlocks.SORTING_DRYING_RACK).build();

	public static final BlockEntityType<OminousCandleBlockEntity> OMINOUS_CANDLE =
		FabricBlockEntityTypeBuilder.create(OminousCandleBlockEntity::new,
			TFBlocks.OMINOUS_CANDLE,
			TFBlocks.OMINOUS_WHITE_CANDLE,
			TFBlocks.OMINOUS_ORANGE_CANDLE,
			TFBlocks.OMINOUS_MAGENTA_CANDLE,
			TFBlocks.OMINOUS_LIGHT_BLUE_CANDLE,
			TFBlocks.OMINOUS_YELLOW_CANDLE,
			TFBlocks.OMINOUS_LIME_CANDLE,
			TFBlocks.OMINOUS_PINK_CANDLE,
			TFBlocks.OMINOUS_GRAY_CANDLE,
			TFBlocks.OMINOUS_LIGHT_GRAY_CANDLE,
			TFBlocks.OMINOUS_CYAN_CANDLE,
			TFBlocks.OMINOUS_PURPLE_CANDLE,
			TFBlocks.OMINOUS_BLUE_CANDLE,
			TFBlocks.OMINOUS_BROWN_CANDLE,
			TFBlocks.OMINOUS_GREEN_CANDLE,
			TFBlocks.OMINOUS_RED_CANDLE,
			TFBlocks.OMINOUS_BLACK_CANDLE).build();

	public static void init() {
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("antibuilder"), ANTIBUILDER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("cinder_furnace"), CINDER_FURNACE);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("carminite_reactor"), CARMINITE_REACTOR);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("reactor_debris"), REACTOR_DEBRIS);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("flame_jet"), FLAME_JET);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("ghast_trap"), GHAST_TRAP);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("smoker"), SMOKER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("tower_builder"), TOWER_BUILDER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("trophy"), TROPHY);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("alpha_yeti_spawner"), ALPHA_YETI_SPAWNER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("final_boss_spawner"), FINAL_BOSS_SPAWNER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("hydra_boss_spawner"), HYDRA_SPAWNER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("knight_phantom_spawner"), KNIGHT_PHANTOM_SPAWNER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("lich_spawner"), LICH_SPAWNER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("minoshroom_spawner"), MINOSHROOM_SPAWNER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("naga_spawner"), NAGA_SPAWNER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("snow_queen_spawner"), SNOW_QUEEN_SPAWNER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("tower_boss_spawner"), UR_GHAST_SPAWNER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("cicada"), CICADA);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("firefly"), FIREFLY);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("moonworm"), MOONWORM);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("skull_chest"), SKULL_CHEST);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("keepsake_casket"), KEEPSAKE_CASKET);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("brazier"), BRAZIER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("chest"), TF_CHEST);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("trapped_chest"), TF_TRAPPED_CHEST);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("skull_candle"), SKULL_CANDLE);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("chiseled_canopy_bookshelf"), CHISELED_CANOPY_BOOKSHELF);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("beanstalk_grower"), BEANSTALK_GROWER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("red_thread"), RED_THREAD);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("candelabra"), CANDELABRA);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("jar"), JAR);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("mason_jar"), MASON_JAR);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("sinister_spawner"), SINISTER_SPAWNER);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("drying_rack"), DRYING_RACK);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightForestMod.prefix("ominous_candle"), OMINOUS_CANDLE);
	}
}
