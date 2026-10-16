package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

import java.util.Set;

/**
 * Rolls a loot pool once per player taking part in a multiplayer boss fight. Only ever consumed in whole-number
 * positions ("rolls", "n"), which is why this is an int provider that rounds the player-scaled float total.
 */
public record MultiplayerBasedNumberProvider(Holder<ContextFloatProvider> rollsPerPlayer, Holder<ContextFloatProvider> defaultRolls) implements ContextIntProvider, LootContextUser {
	public static final MapCodec<MultiplayerBasedNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextFloatProviders.CODEC.fieldOf("per_player_rolls").forGetter(MultiplayerBasedNumberProvider::rollsPerPlayer),
			ContextFloatProviders.CODEC.fieldOf("default_rolls").forGetter(MultiplayerBasedNumberProvider::defaultRolls))
		.apply(instance, MultiplayerBasedNumberProvider::new));

	@Override
	public MapCodec<MultiplayerBasedNumberProvider> codec() {
		return CODEC;
	}

	@Override
	public void validate(ValidationContext context) {
		Validatable.validateHolder(context, "per_player_rolls", this.rollsPerPlayer);
		Validatable.validateHolder(context, "default_rolls", this.defaultRolls);
	}

	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return Set.of(LootContextParams.THIS_ENTITY);
	}

	public static MultiplayerBasedNumberProvider rollsForPlayers(Holder<ContextFloatProvider> rollsPerPlayer, Holder<ContextFloatProvider> defaultRolls) {
		return new MultiplayerBasedNumberProvider(rollsPerPlayer, defaultRolls);
	}

	@Override
	public int getIntUnsafe(LootContext context) {
		// Math.round mirrors the float value the old unified NumberProvider exposed before ints and floats split apart
		return Math.round(this.playerScaledRolls(context));
	}

	private float playerScaledRolls(LootContext context) {
		if (TFConfig.multiplayerFightAdjuster.adjustsLootRolls()
			&& context.getOptional(LootContextParams.THIS_ENTITY) instanceof Entity entity
			&& entity.hasAttached(TFDataAttachments.MULTIPLAYER_FIGHT)) {
			int qualifiedPlayers = TFDataAttachments.getOrCreate(entity, TFDataAttachments.MULTIPLAYER_FIGHT, twilightforest.components.entity.MultiplayerInclusivityAttachment::new).getQualifiedPlayers().size();
			float total = this.defaultRolls.value().getFloat(context);
			for (int i = 0; i < qualifiedPlayers - 1; i++) {
				total += Math.max(0, this.rollsPerPlayer.value().getFloat(context));
			}
			return total;
		}

		return this.defaultRolls.value().getFloat(context);
	}
}
