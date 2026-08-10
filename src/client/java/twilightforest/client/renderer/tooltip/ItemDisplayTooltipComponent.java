package twilightforest.client.renderer.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.item.travellers_gear.TravellersGogglesItem;

public class ItemDisplayTooltipComponent implements ClientTooltipComponent {
	private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_background");
	private static final int SLOT_SIZE = 24;

	private final NonNullList<ItemStack> contents;

	public ItemDisplayTooltipComponent(TravellersGogglesItem.Tooltip tooltip) {
		this.contents = tooltip.contents().items();
	}

	@Override
	public void extractImage(@NotNull Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics) {
		int k = 0;
		for (int gridY = 0; gridY < gridSizeY(); gridY++) {
			for (int gridX = 0; gridX < gridSizeX(); gridX++) {
				int renderX = x + gridX * SLOT_SIZE;
				int renderY = y + gridY * SLOT_SIZE;
				this.renderSlot(renderX, renderY, k++, graphics, font);
			}
		}
	}

	private void renderSlot(int x, int y, int itemIndex, GuiGraphicsExtractor graphics, Font font) {
		if (itemIndex >= this.contents.size()) {
			this.blit(graphics, x, y);
		} else {
			ItemStack itemstack = this.contents.get(itemIndex);
			this.blit(graphics, x, y);
			graphics.item(itemstack, x + 4, y + 4, itemIndex);
			graphics.itemDecorations(font, itemstack, x + 4, y + 4);
		}
	}

	private void blit(GuiGraphicsExtractor graphics, int x, int y) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_SPRITE, x, y, SLOT_SIZE, SLOT_SIZE);
	}

	private int backgroundWidth() {
		return this.gridSizeX() * SLOT_SIZE;
	}

	private int backgroundHeight() {
		return this.gridSizeY() * SLOT_SIZE;
	}

	private int gridSizeX() {
		return ItemDisplayContents.LAYOUT.size();
	}

	private int gridSizeY() {
		return 1;
	}

	@Override
	public int getHeight(@NotNull Font font) {
		return this.backgroundHeight() + 4;
	}

	@Override
	public int getWidth(@NotNull Font font) {
		return this.backgroundWidth();
	}
}
