package twilightforest.world.components.feature.trees;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import twilightforest.util.features.FeaturePlacers;

import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;

public class DarkCanopyTreeFeature implements Feature {

	private final net.minecraft.world.level.levelgen.feature.TreeFeature config;

	public DarkCanopyTreeFeature() {
		this.config = null;
	}

	public DarkCanopyTreeFeature(net.minecraft.world.level.levelgen.feature.TreeFeature config) {
		this.config = config;
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return net.minecraft.world.level.levelgen.feature.TreeFeature.CODEC.xmap(DarkCanopyTreeFeature::new, f -> f.config);
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
		net.minecraft.world.level.levelgen.feature.TreeFeature treeconfiguration = this.config;

		final int x = pos.getX();
		final int z = pos.getZ();
		int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);

		int soilTop = -1;
		final int maxTerrainY = 16;
		int searchStart = Math.min(Math.max(surfaceY, 8), maxTerrainY);
		for (int y = searchStart; y >= level.getMinY() + 2; y--) {
			BlockPos probe = new BlockPos(x, y, z);
			BlockState atState = level.getBlockState(probe);
			BlockState aboveState = level.getBlockState(probe.above());
			boolean atIsSoil = atState.is(BlockTags.DIRT)
					|| atState.is(Blocks.GRASS_BLOCK)
					|| atState.is(Blocks.PODZOL)
					|| atState.is(Blocks.MYCELIUM)
					|| atState.is(Blocks.COARSE_DIRT);
			boolean aboveIsAir = aboveState.isAir()
					|| aboveState.is(BlockTags.REPLACEABLE)
					|| aboveState.is(BlockTags.REPLACEABLE_BY_TREES);
			if (atIsSoil && aboveIsAir) {
				soilTop = y;
				break;
			}
		}

		if (soilTop < level.getMinY() + 1) {
			return false;
		}

		pos = new BlockPos(x, soilTop, z);

		if (treeconfiguration == null) {
			return false;
		}

		int treeHeight;
		try {
			treeHeight = treeconfiguration.trunkPlacer().getTreeHeight(random);
		} catch (Throwable t) {
			treeHeight = 20;
		}
		final boolean isSmallTree = treeHeight <= 14;

		if (!isSmallTree) {
			for (int i = 1; i <= 4; i++) {
				if (!validTreePos(level, pos.relative(Direction.UP, i))) {
					return false;
				}
			}

			for (Direction e : Direction.Plane.HORIZONTAL) {
				if (level.getBlockState(pos.relative(e)).is(BlockTags.LOGS)) {
					return false;
				}
			}
		}

