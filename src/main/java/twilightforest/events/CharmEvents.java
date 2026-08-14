package twilightforest.events;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;


import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.loader.api.FabricLoader;
import twilightforest.beanification.PostConstruct;
import twilightforest.TwilightForestMod;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.block.entity.SkullChestBlockEntity;
import twilightforest.compat.curios.CuriosCompat;
import twilightforest.config.TFConfig;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.network.SpawnCharmPacket;
import twilightforest.init.TFDataAttachments;
import twilightforest.tags.TFItemTags;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.List;
import twilightforest.network.PacketDistributor;

@twilightforest.beanification.Component
public class CharmEvents {

	public static final String CHARM_INV_TAG = "TFCharmInventory";
	public static final String CASKET_DAMAGE_TAG = "CasketDamage";
	public static final String CONSUMED_CHARM_TAG = "CharmStack";

	@PostConstruct
	private void setup() {
		// DIAGNOSTIC: check KEPT_ON_DEATH tag at startup AND after server (datapack) load,
		// to confirm whether the tag is actually registered at runtime.
		checkKeptOnDeathTag("startup");
		net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register(server ->
			checkKeptOnDeathTag("server_started"));
		net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTING.register(server ->
			checkKeptOnDeathTag("server_starting"));

		// 1. applyCharmOfLife - Check for charm of life before lethal damage kills the player
		// Fabric doesn't have a cancellable death event, so we use ALLOW_DAMAGE to intercept lethal damage
		net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			if (entity instanceof Player player && !player.level().isClientSide() && !(entity instanceof FakePlayer) &&
					!player.isCreative() && !player.isSpectator()) {
				// If this damage would kill the player and they have a charm of life, cancel damage and heal
				if (player.getHealth() - amount <= 0.0f && handleCharmOfLife(player)) {
					return false;
				}
			}
			return true;
		});

		// 2. applyKeepingAndCasket - NO LONGER REGISTERED via AFTER_DEATH event.
		// Fabric's AFTER_DEATH fires AFTER dropAllDeathLoot(), so items are already dropped.
		// Instead, handled via ServerPlayerMixin which injects into ServerPlayer.die()
		// BEFORE super.die() calls dropAllDeathLoot().

		// 3. COPY_FROM - Copy persistent data from old player to new player on respawn.
		// This ensures charm inventory data saved in handleDeathSave() (called from mixin)
		// gets transferred to the new player entity that is created on respawn.
		net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			// Copy the persistent data (TFData) from the old player to the new player
			CompoundTag oldData = TFDataAttachments.getOrCreate(oldPlayer, TFDataAttachments.TF_PERSISTENT_DATA, CompoundTag::new);
			if (!oldData.isEmpty()) {
				CompoundTag newData = TFDataAttachments.getOrCreate(newPlayer, TFDataAttachments.TF_PERSISTENT_DATA, CompoundTag::new);
				newData.merge(oldData.copy());
			}
		});

		// 4. returnItemsOnRespawn - Return stored items from charm of keeping when player respawns
		net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			// alive=false means player died and respawned
			// alive=true  means player used End portal (conquered end)
			if (!alive) {
				returnStoredItems(newPlayer);
			}
		});
	}

	/**
	 * Called from ServerPlayerMixin before ServerPlayer.die() calls super.die().
	 * This runs BEFORE dropAllDeathLoot() so we can save items to persistent data and clear the inventory.
	 */
	public static void handleDeathSave(ServerPlayer player) {
		if (player.level().isClientSide() || player instanceof FakePlayer) return;
		if (player.level() instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(GameRules.KEEP_INVENTORY)) {
			TwilightForestMod.LOGGER.info("[CharmEvents] handleDeathSave called for {} (keepInventory OFF, creative={}, spectator={})",
				player.getName().getString(), player.isCreative(), player.isSpectator());
			if (!player.isCreative() && !player.isSpectator()) {
				// Survival: full keeping behavior (charm of keeping + KEPT_ON_DEATH tag + keepsake casket)
				handleCharmOfKeeping(player);
				stockKeepsakeCasket(player);
			} else {
				// Creative/spectator: MC 26.2 still drops worn equipment on death in creative (no creative
				// check in the equipment drop path), so honor the KEPT_ON_DEATH tag here too to keep
				// tagged items (e.g. Phantom armor) from dropping. Charm/casket mechanics stay survival-only.
				saveKeptOnDeathItemsOnly(player);
			}
		}
	}

	/**
	 * Saves every {@code kept_on_death} tagged item (inventory, armor, offhand) to the player's
	 * persistent data and clears it from the player so it is never dropped on death. Used for
	 * creative/spectator deaths where the charm-of-keeping/casket mechanics do not apply.
	 */
	private static void saveKeptOnDeathItemsOnly(Player player) {
		Inventory keepInventory = new Inventory(player, new EntityEquipment());
		ListTag tagList = new ListTag();
		boolean saved = false;

		// Main inventory + hotbar slots
		NonNullList<ItemStack> items = player.getInventory().getNonEquipmentItems();
		for (int slot = 0; slot < items.size(); slot++) {
			ItemStack stack = items.get(slot);
			if (stack.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.getNonEquipmentItems().set(slot, stack.copy());
				items.set(slot, ItemStack.EMPTY);
				saved = true;
				TwilightForestMod.LOGGER.info("[CharmEvents] KEPT_ON_DEATH inventory item saved {} -> slot {}", stack, slot);
			}
		}

		// Armor slots (native inventory slots 36-39)
		for (EquipmentSlot slot : List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)) {
			ItemStack armor = player.getItemBySlot(slot);
			int invSlot = 36 + slot.getIndex();
			if (armor.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.setItem(invSlot, armor.copy());
				clearEquipmentSlotTriple(player, slot, invSlot);
				saved = true;
				TwilightForestMod.LOGGER.info("[CharmEvents] KEPT_ON_DEATH armor saved {} -> native slot {}", armor, invSlot);
			}
		}

		// Offhand slot (native inventory slot 40)
		ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
		if (offhand.is(TFItemTags.KEPT_ON_DEATH)) {
			keepInventory.setItem(40, offhand.copy());
			clearEquipmentSlotTriple(player, EquipmentSlot.OFFHAND, 40);
			saved = true;
			TwilightForestMod.LOGGER.info("[CharmEvents] KEPT_ON_DEATH offhand saved {} -> native slot 40", offhand);
		}

		if (saved && !keepInventory.isEmpty()) {
			saveInventoryToListTag(player.registryAccess(), keepInventory, tagList);
			getPlayerData(player).put(CHARM_INV_TAG, tagList);
			TwilightForestMod.LOGGER.info("[CharmEvents] saved {} KEPT_ON_DEATH item(s) to persistent data", tagList.size());
		}
	}

	private static void checkKeptOnDeathTag(String phase) {
		try {
			net.minecraft.resources.Identifier[] check = {
				net.minecraft.resources.Identifier.fromNamespaceAndPath("twilightforest", "tower_key"),
				net.minecraft.resources.Identifier.fromNamespaceAndPath("twilightforest", "phantom_helmet"),
				net.minecraft.resources.Identifier.fromNamespaceAndPath("twilightforest", "phantom_chestplate")
			};
			StringBuilder sb = new StringBuilder();
			for (net.minecraft.resources.Identifier loc : check) {
				var holder = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(loc);
				boolean inTag = holder != null && holder.isPresent() && holder.get().is(TFItemTags.KEPT_ON_DEATH);
				sb.append(loc).append(" inTag=").append(inTag).append("; ");
			}
			TwilightForestMod.LOGGER.info("[CharmEvents-DIAG] kept_on_death tag check [{}]: {}", phase, sb);
		} catch (Exception ex) {
			TwilightForestMod.LOGGER.error("[CharmEvents-DIAG] Failed to inspect kept_on_death tag [{}]", phase, ex);
		}
	}

	/**
	 * Called from PlayerMixin.dropEquipment HEAD as a LAST-MINUTE safety sweep.
	 * This runs IMMEDIATELY before {@link Player#dropEquipment} would call
	 * {@code destroyVanishingCursedItems()} and {@code inventory.dropAll()}.
	 * <p>
	 * If any KEPT_ON_DEATH item somehow survived the earlier clear (e.g. through an
	 * unexpected code path restoring it, or a different mod overwriting the slot),
	 * this method will:
	 * 1) read the already-saved CHARM_INV_TAG from persistent data,
	 * 2) merge the surviving item on top of it (so we never lose the saved copy),
	 * 3) write back the merged inventory, and
	 * 4) do the triple-clear on the player slot so inventory.dropAll finds nothing.
	 * <p>
	 * If the slot is already empty this is a fast no-op.
	 */
	public static void sweepKeptOnDeathItemsBeforeDrop(Player player) {
		// Keep the KEPT_ON_DEATH safety net for every game mode (MC 26.2 drops equipment in
		// creative too); only client-side / fake players are excluded.
		if (player.level().isClientSide() || player instanceof FakePlayer) return;

		// Armor slots (FEET 0, LEGS 1, CHEST 2, HEAD 3 => native inventory slots 36..39)
		for (EquipmentSlot slot : List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)) {
			ItemStack worn = player.getItemBySlot(slot);
			if (!worn.isEmpty() && worn.is(TFItemTags.KEPT_ON_DEATH)) {
				int invSlot = 36 + slot.getIndex();
				TwilightForestMod.LOGGER.info("[CharmEvents] SWEEP found residual KEPT_ON_DEATH armor {} on {}, persisting to slot {}", worn, slot, invSlot);
				persistResidualKeptItem(player, worn.copy(), invSlot);
				clearEquipmentSlotTriple(player, slot, invSlot);
			}
		}
		// Offhand slot (native inventory slot 40)
		ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
		if (!offhand.isEmpty() && offhand.is(TFItemTags.KEPT_ON_DEATH)) {
			TwilightForestMod.LOGGER.info("[CharmEvents] SWEEP found residual KEPT_ON_DEATH offhand {}, persisting to slot 40", offhand);
			persistResidualKeptItem(player, offhand.copy(), 40);
			clearEquipmentSlotTriple(player, EquipmentSlot.OFFHAND, 40);
		}
	}

	private static void persistResidualKeptItem(Player player, ItemStack keptCopy, int saveSlot) {
		Inventory tmp = new Inventory(player, new EntityEquipment());
		CompoundTag playerData = getPlayerData(player);
		ListTag existing = playerData.contains(CHARM_INV_TAG)
			? playerData.getListOrEmpty(CHARM_INV_TAG)
			: new ListTag();
		loadListTagToInventory(player.registryAccess(), existing, tmp);
		// Overwrite only if the existing saveSlot is empty (don't clobber a previously-saved stack with the
		// same KEPT_ON_DEATH item that was already saved in handleCharmOfKeeping). If the same slot
		// already has a non-empty stack in tmp we assume that was the authoritative save and we just
		// drop this residual duplicate.
		if (tmp.getItem(saveSlot).isEmpty()) {
			tmp.setItem(saveSlot, keptCopy);
		}
		ListTag out = new ListTag();
		saveInventoryToListTag(player.registryAccess(), tmp, out);
		playerData.put(CHARM_INV_TAG, out);
	}

	/**
	 * Called from EntityEquipmentMixin.dropAll HEAD — the absolute lowest-level equipment-drop exit point.
	 * {@code EntityEquipment.dropAll} iterates the equipment items map and spawns one ItemEntity per stack.
	 * This is the last possible moment to intercept KEPT_ON_DEATH items. If a KEPT_ON_DEATH item reaches
	 * here (e.g. Player.dropEquipment → inventory.dropAll → equipment.dropAll), we immediately:
	 * <ol>
	 *   <li>persist the item into the player's TF persistent data (CHARACTER_INV_TAG) using the
	 *       standard loadNoClear slot conventions so {@code returnStoredItems} can restore it on respawn</li>
	 *   <li>remove the item from the EntityEquipment map itself so the super dropAll loop finds nothing</li>
	 *   <li>also remove it from the shared Player Inventory slots to avoid duplication</li>
	 * </ol>
	 *
	 * @param dropper     the LivingEntity dropping equipment; we only act when this is a real Player in survival
	 * @param equipment   the EntityEquipment instance that is about to dropAll (= the mixin target 'this')
	 */
	public static void interceptKeptOnDeathAtEquipmentDropAll(LivingEntity dropper, EntityEquipment equipment) {
		if (!(dropper instanceof Player player)) return;
		// Keep the KEPT_ON_DEATH safety net for every game mode (MC 26.2 drops equipment in
		// creative too); only client-side / fake players are excluded.
		if (player.level().isClientSide() || player instanceof FakePlayer) return;
		if (player.level() instanceof ServerLevel sl && sl.getGameRules().get(GameRules.KEEP_INVENTORY)) return;

		// Walk armor and offhand slots (the slots KEPT_ON_DEATH items typically occupy).
		// We persist using the SAME native inventory slot id that loadNoClear understands,
		// because keepInventory is a real Inventory: only 0-35 (items) and 36-42 (equipment)
		// are valid. 36=FEET,37=LEGS,38=CHEST,39=HEAD,40=OFFHAND.
		record SlotMapping(EquipmentSlot slot, int invSlot) {}
		List<SlotMapping> mappings = List.of(
			new SlotMapping(EquipmentSlot.FEET,  36),
			new SlotMapping(EquipmentSlot.LEGS,  37),
			new SlotMapping(EquipmentSlot.CHEST, 38),
			new SlotMapping(EquipmentSlot.HEAD,  39),
			new SlotMapping(EquipmentSlot.OFFHAND, 40)
		);
		for (SlotMapping m : mappings) {
			ItemStack stack = equipment.get(m.slot());
			if (!stack.isEmpty() && stack.is(TFItemTags.KEPT_ON_DEATH)) {
				TwilightForestMod.LOGGER.info("[CharmEvents] EquipmentDropAll intercept: kept {} from {}, persisting to native slot {}", stack, m.slot(), m.invSlot());
				persistResidualKeptItem(player, stack.copy(), m.invSlot());
				// Remove from the equipment map DIRECTLY — this is what dropAll iterates.
				equipment.set(m.slot(), ItemStack.EMPTY);
				// Also clear from Player Inventory slots so Inventory.dropAll (for items list) / drop
				// observers don't see it on the off-chance they also iterate the inventory.
				player.getInventory().setItem(m.invSlot(), ItemStack.EMPTY);
				player.getInventory().removeItemNoUpdate(m.invSlot());
			}
		}
	}

	// Check for charm of life first to stop a player from dying
	private void applyCharmOfLife(FabricEvents.LivingDeathEvent event) {
		LivingEntity living = event.getEntity();

		//ensure our player is real and in survival before attempting anything
		if (event.isCanceled() || living.level().isClientSide() || !(living instanceof Player player) || living instanceof FakePlayer ||
				player.isCreative() || player.isSpectator()) return;

		if (handleCharmOfLife(player)) event.setCanceled(true); // Executes if the player had charms
	}

	// Then check if the player should keep any items through death
	private void applyKeepingAndCasket(FabricEvents.LivingDeathEvent event) {
		LivingEntity living = event.getEntity();

		//ensure our player is real and in survival before attempting anything
		if (event.isCanceled() || living.level().isClientSide() || !(living instanceof Player player) || living instanceof FakePlayer ||
				player.isCreative() || player.isSpectator()) return;

		if (living.level() instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(GameRules.KEEP_INVENTORY)) {
			// Did the player recover? No? Let's give them their stuff based on the keeping charms
			handleCharmOfKeeping(player);

			// Then let's store the rest of their stuff in the casket
			stockKeepsakeCasket(player);
		}
	}

	private void returnItemsOnRespawn(FabricEvents.PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
		if (!event.isEndConquered()) {
			returnStoredItems(serverPlayer);
		}
	}

	private static boolean handleCharmOfLife(Player player) {
		boolean charm2 = TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_2, getPlayerData(player), false) || hasCharmCurio(TFItems.CHARM_OF_LIFE_2, player);
		boolean charm1 = !charm2 && (TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_1, getPlayerData(player), false) || hasCharmCurio(TFItems.CHARM_OF_LIFE_1, player));

		if (charm2 || charm1) {
			if (charm1) {
				player.setHealth(8);
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
			}

			if (charm2) {
				player.setHealth(player.getMaxHealth());

				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3));
				player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 600, 0));
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0));
			}

			if (player instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, new SpawnCharmPacket(new ItemStack(charm1 ? TFItems.CHARM_OF_LIFE_1 : TFItems.CHARM_OF_LIFE_2), ResourceKey.create(Registries.SOUND_EVENT, TFSounds.CHARM_LIFE.location())));
				serverPlayer.awardStat(TFStats.LIFE_CHARMS_ACTIVATED);
			}

			return true;
		}

		return false;
	}

	private static void handleCharmOfKeeping(Player player) {
		//create a fake inventory to organize our kept inventory in
		Inventory keepInventory = new Inventory(player, new EntityEquipment());
		ListTag tagList = new ListTag();

		if (!applyCharm(TFItems.CHARM_OF_KEEPING_3, keepInventory, player, player.getInventory().getNonEquipmentItems())) {
			if (!applyCharm(TFItems.CHARM_OF_KEEPING_2, keepInventory, player, player.getInventory().getNonEquipmentItems().subList(0, 9))) {
				int i = player.getInventory().getSelectedSlot();
				if (Inventory.isHotbarSlot(i)) {
					applyCharm(TFItems.CHARM_OF_KEEPING_1, keepInventory, player, NonNullList.of(player.getInventory().getNonEquipmentItems().get(i)));
				}
			}
		}

		//keep all items in the kept_on_death tag. This allows modpacks to support other items to keep on death
		for (int slot = 0; slot < player.getInventory().getNonEquipmentItems().size(); slot++) {
			ItemStack stack = player.getInventory().getNonEquipmentItems().get(slot);
			if (stack.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.getNonEquipmentItems().set(slot, stack.copy());
				player.getInventory().getNonEquipmentItems().set(slot, ItemStack.EMPTY);
			}
		}

		// Armor slots: save to keepInventory using Inventory's native equipment slots 36-39
	// (36=FEET, 37=LEGS, 38=CHEST, 39=HEAD per Inventory.EQUIPMENT_SLOT_MAPPING).
	// NOTE: keepInventory is a real Inventory whose setItem() only understands 0-35 (items)
	// and 36-42 (equipment via EQUIPMENT_SLOT_MAPPING). Using 100-103 here would silently drop items!
		for (EquipmentSlot equipmentSlot : List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)) {
			ItemStack armor = player.getItemBySlot(equipmentSlot);
			int invSlot = 36 + equipmentSlot.getIndex(); // 36=FEET,37=LEGS,38=CHEST,39=HEAD
			if (armor.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.setItem(invSlot, armor.copy());
				clearEquipmentSlotTriple(player, equipmentSlot, invSlot);
				TwilightForestMod.LOGGER.info("[CharmEvents] KEPT_ON_DEATH armor saved {} -> native slot {}", armor, invSlot);
			} else {
				TwilightForestMod.LOGGER.info("[CharmEvents] armor {} on {} NOT in kept_on_death tag (empty={})", armor, equipmentSlot, armor.isEmpty());
			}
		}

		// Offhand slot: save to keepInventory using native slot 40 (Inventory.SLOT_OFFHAND)
		ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
		if (offhand.is(TFItemTags.KEPT_ON_DEATH)) {
			keepInventory.setItem(40, offhand.copy());
			clearEquipmentSlotTriple(player, EquipmentSlot.OFFHAND, 40);
			TwilightForestMod.LOGGER.info("[CharmEvents] KEPT_ON_DEATH offhand saved {} -> native slot 40", offhand);
		} else {
			TwilightForestMod.LOGGER.info("[CharmEvents] offhand {} NOT in kept_on_death tag (empty={})", offhand, offhand.isEmpty());
		}

		//take our fake inventory and save it to the persistent player data.
		//by saving it there we can guarantee we will always get all of our items back, even if the player logs out and back in.
		if (!keepInventory.isEmpty()) {
			saveInventoryToListTag(player.registryAccess(), keepInventory, tagList);
			getPlayerData(player).put(CHARM_INV_TAG, tagList);
		}
	}

	private static boolean applyCharm(Object charm, Inventory keptInventory, Player player, List<ItemStack> inventorySlots) {
		List<ItemStack> mergedCheck = new ArrayList<>(inventorySlots);
		//merge armor and offhand into check slots since theyll always be kept by a charm
		for (EquipmentSlot slot : List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)) {
			mergedCheck.add(player.getItemBySlot(slot));
		}
		mergedCheck.add(player.getItemBySlot(EquipmentSlot.OFFHAND));
		//first, check all affected slots to make sure they arent empty.
		//filter out the charm so it doesnt count towards keeping items if its the only thing we are holding
		Item charmItem = charm instanceof Item item ? item : null;
		if (mergedCheck.stream().filter(stack -> charmItem != null && !stack.is(charmItem)).allMatch(ItemStack::isEmpty)) return false;

		//do we even have a charm? No? Then stop operation
		if (charmItem != null && !TFItemStackUtils.consumeInventoryItem(player, charmItem, getPlayerData(player), true) && !hasCharmCurio(charmItem, player)) return false;

		boolean keptACasket = keepWholeListAndCheckCasket(keptInventory.getNonEquipmentItems(), inventorySlots, charm == TFItems.CHARM_OF_KEEPING_3);
		keptACasket = keepArmorAndCheckCasket(keptInventory, player, keptACasket);
		keepOffhandAndCheckCasket(keptInventory, player, keptACasket);

		return true;
	}

	private static boolean keepArmorAndCheckCasket(Inventory keptInventory, Player player, boolean skipCasketCheck) {
		List<ItemStack> armorItems = List.of(
			player.getItemBySlot(EquipmentSlot.FEET),
			player.getItemBySlot(EquipmentSlot.LEGS),
			player.getItemBySlot(EquipmentSlot.CHEST),
			player.getItemBySlot(EquipmentSlot.HEAD)
		);
		boolean keptCasket = false;
		for (int i = 0; i < armorItems.size(); i++) {
			var item = armorItems.get(i).copy();
			EquipmentSlot slot = switch (i) {
				case 0 -> EquipmentSlot.FEET;
				case 1 -> EquipmentSlot.LEGS;
				case 2 -> EquipmentSlot.CHEST;
				case 3 -> EquipmentSlot.HEAD;
				default -> null;
			};
			if (slot == null) continue;
			int invSlot = 36 + slot.getIndex(); // 36=FEET,37=LEGS,38=CHEST,39=HEAD (native Inventory equipment slots)
			if (skipCasketCheck || (!item.is(TFItems.KEEPSAKE_CASKET) || keptCasket)) {
				keptInventory.setItem(invSlot, item);
				clearEquipmentSlotTriple(player, slot, invSlot);
			} else {
				keptCasket = true;
				if (item.getCount() > 1) {
					item.shrink(1);
					keptInventory.setItem(invSlot, item);
					player.setItemSlot(slot, item.copyWithCount(1));
					player.getInventory().setItem(invSlot, item.copyWithCount(1));
				}
			}
		}
		return keptCasket || skipCasketCheck;
	}

	private static boolean keepOffhandAndCheckCasket(Inventory keptInventory, Player player, boolean skipCasketCheck) {
		ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND).copy();
		if (!skipCasketCheck && offhand.is(TFItems.KEEPSAKE_CASKET)) {
			if (offhand.getCount() > 1) {
				offhand.shrink(1);
				keptInventory.setItem(40, offhand); // 40=OFFHAND (native Inventory equipment slot)
				player.setItemSlot(EquipmentSlot.OFFHAND, offhand.copyWithCount(1));
				player.getInventory().setItem(40, offhand.copyWithCount(1));
			}
		} else {
			keptInventory.setItem(40, offhand);
			clearEquipmentSlotTriple(player, EquipmentSlot.OFFHAND, 40);
		}
		return true;
	}

	private static void clearEquipmentSlotTriple(Player player, EquipmentSlot slot, int invSlot) {
		player.setItemSlot(slot, ItemStack.EMPTY);
		player.getInventory().setItem(invSlot, ItemStack.EMPTY);
		player.getInventory().removeItemNoUpdate(invSlot);
	}

	private static void stockKeepsakeCasket(Player player) {
		//make sure we are still actually holding onto items before trying to place a casket
		if (player.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && !stack.is(TFItems.KEEPSAKE_CASKET))) {
			boolean casketConsumed = TFItemStackUtils.consumeInventoryItem(player, TFBlocks.KEEPSAKE_CASKET, getPlayerData(player), false);

			if (!casketConsumed)
				return;

			Level level = player.level();
			BlockPos.MutableBlockPos pos = player.blockPosition().mutable();

			if (pos.getY() < level.dimensionType().minY() + 2) {
				pos.setY(level.dimensionType().minY() + 2);
			} else {
				int logicalHeight = player.level().dimensionType().logicalHeight();

				if (pos.getY() > logicalHeight) {
					pos.setY(logicalHeight - 1);
				}
			}

			pos.move(0, -1, 0);

			do {
				pos.move(0, 1, 0);
			} while (!level.getBlockState(pos).canBeReplaced());

			BlockPos immutablePos = pos.immutable();
			FluidState fluidState = level.getFluidState(immutablePos);

			int damage = getPlayerData(player).contains(CASKET_DAMAGE_TAG) ? getPlayerData(player).getInt(CASKET_DAMAGE_TAG).orElse(0) : 0;
			BlockState setState = TFBlocks.KEEPSAKE_CASKET.defaultBlockState()
				.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType()))
				.setValue(KeepsakeCasketBlock.BREAKAGE, damage)
				.setValue(KeepsakeCasketBlock.FACING, Direction.from2DDataValue(level.getRandom().nextInt(3)));

			if (player.getRandom().nextFloat() <= 0.15F) {
				if (damage >= 2) {
					setState = TFBlocks.SKULL_CHEST.withPropertiesOf(setState);
					TwilightForestMod.LOGGER.debug("{}'s Casket damage value was too high, placing Skull Chest instead", player.getName().getString());
				} else {
					damage = damage + 1;
					setState = TFBlocks.KEEPSAKE_CASKET.withPropertiesOf(setState).setValue(KeepsakeCasketBlock.BREAKAGE, damage);
					TwilightForestMod.LOGGER.debug("{}'s Casket was randomly damaged, applying new damage", player.getName().getString());
				}
			}

			if (!level.setBlockAndUpdate(immutablePos, setState)) {
				TwilightForestMod.LOGGER.error("Could not place Keepsake Casket at {}", pos);
				return;
			}

			if (!(level.getBlockEntity(immutablePos) instanceof SkullChestBlockEntity casket)) {
				TwilightForestMod.LOGGER.error("Failed to set Keepsake Casket data at {}", pos);
				return;
			}

			if (TFConfig.casketUUIDLocking) {
				//make it so only the player who died can open the chest if our config allows us
				casket.owner = ResolvableProfile.createResolved(player.getGameProfile());
			} else {
				casket.owner = null;
			}

			//some names are way too long for the casket so we'll cut them down
			String modifiedName = player.getName().getString().substring(0, Math.min(12, player.getName().getString().length()));
			casket.name = (Component.literal(modifiedName + "'s " + (level.getRandom().nextInt(1000) == 0 ? "Costco Casket" : casket.getDisplayName().getString())));

			int casketCapacity = casket.getContainerSize();
			List<ItemStack> list = new ArrayList<>(casketCapacity);
			NonNullList<ItemStack> filler = NonNullList.withSize(4, ItemStack.EMPTY);

			// lets add our inventory exactly how it was on us
			list.addAll(TFItemStackUtils.sortArmorForCasket(player));
			for (EquipmentSlot slot : List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)) {
				clearEquipmentSlotTriple(player, slot, 36 + slot.getIndex());
			}
			list.addAll(filler);
			list.add(player.getItemBySlot(EquipmentSlot.OFFHAND));
			clearEquipmentSlotTriple(player, EquipmentSlot.OFFHAND, 40);
			list.addAll(TFItemStackUtils.sortInvForCasket(player));
			player.getInventory().getNonEquipmentItems().clear();

			casket.setItems(NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[casketCapacity])));
			getPlayerData(player).remove(CASKET_DAMAGE_TAG);
		} else {
			//inventory is empty minus the casket: put the casket into the kept inventory
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				if (player.getInventory().getItem(i).is(TFItems.KEEPSAKE_CASKET)) {
					Inventory tmp = new Inventory(player, new EntityEquipment());
					ListTag charmInvTag = getPlayerData(player).getListOrEmpty(CHARM_INV_TAG);
					loadListTagToInventory(player.registryAccess(), charmInvTag, tmp);
					tmp.add(player.getInventory().getItem(i).copy());
					player.getInventory().setItem(i, ItemStack.EMPTY);
					ListTag savedTag = new ListTag();
					saveInventoryToListTag(player.registryAccess(), tmp, savedTag);
					getPlayerData(player).put(CHARM_INV_TAG, savedTag);
				}
			}
		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	private static void returnStoredItems(Player player) {

		TwilightForestMod.LOGGER.debug("Player {} ({}) respawned and received items held in storage", player.getName().getString(), player.getUUID());

		//check if our tag is in the persistent player data. If so, copy that inventory over to our own. Cloud storage at its finest!
		CompoundTag playerData = getPlayerData(player);
		if (!player.level().isClientSide() && playerData.contains(CHARM_INV_TAG)) {
			ListTag tagList = playerData.getList(CHARM_INV_TAG).orElse(new ListTag());
			TFItemStackUtils.loadNoClear(player.registryAccess(), tagList, player.getInventory());
			getPlayerData(player).getList(CHARM_INV_TAG).orElse(new ListTag()).clear();
			getPlayerData(player).remove(CHARM_INV_TAG);
		}

		// spawn effect thingers
		if (getPlayerData(player).contains(CONSUMED_CHARM_TAG)) {
			net.minecraft.nbt.Tag charmData = getPlayerData(player).get(CONSUMED_CHARM_TAG);
			CompoundTag compound = charmData instanceof CompoundTag ct ? ct : new CompoundTag();
			ItemStack stack = ItemStack.OPTIONAL_CODEC.decode(player.registryAccess().createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE), compound).result().map(Pair::getFirst).orElse(ItemStack.EMPTY);

			if (player instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, new SpawnCharmPacket(stack, ResourceKey.create(Registries.SOUND_EVENT, TFSounds.CHARM_KEEP.location())));
				serverPlayer.awardStat(TFStats.KEEPING_CHARMS_ACTIVATED);
			}
			getPlayerData(player).remove(CONSUMED_CHARM_TAG);
		}
	}

	public static final String PERSISTED_NBT_TAG = "PlayerPersisted";

	public static CompoundTag getPlayerData(Player player) {
		CompoundTag persistentData = TFDataAttachments.getOrCreate(player, TFDataAttachments.TF_PERSISTENT_DATA, CompoundTag::new);
		if (!persistentData.contains(PERSISTED_NBT_TAG)) {
			persistentData.put(PERSISTED_NBT_TAG, new CompoundTag());
		}
		return persistentData.getCompound(PERSISTED_NBT_TAG).orElse(new CompoundTag());
	}

	//transfers a list of items to another
	private static boolean keepWholeListAndCheckCasket(NonNullList<ItemStack> transferTo, List<ItemStack> transferFrom, boolean skipCasketCheck) {
		boolean keptCasket = false;
		for (int i = 0; i < transferFrom.size(); i++) {
			var item = transferFrom.get(i).copy();
			if (skipCasketCheck || (!item.is(TFItems.KEEPSAKE_CASKET) || keptCasket)) {
				transferTo.set(i, item);
				transferFrom.set(i, ItemStack.EMPTY);
			} else {
				keptCasket = true;
				if (item.getCount() > 1) {
					item.shrink(1);
					transferTo.set(i, item);
					transferFrom.set(i, item.copyWithCount(1));
				}
			}
		}
		return keptCasket || skipCasketCheck;
	}

	// Helper to save inventory items to a ListTag (replaces old Inventory.save(ListTag))
	private static void saveInventoryToListTag(RegistryAccess registryAccess, Inventory inventory, ListTag tagList) {
		NonNullList<ItemStack> items = inventory.getNonEquipmentItems();
		for (int i = 0; i < items.size(); i++) {
			ItemStack stack = items.get(i);
			if (!stack.isEmpty()) {
				CompoundTag tag = new CompoundTag();
				tag.putByte("Slot", (byte) i);
				tag.merge((CompoundTag) ItemStack.CODEC.encodeStart(registryAccess.createSerializationContext(NbtOps.INSTANCE), stack).getOrThrow());
				tagList.add(tag);
			}
		}
		// Save equipment slots using Inventory's NATIVE slot ids so loadNoClear can restore them:
		// 36=FEET, 37=LEGS, 38=CHEST, 39=HEAD, 40=OFFHAND
		for (int saveSlot = 36; saveSlot <= 40; saveSlot++) {
			ItemStack stack = inventory.getItem(saveSlot);
			if (!stack.isEmpty()) {
				CompoundTag tag = new CompoundTag();
				tag.putByte("Slot", (byte) saveSlot);
				tag.merge((CompoundTag) ItemStack.CODEC.encodeStart(registryAccess.createSerializationContext(NbtOps.INSTANCE), stack).getOrThrow());
				tagList.add(tag);
			}
		}
	}

	// Helper to load items from a ListTag into an Inventory
	private static void loadListTagToInventory(RegistryAccess registryAccess, ListTag tagList, Inventory inventory) {
		for (int i = 0; i < tagList.size(); i++) {
			CompoundTag tag = tagList.getCompound(i).orElse(new CompoundTag());
			int slot = tag.getByte("Slot").orElse((byte)0) & 0xFF;
			ItemStack stack = ItemStack.OPTIONAL_CODEC.decode(registryAccess.createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE), tag).result().map(Pair::getFirst).orElse(ItemStack.EMPTY);
			if (stack.isEmpty()) continue;
			if (slot >= 0 && slot < inventory.getNonEquipmentItems().size()) {
				if (inventory.getNonEquipmentItems().get(slot).isEmpty()) {
					inventory.getNonEquipmentItems().set(slot, stack);
				} else {
					inventory.add(stack);
				}
			} else if (slot >= 36 && slot <= 40) {
				// Native equipment slots: 36=FEET,37=LEGS,38=CHEST,39=HEAD,40=OFFHAND.
				// Inventory.setItem(36..40) maps to the shared EntityEquipment.
				if (inventory.getItem(slot).isEmpty()) {
					inventory.setItem(slot, stack);
				} else {
					inventory.add(stack);
				}
			} else if (slot >= 100 && slot <= 103) {
				// Legacy armor slots (100-103): map to 36-39 for backward compatibility
				int nativeSlot = 36 + (slot - 100);
				if (inventory.getItem(nativeSlot).isEmpty()) {
					inventory.setItem(nativeSlot, stack);
				} else {
					inventory.add(stack);
				}
			} else if (slot == 150) {
				// Legacy offhand slot 150: map to 40
				if (inventory.getItem(40).isEmpty()) {
					inventory.setItem(40, stack);
				} else {
					inventory.add(stack);
				}
			} else {
				inventory.add(stack);
			}
		}
	}

	private static boolean hasCharmCurio(Item item, Player player) {
		if (FabricLoader.getInstance().isModLoaded("curios")) {
			return CuriosCompat.findAndConsumeCurio(item, player);
		}

		return false;
	}
}
