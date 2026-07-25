package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.displays.DefaultSmithingDisplay;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class REINoTemplateDisplay {

	public static List<DefaultSmithingDisplay> noTemplate(RecipeHolder<NoTemplateSmithingRecipe> recipe) {
		List<DefaultSmithingDisplay> displays = new ArrayList<>();
		for (Holder<Item> additionHolder : recipe.value().getAddition().items().toList()) {
			for (Holder<Item> baseHolder : recipe.value().getBase().items().toList()) {
				ItemStack additionStack = new ItemStack(additionHolder);
				ItemStack baseStack = new ItemStack(baseHolder);

				EntryIngredient output = EntryIngredients.of(recipe.value().assemble(
					new SmithingRecipeInput(ItemStack.EMPTY, baseStack, additionStack)));

				displays.add(new DefaultSmithingDisplay(List.of(
					EntryIngredients.of(ItemStack.EMPTY),
					EntryIngredients.of(baseStack),
					EntryIngredients.of(additionStack)
				), List.of(output), Optional.of(recipe.id().identifier())));
			}
		}
		return displays;
	}
}