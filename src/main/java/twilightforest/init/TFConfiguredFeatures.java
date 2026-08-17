package twilightforest.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.RandomBooleanSelectorFeature;
import net.minecraft.world.level.levelgen.feature.RandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.NoOpFeature;
import net.minecraft.world.level.levelgen.feature.HugeBrownMushroomFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TorchberryPlantBlock;
import twilightforest.tags.TFBlockTags;
import twilightforest.tags.TFWoodPaletteTags;
import twilightforest.util.woods.WoodPalette;
import twilightforest.world.components.feature.TFSmallLakeFeature;
import twilightforest.world.components.feature.config.*;
import twilightforest.world.registration.TreeConfigurations;
import twilightforest.world.registration.TreeDecorators;

import java.util.List;

public final class TFConfiguredFeatures {

	//vanilla features with custom placement code
	public static final ResourceKey<Feature> LAKE_LAVA = registerKey("lava_lake");
	public static final ResourceKey<Feature> LAKE_WATER = registerKey("water_lake");
	public static final ResourceKey<Feature> LAKE_FROZEN = registerKey("water_frozen");

	//"structures" that arent actually structures
	public static final ResourceKey<Feature> SIMPLE_WELL = registerKey("simple_well");
	public static final ResourceKey<Feature> FANCY_WELL = registerKey("fancy_well");
	public static final ResourceKey<Feature> DRUID_HUT = registerKey("druid_hut");
	public static final ResourceKey<Feature> GRAVEYARD = registerKey("graveyard");

	//all the fun little things you find around the dimension
	public static final ResourceKey<Feature> BIG_MUSHGLOOM = registerKey("mushroom/big_mushgloom");
	public static final ResourceKey<Feature> DENSE_FERNS = registerKey("dense_ferns");
	public static final ResourceKey<Feature> DENSE_LARGE_FERNS = registerKey("dense_large_ferns");
	public static final ResourceKey<Feature> FALLEN_LEAVES = registerKey("fallen_leaves");
	public static final ResourceKey<Feature> MAYAPPLE = registerKey("mayapple");
	public static final ResourceKey<Feature> FIDDLEHEAD = registerKey("fiddlehead");
	public static final ResourceKey<Feature> FIRE_JET = registerKey("fire_jet");
	public static final ResourceKey<Feature> FOUNDATION = registerKey("foundation");
	public static final ResourceKey<Feature> GROVE_RUINS = registerKey("grove_ruins");
	public static final ResourceKey<Feature> HOLLOW_LOG = registerKey("hollow_log");
	public static final ResourceKey<Feature> HOLLOW_STUMP = registerKey("hollow_stump");
	public static final ResourceKey<Feature> HUGE_LILY_PAD = registerKey("huge_lily_pad");
	public static final ResourceKey<Feature> HUGE_WATER_LILY = registerKey("huge_water_lily");
	public static final ResourceKey<Feature> CICADA_LAMPPOST = registerKey("cicada_lamppost");
	public static final ResourceKey<Feature> FIREFLY_LAMPPOST = registerKey("firefly_lamppost");
	public static final ResourceKey<Feature> MONOLITH = registerKey("monolith");
	public static final ResourceKey<Feature> MUSHGLOOM_CLUSTER = registerKey("mushgloom_cluster");
	public static final ResourceKey<Feature> MYCELIUM_BLOB = registerKey("mycelium_blob");
	public static final ResourceKey<Feature> OUTSIDE_STALAGMITE = registerKey("outside_stalagmite");
	public static final ResourceKey<Feature> PLANT_ROOTS = registerKey("plant_roots");
	public static final ResourceKey<Feature> IRON_OREBERRIES = registerKey("iron_oreberries");
	public static final ResourceKey<Feature> GOLD_OREBERRIES = registerKey("gold_oreberries");
	public static final ResourceKey<Feature> COPPER_OREBERRIES = registerKey("copper_oreberries");
	public static final ResourceKey<Feature> ESSENCE_OREBERRIES = registerKey("essence_oreberries");
	public static final ResourceKey<Feature> PUMPKIN_LAMPPOST = registerKey("pumpkin_lamppost");
	public static final ResourceKey<Feature> RASPBERRY_BUSHES = registerKey("raspberry_bushes");
	public static final ResourceKey<Feature> BLUEBERRY_BUSHES = registerKey("blueberry_bushes");
	public static final ResourceKey<Feature> BLACKBERRY_BUSHES = registerKey("blackberry_bushes");
	public static final ResourceKey<Feature> MALOBERRY_BUSHES = registerKey("maloberry_bushes");
	public static final ResourceKey<Feature> SMOKER = registerKey("smoker");
	public static final ResourceKey<Feature> STONE_CIRCLE = registerKey("stone_circle");
	public static final ResourceKey<Feature> THORNS = registerKey("thorns");
	public static final ResourceKey<Feature> TORCH_BERRIES = registerKey("torch_berries");
	public static final ResourceKey<Feature> TROLL_ROOTS = registerKey("troll_roots");
	public static final ResourceKey<Feature> TROLL_BIG_MUSHGLOOMS = registerKey("troll_big_mushglooms");
	public static final ResourceKey<Feature> TROLL_HUGE_RED_MUSHROOMS = registerKey("troll_huge_red_mushrooms");
	public static final ResourceKey<Feature> TROLL_HUGE_BROWN_MUSHROOMS = registerKey("troll_huge_brown_mushrooms");
	public static final ResourceKey<Feature> TROLL_MUSHGLOOMS = registerKey("troll_mushglooms");
	public static final ResourceKey<Feature> VANILLA_ROOTS = registerKey("vanilla_roots");
	public static final ResourceKey<Feature> WEBS = registerKey("webs");
	public static final ResourceKey<Feature> WOOD_ROOTS_SPREAD = registerKey("ore/wood_roots_spread");
	public static final ResourceKey<Feature> SNOW_UNDER_TREES = registerKey("snow_under_trees");
	public static final ResourceKey<Feature> ENCHANTED_FOREST_VINES = registerKey("enchanted_forest_vines");

