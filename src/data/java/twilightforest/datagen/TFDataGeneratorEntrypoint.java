package twilightforest.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import twilightforest.TFRegistries;
import twilightforest.init.*;
import twilightforest.init.custom.*;

public class TFDataGeneratorEntrypoint implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(TFWorldgenProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder
			// Order matters: RegistrySetBuilder applies entries in add() order, so dependencies must be registered first.
			.add(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack::bootstrap)
			.add(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeLayerStack::bootstrapData)
			.add(Registries.BIOME, TFBiomes::bootstrap)
			.add(Registries.DENSITY_FUNCTION, TFDensityFunctions::bootstrap)
			.add(Registries.NOISE_SETTINGS, TFDimensionData::bootstrapNoise)
			.add(Registries.DIMENSION_TYPE, TFDimensionData::bootstrapType)
			.add(Registries.LEVEL_STEM, TFDimensionData::bootstrapStem)
			.add(Registries.FEATURE, TFConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, TFPlacedFeatures::bootstrap)
			.add(Registries.STRUCTURE, TFStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, TFStructureSets::bootstrap)
			.add(Registries.CARVER, TFCaveCarvers::bootstrap)
			.add(Registries.DAMAGE_TYPE, TFDamageTypes::bootstrap)
			.add(Registries.TRIM_MATERIAL, TFTrimMaterials::bootstrap)
			.add(Registries.BANNER_PATTERN, TFBannerPatterns::bootstrap)
			.add(Registries.JUKEBOX_SONG, TFJukeboxSongs::bootstrap)
			.add(Registries.ENCHANTMENT, TFEnchantments::bootstrap)
			.add(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfigs::bootstrap)
			.add(TFRegistries.Keys.WOOD_PALETTES, WoodPalettes::bootstrap)
			.add(TFRegistries.Keys.RESTRICTIONS, Restrictions::bootstrap)
			.add(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariants::bootstrap)
			.add(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, ChunkBlanketProcessors::bootstrap)
			.add(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DwarfRabbitVariants::bootstrap)
			.add(TFRegistries.Keys.TINY_BIRD_VARIANT, TinyBirdVariants::bootstrap)
			.add(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TravellersModifiersManager::bootstrap)
			.add(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TemplateMarkerHandlers::bootstrap);
	}
}
