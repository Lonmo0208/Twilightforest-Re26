package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.codec.RegistryFileCodec;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import twilightforest.TFRegistries;
import twilightforest.world.components.layer.BiomeDensitySource;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A DensityFunction implementation that enables Biomes to influence terrain formulations, if in the noise chunk generator.
 */
public class NoiseDensityRouter implements DensityFunction {
	public static final MapCodec<NoiseDensityRouter> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		RegistryFileCodec.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC, false).fieldOf("terrain_source").forGetter(NoiseDensityRouter::biomeDensitySourceHolder),
		Codec.doubleRange(-64, 0).fieldOf("lower_density_bound").forGetter(NoiseDensityRouter::lowerDensityBound),
		Codec.doubleRange(0, 64).fieldOf("upper_density_bound").forGetter(NoiseDensityRouter::upperDensityBound),
		Codec.doubleRange(0, 32).orElse(8.0).fieldOf("depth_scalar").forGetter(NoiseDensityRouter::depthScalar)
	).apply(inst, NoiseDensityRouter::new));

	private final Holder<BiomeDensitySource> biomeDensitySourceHolder;
	private final double lowerDensityBound;
	private final double upperDensityBound;
	private final double depthScalar;

	/**
	 * @param biomeDensitySource A BiomeDensitySource containing TerrainColumns, providing per-biome scaling and depth behavior that allows biomes to distinguish their landscapes.
	 * @param lowerDensityBound  Lower clamp bound
	 * @param upperDensityBound  Upper clamp bound
	 */
	public NoiseDensityRouter(Holder<BiomeDensitySource> biomeDensitySource, double lowerDensityBound, double upperDensityBound, double depthScalar) {
		this.biomeDensitySourceHolder = biomeDensitySource;
		this.lowerDensityBound = lowerDensityBound;
		this.upperDensityBound = upperDensityBound;
		this.depthScalar = depthScalar;
	}

	@Override
	public DensitySampler compileSampler(DensityFunction.CompileContext context) {
		BiomeDensitySource biomeDensitySource = this.biomeDensitySourceHolder.value();

		Map<TerrainColumn, TerrainColumn.Compiled> compiledColumns = new IdentityHashMap<>();

		return new NoiseDensityRouter.Sampler(
			biomeDensitySource,
			(float) this.lowerDensityBound,
			(float) this.upperDensityBound,
			(float) this.depthScalar,
			column -> compiledColumns.computeIfAbsent(column, terrainColumn -> terrainColumn.compile(context))
		);
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		return this;
	}

	@Override
	public Interval range() {
		return Interval.of((float) this.lowerDensityBound, (float) this.upperDensityBound);
	}

	/**
	 * Vertical insensitivity is the same assumption {@link TerrainColumn} makes: the biome column data is resolved per X-Z,
	 * so only X and Z are claimed here and the density compiler slices/broadcasts Y for us.
	 */
	@Override
	public int domainAxes() {
		return DensityFunction.AXIS_X | DensityFunction.AXIS_Z;
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

	public Holder<BiomeDensitySource> biomeDensitySourceHolder() {
		return this.biomeDensitySourceHolder;
	}

	public double lowerDensityBound() {
		return this.lowerDensityBound;
	}

	public double upperDensityBound() {
		return this.upperDensityBound;
	}

	public double depthScalar() {
		return this.depthScalar;
	}

	public record Sampler(
		BiomeDensitySource biomeDensitySource,
		float lowerDensityBound,
		float upperDensityBound,
		float depthScalar,
		Function<TerrainColumn, TerrainColumn.Compiled> columnCompiler
	) implements DensitySampler {
		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			return (float) this.biomeDensitySource.sampleTerrain(blockX, blockY, blockZ, context, this.columnCompiler).scale;
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, outputBuffer, volume, this);
		}
	}
}
