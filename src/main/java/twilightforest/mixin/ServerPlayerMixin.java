package twilightforest.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.events.CharmEvents;

/**
 * Injects into {@link ServerPlayer#die(DamageSource)} at HEAD to save charm of keeping items
 * BEFORE {@code dropAllDeathLoot()} is called (which happens later in the same method).
 * <p>
 * Fabric's {@code ServerLivingEntityEvents.AFTER_DEATH} fires AFTER items are dropped,
 * so we need this mixin to intercept before the drop.
 */
@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

	@Inject(method = "die", at = @At("HEAD"))
	private void tf$handleDeathSave(DamageSource source, CallbackInfo ci) {
		CharmEvents.handleDeathSave((ServerPlayer) (Object) this);
	}
}