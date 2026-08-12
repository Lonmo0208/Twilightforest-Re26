package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.templates.GraveyardFeature;
import twilightforest.world.components.processors.*;
import twilightforest.world.components.structures.courtyard.CourtyardTerraceTemplateProcessor;

public class TFStructureProcessors {

	public static void init() {
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("cobble_variants"), CobbleVariants.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("smooth_stone_variants"), SmoothStoneVariants.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("stone_brick_variants"), StoneBricksVariants.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("infest_blocks"), InfestBlocksProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("nagastone_variants"), NagastoneVariants.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("state_transfiguring"), StateTransfiguringProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("wood_swizzle"), WoodPaletteSwizzle.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("smart_grass"), SmartGrassProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("box_cutting"), BoxCuttingProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("targeted_rot"), TargetedRotProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("web"), GraveyardFeature.WebTemplateProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("courtyard_terrace"), CourtyardTerraceTemplateProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("soft_replace"), SoftReplaceProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("spawner_processor"), SpawnerProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("update_marking"), UpdateMarkingProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("vertical_decay"), VerticalDecayProcessor.MAP_CODEC);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("wood_multiswizzle"), WoodMultiPaletteSwizzle.MAP_CODEC);
	}
}