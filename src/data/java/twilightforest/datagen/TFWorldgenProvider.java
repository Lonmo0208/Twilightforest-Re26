package twilightforest.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import twilightforest.TFRegistries;

public class TFWorldgenProvider extends FabricDynamicRegistryProvider {
	public TFWorldgenProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		entries.addAll(registries.lookupOrThrow(Registries.FEATURE));
		entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
		entries.addAll(registries.lookupOrThrow(Registries.STRUCTURE));
		entries.addAll(registries.lookupOrThrow(Registries.STRUCTURE_SET));
		entries.addAll(registries.lookupOrThrow(Registries.CARVER));
		entries.addAll(registries.lookupOrThrow(Registries.DENSITY_FUNCTION));
		entries.addAll(registries.lookupOrThrow(Registries.NOISE_SETTINGS));
		entries.addAll(registries.lookupOrThrow(Registries.DIMENSION_TYPE));
		entries.addAll(registries.lookupOrThrow(Registries.LEVEL_STEM));
		entries.addAll(registries.lookupOrThrow(Registries.BIOME));
		entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE));
		entries.addAll(registries.lookupOrThrow(Registries.TRIM_MATERIAL));
		entries.addAll(registries.lookupOrThrow(Registries.BANNER_PATTERN));
		entries.addAll(registries.lookupOrThrow(Registries.JUKEBOX_SONG));
		entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));

		addAllRegistryEntries(registries, entries, TFRegistries.Keys.BIOME_STACK);
		addAllRegistryEntries(registries, entries, TFRegistries.Keys.BIOME_TERRAIN_DATA);
		addAllRegistryEntries(registries, entries, TFRegistries.Keys.WOOD_PALETTES);
		addAllRegistryEntries(registries, entries, TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS);
		addAllRegistryEntries(registries, entries, TFRegistries.Keys.RESTRICTIONS);
		addAllRegistryEntries(registries, entries, TFRegistries.Keys.MAGIC_PAINTINGS);
		addAllRegistryEntries(registries, entries, TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS);
		addAllRegistryEntries(registries, entries, TFRegistries.Keys.DWARF_RABBIT_VARIANT);
		addAllRegistryEntries(registries, entries, TFRegistries.Keys.TINY_BIRD_VARIANT);
		addAllRegistryEntries(registries, entries, TFRegistries.Keys.TRAVELLERS_MODIFIERS);
		addAllRegistryEntries(registries, entries, TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST);
	}

	private static <T> void addAllRegistryEntries(HolderLookup.Provider registries, Entries entries, ResourceKey<Registry<T>> registryKey) {
		var lookup = registries.lookupOrThrow(registryKey);
		lookup.listElementIds().forEach(key -> entries.add(lookup, key));
	}

	@Override
	public String getName() {
		return "Twilight Forest Worldgen";
	}
}
