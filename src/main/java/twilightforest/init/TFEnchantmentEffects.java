package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import twilightforest.TwilightForestMod;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.enchantment.RechargeScepterEffect;
import twilightforest.enchantment.SmashBlocksEffect;
import net.minecraft.core.Registry;

public class TFEnchantmentEffects {

	public static final MapCodec<ApplyFrostedEffect> APPLY_FROSTED = ApplyFrostedEffect.CODEC;
	public static final MapCodec<RechargeScepterEffect> RECHARGE_SCEPTER = RechargeScepterEffect.CODEC;
	public static final MapCodec<SmashBlocksEffect> SMASH_BLOCKS = SmashBlocksEffect.CODEC;

	public static void init() {
		Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, TwilightForestMod.prefix("apply_frosted"), APPLY_FROSTED);
		Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, TwilightForestMod.prefix("recharge_scepter"), RECHARGE_SCEPTER);
		Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, TwilightForestMod.prefix("smash_blocks"), SMASH_BLOCKS);
	}

}