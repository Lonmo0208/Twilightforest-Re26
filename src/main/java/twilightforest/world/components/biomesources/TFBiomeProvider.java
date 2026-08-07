package twilightforest.world.components.biomesources;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.world.components.layer.BiomeDensitySource;

import java.util.List;
import java.util.stream.Stream;

public class TFBiomeProvider extends BiomeSource {
	private static final Logger LOGGER = LoggerFactory.getLogger(TFBiomeProvider.class);

	public static final MapCodec<TFBiomeProvider> TF_CODEC = BiomeDensitySource.CODEC.xmap(
		ds -> new TFBiomeProvider(Holder.direct(ds)),
		TFBiomeProvider::getBiomeConfig
	).fieldOf("terrain_data");

	private final Holder<BiomeDensitySource> biomeTerrainDataHolder;

	public TFBiomeProvider(Holder<BiomeDensitySource> biomeTerrainDataHolder) {
		super();

		this.biomeTerrainDataHolder = biomeTerrainDataHolder;
	}

	private BiomeDensitySource getBiomeConfig() {
		return this.biomeTerrainDataHolder.value();
	}

	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		return this.biomeTerrainDataHolder.value().collectPossibleBiomes();
	}

	@Override
	protected MapCodec<? extends BiomeSource> codec() {
		return TF_CODEC;
	}

	@Override
	public Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ, Climate.Sampler sampler) {
		return this.biomeTerrainDataHolder.value().getNoiseBiome(biomeX, biomeY, biomeZ);
	}

	public Holder<Biome> getMainBiome(int biomeX, int biomeZ) {
		return this.biomeTerrainDataHolder.value().getBiomeColumnKey(biomeX, biomeZ);
	}

	/**
	 * Returns the actual 3D biome at the given block coordinates, accounting for
	 * height-based biome variants (e.g. underground, peak, sky layers).
	 * Block coordinates are converted to biome/quartile units internally.
	 */
	public Holder<Biome> getBiomeAtBlock(int blockX, int blockY, int blockZ) {
		int biomeX = blockX >> 2;
		int biomeY = blockY >> 2;
		int biomeZ = blockZ >> 2;
		return this.biomeTerrainDataHolder.value().getNoiseBiome(biomeX, biomeY, biomeZ);
	}

	@Deprecated
	public BiomeDensitySource getBiomeTerrain() {
		return this.biomeTerrainDataHolder.value();
	}

	@Override
	public void addDebugInfo(List<String> info, BlockPos cameraPos, Climate.Sampler sampler) {
		super.addDebugInfo(info, cameraPos, sampler);

		this.biomeTerrainDataHolder.value().addDebugInfo(info, cameraPos);
	}
}
