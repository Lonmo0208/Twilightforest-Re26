package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.List;

public class BoxDensityFunction implements DensityFunction {
	public static final MapCodec<BoxDensityFunction> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Codec.INT.fieldOf("minX").forGetter(f -> f.minX),
		Codec.INT.fieldOf("minY").forGetter(f -> f.minY),
		Codec.INT.fieldOf("minZ").forGetter(f -> f.minZ),
		Codec.INT.fieldOf("maxX").forGetter(f -> f.maxX),
		Codec.INT.fieldOf("maxY").forGetter(f -> f.maxY),
		Codec.INT.fieldOf("maxZ").forGetter(f -> f.maxZ),
		Codec.DOUBLE.fieldOf("minValue").forGetter(f -> f.minValue),
		Codec.DOUBLE.fieldOf("maxValue").forGetter(f -> f.maxValue),
		TerrainAdjustment.CODEC.fieldOf("beardifier").forGetter(f -> f.terrainAdjustment)
	).apply(inst, BoxDensityFunction::new));

	private final int minX, minY, minZ, maxX, maxY, maxZ;
	private final double minValue, maxValue;
	private final TerrainAdjustment terrainAdjustment;

	public static DensityFunction combine(List<BoundingBox> boxes, int dYMin, int dYMax, TerrainAdjustment terrainAdjustment) {
		if (boxes.isEmpty()) return DensityFunctions.constant(0);

		DensityFunction densityFunction = make(boxes.getFirst(), dYMin, dYMax, terrainAdjustment);

		for (int idx = 1; idx < boxes.size(); idx++) {
			densityFunction = DensityFunctions.add(make(boxes.get(idx), dYMin, dYMax, terrainAdjustment), densityFunction);
		}

		return densityFunction;
	}

	public static BoxDensityFunction make(BoundingBox box, int dYMin, int dYMax, TerrainAdjustment terrainAdjustment) {
		return new BoxDensityFunction(box.minX(), box.minY() + dYMin, box.minZ(), box.maxX(), box.maxY() + dYMax, box.maxZ(), -4.0, 4.0, terrainAdjustment);
	}

	public BoxDensityFunction(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, double minValue, double maxValue, TerrainAdjustment terrainAdjustment) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.terrainAdjustment = terrainAdjustment;
	}

	@Override
	public DensitySampler compileSampler(DensityFunction.CompileContext compileContext) {
		return new BoxSampler(this);
	}

	@Override
	public net.minecraft.util.Interval range() {
		return net.minecraft.util.Interval.of((float) this.minValue, (float) this.maxValue);
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

	@Override
	public int domainAxes() {
		return DensityFunction.ALL_AXES;
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		return this;
	}

	// Replicates the (now private) Beardifier contribution functions so box-based terrain shaping still works in 26.3
	private static final float[] BEARD_KERNEL = Util.make(new float[13824], p -> {
		for (int i = 0; i < 24; i++)
			for (int j = 0; j < 24; j++)
				for (int k = 0; k < 24; k++)
					p[i * 24 * 24 + j * 24 + k] = (float) computeBeardContribution(j - 12, k - 12, i - 12);
	});

	private static boolean isInKernelRange(int value) {
		return value >= 0 && value < 24;
	}

	private static float getBuryContribution(float x, float y, float z) {
		float distSq = Mth.lengthSquared(x, y, z);
		if (distSq >= 36.0F) return 0.0F;
		return 1.0F - Mth.sqrt(distSq) / 6.0F;
	}

	private static double computeBeardContribution(int x, double y, int z) {
		double d = Mth.lengthSquared((double) x, y, (double) z);
		return Math.pow(2.718281828459045D, -d / 16.0D);
	}

	private static float getBeardContribution(int x, int y, int z, int groundLevelDelta) {
		int i = x + 12, j = y + 12, k = z + 12;
		if (isInKernelRange(i) && isInKernelRange(j) && isInKernelRange(k)) {
			float f = (float) groundLevelDelta + 0.5F;
			float f1 = Mth.lengthSquared((float) x, f, (float) z);
			float f2 = (-f) * (float) Mth.fastInvSqrt(f1 / 2.0F) / 2.0F;
			return f2 * BEARD_KERNEL[k * 24 * 24 + j * 24 + i];
		}
		return 0.0F;
	}

	public record BoxSampler(BoxDensityFunction function) implements DensitySampler {
		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer buffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, buffer, volume, this);
		}

		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			// Dist is zero if inside the box
			int xDist = Math.max(0, Math.max(this.function.minX - blockX, blockX - this.function.maxX));
			int zDist = Math.max(0, Math.max(this.function.minZ - blockZ, blockZ - this.function.maxZ));

			int distAboveBottom = blockY - this.function.minY;
			int yDist = switch (this.function.terrainAdjustment) {
				case BURY, BEARD_THIN -> distAboveBottom;
				case BEARD_BOX, ENCAPSULATE -> Math.max(0, Math.max(this.function.minY - blockY, blockY - this.function.maxY));
				default -> 0;
			};

			float densityValue = switch (this.function.terrainAdjustment) {
				case BURY -> getBuryContribution((float) xDist, (float) (yDist * 0.5), (float) zDist);
				case BEARD_THIN, BEARD_BOX -> getBeardContribution(xDist, yDist, zDist, distAboveBottom) * 0.8F;
				case ENCAPSULATE -> getBuryContribution((float) (xDist * 0.5), (float) (yDist * 0.5), (float) (zDist * 0.5)) * 0.8F;
				default -> 0;
			};

			return Mth.clamp(densityValue, (float) this.function.minValue, (float) this.function.maxValue);
		}
	}
}