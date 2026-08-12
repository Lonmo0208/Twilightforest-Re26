package twilightforest.entity.passive.quest.ram;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.loot.TFLootTables;

import java.util.Map;

public record QuestingRamContext(Map<DyeColor, Ingredient> questItems, ResourceKey<LootTable> lootTable) {

	public static final QuestingRamContext FALLBACK = new QuestingRamContext(ImmutableMap.<DyeColor, Ingredient>builder()
		.put(DyeColor.WHITE, Ingredient.of(Items.WOOL.pick(DyeColor.WHITE)))
		.put(DyeColor.LIGHT_GRAY, Ingredient.of(Items.WOOL.pick(DyeColor.LIGHT_GRAY)))
		.put(DyeColor.GRAY, Ingredient.of(Items.WOOL.pick(DyeColor.GRAY)))
		.put(DyeColor.BLACK, Ingredient.of(Items.WOOL.pick(DyeColor.BLACK)))
		.put(DyeColor.RED, Ingredient.of(Items.WOOL.pick(DyeColor.RED)))
		.put(DyeColor.ORANGE, Ingredient.of(Items.WOOL.pick(DyeColor.ORANGE)))
		.put(DyeColor.YELLOW, Ingredient.of(Items.WOOL.pick(DyeColor.YELLOW)))
		.put(DyeColor.GREEN, Ingredient.of(Items.WOOL.pick(DyeColor.GREEN)))
		.put(DyeColor.LIME, Ingredient.of(Items.WOOL.pick(DyeColor.LIME)))
		.put(DyeColor.BLUE, Ingredient.of(Items.WOOL.pick(DyeColor.BLUE)))
		.put(DyeColor.CYAN, Ingredient.of(Items.WOOL.pick(DyeColor.CYAN)))
		.put(DyeColor.LIGHT_BLUE, Ingredient.of(Items.WOOL.pick(DyeColor.LIGHT_BLUE)))
		.put(DyeColor.PURPLE, Ingredient.of(Items.WOOL.pick(DyeColor.PURPLE)))
		.put(DyeColor.MAGENTA, Ingredient.of(Items.WOOL.pick(DyeColor.MAGENTA)))
		.put(DyeColor.PINK, Ingredient.of(Items.WOOL.pick(DyeColor.PINK)))
		.put(DyeColor.BROWN, Ingredient.of(Items.WOOL.pick(DyeColor.BROWN))).build(),
		TFLootTables.QUESTING_RAM_REWARDS);

	public static final Codec<QuestingRamContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.unboundedMap(DyeColor.CODEC, Ingredient.CODEC).validate(QuestingRamContext::validate).fieldOf("items").forGetter(QuestingRamContext::questItems), //FIXME: NONEMPTY_CODEC does not exist
		ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("reward").forGetter(QuestingRamContext::lootTable)
	).apply(instance, QuestingRamContext::new));

	private static DataResult<Map<DyeColor, Ingredient>> validate(Map<DyeColor, Ingredient> map) {
		int colorFlags = 0;
		for (var color : map.keySet()) {
			colorFlags |= (1 << color.getId());
		}
		if (Integer.bitCount(colorFlags) == 16) {
			return DataResult.success(map);
		}
		return DataResult.error(() -> "Questing Ram quest must contain all 16 dye colors");
	}
}
