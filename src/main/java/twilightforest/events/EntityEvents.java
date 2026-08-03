package twilightforest.events;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.beanification.Autowired;
import twilightforest.beanification.PostConstruct;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.DrinkFromFlaskTrigger;
import twilightforest.block.*;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.block.entity.SkullChestBlockEntity;
import twilightforest.config.TFConfig;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;
import twilightforest.entity.projectile.ITFProjectile;
import twilightforest.entity.projectile.LichBomb;
import twilightforest.init.*;
import twilightforest.item.FieryArmorItem;
import twilightforest.network.SyncQuestsPacket;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.util.datamaps.EntityTransformation;
import twilightforest.util.TFEntityExtensions;
import twilightforest.util.entities.EntityUtil;
import twilightforest.util.entities.OminousFireDamageSource;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.finalcastle.FinalCastleBossGazeboComponent;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.ValidatedSpawnLocations;

import java.net.URI;
import java.util.*;
import java.util.function.Consumer;
import twilightforest.network.PacketDistributor;

@twilightforest.beanification.Component
public class EntityEvents {

	@Autowired
	private QuestingRamCurrentContext questingRamCurrentContext;

	@PostConstruct
	private void setup() {
		// TODO: Port to Fabric event system
		/*
		NeoForge.EVENT_BUS.addListener(this::ominousFireConversion);
		NeoForge.EVENT_BUS.addListener(this::zombifiedPlayerAttacks);
		NeoForge.EVENT_BUS.addListener(this::entityHurts);
		NeoForge.EVENT_BUS.addListener(this::alertPlayerCastleIsWIP);
		NeoForge.EVENT_BUS.addListener(this::attachLeadToWroughtFence);
		NeoForge.EVENT_BUS.addListener(this::wipeOreMeterOnLeftClick);
		NeoForge.EVENT_BUS.addListener(this::onCasketBreak);
		NeoForge.EVENT_BUS.addListener(this::reduceFrostedEffectIfOnFire);
		NeoForge.EVENT_BUS.addListener(this::onParryProjectile);
		NeoForge.EVENT_BUS.addListener(this::createSkullCandle);
		NeoForge.EVENT_BUS.addListener(this::addCloudJumpParticles);
		NeoForge.EVENT_BUS.addListener(this::structureSpecialSpawns);
		NeoForge.EVENT_BUS.addListener(this::removeCastleTextIfAttacked);
		NeoForge.EVENT_BUS.addListener(this::adjustEntityHealthInMultiplayerFights);
		NeoForge.EVENT_BUS.addListener(this::addQualifiedGroupPlayerIfNeeded);
		NeoForge.EVENT_BUS.addListener(this::grantGroupAdvancementIfNeeded);
		NeoForge.EVENT_BUS.addListener(this::lichBombsDontBlowUpItems);
		NeoForge.EVENT_BUS.addListener(this::handleQuestSyncing);
		NeoForge.EVENT_BUS.addListener(this::resetFlaskLogic);
		NeoForge.EVENT_BUS.addListener(this::handleLeashPathingOverrides);
		NeoForge.EVENT_BUS.addListener(this::stopEndermenFromGrabbingBlocksInTF);
		*/
	}

	private void ominousFireConversion(FabricEvents.LivingDeathEvent event) {
		if (!event.isCanceled() && event.getSource().is(TFDamageTypes.OMINOUS_FIRE)) {
			EntityTransformation dataMap = null; // TODO: Port - getData from registry data maps

			if (event.getEntity() instanceof ServerPlayer player) {
				var zombie = EntityType.ZOMBIE.create(player.level(), EntitySpawnReason.CONVERSION);
				((TFEntityExtensions) zombie).setData(() -> TFDataAttachments.ZOMBIFIED_PLAYER, player.getGameProfile());
				zombie.setCustomName(player.getName());
				zombie.copyPosition(player);
				zombie.setCanPickUpLoot(true);
				zombie.setBaby(false);
				// EventHooks.finalizeMobSpawn(zombie, player.level(), player.level().getCurrentDifficultyAt(player.blockPosition()), EntitySpawnReason.CONVERSION, null)); // TODO: Port - NeoForge hook
				player.level().addFreshEntity(zombie);
			} else if (dataMap != null && event.getEntity().level() instanceof ServerLevel) {
				EntityUtil.convertEntity(event.getEntity(), dataMap.result());
			}
		}
	}

