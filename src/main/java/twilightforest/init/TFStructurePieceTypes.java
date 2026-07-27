package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.*;
import twilightforest.world.components.structures.courtyard.*;
import twilightforest.world.components.structures.darktower.*;
import twilightforest.world.components.structures.fallentrunk.FallenTrunkPiece;
import twilightforest.world.components.structures.finalcastle.*;
import twilightforest.world.components.structures.hollowtree.*;
import twilightforest.world.components.structures.icetower.*;
import twilightforest.world.components.structures.lichtower.*;
import twilightforest.world.components.structures.lichtowerrevamp.*;
import twilightforest.world.components.structures.minotaurmaze.*;
import twilightforest.world.components.structures.mushroomtower.*;
import twilightforest.world.components.structures.stronghold.*;
import twilightforest.world.components.structures.trollcave.*;



public class TFStructurePieceTypes {
	// Single-Piece Structures
	//IStructurePieceTypes that can be referred to
	public static final StructurePieceType TFHill = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfhill"), HollowHillComponent::new);
	public static final StructurePieceType TFHedge = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfhedge"), HedgeMazeComponent::new);
	public static final StructurePieceType TFQuestGrove = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfquest1"), QuestGrove::new);
	public static final StructurePieceType TFHydra = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfhydra"), HydraLairComponent::new);
	public static final StructurePieceType TFYeti = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfyeti"), YetiCaveComponent::new);
	public static final StructurePieceType TFFallenTrunk = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffallentrunk"), FallenTrunkPiece::new);
	public static final StructurePieceType TFUtilityPiece = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfutilitypiece"), UtilityPiece::new);
	public static final StructurePieceType TFJigsawTemplate = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfjigsawtemplate"), (StructurePieceSerializationContext ctx, net.minecraft.nbt.CompoundTag nbt) -> TwilightJigsawPiece.defaultDeserialize(ctx, nbt));

	// Hollow Tree
	public static final StructurePieceType TFHTLB = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfhtlb"), HollowTreeLargeBranch::new);
	public static final StructurePieceType TFHTMB = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfhtmb"), HollowTreeMedBranch::new);
	public static final StructurePieceType TFHTSB = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfhtsb"), HollowTreeSmallBranch::new);
	public static final StructurePieceType TFHTTr = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfhttr"), HollowTreeTrunk::new);
	public static final StructurePieceType TFHTRo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfhtro"), HollowTreeRoot::new);
	public static final StructurePieceType TFHTLD = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfhtld"), HollowTreeLeafDungeon::new);

	// Mushroom Castle
	public static final StructurePieceType TFMTMai = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmtmai"), MushroomTowerMainComponent::new);
	public static final StructurePieceType TFMTWin = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmtwin"), MushroomTowerWingComponent::new);
	public static final StructurePieceType TFMTBri = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmtbri"), MushroomTowerBridgeComponent::new);
	public static final StructurePieceType TFMTMB = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmtmb"), MushroomTowerMainBridgeComponent::new);
	public static final StructurePieceType TFMTRoofMush = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmtroofmush"), TowerRoofMushroomComponent::new);

	// Naga Courtyard
	public static final StructurePieceType TFNCMn = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfncmn"), CourtyardMain::new);
	public static final StructurePieceType TFNCCp = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfnccp"), NagaCourtyardHedgeCapComponent::new);
	public static final StructurePieceType TFNCCpP = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfnccpp"), NagaCourtyardHedgeCapPillarComponent::new);
	public static final StructurePieceType TFNCCr = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfnccr"), NagaCourtyardHedgeCornerComponent::new);
	public static final StructurePieceType TFNCLn = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfncln"), NagaCourtyardHedgeLineComponent::new);
	public static final StructurePieceType TFNCT = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfnct"), NagaCourtyardHedgeTJunctionComponent::new);
	public static final StructurePieceType TFNCIs = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfncis"), NagaCourtyardHedgeIntersectionComponent::new);
	public static final StructurePieceType TFNCPd = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfncpd"), NagaCourtyardHedgePadderComponent::new);
	public static final StructurePieceType TFNCTe = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfncte"), CourtyardTerrace::new);
	public static final StructurePieceType TFNCHe = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfnche"), CourtyardTerraceHedge::new);
	public static final StructurePieceType TFNCPa = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfncpa"), CourtyardPathPiece::new);
	public static final StructurePieceType TFNCWl = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfncwl"), CourtyardWall::new);
	public static final StructurePieceType TFNCWP = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfncwp"), CourtyardWallPadder::new);
	public static final StructurePieceType TFNCWC = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfncwc"), CourtyardWallCornerOuter::new);
	public static final StructurePieceType TFNCWA = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfncwa"), CourtyardWallCornerInner::new);