	//fallen logs!
	public static final ResourceKey<Feature> TF_OAK_FALLEN_LOG = registerKey("tf_oak_fallen_log");
	public static final ResourceKey<Feature> CANOPY_FALLEN_LOG = registerKey("canopy_fallen_log");
	public static final ResourceKey<Feature> MANGROVE_FALLEN_LOG = registerKey("mangrove_fallen_log");
	public static final ResourceKey<Feature> OAK_FALLEN_LOG = registerKey("oak_fallen_log");
	public static final ResourceKey<Feature> SPRUCE_FALLEN_LOG = registerKey("spruce_fallen_log");
	public static final ResourceKey<Feature> BIRCH_FALLEN_LOG = registerKey("birch_fallen_log");

	//smol stone veins
	public static final ResourceKey<Feature> SMALL_GRANITE = registerKey("small_granite");
	public static final ResourceKey<Feature> SMALL_DIORITE = registerKey("small_diorite");
	public static final ResourceKey<Feature> SMALL_ANDESITE = registerKey("small_andesite");

	//Ores! Lets keep pre 1.18 ore rates :)
	public static final ResourceKey<Feature> LEGACY_COAL_ORE = registerKey("legacy_coal_ore");
	public static final ResourceKey<Feature> LEGACY_IRON_ORE = registerKey("legacy_iron_ore");
	public static final ResourceKey<Feature> LEGACY_GOLD_ORE = registerKey("legacy_gold_ore");
	public static final ResourceKey<Feature> LEGACY_REDSTONE_ORE = registerKey("legacy_redstone_ore");
	public static final ResourceKey<Feature> LEGACY_DIAMOND_ORE = registerKey("legacy_diamond_ore");
	public static final ResourceKey<Feature> LEGACY_LAPIS_ORE = registerKey("legacy_lapis_ore");
	public static final ResourceKey<Feature> LEGACY_COPPER_ORE = registerKey("legacy_copper_ore");

	//Dark Forest needs special placements, so here we go
	public static final ResourceKey<Feature> DARK_PUMPKINS = registerKey("dark_pumpkins");
	public static final ResourceKey<Feature> DARK_GRASS = registerKey("dark_grass");
	public static final ResourceKey<Feature> DARK_FERNS = registerKey("dark_ferns");
	public static final ResourceKey<Feature> DARK_MUSHGLOOMS = registerKey("dark_mushglooms");
	public static final ResourceKey<Feature> DARK_BROWN_MUSHROOMS = registerKey("dark_brown_mushrooms");
	public static final ResourceKey<Feature> DARK_RED_MUSHROOMS = registerKey("dark_red_mushrooms");
	public static final ResourceKey<Feature> DARK_DEAD_BUSHES = registerKey("dark_dead_bushes");

