package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.TFEmiCategories;
import twilightforest.item.recipe.UncraftingRecipe;
import twilightforest.tags.TFItemTags;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EmiUncraftingRecipe<T extends CraftingRecipe> extends TFEmiRecipe<T> {

	@Nullable
	private List<EmiIngredient> displayedOutputs;

	public EmiUncraftingRecipe(RecipeHolder<T> recipe) {
		super(TFEmiCategories.UNCRAFTING, recipe, "/uncrafting/", RecipeViewerConstants.GENERIC_RECIPE_WIDTH + 2, RecipeViewerConstants.GENERIC_RECIPE_HEIGHT);
	}

	@Override
	protected void addAdditionalInputs(List<EmiIngredient> inputs) {
		if (this.getRecipe().value() instanceof UncraftingRecipe uncraftingRecipe) {
			inputs.add(EmiIngredient.of(uncraftingRecipe.getInput(), uncraftingRecipe.getCount()));//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
		} else {
			inputs.add(EmiStack.of(this.getRecipe().value().assemble(CraftingInput.EMPTY)));//Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
		}
	}

	@Override
	protected void addAdditionalOutputs(List<EmiStack> finalOutput) {
		this.displayedOutputs = new ArrayList<>();
		List<Ingredient> outputs = new ArrayList<>(this.getRecipe().value().placementInfo().ingredients()); //Collect each ingredient
		outputs.replaceAll(ingredient -> Ingredient.of(ingredient.items()
				.filter(o -> !o.is(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS))
				.filter(o -> o.value().getCraftingRemainder() == null)
				.map(Holder::value)));

		for (int index = 0, offset = 0; index - offset < outputs.size() && index < 9; index++) {
			int x = index % 3, y = index / 3;
			var recipe = this.getRecipe().value();
			if (((RecipeViewerConstants.getRecipeWidth(recipe) <= x && RecipeViewerConstants.getRecipeHeight(recipe) <= 3) || (RecipeViewerConstants.getRecipeWidth(recipe) <= 3 && RecipeViewerConstants.getRecipeHeight(recipe) <= y)) && !(recipe instanceof ShapelessRecipe)) {
				offset++;
				this.displayedOutputs.add(EmiStack.EMPTY);
				continue;
			} //Skips empty spaces in shaped recipes
			Ingredient ingredient = outputs.get(index - offset);
			this.displayedOutputs.add(EmiIngredient.of(ingredient));
			for (Holder<Item> holder : ingredient.items().toList())
				finalOutput.add(EmiStack.of(new ItemStack(holder))); //Set input as output and place in the grid
		}

		while (this.displayedOutputs.size() < 9) {
			this.displayedOutputs.add(EmiStack.EMPTY);
		}
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 35, 18);

		for (int i = 0; i < this.displayedOutputs.size(); i++) {
			int x = i % 3;
			int y = i / 3;
			widgets.addSlot(this.displayedOutputs.get(i), x * 18 + 63, y * 18);
		}

		if (this.getRecipe().value() instanceof UncraftingRecipe uncraftingRecipe) {
			List<ItemStack> stacks = uncraftingRecipe.getInput().items().map(Holder::value).map(ItemStack::new).toList();
			ItemStack[] stackedStacks = new ItemStack[stacks.size()];
			for (int i = 0; i < stacks.size(); i++) stackedStacks[i] = new ItemStack(stacks.getFirst().getItem(), uncraftingRecipe.getCount());
			widgets.addSlot(EmiIngredient.of(Stream.of(stackedStacks).map(EmiStack::of).toList(), uncraftingRecipe.getCount()), 5, 19);//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
		} else {
			widgets.addSlot(EmiStack.of(this.getRecipe().value().assemble(CraftingInput.EMPTY)), 5, 14).large(true).recipeContext(this); //Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
		}

		int cost = this.getRecipe().value() instanceof UncraftingRecipe ur ? ur.getCost() : RecipeViewerConstants.getRecipeCost(this.displayedOutputs.stream().map(ingredient -> ingredient.getEmiStacks().getFirst().getItemStack()).toList());
		if (cost > 0) {
			String costStr = cost + "";
			widgets.addText(Component.literal(costStr), 48 - Minecraft.getInstance().font.width(costStr), 22, RecipeViewerConstants.getXPColor(cost), true);
		}
	}

	//things get a little too insane when this is true so im gonna leave it false for now
	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
