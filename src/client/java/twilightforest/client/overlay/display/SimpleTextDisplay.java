package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleTextDisplay implements ItemDisplay {

	public abstract Component getText(ItemStack item, Minecraft minecraft, Gui gui, Player player);

	@Override
	public void render(ItemStack item, GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		Component text = this.getText(item, minecraft, gui, player);
		int textWidth = minecraft.font.width(text);
		int contentHeight = minecraft.font.lineHeight;
		int widgetX = Math.max(0, (widestWidgetWidth / 2) - (textWidth / 2));
		int widgetY = 0;

		graphics.text(minecraft.font, text, widgetX, widgetY, 0xFFFFFFFF);
	}

	@Override
	public Bounds getWidgetSize(ItemStack item, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		int textWidth = minecraft.font.width(this.getText(item, minecraft, gui, player));
		int startX = Math.max(0, (widestWidgetWidth / 2) - (textWidth / 2));
		return new Bounds(startX, 0, textWidth, minecraft.font.lineHeight);
	}
}