	//troll caves special stuff
	public static final ResourceKey<Feature> UBEROUS_SOIL_PATCH_BIG = registerKey("uberous_soil_patch_big");
	public static final ResourceKey<Feature> UBEROUS_SOIL_PATCH_SMALL = registerKey("uberous_soil_patch_small");
	public static final ResourceKey<Feature> TROLL_CAVE_MYCELIUM = registerKey("troll_cave_mycelium");
	public static final ResourceKey<Feature> TROLL_CAVE_DIRT = registerKey("troll_cave_dirt");

	//Trees!
	public static final ResourceKey<Feature> TWILIGHT_OAK_TREE = registerKey("tree/twilight_oak_tree");
	public static final ResourceKey<Feature> LARGE_TWILIGHT_OAK_TREE = registerKey("tree/large_twilight_oak_tree");
	public static final ResourceKey<Feature> SWAMPY_OAK_TREE = registerKey("tree/swampy_oak_tree");
	public static final ResourceKey<Feature> CANOPY_TREE = registerKey("tree/canopy_tree");
	public static final ResourceKey<Feature> MEGA_CANOPY_TREE = registerKey("tree/mega_canopy_tree");
	public static final ResourceKey<Feature> FIREFLY_CANOPY_TREE = registerKey("tree/firefly_canopy_tree");
	public static final ResourceKey<Feature> DEAD_CANOPY_TREE = registerKey("tree/dead_canopy_tree");
	public static final ResourceKey<Feature> MANGROVE_TREE = registerKey("tree/mangrove_tree");
	public static final ResourceKey<Feature> DARKWOOD_TREE = registerKey("tree/darkwood_tree");
	public static final ResourceKey<Feature> HOMEGROWN_DARKWOOD_TREE = registerKey("tree/homegrown_darkwood_tree");
	public static final ResourceKey<Feature> DARKWOOD_LANTERN_TREE = registerKey("tree/darkwood_lantern_tree");
	public static final ResourceKey<Feature> TIME_TREE = registerKey("tree/time_tree");
	public static final ResourceKey<Feature> TRANSFORMATION_TREE = registerKey("tree/transformation_tree");
	public static final ResourceKey<Feature> MINING_TREE = registerKey("tree/mining_tree");
	public static final ResourceKey<Feature> SORTING_TREE = registerKey("tree/sorting_tree");
	public static final ResourceKey<Feature> FOREST_MEGA_OAK_TREE = registerKey("tree/forest_mega_oak_tree");
	public static final ResourceKey<Feature> SAVANNAH_MEGA_OAK_TREE = registerKey("tree/savannah_mega_oak_tree");
	public static final ResourceKey<Feature> RAINBOW_OAK_TREE = registerKey("tree/rainbow_oak");
	public static final ResourceKey<Feature> LARGE_RAINBOW_OAK_TREE = registerKey("tree/large_rainbow_oak");
	public static final ResourceKey<Feature> BROWN_CANOPY_MUSHROOM_TREE = registerKey("mushroom/brown_canopy_mushroom");
	public static final ResourceKey<Feature> RED_CANOPY_MUSHROOM_TREE = registerKey("mushroom/red_canopy_mushroom");
	public static final ResourceKey<Feature> CANOPY_RED_VANILLA_MUSHROOM = registerKey("mushroom/canopy_red_vanilla_mushroom");
	public static final ResourceKey<Feature> CANOPY_RED_SMOOTH_MUSHROOM = registerKey("mushroom/canopy_red_smooth_mushroom");
	public static final ResourceKey<Feature> CANOPY_RED_SPHEROID_MUSHROOM = registerKey("mushroom/canopy_red_spheroid_mushroom");
	public static final ResourceKey<Feature> CANOPY_RED_FLAT_MUSHROOM = registerKey("mushroom/canopy_red_flat_mushroom");
	public static final ResourceKey<Feature> MEGA_SPRUCE_TREE = registerKey("tree/mega_spruce_tree");
	public static final ResourceKey<Feature> LARGE_WINTER_TREE = registerKey("tree/large_winter_tree");
	public static final ResourceKey<Feature> SNOWY_SPRUCE_TREE = registerKey("tree/snowy_spruce_tree");
	public static final ResourceKey<Feature> DARK_FOREST_OAK_TREE = registerKey("tree/dark_forest_oak_tree");
	public static final ResourceKey<Feature> DARK_FOREST_BIRCH_TREE = registerKey("tree/dark_forest_birch_tree");
	public static final ResourceKey<Feature> DARK_OAK_BUSH = registerKey("tree/dark_oak_bush");
	public static final ResourceKey<Feature> VANILLA_OAK_TREE = registerKey("tree/vanilla_oak_tree");
	public static final ResourceKey<Feature> VANILLA_BIRCH_TREE = registerKey("tree/vanilla_birch_tree");
	public static final ResourceKey<Feature> SMALLER_JUNGLE_TREE = registerKey("tree/smaller_jungle_tree");
	public static final ResourceKey<Feature> OAK_BUSH = registerKey("tree/oak_bush");
	public static final ResourceKey<Feature> DUMMY_TREE = registerKey("tree/dummy");

