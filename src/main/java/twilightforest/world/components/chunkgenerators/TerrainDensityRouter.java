package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.codec.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.densityfunction.DensityBuffer;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import twilightforest.TFRegistries;
import twilightforest.world.components.layer.BiomeDensitySource;

import java.util.Map;

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

	// Our default method for obtaining column samples of the biome source.
	public BiomeDensitySource.DensityData computeTerrain(Map<ResourceKey<Biome>, TerrainColumn.TerrainColumnSamplers> compiledSamplers, SamplerContext context, int blockX, int blockY, int blockZ) {
		return this.biomeDensitySourceHolder.value().sampleTerrain(blockX, blockZ, blockY, context, compiledSamplers);
	}

	@Override
	public DensitySampler compileSampler(DensityFunction.CompileContext compileContext) {
		return new TerrainRouterSampler(
			this.biomeDensitySourceHolder.value(),
			this.biomeDensitySourceHolder.value().compileTerrain(compileContext),
			this.baseOffset.compileSampler(compileContext),
			this.baseFactor.compileSampler(compileContext),
			this.depthScalar
		);
	}

	@Override
	public net.minecraft.util.Interval range() {
		return net.minecraft.util.Interval.of((float) this.lowerDensityBound, (float) this.upperDensityBound);
	}

	@Override
	public MapCodec<? extends DensityFunction> codec() { return CODEC; }

	@Override
	public int domainAxes() {
		return DensityFunction.ALL_AXES;
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

	@Override
	public DensityFunction rewriteChildren(DfRewriteRule rule) {
		DensityFunction factor = this.baseFactor.rewriteChildren(rule);
		DensityFunction offset = this.baseOffset.rewriteChildren(rule);
		if (factor == this.baseFactor && offset == this.baseOffset) return this;
		return new TerrainDensityRouter(this.biomeDensitySourceHolder, this.lowerDensityBound, this.upperDensityBound, this.depthScalar, factor, offset);
	}

	/**
	 * TerrainDensityRouter is at best, a configuration class with DensityFunction capabilities.
	 * This Sampler made once per Chunk in noisegen caches the first density value obtained from
	 * each unique X-Z coordinate, ambiguating the Y value in coordinate.
	 * Plan your biome density functions accordingly! Don't use anything that's vertically sensitive.
	 */
	public static class TerrainRouterSampler implements DensitySampler {
		private final BiomeDensitySource biomeDensitySource;
		private final Map<ResourceKey<Biome>, TerrainColumn.TerrainColumnSamplers> compiledSamplers;
		private final DensitySampler offsetSampler;
		private final DensitySampler factorSampler;
		private final double depthScalar;

		private final BiomeDensitySource.DensityData[] horizontalCache = new BiomeDensitySource.DensityData[16 * 16];

		public TerrainRouterSampler(BiomeDensitySource biomeDensitySource, Map<ResourceKey<Biome>, TerrainColumn.TerrainColumnSamplers> compiledSamplers, DensitySampler offsetSampler, DensitySampler factorSampler, double depthScalar) {
			this.biomeDensitySource = biomeDensitySource;
			this.compiledSamplers = compiledSamplers;
			this.offsetSampler = offsetSampler;
			this.factorSampler = factorSampler;
			this.depthScalar = depthScalar;
		}

		@Override
		public void sampleVolume(SamplerContext context, DensityBuffer buffer, DensityVolume volume) {
			DensitySampler.sampleVolumeNaive(context, buffer, volume, this);
		}

		@Override
		public float sampleValue(SamplerContext context, int x, int y, int z) {
			int xInChunk = SectionPos.sectionRelative(x);
			int zInChunk = SectionPos.sectionRelative(z);

			int arrayCoord = zInChunk + (xInChunk << 4);

			BiomeDensitySource.DensityData dataColumn = this.horizontalCache[arrayCoord];

			if (dataColumn == null) {
				dataColumn = this.biomeDensitySource.sampleTerrain(x, z, y, context, this.compiledSamplers);
				this.horizontalCache[arrayCoord] = dataColumn;
			}

			double depth = this.offsetSampler.sampleValue(context, x, y, z) + dataColumn.depth * this.factorSampler.sampleValue(context, x, y, z);
			return (float) (depth + dataColumn.depth);
		}
	}
}