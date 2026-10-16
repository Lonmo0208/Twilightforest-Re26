package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

public record SqrtDensityFunction(DensityFunction input) implements DensityFunction {
	public static final MapCodec<SqrtDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		DensityFunction.CODEC.fieldOf("input").forGetter(SqrtDensityFunction::input)
	).apply(instance, SqrtDensityFunction::new));

	@Override
	public DensitySampler compileSampler(DensityFunction.CompileContext context) {
		return new SqrtDensityFunction.Sampler(this.input.compileSampler(context));
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		DensityFunction input = rule.rewrite(this.input);
		return input == this.input ? this : new SqrtDensityFunction(input);
	}

	@Override
	public Interval range() {
		return Interval.of(0.0f, Float.MAX_VALUE);
	}

	@Override
	public int domainAxes() {
		return this.input.domainAxes();
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

	public record Sampler(DensitySampler input) implements DensitySampler {
		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			return (float) Math.sqrt(this.input.sampleValue(context, blockX, blockY, blockZ));
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			this.input.sampleVolume(context, outputBuffer, volume);

			for (int i = 0; i < outputBuffer.size(); i++) {
				outputBuffer.set(i, (float) Math.sqrt(outputBuffer.get(i)));
			}
		}
	}
}
