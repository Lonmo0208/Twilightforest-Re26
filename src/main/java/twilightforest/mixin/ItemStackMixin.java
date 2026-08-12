package twilightforest.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


/**
 * Mixin to add NeoForge-compatible canPerformAction to ItemStack.
 */
@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Unique
    public boolean canPerformAction(String ability) {
        // Fabric-compatible implementation: check if the item can perform the given action
        // For now, return true as a stub - real implementation would check tool tags
        return true;
    }

    public ItemStack toStack() {
        return (ItemStack) (Object) this;
    }
}