	private void zombifiedPlayerAttacks(FabricEvents.LivingIncomingDamageEvent event) {
		if (!(event.getSource() instanceof OminousFireDamageSource) && event.getSource().getEntity() instanceof Zombie zombie && ((TFEntityExtensions) zombie).hasData(() -> TFDataAttachments.ZOMBIFIED_PLAYER)) {
			float amount = event.getAmount();
			event.setCanceled(true);
			event.getEntity().hurt(new OminousFireDamageSource(event.getSource()), amount);
		}
	}

	private void alertPlayerCastleIsWIP(FabricEvents.AdvancementEvent.AdvancementEarnEvent event) {
		if (event.getAdvancement().id().equals(TwilightForestMod.prefix("progression_end"))) {
			event.getEntity().sendSystemMessage(Component.translatable("gui.twilightforest.progression_end.message", Component.translatable("gui.twilightforest.progression_end.discord").withStyle(style -> style.withColor(ChatFormatting.BLUE).applyFormat(ChatFormatting.UNDERLINE).withClickEvent(new ClickEvent.OpenUrl(URI.create("https://discord.experiment115.com/"))))));
		}
	}

	private void attachLeadToWroughtFence(FabricEvents.PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		ItemStack stack = player.getItemInHand(event.getHand());
		if (stack.is(Items.LEAD)) {
			BlockPos pos = event.getPos();
			BlockState state = event.getLevel().getBlockState(pos);
			if (state.is(TFBlocks.WROUGHT_IRON_FENCE) && state.getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.NONE) {
				if (!event.getLevel().isClientSide()) {
					LeadItem.bindPlayerMobs(player, event.getLevel(), event.getPos());
					event.setCanceled(true);
					event.setCancellationResult(InteractionResult.SUCCESS);
				}
			}
		}
	}

	private void wipeOreMeterOnLeftClick(FabricEvents.PlayerInteractEvent.LeftClickEmpty event) {
		// TODO: Port to Fabric - move to client-side handler using twilightforest.network.client.ClientPacketDistributor
		/*
		ItemStack item = event.getItemStack();
		if (item.is(TFItems.ORE_METER) && (item.has(TFDataComponents.ORE_DATA) || item.has(TFDataComponents.ORE_FILTER))) {
			// Use ClientPacketDistributor.sendToServer(new WipeOreMeterPacket(event.getHand()));
			item.remove(TFDataComponents.ORE_DATA);
			item.remove(TFDataComponents.ORE_FILTER);
			event.getLevel().playSound(event.getEntity(), event.getEntity().blockPosition(), TFSounds.ORE_METER_CLEAR, SoundSource.PLAYERS, 1.25F, event.getLevel().getRandom().nextFloat() * 0.2F + 0.6F);
		}
		*/
	}

	private void entityHurts(FabricEvents.LivingDamageEvent.Post event) {
		LivingEntity living = event.getEntity();
		DamageSource source = event.getSource();
		Entity trueSource = source.getEntity();

		// fire react and chill aura
		if (source.getEntity() != null && trueSource != null && event.getOriginalDamage() > 0) {
			int fireLevel = getGearCoverage(living, false) * 5;
			int chillLevel = getGearCoverage(living, true);

			if (fireLevel > 0 && living.getRandom().nextInt(25) < fireLevel && !trueSource.fireImmune()) {
				trueSource.igniteForSeconds(fireLevel / 2);
			}

			if (trueSource instanceof LivingEntity target) {
				ApplyFrostedEffect.doChillAuraEffect(target, chillLevel * 5 + 5, chillLevel, chillLevel > 0);
			}
		}

		// triple bow strips invulnerableTime
		if (source.getMsgId().equals("arrow") && trueSource instanceof Player player) {

			if (player.getItemInHand(player.getUsedItemHand()).is(TFItems.TRIPLE_BOW)) {
				living.invulnerableTime = 0;
			}
		}
	}