	// Old Lich Tower
	public static final StructurePieceType TFLTBea = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltbea"), TowerBeardComponent::new);
	public static final StructurePieceType TFLTBA = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltba"), TowerBeardAttachedComponent::new);
	public static final StructurePieceType TFLTBri = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltbri"), TowerBridgeComponent::new);
	public static final StructurePieceType TFLTMai = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltmai"), TowerMainComponent::new);
	public static final StructurePieceType TFLTOut = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltout"), TowerOutbuildingComponent::new);
	public static final StructurePieceType TFLTRoo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltroo"), TowerRoofComponent::new);
	public static final StructurePieceType TFLTRAS = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltras"), TowerRoofAttachedSlabComponent::new);
	public static final StructurePieceType TFLTRF = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltrf"), TowerRoofFenceComponent::new);
	public static final StructurePieceType TFLTRGF = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltrgf"), TowerRoofGableForwardsComponent::new);
	public static final StructurePieceType TFLTRP = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltrp"), TowerRoofPointyComponent::new);
	public static final StructurePieceType TFLTRPO = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltrpo"), TowerRoofPointyOverhangComponent::new);
	public static final StructurePieceType TFLTRS = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltrs"), TowerRoofSlabComponent::new);
	public static final StructurePieceType TFLTRSF = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltrsf"), TowerRoofSlabForwardsComponent::new);
	public static final StructurePieceType TFLTRSt = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltrst"), TowerRoofStairsComponent::new);
	public static final StructurePieceType TFLTRStO = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltrsto"), TowerRoofStairsOverhangComponent::new);
	public static final StructurePieceType TFLTWin = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltwin"), TowerWingComponent::new);

	// Lich Tower
	public static final StructurePieceType LICH_TOWER_FOYER = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tflttfoy"), LichTowerFoyer::new);
	public static final StructurePieceType LICH_TOWER_BASE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltctbase"), LichTowerBase::new);
	public static final StructurePieceType LICH_TOWER_BASE_TRIM = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltcttrim"), LichTowerBaseTrim::new);
	public static final StructurePieceType LICH_TOWER_SEGMENT = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltctseg"), LichTowerSegment::new);
	public static final StructurePieceType LICH_SPAWNER_BRIDGE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltmobbridge"), LichTowerSpawnerBridge::new);
	public static final StructurePieceType LICH_WING_BRIDGE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltbridge"), LichTowerWingBridge::new);
	public static final StructurePieceType LICH_WING_ROOF = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tflttroof"), LichTowerWingRoof::new);
	public static final StructurePieceType LICH_WING_BEARD = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tflttbeard"), LichTowerWingBeard::new);
	public static final StructurePieceType LICH_WING_ROOM = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tflttroom"), LichTowerWingRoom::new);
	public static final StructurePieceType LICH_TOWER_DECOR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tflttdecor"), LichTowerRoomDecor::new);
	public static final StructurePieceType LICH_MAGIC_GALLERY = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tflttgallery"), LichTowerMagicGallery::new);
	public static final StructurePieceType LICH_FOYER_DECORATION = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tflttfoyd"), LichTowerFoyerDecor::new);
	public static final StructurePieceType LICH_BOSS_ROOM = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tflttboss"), LichBossRoom::new);
	public static final StructurePieceType LICH_BOSS_ROOF = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tflttbossroof"), LichBossRoof::new);
	public static final StructurePieceType LICH_PERIMETER_FENCE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltfence"), LichPerimeterFence::new);
	public static final StructurePieceType LICH_YARD_PATH = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltpath"), LichYardBox::new);
	public static final StructurePieceType LICH_YARD_GRAVE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltgrave"), LichYardGrave::new);
	public static final StructurePieceType LICH_YARD_LIGHTS = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfltlight"), LichYardLights::new);

