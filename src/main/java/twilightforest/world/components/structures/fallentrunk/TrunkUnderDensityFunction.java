package twilightforest.world.components.structures.fallentrunk;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Interval;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.world.components.chunkgenerators.TanhHillFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Terraforms the ground under a fallen trunk: a dirt mound under the trunk itself and flat, gently sloping terrain
 * around it. Kept as a DensityFunction so it can be handed to a structure's CustomDensitySource terraformer.
 */
public class TrunkUnderDensityFunction implements DensityFunction {
	private final boolean isBigTree;
	private final boolean isXOriented;
	private final RandomSource random;  // used to create dirt mounds
	private final BoundingBox boundingBox;
	private final List<TanhHillFunction> tanhHillFunctions;
	private final Beardifier.Rigid fallenTrunkPiece;
	protected final BoundingBox moundBase;
	protected static final float MOUND_RADIUS = 3F;
	protected static final int MAX_RANDOM_RADIUS_INCREASE_BIG_TREE = 4;
	protected static final int MAX_RANDOM_RADIUS_INCREASE_NON_BIG_TREE = 2;
	protected static final float BIG_TREE_MOUND_HEIGHT = 3F;
	protected static final float NON_BIG_TREE_MOUND_HEIGHT = 1.5F;

	public TrunkUnderDensityFunction(List<Beardifier.Rigid> pieces, FallenTrunkPiece piece, boolean isBigTree, int minMounds, int maxMounds) {
		this.isBigTree = isBigTree;
		this.fallenTrunkPiece = pieces.getFirst();
		this.boundingBox = this.fallenTrunkPiece.box();
		this.random = RandomSource.create(this.boundingBox.minX() * 14413411L + this.boundingBox.minZ() * 43387781L);
		this.isXOriented = this.boundingBox.maxX() - this.boundingBox.minX() > this.boundingBox.maxZ() - this.boundingBox.minZ();
		int length = this.isXOriented ? this.boundingBox.getXSpan() : this.boundingBox.getZSpan();
		Vec3i moundBaseCorner = new Vec3i(
			this.isXOriented ? length / 2 : 0,
			1,
			!this.isXOriented ? length / 2 : 0);
		this.moundBase = BoundingBox.fromCorners(moundBaseCorner, moundBaseCorner);
		this.tanhHillFunctions = new ArrayList<>();
		int attemptedMoundsNumber = this.random.nextInt(minMounds, maxMounds + 1);
		for (int i = 0; i < attemptedMoundsNumber; i++) {
			Optional<TanhHillFunction> function = this.getTanhHillFunctionWithoutCoveringHole(piece, i % 2 == 0);
			function.ifPresent(this.tanhHillFunctions::add);
		}
	}

	@Override
	public DensitySampler compileSampler(DensityFunction.CompileContext context) {
		// The mound shapes are already fixed at construction time; their samplers just evaluate the closed-form height
		List<DensitySampler> mounds = new ArrayList<>(this.tanhHillFunctions.size());
		for (TanhHillFunction function : this.tanhHillFunctions) {
			mounds.add(function.compileSampler(context));
		}

		return new TrunkUnderDensityFunction.Sampler(this.boundingBox, mounds, this.isBigTree, this.isXOriented, this.fallenTrunkPiece.groundLevelDelta());
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		return this;
	}

	@Override
	public Interval range() {
		return Interval.INFINITE;
	}

	@Override
	public int domainAxes() {
		return DensityFunction.ALL_AXES;
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		// Rebuilt per chunk from live structure pieces, so there is no datapack form to serialize to.
		throw new UnsupportedOperationException("TrunkUnderDensityFunction is constructed at runtime and cannot be serialized");
	}

	public static double flatSurroundingTerrain(double x, double y, double z) {
		if (y > 20 || y < -10)
			return 0;
		if (x == 0 && z == 0 && y > 0)
			return -1;  // flat everything inside the trunk
		final double a = FallenTrunkPiece.TERRAFORM_PIECE_SIZE, c = FallenTrunkPiece.TERRAFORM_PIECE_SIZE;
		final double verticalScale = 0.7;
		double normX = Math.abs(x / a);
		double normZ = Math.abs(z / c);
		double normalizedSquaredDistance = normX * normX + normZ * normZ;

		if (normalizedSquaredDistance >= 1.0)
			return 0.0;

		double normalizedDistance = Math.sqrt(normalizedSquaredDistance);

		double factor = 1.0 - normalizedDistance;
		if (y < 0)  // make less steep to avoid vertical walls around the trunk
			y = normalizedDistance * 0.5 + y / 5;  // add normalizedDistance to avoid axis aligned "stairs"
		// multiply by exponent to make the beardifier weaker with distance
		return -y * factor / verticalScale * (Math.exp(-normalizedDistance / (Math.max(1 - normalizedDistance, 1e-8))));
	}

