package twilightforest.util.datamaps;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.StrictJsonParser;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataMaps;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Fabric-compatible reload listener that loads NeoForge-format data map JSON files
 * and populates the corresponding DataMapType instances.
 * <p>
 * Reads JSON files from: data/&lt;namespace&gt;/data_maps/&lt;registry_path&gt;/&lt;data_map_name&gt;.json
 */
public class DataMapReloadListener implements IdentifiableResourceReloadListener {

	private static final FileToIdConverter LISTER = FileToIdConverter.json("data_maps");

	@Override
	public Identifier getFabricId() {
		return Identifier.fromNamespaceAndPath(TwilightForestMod.ID, "data_maps");
	}

	@Override
	public CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
		return CompletableFuture.supplyAsync(() -> {
			loadFromResourceManager(currentReload.resourceManager());
			return null;
		}, taskExecutor).thenCompose(preparationBarrier::wait).thenAcceptAsync(v -> {}, reloadExecutor);
	}

	/**
	 * Manually load data maps from the given ResourceManager.
	 * Called via ServerLifecycleEvents.SERVER_STARTED and reload() to ensure data maps are loaded
	 * before world generation and during resource reloads.
	 */
	public static void loadFromResourceManager(ResourceManager manager) {
		// Clear all existing data maps before reloading
		for (DataMapType<?, ?> dataMapType : TFDataMaps.ALL_DATA_MAPS) {
			clearDataMap(dataMapType);
		}

		// Read all JSON files from data_maps directory
		Map<Identifier, JsonElement> entries = new HashMap<>();
		for (var entry : LISTER.listMatchingResources(manager).entrySet()) {
			Identifier location = entry.getKey();
			Identifier id = LISTER.fileToId(location);
			try (Reader reader = entry.getValue().openAsReader()) {
				JsonElement json = StrictJsonParser.parse(reader);
				entries.put(id, json);
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("DataMapReloadListener: Failed to read {}: {}", location, e.getMessage());
			}
		}

		for (DataMapType<?, ?> dataMapType : TFDataMaps.ALL_DATA_MAPS) {
			loadDataMap(dataMapType, entries);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static void clearDataMap(DataMapType<?, ?> dataMapType) {
		((DataMapType) dataMapType).clear();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <R, T> void loadDataMap(DataMapType<R, T> dataMapType, Map<Identifier, JsonElement> allEntries) {
		// Build the expected path key: data_maps/<registry_path>/<data_map_name>
		ResourceKey<Registry<R>> registryKey = dataMapType.registryKey();
		String registryPath = registryKey.identifier().getPath();
		Identifier dataMapId = dataMapType.id();

		// The key in the entries map is: <namespace>:<registry_path>/<data_map_name>
		String keyPath = registryPath + "/" + dataMapId.getPath();
		Identifier fullKey = Identifier.fromNamespaceAndPath(dataMapId.getNamespace(), keyPath);

		JsonElement json = allEntries.get(fullKey);
		if (json == null) {
			return;
		}

		if (!json.isJsonObject()) {
			TwilightForestMod.LOGGER.error("DataMapReloadListener: Invalid JSON for {} - expected object", dataMapId);
			return;
		}

		JsonObject root = json.getAsJsonObject();
		if (!root.has("values")) {
			TwilightForestMod.LOGGER.error("DataMapReloadListener: Missing 'values' key in {} data map", dataMapId);
			return;
		}

		JsonObject values = root.getAsJsonObject("values");

		for (Map.Entry<String, JsonElement> entry : values.entrySet()) {
			String key = entry.getKey();
			JsonElement value = entry.getValue();

			// Skip tag-based keys (e.g., "#minecraft:coal_ores") - not yet supported in Fabric port
			if (key.startsWith("#")) {
				continue;
			}

			Identifier parsedKey = Identifier.parse(key);
			ResourceKey<R> resourceKey = ResourceKey.create(registryKey, parsedKey);

			DataResult<T> result = dataMapType.codec().parse(JsonOps.INSTANCE, value);
			result.resultOrPartial(error -> {
				TwilightForestMod.LOGGER.warn("DataMapReloadListener: Failed to parse {} entry '{}': {}", dataMapId, key, error);
			}).ifPresent(parsed -> {
				((DataMapType) dataMapType).add(resourceKey, parsed);
			});
		}
	}
}