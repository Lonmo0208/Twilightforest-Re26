package twilightforest.world.components.feature.trollcave;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.util.features.FeatureLogic;


// [VanillaCopy]
public class TrollHugeRedMushroomFeature implements AbstractHugeMushroomFeature /* TODO-263: was extends HugeRedMushroomFeature (final) */ {
	public TrollHugeRedMushroomFeature() {
	}

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MapCodec.unit(this);
	}

	@Override
	public BlockStateProvider capProvider() {
		return BlockStateProvider.simple(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState());
	}

	@Override
	public BlockStateProvider stemProvider() {
		return BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState());
	}

	@Override
	public int foliageRadius() {
		return 2;
	}

	@Override
	public BlockPredicate canPlaceOn() {
		return BlockPredicate.matchesTag(BlockTags.HUGE_RED_MUSHROOM_CAN_PLACE_ON);
	}

	@Override
	public int getTreeRadiusForHeight(int trunkHeight, int treeHeight, int foliageRadius, int yo) {
		int radius = 0;
		if (yo < treeHeight && yo >= treeHeight - 3) {
			radius = foliageRadius;
		} else if (yo == treeHeight) {
			radius = foliageRadius;
		}
		return radius;
	}

	@Override
	public void makeCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos) {
		for (int y = height - 3; y <= height; y++) {
			int foliageRadius = y < height ? this.foliageRadius() : this.foliageRadius() - 1;
			int innerRadius = this.foliageRadius() - 2;

			for (int x = -foliageRadius; x <= foliageRadius; x++) {
				for (int z = -foliageRadius; z <= foliageRadius; z++) {
					if (y >= height || FeatureLogic.isEdge(x, z, foliageRadius)) {
						mutableBlockPos.setWithOffset(pos, x, y, z);
						if (!levelAccessor.getBlockState(mutableBlockPos).is(BlockTags.FEATURES_CANNOT_REPLACE)) {
							BlockState blockState = this.capProvider().getState(levelAccessor, random, pos);
							if (FeatureLogic.hasAllMushroomsProperties(blockState)) {
								blockState = blockState.setValue(HugeMushroomBlock.UP, y >= height - 1)
									.setValue(HugeMushroomBlock.WEST, x < -innerRadius)
									.setValue(HugeMushroomBlock.EAST, x > innerRadius)
									.setValue(HugeMushroomBlock.NORTH, z < -innerRadius)
									.setValue(HugeMushroomBlock.SOUTH, z > innerRadius);
							}
							this.setBlock(levelAccessor, mutableBlockPos, blockState);
						}
					}
				}
			}
		}
	}
}
