package twilightforest.compat.jei;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;

//I have to wrap the itemstack in a class like this because otherwise it conflicts with JEI's VanillaTypes.ITEM_STACK
public record FakeLivingBlock(ItemStack stack) {
	public static final Codec<FakeLivingBlock> CODEC = ItemStack.CODEC.xmap(
		FakeLivingBlock::new,
		FakeLivingBlock::stack
	);
}
