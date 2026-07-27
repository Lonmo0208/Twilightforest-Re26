package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.layer.vanillalegacy.SmoothLayer;
import twilightforest.world.components.layer.vanillalegacy.ZoomLayer;
import twilightforest.world.components.layer.*;

public class BiomeLayerTypes {
	public static final Codec<BiomeLayerType> CODEC = Codec.lazyInitialized(TFRegistries.BIOME_LAYER_TYPE::byNameCodec);

	public static final BiomeLayerType RANDOM_BIOMES = () -> RandomBiomeLayer.Factory.CODEC;
	public static final BiomeLayerType KEY_BIOMES = () -> KeyBiomesLayer.Factory.CODEC;
	public static final BiomeLayerType COMPANION_BIOMES = () -> CompanionBiomesLayer.Factory.CODEC;
	public static final BiomeLayerType ZOOM = () -> ZoomLayer.Factory.CODEC;
	public static final BiomeLayerType STABILIZE = () -> StabilizeLayer.Factory.CODEC;
	public static final BiomeLayerType BORDER = () -> BorderLayer.Factory.CODEC;
	public static final BiomeLayerType SEAM = () -> SeamLayer.Factory.CODEC;
	public static final BiomeLayerType SMOOTH = () -> SmoothLayer.Factory.CODEC;
	public static final BiomeLayerType FILTERED = () -> FilteredBiomeLayer.Factory.CODEC;
	public static final BiomeLayerType MEDIAN = () -> MedianLayer.Factory.CODEC;

	public static void init() {
		Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix("random_biomes"), RANDOM_BIOMES);
		Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix("key_biomes"), KEY_BIOMES);
		Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix("companion_biomes"), COMPANION_BIOMES);
		Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix("zoom"), ZOOM);
		Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix("stabilize"), STABILIZE);
		Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix("border"), BORDER);
		Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix("seam"), SEAM);
		Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix("smooth"), SMOOTH);
		Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix("filtered"), FILTERED);
		Registry.register(TFRegistries.BIOME_LAYER_TYPE, TwilightForestMod.prefix("median"), MEDIAN);
	}
}