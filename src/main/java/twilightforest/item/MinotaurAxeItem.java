package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

// TODO-263: Original extends AxeItem, now use components in properties
public class MinotaurAxeItem extends Item {

	public MinotaurAxeItem(ToolMaterial material, float damage, float speed, Properties properties) {
		// TODO-263: Original super(material, damage, speed, properties)
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
		builder.accept(Component.translatable("item.twilightforest.minotaur_axe.desc").withStyle(ChatFormatting.GRAY));
	}
}
