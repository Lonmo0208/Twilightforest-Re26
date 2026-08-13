package twilightforest.inventory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class InventoryUtil {
	public static void giveItemToPlayer(Player player, ItemStack stack) {
		if (player.getInventory().add(stack))
			return;

		if (player.level() instanceof ServerLevel serverLevel) {
			player.spawnAtLocation(serverLevel, stack);
		}
	}
}
