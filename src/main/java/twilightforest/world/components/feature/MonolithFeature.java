package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import twilightforest.entity.passive.Raven;
import twilightforest.init.TFEntities;
import twilightforest.util.features.FeatureUtil;

/**
 * A 2x2 monolith of obsidian
 *
 * @author Ben
 */
public class MonolithFeature implements Feature {

	public MonolithFeature() {
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
		RandomSource rand = random;

		int ht = rand.nextInt(10) + 10;
		int dir = rand.nextInt(4);
		int h0, h1, h2, h3;

		if (!FeatureUtil.isAreaSuitable(world, pos, 2, ht, 2)) {
			return false;
		}

		switch (dir) {
			case 0 -> {
				h0 = ht;
				h1 = (int) (ht * .75);
				h2 = (int) (ht * .75);
				h3 = (int) (ht * .5);
			}
			case 1 -> {
				h0 = (int) (ht * .5);
				h1 = ht;
				h2 = (int) (ht * .75);
				h3 = (int) (ht * .75);
			}
			case 2 -> {
				h0 = (int) (ht * .75);
				h1 = (int) (ht * .5);
				h2 = ht;
				h3 = (int) (ht * .75);
			}
			default -> {
				h0 = (int) (ht * .75);
				h1 = (int) (ht * .75);
				h2 = (int) (ht * .5);
				h3 = ht;
			}
		}

		for (int cy = 0; cy <= h0; cy++) {
			world.setBlock(pos.offset(0, cy - 1, 0), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
		}
		for (int cy = 0; cy <= h1; cy++) {
			world.setBlock(pos.offset(1, cy - 1, 0), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
		}
		for (int cy = 0; cy <= h2; cy++) {
			world.setBlock(pos.offset(0, cy - 1, 1), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
		}
		for (int cy = 0; cy <= h3; cy++) {
			world.setBlock(pos.offset(1, cy - 1, 1), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
		}

		// spawn a few ravens nearby
		for (int i = 0; i < 2; i++) {
			BlockPos dPos = pos.offset(
				rand.nextInt(8) - rand.nextInt(8),
				0,
				rand.nextInt(8) - rand.nextInt(8)
			);
			dPos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, dPos);

			if (dPos.getY() > 0) {
				Raven raven = TFEntities.RAVEN.get().create(world.getLevel(), EntitySpawnReason.STRUCTURE);
				raven.snapTo(dPos, rand.nextFloat() * 360.0F, 0.0F);

				world.addFreshEntity(raven);
			}
		}

		return true;
	}
}
