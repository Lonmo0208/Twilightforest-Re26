package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import twilightforest.init.TFBlocks;

/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public class HugeWaterLilyFeature implements Feature {

	public HugeWaterLilyFeature() {
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
		for (int i = 0; i < 4; i++) {
			BlockPos pos_ = pos.offset(
				random.nextInt(8) - random.nextInt(8),
				random.nextInt(4) - random.nextInt(4),
				random.nextInt(8) - random.nextInt(8)
			);

			if (shouldPlacePadAt(world, pos_)) {
				world.setBlock(pos_, TFBlocks.HUGE_WATER_LILY.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
			}
		}

		return true;
	}

	private boolean shouldPlacePadAt(LevelAccessor world, BlockPos pos) {
		return world.isEmptyBlock(pos) && world.getBlockState(pos.below()).is(Blocks.WATER);
	}
}
