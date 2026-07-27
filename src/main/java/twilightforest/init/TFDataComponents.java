package twilightforest.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.*;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.custom.MagicPaintingVariants;

import java.util.UUID;
import net.minecraft.core.Registry;

public class TFDataComponents {

	public static final DataComponentType<Unit> EMPERORS_CLOTH = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).build();
	public static final DataComponentType<PotionFlaskComponent> POTION_FLASK_CONTENTS = DataComponentType.<PotionFlaskComponent>builder().persistent(PotionFlaskComponent.CODEC).networkSynchronized(PotionFlaskComponent.STREAM_CODEC).build();
	public static final DataComponentType<Unit> INFINITE_GLASS_SWORD = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).build();
	public static final DataComponentType<UUID> THROWN_PROJECTILE = DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).build();
	public static final DataComponentType<String> EXPERIMENT_115_VARIANTS = DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build();
	public static final DataComponentType<SkullCandles> SKULL_CANDLES = DataComponentType.<SkullCandles>builder().persistent(SkullCandles.CODEC).networkSynchronized(SkullCandles.STREAM_CODEC).build();
	public static final DataComponentType<CandelabraData> CANDELABRA_DATA = DataComponentType.<CandelabraData>builder().persistent(CandelabraData.CODEC).build();
	public static final DataComponentType<Holder<MagicPaintingVariant>> MAGIC_PAINTING_VARIANT = DataComponentType.<Holder<MagicPaintingVariant>>builder().persistent(MagicPaintingVariants.CODEC).networkSynchronized(MagicPaintingVariants.STREAM_CODEC).build();
	public static final DataComponentType<Unit> TRANSLATABLE_BOOK = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).build();
	public static final DataComponentType<JarLid> JAR_LID = DataComponentType.<JarLid>builder().persistent(JarLid.CODEC).build();
	public static final DataComponentType<Integer> CASKET_DAMAGE = DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build();

	public static final DataComponentType<OreScannerComponent> ORE_SCANNING = DataComponentType.<OreScannerComponent>builder().persistent(OreScannerComponent.CODEC).build();
	public static final DataComponentType<OreScannerData> ORE_DATA = DataComponentType.<OreScannerData>builder().persistent(OreScannerData.CODEC).networkSynchronized(OreScannerData.STREAM_CODEC).build();
	public static final DataComponentType<Integer> ORE_LOADING = DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT.orElse(0)).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build();
	public static final DataComponentType<Integer> ORE_RANGE = DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT.orElse(1)).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build();
	public static final DataComponentType<Block> ORE_FILTER = DataComponentType.<Block>builder().persistent(BuiltInRegistries.BLOCK.byNameCodec().orElse(Blocks.AIR)).networkSynchronized(ByteBufCodecs.registry(Registries.BLOCK)).cacheEncoding().build();

	public static final DataComponentType<Unit> IS_TRAVELLERS_GEAR = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<ItemAttributeModifiers> STORED_BROKEN_ATTRIBUTES = DataComponentType.<ItemAttributeModifiers>builder().persistent(ItemAttributeModifiers.CODEC).networkSynchronized(ItemAttributeModifiers.STREAM_CODEC).cacheEncoding().build();

	public static final DataComponentType<Unit> TRAVELLERS_HAS_CHESTPLATE = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> TRAVELLERS_HAS_GLOVES = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> TRAVELLERS_HAS_BELT = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> TRAVELLERS_HAS_WINGS = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> TRAVELLERS_HAS_BOOTS = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();

	public static final DataComponentType<Float> AUTO_REPAIR_PROBABILITY = DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build();
	public static final DataComponentType<Float> ZOOM_ABILITY_MODIFIER = DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build();
	public static final DataComponentType<Unit> RED_THREAD_VISION = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> STEALTH_CROUCHING = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> ARROW_MAGNETISM = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Float> EFFICIENT_EATER = DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build();
	public static final DataComponentType<Float> PERFECT_DODGE_PROBABILITY = DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build();
	public static final DataComponentType<Integer> HASTE_AMPLIFIER = DataComponentType.<Integer>builder().persistent(ExtraCodecs.UNSIGNED_BYTE).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build();
	public static final DataComponentType<Unit> SWAP_HOTBAR_ABILITY = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> SWAP_HOTBAR_MODIFIER = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Integer> HIGH_JUMP_AMPLIFIER = DataComponentType.<Integer>builder().persistent(ExtraCodecs.UNSIGNED_BYTE).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build();
	public static final DataComponentType<Float> GRADUALLY_GLIDING_MULTIPLIER = DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build();
	public static final DataComponentType<Float> AGILE_RANGER_MODIFIER = DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build();
	public static final DataComponentType<Unit> DOUBLE_JUMP = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Long> SIDESTEP_COOLDOWN = DataComponentType.<Long>builder().persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG).cacheEncoding().build();
	public static final DataComponentType<Double> STRAIGHT_AHEAD_MULTIPLIER = DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build();
	public static final DataComponentType<Float> SLIMY_SOLES_COEFFICIENT = DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build();
	public static final DataComponentType<Unit> WATER_WALK = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> ALL_NIGHT_GOGGLES = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<ItemDisplayContents> ITEM_DISPLAY = DataComponentType.<ItemDisplayContents>builder().persistent(ItemDisplayContents.CODEC).networkSynchronized(ItemDisplayContents.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> UNRESTRAINED = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();

	public static final DataComponentType<Unit> SWIFT_SWIM = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> HIGH_STEP = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();
	public static final DataComponentType<Unit> AQUATIC_AGILITY = DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build();

	public static void init() {
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("emperors_cloth"), EMPERORS_CLOTH);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("flask_contents"), POTION_FLASK_CONTENTS);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("infinite_glass_sword"), INFINITE_GLASS_SWORD);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("thrown_projectile"), THROWN_PROJECTILE);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("e115_variant"), EXPERIMENT_115_VARIANTS);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("skull_candles"), SKULL_CANDLES);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("candelabra_data"), CANDELABRA_DATA);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("magic_painting_variant"), MAGIC_PAINTING_VARIANT);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("translatable_book"), TRANSLATABLE_BOOK);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("jar_lid"), JAR_LID);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("casket_damage"), CASKET_DAMAGE);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("ore_scanner"), ORE_SCANNING);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("ore_data"), ORE_DATA);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("ore_loading"), ORE_LOADING);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("ore_range"), ORE_RANGE);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("ore_filter"), ORE_FILTER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("travellers_armor"), IS_TRAVELLERS_GEAR);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("stored_broken_attributes"), STORED_BROKEN_ATTRIBUTES);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("has_travellers_chestplate"), TRAVELLERS_HAS_CHESTPLATE);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("has_travellers_gloves"), TRAVELLERS_HAS_GLOVES);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("has_travellers_belt"), TRAVELLERS_HAS_BELT);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("has_travellers_wings"), TRAVELLERS_HAS_WINGS);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("has_travellers_boots"), TRAVELLERS_HAS_BOOTS);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("auto_repair_probability"), AUTO_REPAIR_PROBABILITY);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("zoom_ability_modifier"), ZOOM_ABILITY_MODIFIER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("red_thread_vision"), RED_THREAD_VISION);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("stealth_crouching"), STEALTH_CROUCHING);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("arrow_magnetism"), ARROW_MAGNETISM);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("efficient_eater"), EFFICIENT_EATER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("perfect_dodge_probability"), PERFECT_DODGE_PROBABILITY);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("haste_amplifier"), HASTE_AMPLIFIER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("swap_hotbar_ability"), SWAP_HOTBAR_ABILITY);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("swap_hotbar_modifier"), SWAP_HOTBAR_MODIFIER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("high_jump_amplifier"), HIGH_JUMP_AMPLIFIER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("gradually_gliding_multiplier"), GRADUALLY_GLIDING_MULTIPLIER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("agile_ranger_modifier"), AGILE_RANGER_MODIFIER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("double_jump"), DOUBLE_JUMP);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("sidestep_cooldown"), SIDESTEP_COOLDOWN);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("straight_ahead_multiplier"), STRAIGHT_AHEAD_MULTIPLIER);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("slimy_soles_coefficient"), SLIMY_SOLES_COEFFICIENT);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("water_walk"), WATER_WALK);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("all_night_goggles"), ALL_NIGHT_GOGGLES);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("item_display"), ITEM_DISPLAY);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("unrestrained"), UNRESTRAINED);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("swift_swim"), SWIFT_SWIM);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("high_step"), HIGH_STEP);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, TwilightForestMod.prefix("aquatic_agility"), AQUATIC_AGILITY);
	}
}