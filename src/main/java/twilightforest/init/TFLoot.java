package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import twilightforest.TwilightForestMod;
import twilightforest.loot.LootingEnchantNumberProvider;
import twilightforest.loot.MultiplayerBasedAdditionLootFunction;
import twilightforest.loot.MultiplayerBasedNumberProvider;
import twilightforest.loot.conditions.GiantPickUsedCondition;
import twilightforest.loot.conditions.IsMinionCondition;
import twilightforest.loot.conditions.ModExistsCondition;
import twilightforest.loot.conditions.UncraftingTableEnabledCondition;

public class TFLoot {

	public static final MapCodec<IsMinionCondition> IS_MINION_CODEC = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, TwilightForestMod.prefix("is_minion"), IsMinionCondition.CODEC);
	public static final MapCodec<ModExistsCondition> MOD_EXISTS_CODEC = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, TwilightForestMod.prefix("mod_exists"), ModExistsCondition.CODEC);
	public static final MapCodec<UncraftingTableEnabledCondition> UNCRAFTING_TABLE_ENABLED_CODEC = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, TwilightForestMod.prefix("uncrafting_table_enabled"), UncraftingTableEnabledCondition.CODEC);
	public static final MapCodec<GiantPickUsedCondition> GIANT_PICK_USED_CONDITION_CODEC = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, TwilightForestMod.prefix("giant_pick_used"), GiantPickUsedCondition.CODEC);

	public static final MapCodec<MultiplayerBasedAdditionLootFunction> MULTIPLAYER_MULTIPLIER_CODEC = Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, TwilightForestMod.prefix("multiplayer_addition"), MultiplayerBasedAdditionLootFunction.CODEC);

	public static final MapCodec<MultiplayerBasedNumberProvider> MULTIPLAYER_ROLLS_CODEC = Registry.register(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE, TwilightForestMod.prefix("multiplayer_rolls"), MultiplayerBasedNumberProvider.CODEC);
	public static final MapCodec<LootingEnchantNumberProvider> LOOTING_ROLLS_CODEC = Registry.register(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE, TwilightForestMod.prefix("looting_rolls"), LootingEnchantNumberProvider.CODEC);

	public static void init() {
		// Static initializer triggers all registrations
	}
}