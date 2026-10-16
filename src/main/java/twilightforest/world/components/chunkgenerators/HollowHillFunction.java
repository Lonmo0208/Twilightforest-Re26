package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Interval;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

// Negative radius values cause a bowling-up shaped zero-threshold over this DensityFunction's field, making it useful for the hollow hill's floor alongside as its regular mound shape
public record HollowHillFunction(float centerX, float bottomY, float centerZ, float radius, float heightScale) implements DensityFunction {
	public static final MapCodec<HollowHillFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.FLOAT.fieldOf("x_center").forGetter(HollowHillFunction::centerX),
		Codec.FLOAT.fieldOf("y_bottom").forGetter(HollowHillFunction::bottomY),
		Codec.FLOAT.fieldOf("z_center").forGetter(HollowHillFunction::centerZ),
		Codec.FLOAT.fieldOf("radius").forGetter(HollowHillFunction::radius),
		Codec.FLOAT.fieldOf("height_scale").forGetter(HollowHillFunction::heightScale)
	).apply(instance, HollowHillFunction::new));

	public static HollowHillFunction fromPos(BlockPos blockPos, float radius, float heightScale) {
		return new HollowHillFunction(blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f, radius, heightScale);
	}

	@Override
	public DensitySampler compileSampler(DensityFunction.CompileContext context) {
		return new HollowHillFunction.Sampler(this.centerX, this.bottomY, this.centerZ, this.radius, this.heightScale);
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		return this;
	}

	public float compute(float dX, float dY, float dZ) {
		float dist = Mth.sqrt(dX * dX + dZ * dZ);
		// Because cosine is an even function, the radius multiplying cosine's result is the only variable that can affect this DensityFunction using a negative value.
		float height = Mth.cos(dist / this.radius * Mth.PI) * this.radius * 0.3333333334f;

		float normalizedDist = Mth.clamp(dist / Mth.abs(this.radius), 0, 1);

		if (normalizedDist >= 1) {
			return 0;
		}

		return Mth.clamp((height * this.heightScale - dY), -1, 1);
	}

	@Override
	public Interval range() {
		return Interval.of(-1.0f, 1.0f);
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

	@Override
	public int domainAxes() {
		return DensityFunction.ALL_AXES;
	}

	public record Sampler(float centerX, float bottomY, float centerZ, float radius, float heightScale) implements DensitySampler {
		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			float dX = blockX - this.centerX;
			float dY = blockY - this.bottomY;
			float dZ = blockZ - this.centerZ;

			float dist = Mth.sqrt(dX * dX + dZ * dZ);
			// Because cosine is an even function, the radius multiplying cosine's result is the only variable that can affect this DensityFunction using a negative value.
			float height = Mth.cos(dist / this.radius * Mth.PI) * this.radius * 0.3333333334f;

			float normalizedDist = Mth.clamp(dist / Mth.abs(this.radius), 0, 1);

			if (normalizedDist >= 1) {
				return 0;
			}

			return Mth.clamp((height * this.heightScale - dY), -1, 1);
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, outputBuffer, volume, this);
		}
	}
}
