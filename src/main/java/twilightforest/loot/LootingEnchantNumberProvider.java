package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.Set;

/**
 * Adds the attacker's enchantment level onto a base value. Always consumed in whole-number positions, so it is an int
 * provider; the base value is wrapped in a float provider by the loot JSON when a fractional amount is needed.
 */
public record LootingEnchantNumberProvider(Holder<Enchantment> enchantment, Holder<ContextIntProvider> baseValue) implements ContextIntProvider, LootContextUser {
	public static final MapCodec<LootingEnchantNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Enchantment.CODEC.fieldOf("enchantment").forGetter(LootingEnchantNumberProvider::enchantment),
			ContextIntProviders.CODEC.fieldOf("base_value").forGetter(LootingEnchantNumberProvider::baseValue))
		.apply(instance, LootingEnchantNumberProvider::new)
	);

	@Override
	public MapCodec<LootingEnchantNumberProvider> codec() {
		return CODEC;
	}

	@Override
	public void validate(ValidationContext context) {
		Validatable.validateHolder(context, "base_value", this.baseValue);
	}

	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return Set.of(LootContextParams.ATTACKING_ENTITY);
	}

	public static LootingEnchantNumberProvider applyLootingLevelTo(HolderLookup.Provider provider, Holder<ContextIntProvider> baseValue) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
		return new LootingEnchantNumberProvider(registrylookup.getOrThrow(Enchantments.LOOTING), baseValue);
	}

	@Override
	public int getIntUnsafe(LootContext context) {
		if (context.getOptional(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity living) {
			return EnchantmentHelper.getEnchantmentLevel(this.enchantment, living) + this.baseValue.value().getIntUnsafe(context);
		}
		return this.baseValue.value().getIntUnsafe(context);
	}
}
