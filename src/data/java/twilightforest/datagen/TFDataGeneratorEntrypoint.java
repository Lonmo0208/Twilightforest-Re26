package twilightforest.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import twilightforest.TFRegistries;
import twilightforest.datagen.data.tags.*;
import twilightforest.init.*;
import twilightforest.init.custom.*;

public class TFDataGeneratorEntrypoint implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Worldgen data
		pack.addProvider(TFWorldgenProvider::new);

		// Tags
		pack.addProvider(BannerPatternTagGenerator::new);
		pack.addProvider(BiomeTagGenerator::new);
		pack.addProvider(BlockEntityTypeTagGenerator::new);
		pack.addProvider(BlockTagGenerator::new);
		pack.addProvider(DamageTypeTagGenerator::new);
		pack.addProvider(DimensionTypeTagGenerator::new);
		pack.addProvider(EntityTypeTagGenerator::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(PaintingVariantTagGenerator::new);
		pack.addProvider(StructureTagGenerator::new);
		pack.addProvider(WoodPaletteTagGenerator::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		// Vanilla Minecraft registries
		registryBuilder
			.add(Registries.CONFIGURED_FEATURE, TFConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, TFPlacedFeatures::bootstrap)
			.add(Registries.STRUCTURE, TFStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, TFStructureSets::bootstrap)
			.add(Registries.CONFIGURED_CARVER, TFCaveCarvers::bootstrap)
			.add(Registries.DENSITY_FUNCTION, TFDensityFunctions::bootstrap)
			.add(Registries.NOISE_SETTINGS, TFDimensionData::bootstrapNoise)
			.add(Registries.DIMENSION_TYPE, TFDimensionData::bootstrapType)
			.add(Registries.LEVEL_STEM, TFDimensionData::bootstrapStem)
			.add(Registries.BIOME, TFBiomes::bootstrap)
			.add(Registries.DAMAGE_TYPE, TFDamageTypes::bootstrap)
			.add(Registries.TRIM_MATERIAL, TFTrimMaterials::bootstrap)
			.add(Registries.BANNER_PATTERN, TFBannerPatterns::bootstrap)
			.add(Registries.JUKEBOX_SONG, TFJukeboxSongs::bootstrap)
			.add(Registries.ENCHANTMENT, TFEnchantments::bootstrap)
		// Custom registries (twilightforest:*) - needed for cross-references during bootstrap
			.add(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfigs::bootstrap)
			.add(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack::bootstrap)
			.add(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeLayerStack::bootstrapData)
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