	//if our casket is owned by someone and that player isnt the one breaking it, stop them
	private void onCasketBreak(FabricEvents.BreakBlockEvent event) {
		Player player = event.getPlayer();
		if (event.getState().getBlock() instanceof SkullChestBlock) {
			BlockEntity te = event.getLevel().getBlockEntity(event.getPos());
			if (te instanceof SkullChestBlockEntity casket) {
				ResolvableProfile checker = casket.owner;
				if (checker != null && !casket.isEmpty()) {
					// GameProfile now uses record accessor .id(); ResolvableProfile uses .partialProfile()
					if (!Commands.LEVEL_ADMINS.check(player.permissions()) || !player.getGameProfile().id().equals(checker.partialProfile().id())) {
						event.setCanceled(true);
					}
				}
			}
		}
	}

	private void reduceFrostedEffectIfOnFire(FabricEvents.LivingIncomingDamageEvent event) {
		if (!event.isCanceled()) {
			LivingEntity living = event.getEntity();
			Optional.ofNullable(living.getEffect(TFMobEffects.FROSTY)).ifPresent(mobEffectInstance -> {
				if (event.getSource().typeHolder().is(DamageTypes.FREEZE)) {
					event.setAmount(event.getAmount() + (float) (mobEffectInstance.getAmplifier() / 2));
				} else if (event.getSource().typeHolder().is(DamageTypeTags.IS_FIRE)) {
					living.removeEffect(TFMobEffects.FROSTY);
					mobEffectInstance.amplifier -= 1;
					if (mobEffectInstance.amplifier >= 0) living.addEffect(mobEffectInstance);
				}
			});
		}
	}

