package twilightforest.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.events.EntityEvents;
import twilightforest.events.ToolEvents;

/**
 * Mixin for {@link LivingEntity} that replaces several NeoForge event handlers.
 * Uses {@code hurtServer} (the actual overridable method in 26.1.2) instead of
 * {@code hurt} (which is now a final delegate in {@link net.minecraft.world.entity.Entity}).
 * <ul>
 *   <li>{@code modifyIncomingDamage} (ModifyVariable on hurtServer, for frost/fire/tool modifiers)</li>
 *   <li>{@code zombifiedPlayerAttacks} (Inject cancellable at HEAD of hurtServer)</li>
 *   <li>{@code handleMountDamage} (Inject cancellable at HEAD of hurtServer, from HostileMountEvents)</li>
 *   <li>{@code addCloudJumpParticles} (Inject at HEAD of jumpFromGround)</li>
 *   <li>{@code updateShields} (Inject at TAIL of tick)</li>
 *   <li>{@code preventFatigueWithPocketWatch} (Inject cancellable at HEAD of addEffect)</li>
 * </ul>
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	/**
	 * Replaces NeoForge {@code LivingIncomingDamageEvent} handlers:
	 * reduceFrostedEffectIfOnFire, fieryToolSetFire, doKnightmetalToolLogic, addExtraAxeChargingDamage.
	 * Targets {@code hurtServer} (the overridable method in 26.1.2) instead of {@code hurt} (final in Entity).
	 */
	@ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private float tf$modifyIncomingDamage(float amount, ServerLevel level, DamageSource source) {
		return EntityEvents.modifyIncomingDamage((LivingEntity) (Object) this, source, amount);
	}

	/**
	 * Replaces NeoForge zombifiedPlayerAttacks handler.
	 * Converts zombie player attacks to ominous fire damage.
	 */
	@Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
	private void tf$handleZombifiedPlayerAttack(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (EntityEvents.handleZombifiedPlayerAttack((LivingEntity) (Object) this, source, amount)) {
			cir.setReturnValue(false);
		}
	}

	/**
	 * Replaces NeoForge HostileMountEvents#handleMountDamage handler.
	 * Prevents suffocation damage when riding hostile mount, converts fall damage to yeet damage.
	 */
	@Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
	private void tf$handleMountDamage(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (EntityEvents.handleMountDamage((LivingEntity) (Object) this, source, amount)) {
			cir.setReturnValue(false);
		}
	}

	/**
	 * Replaces NeoForge {@code LivingEvent.LivingJumpEvent} handler for cloud jump particles.
	 */
	@Inject(method = "jumpFromGround", at = @At("HEAD"))
	private void tf$addCloudJumpParticles(CallbackInfo ci) {
		EntityEvents.addCloudJumpParticles((LivingEntity) (Object) this);
	}

	/**
	 * Replaces NeoForge {@code EntityTickEvent.Post} for shield updates (CapabilityEvents#updateShields).
	 */
	@Inject(method = "tick", at = @At("TAIL"))
	private void tf$tickShields(CallbackInfo ci) {
		EntityEvents.tickShields((LivingEntity) (Object) this);
	}

	/**
	 * Replaces NeoForge {@code MobEffectEvent.Applicable} for pocket watch mining fatigue prevention.
	 */
	@Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
	private void tf$preventFatigueWithPocketWatch(MobEffectInstance effectInstance, Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (ToolEvents.shouldBlockEffect((LivingEntity) (Object) this, effectInstance)) {
			cir.setReturnValue(false);
		}
	}
}