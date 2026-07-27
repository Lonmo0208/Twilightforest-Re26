package twilightforest.compat.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * Fabric-compatible stub for Curios compatibility.
 * Real implementation would be in a separate Curios compat module.
 */
public class CuriosCompat {

	public static boolean isCurioEquipped(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return false;
	}

	public static boolean isCurioEquippedAndVisible(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return false;
	}

	public static boolean findAndConsumeCurio(Item item, Player player) {
		return false;
	}
}