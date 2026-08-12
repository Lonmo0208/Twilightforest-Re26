package twilightforest.init.custom;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataComponents;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.modifiers.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TravellersModifiersManager {

	// all
	public static final ResourceKey<TravellersModifier> AUTO_REPAIR_MODIFIER = makeKey("auto_repair");
	// goggles
	public static final ResourceKey<TravellersModifier> ZOOM_ABILITY = makeKey("zoom");
	public static final ResourceKey<TravellersModifier> AQUATIC_AGILITY_MODIFIER = makeKey("aquatic_agility");
	public static final ResourceKey<TravellersModifier> RED_THREAD_VISION_MODIFIER = makeKey("red_thread_vision");
	public static final ResourceKey<TravellersModifier> ALL_NIGHT_GOGGLES_MODIFIER = makeKey("all_night_goggles");
	public static final ResourceKey<TravellersModifier> ITEM_DISPLAY_MODIFIER = makeKey("item_display");
	// vest
	public static final ResourceKey<TravellersModifier> SWIFT_SWIM_ABILITY = makeKey("swift_swim");
	public static final ResourceKey<TravellersModifier> STEALTH_MODIFIER = makeKey("stealth");
	public static final ResourceKey<TravellersModifier> ARROW_MAGNETISM_MODIFIER = makeKey("arrow_magnetism");
	public static final ResourceKey<TravellersModifier> EFFICIENT_EATER_MODIFIER = makeKey("efficient_eater");
	public static final ResourceKey<TravellersModifier> PERFECT_DODGE_MODIFIER = makeKey("perfect_dodge");
	public static final ResourceKey<TravellersModifier> HASTE_MODIFIER = makeKey("haste");
	// belt
	public static final ResourceKey<TravellersModifier> SWAP_HOTBAR_ABILITY = makeKey("swap_hotbar_ability");
	public static final ResourceKey<TravellersModifier> SWAP_HOTBAR_MODIFIER = makeKey("swap_hotbar");
	// wings
	public static final ResourceKey<TravellersModifier> HIGH_JUMP_ABILITY = makeKey("high_jump");
	public static final ResourceKey<TravellersModifier> GRADUAL_GLIDE_MODIFIER = makeKey("gradual_glide");
	public static final ResourceKey<TravellersModifier> AGILE_RANGER_MODIFIER = makeKey("agile_ranger");
	public static final ResourceKey<TravellersModifier> DOUBLE_JUMP_MODIFIER = makeKey("double_jump");
	public static final ResourceKey<TravellersModifier> SIDESTEP_MODIFIER = makeKey("side_step");
	// boots
	public static final ResourceKey<TravellersModifier> STEP_UP_ABILITY = makeKey("step_up");
	public static final ResourceKey<TravellersModifier> STRAIGHT_AHEAD_MODIFIER = makeKey("straight_ahead");
	public static final ResourceKey<TravellersModifier> SLIMY_SOLES_MODIFIER = makeKey("slimy_soles");
	public static final ResourceKey<TravellersModifier> UNRESTRAINED_MODIFIER = makeKey("unrestrained");
	public static final ResourceKey<TravellersModifier> WATER_WALK_MODIFIER = makeKey("water_walk");

	public static final Set<ResourceKey<TravellersModifier>> ALWAYS_ACTIVE = Set.of(AUTO_REPAIR_MODIFIER);

	/**
	 * Fallback storage for all built-in travellers modifiers.
	 * The TRAVELLERS_MODIFIERS registry is a datapack (dynamic) registry that
	 * normally gets populated from JSON files under data/twilightforest/travellers_modifiers/.
	 * If those datapack files are not present, the registry is empty and every
	 * modifier check silently returns false — causing ALL Travellers' Gear abilities
	 * (auto-repair, double-jump, swap hotbar, haste, stealth, high-jump, sidestep,
	 * gradual glide, unrestrained, slimy-soles, straight-ahead, water-walk, step-up,
	 * magnetism, efficient-eater, perfect-dodge, agile-ranger, zoom, aquatic-agility,
	 * red-thread, all-night-goggles, item-display, swift-swim) to stop working.
	 * <p>
	 * This map mirrors everything that {@link #bootstrap} would have written into the
	 * datapack registry, so gameplay never breaks even when no datapack is supplied.
	 */
	private static final Map<ResourceKey<TravellersModifier>, TravellersModifier> FALLBACK_MODIFIERS = new ConcurrentHashMap<>();

	private static final Map<ResourceKey<TravellersModifier>, TravellersModifier> CACHED_MODIFIERS = new ConcurrentHashMap<>();
	private static final Set<ResourceKey<TravellersModifier>> MISSING_MODIFIERS = ConcurrentHashMap.newKeySet();

	static {
		// Pre-populate the fallback map so that gameplay works without datapack files.
		registerFallback(AUTO_REPAIR_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.ARMOR, TFDataComponents.AUTO_REPAIR_PROBABILITY, 0.001F, componentText(AUTO_REPAIR_MODIFIER)));
		registerFallback(ZOOM_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ZOOM_ABILITY_MODIFIER));
		registerFallback(AQUATIC_AGILITY_MODIFIER, new TravellersEntryModifier(EquipmentSlotGroup.HEAD, List.of(
			new ItemAttributeModifiers.Entry(Attributes.OXYGEN_BONUS, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_OXYGEN, EquipmentSlotGroup.HEAD),
			new ItemAttributeModifiers.Entry(Attributes.SUBMERGED_MINING_SPEED, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING, EquipmentSlotGroup.HEAD)
		), TFDataComponents.AQUATIC_AGILITY, componentText(AQUATIC_AGILITY_MODIFIER), false));
		registerFallback(RED_THREAD_VISION_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.RED_THREAD_VISION, Unit.INSTANCE, componentText(RED_THREAD_VISION_MODIFIER)));
		registerFallback(ALL_NIGHT_GOGGLES_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ALL_NIGHT_GOGGLES, Unit.INSTANCE, componentText(ALL_NIGHT_GOGGLES_MODIFIER)));
		registerFallback(ITEM_DISPLAY_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ITEM_DISPLAY, ItemDisplayContents.EMPTY, componentText(ITEM_DISPLAY_MODIFIER)));

		registerFallback(SWIFT_SWIM_ABILITY, new TravellersEntryModifier(EquipmentSlotGroup.CHEST, List.of(new ItemAttributeModifiers.Entry(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM, EquipmentSlotGroup.CHEST)), () -> TFDataComponents.SWIFT_SWIM, true));
		registerFallback(STEALTH_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.STEALTH_CROUCHING, Unit.INSTANCE, componentText(STEALTH_MODIFIER)));
		registerFallback(ARROW_MAGNETISM_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.ARROW_MAGNETISM, Unit.INSTANCE, componentText(ARROW_MAGNETISM_MODIFIER)));
		registerFallback(EFFICIENT_EATER_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.EFFICIENT_EATER, 2F, componentText(EFFICIENT_EATER_MODIFIER)));
		registerFallback(PERFECT_DODGE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.PERFECT_DODGE_PROBABILITY, 0.3F, componentText(PERFECT_DODGE_MODIFIER)));
		registerFallback(HASTE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.HASTE_AMPLIFIER, 1, componentText(HASTE_MODIFIER)));

		registerFallback(SWAP_HOTBAR_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SWAP_HOTBAR_ABILITY));
		registerFallback(SWAP_HOTBAR_MODIFIER, new TransferableComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SWAP_HOTBAR_MODIFIER, DataComponents.CONTAINER, TravellersArmorBeltItem.DEFAULT_EMPTY_BELT_CONTAINER, componentText(SWAP_HOTBAR_MODIFIER)));

		registerFallback(HIGH_JUMP_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.HIGH_JUMP_AMPLIFIER));
		registerFallback(GRADUAL_GLIDE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.GRADUALLY_GLIDING_MULTIPLIER, 1 - 1 / 6F, componentText(GRADUAL_GLIDE_MODIFIER)));
		registerFallback(AGILE_RANGER_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.AGILE_RANGER_MODIFIER, 5F, componentText(AGILE_RANGER_MODIFIER)));
		registerFallback(DOUBLE_JUMP_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.DOUBLE_JUMP, Unit.INSTANCE, componentText(DOUBLE_JUMP_MODIFIER)));
		registerFallback(SIDESTEP_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SIDESTEP_COOLDOWN, 2 * 20L, componentText(SIDESTEP_MODIFIER, Component.keybind("key.left"), Component.keybind("key.right"))));

		registerFallback(STEP_UP_ABILITY, new TravellersEntryModifier(EquipmentSlotGroup.FEET, List.of(new ItemAttributeModifiers.Entry(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP, EquipmentSlotGroup.FEET)), () -> TFDataComponents.HIGH_STEP, true));
		registerFallback(STRAIGHT_AHEAD_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER, 1.4, componentText(STRAIGHT_AHEAD_MODIFIER)));
		registerFallback(SLIMY_SOLES_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.SLIMY_SOLES_COEFFICIENT, 0.5F, componentText(SLIMY_SOLES_MODIFIER)));
		registerFallback(UNRESTRAINED_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.UNRESTRAINED, Unit.INSTANCE, componentText(UNRESTRAINED_MODIFIER)));
		registerFallback(WATER_WALK_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.WATER_WALK, Unit.INSTANCE, componentText(WATER_WALK_MODIFIER)));
	}

	private static void registerFallback(ResourceKey<TravellersModifier> key, TravellersModifier modifier) {
		FALLBACK_MODIFIERS.put(key, modifier);
	}

	private static ResourceKey<TravellersModifier> makeKey(String name) {
		return ResourceKey.create(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TwilightForestMod.prefix(name));
	}

	public static void bootstrap(BootstrapContext<TravellersModifier> context) {
		context.register(AUTO_REPAIR_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.ARMOR, TFDataComponents.AUTO_REPAIR_PROBABILITY, 0.001F, componentText(AUTO_REPAIR_MODIFIER)));
		context.register(ZOOM_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ZOOM_ABILITY_MODIFIER));
		context.register(AQUATIC_AGILITY_MODIFIER, new TravellersEntryModifier(EquipmentSlotGroup.HEAD, List.of(
			new ItemAttributeModifiers.Entry(Attributes.OXYGEN_BONUS, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_OXYGEN, EquipmentSlotGroup.HEAD),
			new ItemAttributeModifiers.Entry(Attributes.SUBMERGED_MINING_SPEED, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING, EquipmentSlotGroup.HEAD)
		), TFDataComponents.AQUATIC_AGILITY, componentText(AQUATIC_AGILITY_MODIFIER), false));
		context.register(RED_THREAD_VISION_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.RED_THREAD_VISION, Unit.INSTANCE, componentText(RED_THREAD_VISION_MODIFIER)));
		context.register(ALL_NIGHT_GOGGLES_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ALL_NIGHT_GOGGLES, Unit.INSTANCE, componentText(ALL_NIGHT_GOGGLES_MODIFIER)));
		context.register(ITEM_DISPLAY_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ITEM_DISPLAY, ItemDisplayContents.EMPTY, componentText(ITEM_DISPLAY_MODIFIER)));

		context.register(SWIFT_SWIM_ABILITY, new TravellersEntryModifier(EquipmentSlotGroup.CHEST, List.of(new ItemAttributeModifiers.Entry(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM, EquipmentSlotGroup.CHEST)), () -> TFDataComponents.SWIFT_SWIM, true));
		context.register(STEALTH_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.STEALTH_CROUCHING, Unit.INSTANCE, componentText(STEALTH_MODIFIER)));
		context.register(ARROW_MAGNETISM_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.ARROW_MAGNETISM, Unit.INSTANCE, componentText(ARROW_MAGNETISM_MODIFIER)));
		context.register(EFFICIENT_EATER_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.EFFICIENT_EATER, 2F, componentText(EFFICIENT_EATER_MODIFIER)));
		context.register(PERFECT_DODGE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.PERFECT_DODGE_PROBABILITY, 0.3F, componentText(PERFECT_DODGE_MODIFIER)));
		context.register(HASTE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.HASTE_AMPLIFIER, 1, componentText(HASTE_MODIFIER)));

		context.register(SWAP_HOTBAR_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SWAP_HOTBAR_ABILITY));
		context.register(SWAP_HOTBAR_MODIFIER, new TransferableComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SWAP_HOTBAR_MODIFIER, DataComponents.CONTAINER, TravellersArmorBeltItem.DEFAULT_EMPTY_BELT_CONTAINER, componentText(SWAP_HOTBAR_MODIFIER)));

		context.register(HIGH_JUMP_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.HIGH_JUMP_AMPLIFIER));
		context.register(GRADUAL_GLIDE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.GRADUALLY_GLIDING_MULTIPLIER, 1 - 1 / 6F, componentText(GRADUAL_GLIDE_MODIFIER)));
		context.register(AGILE_RANGER_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.AGILE_RANGER_MODIFIER, 5F, componentText(AGILE_RANGER_MODIFIER)));
		context.register(DOUBLE_JUMP_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.DOUBLE_JUMP, Unit.INSTANCE, componentText(DOUBLE_JUMP_MODIFIER)));
		context.register(SIDESTEP_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SIDESTEP_COOLDOWN, 2 * 20L, componentText(SIDESTEP_MODIFIER, Component.keybind("key.left"), Component.keybind("key.right"))));

		context.register(STEP_UP_ABILITY, new TravellersEntryModifier(EquipmentSlotGroup.FEET, List.of(new ItemAttributeModifiers.Entry(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP, EquipmentSlotGroup.FEET)), () -> TFDataComponents.HIGH_STEP, true));
		context.register(STRAIGHT_AHEAD_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER, 1.4, componentText(STRAIGHT_AHEAD_MODIFIER)));
		context.register(SLIMY_SOLES_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.SLIMY_SOLES_COEFFICIENT, 0.5F, componentText(SLIMY_SOLES_MODIFIER)));
		context.register(UNRESTRAINED_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.UNRESTRAINED, Unit.INSTANCE, componentText(UNRESTRAINED_MODIFIER)));
		context.register(WATER_WALK_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.WATER_WALK, Unit.INSTANCE, componentText(WATER_WALK_MODIFIER)));
	}

	private static List<Component> componentText(ResourceKey<TravellersModifier> modifier, Object... args) {
		return List.of(Component.translatable(modifier.identifier().toLanguageKey("travellers_gear.modifier", "description"), args));
	}

	public static boolean isModifierActive(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey, boolean spectator) {
		return getCachedModifier(registries, modifierKey).map(modifier -> modifier.isActive(stack, modifierKey, spectator)).orElse(false);
	}

	public static boolean isModifierActive(Entity entity, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		return isModifierActive(entity.registryAccess(), stack, modifierKey, entity.isSpectator());
	}

	public static boolean isModifierActive(Entity entity, ResourceKey<TravellersModifier> modifierKey) {
		return entity instanceof LivingEntity livingEntity && isModifierActive(livingEntity, modifierKey);
	}

	public static boolean isModifierActive(LivingEntity livingEntity, ResourceKey<TravellersModifier> modifierKey) {
		Optional<TravellersModifier> modifier = getCachedModifier(livingEntity.registryAccess(), modifierKey);
		if (modifier.isEmpty())
			return false;
		ItemStack equippedStack = getStackForGroup(livingEntity, modifier.get().group());
		return !equippedStack.isEmpty() && modifier.get().isActive(equippedStack, modifierKey, livingEntity.isSpectator());
	}

	public static boolean hasTravellersModifier(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		return getCachedModifier(registries, modifierKey).map(modifier -> modifier.hasModifier(stack)).orElse(false);
	}

	public static boolean addModifier(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		Optional<TravellersModifier> modifier = getCachedModifier(registries, modifierKey);
		if (modifier.isEmpty() || !(modifier.get() instanceof InsertableTravellersModifier insertableTravellersModifier))
			return false;
		return insertableTravellersModifier.addModifier(stack);
	}

	public static boolean transferModifier(HolderLookup.Provider registries, ItemStack stack, List<Ingredient> ingredients, ResourceKey<TravellersModifier> modifierKey) {
		Optional<TravellersModifier> modifier = getCachedModifier(registries, modifierKey);
		if (modifier.isEmpty() || !(modifier.get() instanceof TransferableTravellersModifier transferableTravellersModifier))
			return false;
		return transferableTravellersModifier.transfer(stack, ingredients);
	}

	public static int getModifierDataComponentProviders(HolderLookup.Provider registries, List<Ingredient> ingredients, ResourceKey<TravellersModifier> modifierKey) {
		Optional<TravellersModifier> modifier = getCachedModifier(registries, modifierKey);
		if (modifier.isEmpty() || !(modifier.get() instanceof TransferableComponentModifier transferableComponentModifier))
			return 0;
		return transferableComponentModifier.findDataComponentProviders(ingredients).size();
	}

	public static MutableComponent getModifierTooltipComponent(Holder<TravellersModifier> modifier) {
		return TooltipStringInterpolator.render(modifier.unwrapKey().orElseThrow().identifier().toLanguageKey(modifier.value().getPrefix()));
	}

	public static List<Holder<TravellersModifier>> findAllInsertableModifiers(HolderLookup.Provider registries, ItemStack stack) {
		var list = registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).listElements()
			.filter(ref -> ref.value() instanceof InsertableTravellersModifier && !ref.value().isAbility() && ref.value().hasModifier(stack))
			.map(ref -> (Holder<TravellersModifier>) ref)
			.toList();
		if (!list.isEmpty()) return list;

		return FALLBACK_MODIFIERS.entrySet().stream()
			.filter(entry -> entry.getValue() instanceof InsertableTravellersModifier mod && !mod.isAbility() && mod.hasModifier(stack))
			.map(entry -> (Holder<TravellersModifier>) new FallbackHolder<>(registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS), entry.getKey(), entry.getValue()))
			.toList();
	}

	public static List<Holder<TravellersModifier>> findAllInsertableModifiers(Entity entity, ItemStack stack) {
		return findAllInsertableModifiers(entity.registryAccess(), stack);
	}

	public static List<Holder<TravellersModifier>> findAllInsertableModifiers(Level level, ItemStack stack) {
		return findAllInsertableModifiers(level.registryAccess(), stack);
	}

	public static List<Holder<TravellersModifier>> findAllAbilityModifiers(HolderLookup.Provider registries, ItemStack stack) {
		var list = registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).listElements()
			.filter(ref -> ref.value().isAbility() && ref.value().hasModifier(stack))
			.map(ref -> (Holder<TravellersModifier>) ref)
			.toList();
		if (!list.isEmpty()) return list;

		return FALLBACK_MODIFIERS.entrySet().stream()
			.filter(entry -> entry.getValue().isAbility() && entry.getValue().hasModifier(stack))
			.map(entry -> (Holder<TravellersModifier>) new FallbackHolder<>(registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS), entry.getKey(), entry.getValue()))
			.toList();
	}

	private static final class FallbackHolder<T> extends Holder.Reference<T> {
		FallbackHolder(HolderLookup.RegistryLookup<T> registry, ResourceKey<T> key, T value) {
			super(Holder.Reference.Type.STAND_ALONE, registry, key, value);
			bindComponents(DataComponentMap.EMPTY);
		}

		@Override
		public boolean is(TagKey<T> tag) {
			return false;
		}

		@Override
		public int hashCode() {
			return key().hashCode() * 31 + System.identityHashCode(value());
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof Holder<?> other)) return false;
			return unwrapKey().equals(other.unwrapKey()) && value() == other.value();
		}
	}

	public static long countInsertableModifiers(HolderLookup.Provider registries, ItemStack stack) {
		return findAllInsertableModifiers(registries, stack).size();
	}

	public static boolean isModifierEnabled(HolderLookup.Provider registries, ResourceKey<TravellersModifier> modifierKey) {
		return getCachedModifier(registries, modifierKey).isPresent();
	}

	private static ItemStack getStackForGroup(LivingEntity livingEntity, EquipmentSlotGroup group) {
		EquipmentSlot matchedSlot = null;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (!slot.isArmor() || !group.test(slot))
				continue;
			if (matchedSlot != null) {
				// Multi-slot group (e.g. ARMOR). Rather than return EMPTY and guarantee
				// detection failure, fall back to the first matching slot so callers using
				// the per-stack isModifierActive() overload still get correct behaviour
				// if the modifier happens to be on that first slot.
				break;
			}
			matchedSlot = slot;
		}
		return matchedSlot == null ? ItemStack.EMPTY : livingEntity.getItemBySlot(matchedSlot);
	}

	public static void clearCache() {
		CACHED_MODIFIERS.clear();
		MISSING_MODIFIERS.clear();
	}

	private static Optional<TravellersModifier> getCachedModifier(HolderLookup.Provider registries, ResourceKey<TravellersModifier> modifierKey) {
		TravellersModifier cached = CACHED_MODIFIERS.get(modifierKey);
		if (cached != null)
			return Optional.of(cached);
		if (MISSING_MODIFIERS.contains(modifierKey))
			return Optional.empty();

		// 1) Try the dynamic (datapack) registry first.
		Optional<Holder.Reference<TravellersModifier>> modifier = registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).get(modifierKey);
		if (modifier.isPresent()) {
			CACHED_MODIFIERS.put(modifierKey, modifier.get().value());
			return Optional.of(modifier.get().value());
		}

		// 2) Fall back to the static built-in map so that gameplay works even when
		//    no datapack is present (e.g. when runDatagen has never been executed).
		TravellersModifier fallback = FALLBACK_MODIFIERS.get(modifierKey);
		if (fallback != null) {
			CACHED_MODIFIERS.put(modifierKey, fallback);
			return Optional.of(fallback);
		}

		TwilightForestMod.LOGGER.warn("Travellers modifier {} is not present in the registry or fallback map", modifierKey.identifier());
		MISSING_MODIFIERS.add(modifierKey);
		return Optional.empty();
	}

	public static final class CacheInvalidationReloadListener extends SimplePreparableReloadListener<Unit> {
		public static final CacheInvalidationReloadListener INSTANCE = new CacheInvalidationReloadListener();

		private CacheInvalidationReloadListener() {
		}

		@Override
		protected Unit prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
			return Unit.INSTANCE;
		}

		@Override
		protected void apply(Unit object, ResourceManager resourceManager, ProfilerFiller profiler) {
			clearCache();
		}
	}
}
