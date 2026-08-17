package twilightforest.world.components.feature.trees;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.iterators.VoxelBresenhamIterator;


public abstract class CanopyMushroomFeature implements AbstractHugeMushroomFeature {

	public CanopyMushroomFeature() {
	}

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MapCodec.unit(this);
	}

	/**
	 * How much space our mushroom needs, this seems to be about right
	 */
	@Override
	public int getTreeRadiusForHeight(int i, int i1, int foliageRadius, int treeHeight) {
		return treeHeight <= 3 ? 0 : (int) ((float) foliageRadius * 1.5F);
	}

	@Override
	public void placeTrunk(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos) {
		int bugsLeft = Math.max(0, random.nextInt(10) - 4) / 2; //Weird math, I know, but I like the odds (and weird math, sue me)

		for (int i = 0; i < height; ++i) {
			mutableBlockPos.set(pos).move(Direction.UP, i);
			if (this.isReplaceable(levelAccessor, mutableBlockPos)) {
				this.setBlock(levelAccessor, mutableBlockPos, this.stemProvider().getState(levelAccessor, random, pos));

				if (bugsLeft > 0 && i > height / 2 && random.nextInt(10) == 9)
					if (this.addFirefly(levelAccessor, mutableBlockPos, random))
						bugsLeft--;
			} else {
				height = i;
				break;
			}
		}

		int numBranches = this.getBranches(random);
		float offset = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			bugsLeft = this.buildABranch(levelAccessor, pos, height - 6 + b, this.getLength(random), 0.3 * b + offset, random, this.stemProvider(), bugsLeft);
		}
	}

	/**
	 * Add a firefly on a RandomSource face of a block
	 */
	protected boolean addFirefly(LevelAccessor levelAccessor, BlockPos pos, RandomSource random) {
		Direction direction = Direction.getRandom(random);

		if (direction.getAxis() == Direction.Axis.Y) {
			return false;
		}

		BlockPos.MutableBlockPos bugPos = new BlockPos.MutableBlockPos();
		bugPos.set(pos).move(direction);

		if (!this.isReplaceable(levelAccessor, bugPos)) {
			return false;
		}

		BlockState bugState = TFBlocks.FIREFLY.defaultBlockState().setValue(DirectionalBlock.FACING, direction);
		this.setBlock(levelAccessor, bugPos, bugState);
		return true;
	}

	@Override
	public int getTreeHeight(RandomSource random) {
		return 9 + random.nextInt(5);
	}

	protected abstract int getBranches(RandomSource random);

	protected abstract double getLength(RandomSource random);

	private int buildABranch(WorldGenLevel levelAccessor, BlockPos pos, int height, double length, double angle, RandomSource random, BlockStateProvider stemProvider, int bugsLeft) {
		BlockPos src = pos.above(height);
		BlockPos dest = FeatureLogic.translate(src, length, angle, 0.2);

		for (BlockPos pixel : new VoxelBresenhamIterator(src, new BlockPos(dest.getX(), src.getY(), dest.getZ()))) {
			BlockState blockstate = stemProvider.getState(levelAccessor, random, pos);

			if (blockstate.hasProperty(HugeMushroomBlock.UP) && blockstate.hasProperty(HugeMushroomBlock.DOWN)) {
				blockstate = blockstate.setValue(HugeMushroomBlock.DOWN, true).setValue(HugeMushroomBlock.UP, true);//Seal up the ups and downs
			}

			if (this.isReplaceable(levelAccessor, pixel)) {
				this.setBlock(levelAccessor, pixel, blockstate);
			}
		}

		int max = Math.max(src.getY(), dest.getY());

		for (int i = Math.min(src.getY(), dest.getY()); i < max + 1; i++) {
			BlockState blockstate = stemProvider.getState(levelAccessor, random, pos);

			if (blockstate.hasProperty(HugeMushroomBlock.DOWN)) {
				if (i == Math.min(src.getY(), dest.getY())) blockstate = blockstate.setValue(HugeMushroomBlock.DOWN, true);//Seal up the bottom one, so it looks better
			}

			BlockPos blockPos = new BlockPos(dest.getX(), i, dest.getZ());

			if (this.isReplaceable(levelAccessor, blockPos)) {
				this.setBlock(levelAccessor, blockPos, blockstate);
			}

			if (bugsLeft > 0 && i > Math.min(src.getY(), dest.getY()) / 2 && random.nextInt(20) == 0)
				if (this.addFirefly(levelAccessor, blockPos, random))
					bugsLeft--;
		}

		this.makeCap(levelAccessor, random, dest, 1, new BlockPos.MutableBlockPos()); //Branches need caps as well, height in this case is set to 1

		return bugsLeft;
	}

	@Override //Pretty much a 1:1 vanilla copy of the big brown mushroom cap code
	public void makeCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos) {
		int foliageRadius = this.foliageRadius();

		for (int x = -foliageRadius; x <= foliageRadius; ++x) {
			for (int z = -foliageRadius; z <= foliageRadius; ++z) {
				if (!FeatureLogic.isCornerInSquare(x, z, foliageRadius)) {
					mutableBlockPos.setWithOffset(pos, x, height, z);
					if (this.isReplaceable(levelAccessor, mutableBlockPos)) {
						BlockState blockState = this.capProvider().getState(levelAccessor, random, pos);
						blockState = FeatureLogic.getHorizontalMushroomBlockState(blockState, x, z, foliageRadius);
						this.setBlock(levelAccessor, mutableBlockPos, blockState);
					}
				}
			}
		}
	}

	public boolean isReplaceable(BlockGetter levelAccessor, BlockPos pos) {
		BlockState blockState = levelAccessor.getBlockState(pos);
		return FeatureLogic.isReplaceable(blockState, true) && !blockState.isSolidRender();
	}
}
