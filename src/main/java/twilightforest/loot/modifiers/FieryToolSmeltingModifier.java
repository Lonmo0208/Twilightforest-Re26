package twilightforest.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

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

	public @NotNull ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (!test(context)) return generatedLoot;
		List<Pair<ItemStack, Float>> list = generatedLoot.stream().map(stack ->
			context.getLevel().recipeAccess().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), context.getLevel())
				.map(holder -> {
					ItemStack result = holder.value().assemble(new SingleRecipeInput(stack)).copy();
					result.setCount(stack.getCount() * result.getCount());
					return Pair.of(result, holder.value().experience());
				})
				.filter(pair -> !pair.getLeft().isEmpty())
				.orElse(Pair.of(stack, 0.0F))).toList();

		float xp = (float) list.stream().mapToDouble(Pair::getRight).sum();
		if (xp > 0.0F && context.hasParameter(LootContextParams.THIS_ENTITY)) {
			ExperienceOrb.award(context.getLevel(), context.getParameter(LootContextParams.THIS_ENTITY).position(), Math.round(xp));
		}

		return list.stream().map(Pair::getLeft).collect(Collectors.toCollection(ObjectArrayList::new));
	}

	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}
}