	// Labyrinth
	public static final StructurePieceType TFMMC = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmc"), MazeCorridorComponent::new);
	public static final StructurePieceType TFMMCIF = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmcif"), MazeCorridorIronFenceComponent::new);
	public static final StructurePieceType TFMMCR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmcr"), MazeCorridorRootsComponent::new);
	public static final StructurePieceType TFMMCS = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmcs"), MazeCorridorShroomsComponent::new);
	public static final StructurePieceType TFMMDE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmde"), MazeDeadEndComponent::new);
	public static final StructurePieceType TFMMDEC = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmdec"), MazeDeadEndChestComponent::new);
	public static final StructurePieceType TFMMDEF = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmdef"), MazeDeadEndFountainComponent::new);
	public static final StructurePieceType TFMMDEFL = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmdefl"), MazeDeadEndFountainLavaComponent::new);
	public static final StructurePieceType TFMMDEP = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmdep"), MazeDeadEndPaintingComponent::new);
	public static final StructurePieceType TFMMDER = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmder"), MazeDeadEndRootsComponent::new);
	public static final StructurePieceType TFMMDES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmdes"), MazeDeadEndShroomsComponent::new);
	public static final StructurePieceType TFMMDET = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmdet"), MazeDeadEndTorchesComponent::new);
	public static final StructurePieceType TFMMDETrC = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmdetrc"), MazeDeadEndTrappedChestComponent::new);
	public static final StructurePieceType TFMMDETC = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmdetc"), MazeDeadEndTripwireChestComponent::new);
	public static final StructurePieceType TFMMES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmes"), MazeEntranceShaftComponent::new);
	public static final StructurePieceType TFMMMound = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmmound"), MazeMoundComponent::new);
	public static final StructurePieceType TFMMMR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmmr"), MazeMushRoomComponent::new);
	public static final StructurePieceType TFMMR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmr"), MazeRoomComponent::new);
	public static final StructurePieceType TFMMRB = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmrb"), MazeRoomBossComponent::new);
	public static final StructurePieceType TFMMRC = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmrc"), MazeRoomCollapseComponent::new);
	public static final StructurePieceType TFMMRE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmre"), MazeRoomExitComponent::new);
	public static final StructurePieceType TFMMRF = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmrf"), MazeRoomFountainComponent::new);
	public static final StructurePieceType TFMMRSC = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmrsc"), MazeRoomSpawnerChestsComponent::new);
	public static final StructurePieceType TFMMRV = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmrv"), MazeRoomVaultComponent::new);
	public static final StructurePieceType TFMMRuins = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmruins"), MazeRuinsComponent::new);
	public static final StructurePieceType TFMMUE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmue"), MazeUpperEntranceComponent::new);
	public static final StructurePieceType TFMMaze = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfmmaze"), MinotaurMazeComponent::new);

