package twilightforest.init;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.*;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapedRecipe;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapelessRecipe;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;

import java.util.function.Supplier;
import net.minecraft.core.Registry;

// TODO: Update recipes to use new codec serialization system. Check RecipeSerializers and ShapedRecipe classes for reference implementation.
public class TFRecipes {

	private static <T extends CustomRecipe> MapCodec<T> simpleCodec(Supplier<T> factory) {
		return RecordCodecBuilder.mapCodec(
			instance -> instance.group(
				CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(r -> r.category())
			).apply(instance, category -> factory.get())
		);
	}

	private static <T extends CustomRecipe> StreamCodec<RegistryFriendlyByteBuf, T> simpleStreamCodec(Supplier<T> factory) {
		return StreamCodec.of((buf, obj) -> {}, buf -> factory.get());
	}

	public static final RecipeSerializer<CasketRepairRecipe> CASKET_REPAIR_RECIPE = new RecipeSerializer<>(simpleCodec(CasketRepairRecipe::new), simpleStreamCodec(CasketRepairRecipe::new));
	public static final RecipeSerializer<EmperorsClothRecipe> EMPERORS_CLOTH_RECIPE = new RecipeSerializer<>(simpleCodec(EmperorsClothRecipe::new), simpleStreamCodec(EmperorsClothRecipe::new));
	public static final RecipeSerializer<EssenceRepairRecipe> ESSENCE_REPAIR_RECIPE = new RecipeSerializer<>(simpleCodec(EssenceRepairRecipe::new), simpleStreamCodec(EssenceRepairRecipe::new));
	public static final RecipeSerializer<MagicMapCloningRecipe> MAGIC_MAP_CLONING_RECIPE = new RecipeSerializer<>(simpleCodec(MagicMapCloningRecipe::new), simpleStreamCodec(MagicMapCloningRecipe::new));
	public static final RecipeSerializer<MazeMapCloningRecipe> MAZE_MAP_CLONING_RECIPE = new RecipeSerializer<>(simpleCodec(MazeMapCloningRecipe::new), simpleStreamCodec(MazeMapCloningRecipe::new));
	public static final RecipeSerializer<MoonwormQueenRepairRecipe> MOONWORM_QUEEN_REPAIR_RECIPE = new RecipeSerializer<>(simpleCodec(MoonwormQueenRepairRecipe::new), simpleStreamCodec(MoonwormQueenRepairRecipe::new));
	public static final RecipeSerializer<ScepterRepairRecipe> SCEPTER_REPAIR_RECIPE = ScepterRepairRecipe.SERIALIZER;
	public static final RecipeSerializer<UncraftingRecipe> UNCRAFTING_SERIALIZER = UncraftingRecipe.SERIALIZER;
	public static final RecipeSerializer<TravellersGearModifierShapelessRecipe> MODIFIER_SHAPELESS_RECIPE_SERIALIZER = TravellersGearModifierShapelessRecipe.SERIALIZER;
	public static final RecipeSerializer<TravellersGearModifierShapedRecipe> MODIFIER_SHAPED_RECIPE_SERIALIZER = TravellersGearModifierShapedRecipe.SERIALIZER;
	public static final RecipeSerializer<TravellersVestGlovesMergeRecipe> TRAVELLERS_VEST_GLOVES_MERGE_RECIPE_SERIALIZER = new RecipeSerializer<>(simpleCodec(TravellersVestGlovesMergeRecipe::new), simpleStreamCodec(TravellersVestGlovesMergeRecipe::new));
	public static final RecipeSerializer<NoTemplateSmithingRecipe> NO_TEMPLATE_SMITHING_SERIALIZER = NoTemplateSmithingRecipe.SERIALIZER;
	public static final RecipeSerializer<DryingRecipe> DRYING_SERIALIZER = DryingRecipe.SERIALIZER;

	public static final RecipeType<UncraftingRecipe> UNCRAFTING_RECIPE = new RecipeType<>() {};
	public static final RecipeType<DryingRecipe> DRYING_RECIPE = new RecipeType<>() {};

	public static void init() {
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("casket_repair_recipe"), CASKET_REPAIR_RECIPE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("emperors_cloth_recipe"), EMPERORS_CLOTH_RECIPE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("essence_repair_recipe"), ESSENCE_REPAIR_RECIPE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("magic_map_cloning_recipe"), MAGIC_MAP_CLONING_RECIPE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("maze_map_cloning_recipe"), MAZE_MAP_CLONING_RECIPE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("moonworm_queen_repair_recipe"), MOONWORM_QUEEN_REPAIR_RECIPE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("scepter_repair"), SCEPTER_REPAIR_RECIPE);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("uncrafting"), UNCRAFTING_SERIALIZER);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("travellers_gear_modifier_shapeless_recipe"), MODIFIER_SHAPELESS_RECIPE_SERIALIZER);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("travellers_gear_modifier_shaped_recipe"), MODIFIER_SHAPED_RECIPE_SERIALIZER);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("travellers_vest_gloves_merge_recipe"), TRAVELLERS_VEST_GLOVES_MERGE_RECIPE_SERIALIZER);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("no_template_smithing"), NO_TEMPLATE_SMITHING_SERIALIZER);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, TwilightForestMod.prefix("drying"), DRYING_SERIALIZER);

		Registry.register(BuiltInRegistries.RECIPE_TYPE, TwilightForestMod.prefix("uncrafting"), UNCRAFTING_RECIPE);
		Registry.register(BuiltInRegistries.RECIPE_TYPE, TwilightForestMod.prefix("drying"), DRYING_RECIPE);
	}
}