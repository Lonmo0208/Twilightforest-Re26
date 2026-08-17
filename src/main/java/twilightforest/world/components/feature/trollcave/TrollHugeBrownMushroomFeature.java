package twilightforest.world.components.feature.trollcave;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.util.features.FeatureLogic;


// [VanillaCopy]
public class TrollHugeBrownMushroomFeature implements AbstractHugeMushroomFeature /* TODO-263: was extends HugeBrownMushroomFeature (final) */ {
	public TrollHugeBrownMushroomFeature() {
	}

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MapCodec.unit(this);
	}

	@Override
	public BlockStateProvider capProvider() {
		return BlockStateProvider.simple(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState());
	}

	@Override
	public BlockStateProvider stemProvider() {
		return BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState());
	}

	@Override
	public int foliageRadius() {
		return 3;
	}

	@Override
	public BlockPredicate canPlaceOn() {
		return BlockPredicate.matchesTag(BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON);
	}

	@Override
	public int getTreeRadiusForHeight(int i, int i1, int foliageRadius, int treeHeight) {
		return treeHeight <= 3 ? 0 : foliageRadius;
	}

	@Override
	public void makeCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos) {
		int foliageRadius = this.foliageRadius();

		for (int x = -foliageRadius; x <= foliageRadius; x++) {
			for (int z = -foliageRadius; z <= foliageRadius; z++) {
				if (!FeatureLogic.isCornerInSquare(x, z, foliageRadius)) {
					mutableBlockPos.setWithOffset(pos, x, height, z);

					if (!levelAccessor.getBlockState(mutableBlockPos).is(BlockTags.FEATURES_CANNOT_REPLACE)) {
						BlockState blockState = this.capProvider().getState(levelAccessor, random, pos);
						if (FeatureLogic.hasHorizontalMushroomProperties(blockState)) {
							blockState = FeatureLogic.getHorizontalMushroomBlockState(blockState, x, z, foliageRadius);
						}

						this.setBlock(levelAccessor, mutableBlockPos, blockState);
					}
				}
			}
		}
	}
}
