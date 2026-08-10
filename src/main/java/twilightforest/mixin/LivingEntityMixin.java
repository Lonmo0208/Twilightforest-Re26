package twilightforest.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.TwilightForestMod;
import twilightforest.events.EntityEvents;
import twilightforest.events.ToolEvents;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersGearLogic;

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

	/**
	 * Water-walking speed fix (part 1 of 2): seed onGround=true at the very START
	 * of {@link LivingEntity#travel} so that {@code moveRelative} runs the GROUND
	 * branch (full MOVEMENT_SPEED acceleration) instead of the sluggish airborne
	 * branch.
	 *
	 * <p>Setting onGround at END of tick (our previous attempt) was useless because
	 * collision resolution inside {@code travel} overwrites the flag back to false
	 * as soon as it notices the entity's AABB bottom isn't resting on a solid
	 * block.  Hooking at HEAD guarantees moveRelative sees the flag and applies
	 * normal walk acceleration.  The second half of the correction lives in
	 * {@link TravellersGearLogic#waterWalkingTick} where we re-apply the correct
	 * ground friction (0.546 equivalent) to compensate for the collision-resolved
	 * air-drag that travel applies after moveRelative.</p>
	 */
	@Inject(method = "travel", at = @At("HEAD"))
	private void tf$waterWalkingSeedOnGroundForTravel(net.minecraft.world.phys.Vec3 travelInput, CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (!TravellersModifiersManager.isModifierActive(self, TravellersModifiersManager.WATER_WALK_MODIFIER))
			return;
		if (self.isShiftKeyDown())
			return;
		double submerged = self.getFluidHeight(net.minecraft.tags.FluidTags.WATER);
		if (submerged > TravellersGearLogic.WATER_WALKING_MAX_SUBMERGED_HEIGHT)
			return;
		// Not yet submerged but standing right on top (fluidHeight=0): check the
		// block directly below feet to confirm we're hovering over water.
		if (submerged <= 0D) {
			net.minecraft.world.level.Level level = self.level();
			net.minecraft.core.BlockPos below = self.blockPosition().below();
			if (!level.getFluidState(below).is(net.minecraft.tags.FluidTags.WATER))
				return;
		}
		self.setOnGround(true);
	}

	@Inject(method = "canStandOnFluid", at = @At("HEAD"), cancellable = true)
	private void tf$waterWalkingCanStandOnFluid(FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
		if (!fluidState.is(FluidTags.WATER))
			return;
		LivingEntity self = (LivingEntity) (Object) this;
		if (!TravellersModifiersManager.isModifierActive(self, TravellersModifiersManager.WATER_WALK_MODIFIER))
			return;

		// ── Official cap: only stand while SUBMERGED_HEIGHT <= 0.4 ─────────────
		// If the entity is deeper into the water we simply do not intervene:
		// vanilla fluid physics takes over, allowing fall damage to be cancelled
		// by the water as usual, and letting the player swim / float up normally.
		// Only once they naturally surface into the "toes dipped" zone do we
		// re-enable the standing behaviour and "catch" them on the fluid top.
		double submergedHeight = self.getFluidHeight(FluidTags.WATER);
		if (submergedHeight > TravellersGearLogic.WATER_WALKING_MAX_SUBMERGED_HEIGHT)
			return;

		boolean shiftDown = self.isShiftKeyDown();
		boolean isWaterWalking = !shiftDown;

		if (submergedHeight > 0 && isWaterWalking && self.level().getGameTime() % 3 == 1)
			TravellersGearLogic.waterWalkingSplashEffect(self);
		cir.setReturnValue(isWaterWalking);
	}
}