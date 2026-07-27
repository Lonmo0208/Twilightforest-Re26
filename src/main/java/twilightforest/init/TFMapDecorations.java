package twilightforest.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.TwilightForestMod;
import net.minecraft.core.Registry;

public class TFMapDecorations {
	public static final MapDecorationType HEDGE_MAZE = new MapDecorationType(TwilightForestMod.prefix("hedge_maze"), true, -1, false, true);
	public static final MapDecorationType SMALL_HOLLOW_HILL = new MapDecorationType(TwilightForestMod.prefix("small_hollow_hill"), true, -1, false, true);
	public static final MapDecorationType MEDIUM_HOLLOW_HILL = new MapDecorationType(TwilightForestMod.prefix("medium_hollow_hill"), true, -1, false, true);
	public static final MapDecorationType LARGE_HOLLOW_HILL = new MapDecorationType(TwilightForestMod.prefix("large_hollow_hill"), true, -1, false, true);
	public static final MapDecorationType QUEST_GROVE = new MapDecorationType(TwilightForestMod.prefix("quest_grove"), true, -1, false, true);
	public static final MapDecorationType NAGA_COURTYARD = new MapDecorationType(TwilightForestMod.prefix("naga_courtyard"), true, -1, false, true);
	public static final MapDecorationType LICH_TOWER = new MapDecorationType(TwilightForestMod.prefix("lich_tower"), true, -1, false, true);
	public static final MapDecorationType LABYRINTH = new MapDecorationType(TwilightForestMod.prefix("labyrinth"), true, -1, false, true);
	public static final MapDecorationType HYDRA_LAIR = new MapDecorationType(TwilightForestMod.prefix("hydra_lair"), true, -1, false, true);
	public static final MapDecorationType KNIGHT_STRONGHOLD = new MapDecorationType(TwilightForestMod.prefix("knight_stronghold"), true, -1, false, true);
	public static final MapDecorationType DARK_TOWER = new MapDecorationType(TwilightForestMod.prefix("dark_tower"), true, -1, false, true);
	public static final MapDecorationType YETI_LAIR = new MapDecorationType(TwilightForestMod.prefix("yeti_lair"), true, -1, false, true);
	public static final MapDecorationType AURORA_PALACE = new MapDecorationType(TwilightForestMod.prefix("aurora_palace"), true, -1, false, true);
	public static final MapDecorationType TROLL_CAVES = new MapDecorationType(TwilightForestMod.prefix("troll_caves"), true, -1, false, true);
	public static final MapDecorationType FINAL_CASTLE = new MapDecorationType(TwilightForestMod.prefix("final_castle"), true, -1, false, true);

	public static void init() {
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("hedge_maze"), HEDGE_MAZE);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("small_hollow_hill"), SMALL_HOLLOW_HILL);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("medium_hollow_hill"), MEDIUM_HOLLOW_HILL);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("large_hollow_hill"), LARGE_HOLLOW_HILL);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("quest_grove"), QUEST_GROVE);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("naga_courtyard"), NAGA_COURTYARD);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("lich_tower"), LICH_TOWER);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("labyrinth"), LABYRINTH);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("hydra_lair"), HYDRA_LAIR);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("knight_stronghold"), KNIGHT_STRONGHOLD);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("dark_tower"), DARK_TOWER);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("yeti_lair"), YETI_LAIR);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("aurora_palace"), AURORA_PALACE);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("troll_caves"), TROLL_CAVES);
		Registry.register(BuiltInRegistries.MAP_DECORATION_TYPE, TwilightForestMod.prefix("final_castle"), FINAL_CASTLE);
	}

}