	// Parrying
	private void onParryProjectile(FabricEvents.ProjectileImpactEvent event) {
		Entity projectileEntity = event.getProjectile();

		if (!(projectileEntity instanceof Projectile projectile)) return;

		if (!projectile.level().isClientSide() && (TFConfig.parryNonTwilightAttacks || projectile instanceof ITFProjectile)) {
			if (event.getRayTraceResult() instanceof EntityHitResult result) {
				Entity entity = result.getEntity();

				if (entity instanceof LivingEntity entityBlocking) {
					if (entityBlocking.isBlocking() && entityBlocking.getUseItem().getUseDuration(entityBlocking) - entityBlocking.getUseItemRemainingTicks() <= TFConfig.shieldParryTicks) {
						projectile.deflect(ProjectileDeflection.AIM_DEFLECT, entityBlocking, EntityReference.of(entityBlocking), true);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	/**
	 * Checks if the player is attempting to create a skull candle
	 */
	// I wanted to make sure absolutely nothing broke, so I also check against the namespaces of the item to make sure theyre vanilla.
	// Worst case some stupid mod adds their own stuff to the minecraft namespace and breaks this, then you can disable this via config.
	private void createSkullCandle(FabricEvents.PlayerInteractEvent.RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		if (!TFConfig.disableSkullCandles) {
			if (stack.is(ItemTags.CANDLES) && BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals("minecraft") && !event.getEntity().isShiftKeyDown()) {
				if (state.getBlock() instanceof AbstractSkullBlock skull && BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace().equals("minecraft")) {
					SkullBlock.Types type = (SkullBlock.Types) skull.getType();
					boolean wall = state.getBlock() instanceof WallSkullBlock;
					switch (type) {
						case SKELETON -> {
							if (wall) makeSkullCandle(event, TFBlocks.SKELETON_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.SKELETON_SKULL_CANDLE);
						}
						case WITHER_SKELETON -> {
							if (wall) makeSkullCandle(event, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.WITHER_SKELE_SKULL_CANDLE);
						}
						case PLAYER -> {
							if (wall) makeSkullCandle(event, TFBlocks.PLAYER_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.PLAYER_SKULL_CANDLE);
						}
						case ZOMBIE -> {
							if (wall) makeSkullCandle(event, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.ZOMBIE_SKULL_CANDLE);
						}
						case CREEPER -> {
							if (wall) makeSkullCandle(event, TFBlocks.CREEPER_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.CREEPER_SKULL_CANDLE);
						}
						case PIGLIN -> {
							if (wall) makeSkullCandle(event, TFBlocks.PIGLIN_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.PIGLIN_SKULL_CANDLE);
						}
						default -> {
							return;
						}
					}
					stack.consume(1, event.getEntity());
					event.getEntity().swing(event.getHand());
					if (event.getEntity() instanceof ServerPlayer)
						event.getEntity().awardStat(TFStats.SKULL_CANDLES_MADE);
					//this is to prevent anything from being placed afterwords
					event.setCanceled(true);
				}
			}
		}
	}

	private static void makeSkullCandle(FabricEvents.PlayerInteractEvent.RightClickBlock event, Block newBlock) {
		ResolvableProfile profile = null;
		Level level = event.getLevel();
		if (level.getBlockEntity(event.getPos()) instanceof SkullBlockEntity skull)
			profile = skull.getOwnerProfile();
		level.playSound(null, event.getPos(), SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlockAndUpdate(event.getPos(), newBlock.withPropertiesOf(level.getBlockState(event.getPos()))
			.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE));
		level.setBlockEntity(new SkullCandleBlockEntity(event.getPos(),
			newBlock.withPropertiesOf(level.getBlockState(event.getPos()))
				.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)));
	}

	/**
	 * Add up the number of armor pieces the player is wearing (either fiery or yeti)
	 */
	public static int getGearCoverage(LivingEntity entity, boolean yeti) {
		int amount = 0;

		for (EquipmentSlot slot : EquipmentSlot.VALUES) {
			ItemStack armor = entity.getItemBySlot(slot);
			if (!armor.isEmpty() && (yeti ? armor.getItem() instanceof twilightforest.item.YetiArmorItem : armor.getItem() instanceof FieryArmorItem)) {
				amount++;
			}
		}

		return amount;
	}

	private void addCloudJumpParticles(FabricEvents.LivingEvent.LivingJumpEvent event) {
		LivingEntity living = event.getEntity();
		if (living.level().isClientSide() && !living.isSpectator() && living.level().getBlockState(living.getOnPos()).getBlock() instanceof CloudBlock) {
			for (int i = 0; i < 12; i++)
				CloudBlock.addEntityMovementParticles(living.level(), living.getOnPos(), living, true);
		}
	}

	private static int getSpawnListIndexAt(StructureStart start, BlockPos pos) {
		int highestFoundIndex = -1;
		for (StructurePiece component : start.getPieces()) {
			if (component.getBoundingBox().isInside(pos) && component instanceof SpawnIndexProvider indexProvider && indexProvider.getSpawnIndex() > highestFoundIndex) {
				highestFoundIndex = indexProvider.getSpawnIndex();
			}
		}
		return highestFoundIndex;
	}

	private static final int STRUCTURE_STARTS_CACHE_MAX_SIZE = 64;
	private static final Map<Long, List<StructureStart>> STRUCTURE_STARTS_CACHE = new LinkedHashMap<>() {
		@Override
		protected boolean removeEldestEntry(Map.Entry<Long, List<StructureStart>> eldest) {
			return size() > STRUCTURE_STARTS_CACHE_MAX_SIZE;
		}
	};

	/**
	 * Returns the structure-controlled spawn list at the given position, or {@code null} when
	 * no controlled-spawns structure applies. This is the Fabric equivalent of the NeoForge
	 * PotentialSpawns event: NaturalSpawnerMixin calls this while the game picks the spawn
	 * list so that mobs inside Twilight Forest structures (Tower, Lich Tower, Dark Tower, ...)
	 * spawn from the structure's own list instead of the biome list.
	 */
	@Nullable
	public static WeightedList<MobSpawnSettings.SpawnerData> getPotentialStructureSpawns(StructureManager structureManager, MobCategory classification, BlockPos pos) {
		ChunkPos chunkPos = ChunkPos.containing(pos);
		List<StructureStart> structureStarts;
		synchronized (STRUCTURE_STARTS_CACHE) {
			structureStarts = STRUCTURE_STARTS_CACHE.computeIfAbsent(ChunkPos.pack(chunkPos.x(), chunkPos.z()), k -> structureManager.startsForStructure(chunkPos, s -> s instanceof ControlledSpawns));
		}

		for (StructureStart start : structureStarts) {
			if (start.getStructure() instanceof ControlledSpawns landmark) {

				if (!start.isValid())
					continue;

				if (classification != MobCategory.MONSTER) {
					return landmark.getSpawnableList(classification);
				}

				if (start instanceof TFStructureStart s && s.isConquered())
					return null;

				if (landmark instanceof ValidatedSpawnLocations validator && !validator.canSpawnMob(pos, start.getBoundingBox()))
					return null;

				final int index = getSpawnListIndexAt(start, pos);
				if (index < 0)
					return null;

				return landmark.getSpawnableMonsterList(index);
			}
		}
		return null;
	}

	public static void gatherPotentialSpawns(StructureManager structureManager, MobCategory classification, BlockPos pos, Consumer<? super MobSpawnSettings.SpawnerData> consumer) {
		WeightedList<MobSpawnSettings.SpawnerData> structureSpawns = getPotentialStructureSpawns(structureManager, classification, pos);
		if (structureSpawns != null) {
			structureSpawns.unwrap().forEach(weighted -> consumer.accept(weighted.value()));
		}
	}

	private void structureSpecialSpawns(FabricEvents.LevelEvent.PotentialSpawns event) {
		if (!(event.getLevel() instanceof ServerLevel serverLevel))
			return;

		List<MobSpawnSettings.SpawnerData> potentialStructureSpawns = new ArrayList<>();

		gatherPotentialSpawns(
			serverLevel.structureManager(),
			event.getMobCategory(),
			event.getPos(),
			potentialStructureSpawns::add
		);

		if (!potentialStructureSpawns.isEmpty()) {
			List.copyOf(event.getSpawnerDataList()).forEach(event::removeSpawnerData);
			potentialStructureSpawns.forEach(event::addSpawnerData);
		}
	}

	private void removeCastleTextIfAttacked(FabricEvents.AttackEntityEvent event) {
		// For clearing our Display text entities at the Final Castle Gazebo, there's no other way to remove them otherwise
		// The tag distinguishes our Interaction entities from other Mods' utilization
		if (event.getTarget().level() instanceof ServerLevel level && event.getTarget() instanceof Interaction interaction
			&& interaction.entityTags().contains(FinalCastleBossGazeboComponent.INTERACTION_TAG)) {
			AABB bounds = interaction.getBoundingBox();
			level.getEntities(interaction, bounds, e -> e instanceof Display).forEach(Entity::discard);
			interaction.discard();
		}
	}

	private void adjustEntityHealthInMultiplayerFights(FabricEvents.FinalizeSpawnEvent event) {
		if (event.getEntity().is(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES)) {
			if (TFConfig.multiplayerFightAdjuster.adjustsHealth()) {
				List<ServerPlayer> nearbyPlayers = event.getLevel().getEntitiesOfClass(ServerPlayer.class, event.getEntity().getBoundingBox().inflate(32, 10, 32), player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.ENTITY_STILL_ALIVE).test(player));
				if (nearbyPlayers.size() > 1 && event.getEntity().getAttribute(Attributes.MAX_HEALTH) != null) {
					event.getEntity().getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(TwilightForestMod.prefix("group_health_boost"), getHealthBasedOnDifficulty(event.getDifficulty().getDifficulty()) * (nearbyPlayers.size() - 1), AttributeModifier.Operation.ADD_VALUE));
				}
			}
		}
	}

	private static double getHealthBasedOnDifficulty(Difficulty difficulty) {
		return switch (difficulty) {
			case EASY -> 20.0D;
			case NORMAL -> 40.0D;
			case HARD -> 60.0D;
			default -> 0.0D;
		};
	}

	private void addQualifiedGroupPlayerIfNeeded(FabricEvents.LivingDamageEvent.Post event) {
		if (event.getEntity().is(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES)) {
			var data = ((TFEntityExtensions) event.getEntity()).getData(() -> TFDataAttachments.MULTIPLAYER_FIGHT);
			if (event.getSource().getEntity() != null) {
				data.maybeAddQualifiedPlayer(event.getSource().getEntity());
			}
		}
	}

	private void grantGroupAdvancementIfNeeded(FabricEvents.LivingDeathEvent event) {
		if (!event.isCanceled() && ((TFEntityExtensions) event.getEntity()).hasData(() -> TFDataAttachments.MULTIPLAYER_FIGHT)) {
			((TFEntityExtensions) event.getEntity()).getData(() -> TFDataAttachments.MULTIPLAYER_FIGHT).grantGroupAdvancement(event.getEntity());
		}
	}

	private void lichBombsDontBlowUpItems(FabricEvents.ExplosionEvent.Detonate event) {
		if (event.getExplosion().getDirectSourceEntity() instanceof LichBomb) {
			event.getAffectedEntities().removeIf(entity -> entity instanceof ItemEntity || entity instanceof LichBomb);
		}
	}

	private void handleQuestSyncing(FabricEvents.OnDatapackSyncEvent event) {
		if (event.getPlayer() != null) {
			PacketDistributor.sendToPlayer((net.minecraft.server.level.ServerPlayer) event.getPlayer(), new SyncQuestsPacket(this.questingRamCurrentContext.getContext()));
		} else {
			event.getPlayerList().getPlayers().forEach(player -> PacketDistributor.sendToPlayer(player, new SyncQuestsPacket(this.questingRamCurrentContext.getContext())));
		}
	}

	private void resetFlaskLogic(FabricEvents.AdvancementEvent.AdvancementEarnEvent event) {
		for (var criteria : event.getAdvancement().value().criteria().entrySet()) {
			if (criteria.getValue().trigger() instanceof DrinkFromFlaskTrigger) {
				((TFEntityExtensions) event.getEntity()).getData(() -> TFDataAttachments.FLASK_DOSES).resetDoses();
				break;
			}
		}
	}

	private void handleLeashPathingOverrides(FabricEvents.EntityJoinLevelEvent event) {
		if (!(event.getEntity() instanceof PathfinderMob mob && ((TFEntityExtensions) mob).hasData(() -> TFDataAttachments.LEASH_PATHFINDER_OVERRIDE))) {
			return;
		}

		if (!mob.mayBeLeashed()) {
			((TFEntityExtensions) mob).removeData(() -> TFDataAttachments.LEASH_PATHFINDER_OVERRIDE);
		}
	}

	private void stopEndermenFromGrabbingBlocksInTF(FabricEvents.EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof EnderMan enderMan) {
			var goalSelector = ((TFEntityExtensions) enderMan).getGoalSelector();
			goalSelector.getAvailableGoals().stream()
				.filter(g -> g.getGoal().getClass().getName().contains("EndermanTakeBlockGoal"))
				.findAny()
				.ifPresent(g -> {
					goalSelector.removeGoal(g.getGoal());
					goalSelector.addGoal(g.getPriority(), new ExtendedEndermanTakeBlockGoal(g.getGoal(), enderMan));
				});
		}
	}

	static class ExtendedEndermanTakeBlockGoal extends Goal {

		private final Goal delegate;
		private final EnderMan enderman;

		public ExtendedEndermanTakeBlockGoal(Goal delegate, EnderMan enderman) {
			this.delegate = delegate;
			this.enderman = enderman;
		}

		@Override
		public boolean canUse() {
			return this.delegate.canUse() && !this.enderman.level().dimensionTypeRegistration().is(TFDimensionData.TWILIGHT_DIM_TYPE);
		}

		@Override
		public void tick() {
			this.delegate.tick();
		}
	}
}
