package twilightforest.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.events.CharmEvents;
import twilightforest.events.PlayerEvents;

/**
 * Mixin for {@link Player} that replaces NeoForge event handlers:
 * <ul>
 *   <li>{@code updatePlayerCaps} (Inject at TAIL of tick, for feather fan, yeti throwing, portal cooldown)</li>
 *   <li>{@code dropEquipment} (Inject at HEAD, final last-minute sweep of KEPT_ON_DEATH items
 *        right before inventory.dropAll() would be called inside the super method)</li>
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

	/**
	 * Last-minute cleanup of KEPT_ON_DEATH armor/offhand items immediately before
	 * {@code Player.dropEquipment} would call {@code inventory.dropAll()} (which iterates
	 * the shared EntityEquipment and drops every non-empty stack). This runs after any
	 * game-rule checks and {@code destroyVanishingCursedItems} but before any drops occur.
	 */
	@Inject(method = "dropEquipment", at = @At("HEAD"))
	private void tf$sweepKeptOnDeathBeforeDrop(ServerLevel level, CallbackInfo ci) {
		if (!level.getGameRules().get(net.minecraft.world.level.gamerules.GameRules.KEEP_INVENTORY)) {
			CharmEvents.sweepKeptOnDeathItemsBeforeDrop((Player) (Object) this);
		}
	}
}