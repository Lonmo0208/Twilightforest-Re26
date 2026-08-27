package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.doubles.Double2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.codec.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import twilightforest.util.Codecs;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class TerrainColumn {
	public static final Codec<TerrainColumn> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			RegistryFixedCodec.create(Registries.BIOME).fieldOf("key_biome").forGetter(o -> o.keyBiome),
			Codecs.doubleTreeCodec(RegistryFixedCodec.create(Registries.BIOME)).fieldOf("biome_layers").forGetter(o -> o.biomes),
			DensityFunction.CODEC.fieldOf("depth").forGetter(o -> o.noiseDepth),
			DensityFunction.CODEC.fieldOf("scale").forGetter(o -> o.noiseScale),
			DensityFunction.CODEC.fieldOf("weight").forGetter(o -> o.noiseWeight)
		).apply(instance, TerrainColumn::new));
	private final ResourceKey<Biome> resourceKey;
	private final Holder<Biome> keyBiome;
	private final Double2ObjectSortedMap<Holder<Biome>> biomes;

	private final DensityFunction noiseDepth, noiseScale, noiseWeight;

	public TerrainColumn(Holder<Biome> keyBiome, Double2ObjectSortedMap<Holder<Biome>> biomes, DensityFunction noiseDepth, DensityFunction noiseScale, DensityFunction noiseWeight) {
		this.keyBiome = keyBiome;
		this.resourceKey = this.keyBiome.unwrapKey().orElseThrow(
			() -> new IllegalArgumentException("TerrainColumn keyBiome must be a Holder.Reference with a valid ResourceKey, got: " + keyBiome.getClass().getName())
		);
		this.biomes = biomes;
		this.noiseDepth = noiseDepth;
		this.noiseScale = noiseScale;
		this.noiseWeight = noiseWeight;

		if (this.biomes instanceof Double2ObjectAVLTreeMap<Holder<Biome>> treeMap)
			treeMap.defaultReturnValue(this.keyBiome);
	}

	public Stream<Holder<Biome>> getBiomes() {
		return this.biomes.double2ObjectEntrySet().stream().map(Map.Entry::getValue);
	}

	public void getBiomesDebug(Consumer<String> accumulator) {
		this.biomes.double2ObjectEntrySet()
			.stream()
			.map(e -> e.getDoubleKey() + ": " + e.getValue().unwrapKey().map(ResourceKey::identifier).map(Identifier::toString).orElse("NOT REFERENCED"))
			.forEach(accumulator);
	}

	public boolean is(Holder<Biome> biome) {
		return this.keyBiome.value().equals(biome.value());
	}

	public boolean is(ResourceKey<Biome> biome) {
		return this.keyBiome.is(biome);
	}

	public Holder<Biome> getMainBiome() {
		return this.keyBiome;
	}

	public Holder<Biome> getBiome(int biomeElevationQuartile) {
		return this.reduce((a, b) -> {
			double aDelta = a.getDoubleKey() - biomeElevationQuartile;
			double bDelta = b.getDoubleKey() - biomeElevationQuartile;

			return Math.abs(aDelta) <= Math.abs(bDelta) ? a : b;
		}, this.keyBiome);
	}

	private Holder<Biome> reduce(BinaryOperator<Double2ObjectMap.Entry<Holder<Biome>>> reducer, Holder<Biome> other) {
		Double2ObjectMap.Entry<Holder<Biome>> result = null;
		for (Double2ObjectMap.Entry<Holder<Biome>> entry : this.biomes.double2ObjectEntrySet()) {
			if (result == null) {
				result = entry;
			} else {
				result = reducer.apply(result, entry);
			}
		}
		return result != null ? result.getValue() : other;
	}

	public double depth(TerrainColumnSamplers samplers, SamplerContext context, int x, int y, int z) {
		return samplers.depth().sampleValue(context, x, y, z);
	}

	public double scale(TerrainColumnSamplers samplers, SamplerContext context, int x, int y, int z) {
		return samplers.scale().sampleValue(context, x, y, z);
	}

	public double weight(TerrainColumnSamplers samplers, SamplerContext context, int x, int y, int z) {
		return samplers.weight().sampleValue(context, x, y, z);
	}

	public TerrainColumnSamplers compileSamplers(DensityFunction.CompileContext compileContext) {
		return new TerrainColumnSamplers(
			this.noiseDepth.compileSampler(compileContext),
			this.noiseScale.compileSampler(compileContext),
			this.noiseWeight.compileSampler(compileContext)
		);
	}

	/**
	 * Compiled, ready-to-sample versions of this column's depth/scale/weight density functions.
	 */
	public record TerrainColumnSamplers(DensitySampler depth, DensitySampler scale, DensitySampler weight) {}

	public ResourceKey<Biome> getResourceKey() {
		return this.resourceKey;
	}
}
