package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.chunk.ChunkGenerator;
//[VanillaCopy] of BaseDiskFeature, but we add a check to make sure the block above is air
public class CheckAbovePatchFeature implements Feature {

	public static final MapCodec<CheckAbovePatchFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(f -> f.stateProvider),
		BlockPredicate.CODEC.fieldOf("target").forGetter(f -> f.target),
		IntProviders.codec(0, 8).fieldOf("radius").forGetter(f -> f.radius),
		com.mojang.serialization.Codec.intRange(0, 4).fieldOf("half_height").forGetter(f -> f.halfHeight)
	).apply(instance, CheckAbovePatchFeature::new));

	private final BlockStateProvider stateProvider;
	private final BlockPredicate target;
	private final IntProvider radius;
	private final int halfHeight;

	public CheckAbovePatchFeature(BlockStateProvider stateProvider, BlockPredicate target, IntProvider radius, int halfHeight) {
		this.stateProvider = stateProvider;
		this.target = target;
		this.radius = radius;
		this.halfHeight = halfHeight;
	}

	public CheckAbovePatchFeature() {
		this(BlockStateProvider.simple(Blocks.MYCELIUM), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK), UniformInt.of(4, 6), 3);
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return CODEC;
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
		BlockPos blockpos = pos;
		boolean flag = false;
		int i = blockpos.getY();
		int j = i + this.halfHeight;
		int k = i - this.halfHeight - 1;
		int l = this.radius.sample(random);
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-l, 0, -l), blockpos.offset(l, 0, l))) {
			int i1 = blockpos1.getX() - blockpos.getX();
			int j1 = blockpos1.getZ() - blockpos.getZ();
			if (i1 * i1 + j1 * j1 <= l * l) {
				flag |= this.placeColumn(level, random, j, k, blockpos$mutableblockpos.set(blockpos1));
			}
		}

		return flag;
	}

	protected boolean placeColumn(WorldGenLevel level, RandomSource random, int start, int end, BlockPos.MutableBlockPos mutablePos) {
		boolean flag = false;

		for (int i = start; i > end; --i) {
			mutablePos.setY(i);
			if (this.target.test(level, mutablePos) && level.getBlockState(mutablePos.above()).canBeReplaced()) {
				BlockState blockstate1 = this.stateProvider.getState(level, random, mutablePos);
				level.setBlock(mutablePos, blockstate1, Block.UPDATE_CLIENTS);
				this.markAboveForPostProcessing(level, mutablePos);
				flag = true;
			}
		}

		return flag;
	}
}
