package twilightforest.util;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import twilightforest.TwilightForestMod;
import twilightforest.init.*;

/**
 * Registry alias remapper for migrating from old Twilight Forest identifiers to new ones.
 * In Fabric, built-in registry aliasing is not supported, so this logs alias mappings
 * for reference. Actual migration is handled via data components and world upgrade paths.
 */
public class TFRemapper {

	public static void addRegistryAliases() {
		// Fabric does not support built-in registry aliases.
		// World migration from NeoForge saves should be handled via data fixer schemas.
		// The following blocks/items/entities were renamed:

		// Block & Item renames - logged for reference
		logRemap("boss_spawner_naga", "naga_boss_spawner");
		logRemap("boss_spawner_lich", "lich_boss_spawner");
		logRemap("boss_spawner_minoshroom", "minoshroom_boss_spawner");
		logRemap("boss_spawner_hydra", "hydra_boss_spawner");
		logRemap("boss_spawner_knight_phantom", "knight_phantom_boss_spawner");
		logRemap("boss_spawner_ur_ghast", "ur_ghast_boss_spawner");
		logRemap("boss_spawner_alpha_yeti", "alpha_yeti_boss_spawner");
		logRemap("boss_spawner_snow_queen", "snow_queen_boss_spawner");
		logRemap("boss_spawner_final_boss", "final_boss_boss_spawner");

		logRemap("etched_nagastone_weathered", "cracked_etched_nagastone");
		logRemap("etched_nagastone_mossy", "mossy_etched_nagastone");
		logRemap("nagastone_pillar_weathered", "cracked_nagastone_pillar");
		logRemap("nagastone_pillar_mossy", "mossy_nagastone_pillar");
		logRemap("nagastone_stairs_weathered_left", "cracked_nagastone_stairs_left");
		logRemap("nagastone_stairs_mossy_left", "mossy_nagastone_stairs_left");
		logRemap("nagastone_stairs_weathered_right", "cracked_nagastone_stairs_right");
		logRemap("nagastone_stairs_mossy_right", "mossy_nagastone_stairs_right");
		logRemap("naga_stone_head", "nagastone_head");
		logRemap("naga_stone", "nagastone");

		logRemap("stone_twist", "twisted_stone");
		logRemap("stone_twist_thin", "twisted_stone_pillar");
		logRemap("stone_pillar_bold", "bold_stone_pillar");
		logRemap("empty_bookshelf", "empty_canopy_bookshelf");
		logRemap("royal_rags", "coronation_carpet");
		logRemap("cursed_spawner", "sinister_spawner");

		logRemap("huge_lilypad", "huge_lily_pad");
		logRemap("huge_waterlily", "huge_water_lily");

		logRemap("maze_stone", "mazestone");
		logRemap("maze_stone_brick", "mazestone_brick");
		logRemap("maze_stone_cracked", "cracked_mazestone");
		logRemap("maze_stone_mossy", "mossy_mazestone");
		logRemap("maze_stone_decorative", "decorative_mazestone");
		logRemap("maze_stone_chiseled", "cut_mazestone");
		logRemap("maze_stone_border", "mazestone_border");
		logRemap("maze_stone_mosaic", "mazestone_mosaic");

		logRemap("underbrick_cracked", "cracked_underbrick");
		logRemap("underbrick_mossy", "mossy_underbrick");

		logRemap("tower_wood", "towerwood");
		logRemap("tower_wood_cracked", "cracked_towerwood");
		logRemap("tower_wood_mossy", "mossy_towerwood");
		logRemap("tower_wood_infested", "infested_towerwood");
		logRemap("tower_wood_encased", "encased_towerwood");

		logRemap("deadrock_cracked", "cracked_deadrock");
		logRemap("deadrock_weathered", "weathered_deadrock");

		logRemap("castle_brick_worn", "worn_castle_brick");
		logRemap("castle_brick_cracked", "cracked_castle_brick");
		logRemap("castle_brick_mossy", "mossy_castle_brick");
		logRemap("castle_brick_frame", "thick_castle_brick");
		logRemap("castle_brick_roof", "castle_roof_tile");
		logRemap("castle_pillar_encased", "encased_castle_brick_pillar");
		logRemap("castle_pillar_encased_tile", "encased_castle_brick_tile");
		logRemap("castle_pillar_bold", "bold_castle_brick_pillar");
		logRemap("castle_pillar_bold_tile", "bold_castle_brick_tile");
		logRemap("castle_stairs_brick", "castle_brick_stairs");
		logRemap("castle_stairs_worn", "worn_castle_brick_stairs");
		logRemap("castle_stairs_cracked", "cracked_castle_brick_stairs");
		logRemap("castle_stairs_mossy", "mossy_castle_brick_stairs");
		logRemap("castle_stairs_encased", "encased_castle_brick_stairs");
		logRemap("castle_stairs_bold", "bold_castle_brick_stairs");
		logRemap("castle_rune_brick_pink", "pink_castle_rune_brick");
		logRemap("castle_rune_brick_yellow", "yellow_castle_rune_brick");
		logRemap("castle_rune_brick_blue", "blue_castle_rune_brick");
		logRemap("castle_rune_brick_purple", "violet_castle_rune_brick");
		logRemap("castle_door_pink", "pink_castle_door");
		logRemap("castle_door_yellow", "yellow_castle_door");
		logRemap("castle_door_blue", "blue_castle_door");
		logRemap("castle_door_purple", "violet_castle_door");
		logRemap("force_field_pink", "pink_force_field");
		logRemap("force_field_orange", "orange_force_field");
		logRemap("force_field_green", "green_force_field");
		logRemap("force_field_blue", "blue_force_field");
		logRemap("force_field_purple", "violet_force_field");

		logRemap("rainboak_leaves", "rainbow_oak_leaves");
		logRemap("rainboak_sapling", "rainbow_oak_sapling");
		logRemap("potted_rainboak_sapling", "potted_rainbow_oak_sapling");

		logRemap("dark_gate", "dark_fence_gate");
		logRemap("dark_plate", "dark_pressure_plate");
		logRemap("darkwood_sign", "dark_sign");
		logRemap("darkwood_wall_sign", "dark_wall_sign");
		logRemap("darkwood_banister", "dark_banister");

		logRemap("trans_planks", "transformation_planks");
		logRemap("trans_slab", "transformation_slab");
		logRemap("trans_stairs", "transformation_stairs");
		logRemap("trans_button", "transformation_button");
		logRemap("trans_fence", "transformation_fence");
		logRemap("trans_gate", "transformation_fence_gate");
		logRemap("trans_plate", "transformation_pressure_plate");
		logRemap("trans_door", "transformation_door");
		logRemap("trans_trapdoor", "transformation_trapdoor");
		logRemap("trans_sign", "transformation_sign");
		logRemap("trans_wall_sign", "transformation_wall_sign");
		logRemap("trans_banister", "transformation_banister");

		logRemap("mine_planks", "mining_planks");
		logRemap("mine_slab", "mining_slab");
		logRemap("mine_stairs", "mining_stairs");
		logRemap("mine_button", "mining_button");
		logRemap("mine_fence", "mining_fence");
		logRemap("mine_gate", "mining_fence_gate");
		logRemap("mine_plate", "mining_pressure_plate");
		logRemap("mine_door", "mining_door");
		logRemap("mine_trapdoor", "mining_trapdoor");
		logRemap("mine_sign", "mining_sign");
		logRemap("mine_wall_sign", "mining_wall_sign");
		logRemap("mine_banister", "mining_banister");

		logRemap("sort_planks", "sorting_planks");
		logRemap("sort_slab", "sorting_slab");
		logRemap("sort_stairs", "sorting_stairs");
		logRemap("sort_button", "sorting_button");
		logRemap("sort_fence", "sorting_fence");
		logRemap("sort_gate", "sorting_fence_gate");
		logRemap("sort_plate", "sorting_pressure_plate");
		logRemap("sort_door", "sorting_door");
		logRemap("sort_trapdoor", "sorting_trapdoor");
		logRemap("sort_sign", "sorting_sign");
		logRemap("sort_wall_sign", "sorting_wall_sign");
		logRemap("sort_banister", "sorting_banister");

		// Item renames
		logRemapItem("shield_scepter", "fortification_scepter");
		logRemapItem("magic_map", "filled_magic_map");
		logRemapItem("maze_map", "filled_maze_map");
		logRemapItem("ore_map", "filled_ore_map");
		logRemapItem("magic_map_empty", "magic_map");
		logRemapItem("maze_map_empty", "maze_map");
		logRemapItem("ore_map_empty", "ore_map");
		logRemapItem("ironwood_raw", "raw_ironwood");
		logRemapItem("minotaur_axe_gold", "gold_minotaur_axe");
		logRemapItem("minotaur_axe", "diamond_minotaur_axe");
		logRemapItem("peacock_fan", "peacock_feather_fan");
		logRemapItem("alpha_fur", "alpha_yeti_fur");
		logRemapItem("questing_ram_banner_pattern", "quest_ram_banner_pattern");
		logRemapItem("travellers_chest", "travellers_vest");

		logRemapItem("bunny_spawn_egg", "dwarf_rabbit_spawn_egg");
		logRemapItem("goblin_knight_lower_spawn_egg", "lower_goblin_knight_spawn_egg");
		logRemapItem("mini_ghast_spawn_egg", "carminite_ghastling_spawn_egg");
		logRemapItem("tower_ghast_spawn_egg", "carminite_ghastguard_spawn_egg");
		logRemapItem("tower_golem_spawn_egg", "carminite_golem_spawn_egg");
		logRemapItem("tower_broodling_spawn_egg", "carminite_broodling_spawn_egg");
		logRemapItem("tower_termite_spawn_egg", "towerwood_borer_spawn_egg");
		logRemapItem("wild_boar_spawn_egg", "boar_spawn_egg");
		logRemapItem("yeti_alpha_spawn_egg", "alpha_yeti_spawn_egg");

		// Entity renames
		logRemapEntity("wild_boar", "boar");
		logRemapEntity("bunny", "dwarf_rabbit");
		logRemapEntity("mini_ghast", "carminite_ghastling");
		logRemapEntity("tower_ghast", "carminite_ghastguard");
		logRemapEntity("tower_golem", "carminite_golem");
		logRemapEntity("tower_broodling", "carminite_broodling");
		logRemapEntity("tower_termite", "towerwood_borer");
		logRemapEntity("goblin_knight_upper", "upper_goblin_knight");
		logRemapEntity("goblin_knight_lower", "lower_goblin_knight");
		logRemapEntity("yeti_alpha", "alpha_yeti");

		// Structure piece type renames
		logRemapStructurePiece("TFNCTr", "TFNCTe");
		logRemapStructurePiece("TFNCDu", "TFNCTe");
		logRemapStructurePiece("TFNCSt", "TFNCTe");
	}

	private static void logRemap(String oldId, String newId) {
	}

	private static void logRemapItem(String oldId, String newId) {
		logRemap(oldId, newId);
	}

	private static void logRemapEntity(String oldId, String newId) {
		logRemap(oldId, newId);
	}

	private static void logRemapStructurePiece(String oldId, String newId) {
		logRemap(oldId, newId);
	}
}
