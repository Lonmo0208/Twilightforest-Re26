package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

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
	public DensitySampler compileSampler(DensityFunction.CompileContext context) {
		AbsoluteDifferenceFunction function = this;

		return new DensitySampler() {
			@Override
			public float sampleValue(SamplerContext samplerContext, int blockX, int blockY, int blockZ) {
				return function.sampleColumn(blockX, blockZ);
			}

			@Override
			public void sampleVolume(SamplerContext samplerContext, DensityBuffer outputBuffer, DensityVolume volume) {
				DensitySampler.sampleVolumeNaive(samplerContext, outputBuffer, volume, this);
			}
		};
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		return this;
	}

	/** Both variants only ever read X and Z, so the density compiler is free to slice and broadcast Y. */
	@Override
	public int domainAxes() {
		return DensityFunction.AXIS_X | DensityFunction.AXIS_Z;
	}

	protected abstract float sampleColumn(int blockX, int blockZ);

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
		protected float sampleColumn(int blockX, int blockZ) {
			return (float) Math.min(Math.min(Math.abs(blockX - this.centerX), Math.abs(blockZ - this.centerZ)), this.max);
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
		protected float sampleColumn(int blockX, int blockZ) {
			return (float) Math.min(Math.max(Math.abs(blockX - this.centerX), Math.abs(blockZ - this.centerZ)), this.max);
		}

		@Override
		public MapCodec<? extends DensityFunction> codec() {
			return CODEC;
		}
	}
}
