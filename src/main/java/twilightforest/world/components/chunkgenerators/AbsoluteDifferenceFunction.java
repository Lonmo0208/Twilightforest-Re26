package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

public abstract class AbsoluteDifferenceFunction implements DensityFunction {
	public static Min min(double max, BlockPos pos) {
		return new Min(max, pos.getX(), pos.getZ());
	}

	public static Max max(double max, BlockPos pos) {
		return new Max(max, pos.getX(), pos.getZ());
	}

	protected final double max, centerX, centerZ;

	public AbsoluteDifferenceFunction(double max, double centerX, double centerZ) {
		this.max = max;
		this.centerX = centerX;
		this.centerZ = centerZ;
	}

	@Override
	public Interval range() {
		return Interval.of(0.0F, (float) this.max);
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
		return this;
	}

	public static class Min extends AbsoluteDifferenceFunction {
		public static final MapCodec<Min> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.DOUBLE.fieldOf("max").forGetter(f -> f.max),
			Codec.DOUBLE.fieldOf("x_center").forGetter(f -> f.centerX),
			Codec.DOUBLE.fieldOf("z_center").forGetter(f -> f.centerZ)
		).apply(instance, Min::new));

		public Min(double max, double xCenter, double zCenter) {
			super(max, xCenter, zCenter);
		}

		@Override
		public float compute(FunctionContext context) {
			return (float) Math.min(Math.min(Math.abs(context.blockX() - this.centerX), Math.abs(context.blockZ() - this.centerZ)), this.max);
		}

		@Override
		public MapCodec<? extends DensityFunction> codec() {
			return CODEC;
		}
	}

	public static class Max extends AbsoluteDifferenceFunction {
		public static final MapCodec<Max> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.DOUBLE.fieldOf("max").forGetter(f -> f.max),
			Codec.DOUBLE.fieldOf("x_center").forGetter(f -> f.centerX),
			Codec.DOUBLE.fieldOf("z_center").forGetter(f -> f.centerZ)
		).apply(instance, Max::new));

		public Max(double max, double xCenter, double zCenter) {
			super(max, xCenter, zCenter);
		}

		@Override
		public float compute(FunctionContext context) {
			return (float) Math.min(Math.max(Math.abs(context.blockX() - this.centerX), Math.abs(context.blockZ() - this.centerZ)), this.max);
		}

		@Override
		public MapCodec<? extends DensityFunction> codec() {
			return CODEC;
		}
	}
}
