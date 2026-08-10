package twilightforest.item.recipe.travellers;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiable;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.stream.StreamSupport;

//import twilightforest.data.helpers.TFLangProvider; // TODO: package doesn't exist in 26.1.2

public abstract class TravellersGearModifierRecipe extends CustomRecipe {
	protected final ResourceKey<TravellersModifier> travellersModifierKey;
	public TravellersGearModifierRecipe(ResourceKey<TravellersModifier> travellersModifier) {
		super();
		this.travellersModifierKey = travellersModifier;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack stack = getModifiableArmor(input);
		if (stack == null)
			return false;
		int slots = 0;
		if (stack.getItem() instanceof TravellersModifiable travellersModifiableItem)
			slots = travellersModifiableItem.getModifierSlots();
		return TravellersModifiersManager.countInsertableModifiers(level.registryAccess(), stack) < slots
			&& !TravellersModifiersManager.hasTravellersModifier(level.registryAccess(), stack, this.travellersModifierKey)
			&& TravellersModifiersManager.getModifierDataComponentProviders(level.registryAccess(), ingredientListFrom(input), this.travellersModifierKey) <= 1;
	}

	// Note: No @Override — this signature does not exist in CustomRecipe on Fabric 26.1.x, kept for forward compatibility
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack travellerArmorStack = getModifiableArmor(input);
		if (travellerArmorStack == null)
			return ItemStack.EMPTY;

		ItemStack stack = travellerArmorStack.copy();
		return applyModifier(registries, stack, ingredientListFrom(input));
	}

	private static List<Ingredient> ingredientListFrom(CraftingInput input) {
		return input.items().stream()
			.filter(stack -> !stack.isEmpty())
			.map(stack -> Ingredient.of(stack.getItem()))
			.toList();
	}

	@Deprecated
	@Override
	public ItemStack assemble(CraftingInput input) {
		// Relies on the TravellersModifiersManager cache being populated during matches(),
		// so the EMPTY RegistryAccess fall-through still resolves modifiers via CACHED_MODIFIERS.
		return assemble(input, net.minecraft.core.RegistryAccess.EMPTY);
	}

	public ItemStack applyModifier(HolderLookup.Provider registries, ItemStack stack, List<Ingredient> inputs) {
		if (TravellersModifiersManager.transferModifier(registries, stack, inputs, this.travellersModifierKey))
			return stack;
		boolean modifierAdded = TravellersModifiersManager.addModifier(registries, stack, this.travellersModifierKey);
		return modifierAdded ? stack : ItemStack.EMPTY;
	}

	public abstract boolean isShapeless();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract NonNullList<Ingredient> getIngredients();

	protected static @Nullable ItemStack getModifiableArmor(CraftingInput input) {
		return getModifiableArmor(input.items());
	}

	protected static @Nullable ItemStack getModifiableArmor(Iterable<ItemStack> items) {
		return StreamSupport.stream(items.spliterator(), false)
			.filter(stack -> stack.getItem() instanceof TravellersModifiable modifiable && modifiable.getModifierSlots() > 0).findFirst().orElse(null);
	}

	public static ItemStack getModifiableArmorFromIngredients(Iterable<Ingredient> ingredients) {
		return StreamSupport.stream(ingredients.spliterator(), false)
			.flatMap(ingredient -> ingredient.items().map(h -> new ItemStack(h.value())))
			.filter(stack -> stack.getItem() instanceof TravellersModifiable).findFirst().orElseThrow();
	}

	public Identifier getId() {
		return travellersModifierKey.identifier()
			.withPrefix(StringUtils.substringAfterLast(getModifiableArmorFromIngredients(getIngredients()).getItem().getDescriptionId(), '.') + "/")
			.withPrefix("add_modifier_to_travellers_gear/")
			.withSuffix("_modifier");
	}

	public ResourceKey<TravellersModifier> getTravellersModifierKey() {
		return travellersModifierKey;
	}

	public static class AbstractModifierRecipeSerializer<T extends TravellersGearModifierRecipe> {
		protected final MapCodec<T> codec;

		protected AbstractModifierRecipeSerializer(MapCodec<T> codec) {
			this.codec = codec;
		}

		public MapCodec<T> codec() {
			return codec;
		}

		public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
			return StreamCodec.of(this::toNetwork, this::fromNetwork);
		}

		public T fromNetwork(RegistryFriendlyByteBuf buf) {
			RegistryOps<JsonElement> registryops = buf.registryAccess().createSerializationContext(JsonOps.INSTANCE);
			JsonElement jsonelementDeserialized = GsonHelper.fromJson(new com.google.gson.Gson(), buf.readUtf(), JsonElement.class);
			return codec.codec().decode(registryops, jsonelementDeserialized).getOrThrow().getFirst();
		}

		public void toNetwork(RegistryFriendlyByteBuf buf, T recipe) {
			RegistryOps<JsonElement> registryops = buf.registryAccess().createSerializationContext(JsonOps.INSTANCE);
			JsonElement jsonelement = codec.codec().encodeStart(registryops, recipe).getOrThrow();
			buf.writeUtf(jsonelement.toString());
		}
	}
}
