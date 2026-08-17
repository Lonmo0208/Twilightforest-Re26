package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeatureLogic;

// TODO-263: config stored as instance fields; register via FeatureType bootstrap
public class BigMushgloomFeature implements AbstractHugeMushroomFeature {

	public BigMushgloomFeature() {
	}

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MapCodec.unit(this);
	}

	@Override
	public BlockStateProvider capProvider() {
		return BlockStateProvider.simple(TFBlocks.HUGE_MUSHGLOOM.defaultBlockState());
	}

	@Override
	public BlockStateProvider stemProvider() {
		return BlockStateProvider.simple(TFBlocks.HUGE_MUSHGLOOM_STEM.defaultBlockState());
	}

	@Override
	public int foliageRadius() {
		return 3;
	}

	@Override
	public BlockPredicate canPlaceOn() {
		return BlockPredicate.alwaysTrue(); // TODO-263: use proper placeable predicate
	}

	@Override
	public int getTreeHeight(RandomSource rand) {
		return 2 + rand.nextInt(2);
	}

	@Override
	public int getTreeRadiusForHeight(int i, int i1, int foliageRadius, int treeHeight) {
		return treeHeight <= 2 ? 0 : foliageRadius;
	}

	@Override
	public void makeCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos) {
		int foliageRadius = this.foliageRadius();
		int capHeight = random.nextBoolean() ? 1 : 2;

		for (int y = 0; y < capHeight; y++) {
			for (int x = -foliageRadius; x <= foliageRadius; ++x) {
				for (int z = -foliageRadius; z <= foliageRadius; ++z) {
					mutableBlockPos.setWithOffset(pos, x, height + y, z);
					if (!levelAccessor.getBlockState(mutableBlockPos).isSolidRender()) {
						BlockState blockstate = this.capProvider().getState(levelAccessor, random, pos);
						blockstate = FeatureLogic.getSphericalMushroomBlockState(blockstate, x, y, z, foliageRadius, capHeight);
						this.setBlock(levelAccessor, mutableBlockPos, blockstate);
					}
				}
			}
		}
	}
}
