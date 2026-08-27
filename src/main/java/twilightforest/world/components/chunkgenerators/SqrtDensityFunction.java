package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

public record SqrtDensityFunction(DensityFunction input) implements DensityFunction {
	public static final MapCodec<SqrtDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		DensityFunction.CODEC.fieldOf("input").forGetter(SqrtDensityFunction::input)
	).apply(instance, SqrtDensityFunction::new));

	@Override
	public DensitySampler compileSampler(DensityFunction.CompileContext compileContext) {
		return new SqrtSampler(this.input.compileSampler(compileContext));
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		DensityFunction input = this.input.rewriteChildren(rule);
		return input == this.input ? this : new SqrtDensityFunction(input);
	}

	@Override
	public net.minecraft.util.Interval range() {
		return net.minecraft.util.Interval.of(0.0f, Float.MAX_VALUE);
	}

	@Override
	public com.mojang.serialization.MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

	@Override
	public int domainAxes() {
		return DensityFunction.ALL_AXES;
	}

	public record SqrtSampler(DensitySampler input) implements DensitySampler {
		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer buffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, buffer, volume, this);
		}

		@Override
		public float sampleValue(SamplerContext context, int x, int y, int z) {
			return (float) Math.sqrt(this.input.sampleValue(context, x, y, z));
		}
	}
}