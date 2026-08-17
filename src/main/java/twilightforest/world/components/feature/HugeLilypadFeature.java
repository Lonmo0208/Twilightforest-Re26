package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import twilightforest.init.TFBlocks;

import static twilightforest.block.HugeLilyPadBlock.FACING;
import static twilightforest.block.HugeLilyPadBlock.PIECE;
import static twilightforest.enums.HugeLilypadPiece.*;

/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public class HugeLilypadFeature implements Feature {

	public HugeLilypadFeature() {
	}

	@Override
	public com.mojang.serialization.MapCodec<? extends net.minecraft.world.level.levelgen.feature.Feature> codec() {
		return com.mojang.serialization.MapCodec.unit(this);
	}

	@Override
	public boolean place(net.minecraft.world.level.WorldGenLevel level, net.minecraft.world.level.chunk.ChunkGenerator chunkGenerator, net.minecraft.util.RandomSource random, net.minecraft.core.BlockPos pos) {
		// ===== 26.3 过渡变量：原 ctx 引用迁移 =====
		@SuppressWarnings("unused") Object _cfg = null; /* _cfg 原本从此处取，现为 Feature 字段 TODO */
		WorldGenLevel world = level;
		for (int i = 0; i < 10; i++) {
			BlockPos dPos = pos.offset(
				random.nextInt(8) - random.nextInt(8),
				random.nextInt(4) - random.nextInt(4),
				random.nextInt(8) - random.nextInt(8)
			);

			if (shouldPlacePadAt(world, dPos)) {
				final Direction horizontal = Direction.from2DDataValue(random.nextInt(4));
				final BlockState lilypad = TFBlocks.HUGE_LILY_PAD.defaultBlockState().setValue(FACING, horizontal);

				world.setBlock(dPos, lilypad.setValue(PIECE, NW), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				world.setBlock(dPos.east(), lilypad.setValue(PIECE, NE), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				world.setBlock(dPos.east().south(), lilypad.setValue(PIECE, SE), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				world.setBlock(dPos.south(), lilypad.setValue(PIECE, SW), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
			}
		}

		return true;
	}

	private boolean shouldPlacePadAt(LevelAccessor world, BlockPos pos) {
		return world.isEmptyBlock(pos) && world.getBlockState(pos.below()).is(Blocks.WATER)
			&& world.isEmptyBlock(pos.east()) && world.getBlockState(pos.east().below()).is(Blocks.WATER)
			&& world.isEmptyBlock(pos.south()) && world.getBlockState(pos.south().below()).is(Blocks.WATER)
			&& world.isEmptyBlock(pos.east().south()) && world.getBlockState(pos.east().south().below()).is(Blocks.WATER);
	}
}
