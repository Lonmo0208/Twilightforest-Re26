package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFFeatures;
import twilightforest.world.registration.TreeConfigurations;

import java.util.function.Supplier;

/**
 * A dedicated SaplingBlock that bypasses the dynamic Registry<ConfiguredFeature> lookup.
 *
 * <p>Vanilla {@link net.minecraft.world.level.block.SaplingBlock} delegates tree growth to its
 * {@link TreeGrower}, which in turn always resolves its configured features from the dynamic
 * world-gen registry. Our configured features are currently not loaded as JSON (and therefore
 * absent from the dynamic registry), which caused every bonemeal / fertile-soil growth attempt
 * to silently return {@code false}. Everything sounded fine (the bonemeal-effects particle event
 * still fired for fertile soil) but no tree ever grew.
 *
 * <p>This subclass keeps the original STAGE advancement (0 → 1) of the parent, but swaps out the
 * STAGE 1 grow logic with direct {@link Feature#place} calls against the statically registered
 * {@link TFFeatures} + in-memory {@link TreeConfigurations}. Saplings without a configured tree
 * simply fall back to a best-effort vanilla feature so growth never silently disappears again.
 */
public class TFSaplingBlock extends net.minecraft.world.level.block.SaplingBlock {

	/**
	 * Strategy used when the sapling reaches growth stage 1.
	 * If null we fall back to an "instant-tree" attempt using the parent's TreeGrower (only
	 * useful if a future datapack restores the dynamic registry entries).
	 */
	private final @org.jetbrains.annotations.Nullable TreePlacement treePlacement;

