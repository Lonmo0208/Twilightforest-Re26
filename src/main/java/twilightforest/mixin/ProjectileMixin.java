package twilightforest.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.config.TFConfig;
import twilightforest.entity.projectile.ITFProjectile;

/**
 * Mixin for {@link Projectile} that replaces NeoForge {@code ProjectileImpactEvent} handler
 * in EntityEvents#onParryProjectile. Handles parrying of projectiles (non-arrow projectiles
 * like Lich Bombs, etc.) when the blocking entity has a shield equipped.
 * <p>
 * Arrow-specific parrying is handled by {@link AbstractArrowMixin} via the TravellersGearEvents path.
 */
@Mixin(Projectile.class)
public abstract class ProjectileMixin {

	@Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
	private void tf$onParryProjectile(HitResult hitResult, CallbackInfo ci) {
		Projectile self = (Projectile) (Object) this;

		if (self.level().isClientSide()) return;
		if (!(TFConfig.parryNonTwilightAttacks || self instanceof ITFProjectile)) return;
		if (!(hitResult instanceof EntityHitResult entityHitResult)) return;
		if (!(entityHitResult.getEntity() instanceof LivingEntity entityBlocking)) return;

		if (entityBlocking.isBlocking()
			&& entityBlocking.getUseItem().getUseDuration(entityBlocking) - entityBlocking.getUseItemRemainingTicks() <= TFConfig.shieldParryTicks) {
			// Use AIM_DEFLECT so the projectile bounces back toward the shooter
			self.deflect(ProjectileDeflection.AIM_DEFLECT, entityBlocking, EntityReference.of(entityBlocking), true, self.getDeltaMovement().length());
			ci.cancel(); // Prevent the original onHit logic from running
		}
	}
}