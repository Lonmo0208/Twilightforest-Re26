package twilightforest.world.components.feature.trees;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class BrownCanopyMushroomFeature extends CanopyMushroomFeature {
	public BrownCanopyMushroomFeature() {
	}

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MapCodec.unit(this);
	}

	@Override
	public Holder<BlockStateProvider> capProvider() {
		return BlockStateProvider.holderOf(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState());
	}

	@Override
	public Holder<BlockStateProvider> stemProvider() {
		return BlockStateProvider.holderOf(Blocks.MUSHROOM_STEM.defaultBlockState());
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
	protected int getBranches(RandomSource random) {
		return Math.max(random.nextInt(5), 3);
	}

	@Override
	protected double getLength(RandomSource random) {
		return 9 - random.nextInt(2);
	}
}
