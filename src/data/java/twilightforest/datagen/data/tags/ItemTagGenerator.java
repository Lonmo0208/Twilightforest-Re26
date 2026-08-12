package twilightforest.datagen.data.tags;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import twilightforest.datagen.data.tags.compat.ModdedItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.tags.TFItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ModdedItemTagGenerator {

	public ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, future);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);

		// Define vanilla/convention item tags that Fabric's datagen doesn't generate
		this.valueLookupBuilder(ConventionalItemTags.DIAMOND_GEMS).add(Items.DIAMOND);
		this.valueLookupBuilder(ConventionalItemTags.IRON_INGOTS).add(Items.IRON_INGOT);
		this.valueLookupBuilder(ConventionalItemTags.GOLD_INGOTS).add(Items.GOLD_INGOT);
		this.valueLookupBuilder(ConventionalItemTags.NETHERITE_INGOTS).add(Items.NETHERITE_INGOT);
		this.valueLookupBuilder(ConventionalItemTags.COPPER_INGOTS).add(Items.COPPER_INGOT);
		this.valueLookupBuilder(ConventionalItemTags.GEMS).addTag(ConventionalItemTags.DIAMOND_GEMS);
		this.valueLookupBuilder(ConventionalItemTags.INGOTS).addTag(ConventionalItemTags.IRON_INGOTS)
			.addTag(ConventionalItemTags.GOLD_INGOTS).addTag(ConventionalItemTags.NETHERITE_INGOTS)
			.addTag(ConventionalItemTags.COPPER_INGOTS);
		this.valueLookupBuilder(ConventionalItemTags.RAW_MATERIALS).add(Items.RAW_IRON, Items.RAW_GOLD, Items.RAW_COPPER);
		this.valueLookupBuilder(ConventionalItemTags.CARROT_CROPS).add(Items.CARROT);
		this.valueLookupBuilder(ConventionalItemTags.POTATO_CROPS).add(Items.POTATO);
		this.valueLookupBuilder(ConventionalItemTags.BEETROOT_CROPS).add(Items.BEETROOT);
		this.valueLookupBuilder(ConventionalItemTags.WHEAT_CROPS).add(Items.WHEAT);
		this.valueLookupBuilder(ConventionalItemTags.SEEDS).add(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS);
		this.valueLookupBuilder(ConventionalItemTags.WOODEN_RODS).add(Items.STICK);
		this.valueLookupBuilder(ItemTags.FISHES).add(Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);

		// Log tags - defined directly because HolderLookup.Provider does not have tag data at datagen time
		this.valueLookupBuilder(TFItemTags.TWILIGHT_OAK_LOGS).add(
			TFBlocks.TWILIGHT_OAK_LOG.asItem(),
			TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.asItem(),
			TFBlocks.TWILIGHT_OAK_WOOD.asItem(),
			TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.asItem()
		);
		this.valueLookupBuilder(TFItemTags.CANOPY_LOGS).add(
			TFBlocks.CANOPY_LOG.asItem(),
			TFBlocks.STRIPPED_CANOPY_LOG.asItem(),
			TFBlocks.CANOPY_WOOD.asItem(),
			TFBlocks.STRIPPED_CANOPY_WOOD.asItem()
		);
		this.valueLookupBuilder(TFItemTags.MANGROVE_LOGS).add(
			TFBlocks.MANGROVE_LOG.asItem(),
			TFBlocks.STRIPPED_MANGROVE_LOG.asItem(),
			TFBlocks.MANGROVE_WOOD.asItem(),
			TFBlocks.STRIPPED_MANGROVE_WOOD.asItem()
		);
		this.valueLookupBuilder(TFItemTags.DARKWOOD_LOGS).add(
			TFBlocks.DARK_LOG.asItem(),
			TFBlocks.STRIPPED_DARK_LOG.asItem(),
			TFBlocks.DARK_WOOD.asItem(),
			TFBlocks.STRIPPED_DARK_WOOD.asItem()
		);
		this.valueLookupBuilder(TFItemTags.TIME_LOGS).add(
			TFBlocks.TIME_LOG.asItem(),
			TFBlocks.STRIPPED_TIME_LOG.asItem(),
			TFBlocks.TIME_WOOD.asItem(),
			TFBlocks.STRIPPED_TIME_WOOD.asItem()
		);
		this.valueLookupBuilder(TFItemTags.TRANSFORMATION_LOGS).add(
			TFBlocks.TRANSFORMATION_LOG.asItem(),
			TFBlocks.STRIPPED_TRANSFORMATION_LOG.asItem(),
			TFBlocks.TRANSFORMATION_WOOD.asItem(),
			TFBlocks.STRIPPED_TRANSFORMATION_WOOD.asItem()
		);
		this.valueLookupBuilder(TFItemTags.MINING_LOGS).add(
			TFBlocks.MINING_LOG.asItem(),
			TFBlocks.STRIPPED_MINING_LOG.asItem(),
			TFBlocks.MINING_WOOD.asItem(),
			TFBlocks.STRIPPED_MINING_WOOD.asItem()
		);
		this.valueLookupBuilder(TFItemTags.SORTING_LOGS).add(
			TFBlocks.SORTING_LOG.asItem(),
			TFBlocks.STRIPPED_SORTING_LOG.asItem(),
			TFBlocks.SORTING_WOOD.asItem(),
			TFBlocks.STRIPPED_SORTING_WOOD.asItem()
		);

		this.valueLookupBuilder(TFItemTags.TWILIGHT_LOGS)
			.addTag(TFItemTags.TWILIGHT_OAK_LOGS).addTag(TFItemTags.CANOPY_LOGS)
			.addTag(TFItemTags.MANGROVE_LOGS).addTag(TFItemTags.DARKWOOD_LOGS)
			.addTag(TFItemTags.TIME_LOGS).addTag(TFItemTags.TRANSFORMATION_LOGS)
			.addTag(TFItemTags.MINING_LOGS).addTag(TFItemTags.SORTING_LOGS);
		this.valueLookupBuilder(ItemTags.LOGS).addTag(TFItemTags.TWILIGHT_LOGS);
		this.valueLookupBuilder(ItemTags.LOGS_THAT_BURN)
			.addTag(TFItemTags.TWILIGHT_OAK_LOGS).addTag(TFItemTags.CANOPY_LOGS).addTag(TFItemTags.MANGROVE_LOGS)
			.addTag(TFItemTags.TIME_LOGS).addTag(TFItemTags.TRANSFORMATION_LOGS).addTag(TFItemTags.MINING_LOGS).addTag(TFItemTags.SORTING_LOGS);

		// Vanilla block to item tag copies - defined directly for consistency
		this.valueLookupBuilder(ItemTags.SAPLINGS)
			.add(TFBlocks.TWILIGHT_OAK_SAPLING.asItem())
			.add(TFBlocks.CANOPY_SAPLING.asItem())
			.add(TFBlocks.MANGROVE_SAPLING.asItem())
			.add(TFBlocks.DARKWOOD_SAPLING.asItem())
			.add(TFBlocks.TIME_SAPLING.asItem())
			.add(TFBlocks.TRANSFORMATION_SAPLING.asItem())
			.add(TFBlocks.MINING_SAPLING.asItem())
			.add(TFBlocks.SORTING_SAPLING.asItem())
			.add(TFBlocks.HOLLOW_OAK_SAPLING.asItem())
			.add(TFBlocks.RAINBOW_OAK_SAPLING.asItem());

		this.valueLookupBuilder(ItemTags.LEAVES)
			.add(TFBlocks.RAINBOW_OAK_LEAVES.asItem())
			.add(TFBlocks.TWILIGHT_OAK_LEAVES.asItem())
			.add(TFBlocks.CANOPY_LEAVES.asItem())
			.add(TFBlocks.MANGROVE_LEAVES.asItem())
			.add(TFBlocks.DARK_LEAVES.asItem())
			.add(TFBlocks.TIME_LEAVES.asItem())
			.add(TFBlocks.TRANSFORMATION_LEAVES.asItem())
			.add(TFBlocks.MINING_LEAVES.asItem())
			.add(TFBlocks.SORTING_LEAVES.asItem())
			.add(TFBlocks.THORN_LEAVES.asItem())
			.add(TFBlocks.BEANSTALK_LEAVES.asItem());

		this.valueLookupBuilder(ItemTags.PLANKS)
			.add(TFBlocks.TWILIGHT_OAK_PLANKS.asItem())
			.add(TFBlocks.CANOPY_PLANKS.asItem())
			.add(TFBlocks.MANGROVE_PLANKS.asItem())
			.add(TFBlocks.DARK_PLANKS.asItem())
			.add(TFBlocks.TIME_PLANKS.asItem())
			.add(TFBlocks.TRANSFORMATION_PLANKS.asItem())
			.add(TFBlocks.MINING_PLANKS.asItem())
			.add(TFBlocks.SORTING_PLANKS.asItem())
			.add(TFBlocks.TOWERWOOD.asItem())
			.add(TFBlocks.MOSSY_TOWERWOOD.asItem())
			.add(TFBlocks.CRACKED_TOWERWOOD.asItem())
			.add(TFBlocks.INFESTED_TOWERWOOD.asItem());

		this.valueLookupBuilder(ItemTags.WOODEN_FENCES)
			.add(TFBlocks.TWILIGHT_OAK_FENCE.asItem())
			.add(TFBlocks.CANOPY_FENCE.asItem())
			.add(TFBlocks.MANGROVE_FENCE.asItem())
			.add(TFBlocks.DARK_FENCE.asItem())
			.add(TFBlocks.TIME_FENCE.asItem())
			.add(TFBlocks.TRANSFORMATION_FENCE.asItem())
			.add(TFBlocks.MINING_FENCE.asItem())
			.add(TFBlocks.SORTING_FENCE.asItem());

		this.valueLookupBuilder(ItemTags.FENCE_GATES)
			.add(TFBlocks.TWILIGHT_OAK_GATE.asItem())
			.add(TFBlocks.CANOPY_GATE.asItem())
			.add(TFBlocks.MANGROVE_GATE.asItem())
			.add(TFBlocks.DARK_GATE.asItem())
			.add(TFBlocks.TIME_GATE.asItem())
			.add(TFBlocks.TRANSFORMATION_GATE.asItem())
			.add(TFBlocks.MINING_GATE.asItem())
			.add(TFBlocks.SORTING_GATE.asItem());

		this.valueLookupBuilder(ConventionalItemTags.WOODEN_FENCE_GATES)
			.add(TFBlocks.TWILIGHT_OAK_GATE.asItem())
			.add(TFBlocks.CANOPY_GATE.asItem())
			.add(TFBlocks.MANGROVE_GATE.asItem())
			.add(TFBlocks.DARK_GATE.asItem())
			.add(TFBlocks.TIME_GATE.asItem())
			.add(TFBlocks.TRANSFORMATION_GATE.asItem())
			.add(TFBlocks.MINING_GATE.asItem())
			.add(TFBlocks.SORTING_GATE.asItem());

		this.valueLookupBuilder(ItemTags.WOODEN_SLABS)
			.add(TFBlocks.TWILIGHT_OAK_SLAB.asItem())
			.add(TFBlocks.CANOPY_SLAB.asItem())
			.add(TFBlocks.MANGROVE_SLAB.asItem())
			.add(TFBlocks.DARK_SLAB.asItem())
			.add(TFBlocks.TIME_SLAB.asItem())
			.add(TFBlocks.TRANSFORMATION_SLAB.asItem())
			.add(TFBlocks.MINING_SLAB.asItem())
			.add(TFBlocks.SORTING_SLAB.asItem());

		this.valueLookupBuilder(ItemTags.SLABS).add(TFBlocks.AURORA_SLAB.asItem());

		this.valueLookupBuilder(ItemTags.WOODEN_STAIRS)
			.add(TFBlocks.TWILIGHT_OAK_STAIRS.asItem())
			.add(TFBlocks.CANOPY_STAIRS.asItem())
			.add(TFBlocks.MANGROVE_STAIRS.asItem())
			.add(TFBlocks.DARK_STAIRS.asItem())
			.add(TFBlocks.TIME_STAIRS.asItem())
			.add(TFBlocks.TRANSFORMATION_STAIRS.asItem())
			.add(TFBlocks.MINING_STAIRS.asItem())
			.add(TFBlocks.SORTING_STAIRS.asItem());

		this.valueLookupBuilder(ItemTags.STAIRS)
			.add(TFBlocks.CASTLE_BRICK_STAIRS.asItem())
			.add(TFBlocks.WORN_CASTLE_BRICK_STAIRS.asItem())
			.add(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.asItem())
			.add(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.asItem())
			.add(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.asItem())
			.add(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.asItem())
			.add(TFBlocks.NAGASTONE_STAIRS_LEFT.asItem())
			.add(TFBlocks.NAGASTONE_STAIRS_RIGHT.asItem())
			.add(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.asItem())
			.add(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.asItem())
			.add(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.asItem())
			.add(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.asItem());

		this.valueLookupBuilder(ItemTags.WOODEN_BUTTONS)
			.add(TFBlocks.TWILIGHT_OAK_BUTTON.asItem())
			.add(TFBlocks.CANOPY_BUTTON.asItem())
			.add(TFBlocks.MANGROVE_BUTTON.asItem())
			.add(TFBlocks.DARK_BUTTON.asItem())
			.add(TFBlocks.TIME_BUTTON.asItem())
			.add(TFBlocks.TRANSFORMATION_BUTTON.asItem())
			.add(TFBlocks.MINING_BUTTON.asItem())
			.add(TFBlocks.SORTING_BUTTON.asItem());

		this.valueLookupBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
			.add(TFBlocks.TWILIGHT_OAK_PLATE.asItem())
			.add(TFBlocks.CANOPY_PLATE.asItem())
			.add(TFBlocks.MANGROVE_PLATE.asItem())
			.add(TFBlocks.DARK_PLATE.asItem())
			.add(TFBlocks.TIME_PLATE.asItem())
			.add(TFBlocks.TRANSFORMATION_PLATE.asItem())
			.add(TFBlocks.MINING_PLATE.asItem())
			.add(TFBlocks.SORTING_PLATE.asItem());

		this.valueLookupBuilder(ItemTags.WOODEN_TRAPDOORS)
			.add(TFBlocks.TWILIGHT_OAK_TRAPDOOR.asItem())
			.add(TFBlocks.CANOPY_TRAPDOOR.asItem())
			.add(TFBlocks.MANGROVE_TRAPDOOR.asItem())
			.add(TFBlocks.DARK_TRAPDOOR.asItem())
			.add(TFBlocks.TIME_TRAPDOOR.asItem())
			.add(TFBlocks.TRANSFORMATION_TRAPDOOR.asItem())
			.add(TFBlocks.MINING_TRAPDOOR.asItem())
			.add(TFBlocks.SORTING_TRAPDOOR.asItem());

		this.valueLookupBuilder(ItemTags.WOODEN_DOORS)
			.add(TFBlocks.TWILIGHT_OAK_DOOR.asItem())
			.add(TFBlocks.CANOPY_DOOR.asItem())
			.add(TFBlocks.MANGROVE_DOOR.asItem())
			.add(TFBlocks.DARK_DOOR.asItem())
			.add(TFBlocks.TIME_DOOR.asItem())
			.add(TFBlocks.TRANSFORMATION_DOOR.asItem())
			.add(TFBlocks.MINING_DOOR.asItem())
			.add(TFBlocks.SORTING_DOOR.asItem());

		this.valueLookupBuilder(ItemTags.HANGING_SIGNS)
			.add(TFItems.TWILIGHT_OAK_HANGING_SIGN)
			.add(TFItems.CANOPY_HANGING_SIGN)
			.add(TFItems.MANGROVE_HANGING_SIGN)
			.add(TFItems.DARK_HANGING_SIGN)
			.add(TFItems.TIME_HANGING_SIGN)
			.add(TFItems.TRANSFORMATION_HANGING_SIGN)
			.add(TFItems.MINING_HANGING_SIGN)
			.add(TFItems.SORTING_HANGING_SIGN);

		this.valueLookupBuilder(ItemTags.SIGNS)
			.add(TFItems.TWILIGHT_OAK_SIGN)
			.add(TFItems.CANOPY_SIGN)
			.add(TFItems.MANGROVE_SIGN)
			.add(TFItems.DARK_SIGN)
			.add(TFItems.TIME_SIGN)
			.add(TFItems.TRANSFORMATION_SIGN)
			.add(TFItems.MINING_SIGN)
			.add(TFItems.SORTING_SIGN);

		this.valueLookupBuilder(ConventionalItemTags.WOODEN_CHESTS)
			.add(TFBlocks.TWILIGHT_OAK_CHEST.asItem())
			.add(TFBlocks.CANOPY_CHEST.asItem())
			.add(TFBlocks.MANGROVE_CHEST.asItem())
			.add(TFBlocks.DARK_CHEST.asItem())
			.add(TFBlocks.TIME_CHEST.asItem())
			.add(TFBlocks.TRANSFORMATION_CHEST.asItem())
			.add(TFBlocks.MINING_CHEST.asItem())
			.add(TFBlocks.SORTING_CHEST.asItem());

		this.valueLookupBuilder(TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.asItem());
		this.valueLookupBuilder(TFItemTags.STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.asItem());
		this.valueLookupBuilder(TFItemTags.STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.asItem());
		this.valueLookupBuilder(TFItemTags.STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.asItem());
		this.valueLookupBuilder(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.asItem());
		this.valueLookupBuilder(TFItemTags.STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.asItem());

		this.valueLookupBuilder(ConventionalItemTags.STORAGE_BLOCKS)
			.addTag(TFItemTags.STORAGE_BLOCKS_FIERY).addTag(TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR)
			.addTag(TFItemTags.STORAGE_BLOCKS_CARMINITE).addTag(TFItemTags.STORAGE_BLOCKS_IRONWOOD)
			.addTag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).addTag(TFItemTags.STORAGE_BLOCKS_STEELEAF);

		this.valueLookupBuilder(TFItemTags.TOWERWOOD)
			.add(TFBlocks.TOWERWOOD.asItem())
			.add(TFBlocks.MOSSY_TOWERWOOD.asItem())
			.add(TFBlocks.CRACKED_TOWERWOOD.asItem())
			.add(TFBlocks.INFESTED_TOWERWOOD.asItem());

		this.valueLookupBuilder(TFItemTags.BANISTERS)
			.add(TFBlocks.OAK_BANISTER.asItem())
			.add(TFBlocks.SPRUCE_BANISTER.asItem())
			.add(TFBlocks.BIRCH_BANISTER.asItem())
			.add(TFBlocks.JUNGLE_BANISTER.asItem())
			.add(TFBlocks.ACACIA_BANISTER.asItem())
			.add(TFBlocks.DARK_OAK_BANISTER.asItem())
			.add(TFBlocks.CRIMSON_BANISTER.asItem())
			.add(TFBlocks.WARPED_BANISTER.asItem())
			.add(TFBlocks.VANGROVE_BANISTER.asItem())
			.add(TFBlocks.BAMBOO_BANISTER.asItem())
			.add(TFBlocks.CHERRY_BANISTER.asItem())
			.add(TFBlocks.PALE_OAK_BANISTER.asItem())
			.add(TFBlocks.TWILIGHT_OAK_BANISTER.asItem())
			.add(TFBlocks.CANOPY_BANISTER.asItem())
			.add(TFBlocks.MANGROVE_BANISTER.asItem())
			.add(TFBlocks.DARK_BANISTER.asItem())
			.add(TFBlocks.TIME_BANISTER.asItem())
			.add(TFBlocks.TRANSFORMATION_BANISTER.asItem())
			.add(TFBlocks.MINING_BANISTER.asItem())
			.add(TFBlocks.SORTING_BANISTER.asItem());

		this.valueLookupBuilder(TFItemTags.PAPER).add(Items.PAPER);
		this.valueLookupBuilder(ConventionalItemTags.FEATHERS).add(TFItems.RAVEN_FEATHER);

		this.valueLookupBuilder(TFItemTags.FIERY_VIAL).add(TFItems.FIERY_BLOOD, TFItems.FIERY_TEARS);

		this.valueLookupBuilder(TFItemTags.ARCTIC_FUR).add(TFItems.ARCTIC_FUR);
		this.valueLookupBuilder(TFItemTags.CARMINITE_GEMS).add(TFItems.CARMINITE);
		this.valueLookupBuilder(TFItemTags.FIERY_INGOTS).add(TFItems.FIERY_INGOT);
		this.valueLookupBuilder(TFItemTags.IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT);
		this.valueLookupBuilder(TFItemTags.KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT);
		this.valueLookupBuilder(TFItemTags.STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT);
		this.valueLookupBuilder(TFItemTags.WROUGHT_IRON_INGOTS).add(TFItems.WROUGHT_IRON_BAR);

		this.valueLookupBuilder(ConventionalItemTags.GEMS).addTag(TFItemTags.CARMINITE_GEMS);

		this.valueLookupBuilder(ConventionalItemTags.INGOTS)
			.addTag(TFItemTags.IRONWOOD_INGOTS).addTag(TFItemTags.FIERY_INGOTS)
			.addTag(TFItemTags.KNIGHTMETAL_INGOTS).addTag(TFItemTags.STEELEAF_INGOTS);

		this.valueLookupBuilder(TFItemTags.RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD);
		this.valueLookupBuilder(TFItemTags.RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER);
		this.valueLookupBuilder(ConventionalItemTags.RAW_MATERIALS).addTag(TFItemTags.RAW_MATERIALS_IRONWOOD).addTag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL);

		this.valueLookupBuilder(TFItemTags.PORTAL_ACTIVATOR).addTag(ConventionalItemTags.DIAMOND_GEMS);

		this.valueLookupBuilder(ItemTags.BOATS).add(
			TFItems.TWILIGHT_OAK_BOAT, TFItems.CANOPY_BOAT,
			TFItems.MANGROVE_BOAT, TFItems.DARK_BOAT,
			TFItems.TIME_BOAT, TFItems.TRANSFORMATION_BOAT,
			TFItems.MINING_BOAT, TFItems.SORTING_BOAT
		);

		this.valueLookupBuilder(ItemTags.CHEST_BOATS).add(
			TFItems.TWILIGHT_OAK_CHEST_BOAT, TFItems.CANOPY_CHEST_BOAT,
			TFItems.MANGROVE_CHEST_BOAT, TFItems.DARK_CHEST_BOAT,
			TFItems.TIME_CHEST_BOAT, TFItems.TRANSFORMATION_CHEST_BOAT,
			TFItems.MINING_CHEST_BOAT, TFItems.SORTING_CHEST_BOAT
		);

		this.valueLookupBuilder(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
			TFItems.FIERY_HELMET,
			TFItems.FIERY_CHESTPLATE,
			TFItems.FIERY_LEGGINGS,
			TFItems.FIERY_BOOTS,
			TFItems.ARCTIC_HELMET,
			TFItems.ARCTIC_CHESTPLATE,
			TFItems.ARCTIC_LEGGINGS,
			TFItems.ARCTIC_BOOTS,
			TFItems.YETI_HELMET,
			TFItems.YETI_CHESTPLATE,
			TFItems.YETI_LEGGINGS,
			TFItems.YETI_BOOTS
		);

		this.valueLookupBuilder(TFItemTags.WIP).add(
			TFBlocks.AURORALIZED_GLASS.asItem(),
			TFItems.QUEST_RAM_BANNER_PATTERN,
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem(),
			TFItems.CUBE_TALISMAN,
			TFItems.CUBE_OF_ANNIHILATION,
			TFBlocks.CINDER_FURNACE.asItem(),
			TFBlocks.CINDER_LOG.asItem(),
			TFBlocks.CINDER_WOOD.asItem(),
			TFBlocks.SLIDER.asItem(),
			TFBlocks.BRAZIER.asItem(),
			TFBlocks.MAZE_SLIME_BLOCK.asItem()
		);

		this.valueLookupBuilder(TFItemTags.KOBOLD_PACIFICATION_BREADS).add(Items.BREAD);
		this.valueLookupBuilder(TFItemTags.BOAR_TEMPT_ITEMS).addTag(ConventionalItemTags.CARROT_CROPS).addTag(ConventionalItemTags.POTATO_CROPS).addTag(ConventionalItemTags.BEETROOT_CROPS);
		this.valueLookupBuilder(TFItemTags.DEER_TEMPT_ITEMS).addTag(ConventionalItemTags.WHEAT_CROPS).add(Items.APPLE);
		this.valueLookupBuilder(TFItemTags.DWARF_RABBIT_TEMPT_ITEMS).addTag(ConventionalItemTags.CARROT_CROPS).add(Items.GOLDEN_CARROT).add(Items.DANDELION);
		this.valueLookupBuilder(TFItemTags.PENGUIN_TEMPT_ITEMS).addTag(ItemTags.FISHES);
		this.valueLookupBuilder(TFItemTags.RAVEN_TEMPT_ITEMS).addTag(ConventionalItemTags.SEEDS);
		this.valueLookupBuilder(TFItemTags.SQUIRREL_TEMPT_ITEMS).addTag(ConventionalItemTags.SEEDS);
		this.valueLookupBuilder(TFItemTags.TINY_BIRD_TEMPT_ITEMS).addTag(ConventionalItemTags.SEEDS);

		this.valueLookupBuilder(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS).add(
			TFBlocks.INFESTED_TOWERWOOD.asItem(),
			TFBlocks.HOLLOW_OAK_SAPLING.asItem(),
			TFBlocks.TIME_SAPLING.asItem(),
			TFBlocks.TRANSFORMATION_SAPLING.asItem(),
			TFBlocks.MINING_SAPLING.asItem(),
			TFBlocks.SORTING_SAPLING.asItem(),
			TFItems.TRANSFORMATION_POWDER);

		this.valueLookupBuilder(TFItemTags.BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.asItem());
		this.valueLookupBuilder(TFItemTags.UNCRAFTING_IGNORES_COST).addTag(ConventionalItemTags.WOODEN_RODS);

		this.valueLookupBuilder(TFItemTags.KEPT_ON_DEATH).add(TFItems.TOWER_KEY, TFItems.PHANTOM_HELMET, TFItems.PHANTOM_CHESTPLATE);

		this.valueLookupBuilder(TFItemTags.SCEPTERS).add(TFItems.TWILIGHT_SCEPTER, TFItems.LIFEDRAIN_SCEPTER, TFItems.ZOMBIE_SCEPTER, TFItems.FORTIFICATION_SCEPTER);

		this.valueLookupBuilder(TFItemTags.IMMUNE_TO_THORNS).add(TFBlocks.THORN_LEAVES.asItem(), TFBlocks.THORN_ROSE.asItem());

		this.valueLookupBuilder(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE, TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_LIFE_2, TFItems.LAMP_OF_CINDERS);

		this.valueLookupBuilder(ItemTags.SKULLS).add(
			TFItems.ZOMBIE_SKULL_CANDLE,
			TFItems.SKELETON_SKULL_CANDLE,
			TFItems.WITHER_SKELETON_SKULL_CANDLE,
			TFItems.CREEPER_SKULL_CANDLE,
			TFItems.PLAYER_SKULL_CANDLE,
			TFItems.PIGLIN_SKULL_CANDLE);

		this.valueLookupBuilder(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(
			TFItems.ZOMBIE_SKULL_CANDLE,
			TFItems.SKELETON_SKULL_CANDLE,
			TFItems.WITHER_SKELETON_SKULL_CANDLE,
			TFItems.CREEPER_SKULL_CANDLE,
			TFItems.PLAYER_SKULL_CANDLE,
			TFItems.PIGLIN_SKULL_CANDLE);

		this.valueLookupBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(
			TFItems.IRONWOOD_HELMET,
			TFItems.STEELEAF_HELMET,
			TFItems.KNIGHTMETAL_HELMET,
			TFItems.PHANTOM_HELMET,
			TFItems.FIERY_HELMET,
			TFItems.ARCTIC_HELMET,
			TFItems.YETI_HELMET);

		this.valueLookupBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(
			TFItems.NAGA_CHESTPLATE,
			TFItems.IRONWOOD_CHESTPLATE,
			TFItems.STEELEAF_CHESTPLATE,
			TFItems.KNIGHTMETAL_CHESTPLATE,
			TFItems.PHANTOM_CHESTPLATE,
			TFItems.FIERY_CHESTPLATE,
			TFItems.ARCTIC_CHESTPLATE,
			TFItems.YETI_CHESTPLATE);

		this.valueLookupBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE).add(
			TFItems.NAGA_LEGGINGS,
			TFItems.IRONWOOD_LEGGINGS,
			TFItems.STEELEAF_LEGGINGS,
			TFItems.KNIGHTMETAL_LEGGINGS,
			TFItems.FIERY_LEGGINGS,
			TFItems.ARCTIC_LEGGINGS,
			TFItems.YETI_LEGGINGS);

		this.valueLookupBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(
			TFItems.IRONWOOD_BOOTS,
			TFItems.STEELEAF_BOOTS,
			TFItems.KNIGHTMETAL_BOOTS,
			TFItems.FIERY_BOOTS,
			TFItems.ARCTIC_BOOTS,
			TFItems.YETI_BOOTS);

		this.valueLookupBuilder(ItemTags.SWORDS).add(
			TFItems.IRONWOOD_SWORD,
			TFItems.STEELEAF_SWORD,
			TFItems.KNIGHTMETAL_SWORD,
			TFItems.FIERY_SWORD,
			TFItems.GIANT_SWORD,
			TFItems.ICE_SWORD,
			TFItems.GLASS_SWORD);

		this.valueLookupBuilder(ItemTags.PICKAXES).add(
			TFItems.IRONWOOD_PICKAXE,
			TFItems.STEELEAF_PICKAXE,
			TFItems.KNIGHTMETAL_PICKAXE,
			TFItems.MAZEBREAKER_PICKAXE,
			TFItems.FIERY_PICKAXE,
			TFItems.GIANT_PICKAXE);

		this.valueLookupBuilder(ItemTags.AXES).add(TFItems.IRONWOOD_AXE, TFItems.STEELEAF_AXE, TFItems.KNIGHTMETAL_AXE, TFItems.GOLDEN_MINOTAUR_AXE, TFItems.DIAMOND_MINOTAUR_AXE);
		this.valueLookupBuilder(ItemTags.SHOVELS).add(TFItems.IRONWOOD_SHOVEL, TFItems.STEELEAF_SHOVEL);
		this.valueLookupBuilder(ItemTags.HOES).add(TFItems.IRONWOOD_HOE, TFItems.STEELEAF_HOE);
		this.valueLookupBuilder(ConventionalItemTags.SHIELD_TOOLS).add(TFItems.KNIGHTMETAL_SHIELD);
		this.valueLookupBuilder(ConventionalItemTags.BOW_TOOLS).add(TFItems.TRIPLE_BOW, TFItems.SEEKER_BOW, TFItems.ICE_BOW, TFItems.ENDER_BOW);

		this.valueLookupBuilder(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
			TFItems.IRONWOOD_PICKAXE,
			TFItems.STEELEAF_PICKAXE,
			TFItems.KNIGHTMETAL_PICKAXE,
			TFItems.MAZEBREAKER_PICKAXE,
			TFItems.FIERY_PICKAXE,
			TFItems.GIANT_PICKAXE);

		this.valueLookupBuilder(ItemTags.SMALL_FLOWERS).add(TFBlocks.THORN_ROSE.asItem());

		this.valueLookupBuilder(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT, TFItems.STEELEAF_INGOT, TFItems.KNIGHTMETAL_INGOT, TFItems.NAGA_SCALE, TFItems.CARMINITE, TFItems.FIERY_INGOT);

		this.valueLookupBuilder(TFItemTags.REPAIRS_IRONWOOD_TOOLS).addTag(TFItemTags.IRONWOOD_INGOTS);
		this.valueLookupBuilder(TFItemTags.REPAIRS_STEELEAF_TOOLS).addTag(TFItemTags.STEELEAF_INGOTS);
		this.valueLookupBuilder(TFItemTags.REPAIRS_KNIGHTMETAL_TOOLS).addTag(TFItemTags.KNIGHTMETAL_INGOTS);
		this.valueLookupBuilder(TFItemTags.REPAIRS_FIERY_TOOLS).addTag(TFItemTags.FIERY_INGOTS);
		this.valueLookupBuilder(TFItemTags.REPAIRS_GIANT_TOOLS).add(TFBlocks.GIANT_COBBLESTONE.asItem());
		this.valueLookupBuilder(TFItemTags.REPAIRS_ICE_TOOLS).add(Blocks.ICE.asItem(), Blocks.PACKED_ICE.asItem(), Blocks.BLUE_ICE.asItem());

		this.valueLookupBuilder(ItemTags.MEAT).add(TFItems.RAW_VENISON, TFItems.COOKED_VENISON, TFItems.RAW_MEEF, TFItems.COOKED_MEEF, TFItems.MEEF_STROGANOFF, TFItems.EXPERIMENT_115, TFItems.HYDRA_CHOP);
		this.valueLookupBuilder(ItemTags.BEACON_PAYMENT_ITEMS).addTag(TFItemTags.IRONWOOD_INGOTS).addTag(TFItemTags.STEELEAF_INGOTS).addTag(TFItemTags.KNIGHTMETAL_INGOTS).addTag(TFItemTags.FIERY_INGOTS);

		this.valueLookupBuilder(ItemTags.TRIMMABLE_ARMOR);

		this.valueLookupBuilder(ItemTags.HEAD_ARMOR).add(
			TFItems.IRONWOOD_HELMET,
			TFItems.STEELEAF_HELMET,
			TFItems.KNIGHTMETAL_HELMET,
			TFItems.ARCTIC_HELMET,
			TFItems.YETI_HELMET,
			TFItems.FIERY_HELMET,
			TFItems.PHANTOM_HELMET);

		this.valueLookupBuilder(ItemTags.CHEST_ARMOR).add(
			TFItems.IRONWOOD_CHESTPLATE,
			TFItems.STEELEAF_CHESTPLATE,
			TFItems.KNIGHTMETAL_CHESTPLATE,
			TFItems.ARCTIC_CHESTPLATE,
			TFItems.YETI_CHESTPLATE,
			TFItems.FIERY_CHESTPLATE,
			TFItems.PHANTOM_CHESTPLATE,
			TFItems.NAGA_CHESTPLATE);

		this.valueLookupBuilder(ItemTags.LEG_ARMOR).add(
			TFItems.IRONWOOD_LEGGINGS,
			TFItems.STEELEAF_LEGGINGS,
			TFItems.KNIGHTMETAL_LEGGINGS,
			TFItems.ARCTIC_LEGGINGS,
			TFItems.YETI_LEGGINGS,
			TFItems.FIERY_LEGGINGS,
			TFItems.NAGA_LEGGINGS);

		this.valueLookupBuilder(ItemTags.FOOT_ARMOR).add(
			TFItems.IRONWOOD_BOOTS,
			TFItems.STEELEAF_BOOTS,
			TFItems.KNIGHTMETAL_BOOTS,
			TFItems.ARCTIC_BOOTS,
			TFItems.YETI_BOOTS,
			TFItems.FIERY_BOOTS);

		this.valueLookupBuilder(TFItemTags.BLOCK_AND_CHAIN_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN);
		this.valueLookupBuilder(ItemTags.BOW_ENCHANTABLE).add(TFItems.TRIPLE_BOW, TFItems.SEEKER_BOW, TFItems.ICE_BOW, TFItems.ENDER_BOW);
		this.valueLookupBuilder(ItemTags.MINING_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN);
		this.valueLookupBuilder(ItemTags.MINING_LOOT_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN);
		this.valueLookupBuilder(ItemTags.DURABILITY_ENCHANTABLE).add(
			TFItems.TRIPLE_BOW, TFItems.SEEKER_BOW, TFItems.ICE_BOW, TFItems.ENDER_BOW,
			TFItems.BLOCK_AND_CHAIN, TFItems.KNIGHTMETAL_SHIELD, TFItems.ORE_MAGNET,
			TFItems.PEACOCK_FEATHER_FAN, TFItems.CRUMBLE_HORN);
		this.valueLookupBuilder(ItemTags.FIRE_ASPECT_ENCHANTABLE);
		this.valueLookupBuilder(ItemTags.VANISHING_ENCHANTABLE);
		this.valueLookupBuilder(ItemTags.EQUIPPABLE_ENCHANTABLE);
		this.valueLookupBuilder(ItemTags.BREAKS_DECORATED_POTS).add(TFItems.BLOCK_AND_CHAIN);

		this.valueLookupBuilder(ConventionalItemTags.BERRY_FOODS).add(TFItems.TORCHBERRIES);
		this.valueLookupBuilder(ConventionalItemTags.RAW_MEAT_FOODS).add(TFItems.RAW_VENISON, TFItems.RAW_MEEF);
		this.valueLookupBuilder(ConventionalItemTags.COOKED_MEAT_FOODS).add(TFItems.COOKED_VENISON, TFItems.COOKED_MEEF, TFItems.HYDRA_CHOP);
		this.valueLookupBuilder(ConventionalItemTags.SOUP_FOODS).add(TFItems.MEEF_STROGANOFF);
		this.valueLookupBuilder(ConventionalItemTags.EDIBLE_WHEN_PLACED_FOODS).add(TFItems.EXPERIMENT_115);
		this.valueLookupBuilder(ConventionalItemTags.ROPES).add(TFItems.ROPE);
		this.valueLookupBuilder(ConventionalItemTags.MUSHROOMS).add(TFBlocks.MUSHGLOOM.asItem());
		this.valueLookupBuilder(ConventionalItemTags.MUSIC_DISCS).add(
			TFItems.MUSIC_DISC_RADIANCE, TFItems.MUSIC_DISC_STEPS, TFItems.MUSIC_DISC_SUPERSTITIOUS,
			TFItems.MUSIC_DISC_HOME, TFItems.MUSIC_DISC_WAYFARER, TFItems.MUSIC_DISC_FINDINGS,
			TFItems.MUSIC_DISC_MAKER, TFItems.MUSIC_DISC_THREAD, TFItems.MUSIC_DISC_MOTION
		);

		this.valueLookupBuilder(ItemTags.LOOM_PATTERNS).add(
			TFItems.NAGA_BANNER_PATTERN,
			TFItems.LICH_BANNER_PATTERN,
			TFItems.MINOSHROOM_BANNER_PATTERN,
			TFItems.HYDRA_BANNER_PATTERN,
			TFItems.KNIGHT_PHANTOM_BANNER_PATTERN,
			TFItems.UR_GHAST_BANNER_PATTERN,
			TFItems.ALPHA_YETI_BANNER_PATTERN,
			TFItems.SNOW_QUEEN_BANNER_PATTERN,
			TFItems.QUEST_RAM_BANNER_PATTERN
		);
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}
