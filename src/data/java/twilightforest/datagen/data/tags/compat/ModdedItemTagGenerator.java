package twilightforest.datagen.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.tags.TFItemTags;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

public class ModdedItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {

	public ModdedItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, future);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.valueLookupBuilder(TFItemTags.AC_FERNS).add(TFBlocks.FIDDLEHEAD.asItem());
		this.valueLookupBuilder(TFItemTags.AC_FERROMAGNETIC_ITEMS)
			.addTag(TFItemTags.STORAGE_BLOCKS_IRONWOOD)
			.addTag(TFItemTags.STORAGE_BLOCKS_STEELEAF)
			.addTag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL)
			.add(TFBlocks.CANDELABRA.asItem(), TFBlocks.WROUGHT_IRON_FENCE.asItem())
			.add(TFItems.RAW_IRONWOOD, TFItems.IRONWOOD_INGOT, TFItems.STEELEAF_INGOT,
				TFItems.ARMOR_SHARD, TFItems.ARMOR_SHARD_CLUSTER, TFItems.KNIGHTMETAL_INGOT, TFItems.KNIGHTMETAL_RING,
				TFItems.FIERY_INGOT, TFItems.CHARM_OF_KEEPING_2, TFItems.ORE_MAGNET,
				TFItems.IRONWOOD_HELMET, TFItems.IRONWOOD_CHESTPLATE, TFItems.IRONWOOD_LEGGINGS, TFItems.IRONWOOD_BOOTS,
				TFItems.STEELEAF_HELMET, TFItems.STEELEAF_CHESTPLATE, TFItems.STEELEAF_LEGGINGS, TFItems.STEELEAF_BOOTS,
				TFItems.KNIGHTMETAL_HELMET, TFItems.KNIGHTMETAL_CHESTPLATE, TFItems.KNIGHTMETAL_LEGGINGS, TFItems.KNIGHTMETAL_BOOTS,
				TFItems.FIERY_HELMET, TFItems.FIERY_CHESTPLATE, TFItems.FIERY_LEGGINGS, TFItems.FIERY_BOOTS,
				TFItems.IRONWOOD_SWORD, TFItems.IRONWOOD_PICKAXE, TFItems.IRONWOOD_AXE, TFItems.IRONWOOD_SHOVEL, TFItems.IRONWOOD_HOE,
				TFItems.STEELEAF_SWORD, TFItems.STEELEAF_PICKAXE, TFItems.STEELEAF_AXE, TFItems.STEELEAF_SHOVEL, TFItems.STEELEAF_HOE,
				TFItems.KNIGHTMETAL_SWORD, TFItems.KNIGHTMETAL_PICKAXE, TFItems.KNIGHTMETAL_AXE, TFItems.BLOCK_AND_CHAIN, TFItems.KNIGHTMETAL_SHIELD,
				TFItems.FIERY_SWORD, TFItems.FIERY_PICKAXE, TFItems.MAZEBREAKER_PICKAXE);

		this.valueLookupBuilder(TFItemTags.AC_RAW_MEATS).add(TFItems.RAW_VENISON, TFItems.RAW_MEEF);

		this.valueLookupBuilder(TFItemTags.CURIOS_CHARM).add(
			TFItems.CHARM_OF_LIFE_1, TFItems.CHARM_OF_LIFE_2,
			TFItems.CHARM_OF_KEEPING_1, TFItems.CHARM_OF_KEEPING_2, TFItems.CHARM_OF_KEEPING_3
		);

		this.valueLookupBuilder(TFItemTags.CURIOS_HEAD).add(
			TFItems.NAGA_TROPHY,
			TFItems.LICH_TROPHY,
			TFItems.MINOSHROOM_TROPHY,
			TFItems.HYDRA_TROPHY,
			TFItems.KNIGHT_PHANTOM_TROPHY,
			TFItems.UR_GHAST_TROPHY,
			TFItems.ALPHA_YETI_TROPHY,
			TFItems.SNOW_QUEEN_TROPHY,
			TFItems.QUEST_RAM_TROPHY,
			TFBlocks.CICADA.asItem(),
			TFBlocks.FIREFLY.asItem(),
			TFBlocks.MOONWORM.asItem(),
			TFItems.CREEPER_SKULL_CANDLE,
			TFItems.PIGLIN_SKULL_CANDLE,
			TFItems.PLAYER_SKULL_CANDLE,
			TFItems.SKELETON_SKULL_CANDLE,
			TFItems.WITHER_SKELETON_SKULL_CANDLE,
			TFItems.ZOMBIE_SKULL_CANDLE);

		this.valueLookupBuilder(TFItemTags.CA_PLANT_FOODS).add(TFItems.TORCHBERRIES);

		this.valueLookupBuilder(TFItemTags.CA_PLANTS).add(TFItems.LIVEROOT, TFItems.MAGIC_BEANS,
			TFBlocks.HUGE_WATER_LILY.asItem(), TFBlocks.HUGE_LILY_PAD.asItem(),
			TFBlocks.TROLLVIDR.asItem(), TFBlocks.UNRIPE_TROLLBER.asItem(),
			TFBlocks.TROLLBER.asItem(), TFBlocks.HUGE_STALK.asItem(),
			TFBlocks.THORN_ROSE.asItem(), TFBlocks.MAYAPPLE.asItem(),
			TFBlocks.CLOVER_PATCH.asItem(), TFBlocks.FIDDLEHEAD.asItem(),
			TFBlocks.MUSHGLOOM.asItem(), TFBlocks.TORCHBERRY_PLANT.asItem(),
			TFBlocks.ROOT_STRAND.asItem(), TFBlocks.FALLEN_LEAVES.asItem(),
			TFBlocks.HEDGE.asItem(), TFBlocks.ROOT_BLOCK.asItem(), TFBlocks.LIVEROOT_BLOCK.asItem());

		this.valueLookupBuilder(TFItemTags.FD_CABBAGE_ROLL_INGREDIENTS).add(TFItems.RAW_VENISON, TFItems.RAW_MEEF);

		this.valueLookupBuilder(TFItemTags.RANDOMIUM_BLACKLIST).addTag(TFItemTags.WIP).add(TFItems.GLASS_SWORD,
			TFBlocks.TIME_LOG_CORE.asItem(), TFBlocks.TRANSFORMATION_LOG_CORE.asItem(),
			TFBlocks.MINING_LOG_CORE.asItem(), TFBlocks.SORTING_LOG_CORE.asItem(),
			TFBlocks.ANTIBUILDER.asItem(), TFBlocks.STRONGHOLD_SHIELD.asItem(),
			TFBlocks.LOCKED_VANISHING_BLOCK.asItem(), TFBlocks.BROWN_THORNS.asItem(),
			TFBlocks.GREEN_THORNS.asItem(), TFBlocks.BURNT_THORNS.asItem(),
			TFBlocks.PINK_FORCE_FIELD.asItem(), TFBlocks.ORANGE_FORCE_FIELD.asItem(),
			TFBlocks.GREEN_FORCE_FIELD.asItem(), TFBlocks.BLUE_FORCE_FIELD.asItem(),
			TFBlocks.VIOLET_FORCE_FIELD.asItem(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem(),
			TFBlocks.NAGA_BOSS_SPAWNER.asItem(), TFBlocks.LICH_BOSS_SPAWNER.asItem(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.asItem(), TFBlocks.HYDRA_BOSS_SPAWNER.asItem(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.asItem(), TFBlocks.UR_GHAST_BOSS_SPAWNER.asItem(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.asItem(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.asItem());
	}
}