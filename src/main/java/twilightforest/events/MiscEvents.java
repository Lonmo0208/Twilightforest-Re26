package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.beanification.Component;
import twilightforest.beanification.PostConstruct;
import twilightforest.compat.curios.CuriosCompat;
import twilightforest.entity.monster.DeathTome;
import twilightforest.entity.passive.Bighorn;
import twilightforest.entity.passive.DwarfRabbit;
import twilightforest.entity.passive.Squirrel;
import twilightforest.entity.passive.TinyBird;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFEntities;
import twilightforest.network.CreateMovingCicadaSoundPacket;
import twilightforest.network.PacketDistributor;

@Component
public class MiscEvents {

	@PostConstruct
	private void setup() {
		// 1. addPrey - Add prey targeting goals to cats/foxes/wolves when they spawn
		net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
			if (level instanceof net.minecraft.server.level.ServerLevel) {
				addPrey(new FabricEvents.EntityJoinLevelEvent(entity, level));
			}
		});

		// 2. updateCicadaSoundsOnHead - NOT PORTED: No Fabric equipment change event available
		// Would need a Mixin in LivingEntityMixin to detect equipment changes to the HEAD slot.
		// Kept commented - low priority cosmetic feature.
		// NeoForge.EVENT_BUS.addListener(this::updateCicadaSoundsOnHead);

		// 3. addTomesToLecterns + washOffCloth - Right-click block interactions
		net.fabricmc.fabric.api.event.player.UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			FabricEvents.PlayerInteractEvent.RightClickBlock event = new FabricEvents.PlayerInteractEvent.RightClickBlock(player, hand, hitResult.getBlockPos(), hitResult);
			addTomesToLecterns(event);
			if (event.isCanceled()) return event.getCancellationResult();
			washOffCloth(event);
			return event.isCanceled() ? event.getCancellationResult() : net.minecraft.world.InteractionResult.PASS;
		});
	}

	private void addPrey(FabricEvents.EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof Mob mob) {
			EntityType<?> type = mob.getType();
			if (type == EntityTypes.CAT) {
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, DwarfRabbit.class, true, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Squirrel.class, true, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, TinyBird.class, true, null));
			} else if (type == EntityTypes.OCELOT) {
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, true));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, Squirrel.class, true));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, TinyBird.class, true));
			} else if (type == EntityTypes.FOX) {
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, true));
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, Squirrel.class, true));
			} else if (type == EntityTypes.WOLF) {
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, DwarfRabbit.class, true, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Squirrel.class, true, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Bighorn.class, true, null));
			}
		}
	}

	private void updateCicadaSoundsOnHead(FabricEvents.LivingEquipmentChangeEvent event) {
		LivingEntity living = event.getEntity();

		// from what I can see, vanilla doesn't have a hook for this in the item class. So this will have to do.
		// we only have to check equipping, when its unequipped the sound instance handles the rest

		//if we have a cicada in our curios slot, don't try to run this
		 if (FabricLoader.getInstance().isModLoaded("curios")) {
		 	if (CuriosCompat.isCurioEquipped(living, stack -> stack.is(TFBlocks.CICADA.asItem()))) return;
		 }

		if (!living.level().isClientSide() && event.getSlot() == EquipmentSlot.HEAD && event.getTo().is(TFBlocks.CICADA.asItem())) {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(living, new CreateMovingCicadaSoundPacket(living.getId()));
		}
	}

	private void addTomesToLecterns(FabricEvents.PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		ItemStack stack = player.getItemInHand(event.getHand());

		if (!(stack.getItem() instanceof SpawnEggItem spawnEggItem) || SpawnEggItem.getType(stack) != TFEntities.DEATH_TOME.get())
			return;

		BlockPos pos = event.getPos();
		Level level = event.getLevel();
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() instanceof LecternBlock && !state.getValue(BlockStateProperties.HAS_BOOK)) {
			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
			level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F);

			if (level instanceof ServerLevel serverLevel) {
				DeathTome tome = TFEntities.DEATH_TOME.get().spawn(serverLevel, stack, player, pos.below(), EntitySpawnReason.SPAWN_ITEM_USE, true, false);
				if (tome != null) {
					stack.consume(1, player);
					serverLevel.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
					tome.setOnLectern(true);
				}
			}
		}
	}

	private void washOffCloth(FabricEvents.PlayerInteractEvent.RightClickBlock event) {
		if (event.isCanceled()) return;
		BlockState state = event.getLevel().getBlockState(event.getPos());
		if (!state.is(Blocks.WATER_CAULDRON) || state.getValue(LayeredCauldronBlock.LEVEL) <= 0) return;
		if (event.getItemStack().has(TFDataComponents.EMPERORS_CLOTH)) {
			LayeredCauldronBlock.lowerFillLevel(state, event.getLevel(), event.getPos());
			event.getItemStack().remove(TFDataComponents.EMPERORS_CLOTH);
			event.getEntity().awardStat(Stats.CLEAN_ARMOR);
			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
		}
	}
}
