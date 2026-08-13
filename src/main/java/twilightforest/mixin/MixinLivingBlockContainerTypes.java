package twilightforest.mixin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.livingblock.LivingBlockType;
import net.minecraft.world.entity.livingblock.LivingBlockTypeRegistry;
import net.minecraft.world.entity.livingblock.LivingBlockTypes;
import net.minecraft.world.entity.livingblock.behavior.LivingBlockContainerBehavior;
import net.minecraft.world.entity.livingblock.behavior.ShowCraftingGridBehavior;
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

/**
 * Registers Twilight Forest containers as LivingBlock types so that, when thrown/picked
 * up as LivingBlocks, they gain container behaviour (inventory + open interaction) instead
 * of behaving as plain blocks.
 * <p>
 * Mirrors the NeoForge 26w14a {@code twilightforest.mixin.MixinLivingBlockContainerTypes}: we
 * maintain a small map of TF container items -> LivingBlockType and override
 * {@code LivingBlockTypeRegistry#get(Item)} to return the correct type. Chests use the
 * vanilla {@code CONTAINER} preset plus a 27-slot container behaviour and the default
 * open-container interaction; the uncrafting table uses a crafting-grid behaviour plus a
 * custom {@link OpenUncraftingTableInteraction}.
 */
@Mixin(LivingBlockTypeRegistry.class)
public class MixinLivingBlockContainerTypes {
	private static final Map<Item, LivingBlockType> TF_CONTAINER_TYPES = new HashMap<>();
	private static boolean initialized = false;

	private static void initIfNeeded() {
		if (initialized) {
			return;
		}
		initialized = true;

		LivingBlockType chestType = LivingBlockType.builder()
			.apply(LivingBlockTypes.CONTAINER)
			.behavior(LivingBlockContainerBehavior.containerBlockEntity(27, SoundEvents.LLAMA_EAT, SoundEvents.LLAMA_SPIT))
			.moveUsing(WaterFloatingMovement::new)
			.onInteract(OpenContainerInteraction::new)
			.build();

		LivingBlockType uncraftingTableType = LivingBlockType.builder()
			.apply(LivingBlockTypes.CONTAINER)
			.behavior(ShowCraftingGridBehavior.showCraftingTable())
			.moveUsing(WaterFloatingMovement::new)
			.onInteract(OpenUncraftingTableInteraction::new)
			.build();

		List<Block> containerBlocks = List.of(
			TFBlocks.TWILIGHT_OAK_CHEST, TFBlocks.CANOPY_CHEST, TFBlocks.MANGROVE_CHEST, TFBlocks.DARK_CHEST,
			TFBlocks.TIME_CHEST, TFBlocks.TRANSFORMATION_CHEST, TFBlocks.MINING_CHEST, TFBlocks.SORTING_CHEST,
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, TFBlocks.CANOPY_TRAPPED_CHEST, TFBlocks.MANGROVE_TRAPPED_CHEST, TFBlocks.DARK_TRAPPED_CHEST,
			TFBlocks.TIME_TRAPPED_CHEST, TFBlocks.TRANSFORMATION_TRAPPED_CHEST, TFBlocks.MINING_TRAPPED_CHEST, TFBlocks.SORTING_TRAPPED_CHEST,
			TFBlocks.SKULL_CHEST, TFBlocks.KEEPSAKE_CASKET
		);
		for (Block block : containerBlocks) {
			TF_CONTAINER_TYPES.put(block.asItem(), chestType);
		}
		TF_CONTAINER_TYPES.put(TFBlocks.UNCRAFTING_TABLE.asItem(), uncraftingTableType);
	}

	@Inject(method = "get(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/entity/livingblock/LivingBlockType;", at = @At("RETURN"), cancellable = true)
	private void tf$containerType(Item item, CallbackInfoReturnable<LivingBlockType> cir) {
		initIfNeeded();
		LivingBlockType containerType = TF_CONTAINER_TYPES.get(item);
		if (containerType != null) {
			cir.setReturnValue(containerType);
		}
	}
}