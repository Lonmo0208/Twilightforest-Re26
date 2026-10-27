package twilightforest.events;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import twilightforest.beanification.Component;
import twilightforest.beanification.PostConstruct;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.SlimySolesAttachment;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.inventory.InventoryUtil;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.item.travellers_gear.modifiers.InsertableTravellersModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.network.GradualGlidePacket;
import twilightforest.network.ParticlePacket;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import twilightforest.network.PacketDistributor;
import twilightforest.util.TFEntityExtensions;

@Component
public class TravellersGearEvents {
	private static final List<AttachmentType<?>> ATTACHMENTS_TO_PRESERVE_ON_DEATH = List.of(
		TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION
	);

	@PostConstruct
	private void setup() {
		// Player tick (pre): movement modifiers, double jump state, step height, stealth, and per-entity modifier updates
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				FabricEvents.PlayerTickEvent.Pre pre = new FabricEvents.PlayerTickEvent.Pre(player);
				tickMovementModifiers(pre);
				disableHighStepWhileSneaking(pre);

				// Also run the post-tick and entity-tick logic for players
				FabricEvents.PlayerTickEvent.Post post = new FabricEvents.PlayerTickEvent.Post(player);
				performStealth(post);

				// Run entity tick logic for this player (covers most travellers gear modifiers)
				if (player.getItemBySlot(EquipmentSlot.CHEST).has(TFDataComponents.IS_TRAVELLERS_GEAR)
					|| player.getItemBySlot(EquipmentSlot.FEET).has(TFDataComponents.IS_TRAVELLERS_GEAR)
					|| player.getItemBySlot(EquipmentSlot.HEAD).has(TFDataComponents.IS_TRAVELLERS_GEAR)
					|| player.getItemBySlot(EquipmentSlot.LEGS).has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
					FabricEvents.EntityTickEvent.Post entityPost = new FabricEvents.EntityTickEvent.Post(player);
					updateOtherModifiers(entityPost);
				}
			}
		});

		// Projectile hit (for magnetize/perfect dodge): handled via AbstractArrowMixin
		// Fall damage, jump, armor hurt, anvil, grindstone, crafting: handled via dedicated mixins

		// Player data copy on death/respawn
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			FabricEvents.PlayerEvent.Clone clone = new FabricEvents.PlayerEvent.Clone(newPlayer, oldPlayer, !alive);
			keepAttachmentsOnDeath(clone);
		});

		// === Handlers NOT PORTED (no Fabric equivalent, would need Mixins) ===
		// - reduceSlimySolesFallDamage    → No Fabric LivingFallEvent (need Mixin in LivingEntity)
		// - cancelSlimySolesJump          → No Fabric LivingJumpEvent (need Mixin in LivingEntity)
		// - activateAndDeactivateTravellersModifiers → No Fabric ItemAttributeModifierEvent
		// - stopDamagingTravellersGear    → No Fabric ArmorHurtEvent
		// - setLastDamageArmorTime        → No Fabric ArmorHurtEvent
		// - cancelCombiningTravellersGear → No Fabric AnvilUpdateEvent
		// - removeModifiersFromTravellersGear → No Fabric GrindstoneEvent.OnPlaceItem
		// - extractItemsFromSwapHotbarModifier → No Fabric GrindstoneEvent.OnTakeItem
		// - cancelPhantomSpawns           → No Fabric phantom spawn event
		// - fireCraftingModifierTrigger   → No Fabric ItemCraftedEvent
		//
		// These methods are kept below as dead code for reference.
		// TODO: Implement dedicated Mixins for each of these if needed.
	}

	public static void magnetizeArrows(FabricEvents.ProjectileImpactEvent event) {
		Projectile projectile = (Projectile) event.getProjectile();
		Entity entity = projectile.getOwner();
		if (!(entity instanceof LivingEntity livingEntity) || !event.getRayTraceResult().getType().equals(HitResult.Type.BLOCK) || projectile.tickCount >= 200)
			return;

		if (!TravellersModifiersManager.isModifierActive(livingEntity, TravellersModifiersManager.ARROW_MAGNETISM_MODIFIER)
			|| !(projectile instanceof AbstractArrow arrow) || projectile.level().isClientSide())
			return;

		if (!(livingEntity instanceof Player player)) {
			projectile.discard();
			return;
		}
		AbstractArrow.Pickup pickup = arrow.pickup;
		if (!player.hasInfiniteMaterials() && pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
			InventoryUtil.giveItemToPlayer(player, arrow.getPickupItemStackOrigin());
			player.getInventory().setChanged();
		}
		if (pickup.equals(AbstractArrow.Pickup.ALLOWED) || pickup.equals(AbstractArrow.Pickup.CREATIVE_ONLY) && player.isCreative())
			projectile.discard();
	}

	public static void performPerfectDodge(FabricEvents.ProjectileImpactEvent event) {
		HitResult rayResult = event.getRayTraceResult();
		if (!(rayResult instanceof EntityHitResult entityHitResult) || !(entityHitResult.getEntity() instanceof LivingEntity livingEntity))
			return;
		ItemStack chest = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		Float probability = chest.get(TFDataComponents.PERFECT_DODGE_PROBABILITY);
		Level level = livingEntity.level();
		if (!TravellersModifiersManager.isModifierActive(livingEntity, chest, TravellersModifiersManager.PERFECT_DODGE_MODIFIER) || probability == null)
			return;
		if (level.isClientSide()) {
			event.setCanceled(true); // always cancel on the client side because the game sends a damage packet when it hits the player
			return;
		}
		if (probability <= level.getRandom().nextFloat())
			return;
		Entity projectile = event.getProjectile();
		Vec3 hitPosition = projectile.position().add(projectile.getDeltaMovement());
		level.playSound(null, hitPosition.x(), hitPosition.y(), hitPosition.z(), TFSounds.PERFECT_DODGE, livingEntity.getSoundSource(), 1.5F, livingEntity.getVoicePitch());
		event.setCanceled(true);
		ParticlePacket particlePacket = new ParticlePacket();
		for (int particleNumber = 0; particleNumber < 20; particleNumber++) {
			Vec3 particleVelocity = new Vec3(
				(level.getRandom().nextDouble() - 0.5),
				(level.getRandom().nextDouble() - 0.5),
				(level.getRandom().nextDouble() - 0.5)
			);
			ParticleOptions type = TFParticleType.PERFECT_DODGE;
			particlePacket.queueParticle(type, hitPosition, particleVelocity);
		}
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, particlePacket);
	}

	private void reduceSlimySolesFallDamage(FabricEvents.LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntity();
		ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);
		Float coefficient = boots.get(TFDataComponents.SLIMY_SOLES_COEFFICIENT);
		SlimySolesAttachment slimySolesAttachment = TFDataAttachments.getOrCreate(livingEntity, TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, twilightforest.components.entity.SlimySolesAttachment::new);
		if (!livingEntity.isShiftKeyDown() && TravellersModifiersManager.isModifierActive(livingEntity, boots, TravellersModifiersManager.SLIMY_SOLES_MODIFIER) && coefficient != null && (calculateFallDamage(event) > 0 || slimySolesAttachment.forceBounce)) {
			event.setCanceled(true);
			slimySolesAttachment.bounceVelocity = -livingEntity.getDeltaMovement().y() * Math.sqrt(coefficient);
			slimySolesAttachment.doubleJumpBoostVelocity = slimySolesAttachment.bounceVelocity;
			slimySolesAttachment.hasBounced = false;
			livingEntity.setAttached(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, slimySolesAttachment);
		}
	}

	// [VanillaCopy]
	private double calculateFallDamage(FabricEvents.LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntity();
		double safeFallDistance = livingEntity.getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
		double unsafeFallDistance = event.getDistance() - safeFallDistance;
		return Mth.ceil(unsafeFallDistance * event.getDamageMultiplier() * livingEntity.getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER));
	}

	private void cancelSlimySolesJump(FabricEvents.LivingEvent.LivingJumpEvent event) {
		LivingEntity livingEntity = event.getEntity();
		SlimySolesAttachment slimySolesAttachment = TFDataAttachments.getOrCreate(livingEntity, TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, twilightforest.components.entity.SlimySolesAttachment::new);
		slimySolesAttachment.bounceVelocity = 0;
		slimySolesAttachment.forceBounce = false;
		livingEntity.setAttached(TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, slimySolesAttachment);
	}

	private void tickMovementModifiers(FabricEvents.PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Boolean hasDoubleJump = null;
		boolean doubleJumpModifierActive = TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER);
		if (!doubleJumpModifierActive)
			hasDoubleJump = false;
		else if (player.onGround() || player.isInLiquid() || player.onClimbable())
			hasDoubleJump = true;

		boolean currentHasDoubleJump = Boolean.TRUE.equals(TFDataAttachments.getOrCreate(player, TFDataAttachments.HAS_DOUBLE_JUMP, () -> false));
		if (hasDoubleJump != null && hasDoubleJump != currentHasDoubleJump) {
			player.setAttached(TFDataAttachments.HAS_DOUBLE_JUMP, hasDoubleJump);
			player.setAttached(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
			AttributeInstance instance = player.getAttribute(Attributes.SAFE_FALL_DISTANCE);
			if (instance != null)
				instance.removeModifier(TFAttributeModifiers.TRAVELLERS_DOUBLE_JUMP_SAFE_FALL_DISTANCE);
		}

		if (!player.level().isClientSide()) {
			boolean modifierActive = TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER);
			if (!modifierActive && Boolean.TRUE.equals(TFDataAttachments.getOrCreate(player, TFDataAttachments.IS_GRADUALLY_GLIDING, () -> false))) {
				player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, false);
				PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new GradualGlidePacket(false, player.getUUID()));
			}
		}

		//reset double jump wing anim if on the ground
		if (event.getEntity().level().isClientSide()) {
			var anim = TFDataAttachments.getOrCreate(player, TFDataAttachments.TRAVELLERS_WINGS_ANIM, twilightforest.components.entity.TravellersWingsAnimAttachment::new);
			if (anim.doubleJump && player.onGround()) {
				anim.doubleJump = false;
			}
		}

		TravellersGearLogic.travellersWingsSidestepCooldownSound(player);
	}

	private void performStealth(FabricEvents.PlayerTickEvent.Post event) {
		if (!event.getEntity().level().isClientSide()) {
			TravellersGearLogic.travellersStealth(event.getEntity(), player1 -> player1.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2, 0, false, false, false)));
		}
	}

	private void disableHighStepWhileSneaking(FabricEvents.PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.STEP_UP_ABILITY))
			return;
		AttributeInstance attribute = player.getAttributes().getInstance(Attributes.STEP_HEIGHT);
		if (attribute == null)
			return;

		boolean shouldHaveHighStepModifier = !player.isCrouching();
		boolean hasHighStepModifier = attribute.hasModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP.id());
		if (!shouldHaveHighStepModifier && hasHighStepModifier)
			attribute.removeModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP);
		if (shouldHaveHighStepModifier && !hasHighStepModifier)
			attribute.addPermanentModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP);
	}

	private void updateOtherModifiers(FabricEvents.EntityTickEvent.Post event) {
		if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
		TravellersGearLogic.travellersWingsGradualGlide(livingEntity);
		TravellersGearLogic.travellersBootsUnrestrained(livingEntity);
		TravellersGearLogic.travellersBootsSlimySolesBounce(livingEntity);
		TravellersGearLogic.waterWalkingTick(livingEntity);
		this.applyAquaticAgilityTransient(livingEntity);

		if (livingEntity.level().isClientSide()) return;

		TravellersGearLogic.travellersVestHaste(livingEntity);
		TravellersGearLogic.travellersWingsHighJump(livingEntity);
		TravellersGearLogic.travellersGearAutoRepair(livingEntity);
		TravellersGearLogic.travellersBootsStraightAhead(livingEntity);
		TravellersGearLogic.determineWingState(livingEntity);

		if (livingEntity instanceof Player player) {
			boolean shouldHaveNightVision = TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER);
			MobEffectInstance existingNightVision = player.getEffect(MobEffects.NIGHT_VISION);
			boolean isOurNightVision = existingNightVision != null
				&& existingNightVision.getDuration() >= 399
				&& existingNightVision.getDuration() <= 401
				&& !existingNightVision.isVisible();

			if (shouldHaveNightVision) {
				if (!isOurNightVision) {
					int duration = 20 * 20; // 20 seconds
					MobEffectInstance nightVision = new MobEffectInstance(MobEffects.NIGHT_VISION, duration, 0, false, false);
					player.addEffect(nightVision, player);
				}
			} else if (isOurNightVision) {
				player.removeEffect(MobEffects.NIGHT_VISION);
			}
		}
	}

	private void applyAquaticAgilityTransient(LivingEntity livingEntity) {
		ItemStack headStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
		boolean hasModifier = TravellersModifiersManager.isModifierActive(livingEntity, headStack, TravellersModifiersManager.AQUATIC_AGILITY_MODIFIER);
		AttributeInstance miningAttr = livingEntity.getAttribute(Attributes.SUBMERGED_MINING_SPEED);

		// NOTE: oxygenBonus is already correctly applied via ItemStack ATTRIBUTE_MODIFIERS.
		// Only submerged_mining_speed (ADD_MULTIPLIED_TOTAL) fails to apply through the
		// vanilla equipment attribute pipeline for an unknown reason, so we patch it on
		// every tick with a transient modifier.
		if (miningAttr != null) {
			boolean shouldHave = hasModifier;
			boolean hasMining = miningAttr.hasModifier(TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING.id());
			if (shouldHave != hasMining) {
				if (shouldHave) {
					miningAttr.addOrUpdateTransientModifier(TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING);
				} else {
					miningAttr.removeModifier(TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING);
				}
			}
		}
	}

	private void activateAndDeactivateTravellersModifiers(FabricEvents.ItemAttributeModifierEvent event) {
		if (null == null)
			return;

		ItemStack armor = event.getItemStack();
		if (!armor.has(TFDataComponents.IS_TRAVELLERS_GEAR) || !armor.isDamageableItem())
			return;

		if (armor.getMaxDamage() - 1 <= armor.getDamageValue()) {
			if (armor.has(DataComponents.ATTRIBUTE_MODIFIERS)) {
				Set<ItemAttributeModifiers.Entry> entries = new LinkedHashSet<>(armor.get(DataComponents.ATTRIBUTE_MODIFIERS).modifiers());
				if (armor.has(TFDataComponents.STORED_BROKEN_ATTRIBUTES)) {
					entries.addAll(armor.get(TFDataComponents.STORED_BROKEN_ATTRIBUTES).modifiers());
				}
				armor.set(TFDataComponents.STORED_BROKEN_ATTRIBUTES, new ItemAttributeModifiers(entries.stream().toList()));
				event.clearModifiers();
			}
		} else {
			if (armor.has(TFDataComponents.STORED_BROKEN_ATTRIBUTES)) {
				armor.get(TFDataComponents.STORED_BROKEN_ATTRIBUTES).modifiers().forEach(entry -> event.replaceModifier(entry.attribute(), entry.modifier(), entry.slot()));
				armor.remove(TFDataComponents.STORED_BROKEN_ATTRIBUTES);
				armor.set(DataComponents.ATTRIBUTE_MODIFIERS, event.build());
			}
		}
	}

	private void stopDamagingTravellersGear(FabricEvents.ArmorHurtEvent event) {
		if (event.isCanceled())
			return;
		event.getArmorMap().forEach((slot, entry) -> {
			ItemStack damagedStack = event.getArmorItemStack(slot);
			if (!damagedStack.has(TFDataComponents.IS_TRAVELLERS_GEAR))
				return;
			if (damagedStack.getDamageValue() + event.getNewDamage(slot) >= damagedStack.getMaxDamage()) {
				event.setNewDamage(slot, damagedStack.getMaxDamage() - damagedStack.getDamageValue() - 1);
			} else if (damagedStack.getDamageValue() + event.getNewDamage(slot) >= damagedStack.getMaxDamage() - 1 && event.getEntity() instanceof ServerPlayer player) {
				player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS, 1.0F, player.getVoicePitch(), false);
			}
		});
	}

	private void setLastDamageArmorTime(FabricEvents.ArmorHurtEvent event) {
		if (Arrays.stream(EquipmentSlot.values()).noneMatch(slot -> event.getNewDamage(slot) > 0)) return;
		LivingEntity entity = event.getEntity();
		entity.setAttached(TFDataAttachments.LAST_DAMAGE_ARMOR_TIME, entity.level().getGameTime());
	}


	private void cancelCombiningTravellersGear(FabricEvents.AnvilUpdateEvent event) {
		if (event.getLeft().has(TFDataComponents.IS_TRAVELLERS_GEAR) && event.getRight().has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
			event.setCanceled(true);
		}
	}

	private void removeModifiersFromTravellersGear(FabricEvents.GrindstoneEvent.OnPlaceItem event) {
		if (null == null)
			return;
		RegistryAccess access = /* TODO: Port - registryAccess */ null;
		List<ItemStack> travellersItemStacks = Stream.of(event.getTopItem(), event.getBottomItem())
				.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR))
				.toList();

		if (travellersItemStacks.isEmpty())
			return; // Delegate to vanilla logic
		if (travellersItemStacks.size() > 1) {
			event.setCanceled(true);
			return;
		}
		ItemStack inputStack = travellersItemStacks.getFirst();
		List<Holder<TravellersModifier>> modifiers = TravellersModifiersManager.findAllInsertableModifiers(access, inputStack);
		if (modifiers.isEmpty()) {
			event.setCanceled(true);
			return;
		}

		ItemStack unmodifiedStack = inputStack.copy();
		modifiers.forEach(modifier -> ((InsertableTravellersModifier) modifier.value()).removeModifier(unmodifiedStack));
		event.setOutput(unmodifiedStack.copy());
	}

	private void extractItemsFromSwapHotbarModifier(FabricEvents.GrindstoneEvent.OnTakeItem event) {
		returnModifierItems(event,
			TravellersModifiersManager.SWAP_HOTBAR_MODIFIER,
			DataComponents.CONTAINER,
			ItemContainerContents::nonEmptyItemCopyStream
		);

		returnModifierItems(event,
			TravellersModifiersManager.ITEM_DISPLAY_MODIFIER,
			TFDataComponents.ITEM_DISPLAY,
			contents -> contents.items().stream()
		);
	}

	private <T> void returnModifierItems(FabricEvents.GrindstoneEvent.OnTakeItem event, ResourceKey<TravellersModifier> modifierKey, DataComponentType<T> componentType, Function<T, Stream<ItemStack>> itemStreamExtractor) {
		getUniqueTravellersGear(event.getTopItem(), event.getBottomItem(), stack ->
			TravellersModifiersManager.hasTravellersModifier(event.getPlayer().registryAccess(), stack, modifierKey)
		).map(stack -> stack.get(componentType))
			.ifPresent(component ->
				itemStreamExtractor.apply(component)
					.forEach(itemStack -> InventoryUtil.giveItemToPlayer(event.getPlayer(), itemStack))
			);
	}

	private Optional<ItemStack> getUniqueTravellersGear(ItemStack top, ItemStack bottom, Predicate<ItemStack> predicate) {
		List<ItemStack> travellersItemStacks = Stream.of(top, bottom)
			.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR))
			.filter(predicate)
			.toList();
		return travellersItemStacks.size() == 1 ? Optional.of(travellersItemStacks.getFirst()) : Optional.empty();
	}

	private void cancelPhantomSpawns(FabricEvents.PlayerSpawnPhantomsEvent event) {
		if (TravellersModifiersManager.isModifierActive(event.getEntity(), TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)) {
			event.setResult(FabricEvents.PlayerSpawnPhantomsEvent.Result.DENY);
		}
	}

	private void fireCraftingModifierTrigger(FabricEvents.PlayerEvent.ItemCraftedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getCrafting().has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
			ItemStack compareStack = ItemStack.EMPTY;
			for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
				if (event.getInventory().getItem(i).is(event.getCrafting().getItem())) compareStack = event.getInventory().getItem(i);
			}

			if (!compareStack.isEmpty()) {
				var oldMods = TravellersModifiersManager.findAllInsertableModifiers(player, compareStack);
				TravellersModifiersManager.findAllInsertableModifiers(player, event.getCrafting()).stream()
					.filter(modifier -> !oldMods.contains(modifier)).toList()
						.forEach(modifier -> TFAdvancements.ADD_MODIFIER.trigger(player, modifier.unwrapKey().get().identifier()));
			}
		}
	}

	public void keepAttachmentsOnDeath(FabricEvents.PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			for (AttachmentType<?> attachmentHolder : ATTACHMENTS_TO_PRESERVE_ON_DEATH) {
				copyAttachmentData(event.getOriginal(), event.getEntity(), attachmentHolder);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void copyAttachmentData(Player source, Player target, AttachmentType<T> type) {
		T value = source.getAttached(type);
		if (value != null) {
			target.setAttached(type, value);
		}
	}
}
