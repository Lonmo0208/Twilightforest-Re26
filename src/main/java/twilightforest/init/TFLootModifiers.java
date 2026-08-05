package twilightforest.init;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import twilightforest.TwilightForestMod;
import twilightforest.loot.modifiers.FieryToolSmeltingModifier;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;

public class TFLootModifiers {

	public static final MapCodec<FieryToolSmeltingModifier> FIERY_PICK_SMELTING = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, TwilightForestMod.prefix("fiery_pick_smelting"), FieryToolSmeltingModifier.CODEC);

	public static void init() {
		// Static initializer triggers all registrations
	}

	/**
	 * Apply all loot modifiers (fiery smelting, giant pick grouping) to the generated loot.
	 * Called from {@link twilightforest.mixin.LootTableMixin} at RETURN of getRandomItems.
	 */
	public static ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootParams params) {
		return GiantToolGroupingModifier.apply(FieryToolSmeltingModifier.apply(generatedLoot, params), params);
	}
}