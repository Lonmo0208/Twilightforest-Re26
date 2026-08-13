package twilightforest.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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

/**
 * Forces Twilight Forest chests (used as LivingBlocks) to have 200 max health, matching how
 * vanilla chests/barrels/shulker boxes behave. Twilight chests are not in any of the vanilla
 * container tags, so vanilla's {@code setItemStack} would otherwise give them a low health
 * derived from block explosion resistance. Mirrors NeoForge's 26w14a
 * {@code MixinLivingBlockHealth}.
 */
@Mixin(LivingBlock.class)
public abstract class MixinLivingBlockHealth {

	@Accessor("DATA_MAX_HEALTH_ID")
	private static EntityDataAccessor<Float> tf$getDataMaxHealthId() {
		throw new AssertionError();
	}

	private static boolean isTwilightContainer(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			return block instanceof TFChestBlock || block instanceof TFTrappedChestBlock || block instanceof SkullChestBlock;
		}
		return false;
	}

	@Inject(method = "setItemStack", at = @At("TAIL"))
	private void tf$fixHealth(ItemStack stack, CallbackInfo ci) {
		if (isTwilightContainer(stack)) {
			LivingBlock self = (LivingBlock) (Object) this;
			float change = 200.0F - self.getMaxHealth();
			self.getEntityData().set(tf$getDataMaxHealthId(), 200.0F);
			self.setHealth(Mth.clamp(self.getHealth() + change, 1.0F, 200.0F));
		}
	}
}