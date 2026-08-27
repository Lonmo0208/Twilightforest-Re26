package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

// Negative radius values cause a bowling-up shaped zero-threshold over this DensityFunction's field, making it useful for the hollow hill's floor alongside as its regular mound shape
public record TanhHillFunction(float centerX, float bottomY, float centerZ, float radius, float heightScale, float cosAngleBiasDirection, float sinAngleBiasDirection, boolean isXOriented, boolean isOnRightSide) implements DensityFunction {
	public static final MapCodec<TanhHillFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.FLOAT.fieldOf("x_center").forGetter(TanhHillFunction::centerX),
		Codec.FLOAT.fieldOf("y_bottom").forGetter(TanhHillFunction::bottomY),
		Codec.FLOAT.fieldOf("z_center").forGetter(TanhHillFunction::centerZ),
		Codec.FLOAT.fieldOf("radius").forGetter(TanhHillFunction::radius),
		Codec.FLOAT.fieldOf("height_scale").forGetter(TanhHillFunction::heightScale),
		Codec.FLOAT.fieldOf("cos_angle_bias_direction").forGetter(TanhHillFunction::cosAngleBiasDirection),
		Codec.FLOAT.fieldOf("sin_angle_bias_direction").forGetter(TanhHillFunction::sinAngleBiasDirection),
		Codec.BOOL.fieldOf("is_x_oriented").forGetter(TanhHillFunction::isXOriented),
		Codec.BOOL.fieldOf("is_on_right_side").forGetter(TanhHillFunction::isOnRightSide)
	).apply(instance, TanhHillFunction::new));
	private static final float PERPENDICULAR_BIAS = 1.3F;

	public TanhHillFunction(float centerX, float bottomY, float centerZ, float radius, float heightScale, float angleBiasDirection, boolean isXOriented, boolean isOnRightSide) {
		this(centerX, bottomY, centerZ, radius, heightScale, Mth.cos(angleBiasDirection), Mth.sin(angleBiasDirection), isXOriented, isOnRightSide);
	}

	public double compute(float dX, float dY, float dZ) {
		if (dY > 10 || dY < -10)
			return -1;
		float perpendicularDist = isXOriented ? dZ : dX;
		if (!isOnRightSide)
			perpendicularDist = -perpendicularDist;
		if (perpendicularDist < 0)
			return -1;

		double distSquared = dX * dX + dZ * dZ;
		if (distSquared > 9 * radius * radius)
			return -1;
		double dist = Math.sqrt(distSquared);
		// phi = atan2(dZ, dX); atan2 is expensive, don't use it.
		double cosPhi = dX / dist;
		double sinPhi = dZ / dist;
		double cos2Phi = cosPhi * cosPhi - sinPhi * sinPhi;
		double sin2Phi = 2 * cosPhi * sinPhi;
		dist /= (1 + (sin2Phi * cosAngleBiasDirection - cos2Phi * sinAngleBiasDirection) / 3);  // = (1 + Math.sin(phi * 2 - angleBiasDirection) / 3)
		double height = getHeight(dist);
		if (this.heightScale * (height + 1) > dY)
			return 1;
		return -1;
	}

	private double getHeight(double dist) {
		double distOverR = dist / this.radius;
		double tanhExp = Math.exp(4 - 4 * distOverR);
		double height = ((tanhExp - 1) / (tanhExp + 1));

		double beardArg = -dist * this.radius * 0.01f;
		if (beardArg < -10) {
			height -= dist;
		} else {
			height -= dist * (1 - Math.exp(beardArg));
		}

		return height;
	}

	@Override
	public DensitySampler compileSampler(DensityFunction.CompileContext compileContext) {
		return new TanhHillSampler(this);
	}

	@Override
	public net.minecraft.util.Interval range() {
		return net.minecraft.util.Interval.of(-1.0f, 1.0f);
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

	public record TanhHillSampler(TanhHillFunction function) implements DensitySampler {
		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer buffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, buffer, volume, this);
		}

		@Override
		public float sampleValue(SamplerContext context, int x, int y, int z) {
			float dX = x - this.function.centerX();
			float dY = y - this.function.bottomY();
			float dZ = z - this.function.centerZ();
			if (this.function.isXOriented())  // Make mounds more parallel to the trunk axis in shape
				dZ *= PERPENDICULAR_BIAS;
			else
				dX *= PERPENDICULAR_BIAS;
			return (float) this.function.compute(dX, dY, dZ);
		}
	}
}