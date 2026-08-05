package twilightforest.loot.modifiers;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;
import twilightforest.util.TFEntityExtensions;

import java.util.HashMap;
import java.util.Map;

public final class GiantToolGroupingModifier {
	public static final Map<Block, Item> CONVERSIONS = new HashMap<>();

	private GiantToolGroupingModifier() {
	}

	public static void bootstrapConversions() {
		Item giantCobble = TFBlocks.GIANT_COBBLESTONE.asItem();
		Item giantLog = TFBlocks.GIANT_LOG.asItem();
		Item giantLeaves = TFBlocks.GIANT_LEAVES.asItem();
		Item giantObsidian = TFBlocks.GIANT_OBSIDIAN.asItem();
		
		CONVERSIONS.put(Blocks.COBBLESTONE, giantCobble);
		CONVERSIONS.put(Blocks.STONE, giantCobble);
		CONVERSIONS.put(Blocks.OAK_LOG, giantLog);
		CONVERSIONS.put(Blocks.OAK_LEAVES, giantLeaves);
		CONVERSIONS.put(Blocks.OBSIDIAN, giantObsidian);
	}

	/**
	 * Called from TFLootModifiers.apply() via LootTableMixin at RETURN of getRandomItems.
	 * Converts 64 of the same block type into 1 giant block item.
	 *
	 * The conversion count starts at 64 (set by handleGiantPickaxeMining).
	 * When count == 64 (first evaluation), the giant block is returned instead of the individual drops.
	 * For subsequent evaluations, empty list is returned (all gets converted into giant block).
	 */
	public static ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootParams context) {
		Entity entity = context.contextMap().getOptional(LootContextParams.THIS_ENTITY);

		if (!(entity instanceof Player player)) {
			return generatedLoot;
		}

		if (generatedLoot.isEmpty()) {
			return generatedLoot;
		}

		if (!(generatedLoot.getFirst().getItem() instanceof BlockItem blockItem)) {
			return generatedLoot;
		}

		Item giantBlock = CONVERSIONS.get(blockItem.getBlock());
		if (giantBlock == null) {
			return generatedLoot;
		}

		var attachment = ((TFEntityExtensions) player).twilightforest$getData(TFDataAttachments.GIANT_PICKAXE_MINING);

		if (attachment.getMining() == 0L || !attachment.canMakeGiantBlock()) {
			return generatedLoot;
		}

		int blockConversion = attachment.getGiantBlockConversion();
		attachment.setGiantBlockConversion(blockConversion - 1);
		return blockConversion == 64 ? ObjectArrayList.of(new ItemStack(giantBlock)) : new ObjectArrayList<>();
	}
}
