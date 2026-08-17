package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.core.registries.codec.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.loot.TFLootTables;
import twilightforest.world.components.structures.markerhandler.*;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

import java.util.Map;

public class TemplateMarkerHandlers {

	public static final Codec<TemplateMarkerHandlerType> TYPE_CODEC = Codec.lazyInitialized(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES::byNameCodec);
	public static final Codec<TemplateMarkerHandler> DISPATCH_CODEC = TYPE_CODEC.dispatch("type", TemplateMarkerHandler::getType, TemplateMarkerHandlerType::getCodec);
	public static final Codec<Holder<TemplateMarkerHandler>> HOLDER_CODEC = RegistryFileCodec.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER, DISPATCH_CODEC, true);

	public static final TemplateMarkerHandlerType BLOCK_PLACEMENT = () -> BlockPlaceMarkerHandler.CODEC;
	public static final TemplateMarkerHandlerType HANDLER_SWITCH = () -> SwitchMarkerHandler.CODEC;
	public static final TemplateMarkerHandlerType ROTATION = () -> RotationMarkerHandler.CODEC;
	public static final TemplateMarkerHandlerType DRYING_RACK = () -> DryingRackMarkerHandler.CODEC;
	public static final TemplateMarkerHandlerType PAINTING = () -> PaintingMarkerHandler.CODEC;
	public static final TemplateMarkerHandlerType LOOT = () -> LootMarkerHandler.CODEC;

	public static final ResourceKey<TemplateMarkerHandlerList> CAMP_MARKER_HANDLERS = ResourceKey.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TwilightForestMod.prefix("camp_marker_handlers"));

	public static void init() {
		Registry.register(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES, TwilightForestMod.prefix("block_placement"), BLOCK_PLACEMENT);
		Registry.register(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES, TwilightForestMod.prefix("handler_switch"), HANDLER_SWITCH);
		Registry.register(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES, TwilightForestMod.prefix("rotation"), ROTATION);
		Registry.register(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES, TwilightForestMod.prefix("drying_rack"), DRYING_RACK);
		Registry.register(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES, TwilightForestMod.prefix("painting"), PAINTING);
		Registry.register(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES, TwilightForestMod.prefix("loot"), LOOT);
	}

	public static void bootstrap(BootstrapContext<TemplateMarkerHandlerList> context) {
		BlockPlaceMarkerHandler campfireSeat = new BlockPlaceMarkerHandler(new WeightedStateProvider(WeightedList.<BlockState>builder()
			.add(TFBlocks.TWILIGHT_OAK_SLAB.defaultBlockState(), 1)
			.add(Blocks.AIR.defaultBlockState(), 3)
			.build()));

		DryingRackMarkerHandler armorRack = new DryingRackMarkerHandler(new SimpleStateProvider(TFBlocks.CANOPY_DRYING_RACK.defaultBlockState()), TFLootTables.CAMP_ARMOR_RACK);

		DryingRackMarkerHandler birchDryingRack = new DryingRackMarkerHandler(new SimpleStateProvider(TFBlocks.BIRCH_DRYING_RACK.defaultBlockState()), TFLootTables.CAMP_DRYING_RACK);

		PaintingMarkerHandler painting = new PaintingMarkerHandler(PaintingVariantTags.PLACEABLE);

		LootMarkerHandler tentPot = new LootMarkerHandler(BlockStateProvider.simple(Blocks.DECORATED_POT), TFLootTables.CAMP_POT);

		Map<String, Holder<TemplateMarkerHandler>> keyedHandlers = Map.of(
			"twilight_oak_slab", Holder.direct(campfireSeat),
			"camp_armor_rack", Holder.direct(armorRack),
			"birch_drying_rack", Holder.direct(birchDryingRack),
			"painting", Holder.direct(painting),
			"tent_pot",  Holder.direct(tentPot)
		);
		context.register(CAMP_MARKER_HANDLERS, TemplateMarkerHandlerList.of(
			new RotationMarkerHandler(Holder.direct(new SwitchMarkerHandler(keyedHandlers)))
		));
	}

}