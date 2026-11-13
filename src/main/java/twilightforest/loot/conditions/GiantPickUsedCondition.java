package twilightforest.loot.conditions;

import net.minecraft.world.level.storage.loot.ValidationContext;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import twilightforest.init.TFDataAttachments;

import java.util.Set;

public record GiantPickUsedCondition(LootContext.EntityTarget target) implements LootItemCondition.ContextUser {

	public static final MapCodec<GiantPickUsedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(o -> o.target)).apply(instance, GiantPickUsedCondition::new));

	@Override
	public MapCodec<? extends LootItemCondition.ContextUser> codec() {
		return CODEC;
	}

	@Override
	public Set<ContextKey<?>> requiredContextParams() {
		return ImmutableSet.of(this.target.contextParam());
	}

	@Override
	public boolean test(LootContext context) {
		if (context.getOptional(this.target.contextParam()) instanceof Player player) {
			var attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.GIANT_PICKAXE_MINING, twilightforest.components.entity.GiantPickaxeMiningAttachment::new);
			return player.level().getGameTime() == attachment.getMining() && attachment.canMakeGiantBlock();
		}
		return false;
	}

	public static LootItemCondition.Builder builder(LootContext.EntityTarget target) {
		return () -> new GiantPickUsedCondition(target);
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