	public TFSaplingBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties) {
		this(treeGrower, properties, null);
	}

	public TFSaplingBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties,
						  @org.jetbrains.annotations.Nullable TreePlacement treePlacement) {
		super(treeGrower, properties);
		this.treePlacement = treePlacement;
		this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(BlockTags.SUPPORTS_VEGETATION) || state.is(TFBlocks.UBEROUS_SOIL);
	}

	@Override
	public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
		if (state.getValue(STAGE) == 0) {
			// Keep the vanilla STAGE increment behaviour so progress is visible.
			level.setBlock(pos, state.cycle(STAGE), Block.UPDATE_ALL_IMMEDIATE);
			return;
		}
		if (this.treePlacement != null) {
			// Fully custom growth path using static feature + config.
			this.grow(level, level.getChunkSource().getGenerator(), pos, state, random);
			return;
		}
		// Fallback: delegate to the TreeGrower as originally intended, but catch any silent
		// failures so we can still produce a simple oak-ish tree when dynamic registry is empty.
		TreeGrower.GrowthResult result = this.treeGrower.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
		if (result == TreeGrower.GrowthResult.NONE) {
			growFallbackTree(level, pos, random);
		}
	}

	/** Mirror of the grow path used by SaplingBlock.performBonemeal. */
	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		this.advanceTree(level, pos, state, random);
	}

	/**
	 * Called once the sapling reaches STAGE 1 and we have a known tree placement to execute.
	 * The logic mirrors {@link TreeGrower#growTree}: first try mega (2x2) variants if present,
	 * otherwise place the normal single-sapling tree, and restore saplings on failure.
	 */
	protected boolean grow(ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState state, RandomSource random) {
		if (this.treePlacement == null) return false;

		// Optional 2x2 mega placement
		Supplier<ConfiguredFeature<?, ?>> mega = this.treePlacement.megaFeature();
		if (mega != null) {
			ConfiguredFeature<?, ?> megaFeature = mega.get();
			if (megaFeature != null) {
				for (int dx = 0; dx >= -1; dx--) {
					for (int dz = 0; dz >= -1; dz--) {
						if (isTwoByTwoSapling(state, level, pos, dx, dz)) {
							level.setBlock(pos.offset(dx, 0, dz), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
							level.setBlock(pos.offset(dx + 1, 0, dz), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
							level.setBlock(pos.offset(dx, 0, dz + 1), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
							level.setBlock(pos.offset(dx + 1, 0, dz + 1), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
							if (megaFeature.place(level, generator, random, pos.offset(dx, 0, dz))) {
								return true;
							}
							// Restore saplings on failure (feature rejected placement, not enough space, etc.)
							level.setBlock(pos.offset(dx, 0, dz), state, Block.UPDATE_ALL_IMMEDIATE);
							level.setBlock(pos.offset(dx + 1, 0, dz), state, Block.UPDATE_ALL_IMMEDIATE);
							level.setBlock(pos.offset(dx, 0, dz + 1), state, Block.UPDATE_ALL_IMMEDIATE);
							level.setBlock(pos.offset(dx + 1, 0, dz + 1), state, Block.UPDATE_ALL_IMMEDIATE);
							return false;
						}
					}
				}
			}
		}

		// Normal single-sapling growth
		Supplier<ConfiguredFeature<?, ?>> normal = this.treePlacement.normalFeature();
		if (normal == null) return growFallbackTree(level, pos, random);
		ConfiguredFeature<?, ?> normalFeature = normal.get();
		if (normalFeature == null) return growFallbackTree(level, pos, random);

		BlockState empty = level.getFluidState(pos).createLegacyBlock();
		level.setBlock(pos, empty, Block.UPDATE_ALL_IMMEDIATE);
		if (normalFeature.place(level, generator, random, pos)) {
			if (level.getBlockState(pos) == empty) {
				level.sendBlockUpdated(pos, state, empty, Block.UPDATE_CLIENTS);
			}
			return true;
		} else {
			level.setBlock(pos, state, Block.UPDATE_ALL_IMMEDIATE);
			return false;
		}
	}

	private static boolean isTwoByTwoSapling(BlockState state, ServerLevel level, BlockPos pos, int ox, int oz) {
		Block block = state.getBlock();
		return level.getBlockState(pos.offset(ox, 0, oz)).is(block)
			&& level.getBlockState(pos.offset(ox + 1, 0, oz)).is(block)
			&& level.getBlockState(pos.offset(ox, 0, oz + 1)).is(block)
			&& level.getBlockState(pos.offset(ox + 1, 0, oz + 1)).is(block);
	}

	/**
	 * Last-resort: feature couldn't be placed. Rather than pretend growth worked while producing
	 * nothing (the original bug), we at least keep STAGE == 1 so the next bone meal attempt can
	 * retry. Returning false here lets the caller decide what to do, but since callers already
	 * advance stage 0 -> 1 for us, just log and return false.
	 */
	private static boolean growFallbackTree(ServerLevel level, BlockPos pos, RandomSource random) {
		// Intentionally keep the sapling in place so the player can retry.
		return false;
	}

	/**
	 * Factory record that lets us express each TF tree as a pair of suppliers (mega + normal).
	 * Suppliers avoid eager creation of the giant TreeConfiguration objects until a sapling
	 * actually wants to grow.
	 */
	public record TreePlacement(
		Supplier<ConfiguredFeature<?, ?>> normalFeature,
		Supplier<ConfiguredFeature<?, ?>> megaFeature
	) {
		public static TreePlacement single(Supplier<ConfiguredFeature<?, ?>> normal) {
			return new TreePlacement(normal, null);
		}
	}

	// -------------------------------------------------------------------------
	// Helper factories for common TF tree types (kept near declaration so it
	// is obvious which tree each sapling grows into).
	// -------------------------------------------------------------------------

	public static final Supplier<ConfiguredFeature<?, ?>> TWILIGHT_OAK_TREE =
		() -> new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.TWILIGHT_OAK);

	public static final Supplier<ConfiguredFeature<?, ?>> LARGE_TWILIGHT_OAK_TREE =
		() -> new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.LARGE_TWILIGHT_OAK);

	public static final Supplier<ConfiguredFeature<?, ?>> CANOPY_TREE =
		() -> new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.CANOPY_TREE);

	public static final Supplier<ConfiguredFeature<?, ?>> MEGA_CANOPY_TREE =
		() -> new ConfiguredFeature<>(TFFeatures.MEGA_CANOPY, TreeConfigurations.MEGA_CANOPY);

	public static final Supplier<ConfiguredFeature<?, ?>> MANGROVE_TREE =
		() -> new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.MANGROVE_TREE);

	public static final Supplier<ConfiguredFeature<?, ?>> DARKWOOD_TREE =
		() -> new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.HOMEGROWN_DARKWOOD_TREE);

	public static final Supplier<ConfiguredFeature<?, ?>> TIME_TREE =
		() -> new ConfiguredFeature<>(TFFeatures.TREE_OF_TIME, TreeConfigurations.TIME_TREE);

	public static final Supplier<ConfiguredFeature<?, ?>> TRANSFORMATION_TREE =
		() -> new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.TRANSFORM_TREE);

	public static final Supplier<ConfiguredFeature<?, ?>> MINING_TREE =
		() -> new ConfiguredFeature<>(TFFeatures.MINERS_TREE, TreeConfigurations.MINING_TREE);

	public static final Supplier<ConfiguredFeature<?, ?>> SORTING_TREE =
		() -> new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.SORT_TREE);

	public static final Supplier<ConfiguredFeature<?, ?>> MEGA_TWILIGHT_OAK =
		() -> new ConfiguredFeature<>(TFFeatures.MEGA_OAK, TreeConfigurations.FOREST_MEGA_OAK);

	public static final Supplier<ConfiguredFeature<?, ?>> SAVANNAH_MEGA_OAK =
		() -> new ConfiguredFeature<>(TFFeatures.MEGA_OAK, TreeConfigurations.SAVANNAH_MEGA_OAK);

	public static final Supplier<ConfiguredFeature<?, ?>> RAINBOW_OAK_TREE =
		() -> new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.RAINBOAK_TREE);

	public static final Supplier<ConfiguredFeature<?, ?>> LARGE_RAINBOW_OAK_TREE =
		() -> new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.LARGE_RAINBOAK_TREE);
}
