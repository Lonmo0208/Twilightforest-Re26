package twilightforest.events;

import net.minecraft.world.entity.player.Player;
import twilightforest.beanification.Component;
import twilightforest.beanification.PostConstruct;
import twilightforest.init.TFDataAttachments;

/**
 * Handles player tick events that were previously registered via NeoForge events.
 * Called from {@link twilightforest.mixin.PlayerMixin} via static methods.
 */
@Component
public class PlayerEvents {

	@PostConstruct
	private void setup() {
		// No event registration needed; logic is called from PlayerMixin mixin
	}

	/**
	 * Called from PlayerMixin at TAIL of tick().
	 * Handles feather fan, yeti throwing, and portal cooldown ticking.
	 */
	public static void tickPlayerCaps(Player player) {
		// Feather fan: clear fall damage
		if (Boolean.TRUE.equals(TFDataAttachments.getOrCreate(player, TFDataAttachments.FEATHER_FAN, () -> false))) {
			player.setIgnoreFallDamageFromCurrentImpulse(true, player.position());
			player.currentImpulseImpactPos = player.position();

			if (player.onGround() || player.isSwimming() || player.isInWater()) {
				player.setAttached(TFDataAttachments.FEATHER_FAN, false);
			}
		}

		// Yeti throwing progress
		TFDataAttachments.getOrCreate(player, TFDataAttachments.YETI_THROWING, twilightforest.components.entity.YetiThrowAttachment::new).tick(player);

		// Portal cooldown
		TFDataAttachments.getOrCreate(player, TFDataAttachments.TF_PORTAL_COOLDOWN, twilightforest.components.entity.TFPortalAttachment::new).tick(player);
	}
}