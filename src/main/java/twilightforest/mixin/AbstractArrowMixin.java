package twilightforest.mixin;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.events.FabricEvents;
import twilightforest.events.ToolEvents;
import twilightforest.events.TravellersGearEvents;

/**
 * Fires the Ender Bow's position-swap effect when an arrow marked by
 * {@link twilightforest.init.TFDataAttachments#ENDER_BOW_ARROW} hits a living entity.
 * Fabric has no projectile impact event, so this replaces the NeoForge
 * {@code ProjectileImpactEvent} listener used upstream.
 */
@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

	@Inject(method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V", at = @At("HEAD"))
	private void twilightforest$enderBowSwap(EntityHitResult hitResult, CallbackInfo ci) {
		ToolEvents.performEnderBowSwap((AbstractArrow) (Object) this, hitResult);
		AbstractArrow self = (AbstractArrow) (Object) this;
		FabricEvents.ProjectileImpactEvent event = new FabricEvents.ProjectileImpactEvent(self, hitResult);
		TravellersGearEvents.performPerfectDodge(event);
	}

	@Inject(method = "onHitBlock(Lnet/minecraft/world/phys/BlockHitResult;)V", at = @At("HEAD"))
	private void twilightforest$onHitBlock(BlockHitResult hitResult, CallbackInfo ci) {
		AbstractArrow self = (AbstractArrow) (Object) this;
		FabricEvents.ProjectileImpactEvent event = new FabricEvents.ProjectileImpactEvent(self, hitResult);
		TravellersGearEvents.magnetizeArrows(event);
	}
}
