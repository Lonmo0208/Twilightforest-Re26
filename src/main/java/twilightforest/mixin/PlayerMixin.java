package twilightforest.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.events.PlayerEvents;

/**
 * Mixin for {@link Player} that replaces NeoForge event handlers:
 * <ul>
 *   <li>{@code updatePlayerCaps} (Inject at TAIL of tick, for feather fan, yeti throwing, portal cooldown)</li>
 * </ul>
 */
@Mixin(Player.class)
public abstract class PlayerMixin {

	/**
	 * Replaces NeoForge {@code PlayerTickEvent.Post} for CapabilityEvents#updatePlayerCaps.
	 * Handles feather fan, yeti throwing, and portal cooldown ticking.
	 */
	@Inject(method = "tick", at = @At("TAIL"))
	private void tf$updatePlayerCaps(CallbackInfo ci) {
		PlayerEvents.tickPlayerCaps((Player) (Object) this);
	}
}