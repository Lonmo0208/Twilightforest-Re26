package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;

public class WebFeature implements Feature {

	public WebFeature() {
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return MapCodec.unit(this);
	}

	private static boolean isValidMaterial(BlockState state) {
		return state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
		BlockPos origin = pos;
		BlockPos blockPos = origin.above(random.nextInt(level.getMaxY() - origin.getY()));
		while (blockPos.getY() > origin.getY()) {
			blockPos = blockPos.below();
			BlockState state = level.getBlockState(blockPos);
			if (level.isEmptyBlock(blockPos.below()) && isValidMaterial(state)) {
				level.setBlock(state.is(BlockTags.LEAVES) && random.nextBoolean() ? blockPos : blockPos.below(), Blocks.COBWEB.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				return true;
			}
		}

		return false;
	}
}
