package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

public record SqrtDensityFunction(DensityFunction input) implements DensityFunction {
	public static final MapCodec<SqrtDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		DensityFunction.CODEC.fieldOf("input").forGetter(SqrtDensityFunction::input)
	).apply(instance, SqrtDensityFunction::new));

	@Override
	public float compute(FunctionContext context) {
		double sqrt = Math.sqrt(this.input.compute(context));
		return (float) sqrt;
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

	@Override
	public void fillArray(float[] ds, DensityFunction.ContextProvider contextProvider) {
		for (int i = 0; i < ds.length; i++) {
			ds[i] = this.compute(contextProvider.forIndex(i));
		}
	}

	@Override
	public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
		return visitor.apply(new SqrtDensityFunction(this.input.mapChildren(visitor)));
	}
}
