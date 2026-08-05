package twilightforest.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.IHostileMount;
import twilightforest.events.HostileMountEvents;

/**
 * Mixin for {@link Entity} that replaces NeoForge {@code EntityMountEvent} handler
 * in HostileMountEvents#preventMountDismount. Prevents players from dismounting
 * hostile mounts (e.g., Alpha Yeti, Pinch Beetle) unless explicitly allowed.
 *
 * @see HostileMountEvents#hostileDismount(Entity)
 */
@Mixin(Entity.class)
public abstract class EntityStopRidingMixin {

	@Inject(method = "stopRiding", at = @At("HEAD"), cancellable = true)
	private void tf$preventHostileMountDismount(CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		Entity vehicle = self.getVehicle();
		if (!self.level().isClientSide()
			&& self instanceof Player player
			&& player.isAlive()
			&& vehicle instanceof IHostileMount
			&& vehicle.isAlive()
			&& !HostileMountEvents.allowDismount
			&& !player.getAbilities().invulnerable) {
			ci.cancel();
		}
	}
}