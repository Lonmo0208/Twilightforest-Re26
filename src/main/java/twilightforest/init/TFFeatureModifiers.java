package twilightforest.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.trees.treeplacers.*;
import twilightforest.world.components.placements.AvoidLandmarkModifier;
import twilightforest.world.components.placements.ChunkBlanketingModifier;
import twilightforest.world.components.placements.ChunkCenterModifier;
import net.minecraft.core.Registry;

public final class TFFeatureModifiers {

	public static final TrunkPlacerType<BranchingTrunkPlacer> TRUNK_BRANCHING = new TrunkPlacerType<>(BranchingTrunkPlacer.CODEC);
	public static final TrunkPlacerType<TrunkRiser> TRUNK_RISER = new TrunkPlacerType<>(TrunkRiser.CODEC);

	public static final FoliagePlacerType<LeafSpheroidFoliagePlacer> FOLIAGE_SPHEROID = new FoliagePlacerType<>(LeafSpheroidFoliagePlacer.CODEC);

	public static final TreeDecoratorType<TreeCorePlacer> CORE_PLACER = new TreeDecoratorType<>(TreeCorePlacer.CODEC);
	public static final TreeDecoratorType<TrunkSideDecorator> TRUNKSIDE_DECORATOR = new TreeDecoratorType<>(TrunkSideDecorator.CODEC);
	public static final TreeDecoratorType<TreeRootsDecorator> TREE_ROOTS = new TreeDecoratorType<>(TreeRootsDecorator.CODEC);
	public static final TreeDecoratorType<DangleFromTreeDecorator> DANGLING_DECORATOR = new TreeDecoratorType<>(DangleFromTreeDecorator.CODEC);

	public static final PlacementModifierType<AvoidLandmarkModifier> NO_STRUCTURE = () -> AvoidLandmarkModifier.CODEC;
	public static final PlacementModifierType<ChunkCenterModifier> CHUNK_CENTERER = () -> ChunkCenterModifier.CODEC;
	public static final PlacementModifierType<ChunkBlanketingModifier> CHUNK_BLANKETING = () -> ChunkBlanketingModifier.CODEC;

	public static void init() {
		Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, TwilightForestMod.prefix("branching_trunk_placer"), TRUNK_BRANCHING);
		Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, TwilightForestMod.prefix("trunk_mover_upper"), TRUNK_RISER);

		Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, TwilightForestMod.prefix("spheroid_foliage_placer"), FOLIAGE_SPHEROID);

		Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, TwilightForestMod.prefix("core_placer"), CORE_PLACER);
		Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, TwilightForestMod.prefix("trunkside_decorator"), TRUNKSIDE_DECORATOR);
		Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, TwilightForestMod.prefix("tree_roots"), TREE_ROOTS);
		Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, TwilightForestMod.prefix("dangle_from_tree_decorator"), DANGLING_DECORATOR);

		Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, TwilightForestMod.prefix("no_structure"), NO_STRUCTURE);
		Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, TwilightForestMod.prefix("chunk_centerer"), CHUNK_CENTERER);
		Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, TwilightForestMod.prefix("chunk_blanketing"), CHUNK_BLANKETING);
	}
}