package twilightforest.compat.jei.renderers;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.jei.FakeLivingBlock;
import twilightforest.compat.jei.JEICompat;

import java.util.Objects;

public class FakeLivingBlockHelper implements IIngredientHelper<FakeLivingBlock> {

	@Override
	public IIngredientType<FakeLivingBlock> getIngredientType() {
		return JEICompat.FAKE_ITEM_ENTITY;
	}

	@Override
	public String getDisplayName(FakeLivingBlock ingredient) {
		return ingredient.stack().getHoverName().getString();
	}

	@Override
	public Object getUid(FakeLivingBlock ingredient, UidContext context) {
		return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(ingredient.stack().getItem())).toString();
	}

	@Override
	public Identifier getIdentifier(FakeLivingBlock ingredient) {
		return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(ingredient.stack().getItem()));
	}

	@Override
	public FakeLivingBlock copyIngredient(FakeLivingBlock ingredient) {
		return ingredient;
	}

	@Override
	public String getErrorInfo(@Nullable FakeLivingBlock ingredient) {
		if (ingredient == null) {
			return "null";
		}
		return ingredient.stack().toString();
	}
}
