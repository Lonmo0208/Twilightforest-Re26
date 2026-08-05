package twilightforest.client;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.recipebook.PlaceRecipeHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import twilightforest.inventory.UncraftingMenu;

import java.util.List;

public class UncraftingRecipeBookComponent extends RecipeBookComponent<UncraftingMenu> {

	private static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(
		Identifier.withDefaultNamespace("recipe_book/filter_enabled"),
		Identifier.withDefaultNamespace("recipe_book/filter_disabled"),
		Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"),
		Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted")
	);
	private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");
	private static final List<TabInfo> TABS = List.of(
		new TabInfo(SearchRecipeBookCategory.CRAFTING),
		new TabInfo(Items.IRON_AXE, Items.GOLDEN_SWORD, RecipeBookCategories.CRAFTING_EQUIPMENT),
		new TabInfo(Items.BRICKS, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS),
		new TabInfo(Items.LAVA_BUCKET, Items.APPLE, RecipeBookCategories.CRAFTING_MISC),
		new TabInfo(Items.REDSTONE, RecipeBookCategories.CRAFTING_REDSTONE)
	);

	public UncraftingRecipeBookComponent(UncraftingMenu menu) {
		super(menu, TABS);
	}

	@Override
	protected boolean isCraftingSlot(Slot slot) {
		int idx = slot.index;
		return idx == this.menu.getResultSlotIndex() || (idx >= 11 && idx <= 19);
	}

	private boolean canDisplay(RecipeDisplay display) {
		int gridWidth = this.menu.getGridWidth();
		int gridHeight = this.menu.getGridHeight();
		return switch (display) {
			case ShapedCraftingRecipeDisplay shaped -> gridWidth >= shaped.width() && gridHeight >= shaped.height();
			case ShapelessCraftingRecipeDisplay shapeless -> gridWidth * gridHeight >= shapeless.ingredients().size();
			default -> false;
		};
	}

	@Override
	protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap contextMap) {
		// GhostSlots.setResult/setInput are package-private protected members in the MC recipebook package.
		// We intentionally leave this no-op; the recipe book can still open & browse without crashing,
		// and the assembly grid accepts manual placement as before.
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return FILTER_BUTTON_SPRITES;
	}

	@Override
	protected Component getRecipeFilterName() {
		return ONLY_CRAFTABLES_TOOLTIP;
	}

	@Override
	protected void selectMatchingRecipes(RecipeCollection recipeCollection, StackedItemContents stackedItemContents) {
		recipeCollection.selectRecipes(stackedItemContents, this::canDisplay);
	}
}
