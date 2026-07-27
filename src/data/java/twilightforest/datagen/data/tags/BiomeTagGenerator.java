package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.TFBiomes;
import twilightforest.tags.TFBiomeTags;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

public class BiomeTagGenerator extends FabricTagsProvider<Biome> {

	public BiomeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.BIOME, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		this.builder(TFBiomeTags.IS_TWILIGHT).add(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST,
			TFBiomes.ENCHANTED_FOREST, TFBiomes.DENSE_MUSHROOM_FOREST,
			TFBiomes.LAKE, TFBiomes.STREAM, TFBiomes.UNDERGROUND,
			TFBiomes.SWAMP, TFBiomes.FIRE_SWAMP,
			TFBiomes.DARK_FOREST, TFBiomes.DARK_FOREST_CENTER,
			TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER,
			TFBiomes.HIGHLANDS, TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU
		);

		this.builder(TFBiomeTags.VALID_QUEST_GROVE_BIOMES).add(TFBiomes.ENCHANTED_FOREST);
		this.builder(TFBiomeTags.VALID_MUSHROOM_TOWER_BIOMES).add(TFBiomes.DENSE_MUSHROOM_FOREST);

		this.builder(TFBiomeTags.VALID_CAMP_BIOMES).add(
			TFBiomes.OAK_SAVANNAH, TFBiomes.CLEARING, TFBiomes.MUSHROOM_FOREST, TFBiomes.FOREST, TFBiomes.FIREFLY_FOREST
		);

		this.builder(TFBiomeTags.VALID_HOLLOW_TREE_BIOMES).add(
			TFBiomes.DENSE_FOREST, TFBiomes.FIRE_SWAMP,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.ENCHANTED_FOREST
		);
		this.builder(TFBiomeTags.VALID_HEDGE_MAZE_BIOMES).add(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		this.builder(TFBiomeTags.VALID_HOLLOW_HILL_BIOMES).add(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		this.builder(TFBiomeTags.VALID_NAGA_COURTYARD_BIOMES).add(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		this.builder(TFBiomeTags.VALID_LICH_TOWER_BIOMES).add(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		);
		this.builder(TFBiomeTags.VALID_LABYRINTH_BIOMES).add(TFBiomes.SWAMP);
		this.builder(TFBiomeTags.VALID_HYDRA_LAIR_BIOMES).add(TFBiomes.FIRE_SWAMP);
		this.builder(TFBiomeTags.VALID_KNIGHT_STRONGHOLD_BIOMES).add(TFBiomes.DARK_FOREST);
		this.builder(TFBiomeTags.VALID_DARK_TOWER_BIOMES).add(TFBiomes.DARK_FOREST_CENTER);
		this.builder(TFBiomeTags.VALID_YETI_CAVE_BIOMES).add(TFBiomes.SNOWY_FOREST);
		this.builder(TFBiomeTags.VALID_AURORA_PALACE_BIOMES).add(TFBiomes.GLACIER);
		this.builder(TFBiomeTags.VALID_TROLL_CAVE_BIOMES).add(TFBiomes.HIGHLANDS);
		this.builder(TFBiomeTags.VALID_GIANT_HOUSE_BIOMES).add(TFBiomes.HIGHLANDS);
		this.builder(TFBiomeTags.VALID_FINAL_CASTLE_BIOMES).add(TFBiomes.FINAL_PLATEAU);

		//other vanilla tags
		this.builder(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).addTag(TFBiomeTags.IS_TWILIGHT);
		this.builder(BiomeTags.WITHOUT_ZOMBIE_SIEGES).addTag(TFBiomeTags.IS_TWILIGHT);

		//even though we won't spawn vanilla frogs, we'll still add support for the variants
		this.builder(BiomeTags.SPAWNS_COLD_VARIANT_FROGS).add(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER);
		this.builder(BiomeTags.SPAWNS_WARM_VARIANT_FROGS).add(TFBiomes.OAK_SAVANNAH, TFBiomes.FIRE_SWAMP);

		this.builder(BiomeTags.SPAWNS_SNOW_FOXES).add(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER);
		this.builder(BiomeTags.SPAWNS_WHITE_RABBITS).add(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER);
	}

	@Override
	public String getName() {
		return "Twilight Forest Biome Tags";
	}
}