	//random selectors
	public static final ResourceKey<Feature> CANOPY_TREES = registerKey("tree/selector/canopy_trees");
	public static final ResourceKey<Feature> DENSE_CANOPY_TREES = registerKey("tree/selector/dense_canopy_trees");
	public static final ResourceKey<Feature> FIREFLY_FOREST_TREES = registerKey("tree/selector/firefly_forest_trees");
	public static final ResourceKey<Feature> DARK_FOREST_TREES = registerKey("tree/selector/dark_forest_trees");
	public static final ResourceKey<Feature> HIGHLANDS_TREES = registerKey("tree/selector/highlands_trees");
	public static final ResourceKey<Feature> ENCHANTED_FOREST_TREES = registerKey("tree/selector/enchanted_forest_trees");
	public static final ResourceKey<Feature> SNOWY_FOREST_TREES = registerKey("tree/selector/snowy_forest_trees");
	public static final ResourceKey<Feature> VANILLA_TF_TREES = registerKey("tree/selector/vanilla_trees");
	public static final ResourceKey<Feature> VANILLA_TF_BIG_MUSH = registerKey("tree/selector/vanilla/vanilla_mushrooms");

	public static final ResourceKey<Feature> WELL_PLACER = TFConfiguredFeatures.registerKey("well_placer");
	public static final ResourceKey<Feature> LAMPPOST_PLACER = TFConfiguredFeatures.registerKey("lamppost_placer");
	public static final ResourceKey<Feature> DEFAULT_FALLEN_LOGS = TFConfiguredFeatures.registerKey("default_fallen_logs");

	//super funky tree placement lists
	public static final ResourceKey<Feature> CANOPY_MUSHROOMS_SPARSE = registerKey("mushroom/canopy_mushrooms_sparse");
	public static final ResourceKey<Feature> CANOPY_MUSHROOMS_DENSE = registerKey("mushroom/canopy_mushrooms_dense");

	//ground decoration
	public static final BlockStateProvider SMALL_FLOWER_CONFIG = new NoiseProvider(2345L, NormalNoise.createParity(0, 1.0D), 0.020833334F, List.of(
			Blocks.POPPY.defaultBlockState(),
			Blocks.DANDELION.defaultBlockState(),
			Blocks.RED_TULIP.defaultBlockState(),
			Blocks.ORANGE_TULIP.defaultBlockState(),
			Blocks.PINK_TULIP.defaultBlockState(),
			Blocks.WHITE_TULIP.defaultBlockState(),
			Blocks.CORNFLOWER.defaultBlockState(),
			Blocks.LILY_OF_THE_VALLEY.defaultBlockState(),
			Blocks.BLUE_ORCHID.defaultBlockState(),
			Blocks.ALLIUM.defaultBlockState(),
			Blocks.AZURE_BLUET.defaultBlockState(),
			Blocks.OXEYE_DAISY.defaultBlockState())
		);

	public static final ResourceKey<Feature> FLOWER_PLACER = registerKey("flower_placer");

