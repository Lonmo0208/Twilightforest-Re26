package twilightforest.client;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import org.jetbrains.annotations.Nullable;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.inventory.UncraftingPlaceRecipe;

import java.util.List;

public class UncraftingRecipeBookComponent extends RecipeBookComponent<UncraftingMenu> implements UncraftingPlaceRecipe<Ingredient> {

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
		return slot.index == this.menu.getResultSlotIndex() || slot.container == this.menu.assemblyMatrix;
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
		// The menu handles recipe placement on the server.
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

	@Override
	public void setupGhostRecipe(RecipeHolder<?> recipe, List<Slot> slots) {
		if (recipe.value() instanceof CraftingRecipe craftingRecipe && !craftingRecipe.placementInfo().isImpossibleToPlace()) {
			this.placeRecipe(this.menu.getStateId(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, craftingRecipe.placementInfo().ingredients().iterator(), 0);
		}
	}

	@Override
	public void recipeClicked(ServerPlayer player, @Nullable RecipeHolder<?> recipe, boolean placeAll) {

	}

	@Override
	public void addItemToSlot(Ingredient ingredient, int slot, int maxAmount, int gridY, int gridX) {

	}
}
