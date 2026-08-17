package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
public class SnowUnderTreeFeature implements Feature {

	public SnowUnderTreeFeature() {
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
		BlockPos.MutableBlockPos mPos = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos mPosDown = new BlockPos.MutableBlockPos();

		for (int xi = 0; xi < 16; xi++) {
			for (int zi = 0; zi < 16; zi++) {
				int x = pos.getX() + xi;
				int z = pos.getZ() + zi;
				mPos.set(x, world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) - 1, z);

				if (world.getBlockState(mPos).getBlock() instanceof LeavesBlock) {
					BlockState state;
					mPos.set(x, world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
					state = world.getBlockState(mPos);

					if (state.isAir()) {
						BlockState stateBelow;
						mPosDown.set(mPos).move(Direction.DOWN);
						stateBelow = world.getBlockState(mPosDown);

						if (stateBelow.isFaceSturdy(world, mPosDown, Direction.UP)) {
							world.setBlock(mPos, Blocks.SNOW.defaultBlockState(), Block.UPDATE_CLIENTS);

							if (stateBelow.hasProperty(SnowyBlock.SNOWY)) {
								world.setBlock(mPosDown, stateBelow.setValue(SnowyBlock.SNOWY, true), Block.UPDATE_CLIENTS);
							}
						}
					}
				}
			}
		}
		return true;
	}
}