	public static final BlockStateProvider SMALL_FLOWER_CONFIG_ALT = new NoiseProvider(2345L, NormalNoise.createParity(0, 1.0D), 0.020833334F, List.of(
			Blocks.WHITE_TULIP.defaultBlockState(),
			Blocks.PINK_TULIP.defaultBlockState(),
			Blocks.ORANGE_TULIP.defaultBlockState(),
			Blocks.RED_TULIP.defaultBlockState(),
			Blocks.DANDELION.defaultBlockState(),
			Blocks.POPPY.defaultBlockState(),
			Blocks.OXEYE_DAISY.defaultBlockState(),
			Blocks.AZURE_BLUET.defaultBlockState(),
			Blocks.ALLIUM.defaultBlockState(),
			Blocks.BLUE_ORCHID.defaultBlockState(),
			Blocks.LILY_OF_THE_VALLEY.defaultBlockState(),
			Blocks.CORNFLOWER.defaultBlockState())
		);

	public static final ResourceKey<Feature> FLOWER_PLACER_ALT = registerKey("flower_placer_alt");

	public static ResourceKey<Feature> registerKey(String name) {
		return ResourceKey.create(Registries.FEATURE, TwilightForestMod.prefix(name));
	}

	public static void bootstrap(BootstrapContext<Feature> context) {
		HolderGetter<Feature> features = context.lookup(Registries.FEATURE);
		context.register(LAKE_LAVA,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(LAKE_WATER,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(LAKE_FROZEN,  new NoOpFeature() /* TODO-263: Feature fallback */);

		registerTemplateFeatures(context);

		context.register(BIG_MUSHGLOOM,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(DENSE_FERNS, new SimpleBlockFeature(BlockStateProvider.simple(Blocks.FERN)));
		context.register(DENSE_LARGE_FERNS, new SimpleBlockFeature(BlockStateProvider.simple(Blocks.LARGE_FERN)));
		context.register(FALLEN_LEAVES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(MAYAPPLE, new SimpleBlockFeature(BlockStateProvider.simple(TFBlocks.MAYAPPLE)));
		context.register(FIDDLEHEAD, new SimpleBlockFeature(BlockStateProvider.simple(TFBlocks.FIDDLEHEAD)));
		context.register(FIRE_JET,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(FOUNDATION,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(GROVE_RUINS, new NoOpFeature() /* TFFeatures.GROVE_RUINS */);
		context.register(HOLLOW_LOG, new NoOpFeature() /* TFFeatures.FALLEN_HOLLOW_LOG */);
		context.register(HOLLOW_STUMP, new NoOpFeature() /* TODO-263: TFFeatures.HOLLOW_STUMP */);
		context.register(HUGE_LILY_PAD, new NoOpFeature() /* TFFeatures.HUGE_LILY_PAD */);
		context.register(HUGE_WATER_LILY, new NoOpFeature() /* TFFeatures.HUGE_WATER_LILY */);
		context.register(CICADA_LAMPPOST,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(FIREFLY_LAMPPOST,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(MONOLITH, new NoOpFeature() /* TFFeatures.MONOLITH */);
		context.register(MUSHGLOOM_CLUSTER, new SimpleBlockFeature(BlockStateProvider.simple(TFBlocks.MUSHGLOOM)));
		context.register(MYCELIUM_BLOB,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(OUTSIDE_STALAGMITE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(PLANT_ROOTS,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(IRON_OREBERRIES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(GOLD_OREBERRIES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(COPPER_OREBERRIES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(ESSENCE_OREBERRIES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(PUMPKIN_LAMPPOST,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(RASPBERRY_BUSHES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(BLUEBERRY_BUSHES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(BLACKBERRY_BUSHES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(MALOBERRY_BUSHES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(SMOKER,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(STONE_CIRCLE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(THORNS,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(TORCH_BERRIES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(TROLL_ROOTS,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(TROLL_BIG_MUSHGLOOMS,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(TROLL_HUGE_RED_MUSHROOMS,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(TROLL_HUGE_BROWN_MUSHROOMS,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(TROLL_MUSHGLOOMS,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(VANILLA_ROOTS,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(WEBS,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(WOOD_ROOTS_SPREAD,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(SNOW_UNDER_TREES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(ENCHANTED_FOREST_VINES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(OAK_BUSH, TreeConfigurations.OAK_BUSH);

		context.register(TF_OAK_FALLEN_LOG,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(CANOPY_FALLEN_LOG,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(MANGROVE_FALLEN_LOG,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(OAK_FALLEN_LOG,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(SPRUCE_FALLEN_LOG,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(BIRCH_FALLEN_LOG,  new NoOpFeature() /* TODO-263: Feature fallback */);

		context.register(SMALL_GRANITE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(SMALL_DIORITE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(SMALL_ANDESITE,  new NoOpFeature() /* TODO-263: Feature fallback */);

		context.register(LEGACY_COAL_ORE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(LEGACY_IRON_ORE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(LEGACY_GOLD_ORE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(LEGACY_REDSTONE_ORE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(LEGACY_DIAMOND_ORE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(LEGACY_LAPIS_ORE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(LEGACY_COPPER_ORE,  new NoOpFeature() /* TODO-263: Feature fallback */);

		context.register(DARK_PUMPKINS, new SimpleBlockFeature(BlockStateProvider.simple(Blocks.PUMPKIN)));
		context.register(DARK_GRASS, new SimpleBlockFeature(BlockStateProvider.simple(Blocks.SHORT_GRASS))); // [VanillaCopy] Registration of PATCH_GRASS_JUNGLE in VegetationFeatures
		context.register(DARK_FERNS, new SimpleBlockFeature(BlockStateProvider.simple(Blocks.FERN)));
		context.register(DARK_MUSHGLOOMS, new SimpleBlockFeature(BlockStateProvider.simple(TFBlocks.MUSHGLOOM)));
		context.register(DARK_BROWN_MUSHROOMS, new SimpleBlockFeature(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM)));
		context.register(DARK_RED_MUSHROOMS, new SimpleBlockFeature(BlockStateProvider.simple(Blocks.RED_MUSHROOM)));
		context.register(DARK_DEAD_BUSHES, new SimpleBlockFeature(BlockStateProvider.simple(Blocks.DEAD_BUSH)));

		context.register(UBEROUS_SOIL_PATCH_BIG,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(UBEROUS_SOIL_PATCH_SMALL,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(TROLL_CAVE_MYCELIUM,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(TROLL_CAVE_DIRT,  new NoOpFeature() /* TODO-263: Feature fallback */);

		context.register(TWILIGHT_OAK_TREE, TreeConfigurations.TWILIGHT_OAK);
		context.register(LARGE_TWILIGHT_OAK_TREE, TreeConfigurations.LARGE_TWILIGHT_OAK);
		context.register(SWAMPY_OAK_TREE, TreeConfigurations.SWAMPY_OAK);
		context.register(CANOPY_TREE, TreeConfigurations.CANOPY_TREE);
		context.register(MEGA_CANOPY_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(FIREFLY_CANOPY_TREE, TreeConfigurations.CANOPY_TREE_FIREFLY);
		context.register(DEAD_CANOPY_TREE, TreeConfigurations.CANOPY_TREE_DEAD);
		context.register(MANGROVE_TREE, TreeConfigurations.MANGROVE_TREE);
		context.register(DARKWOOD_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(HOMEGROWN_DARKWOOD_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(DARKWOOD_LANTERN_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(TIME_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(TRANSFORMATION_TREE, TreeConfigurations.TRANSFORM_TREE);
		context.register(MINING_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(SORTING_TREE, TreeConfigurations.SORT_TREE);
		context.register(FOREST_MEGA_OAK_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(SAVANNAH_MEGA_OAK_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(RAINBOW_OAK_TREE, TreeConfigurations.RAINBOAK_TREE);
		context.register(LARGE_RAINBOW_OAK_TREE, TreeConfigurations.LARGE_RAINBOAK_TREE);
		context.register(BROWN_CANOPY_MUSHROOM_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);

		context.register(CANOPY_RED_VANILLA_MUSHROOM,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(CANOPY_RED_SMOOTH_MUSHROOM,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(CANOPY_RED_SPHEROID_MUSHROOM,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(CANOPY_RED_FLAT_MUSHROOM,  new NoOpFeature() /* TODO-263: Feature fallback */);

		context.register(RED_CANOPY_MUSHROOM_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);

		context.register(MEGA_SPRUCE_TREE, TreeConfigurations.BIG_SPRUCE);
		context.register(LARGE_WINTER_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(SNOWY_SPRUCE_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(DARK_FOREST_OAK_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(DARK_FOREST_BIRCH_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(DARK_OAK_BUSH,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(VANILLA_OAK_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(VANILLA_BIRCH_TREE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(SMALLER_JUNGLE_TREE, TreeConfigurations.SMALL_JUNGLE);
		context.register(DUMMY_TREE, new NoOpFeature());

		context.register(WELL_PLACER, new RandomSelectorFeature(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(FANCY_WELL)), 0.05F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(SIMPLE_WELL)), 0.0F)), PlacementUtils.inlinePlaced(features.getOrThrow(SIMPLE_WELL))));
		context.register(LAMPPOST_PLACER, new RandomSelectorFeature(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(CICADA_LAMPPOST)), 0.1F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(FIREFLY_LAMPPOST)), 0.0F)), PlacementUtils.inlinePlaced(features.getOrThrow(FIREFLY_LAMPPOST))));
		context.register(DEFAULT_FALLEN_LOGS,  new NoOpFeature() /* TODO-263: Feature fallback */);

		context.register(CANOPY_TREES, new RandomSelectorFeature(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(CANOPY_TREE)), 0.6F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TWILIGHT_OAK_TREE)), 0.0F)), PlacementUtils.inlinePlaced(features.getOrThrow(TWILIGHT_OAK_TREE))));
		context.register(DENSE_CANOPY_TREES, new RandomSelectorFeature(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(CANOPY_TREE)), 0.7F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TWILIGHT_OAK_TREE)), 0.0F)), PlacementUtils.inlinePlaced(features.getOrThrow(TWILIGHT_OAK_TREE))));
		context.register(FIREFLY_FOREST_TREES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(DARK_FOREST_TREES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(HIGHLANDS_TREES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(ENCHANTED_FOREST_TREES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(SNOWY_FOREST_TREES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(VANILLA_TF_TREES,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(VANILLA_TF_BIG_MUSH, new RandomBooleanSelectorFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM)), PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM))));

		context.register(CANOPY_MUSHROOMS_SPARSE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(CANOPY_MUSHROOMS_DENSE,  new NoOpFeature() /* TODO-263: Feature fallback */);
		context.register(FLOWER_PLACER, new SimpleBlockFeature(SMALL_FLOWER_CONFIG));
		context.register(FLOWER_PLACER_ALT, new SimpleBlockFeature(SMALL_FLOWER_CONFIG_ALT));
	}

	private static void registerTemplateFeatures(BootstrapContext<Feature> context) {
		HolderGetter<WoodPalette> paletteHolders = context.lookup(TFRegistries.Keys.WOOD_PALETTES);
		var paletteChoices = SwizzleConfig.buildRarityPalette(paletteHolders);

		ProcessorRule processorCobbleBlock = new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.defaultBlockState());
		ProcessorRule processorCobbleStair = new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE_STAIRS, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState());
		ProcessorRule processorCobbleSlab = new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE_SLAB, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState());
		ProcessorRule processorCobbleWall = new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE_WALL, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState());

		ProcessorRule processorStoneBrickBlock = new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICKS.defaultBlockState());
		ProcessorRule processorStoneBrickStair = new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICK_STAIRS, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState());
		ProcessorRule processorStoneBrickSlab = new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICK_SLAB, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState());
		ProcessorRule processorStoneBrickWall = new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICK_WALL, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICK_WALL.defaultBlockState());

		SwizzleConfig simpleWellConfig = SwizzleConfig.generate(paletteHolders, TFWoodPaletteTags.WELL_SWIZZLE_MASK, paletteChoices, processorCobbleBlock, processorCobbleStair, processorCobbleSlab, processorCobbleWall);
		context.register(SIMPLE_WELL,  new NoOpFeature() /* TODO-263: Feature fallback */);

		SwizzleConfig fancyWellConfig = SwizzleConfig.generate(paletteHolders, TFWoodPaletteTags.WELL_SWIZZLE_MASK, paletteChoices, processorCobbleBlock, processorCobbleStair, processorCobbleSlab, processorCobbleWall, processorStoneBrickBlock, processorStoneBrickStair, processorStoneBrickSlab, processorStoneBrickWall);
		context.register(FANCY_WELL,  new NoOpFeature() /* TODO-263: Feature fallback */);

		SwizzleConfig hutConfig = SwizzleConfig.generate(paletteHolders, TFWoodPaletteTags.DRUID_HUT_SWIZZLE_MASK, paletteChoices, processorCobbleBlock, processorCobbleStair, processorCobbleSlab, processorCobbleWall, processorStoneBrickBlock, processorStoneBrickStair, processorStoneBrickSlab, processorStoneBrickWall);
		context.register(DRUID_HUT,  new NoOpFeature() /* TODO-263: Feature fallback */);

		context.register(GRAVEYARD,  new NoOpFeature() /* TODO-263: Feature fallback */);
	}
}