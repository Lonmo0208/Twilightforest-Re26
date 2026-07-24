package twilightforest.compat.rei.displays;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.rei.TFREIServerPlugin;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class REIUncraftingDisplay extends BasicDisplay {

	private final RecipeHolder<? extends CraftingRecipe> recipe;

	private REIUncraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, RecipeHolder<? extends CraftingRecipe> recipe) {
		super(inputs, outputs, Optional.of(recipe.id().identifier()));
		this.recipe = recipe;
	}

	public static REIUncraftingDisplay ofUncrafting(RecipeHolder<UncraftingRecipe> recipe) {
		List<EntryIngredient> inputs = EntryIngredients.ofIngredients(recipe.value().placementInfo().ingredients());
		List<EntryIngredient> outputs = List.of(EntryIngredients.of(recipe.value().assemble(CraftingInput.EMPTY)));

		return new REIUncraftingDisplay(inputs, outputs, recipe);
	}

	public static REIUncraftingDisplay of(RecipeHolder<CraftingRecipe> recipe) {
		boolean isUncraftingRecipe = recipe.value() instanceof UncraftingRecipe;

		List<EntryIngredient> inputs = isUncraftingRecipe ? EntryIngredients.ofIngredients(recipe.value().placementInfo().ingredients()) : List.of(EntryIngredients.of(recipe.value().assemble(CraftingInput.EMPTY)));
		List<EntryIngredient> outputs = isUncraftingRecipe ? List.of(EntryIngredients.of(recipe.value().assemble(CraftingInput.EMPTY))) : EntryIngredients.ofIngredients(recipe.value().placementInfo().ingredients());

		return new REIUncraftingDisplay(inputs, outputs, recipe);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return TFREIServerPlugin.UNCRAFTING;
	}

	public CraftingRecipe getRecipe() {
		return this.recipe.value();
	}

	public Identifier getRecipeId() {
		return this.recipe.id().identifier();
	}

	@Override
	public DisplaySerializer<? extends Display> getSerializer() {
		return SERIALIZER;
	}

	@SuppressWarnings("unchecked")
	public static final DisplaySerializer<REIUncraftingDisplay> SERIALIZER = DisplaySerializer.of(
		new MapCodec<>() {
			@Override
			public <T> DataResult<REIUncraftingDisplay> decode(DynamicOps<T> ops, MapLike<T> input) {
				T idValue = input.get("recipe_id");
				if (idValue == null) {
					return DataResult.error(() -> "Missing recipe_id");
				}
				return Identifier.CODEC.decode(ops, idValue).flatMap(pair -> {
					Identifier location = pair.getFirst();
					Optional<RecipeHolder<?>> recipe = Minecraft.getInstance().getSingleplayerServer().getRecipeManager().byKey(ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, location));
					if (recipe.isPresent()) {
						return DataResult.success(REIUncraftingDisplay.of((RecipeHolder<CraftingRecipe>) recipe.get()));
					}
					return DataResult.error(() -> "Unknown recipe: " + location);
				});
			}

			@Override
			public <T> RecordBuilder<T> encode(REIUncraftingDisplay input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
				prefix.add("recipe_id", Identifier.CODEC.encodeStart(ops, input.getRecipeId()));
				return prefix;
			}

			@Override
			public <T> Stream<T> keys(DynamicOps<T> ops) {
				return Stream.of(ops.createString("recipe_id"));
			}
		},
		StreamCodec.of(
			(buf, display) -> ByteBufCodecs.STRING_UTF8.encode(buf, display.getRecipeId().toString()),
			buf -> {
				Identifier location = Identifier.tryParse(ByteBufCodecs.STRING_UTF8.decode(buf));
				if (location != null) {
					Optional<RecipeHolder<?>> recipe = Minecraft.getInstance().getSingleplayerServer().getRecipeManager().byKey(ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, location));
					if (recipe.isPresent()) {
						return REIUncraftingDisplay.of((RecipeHolder<CraftingRecipe>) recipe.get());
					}
				}
				return null;
			}
		)
	);
}
