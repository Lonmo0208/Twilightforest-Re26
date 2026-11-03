package twilightforest.world.components.feature.trees.growers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.init.TFConfiguredFeatures;

import java.util.List;

public class TFTreeGrowers {

	public static final TreeGrower TWILIGHT_OAK = new TreeGrower(
		WeightedList.of(TFConfiguredFeatures.LARGE_TWILIGHT_OAK_TREE),
		WeightedList.of(
			new net.minecraft.util.random.Weighted<>(TFConfiguredFeatures.FOREST_MEGA_OAK_TREE, 1),
			new net.minecraft.util.random.Weighted<>(TFConfiguredFeatures.SAVANNAH_MEGA_OAK_TREE, 1)),
		WeightedList.of(TFConfiguredFeatures.TWILIGHT_OAK_TREE),
		TFConfiguredFeatures.LARGE_TWILIGHT_OAK_TREE);

	public static final TreeGrower CANOPY = new TreeGrower(
		WeightedList.of(TFConfiguredFeatures.CANOPY_TREE),
		WeightedList.of(TFConfiguredFeatures.MEGA_CANOPY_TREE),
		WeightedList.of(),
		TFConfiguredFeatures.CANOPY_TREE);

	public static final TreeGrower MANGROVE = new TreeGrower(
		WeightedList.of(TFConfiguredFeatures.MANGROVE_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFConfiguredFeatures.MANGROVE_TREE);

	public static final TreeGrower DARK = new TreeGrower(
		WeightedList.of(TFConfiguredFeatures.HOMEGROWN_DARKWOOD_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFConfiguredFeatures.HOMEGROWN_DARKWOOD_TREE);

	public static final TreeGrower TIME = new TreeGrower(
		WeightedList.of(TFConfiguredFeatures.TIME_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFConfiguredFeatures.TIME_TREE);

	public static final TreeGrower TRANSFORMATION = new TreeGrower(
		WeightedList.of(TFConfiguredFeatures.TRANSFORMATION_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFConfiguredFeatures.TRANSFORMATION_TREE);

	public static final TreeGrower MINING = new TreeGrower(
		WeightedList.of(TFConfiguredFeatures.MINING_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFConfiguredFeatures.MINING_TREE);

	public static final TreeGrower SORTING = new TreeGrower(
		WeightedList.of(TFConfiguredFeatures.SORTING_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFConfiguredFeatures.SORTING_TREE);

	public static final TreeGrower HOLLOW_OAK = new TreeGrower(
		WeightedList.of(TFConfiguredFeatures.TWILIGHT_OAK_TREE),
		WeightedList.of(),
		WeightedList.of(),
		TFConfiguredFeatures.TWILIGHT_OAK_TREE);

	public static final TreeGrower RAINBOW_OAK = new TreeGrower(
		WeightedList.of(TFConfiguredFeatures.LARGE_RAINBOW_OAK_TREE),
		WeightedList.of(),
		WeightedList.of(TFConfiguredFeatures.RAINBOW_OAK_TREE),
		TFConfiguredFeatures.LARGE_RAINBOW_OAK_TREE);
}