		Set<BlockPos> set = Sets.newHashSet();
		Set<BlockPos> set1 = Sets.newHashSet();
		Set<BlockPos> set2 = Sets.newHashSet();
		Set<BlockPos> set3 = Sets.newHashSet();
		BiConsumer<BlockPos, BlockState> biconsumer = (p_160555_, p_160556_) -> {
			set.add(p_160555_.immutable());
			level.setBlock(p_160555_, p_160556_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		BiConsumer<BlockPos, BlockState> biconsumer1 = (p_160548_, p_160549_) -> {
			set1.add(p_160548_.immutable());
			level.setBlock(p_160548_, p_160549_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		FoliagePlacer.FoliageSetter setter = new FoliagePlacer.FoliageSetter() {
			@Override
			public void set(BlockPos pos, BlockState state) {
				set2.add(pos.immutable());
				level.setBlock(pos, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			}

			@Override
			public boolean isSet(BlockPos p_272999_) {
				return set2.contains(p_272999_);
			}
		};
		BiConsumer<BlockPos, BlockState> biconsumer3 = (p_225290_, p_225291_) -> {
			set3.add(p_225290_.immutable());
			level.setBlock(p_225290_, p_225291_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		};
		boolean flag = this.doPlace(level, random, pos, biconsumer, biconsumer1, setter, treeconfiguration);

		if (flag) {
			if (set1.isEmpty() && set2.isEmpty()) {
				return false;
			}
			if (!treeconfiguration.decorators().isEmpty()) {
				TreeDecorator.Context treedecorator$context = new TreeDecorator.Context(level, biconsumer3, random, set1, set2, set);
				treeconfiguration.decorators().forEach((p_225282_) -> {
					p_225282_.place(treedecorator$context);
				});
			}

			return BoundingBox.encapsulatingPositions(Iterables.concat(set1, set2, set3)).map((p_160521_) -> {
				DiscreteVoxelShape shape = updateLeaves(level, p_160521_, set1, set3);
				StructureTemplate.updateShapeAtEdge(level, 3, shape, p_160521_.minX(), p_160521_.minY(), p_160521_.minZ());
				return true;
			}).orElse(false);
		}
		return false;
	}

	private boolean doPlace(WorldGenLevel level, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> consumer, BiConsumer<BlockPos, BlockState> consumer1, FoliagePlacer.FoliageSetter foliageSetter, net.minecraft.world.level.levelgen.feature.TreeFeature config) {
		int i = config.trunkPlacer().getTreeHeight(random);
		int j = config.foliagePlacer().foliageHeight(random, i, config);
		int k = i - j;
		int l = config.foliagePlacer().foliageRadius(random, k);
		BlockPos rawTrunkOrigin = config.rootPlacer().map((placer) -> placer.getTrunkOrigin(pos, random)).orElse(pos);
		BlockPos blockpos = rawTrunkOrigin.above();
		int i1 = Math.min(pos.getY(), blockpos.getY());
		int j1 = Math.max(pos.getY(), blockpos.getY()) + i + 1;
		if (i1 >= level.getMinY() + 1 && j1 <= level.getMaxY() + 1) {
			OptionalInt optionalint = config.minimumSize().minClippedHeight();
			int k1 = Math.max(i, optionalint.orElse(0));
			if (config.rootPlacer().isPresent() && !config.rootPlacer().get().placeRoots(level, consumer, random, pos, rawTrunkOrigin, config)) {
				return false;
			} else {
				List<FoliagePlacer.FoliageAttachment> list = config.trunkPlacer().placeTrunk(level, consumer1, random, k1, blockpos, config);
				list.forEach((attachment) -> {
					config.foliagePlacer().createFoliage(level, foliageSetter, random, config, k1, attachment, j, l);
				});
				return true;
			}
		}
		return false;
	}

	private int getMaxFreeTreeHeight(WorldGenLevel level, int trunkHeight, BlockPos pos, net.minecraft.world.level.levelgen.feature.TreeFeature config) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int i = 0; i <= trunkHeight + 1; ++i) {
			int j = config.minimumSize().getSizeAtHeight(trunkHeight, i);
			for (int k = -j; k <= j; ++k) {
				for (int l = -j; l <= j; ++l) {
					mutable.setWithOffset(pos, k, i, l);
					if (!FeaturePlacers.validTreePos(level, mutable) || (!config.ignoreVines() && isVine(level, mutable))) {
						return i - 2;
					}
				}
			}
		}
		return trunkHeight;
	}

	public static boolean isVine(LevelSimulatedReader level, BlockPos pos) {
		return level.isStateAtPosition(pos, state -> state.is(Blocks.VINE));
	}

	public static void setBlockKnownShape(LevelWriter p_236408_0_, BlockPos p_236408_1_, BlockState p_236408_2_) {
		p_236408_0_.setBlock(p_236408_1_, p_236408_2_, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
	}

	private static DiscreteVoxelShape updateLeaves(LevelAccessor p_67203_, BoundingBox p_67204_, Set<BlockPos> p_67205_, Set<BlockPos> p_67206_) {
		List<Set<BlockPos>> list = Lists.newArrayList();
		DiscreteVoxelShape discretevoxelshape = new BitSetDiscreteVoxelShape(p_67204_.getXSpan(), p_67204_.getYSpan(), p_67204_.getZSpan());
		int i = 6;
		for (int j = 0; j < 6; ++j) {
			list.add(Sets.newHashSet());
		}
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		for (BlockPos blockpos : Lists.newArrayList(p_67206_)) {
			if (p_67204_.isInside(blockpos)) {
				discretevoxelshape.fill(blockpos.getX() - p_67204_.minX(), blockpos.getY() - p_67204_.minY(), blockpos.getZ() - p_67204_.minZ());
			}
		}
		for (BlockPos blockpos1 : Lists.newArrayList(p_67205_)) {
			if (p_67204_.isInside(blockpos1)) {
				discretevoxelshape.fill(blockpos1.getX() - p_67204_.minX(), blockpos1.getY() - p_67204_.minY(), blockpos1.getZ() - p_67204_.minZ());
			}
			for (Direction direction : Direction.values()) {
				blockpos$mutableblockpos.setWithOffset(blockpos1, direction);
				if (!p_67205_.contains(blockpos$mutableblockpos)) {
					BlockState blockstate = p_67203_.getBlockState(blockpos$mutableblockpos);
					if (blockstate.hasProperty(BlockStateProperties.DISTANCE)) {
						list.get(0).add(blockpos$mutableblockpos.immutable());
						setBlockKnownShape(p_67203_, blockpos$mutableblockpos, blockstate.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(1)));
						if (p_67204_.isInside(blockpos$mutableblockpos)) {
							discretevoxelshape.fill(blockpos$mutableblockpos.getX() - p_67204_.minX(), blockpos$mutableblockpos.getY() - p_67204_.minY(), blockpos$mutableblockpos.getZ() - p_67204_.minZ());
						}
					}
				}
			}
		}
		for (int l = 1; l < 6; ++l) {
			Set<BlockPos> set = list.get(l - 1);
			Set<BlockPos> set1 = list.get(l);
			for (BlockPos blockpos2 : set) {
				if (p_67204_.isInside(blockpos2)) {
					discretevoxelshape.fill(blockpos2.getX() - p_67204_.minX(), blockpos2.getY() - p_67204_.minY(), blockpos2.getZ() - p_67204_.minZ());
				}
				for (Direction direction1 : Direction.values()) {
					blockpos$mutableblockpos.setWithOffset(blockpos2, direction1);
					if (!set.contains(blockpos$mutableblockpos) && !set1.contains(blockpos$mutableblockpos)) {
						BlockState blockstate1 = p_67203_.getBlockState(blockpos$mutableblockpos);
						if (blockstate1.hasProperty(BlockStateProperties.DISTANCE)) {
							int k = blockstate1.getValue(BlockStateProperties.DISTANCE);
							if (k > l + 1) {
								BlockState blockstate2 = blockstate1.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(l + 1));
								setBlockKnownShape(p_67203_, blockpos$mutableblockpos, blockstate2);
								if (p_67204_.isInside(blockpos$mutableblockpos)) {
									discretevoxelshape.fill(blockpos$mutableblockpos.getX() - p_67204_.minX(), blockpos$mutableblockpos.getY() - p_67204_.minY(), blockpos$mutableblockpos.getZ() - p_67204_.minZ());
								}
								set1.add(blockpos$mutableblockpos.immutable());
							}
						}
					}
				}
			}
		}
		return discretevoxelshape;
	}

	public static boolean validTreePos(LevelSimulatedReader reader, BlockPos pos) {
		return reader.isStateAtPosition(pos, (state) -> state.isAir() || state.is(BlockTags.REPLACEABLE_BY_TREES));
	}

	private static boolean isPlantable(BlockState state) {
		return state.is(Blocks.GRASS_BLOCK) ||
				state.is(Blocks.DIRT) ||
				state.is(Blocks.COARSE_DIRT) ||
				state.is(Blocks.PODZOL) ||
				state.is(Blocks.MYCELIUM) ||
				state.is(Blocks.SAND) ||
				state.is(Blocks.RED_SAND);
	}
}
