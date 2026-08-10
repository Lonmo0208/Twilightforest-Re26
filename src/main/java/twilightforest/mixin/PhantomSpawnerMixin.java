package twilightforest.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.custom.TravellersModifiersManager;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

	/**
	 * If all non-spectator players have the Night Vision Goggles modifier,
	 * cancel phantom spawning entirely.
	 * <p>
	 * Note: This is a global check - if any player doesn't have goggles,
	 * phantoms can still spawn (including near goggles-wearing players).
	 * For per-player filtering, additional ASM-based injection would be needed.
	 */
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void tf$cancelPhantomSpawnIfAllPlayersHaveGoggles(ServerLevel level, boolean spawnEnemies, CallbackInfo ci) {
		if (!spawnEnemies) return;

		boolean hasVisiblePlayerWithoutGoggles = false;

		for (ServerPlayer player : level.players()) {
			if (!player.isSpectator()) {
				if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)) {
					// Found a non-spectator player without goggles, allow normal phantom spawning
					hasVisiblePlayerWithoutGoggles = true;
					break;
				}
			}
		}

		// If all non-spectator players have goggles (or are spectators), cancel phantom spawning
		if (!hasVisiblePlayerWithoutGoggles) {
			ci.cancel();
		}
	}
}
