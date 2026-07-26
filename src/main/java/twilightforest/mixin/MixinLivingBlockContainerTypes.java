package twilightforest.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.livingblock.CollisionInteraction;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.LivingBlockType;
import net.minecraft.world.entity.livingblock.LivingBlockTypeRegistry;
import net.minecraft.world.entity.livingblock.behavior.*;
import net.minecraft.world.entity.livingblock.cognition.Desires;
import net.minecraft.world.entity.livingblock.interact.OpenContainerInteraction;
import net.minecraft.world.entity.livingblock.movement.WaterFloatingMovement;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.livingblock.OpenUncraftingTableInteraction;
import twilightforest.init.TFBlocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(LivingBlockTypeRegistry.class)
public class MixinLivingBlockContainerTypes {

	private static final Map<Item, LivingBlockType> TF_CONTAINER_TYPES = new HashMap<>();
	private static boolean initialized = false;

	private static void initIfNeeded() {
		if (initialized) return;
		initialized = true;

		LivingBlockType chestType = LivingBlockType.builder()
			.behavior(ActOnDesireBehavior.actOnDesire(Desires.APPROACH, LivingBlock.MOVE_TOWARDS))
			.behavior(ActOnDesireBehavior.actOnDesire(Desires.ATTACK, LivingBlock.BE_MAD_AT))
			.behavior(BeCraftingIngredient.BECOME_CRAFTING_INGREDIENT)
			.behavior(AvoidBeingInTheWayOfCrafting.AVOID_DISRUPTING_CRAFTING)
			.behavior(AttackEntityBehavior.BEHAVIOR)
			.behavior(ProtectPositionBehavior.BE_MAD_AT_INTRUDERS)
			.behavior(ProtectPositionBehavior.RETURN_HOME_WHEN_ABLE)
			.behavior(EquipOnEntityBehavior.BEHAVIOR)
			.behavior(SmeltBehavior.LAVA)
			.behavior(SmeltBehavior.FIRE)
			.behavior(LivingBlockContainerBehavior.containerBlockEntity(27, SoundEvents.LLAMA_EAT, SoundEvents.LLAMA_SPIT))
			.moveUsing(WaterFloatingMovement::new)
			.onInteract(OpenContainerInteraction::new)
			.collision(CollisionInteraction.BLOCK)
			.build();

		LivingBlockType uncraftingTableType = LivingBlockType.builder()
			.behavior(ActOnDesireBehavior.actOnDesire(Desires.APPROACH, LivingBlock.MOVE_TOWARDS))
			.behavior(ActOnDesireBehavior.actOnDesire(Desires.ATTACK, LivingBlock.BE_MAD_AT))
			.behavior(BeCraftingIngredient.BECOME_CRAFTING_INGREDIENT)
			.behavior(AvoidBeingInTheWayOfCrafting.AVOID_DISRUPTING_CRAFTING)
			.behavior(AttackEntityBehavior.BEHAVIOR)
			.behavior(ProtectPositionBehavior.BE_MAD_AT_INTRUDERS)
			.behavior(ProtectPositionBehavior.RETURN_HOME_WHEN_ABLE)
			.behavior(EquipOnEntityBehavior.BEHAVIOR)
			.behavior(SmeltBehavior.LAVA)
			.behavior(SmeltBehavior.FIRE)
			.behavior(ShowCraftingGridBehavior.showCraftingTable())
			.moveUsing(WaterFloatingMovement::new)
			.onInteract(OpenUncraftingTableInteraction::new)
			.collision(CollisionInteraction.BLOCK)
			.build();

		List<Block> containerBlocks = List.of(
			TFBlocks.TWILIGHT_OAK_CHEST.get(), TFBlocks.CANOPY_CHEST.get(),
			TFBlocks.MANGROVE_CHEST.get(), TFBlocks.DARK_CHEST.get(),
			TFBlocks.TIME_CHEST.get(), TFBlocks.TRANSFORMATION_CHEST.get(),
			TFBlocks.MINING_CHEST.get(), TFBlocks.SORTING_CHEST.get(),
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.get(), TFBlocks.CANOPY_TRAPPED_CHEST.get(),
			TFBlocks.MANGROVE_TRAPPED_CHEST.get(), TFBlocks.DARK_TRAPPED_CHEST.get(),
			TFBlocks.TIME_TRAPPED_CHEST.get(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.get(),
			TFBlocks.MINING_TRAPPED_CHEST.get(), TFBlocks.SORTING_TRAPPED_CHEST.get(),
			TFBlocks.SKULL_CHEST.get(), TFBlocks.KEEPSAKE_CASKET.get()
		);

		for (Block block : containerBlocks) {
			TF_CONTAINER_TYPES.put(block.asItem(), chestType);
		}

		TF_CONTAINER_TYPES.put(TFBlocks.UNCRAFTING_TABLE.get().asItem(), uncraftingTableType);
	}

	@Inject(method = "get(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/entity/livingblock/LivingBlockType;",
		at = @At("RETURN"), cancellable = true)
	private void twilightforest$containerType(Item item, CallbackInfoReturnable<LivingBlockType> cir) {
		initIfNeeded();
		LivingBlockType containerType = TF_CONTAINER_TYPES.get(item);
		if (containerType != null) {
			cir.setReturnValue(containerType);
		}
	}
}
