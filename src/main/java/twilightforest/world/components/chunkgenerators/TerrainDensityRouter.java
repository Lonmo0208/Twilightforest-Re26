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
public class TerrainDensityRouter implements DensityFunction {
	public static final MapCodec<TerrainDensityRouter> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		RegistryFileCodec.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC, false).fieldOf("terrain_source").forGetter(TerrainDensityRouter::biomeDensitySourceHolder),
		Codec.doubleRange(-64, 0).fieldOf("lower_density_bound").forGetter(TerrainDensityRouter::lowerDensityBound),
		Codec.doubleRange(0, 64).fieldOf("upper_density_bound").forGetter(TerrainDensityRouter::upperDensityBound),
		Codec.doubleRange(0, 32).orElse(8.0).fieldOf("depth_scalar").forGetter(TerrainDensityRouter::depthScalar),
		DensityFunction.CODEC.fieldOf("base_factor").forGetter(TerrainDensityRouter::baseFactor),
		DensityFunction.CODEC.fieldOf("base_offset").forGetter(TerrainDensityRouter::baseOffset)
	).apply(inst, TerrainDensityRouter::new));

	private final Holder<BiomeDensitySource> biomeDensitySourceHolder;
	private final double lowerDensityBound;
	private final double upperDensityBound;
	private final double depthScalar;
	private final DensityFunction baseFactor;
	private final DensityFunction baseOffset;

	/**
	 * @param biomeDensitySource A BiomeDensitySource containing TerrainColumns, providing per-biome scaling and depth behavior that allows biomes to distinguish their landscapes.
	 * @param lowerDensityBound  Lower clamp bound
	 * @param upperDensityBound  Upper clamp bound
	 * @param baseFactor         Density function (can be constant) for the height of the vertical y-gradient at a given X-Z position. A biome speeds or slows this vertical rate of change.
	 * @param baseOffset         Density function (can be constant) for the elevation of the vertical y-gradient at a given X-Z position. A biome moves it up and down.
	 */
	public TerrainDensityRouter(Holder<BiomeDensitySource> biomeDensitySource, double lowerDensityBound, double upperDensityBound, double depthScalar, DensityFunction baseFactor, DensityFunction baseOffset) {
		this.biomeDensitySourceHolder = biomeDensitySource;
		this.lowerDensityBound = lowerDensityBound;
		this.upperDensityBound = upperDensityBound;
		this.depthScalar = depthScalar;
		this.baseFactor = baseFactor;
		this.baseOffset = baseOffset;
	}

	@Override
	public DensitySampler compileSampler(DensityFunction.CompileContext context) {
		BiomeDensitySource biomeDensitySource = this.biomeDensitySourceHolder.value();

		// Compiling a TerrainColumn is only ever done once per worldgen RandomState, and the blend below hits the same handful of columns per lookup.
		Map<TerrainColumn, TerrainColumn.Compiled> compiledColumns = new IdentityHashMap<>();

		return new TerrainDensityRouter.Sampler(
			biomeDensitySource,
			(float) this.lowerDensityBound,
			(float) this.upperDensityBound,
			(float) this.depthScalar,
			this.baseFactor.compileSampler(context),
			this.baseOffset.compileSampler(context),
			column -> compiledColumns.computeIfAbsent(column, terrainColumn -> terrainColumn.compile(context))
		);
	}

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		DensityFunction factor = rule.rewrite(this.baseFactor);
		DensityFunction offset = rule.rewrite(this.baseOffset);

		if (factor == this.baseFactor && offset == this.baseOffset) return this;

		return new TerrainDensityRouter(this.biomeDensitySourceHolder, this.lowerDensityBound, this.upperDensityBound, this.depthScalar, factor, offset);
	}

	@Override
	public Interval range() {
		return Interval.of((float) this.lowerDensityBound, (float) this.upperDensityBound);
	}

	/**
	 * The terrain blend is resolved per X-Z column - TerrainColumn noise is expected to be vertically insensitive, see
	 * {@link TerrainColumn}. Declaring only the axes the base functions actually read lets the density compiler slice and
	 * broadcast Y for us, which is what keeps biome blending from being recomputed for every Y in a sample volume.
	 */
	@Override
	public int domainAxes() {
		return DensityFunction.AXIS_X | DensityFunction.AXIS_Z | this.baseFactor.domainAxes() | this.baseOffset.domainAxes();
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

	public DensityFunction baseFactor() {
		return this.baseFactor;
	}

	public DensityFunction baseOffset() {
		return this.baseOffset;
	}

	public record Sampler(
		BiomeDensitySource biomeDensitySource,
		float lowerDensityBound,
		float upperDensityBound,
		float depthScalar,
		DensitySampler baseFactor,
		DensitySampler baseOffset,
		Function<TerrainColumn, TerrainColumn.Compiled> columnCompiler
	) implements DensitySampler {
		@Override
		public float sampleValue(SamplerContext context, int blockX, int blockY, int blockZ) {
			BiomeDensitySource.DensityData densityData = this.biomeDensitySource.sampleTerrain(blockX, blockY, blockZ, context, this.columnCompiler);
			double depth = this.baseOffset.sampleValue(context, blockX, blockY, blockZ) + densityData.depth * this.baseFactor.sampleValue(context, blockX, blockY, blockZ);
			return (float) (depth + densityData.depth);
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer outputBuffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, outputBuffer, volume, this);
		}
	}
}