	// Knight Stronghold
	public static final StructurePieceType TFSSH = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfssh"), StrongholdSmallHallwayComponent::new);
	public static final StructurePieceType TFSLT = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfslt"), StrongholdLeftTurnComponent::new);
	public static final StructurePieceType TFSCr = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfscr"), StrongholdCrossingComponent::new);
	public static final StructurePieceType TFSRT = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsrt"), StrongholdRightTurnComponent::new);
	public static final StructurePieceType TFSDE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsde"), StrongholdDeadEndComponent::new);
	public static final StructurePieceType TFSBalR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsbalr"), StrongholdBalconyRoomComponent::new);
	public static final StructurePieceType TFSTR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfstr"), StrongholdTrainingRoomComponent::new);
	public static final StructurePieceType TFSSS = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsss"), StrongholdSmallStairsComponent::new);
	public static final StructurePieceType TFSTC = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfstc"), StrongholdTreasureCorridorComponent::new);
	public static final StructurePieceType TFSAt = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsat"), StrongholdAtriumComponent::new);
	public static final StructurePieceType TFSFo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsfo"), StrongholdFoundryComponent::new);
	public static final StructurePieceType TFTreaR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tftrear"), StrongholdTreasureRoomComponent::new);
	public static final StructurePieceType TFSBR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsbr"), StrongholdBossRoomComponent::new);
	public static final StructurePieceType TFSAC = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsac"), StrongholdAccessChamberComponent::new);
	public static final StructurePieceType TFSEnter = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsenter"), StrongholdEntranceComponent::new);
	public static final StructurePieceType TFSUA = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsua"), StrongholdUpperAscenderComponent::new);
	public static final StructurePieceType TFSULT = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsult"), StrongholdUpperLeftTurnComponent::new);
	public static final StructurePieceType TFSURT = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsurt"), StrongholdUpperRightTurnComponent::new);
	public static final StructurePieceType TFSUCo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsuco"), StrongholdUpperCorridorComponent::new);
	public static final StructurePieceType TFSUTI = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsuti"), StrongholdUpperTIntersectionComponent::new);
	public static final StructurePieceType TFSShield = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfsshield"), StrongholdShieldStructure::new);

	// Dark Tower
	public static final StructurePieceType TFDTBal = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtbal"), DarkTowerBalconyComponent::new);
	public static final StructurePieceType TFDTBea = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtbea"), DarkTowerBeardComponent::new);
	public static final StructurePieceType TFDTBB = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtbb"), DarkTowerBossBridgeComponent::new);
	public static final StructurePieceType TFDTBT = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtbt"), DarkTowerBossTrapComponent::new);
	public static final StructurePieceType TFDTBri = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtbri"), DarkTowerBridgeComponent::new);
	public static final StructurePieceType TFDTEnt = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtent"), DarkTowerEntranceComponent::new);
	public static final StructurePieceType TFDTEB = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdteb"), DarkTowerEntranceBridgeComponent::new);
	public static final StructurePieceType TFDTMai = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtmai"), DarkTowerMainComponent::new);
	public static final StructurePieceType TFDTMB = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtmb"), DarkTowerMainBridgeComponent::new);
	public static final StructurePieceType TFDTRooS = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtroos"), DarkTowerRoofComponent::new);
	public static final StructurePieceType TFDTRA = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtra"), DarkTowerRoofAntennaComponent::new);
	public static final StructurePieceType TFDTRC = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtrc"), DarkTowerRoofCactusComponent::new);
	public static final StructurePieceType TFDTRFP = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtrfp"), DarkTowerRoofFourPostComponent::new);
	public static final StructurePieceType TFDTRR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtrr"), DarkTowerRoofRingsComponent::new);
	public static final StructurePieceType TFDTWin = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfdtwin"), DarkTowerWingComponent::new);

	// Aurora Palace
	public static final StructurePieceType TFITMai = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfitmai"), IceTowerMainComponent::new);
	public static final StructurePieceType TFITWin = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfitwin"), IceTowerWingComponent::new);
	public static final StructurePieceType TFITRoof = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfitroof"), IceTowerRoofComponent::new);
	public static final StructurePieceType TFITBea = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfitbea"), IceTowerBeardComponent::new);
	public static final StructurePieceType TFITBoss = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfitboss"), IceTowerBossWingComponent::new);
	public static final StructurePieceType TFITEnt = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfitent"), IceTowerEntranceComponent::new);
	public static final StructurePieceType TFITBri = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfitbri"), IceTowerBridgeComponent::new);
	public static final StructurePieceType TFITSt = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfitst"), IceTowerStairsComponent::new);

