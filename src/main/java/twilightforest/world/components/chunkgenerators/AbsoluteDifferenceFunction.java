package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
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
	public int domainAxes() {
		return DensityFunction.ALL_AXES;
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
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
		public DensitySampler compileSampler(DensityFunction.CompileContext compileContext) {
			return new MinSampler(this.max, this.centerX, this.centerZ);
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
		public DensitySampler compileSampler(DensityFunction.CompileContext compileContext) {
			return new MaxSampler(this.max, this.centerX, this.centerZ);
		}

		@Override
		public MapCodec<? extends DensityFunction> codec() {
			return CODEC;
		}
	}

	public record MinSampler(double max, double centerX, double centerZ) implements DensitySampler {
		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer buffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, buffer, volume, this);
		}

		@Override
		public float sampleValue(SamplerContext context, int x, int y, int z) {
			return (float) Math.min(Math.min(Math.abs(x - this.centerX), Math.abs(z - this.centerZ)), this.max);
		}
	}

	public record MaxSampler(double max, double centerX, double centerZ) implements DensitySampler {
		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer buffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, buffer, volume, this);
		}

		@Override
		public float sampleValue(SamplerContext context, int x, int y, int z) {
			return (float) Math.min(Math.max(Math.abs(x - this.centerX), Math.abs(z - this.centerZ)), this.max);
		}
	}
}