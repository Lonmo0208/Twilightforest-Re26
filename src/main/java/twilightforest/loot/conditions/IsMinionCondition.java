package twilightforest.loot.conditions;

import net.minecraft.world.level.storage.loot.ValidationContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import twilightforest.entity.monster.CarminiteGhastling;

import org.jetbrains.annotations.NotNull;

public record IsMinionCondition(boolean inverse) implements LootItemCondition.ContextUser {

	public static final MapCodec<IsMinionCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.BOOL.fieldOf("inverse").forGetter(o -> o.inverse)).apply(instance, IsMinionCondition::new));

	@Override
	public MapCodec<? extends LootItemCondition.ContextUser> codec() {
		return CODEC;
	}

	@Override
	public java.util.Set<net.minecraft.util.context.ContextKey<?>> requiredContextParams() {
		return java.util.Set.of(LootContextParams.THIS_ENTITY);
	}

	@Override
	public boolean test(@NotNull LootContext context) {
		return context.getOptional(LootContextParams.THIS_ENTITY) instanceof CarminiteGhastling ghastling && ghastling.isMinion() == !inverse;
	}

	public static Builder builder(boolean inverse) {
		return () -> new IsMinionCondition(inverse);
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
