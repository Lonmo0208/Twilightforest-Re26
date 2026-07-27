package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.loot.modifiers.FieryToolSmeltingModifier;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;

public class TFLootModifiers {

	public static final MapCodec<FieryToolSmeltingModifier> FIERY_PICK_SMELTING = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, TwilightForestMod.prefix("fiery_pick_smelting"), FieryToolSmeltingModifier.CODEC);
	public static final MapCodec<GiantToolGroupingModifier> GIANT_PICK_GROUPING = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, TwilightForestMod.prefix("giant_block_grouping"), GiantToolGroupingModifier.CODEC);

	public static void init() {
		// Static initializer triggers all registrations
	}
}