package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.AvoidLandmarkGridPlacement;
import twilightforest.world.components.structures.placements.LandmarkGridPlacement;
import net.minecraft.core.Registry;

public class TFStructurePlacementTypes {
	public static final MapCodec<LandmarkGridPlacement> GRID_LANDMARK_PLACEMENT_TYPE = LandmarkGridPlacement.CODEC;
	public static final MapCodec<AvoidLandmarkGridPlacement> AVOID_GRID_LANDMARK_PLACEMENT_TYPE = AvoidLandmarkGridPlacement.CODEC;

	public static void init() {
		Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, TwilightForestMod.prefix("landmark_grid"), GRID_LANDMARK_PLACEMENT_TYPE);
		Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, TwilightForestMod.prefix("avoid_landmark_grid"), AVOID_GRID_LANDMARK_PLACEMENT_TYPE);
	}
}