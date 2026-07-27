package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.type.*;



public class TFStructureTypes {
	public static final StructureType<LandmarkWrappedStructure> LANDMARK_WRAPPED = () -> LandmarkWrappedStructure.CODEC;
	public static final StructureType<ProgressionWrappedStructure> PROGRESSION_WRAPPED = () -> ProgressionWrappedStructure.CODEC;
	public static final StructureType<FallenTrunkStructure> FALLEN_TRUNK = () -> FallenTrunkStructure.CODEC;
	public static final StructureType<HollowTreeStructure> HOLLOW_TREE = () -> HollowTreeStructure.CODEC;
	public static final StructureType<CampStructure> CAMP = () -> CampStructure.CODEC;
	public static final StructureType<HedgeMazeStructure> HEDGE_MAZE = () -> HedgeMazeStructure.CODEC;
	public static final StructureType<HollowHillStructure> HOLLOW_HILL = () -> HollowHillStructure.CODEC;
	public static final StructureType<QuestGroveStructure> QUEST_GROVE = () -> QuestGroveStructure.CODEC;
	public static final StructureType<MushroomTowerStructure> MUSHROOM_TOWER = () -> MushroomTowerStructure.CODEC;
	public static final StructureType<NagaCourtyardStructure> NAGA_COURTYARD = () -> NagaCourtyardStructure.CODEC;
	public static final StructureType<LichTowerStructure> LICH_TOWER = () -> LichTowerStructure.CODEC;
	public static final StructureType<LabyrinthStructure> LABYRINTH = () -> LabyrinthStructure.CODEC;
	public static final StructureType<HydraLairStructure> HYDRA_LAIR = () -> HydraLairStructure.CODEC;
	public static final StructureType<KnightStrongholdStructure> KNIGHT_STRONGHOLD = () -> KnightStrongholdStructure.CODEC;
	public static final StructureType<DarkTowerStructure> DARK_TOWER = () -> DarkTowerStructure.CODEC;
	public static final StructureType<YetiCaveStructure> YETI_CAVE = () -> YetiCaveStructure.CODEC;
	public static final StructureType<AuroraPalaceStructure> AURORA_PALACE = () -> AuroraPalaceStructure.CODEC;
	public static final StructureType<TrollCaveStructure> TROLL_CAVE = () -> TrollCaveStructure.CODEC;
	public static final StructureType<GiantHouseStructure> GIANT_HOUSE = () -> GiantHouseStructure.CODEC;
	public static final StructureType<FinalCastleStructure> FINAL_CASTLE = () -> FinalCastleStructure.CODEC;

	public static void init() {
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("landmark"), LANDMARK_WRAPPED);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("progression"), PROGRESSION_WRAPPED);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("fallen_trunk"), FALLEN_TRUNK);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("hollow_tree"), HOLLOW_TREE);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("camp"), CAMP);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("hedge_maze"), HEDGE_MAZE);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("hollow_hill"), HOLLOW_HILL);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("quest_grove"), QUEST_GROVE);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("mushroom_tower"), MUSHROOM_TOWER);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("naga_courtyard"), NAGA_COURTYARD);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("lich_tower"), LICH_TOWER);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("labyrinth"), LABYRINTH);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("hydra_lair"), HYDRA_LAIR);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("knight_stronghold"), KNIGHT_STRONGHOLD);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("dark_tower"), DARK_TOWER);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("yeti_cave"), YETI_CAVE);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("aurora_palace"), AURORA_PALACE);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("troll_cave"), TROLL_CAVE);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("giant_house"), GIANT_HOUSE);
		Registry.register(BuiltInRegistries.STRUCTURE_TYPE, TwilightForestMod.prefix("final_castle"), FINAL_CASTLE);
	}
}
