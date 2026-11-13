package twilightforest.loot.conditions;

import net.minecraft.world.level.storage.loot.ValidationContext;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import twilightforest.config.TFConfig;

public class UncraftingTableEnabledCondition implements LootItemCondition {

	private static final UncraftingTableEnabledCondition INSTANCE = new UncraftingTableEnabledCondition();
	public static final MapCodec<UncraftingTableEnabledCondition> CODEC = MapCodec.unit(INSTANCE);

	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(LootContext context) {
		return !TFConfig.disableEntireTable;
	}

	public static LootItemCondition.Builder uncraftingTableEnabled() {
		return UncraftingTableEnabledCondition::new;
	}
	// 26.4-snapshot-8: LootItemCondition gained the abstract constantValue hook; these conditions are runtime-dependent, never constant
	@org.jetbrains.annotations.Nullable
	@Override
	public Boolean constantValue(LootItemCondition.ConstantConditionEvaluationContext context) {
		return null;
	}
	// 26.4-snapshot-8: Validatable gained the abstract validate hook
	@Override
	public void validate(ValidationContext context) {
	}
}