	// Troll Cave
	public static final StructurePieceType TFTCMai = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tftcmai"), TrollCaveMainComponent::new);
	public static final StructurePieceType TFTCCon = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tftccon"), TrollCaveConnectComponent::new);
	public static final StructurePieceType TFTCGard = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tftcgard"), TrollCaveGardenComponent::new);
	public static final StructurePieceType TFTCloud = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tftcloud"), TrollCloudComponent::new);
	public static final StructurePieceType TFClCa = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfclca"), CloudCastleComponent::new);
	public static final StructurePieceType TFClTr = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfcltr"), CloudTreeComponent::new);
	public static final StructurePieceType TFTCVa = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tftcva"), TrollVaultComponent::new);
	public static final StructurePieceType TFCloud = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tfcloud"), CloudComponent::new);

	// Final Castle
	public static final StructurePieceType TFFCMain = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcmain"), FinalCastleMainComponent::new);
	public static final StructurePieceType TFFCStTo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcstto"), FinalCastleStairTowerComponent::new);
	public static final StructurePieceType TFFCLaTo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffclato"), FinalCastleLargeTowerComponent::new);
	public static final StructurePieceType TFFCMur = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcmur"), FinalCastleMuralComponent::new);
	public static final StructurePieceType TFFCToF48 = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffctof48"), FinalCastleFoundation48Component::new);
	public static final StructurePieceType TFFCRo48Cr = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcro48cr"), FinalCastleRoof48CrenellatedComponent::new);
	public static final StructurePieceType TFFCBoGaz = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcbogaz"), FinalCastleBossGazeboComponent::new);
	public static final StructurePieceType TFFCSiTo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcsito"), FinalCastleMazeTower13Component::new);
	public static final StructurePieceType TFFCDunSt = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcdunst"), FinalCastleDungeonStepsComponent::new);
	public static final StructurePieceType TFFCDunEn = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcdunen"), FinalCastleDungeonEntranceComponent::new);
	public static final StructurePieceType TFFCDunR31 = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcdunr31"), FinalCastleDungeonRoom31Component::new);
	public static final StructurePieceType TFFCDunEx = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcdunex"), FinalCastleDungeonExitComponent::new);
	public static final StructurePieceType TFFCDunBoR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcdunbor"), FinalCastleDungeonForgeRoomComponent::new);
	public static final StructurePieceType TFFCRo9Cr = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcro9cr"), FinalCastleRoof9CrenellatedComponent::new);
	public static final StructurePieceType TFFCRo13Cr = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcro13cr"), FinalCastleRoof13CrenellatedComponent::new);
	public static final StructurePieceType TFFCRo13Con = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcro13con"), FinalCastleRoof13ConicalComponent::new);
	public static final StructurePieceType TFFCRo13Pk = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcro13pk"), FinalCastleRoof13PeakedComponent::new);
	public static final StructurePieceType TFFCEnTo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcento"), FinalCastleEntranceTowerComponent::new);
	public static final StructurePieceType TFFCEnSiTo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcensito"), FinalCastleEntranceSideTowerComponent::new);
	public static final StructurePieceType TFFCEnBoTo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcenboto"), FinalCastleEntranceBottomTowerComponent::new);
	public static final StructurePieceType TFFCEnSt = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcenst"), FinalCastleEntranceStairsComponent::new);
	public static final StructurePieceType TFFCBelTo = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcbelto"), FinalCastleBellTower21Component::new);
	public static final StructurePieceType TFFCBri = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcbri"), FinalCastleBridgeComponent::new);
	public static final StructurePieceType TFFCToF13 = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffctof13"), FinalCastleFoundation13Component::new);
	public static final StructurePieceType TFFCBeF21 = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcbef21"), FinalCastleBellFoundation21Component::new);
	public static final StructurePieceType TFFCFTh21 = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcfth21"), FinalCastleFoundation13ComponentThorns::new);
	public static final StructurePieceType TFFCDamT = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcdamt"), FinalCastleDamagedTowerComponent::new);
	public static final StructurePieceType TFFCWrT = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, TwilightForestMod.prefix("tffcwrt"), FinalCastleWreckedTowerComponent::new);

	public static void init() {
		// All registrations happen in field declarations above
	}
}
