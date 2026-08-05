package twilightforest.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.events.HostileMountEvents;

import java.util.Set;

/**
 * Mixin for {@link Entity} that replaces NeoForge {@code EntityTeleportEvent} handler
 * in HostileMountEvents#preventTeleportingOffHostileMounts. Prevents entities riding
 * hostile mounts from teleporting (e.g., using ender pearls or other teleportation items).
 */
@Mixin(Entity.class)
public abstract class EntityTeleportMixin {

	@Inject(method = "teleportTo(DDD)V", at = @At("HEAD"), cancellable = true)
	private void tf$preventTeleportOffHostileMount(double x, double y, double z, CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		if (self instanceof LivingEntity living && HostileMountEvents.isRidingUnfriendly(living)) {
			ci.cancel();
		}
	}

	@Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z", at = @At("HEAD"), cancellable = true)
	private void tf$preventTeleportOffHostileMountWithLevel(ServerLevel targetLevel, double x, double y, double z, Set<net.minecraft.world.entity.Relative> relatives, float yRot, float xRot, boolean resetCamera, CallbackInfoReturnable<Boolean> cir) {
		Entity self = (Entity) (Object) this;
		if (self instanceof LivingEntity living && HostileMountEvents.isRidingUnfriendly(living)) {
			cir.setReturnValue(false);
		}
	}
}