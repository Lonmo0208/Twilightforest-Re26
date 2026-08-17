package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

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

	@Override
	public float compute(FunctionContext context) {
		float dX = this.centerX - context.blockX();
		float dY = this.bottomY - context.blockY();
		float dZ = this.centerZ - context.blockZ();

		float dist = Mth.sqrt(dX * dX + dY * dY + dZ * dZ);

		return Mth.clampedMap(dist, 0, this.radius, this.nearValue, this.farValue);
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
	public void fillArray(float[] ds, DensityFunction.ContextProvider contextProvider) {
		for (int i = 0; i < ds.length; i++) {
			ds[i] = this.compute(contextProvider.forIndex(i));
		}
	}

	@Override
	public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
		return this;
	}
}
