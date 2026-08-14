package twilightforest.potions;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import twilightforest.TwilightForestMod;

public class FrostedEffect extends MobEffect {
	public static final Identifier MOVEMENT_SPEED_MODIFIER = TwilightForestMod.prefix("frosted_slowdown");
	public static final double FROST_MULTIPLIER = -0.15D;

	public FrostedEffect() {
		super(MobEffectCategory.HARMFUL, 0x56CBFD);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, FrostedEffect.MOVEMENT_SPEED_MODIFIER, FROST_MULTIPLIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
		// Mark as in powder snow to prevent LivingEntity.aiStep() from decreasing ticksFrozen
		// (!isInPowderSnow || !canFreeze() → ticksFrozen -= 2). Without this, ticksFrozen
		// would oscillate +amplification / -2 every tick, causing the freeze overlay to jitter.
		mob.setIsInPowderSnow(true);
		if (mob.canFreeze()) {
			// Always add at least 1 tick of freeze progress even at amplification 0,
			// so the ice overlay gradually builds up rather than toggling on/off
			int addAmount = Math.max(1, amplification);
			mob.setTicksFrozen(Math.min(mob.getTicksRequiredToFreeze(), mob.getTicksFrozen() + addAmount));
		}
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
		return true;
	}
}