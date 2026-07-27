package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.datamaps.DataMapType;
import twilightforest.TwilightForestMod;
import twilightforest.util.datamaps.CrumbledBlock;
import twilightforest.util.datamaps.EntityTransformation;
import twilightforest.util.datamaps.MagicMapBiomeColor;
import twilightforest.util.datamaps.OreMapOreColor;

import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TFDataMaps {

	public static final DataMapType<EntityType<?>, EntityTransformation> TRANSFORMATION_POWDER = DataMapType.builder(
		TwilightForestMod.prefix("transformation_powder"), Registries.ENTITY_TYPE, EntityTransformation.CODEC).synced(EntityTransformation.CODEC, false).build();

	public static final DataMapType<EntityType<?>, EntityTransformation> OMINOUS_FIRE = DataMapType.builder(
		TwilightForestMod.prefix("ominous_fire"), Registries.ENTITY_TYPE, EntityTransformation.CODEC).synced(EntityTransformation.CODEC, false).build();

	public static final DataMapType<Block, CrumbledBlock> CRUMBLE_HORN = DataMapType.builder(
		TwilightForestMod.prefix("crumble_horn"), Registries.BLOCK, CrumbledBlock.CODEC).synced(CrumbledBlock.CODEC, false).build();

	// Data-driven data map for magic map biome colors (JSON-based, loaded via DataMapReloadListener).
	// This is kept for mod compatibility (other mods can add biome colors via JSON).
	public static final DataMapType<Biome, MagicMapBiomeColor> MAGIC_MAP_BIOME_COLOR = DataMapType.builder(
		TwilightForestMod.prefix("magic_map_color"), Registries.BIOME, MagicMapBiomeColor.CODEC).synced(MagicMapBiomeColor.CODEC, false).build();

	public static final DataMapType<Block, OreMapOreColor> ORE_MAP_ORE_COLOR = DataMapType.builder(
		TwilightForestMod.prefix("ore_map_color"), Registries.BLOCK, OreMapOreColor.CODEC).synced(OreMapOreColor.CODEC, false).build();

	/**
	 * All registered data map types for the reload listener to iterate over.
	 */
	public static final List<DataMapType<?, ?>> ALL_DATA_MAPS = List.of(
		TRANSFORMATION_POWDER,
		OMINOUS_FIRE,
		CRUMBLE_HORN,
		MAGIC_MAP_BIOME_COLOR,
		ORE_MAP_ORE_COLOR
	);

	// Static hardcoded biome color map, populated at class initialization time.
	// Uses TFBiomes ResourceKeys as keys for reliable lookup.
	private static final Map<ResourceKey<Biome>, MagicMapBiomeColor> MAGIC_MAP_BIOME_COLORS = Map.ofEntries(
		Map.entry(TFBiomes.FOREST, new MagicMapBiomeColor(MapColor.PLANT, 1)),
		Map.entry(TFBiomes.DENSE_FOREST, new MagicMapBiomeColor(MapColor.PLANT, 0)),
		Map.entry(TFBiomes.LAKE, new MagicMapBiomeColor(MapColor.WATER, 3)),
		Map.entry(TFBiomes.STREAM, new MagicMapBiomeColor(MapColor.WATER, 1)),
		Map.entry(TFBiomes.SWAMP, new MagicMapBiomeColor(MapColor.DIAMOND, 3)),
		Map.entry(TFBiomes.FIRE_SWAMP, new MagicMapBiomeColor(MapColor.NETHER, 1)),
		Map.entry(TFBiomes.CLEARING, new MagicMapBiomeColor(MapColor.GRASS, 2)),
		Map.entry(TFBiomes.OAK_SAVANNAH, new MagicMapBiomeColor(MapColor.GRASS, 0)),
		Map.entry(TFBiomes.HIGHLANDS, new MagicMapBiomeColor(MapColor.DIRT, 0)),
		Map.entry(TFBiomes.THORNLANDS, new MagicMapBiomeColor(MapColor.WOOD, 3)),
		Map.entry(TFBiomes.FINAL_PLATEAU, new MagicMapBiomeColor(MapColor.COLOR_LIGHT_GRAY, 2)),
		Map.entry(TFBiomes.FIREFLY_FOREST, new MagicMapBiomeColor(MapColor.EMERALD, 1)),
		Map.entry(TFBiomes.DARK_FOREST, new MagicMapBiomeColor(MapColor.COLOR_GREEN, 3)),
		Map.entry(TFBiomes.DARK_FOREST_CENTER, new MagicMapBiomeColor(MapColor.COLOR_ORANGE, 3)),
		Map.entry(TFBiomes.SNOWY_FOREST, new MagicMapBiomeColor(MapColor.SNOW, 1)),
		Map.entry(TFBiomes.GLACIER, new MagicMapBiomeColor(MapColor.ICE, 1)),
		Map.entry(TFBiomes.MUSHROOM_FOREST, new MagicMapBiomeColor(MapColor.COLOR_ORANGE, 0)),
		Map.entry(TFBiomes.DENSE_MUSHROOM_FOREST, new MagicMapBiomeColor(MapColor.COLOR_PINK, 0)),
		Map.entry(TFBiomes.ENCHANTED_FOREST, new MagicMapBiomeColor(MapColor.COLOR_CYAN, 2)),
		Map.entry(TFBiomes.SPOOKY_FOREST, new MagicMapBiomeColor(MapColor.COLOR_PURPLE, 0))
	);

	// Identifier-keyed lookup map for fast O(1) lookup by biome identifier.
	private static final Map<Identifier, MagicMapBiomeColor> MAGIC_MAP_BIOME_COLORS_BY_ID = MAGIC_MAP_BIOME_COLORS.entrySet().stream()
		.collect(Collectors.toUnmodifiableMap(e -> e.getKey().identifier(), Map.Entry::getValue));

	/**
	 * Convenience method to get magic map biome color for a biome holder.
	 * Uses the static hardcoded map first, then falls back to data-driven JSON data maps.
	 * Returns null only if the biome is completely unknown.
	 */
	@Nullable
	public static MagicMapBiomeColor getMagicMapBiomeColor(Holder<Biome> biome) {
		Optional<ResourceKey<Biome>> key = biome.unwrapKey();
		if (key.isPresent()) {
			MagicMapBiomeColor color = MAGIC_MAP_BIOME_COLORS_BY_ID.get(key.get().identifier());
			if (color != null) return color;
		}

		// Fall back to data-driven JSON data maps for mod compatibility
		return DataMapType.getData(biome, MAGIC_MAP_BIOME_COLOR);
	}

	/**
	 * Resolves a Holder<Biome> to its ResourceKey. Uses unwrapKey().
	 * After TerrainColumn fix, all TF biomes should be Holder.Reference with valid keys.
	 */
	public static Optional<ResourceKey<Biome>> getBiomeKey(Holder<Biome> biome) {
		return biome.unwrapKey();
	}

	/**
	 * Expose the hardcoded biome color map for data-driven system to reference.
	 */
	public static Map<ResourceKey<Biome>, MagicMapBiomeColor> magicMapBiomeColorEntries() {
		return MAGIC_MAP_BIOME_COLORS;
	}
}
