package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.EssenceRepairRecipe;
import twilightforest.tags.TFItemTags;

import java.util.ArrayList;
import java.util.List;

public class ExanimateEssenceRepairExtension implements ICraftingCategoryExtension<EssenceRepairRecipe> {

	@Override
	public List<SlotDisplay> getIngredients(RecipeHolder<EssenceRepairRecipe> recipeHolder) {
		return List.of();
	}

	@Override
	public void setRecipe(RecipeHolder<EssenceRepairRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		List<List<ItemStack>> inputs = new ArrayList<>();
		List<ItemStack> scepters = new ArrayList<>();
BuiltInRegistries.ITEM.getTagOrEmpty(TFItemTags.SCEPTERS).forEach(h -> scepters.add(new ItemStack(h.value())));

		craftingGridHelper.createAndSetOutputs(builder, scepters);

		scepters.forEach(stack -> stack.setDamageValue(stack.getMaxDamage()));
		inputs.add(scepters);
		inputs.add(List.of(TFItems.EXANIMATE_ESSENCE.toStack()));

		craftingGridHelper.createAndSetInputs(builder, inputs, 0, 0);
		builder.setShapeless();
	}

	@Override
	public void onDisplayedIngredientsUpdate(RecipeHolder<EssenceRepairRecipe> recipeHolder, List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses) {
		//prevent input slot from cycling if we have a certain scepter selected
		if (!focuses.getFocuses(RecipeIngredientRole.OUTPUT).toList().isEmpty()) {
			ItemStack damaged = focuses.getFocuses(RecipeIngredientRole.OUTPUT).findFirst().orElseGet(() -> focuses.getFocuses(RecipeIngredientRole.CRAFTING_STATION).findFirst().orElseThrow()).getTypedValue().getItemStack().orElseThrow().copy();
			damaged.setDamageValue(damaged.getMaxDamage());
			recipeSlots.get(1).createDisplayOverrides().add(damaged);
		}

		//the output scepter should always match whatever the input is. Doesn't matter if the input is cycling or not
		recipeSlots.getFirst().createDisplayOverrides().add(new ItemStack(recipeSlots.get(1).getDisplayedItemStack().orElse(ItemStack.EMPTY).getItem()));
	}
}
