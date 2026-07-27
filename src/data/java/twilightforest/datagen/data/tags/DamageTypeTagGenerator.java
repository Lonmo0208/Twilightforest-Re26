package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import twilightforest.init.TFDamageTypes;
import twilightforest.tags.TFDamageTypeTags;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

public class DamageTypeTagGenerator extends FabricTagsProvider<DamageType> {

	// NeoForge convention tags not present in Fabric's conventional tags
	private static final TagKey<DamageType> IS_PHYSICAL = TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("c", "is_physical"));
	private static final TagKey<DamageType> IS_MAGIC = TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("c", "is_magic"));
	private static final TagKey<DamageType> IS_ENVIRONMENT = TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("c", "is_environment"));
	private static final TagKey<DamageType> IS_TECHNICAL = TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("c", "is_technical"));

	public DamageTypeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, Registries.DAMAGE_TYPE, future);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		addDamageTypeTags(TFDamageTypes.GHAST_TEAR, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.DAMAGES_HELMET, IS_PHYSICAL, DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
		addDamageTypeTags(TFDamageTypes.HYDRA_BITE, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.HYDRA_FIRE, DamageTypeTags.IS_FIRE, DamageTypeTags.IGNITES_ARMOR_STANDS, DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
		addDamageTypeTags(TFDamageTypes.HYDRA_MORTAR, DamageTypeTags.IS_FIRE, DamageTypeTags.IGNITES_ARMOR_STANDS, DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
		addDamageTypeTags(TFDamageTypes.LICH_BOLT, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, DamageTypeTags.WITCH_RESISTANT_TO, TFDamageTypeTags.BREAKS_LICH_SHIELDS, IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		addDamageTypeTags(TFDamageTypes.LICH_BOMB, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, DamageTypeTags.WITCH_RESISTANT_TO, IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR, DamageTypeTags.IS_EXPLOSION);
		addDamageTypeTags(TFDamageTypes.CHILLING_BREATH, IS_MAGIC);
		addDamageTypeTags(TFDamageTypes.SQUISH, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.THROWN_AXE, DamageTypeTags.IS_PROJECTILE, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.THROWN_PICKAXE, DamageTypeTags.IS_PROJECTILE, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.THORNS, IS_ENVIRONMENT, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.KNIGHTMETAL, IS_ENVIRONMENT);
		addDamageTypeTags(TFDamageTypes.FIERY, DamageTypeTags.IS_FIRE, IS_ENVIRONMENT, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.FIRE_JET, DamageTypeTags.IS_FIRE, IS_ENVIRONMENT, IS_PHYSICAL, DamageTypeTags.IGNITES_ARMOR_STANDS);
		addDamageTypeTags(TFDamageTypes.REACTOR, IS_ENVIRONMENT, IS_MAGIC);
		addDamageTypeTags(TFDamageTypes.SLIDER, IS_ENVIRONMENT, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.THROWN_BLOCK, DamageTypeTags.DAMAGES_HELMET, DamageTypeTags.IS_PROJECTILE, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.AXING, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.SLAM, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.NO_ANGER, IS_PHYSICAL, DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
		addDamageTypeTags(TFDamageTypes.YEETED, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_FALL, IS_PHYSICAL, DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
		addDamageTypeTags(TFDamageTypes.ANT, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.HAUNT, IS_MAGIC);
		addDamageTypeTags(TFDamageTypes.CLAMPED, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.SCORCHED, DamageTypeTags.IS_FIRE, DamageTypeTags.IGNITES_ARMOR_STANDS);
		addDamageTypeTags(TFDamageTypes.FROZEN, IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		addDamageTypeTags(TFDamageTypes.SPIKED, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.LEAF_BRAIN, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, DamageTypeTags.WITCH_RESISTANT_TO, IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		addDamageTypeTags(TFDamageTypes.LOST_WORDS, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, DamageTypeTags.WITCH_RESISTANT_TO, IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		addDamageTypeTags(TFDamageTypes.SCHOOLED, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, DamageTypeTags.WITCH_RESISTANT_TO, IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		addDamageTypeTags(TFDamageTypes.SNOWBALL_FIGHT, DamageTypeTags.IS_PROJECTILE, IS_MAGIC);
		addDamageTypeTags(TFDamageTypes.TWILIGHT_SCEPTER, DamageTypeTags.IS_PROJECTILE, TFDamageTypeTags.BREAKS_LICH_SHIELDS, IS_MAGIC);
		addDamageTypeTags(TFDamageTypes.LIFEDRAIN, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		addDamageTypeTags(TFDamageTypes.EXPIRED, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_RESISTANCE, DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL, DamageTypeTags.BYPASSES_INVULNERABILITY, IS_TECHNICAL, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		addDamageTypeTags(TFDamageTypes.FALLING_ICE, DamageTypeTags.BYPASSES_ENCHANTMENTS, IS_ENVIRONMENT, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.MOONWORM, IS_PHYSICAL);
		addDamageTypeTags(TFDamageTypes.ACID_RAIN, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.WITCH_RESISTANT_TO, IS_ENVIRONMENT, IS_MAGIC, DamageTypeTags.BYPASSES_WOLF_ARMOR);
		addDamageTypeTags(TFDamageTypes.OMINOUS_FIRE, IS_MAGIC, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.WITHER_IMMUNE_TO, DamageTypeTags.NO_KNOCKBACK, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);

		this.builder(TFDamageTypeTags.BREAKS_LICH_SHIELDS).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.SONIC_BOOM);
	}

	@SafeVarargs
	private void addDamageTypeTags(ResourceKey<DamageType> type, TagKey<DamageType>... tags) {
		for (TagKey<DamageType> key : tags) {
			builder(key).add(type);
		}
	}
}