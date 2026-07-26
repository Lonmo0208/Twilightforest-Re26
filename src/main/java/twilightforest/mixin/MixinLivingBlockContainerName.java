package twilightforest.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.behavior.LivingBlockContainerBehavior;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.SkullChestBlock;
import twilightforest.block.TFChestBlock;
import twilightforest.block.TFTrappedChestBlock;

@Mixin(LivingBlockContainerBehavior.class)
public class MixinLivingBlockContainerName {

	private static boolean isTwilightContainer(ItemStack stack) {
		if (stack.getItem() instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			return block instanceof TFChestBlock || block instanceof TFTrappedChestBlock || block instanceof SkullChestBlock;
		}
		return false;
	}

	@Inject(method = "displayName", at = @At("RETURN"), cancellable = true, remap = false, require = 0)
	private void twilightforest$fixContainerName(LivingBlock entity, CallbackInfoReturnable<Component> cir) {
		ItemStack stack = entity.getItemStack();
		if (isTwilightContainer(stack)) {
			cir.setReturnValue(Component.translatable(stack.getItem().getDescriptionId()));
		}
	}
}
