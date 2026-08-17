package twilightforest.world.components.feature.trees;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import twilightforest.util.RootPlacer;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class TFTreeFeature implements Feature {
	protected TFTreeFeatureConfig config;

	protected TFTreeFeature() {
	}

	protected TFTreeFeature(TFTreeFeatureConfig config) {
		this.config = config;
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return TFTreeFeatureConfig.MAP_CODEC.xmap(this::create, f -> f.config);
	}

	// factory used by the codec to reconstruct the concrete subclass from a decoded config
	protected abstract TFTreeFeature create(TFTreeFeatureConfig config);

	// [VanillaCopy] TreeFeature.place, swapped TreeFeature for generic <T extends TFTreeFeatureConfig>. Omitted code are commented out instead of deleted
	@Override
	public final boolean place(WorldGenLevel worldgenlevel, ChunkGenerator chunkGenerator, RandomSource randomsource, BlockPos blockpos) {
		// 26.3: TFTreeFeatureConfig stored as instance field (replaces FeaturePlaceContext.config())
		TFTreeFeatureConfig treeconfiguration = this.config;
		Set<BlockPos> set = Sets.newHashSet();
		Set<BlockPos> set1 = Sets.newHashSet();
		Set<BlockPos> set2 = Sets.newHashSet();
		Set<BlockPos> set3 = Sets.newHashSet();
		BiConsumer<BlockPos, BlockState> biconsumer = (pos, state) -> {
			set.add(pos.immutable());
			worldgenlevel.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		BiConsumer<BlockPos, BlockState> biconsumer1 = (pos, state) -> {
			set1.add(pos.immutable());
			worldgenlevel.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		BiConsumer<BlockPos, BlockState> biconsumer2 = (pos, state) -> {
			set2.add(pos.immutable());
			worldgenlevel.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		BiConsumer<BlockPos, BlockState> biconsumer3 = (pos, state) -> {
			set3.add(pos.immutable());
			worldgenlevel.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		boolean flag = this.generate(worldgenlevel, randomsource, blockpos, biconsumer, biconsumer1, new RootPlacer(biconsumer2, 1), treeconfiguration);
		if (flag && (!set1.isEmpty() || !set2.isEmpty())) {
			if (treeconfiguration != null && !treeconfiguration.decorators.isEmpty()) {
				TreeDecorator.Context treedecorator$context = new TreeDecorator.Context(worldgenlevel, biconsumer3, randomsource, set1, set2, set);
				treeconfiguration.decorators.forEach((p_225282_) -> {
					p_225282_.place(treedecorator$context);
				});
			}

			return BoundingBox.encapsulatingPositions(Iterables.concat(set, set1, set2, set3)).map((boundingBox) -> {
				DiscreteVoxelShape discretevoxelshape = TreeFeature.updateLeaves(worldgenlevel, boundingBox, set1, set3, set);
				StructureTemplate.updateShapeAtEdge(worldgenlevel, 3, discretevoxelshape, boundingBox.minX(), boundingBox.minY(), boundingBox.minZ());
				return true;
			}).orElse(false);
		} else {
			return false;
		}
	}

	/**
	 * This works akin to the AbstractTreeFeature.generate, but put our branches and roots here
	 */
	protected abstract boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer, TFTreeFeatureConfig config);

}