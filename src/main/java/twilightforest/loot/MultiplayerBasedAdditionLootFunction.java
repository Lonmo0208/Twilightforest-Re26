package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

import java.util.Optional;

public class MultiplayerBasedAdditionLootFunction extends LootItemConditionalFunction {
	public static final MapCodec<MultiplayerBasedAdditionLootFunction> CODEC = RecordCodecBuilder.mapCodec(
		p_298131_ -> commonFields(p_298131_)
			.and(ContextFloatProviders.CODEC.fieldOf("extra_count_per_player").forGetter(o -> o.value))
			.apply(p_298131_, MultiplayerBasedAdditionLootFunction::new)
	);

	private final Holder<ContextFloatProvider> value;

	public MultiplayerBasedAdditionLootFunction(Optional<Holder<LootItemCondition>> predicate, Holder<ContextFloatProvider> value) {
		super(predicate);
		this.value = value;
	}

	public static MultiplayerBasedAdditionLootFunction.Builder addForAllParticipatingPlayers(Holder<ContextFloatProvider> additionPerPlayer) {
		return new MultiplayerBasedAdditionLootFunction.Builder(additionPerPlayer);
	}

	@Override
	public MapCodec<? extends LootItemConditionalFunction> codec() {
		return CODEC;
	}

	@Override
	public void validate(ValidationContext context) {
		super.validate(context);
		Validatable.validateHolder(context, "extra_count_per_player", this.value);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		if (TFConfig.multiplayerFightAdjuster.adjustsLootRolls()
			&& context.getOptional(LootContextParams.THIS_ENTITY) instanceof Entity entity
			&& entity.hasAttached(TFDataAttachments.MULTIPLAYER_FIGHT)) {
			int qualifiedPlayers = TFDataAttachments.getOrEmpty(entity, TFDataAttachments.MULTIPLAYER_FIGHT, twilightforest.components.entity.MultiplayerInclusivityAttachment::new).getQualifiedPlayers().size();
			if (qualifiedPlayers > 1) {
				int participatingPlayers = qualifiedPlayers - 1;
				int extraItems = Math.round(this.value.value().getFloat(context)) * participatingPlayers;
				stack.setCount(Math.clamp(stack.getCount() + extraItems, 0, stack.getMaxStackSize()));
			}
		}
		return stack;
	}

	public static class Builder extends LootItemConditionalFunction.Builder<MultiplayerBasedAdditionLootFunction.Builder> {
		private final Holder<ContextFloatProvider> count;

		public Builder(Holder<ContextFloatProvider> pLootingMultiplier) {
			this.count = pLootingMultiplier;
		}

		@Override
		protected MultiplayerBasedAdditionLootFunction.Builder getThis() {
			return this;
		}

		@Override
		public MultiplayerBasedAdditionLootFunction build() {
			return new MultiplayerBasedAdditionLootFunction(this.getCondition(), this.count);
		}
	}
}
