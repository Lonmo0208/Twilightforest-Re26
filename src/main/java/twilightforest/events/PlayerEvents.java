package twilightforest.events;

import net.minecraft.world.entity.player.Player;
import twilightforest.beanification.Component;
import twilightforest.beanification.PostConstruct;
import twilightforest.init.TFDataAttachments;
import twilightforest.util.TFEntityExtensions;

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
		if (((TFEntityExtensions) player).twilightforest$getData(TFDataAttachments.FEATHER_FAN)) {
			player.setIgnoreFallDamageFromCurrentImpulse(true, player.position());
			player.currentImpulseImpactPos = player.position();

			if (player.onGround() || player.isSwimming() || player.isInWater()) {
				((TFEntityExtensions) player).twilightforest$setData(TFDataAttachments.FEATHER_FAN, false);
			}
		}

		// Yeti throwing progress
		((TFEntityExtensions) player).twilightforest$getData(TFDataAttachments.YETI_THROWING).tick(player);

		// Portal cooldown
		((TFEntityExtensions) player).twilightforest$getData(TFDataAttachments.TF_PORTAL_COOLDOWN).tick(player);
	}
}