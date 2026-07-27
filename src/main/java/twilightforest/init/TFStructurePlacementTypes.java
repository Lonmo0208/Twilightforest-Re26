package twilightforest.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.AvoidLandmarkGridPlacement;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;
import net.minecraft.core.Registry;

public class TFStructurePlacementTypes {
	public static final StructurePlacementType<LandmarkGridPlacement> GRID_LANDMARK_PLACEMENT_TYPE = () -> LandmarkGridPlacement.CODEC;
	public static final StructurePlacementType<AvoidLandmarkGridPlacement> AVOID_GRID_LANDMARK_PLACEMENT_TYPE = () -> AvoidLandmarkGridPlacement.CODEC;

	public static void init() {
		Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, TwilightForestMod.prefix("landmark_grid"), GRID_LANDMARK_PLACEMENT_TYPE);
		Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, TwilightForestMod.prefix("avoid_landmark_grid"), AVOID_GRID_LANDMARK_PLACEMENT_TYPE);
	}
}