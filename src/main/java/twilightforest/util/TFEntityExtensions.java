package twilightforest.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

/**
 * Interface for methods added to Entity via mixin.
 * Cast Entity to this interface to call these methods at compile time.
 *
 * NOTE: Twilight Forest-specific Entity extensions. Methods in this interface are ONLY those
 * which are truly NOT available through Fabric API (or vanilla Minecraft), such as Forge's
 * MultiPartEntity hooks needed for Twilight Bosses (Hydra, Naga, Snow Queen).
 *
 * General-purpose attachment access (getData / setData / hasData / removeData) and
 * generic persistent NBT storage (getPersistentData) are now handled via Fabric's
 * official Attachment API (see {@link twilightforest.init.TFDataAttachments} and use
 * {@code Entity.getAttached()} / {@code Entity.setAttached()} / {@code Entity.hasAttached()}
 * directly). These methods were removed from the extension interface to avoid having
 * two parallel attachment stores with differing persistence guarantees.
 */
public interface TFEntityExtensions {

	// --- MultiPartEntity support (Fabric has NO equivalent, required for Hydra/Naga/SnowQueen Bosses) ---
	Entity[] twilightforest$getParts();

	boolean twilightforest$isMultipartEntity();

	// --- Misc small Forge-style convenience hooks ---
	boolean twilightforest$canFitInsideContainerItems();

	void twilightforest$breakItem(ItemStack stack);

	net.minecraft.world.entity.ai.goal.GoalSelector twilightforest$getGoalSelector();
}