	protected Optional<TanhHillFunction> getTanhHillFunctionWithoutCoveringHole(FallenTrunkPiece piece, boolean isOnRightSide) {
		if (this.isBigTree)
			return Optional.ofNullable(this.getTanhHillFunction((BlockPos) Util.getRandom(piece.getAllPotentialBaseMoundBlockPos(isOnRightSide).toArray(), this.random), isOnRightSide));  // Big trees don't have holes

		Set<BlockPos> blockPosSet = piece.getAllowedBaseMoundBlockPos(isOnRightSide);
		if (blockPosSet.isEmpty())
			return Optional.empty();
		BlockPos moundBLockPos = (BlockPos) Util.getRandom(blockPosSet.toArray(), this.random);
		return Optional.ofNullable(this.getTanhHillFunction(moundBLockPos, isOnRightSide));
	}

	protected TanhHillFunction getTanhHillFunction(BlockPos baseBlockPos, boolean isOnRightSide) {
		int maxRandomRadiusIncrease = this.isBigTree ? MAX_RANDOM_RADIUS_INCREASE_BIG_TREE : MAX_RANDOM_RADIUS_INCREASE_NON_BIG_TREE;
		float moundRadius = MOUND_RADIUS + this.random.nextInt(0, maxRandomRadiusIncrease);
		float moundHeight = this.isBigTree ? BIG_TREE_MOUND_HEIGHT : NON_BIG_TREE_MOUND_HEIGHT;

		float moundBiasAngle = Mth.TWO_PI * this.random.nextFloat();
		return new TanhHillFunction(baseBlockPos.getX(), baseBlockPos.getY(), baseBlockPos.getZ(), moundRadius, moundHeight, moundBiasAngle, this.isXOriented, isOnRightSide);
	}

	protected Beardifier.Rigid getFallenTrunkPiece() {
		return this.fallenTrunkPiece;
	}

	private static int getRadius(BoundingBox box) {
		return getRadius(box.getYSpan());
	}

	private static int getRadius(int diameter) {
		return (int) Math.ceil(diameter / 2D);
	}

	public record Sampler(BoundingBox boundingBox, List<DensitySampler> mounds, boolean isBigTree, boolean isXOriented, int groundLevelDelta) implements DensitySampler {
		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			int horizontalDistanceX = Math.max(0, Math.max(this.boundingBox.minX() - blockX, blockX - this.boundingBox.maxX()));
			int horizontalDistanceZ = Math.max(0, Math.max(this.boundingBox.minZ() - blockZ, blockZ - this.boundingBox.maxZ()));
			int adjustedGroundLevel = this.boundingBox.minY() + this.groundLevelDelta + (this.isBigTree ? 1 : 0);
			int verticalDistance = blockY - adjustedGroundLevel;

			double moundContribution = this.computeMoundsContribution(context, blockX, blockY, blockZ);
			if (moundContribution > 0)
				return (float) moundContribution;
			return (float) TrunkUnderDensityFunction.flatSurroundingTerrain(horizontalDistanceX, verticalDistance, horizontalDistanceZ);
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, outputBuffer, volume, this);
		}

		private double computeMoundsContribution(SamplerContext context, int blockX, int blockY, int blockZ) {
			int x = blockX - this.boundingBox.minX();
			int y = blockY - this.boundingBox.minY();
			int z = blockZ - this.boundingBox.minZ();
			int radius = TrunkUnderDensityFunction.getRadius(this.boundingBox);
			double ax = Math.abs((this.isXOriented ? z : x) - radius + 1);
			double az = Math.abs(y - radius + 1);
			if (radius == 2D) {  // This case is generated differently
				if (Math.abs((this.isXOriented ? z : x) - 1.5) + Math.abs(y - 1.5) <= 2)
					return -1;
			} else {
				if ((int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5)) < radius)
					return -1;
			}

			double max = -1D;
			for (DensitySampler mound : this.mounds) {
				double value = mound.sampleValue(context, blockX, blockY, blockZ);
				if (value > max)
					max = value;
			}
			return max;
		}
	}
}
