package twilightforest.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootTable.class)
public interface LootTableMixin {

	@Invoker("shuffleAndSplitItems")
	void invokeShuffleAndSplitItems(ObjectArrayList<ItemStack> items, int count, RandomSource random);
}