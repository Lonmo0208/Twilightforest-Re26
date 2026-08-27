package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

// For making spheres
public record FocusedDensityFunction(float centerX, float bottomY, float centerZ, float radius, float nearValue, float farValue) implements DensityFunction {
	public static final MapCodec<FocusedDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.FLOAT.fieldOf("x_center").forGetter(FocusedDensityFunction::centerX),
		Codec.FLOAT.fieldOf("y_bottom").forGetter(FocusedDensityFunction::bottomY),
		Codec.FLOAT.fieldOf("z_center").forGetter(FocusedDensityFunction::centerZ),
		Codec.FLOAT.fieldOf("radius").forGetter(FocusedDensityFunction::radius),
		Codec.FLOAT.fieldOf("near_value").forGetter(FocusedDensityFunction::nearValue),
		Codec.FLOAT.fieldOf("far_value").forGetter(FocusedDensityFunction::farValue)
	).apply(instance, FocusedDensityFunction::new));

	public static FocusedDensityFunction fromPos(BlockPos blockPos, float radius, float nearValue, float farValue) {
		return new FocusedDensityFunction(blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f, radius, nearValue, farValue);
	}

	public float computeValue(float blockX, float blockY, float blockZ) {
		float dX = this.centerX - blockX;
		float dY = this.bottomY - blockY;
		float dZ = this.centerZ - blockZ;

		float dist = Mth.sqrt(dX * dX + dY * dY + dZ * dZ);

		return Mth.clampedMap(dist, 0, this.radius, this.nearValue, this.farValue);
	}

	@Override
	public DensitySampler compileSampler(DensityFunction.CompileContext compileContext) {
		return new FocusedSampler(this);
	}

	@Override
	public net.minecraft.util.Interval range() {
		return net.minecraft.util.Interval.of(Math.min(this.nearValue, this.farValue), Math.max(this.nearValue, this.farValue));
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

	public record FocusedSampler(FocusedDensityFunction function) implements DensitySampler {
		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer buffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, buffer, volume, this);
		}

		@Override
		public float sampleValue(SamplerContext context, int x, int y, int z) {
			return this.function.computeValue(x, y, z);
		}
	}
}