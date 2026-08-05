package twilightforest.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFItems;

import java.util.List;

public class FieryToolSmeltingModifier implements LootItemCondition {

	public static final MapCodec<FieryToolSmeltingModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(c -> List.of(c.conditions))
	).apply(inst, FieryToolSmeltingModifier::new));

	private final LootItemCondition[] conditions;

	public FieryToolSmeltingModifier(List<LootItemCondition> conditions) {
		this.conditions = conditions.toArray(new LootItemCondition[0]);
	}

	@Override
	public boolean test(LootContext context) {
		for (LootItemCondition condition : this.conditions) {
			if (!condition.test(context)) return false;
		}
		return true;
	}

	public static @NotNull ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootParams context) {
		ItemInstance tool = context.contextMap().getOptional(LootContextParams.TOOL);
		if (tool == null || !tool.is(TFItems.FIERY_PICKAXE) || generatedLoot.isEmpty()) {
			return generatedLoot;
		}
		ObjectArrayList<ItemStack> result = new ObjectArrayList<>(generatedLoot.size());
		float xp = 0.0F;
		for (ItemStack stack : generatedLoot) {
			var recipe = context.getLevel().recipeAccess().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), context.getLevel());
			if (recipe.isPresent()) {
				ItemStack smelted = recipe.get().value().assemble(new SingleRecipeInput(stack)).copy();
				if (!smelted.isEmpty()) {
					smelted.setCount(stack.getCount() * smelted.getCount());
					result.add(smelted);
					xp += recipe.get().value().experience();
					continue;
				}
			}
			result.add(stack);
		}

		Entity entity = context.contextMap().getOptional(LootContextParams.THIS_ENTITY);
		if (xp > 0.0F && entity != null) {
			ExperienceOrb.award(context.getLevel(), entity.position(), Math.round(xp));
		}
		return result;
	}

	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}
}