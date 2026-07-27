package twilightforest.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFDataAttachments;
import twilightforest.util.TFEntityExtensions;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GiantToolGroupingModifier implements LootItemCondition {
	public static final Map<Block, Item> CONVERSIONS = new ConcurrentHashMap<>();

	public static final MapCodec<GiantToolGroupingModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(c -> List.of(c.conditions))
	).apply(inst, GiantToolGroupingModifier::new));

	private final LootItemCondition[] conditions;

	public GiantToolGroupingModifier(List<LootItemCondition> conditions) {
		this.conditions = conditions.toArray(new LootItemCondition[0]);
	}

	@Override
	public boolean test(LootContext context) {
		for (LootItemCondition condition : this.conditions) {
			if (!condition.test(context)) {
				return false;
			}
		}
		return true;
	}

	public @NotNull ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (!test(context)) return generatedLoot;
		if (context.getParameter(LootContextParams.THIS_ENTITY) instanceof Player player) {
			if (!generatedLoot.isEmpty() && generatedLoot.getFirst().getItem() instanceof BlockItem block) {
				if (CONVERSIONS.containsKey(block.getBlock())) {
					var attachment = ((TFEntityExtensions) player).getData(() -> TFDataAttachments.GIANT_PICKAXE_MINING);
					int blockConversion = attachment.getGiantBlockConversion();
					attachment.setGiantBlockConversion(blockConversion - 1);
					if (blockConversion == 64)
						return ObjectArrayList.of(new ItemStack(CONVERSIONS.get(block.getBlock())));
					else return new ObjectArrayList<>();
				}
			}
		}
		return generatedLoot;
	}

	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}
}
