package twilightforest.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.templates.GraveyardFeature;
import twilightforest.world.components.processors.*;
import twilightforest.world.components.structures.courtyard.CourtyardTerraceTemplateProcessor;
import net.minecraft.core.Registry;

/**
 * Class for registering IStructureProcessorTypes. These are just used for StructureProcessor.getType()
 */
public class TFStructureProcessors {

	public static final StructureProcessorType<CobbleVariants> COBBLE_VARIANTS = () -> CobbleVariants.CODEC;
	public static final StructureProcessorType<SmoothStoneVariants> SMOOTH_STONE_VARIANTS = () -> SmoothStoneVariants.CODEC;
	public static final StructureProcessorType<StoneBricksVariants> STONE_BRICK_VARIANTS = () -> StoneBricksVariants.CODEC;
	public static final StructureProcessorType<InfestBlocksProcessor> INFEST_BLOCKS = () -> InfestBlocksProcessor.CODEC;
	public static final StructureProcessorType<NagastoneVariants> NAGASTONE_VARIANTS = () -> NagastoneVariants.CODEC;

	public static final StructureProcessorType<StateTransfiguringProcessor> STATE_TRANSFIGURING = () -> StateTransfiguringProcessor.CODEC;

	public static final StructureProcessorType<WoodPaletteSwizzle> PLANK_SWIZZLE = () -> WoodPaletteSwizzle.CODEC;
	public static final StructureProcessorType<SmartGrassProcessor> SMART_GRASS = () -> SmartGrassProcessor.CODEC;
	public static final StructureProcessorType<BoxCuttingProcessor> BOX_CUTTING_PROCESSOR = () -> BoxCuttingProcessor.CODEC;
	public static final StructureProcessorType<TargetedRotProcessor> TARGETED_ROT = () -> TargetedRotProcessor.CODEC;

	public static final StructureProcessorType<GraveyardFeature.WebTemplateProcessor> WEB = () -> GraveyardFeature.WebTemplateProcessor.CODEC;
	public static final StructureProcessorType<CourtyardTerraceTemplateProcessor> COURTYARD_TERRACE = () -> CourtyardTerraceTemplateProcessor.CODEC;

	public static final StructureProcessorType<SoftReplaceProcessor> SOFT_REPLACE = () -> SoftReplaceProcessor.CODEC;

	public static final StructureProcessorType<SpawnerProcessor> SPAWNER_PROCESSOR = () -> SpawnerProcessor.CODEC;
	public static final StructureProcessorType<UpdateMarkingProcessor> UPDATE_MARKING_PROCESSOR = () -> UpdateMarkingProcessor.CODEC;

	public static final StructureProcessorType<VerticalDecayProcessor> VERTICAL_DECAY = () -> VerticalDecayProcessor.CODEC;
	public static final StructureProcessorType<WoodMultiPaletteSwizzle> PLANK_MULTISWIZZLE = () -> WoodMultiPaletteSwizzle.CODEC;

	public static void init() {
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("cobble_variants"), COBBLE_VARIANTS);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("smooth_stone_variants"), SMOOTH_STONE_VARIANTS);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("stone_brick_variants"), STONE_BRICK_VARIANTS);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("infest_blocks"), INFEST_BLOCKS);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("nagastone_variants"), NAGASTONE_VARIANTS);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("state_transfiguring"), STATE_TRANSFIGURING);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("wood_swizzle"), PLANK_SWIZZLE);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("smart_grass"), SMART_GRASS);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("box_cutting"), BOX_CUTTING_PROCESSOR);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("targeted_rot"), TARGETED_ROT);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("web"), WEB);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("courtyard_terrace"), COURTYARD_TERRACE);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("soft_replace"), SOFT_REPLACE);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("spawner_processor"), SPAWNER_PROCESSOR);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("update_marking"), UPDATE_MARKING_PROCESSOR);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("vertical_decay"), VERTICAL_DECAY);
		Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, TwilightForestMod.prefix("wood_multiswizzle"), PLANK_MULTISWIZZLE);
	}
}