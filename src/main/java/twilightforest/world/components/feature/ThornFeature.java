package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.feature.config.ThornsConfig;

public class ThornFeature implements Feature {
	private final ThornsConfig config;

	public ThornFeature() {
		this(new ThornsConfig(7, 3, 3, 50));
	}

	public ThornFeature(ThornsConfig config) {
		this.config = config;
	}

	@Override
	public com.mojang.serialization.MapCodec<? extends net.minecraft.world.level.levelgen.feature.Feature> codec() {
		return ThornsConfig.MAP_CODEC.xmap(ThornFeature::new, f -> f.config);
	}

	@Override
	public boolean place(net.minecraft.world.level.WorldGenLevel level, net.minecraft.world.level.chunk.ChunkGenerator chunkGenerator, net.minecraft.util.RandomSource random, net.minecraft.core.BlockPos pos) {
		ThornsConfig config = this.config;
		WorldGenLevel world = level;
		RandomSource rand = random;

		int nextLength = 2 + rand.nextInt(4);
		int maxLength = 2 + rand.nextInt(4) + rand.nextInt(4) + rand.nextInt(4);

		int placed = placeThorns(world, rand, pos, nextLength, Direction.UP, maxLength, pos, config, true);

		return true;
	}

	private int placeThorns(WorldGenLevel world, RandomSource rand, BlockPos pos, int length, Direction dir, int maxLength, BlockPos oPos, ThornsConfig config, boolean avoidGiantCloud) {
		int placed = 0;
		boolean complete = false;
		for (int i = 0; i < length; i++) {
			BlockPos dPos = pos.relative(dir, i);

			if (!avoidGiantCloud || checkIsUnderCloud(world, pos, dPos)) {
				if (Math.abs(dPos.getX() - oPos.getX()) < config.maxSpread() && Math.abs(dPos.getZ() - oPos.getZ()) < config.maxSpread() && canPlaceThorns(world, dPos)) {
					world.setBlock(dPos, TFBlocks.BROWN_THORNS.defaultBlockState().setValue(RotatedPillarBlock.AXIS, dir.getAxis()), Block.UPDATE_CLIENTS);
					world.getChunk(dPos).markPosForPostProcessing(dPos);
					placed++;

					if (i == length - 1) {
						complete = true;
						if (rand.nextInt(config.chanceOfLeaf()) == 0 && world.isEmptyBlock(dPos.relative(dir))) {
							if (rand.nextInt(config.chanceLeafIsRose()) > 0) {
								world.setBlock(dPos.relative(dir), TFBlocks.THORN_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, 1), Block.UPDATE_ALL);
								placed++;
							} else {
								world.setBlock(dPos.relative(dir), TFBlocks.THORN_ROSE.defaultBlockState().setValue(DirectionalBlock.FACING, dir), Block.UPDATE_ALL);
								placed++;
							}
						}
					}
				} else {
					break;
				}
			} else {
				break;
			}
		}

		if (complete && maxLength > 1) {
			Direction nextDir = Direction.getRandom(rand);
			BlockPos nextPos = pos.relative(dir, length - 1).relative(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);
			placed += this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos, config, false);
		}

		if (complete && length > 3 && rand.nextInt(config.chanceOfBranch()) == 0) {
			int middle = rand.nextInt(length);
			Direction nextDir = Direction.getRandom(rand);
			BlockPos nextPos = pos.relative(dir, middle).relative(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);
			placed += this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos, config, false);
		}

		if (complete && length > 3 && rand.nextInt(config.chanceOfLeaf()) == 0) {
			int middle = rand.nextInt(length);
			Direction nextDir = Direction.getRandom(rand);
			BlockPos nextPos = pos.relative(dir, middle).relative(nextDir);
			if (world.isEmptyBlock(nextPos)) {
				world.setBlock(nextPos, TFBlocks.THORN_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, 1), Block.UPDATE_ALL);
				placed++;
			}
		}

		return placed;
	}

	private static boolean checkIsUnderCloud(WorldGenLevel world, BlockPos pos, BlockPos dPos) {
		return world.hasChunk(pos.getX() >> 4, pos.getZ() >> 4)
			&& Math.max(dPos.getY(), world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, dPos.getX(), dPos.getZ())) <= WorldUtil.getGeneratorSeaLevel(world) + 150;
	}

	private boolean canPlaceThorns(LevelAccessor world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return state.isAir()
			|| state.is(BlockTags.LEAVES);
	}
}
