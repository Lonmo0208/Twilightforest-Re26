package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.beanification.Component;
import twilightforest.beanification.PostConstruct;
import twilightforest.block.GiantBlock;
import twilightforest.components.entity.GiantPickaxeMiningAttachment;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.util.TFEntityExtensions;
import twilightforest.item.*;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;
import twilightforest.tags.TFBlockTags;
import twilightforest.tags.TFEntityTypeTags;

import java.util.List;

@Component
public class ToolEvents {

	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;

	@PostConstruct
	private void setup() {
		// TODO: Port to Fabric event system
		/*
		NeoForge.EVENT_BUS.addListener(this::onEnderBowHit);
		NeoForge.EVENT_BUS.addListener(this::fieryToolSetFire);
		NeoForge.EVENT_BUS.addListener(this::doKnightmetalToolLogic);
		NeoForge.EVENT_BUS.addListener(this::addExtraAxeChargingDamage);
		NeoForge.EVENT_BUS.addListener(this::damageNonMazebreakerToolsMore);
		NeoForge.EVENT_BUS.addListener(this::preventFatigueWithPocketWatch);
		NeoForge.EVENT_BUS.addListener(this::handleGiantPickaxeMining);
		NeoForge.EVENT_BUS.addListener(this::refreshOreMagnetCache);
		*/
	}

	private void onEnderBowHit(FabricEvents.ProjectileImpactEvent evt) {
		Projectile arrow = (Projectile) evt.getProjectile();
		if (arrow.getOwner() instanceof Player player
			&& evt.getRayTraceResult() instanceof EntityHitResult result
			&& result.getEntity() instanceof LivingEntity living
			&& arrow.getOwner() != result.getEntity() && !result.getEntity().is(TFEntityTypeTags.BOSSES)) { // TODO: Port - verify BOSSES tag exists in TFEntityTypeTags

			if (((TFEntityExtensions) player).getPersistentData().getCompound("PlayerPersisted").orElse(new net.minecraft.nbt.CompoundTag()).contains(EnderBowItem.KEY)) {
				double sourceX = player.getX(), sourceY = player.getY(), sourceZ = player.getZ();
				float sourceYaw = player.getYRot(), sourcePitch = player.getXRot();
				@Nullable Entity playerVehicle = player.getVehicle();

				player.setYRot(living.getYRot());
				player.teleportTo(living.getX(), living.getY(), living.getZ());
				player.invulnerableTime = 40;
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

	private void handleGiantPickaxeMining(FabricEvents.BreakBlockEvent event) {
		BlockPos pos = event.getPos();
		BlockState state = event.getState();

		if (event.getPlayer() instanceof ServerPlayer player && canHarvestWithGiantPick(player, state, pos)) {
			var attachment = ((TFEntityExtensions) player).getData(() -> TFDataAttachments.GIANT_PICKAXE_MINING);

			if (shouldBreakGiantBlock(player, attachment)) {
				attachment.setBreaking(true); // Tell the capability that a block breaking loop is happening, so it knows to fail the if check above. Otherwise, this would go on forever

				LootParams.Builder builder = new LootParams.Builder(player.level())
					.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
					.withParameter(LootContextParams.BLOCK_STATE, state)
					.withOptionalParameter(LootContextParams.THIS_ENTITY, player)
					.withParameter(LootContextParams.TOOL, player.getMainHandItem());

				List<ItemStack> drops = state.getDrops(builder);

				if (!drops.isEmpty() && drops.getFirst().getItem() instanceof BlockItem block) {
					boolean allTheSame = GiantToolGroupingModifier.CONVERSIONS.containsKey(block.getBlock()); //check if the block drops can be converted instead of the block itself so things like stone can make giant cobble
					if (allTheSame) {
						for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
							if (!player.level().getBlockState(offsetPos).is(state.getBlock())) {
								allTheSame = false;
								break; //end early: we have determined we arent getting a giant block from this. No need to keep checking positions
							}
						}
					}
					attachment.setGiantBlockConversion(allTheSame ? 64 : 0); // NO IN-BETWEEN! Either the whole 64 get converted, or none do
				}
				event.setCanceled(true); // We cancel this event, since we want to break the block we're looking at first
				player.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				player.gameMode.destroyBlock(pos); // Break the block we broke, for real this time

				// Break all the other blocks, if they're the same type
				for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
					if (!offsetPos.equals(pos) && player.level().getBlockState(offsetPos).is(state.getBlock())) {
						BlockPos newPos = new BlockPos(offsetPos); // This feels dumb, but without it, the client thinks the last block in the iterator is broken too
						player.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(player.level().getBlockState(newPos)));
						player.gameMode.destroyBlock(newPos);
					}
				}
				attachment.setBreaking(false); // Tell the capability that the loop is over, and all is good in the world
			}
		}
	}

	private static boolean canHarvestWithGiantPick(Player player, BlockState state, BlockPos pos) {
		return player.getMainHandItem().getItem() instanceof GiantPickItem && player.hasCorrectToolForDrops(state);
	}

	private static boolean shouldBreakGiantBlock(Player player, GiantPickaxeMiningAttachment attachment) {
		return attachment.getMining() == player.level().getGameTime() && !attachment.getBreaking();
	}

	private void refreshOreMagnetCache(FabricEvents.TagsUpdatedEvent event) {
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
}
