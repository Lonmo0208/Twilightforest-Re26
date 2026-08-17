package twilightforest.client.overlay;

import com.mojang.blaze3d.platform.Window;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import twilightforest.client.overlay.display.ItemDisplay;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.ItemDisplays;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ItemDisplayOverlay implements HudElement {

	@Override
	public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
		Minecraft minecraft = Minecraft.getInstance();
		render(graphics, minecraft, minecraft.getWindow(), minecraft.gui, minecraft.player);
	}

	public static void render(GuiGraphicsExtractor graphics, Minecraft minecraft, Window window, Gui gui, Player player) {
		// Don't render when the HUD is hidden (F1). Note: the F3 debug overlay does NOT hide the vanilla HUD,
		// so unlike the old ported gating we do NOT bail out on showDebugScreen() here — otherwise the travel
		// goggles' item display disappears whenever the player opens F3.
		boolean hudHidden = minecraft.gameRenderer.gameRenderState().guiRenderState.isHudHidden;
		if (player == null || hudHidden)
			return;

		ItemStack goggles = player.getItemBySlot(EquipmentSlot.HEAD);
		if (!TravellersModifiersManager.isModifierActive(player, goggles, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER))
			return;

		ItemDisplayContents contents = goggles.get(TFDataComponents.ITEM_DISPLAY);
		if (contents == null || contents.isEmpty())
			return;

		List<DisplayHolder> typesToRender = new ArrayList<>();
		int widest = fillDisplayHolders(typesToRender, contents, minecraft, gui, player);
		if (typesToRender.isEmpty())
			return;

		renderHolders(graphics, minecraft, gui, player, typesToRender, widest);
	}

	private static void renderHolders(GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, List<DisplayHolder> typesToRender, int widest) {
		float scale = (float) TFConfig.itemDisplayScale;
		int xOffs = TFConfig.itemDisplayXOffs;
		int yOffs = TFConfig.itemDisplayYOffs;

		graphics.pose().pushMatrix();
		graphics.pose().translate(xOffs, yOffs);
		if (scale != 1.0F) {
			graphics.pose().scale(scale, scale);
		}

		typesToRender.sort(Comparator.comparing(holder -> holder.display().displayPosition()));
		for (DisplayHolder holder : typesToRender) {
			holder.display().render(holder.stack(), graphics, minecraft, gui, player, widest);
			graphics.pose().translate(0, holder.bounds().height());
		}

		graphics.pose().popMatrix();
	}

	private static int fillDisplayHolders(List<DisplayHolder> typesToRender, ItemDisplayContents contents, Minecraft minecraft, Gui gui, Player player) {
		NonNullList<ItemStack> items = contents.items();
		int slots = Math.min(ItemDisplayContents.LAYOUT.size(), items.size());
		int activeMapSlot = contents.findActiveMapSlot();

		List<ItemStack> validStacks = new ArrayList<>();
		List<ItemDisplay> validDisplays = new ArrayList<>();

		for (int i = 0; i < slots; i++) {
			ItemStack stack = items.get(i);
			if (stack.isEmpty()) continue;

			ItemDisplayType type = ItemDisplayContents.LAYOUT.get(i);
			if (type == ItemDisplays.MAP && i != activeMapSlot)
				continue;

			if (!ItemDisplayContents.LAYOUT.get(i).validItems().test(stack))
				continue;

			ItemDisplay display = (ItemDisplay) type.display().get();
			validStacks.add(stack);
			validDisplays.add(display);
		}

		if (validStacks.isEmpty())
			return 0;

		int widest = 0;
		for (int i = 0; i < validStacks.size(); i++) {
			ItemDisplay.Bounds b = validDisplays.get(i).getWidgetSize(validStacks.get(i), minecraft, gui, player, 0);
			widest = Math.max(widest, b.width());
		}

		for (int i = 0; i < validStacks.size(); i++) {
			ItemDisplay.Bounds bounds = validDisplays.get(i).getWidgetSize(validStacks.get(i), minecraft, gui, player, widest);
			typesToRender.add(new DisplayHolder(validStacks.get(i), validDisplays.get(i), bounds));
		}

		return widest;
	}

	public record DisplayHolder(ItemStack stack, ItemDisplay display, ItemDisplay.Bounds bounds) {

	}
}
