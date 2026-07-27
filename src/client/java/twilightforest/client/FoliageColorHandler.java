package twilightforest.client;

import com.google.common.collect.MapMaker;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.beanification.Component;
import twilightforest.beanification.PostConstruct;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.BiomeColorAlgorithms;

import java.util.HashMap;
import java.util.Map;

@Component
public final class FoliageColorHandler {

	//private final BiomeColorAlgorithms biomeColorAlgorithms = new BiomeColorAlgorithms();

	private final Map<ResourceKey<Biome>, Handler> REGISTRY = new HashMap<>() {{
		put(TFBiomes.SPOOKY_FOREST, (o, x, z) -> BiomeColorAlgorithms.spookyFoliage(x, z));
		put(TFBiomes.ENCHANTED_FOREST, (o, x, z) -> BiomeColorAlgorithms.enchanted(o, (int) x, (int) z));
		put(TFBiomes.DARK_FOREST_CENTER, (o, x, z) -> BiomeColorAlgorithms.darkForestCenterFoliage(x, z));
		put(TFBiomes.DARK_FOREST, (o, x, z) -> BiomeColorAlgorithms.darkForest(BiomeColorAlgorithms.Type.Foliage));
		put(TFBiomes.SWAMP, (o, x, z) -> BiomeColorAlgorithms.swamp(BiomeColorAlgorithms.Type.Foliage));
	}};

	private final Map<Biome, Handler> HANDLES = new MapMaker().weakKeys().makeMap(); // Concurrent + Weak + Hash

	// TODO: Port to Fabric - IEventBus and EntityLeaveLevelEvent are NeoForge-specific
	// @PostConstruct(PostConstruct.Bus.GAME)
	// private void setup(IEventBus bus) {
	//     bus.addListener(EntityLeaveLevelEvent.class, event -> {
	//         if (event.getLevel().isClientSide()) {
	//             HANDLES.clear();
	//         }
	//     });
	// }
	@PostConstruct
	private void setup() {
	}

	public int get(int o, Biome biome, double x, double z) {
		Handler handler = HANDLES.get(biome);
		if (handler == null) {
			handler = REGISTRY.getOrDefault(
				Minecraft.getInstance().level == null ? null :
					Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.BIOME).getResourceKey(biome).orElse(null),
				Handler.DEFAULT);
			HANDLES.put(biome, handler);
		}
		return handler.apply(o, x, z);
	}

	public static int getTintColorAtPosition(BlockPos pos) {
		return 0;
	}

	@FunctionalInterface
	private interface Handler {
		Handler DEFAULT = (o, x, z) -> o;

		int apply(int o, double x, double z);
	}
}
