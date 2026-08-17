package twilightforest.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.beanification.Component;
import twilightforest.beanification.PostConstruct;
import twilightforest.block.GiantBlock;
import twilightforest.components.entity.GiantPickaxeMiningAttachment;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.mixin.AbstractArrowMixin;
import twilightforest.item.*;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;
import twilightforest.tags.TFBlockTags;
import twilightforest.tags.TFEntityTypeTags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;


@Component
public class ToolEvents {

	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;

	// Static flag to prevent infinite recursion during giant pickaxe mining
	// This is necessary because Fabric's PlayerBlockBreakEvents.BEFORE fires for every
	// destroyBlock call, including the ones triggered from within this handler
	private static boolean isBreakingWithGiantPick = false;

	@PostConstruct
	private void setup() {
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
			OreMagnetItem.markOreCacheDirty();
			OreMagnetItem.refreshOreCacheFromTags();
		});

		// Giant pickaxe mining: BEFORE handler handles the entire 4x4x4 break
		// Matches original NeoForge handleGiantPickaxeMining flow:
		// - check canHarvestWithGiantPick + shouldBreakGiantBlock
		// - setCanceled(true) to prevent normal break
		// - manually break all 4x4x4 blocks
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
			// Skip if we're already inside a giant pickaxe break loop
			if (isBreakingWithGiantPick) {
				return true; // Allow normal break to proceed
			}
			
			FabricEvents.BreakBlockEvent event = new FabricEvents.BreakBlockEvent(world, pos, state, player);
			handleGiantPickaxeMining(event);
			return !event.isCanceled();
		});

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			FabricEvents.BreakBlockEvent event = new FabricEvents.BreakBlockEvent(world, pos, state, player);
			damageNonMazebreakerToolsMore(event);
		});

		// === Handlers already implemented via Mixin → EntityEvents.modifyIncomingDamage() ===
		// - fieryToolSetFire       → LivingEntityMixin → EntityEvents.modifyIncomingDamage() lines 611-617
		// - doKnightmetalToolLogic → LivingEntityMixin → EntityEvents.modifyIncomingDamage() lines 619-637
		// - addExtraAxeChargingDamage → LivingEntityMixin → EntityEvents.modifyIncomingDamage() lines 639-647
		// - preventFatigueWithPocketWatch → LivingEntityMixin → ToolEvents.shouldBlockEffect() lines 84-89
		//
		// The instance methods below (fieryToolSetFire, doKnightmetalToolLogic, addExtraAxeChargingDamage,
		// preventFatigueWithPocketWatch) are kept as dead code for reference only.
		// Their actual logic is in EntityEvents.modifyIncomingDamage() and ToolEvents.shouldBlockEffect().
	}

	private void onEnderBowHit(FabricEvents.ProjectileImpactEvent evt) {
		Projectile arrow = (Projectile) evt.getProjectile();
		if (arrow.getOwner() instanceof Player player
			&& evt.getRayTraceResult() instanceof EntityHitResult result
			&& result.getEntity() instanceof LivingEntity living
			&& arrow.getOwner() != result.getEntity() && !result.getEntity().is(TFEntityTypeTags.BOSSES)) { // TODO: Port - verify BOSSES tag exists in TFEntityTypeTags

			if (TFDataAttachments.getOrCreate(player, TFDataAttachments.TF_PERSISTENT_DATA, CompoundTag::new).getCompound("PlayerPersisted").orElse(new net.minecraft.nbt.CompoundTag()).contains(EnderBowItem.KEY)) {
				double sourceX = player.getX(), sourceY = player.getY(), sourceZ = player.getZ();
				float sourceYaw = player.getYRot(), sourcePitch = player.getXRot();
				@Nullable Entity playerVehicle = player.getVehicle();

				player.setYRot(living.getYRot());
				player.teleportTo(living.getX(), living.getY(), living.getZ());
				player.setInvulnerableTime(40);
				player.level().broadcastEntityEvent(player, (byte) 46);
				if (living.isPassenger() && living.getVehicle() != null) {
					player.startRiding(living.getVehicle());
					living.stopRiding();
				}
				player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

				living.setYRot(sourceYaw);
				living.setXRot(sourcePitch);
				living.teleportTo(sourceX, sourceY, sourceZ);
				living.level().broadcastEntityEvent(player, (byte) 46);
				if (playerVehicle != null) {
					living.startRiding(playerVehicle);
					player.stopRiding();
				}
				living.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Triggered by {@link AbstractArrowMixin} when an arrow marked with
	 * {@link TFDataAttachments#ENDER_BOW_ARROW} hits a living entity.
	 * Performs the ender bow position swap between shooter and target.
	 */
	public static void performEnderBowSwap(AbstractArrow arrow, EntityHitResult result) {
		if (!arrow.hasAttached(TFDataAttachments.ENDER_BOW_ARROW)) return;
		if (!(arrow.getOwner() instanceof Player player)) return;
		if (!(result.getEntity() instanceof LivingEntity living)) return;
		if (player == living || living.is(TFEntityTypeTags.BOSSES)) return;

		double sourceX = player.getX(), sourceY = player.getY(), sourceZ = player.getZ();
		float sourceYaw = player.getYRot(), sourcePitch = player.getXRot();
		@Nullable Entity playerVehicle = player.getVehicle();

		player.setYRot(living.getYRot());
		player.teleportTo(living.getX(), living.getY(), living.getZ());
		player.setInvulnerableTime(40);
		player.level().broadcastEntityEvent(player, (byte) 46);
		if (living.isPassenger() && living.getVehicle() != null) {
			player.startRiding(living.getVehicle());
			living.stopRiding();
		}
		player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

		living.setYRot(sourceYaw);
		living.setXRot(sourcePitch);
		living.teleportTo(sourceX, sourceY, sourceZ);
		living.level().broadcastEntityEvent(living, (byte) 46);
		if (playerVehicle != null) {
			living.startRiding(playerVehicle);
			player.stopRiding();
		}
		living.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

		arrow.discard();
	}

	private void fieryToolSetFire(FabricEvents.LivingIncomingDamageEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity living && (living.getMainHandItem().is(TFItems.FIERY_SWORD) || living.getMainHandItem().is(TFItems.FIERY_PICKAXE)) && !event.getEntity().fireImmune()) {
			event.getEntity().igniteForSeconds(1);
		}
	}

	private void doKnightmetalToolLogic(FabricEvents.LivingIncomingDamageEvent event) {
		if (!event.isCanceled()) {
			LivingEntity target = event.getEntity();

			if (!target.level().isClientSide() && event.getSource().getDirectEntity() instanceof LivingEntity living) {
				ItemStack weapon = living.getMainHandItem();

				if (!weapon.isEmpty()) {
					if (target.getArmorValue() > 0 && (weapon.is(TFItems.KNIGHTMETAL_PICKAXE) || weapon.is(TFItems.KNIGHTMETAL_SWORD))) {
						if (target.getArmorCoverPercentage() > 0) {
							int moreBonus = (int) (KNIGHTMETAL_BONUS_DAMAGE * target.getArmorCoverPercentage());
							event.setAmount(event.getAmount() + moreBonus);
						} else {
							event.setAmount(event.getAmount() + KNIGHTMETAL_BONUS_DAMAGE);
						}
						// enchantment attack sparkles
						((ServerLevel) target.level()).getChunkSource().sendToTrackingPlayersAndSelf(target, new ClientboundAnimatePacket(target, 5));
					} else if (target.getArmorValue() == 0 && weapon.is(TFItems.KNIGHTMETAL_AXE)) {
						event.setAmount(event.getAmount() + KNIGHTMETAL_BONUS_DAMAGE);
						// enchantment attack sparkles
						((ServerLevel) target.level()).getChunkSource().sendToTrackingPlayersAndSelf(target, new ClientboundAnimatePacket(target, 5));
					}
				}
			}
		}
	}

	private void addExtraAxeChargingDamage(FabricEvents.LivingIncomingDamageEvent event) {
		if (!event.isCanceled()) {
			LivingEntity target = event.getEntity();
			if (!target.level().isClientSide() && event.getSource().getDirectEntity() instanceof LivingEntity living && living.isSprinting()) {
				ItemStack weapon = living.getMainHandItem();
				if (!weapon.isEmpty() && weapon.getItem() instanceof MinotaurAxeItem) {
					event.setAmount(event.getAmount() + MINOTAUR_AXE_BONUS_DAMAGE);
					// enchantment attack sparkles
					((ServerLevel) target.level()).getChunkSource().sendToTrackingPlayersAndSelf(target, new ClientboundAnimatePacket(target, 5));
				}
			}
		}
	}

	private void damageNonMazebreakerToolsMore(FabricEvents.BreakBlockEvent event) {
		ItemStack stack = event.getPlayer().getMainHandItem();
		if (event.getState().is(TFBlockTags.MAZEBREAKER_ACCELERATED)) {
			if (stack.isDamageableItem() && !(stack.getItem() instanceof MazebreakerPickItem)) {
				stack.hurtAndBreak(16, event.getPlayer(), EquipmentSlot.MAINHAND);
			}
		}
	}

	private void preventFatigueWithPocketWatch(FabricEvents.MobEffectEvent.Applicable event) {
		if (event.getResult() && event.getEffectInstance().is(MobEffects.MINING_FATIGUE) && event.getEntity().isHolding(TFItems.POCKET_WATCH)) {
			event.setResult(false);
		}
	}

	/**
	 * Called from PlayerBlockBreakEvents.BEFORE (via FabricEvents.BreakBlockEvent wrapper).
	 * Matches the original NeoForge handleGiantPickaxeMining flow exactly.
	 * Uses static flag isBreakingWithGiantPick to prevent recursion during the 4x4x4 block break loop.
	 */
		private void handleGiantPickaxeMining(FabricEvents.BreakBlockEvent event) {
		BlockPos pos = event.getPos();
		BlockState state = event.getState();

		if (event.getPlayer() instanceof ServerPlayer player && canHarvestWithGiantPick(player, state, pos)) {
			var attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.GIANT_PICKAXE_MINING, twilightforest.components.entity.GiantPickaxeMiningAttachment::new);

			// Initialize mining time if not already set
			if (attachment.getMining() != player.level().getGameTime()) {
				attachment.setMining(player.level().getGameTime());
				attachment.setBreaking(false);
				attachment.setGiantBlockConversion(0);
			}

			if (shouldBreakGiantBlock(player, attachment)) {
				isBreakingWithGiantPick = true;
				
				try {
					// First, determine if this block can form a giant block (check 4x4x4 area and conversion map)
					boolean canFormGiantBlock = false;
					if (GiantToolGroupingModifier.CONVERSIONS.containsKey(state.getBlock())) {
						canFormGiantBlock = true;
						for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
							if (!player.level().getBlockState(offsetPos).is(state.getBlock())) {
								canFormGiantBlock = false;
								break;
							}
						}
					}
					attachment.setGiantBlockConversion(canFormGiantBlock ? 64 : 0);
					
					event.setCanceled(true);
					player.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
					player.gameMode.destroyBlock(pos);

					for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
						if (!offsetPos.equals(pos) && player.level().getBlockState(offsetPos).is(state.getBlock())) {
							BlockPos newPos = offsetPos.immutable();
							player.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(player.level().getBlockState(newPos)));
							player.gameMode.destroyBlock(newPos);
						}
					}
				} finally {
					isBreakingWithGiantPick = false;
				}
			}
		}
	}

	private static boolean canHarvestWithGiantPick(Player player, BlockState state, BlockPos pos) {
		return player.getMainHandItem().getItem() instanceof GiantPickItem && player.hasCorrectToolForDrops(state);
	}

	private static boolean shouldBreakGiantBlock(Player player, GiantPickaxeMiningAttachment attachment) {
		return attachment.getMining() == player.level().getGameTime() && !attachment.getBreaking();
	}

	private void refreshOreMagnetCache(RegistryAccess registryAccess) {
		OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.clear();
		OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.clear();

		//collect all tags
		for (TagKey<Block> tag : BuiltInRegistries.BLOCK.getTags().map(HolderSet.Named::key).filter(location -> location.location().getNamespace().equals("c")).toList()) {
			//check if the tag is a valid ore tag
			if (tag.location().getPath().contains("ores_in_ground/")) {
				//grab the part after the slash for use later
				String oreground = tag.location().getPath().substring(15);
				//check if a tag for ore grounds matches up with our ores in ground tag
				if (BuiltInRegistries.BLOCK.getTags().map(HolderSet.Named::key).filter(location -> location.location().getNamespace().equals("c")).anyMatch(blockTagKey -> blockTagKey.location().getPath().equals("ore_bearing_ground/" + oreground))) {
					//add each ground type to each ore
					BuiltInRegistries.BLOCK.get(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ore_bearing_ground/" + oreground))).get().forEach(ground ->
						BuiltInRegistries.BLOCK.get(tag).get().forEach(ore -> {
							//exclude ignored ores
							if (!ore.value().defaultBlockState().is(TFBlockTags.ORE_MAGNET_IGNORE)) {
								OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.put(ore.value(), ground.value());
							}
							if (!ore.value().defaultBlockState().is(TFBlockTags.MINING_CORE_EXCLUDED)) {
								OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.put(ore.value(), ground.value());
							}
						}));
				}
			}
		}

		//Gonna need to special case this one as it isn't covered by tags.
		//Ancient debris isn't exactly an ore, so it makes sense that the tag doesn't include it
		if (!Blocks.ANCIENT_DEBRIS.defaultBlockState().is(TFBlockTags.ORE_MAGNET_IGNORE) && !OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.containsKey(Blocks.ANCIENT_DEBRIS)) {
			OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.put(Blocks.ANCIENT_DEBRIS, Blocks.NETHERRACK);
		}

		if (!Blocks.ANCIENT_DEBRIS.defaultBlockState().is(TFBlockTags.MINING_CORE_EXCLUDED) && !OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.containsKey(Blocks.ANCIENT_DEBRIS)) {
			OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.put(Blocks.ANCIENT_DEBRIS, Blocks.NETHERRACK);
		}
	}

	/**
	 * Called from LivingEntityMixin (Inject at HEAD of addEffect).
	 * Returns true if the effect should be blocked (pocket watch prevents mining fatigue).
	 */
	public static boolean shouldBlockEffect(LivingEntity entity, net.minecraft.world.effect.MobEffectInstance effectInstance) {
		return effectInstance.is(MobEffects.MINING_FATIGUE) && entity.isHolding(TFItems.POCKET_WATCH);
	}
}
