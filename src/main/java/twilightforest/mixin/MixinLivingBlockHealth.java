package twilightforest.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.block.SkullChestBlock;
import twilightforest.block.TFChestBlock;
import twilightforest.block.TFTrappedChestBlock;

@Mixin(LivingBlock.class)
public class MixinLivingBlockHealth {

	@Accessor("DATA_MAX_HEALTH_ID")
	private static EntityDataAccessor<Float> getDataMaxHealthId() {
		return null;
	}

	private static boolean isTwilightContainer(ItemStack stack) {
		if (stack.getItem() instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			return block instanceof TFChestBlock || block instanceof TFTrappedChestBlock || block instanceof SkullChestBlock;
		}
		return false;
	}

	@Inject(method = "setItemStack", at = @At("TAIL"), remap = false, require = 0)
	private void twilightforest$fixHealth(ItemStack stack, CallbackInfo ci) {
		if (isTwilightContainer(stack)) {
			LivingBlock self = (LivingBlock) (Object) this;
			float change = 200.0F - self.getMaxHealth();
			self.getEntityData().set(getDataMaxHealthId(), 200.0F);
			self.setHealth(Mth.clamp(self.getHealth() + change, 1.0F, 200.0F));
		}
	}
}
