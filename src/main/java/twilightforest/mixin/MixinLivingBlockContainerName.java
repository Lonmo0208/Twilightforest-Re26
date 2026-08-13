package twilightforest.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.behavior.LivingBlockContainerBehavior;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.SkullChestBlock;
import twilightforest.block.TFChestBlock;
import twilightforest.block.TFTrappedChestBlock;

/**
 * Fixes the window title shown when opening a Twilight Forest chest as a LivingBlock.
 * <p>
 * Vanilla's {@code LivingBlockContainerBehavior#displayName} builds a translation key like
 * {@code container.<path>}, which does not exist for Twilight Forest containers. This mixin
 * makes the container use the item's own description id (e.g.
 * {@code block.twilightforest.twilight_oak_chest}) instead. Mirrors NeoForge's 26w14a
 * {@code MixinLivingBlockContainerName}.
 */
@Mixin(LivingBlockContainerBehavior.class)
public class MixinLivingBlockContainerName {

	private static boolean isTwilightContainer(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			return block instanceof TFChestBlock || block instanceof TFTrappedChestBlock || block instanceof SkullChestBlock;
		}
		return false;
	}

	@Inject(method = "displayName(Lnet/minecraft/world/entity/livingblock/LivingBlock;)Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
	private void tf$fixContainerName(LivingBlock entity, CallbackInfoReturnable<Component> cir) {
		ItemStack stack = entity.getItemStack();
		if (isTwilightContainer(stack)) {
			cir.setReturnValue(Component.translatable(stack.getItem().getDescriptionId()));
		}
	}
}