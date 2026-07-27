package twilightforest.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import twilightforest.TwilightForestMod;
import twilightforest.item.effects.StackableEffectConsumeEffect;
import net.minecraft.core.Registry;

public class TFConsumeEffects {

	public static final ConsumeEffect.Type<StackableEffectConsumeEffect> STACKABLE_EFFECTS = new ConsumeEffect.Type<>(StackableEffectConsumeEffect.CODEC, StackableEffectConsumeEffect.STREAM_CODEC);

	public static void init() {
		Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, TwilightForestMod.prefix("stackable_effects"), STACKABLE_EFFECTS);
	}

}