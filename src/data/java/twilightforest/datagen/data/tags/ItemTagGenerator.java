package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import twilightforest.datagen.data.tags.compat.ModdedItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.tags.TFItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ModdedItemTagGenerator {

	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, future);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);

		// Log tags - defined directly because HolderLookup.Provider does not have tag data at datagen time
		this.tag(TFItemTags.TWILIGHT_OAK_LOGS).add(
			TFBlocks.TWILIGHT_OAK_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.TWILIGHT_OAK_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.asItem().builtInRegistryHolder().key()
		);
		this.tag(TFItemTags.CANOPY_LOGS).add(
			TFBlocks.CANOPY_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_CANOPY_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.CANOPY_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_CANOPY_WOOD.asItem().builtInRegistryHolder().key()
		);
		this.tag(TFItemTags.MANGROVE_LOGS).add(
			TFBlocks.MANGROVE_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_MANGROVE_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.MANGROVE_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_MANGROVE_WOOD.asItem().builtInRegistryHolder().key()
		);
		this.tag(TFItemTags.DARKWOOD_LOGS).add(
			TFBlocks.DARK_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_DARK_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.DARK_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_DARK_WOOD.asItem().builtInRegistryHolder().key()
		);
		this.tag(TFItemTags.TIME_LOGS).add(
			TFBlocks.TIME_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_TIME_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.TIME_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_TIME_WOOD.asItem().builtInRegistryHolder().key()
		);
		this.tag(TFItemTags.TRANSFORMATION_LOGS).add(
			TFBlocks.TRANSFORMATION_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_TRANSFORMATION_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.TRANSFORMATION_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_TRANSFORMATION_WOOD.asItem().builtInRegistryHolder().key()
		);
		this.tag(TFItemTags.MINING_LOGS).add(
			TFBlocks.MINING_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_MINING_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.MINING_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_MINING_WOOD.asItem().builtInRegistryHolder().key()
		);
		this.tag(TFItemTags.SORTING_LOGS).add(
			TFBlocks.SORTING_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_SORTING_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.SORTING_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.STRIPPED_SORTING_WOOD.asItem().builtInRegistryHolder().key()
		);

		this.tag(TFItemTags.TWILIGHT_LOGS)
			.addTag(TFItemTags.TWILIGHT_OAK_LOGS).addTag(TFItemTags.CANOPY_LOGS)
			.addTag(TFItemTags.MANGROVE_LOGS).addTag(TFItemTags.DARKWOOD_LOGS)
			.addTag(TFItemTags.TIME_LOGS).addTag(TFItemTags.TRANSFORMATION_LOGS)
			.addTag(TFItemTags.MINING_LOGS).addTag(TFItemTags.SORTING_LOGS);
		this.tag(ItemTags.LOGS).addTag(TFItemTags.TWILIGHT_LOGS);
		this.tag(ItemTags.LOGS_THAT_BURN)
			.addTag(TFItemTags.TWILIGHT_OAK_LOGS).addTag(TFItemTags.CANOPY_LOGS).addTag(TFItemTags.MANGROVE_LOGS)
			.addTag(TFItemTags.TIME_LOGS).addTag(TFItemTags.TRANSFORMATION_LOGS).addTag(TFItemTags.MINING_LOGS).addTag(TFItemTags.SORTING_LOGS);

		// Vanilla block to item tag copies - defined directly for consistency
		this.tag(ItemTags.SAPLINGS)
			.add(TFBlocks.TWILIGHT_OAK_SAPLING.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_SAPLING.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_SAPLING.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARKWOOD_SAPLING.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_SAPLING.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_SAPLING.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_SAPLING.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_SAPLING.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.HOLLOW_OAK_SAPLING.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.RAINBOW_OAK_SAPLING.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.LEAVES)
			.add(TFBlocks.RAINBOW_OAK_LEAVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TWILIGHT_OAK_LEAVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_LEAVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_LEAVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_LEAVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_LEAVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_LEAVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_LEAVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_LEAVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.THORN_LEAVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.BEANSTALK_LEAVES.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.PLANKS)
			.add(TFBlocks.TWILIGHT_OAK_PLANKS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_PLANKS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_PLANKS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_PLANKS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_PLANKS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_PLANKS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_PLANKS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_PLANKS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TOWERWOOD.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MOSSY_TOWERWOOD.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CRACKED_TOWERWOOD.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.INFESTED_TOWERWOOD.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.WOODEN_FENCES)
			.add(TFBlocks.TWILIGHT_OAK_FENCE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_FENCE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_FENCE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_FENCE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_FENCE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_FENCE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_FENCE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_FENCE.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.FENCE_GATES)
			.add(TFBlocks.TWILIGHT_OAK_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_GATE.asItem().builtInRegistryHolder().key());

		this.tag(Tags.Items.FENCE_GATES_WOODEN)
			.add(TFBlocks.TWILIGHT_OAK_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_GATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_GATE.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.WOODEN_SLABS)
			.add(TFBlocks.TWILIGHT_OAK_SLAB.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_SLAB.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_SLAB.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_SLAB.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_SLAB.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_SLAB.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_SLAB.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_SLAB.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.WOODEN_STAIRS)
			.add(TFBlocks.TWILIGHT_OAK_STAIRS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_STAIRS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_STAIRS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_STAIRS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_STAIRS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_STAIRS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_STAIRS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_STAIRS.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.WOODEN_BUTTONS)
			.add(TFBlocks.TWILIGHT_OAK_BUTTON.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_BUTTON.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_BUTTON.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_BUTTON.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_BUTTON.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_BUTTON.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_BUTTON.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_BUTTON.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.WOODEN_PRESSURE_PLATES)
			.add(TFBlocks.TWILIGHT_OAK_PLATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_PLATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_PLATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_PLATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_PLATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_PLATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_PLATE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_PLATE.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.WOODEN_TRAPDOORS)
			.add(TFBlocks.TWILIGHT_OAK_TRAPDOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_TRAPDOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_TRAPDOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_TRAPDOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_TRAPDOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_TRAPDOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_TRAPDOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_TRAPDOOR.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.WOODEN_DOORS)
			.add(TFBlocks.TWILIGHT_OAK_DOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_DOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_DOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_DOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_DOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_DOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_DOOR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_DOOR.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.HANGING_SIGNS)
			.add(TFItems.TWILIGHT_OAK_HANGING_SIGN.getKey())
			.add(TFItems.CANOPY_HANGING_SIGN.getKey())
			.add(TFItems.MANGROVE_HANGING_SIGN.getKey())
			.add(TFItems.DARK_HANGING_SIGN.getKey())
			.add(TFItems.TIME_HANGING_SIGN.getKey())
			.add(TFItems.TRANSFORMATION_HANGING_SIGN.getKey())
			.add(TFItems.MINING_HANGING_SIGN.getKey())
			.add(TFItems.SORTING_HANGING_SIGN.getKey());

		this.tag(ItemTags.SIGNS)
			.add(TFItems.TWILIGHT_OAK_SIGN.getKey())
			.add(TFItems.CANOPY_SIGN.getKey())
			.add(TFItems.MANGROVE_SIGN.getKey())
			.add(TFItems.DARK_SIGN.getKey())
			.add(TFItems.TIME_SIGN.getKey())
			.add(TFItems.TRANSFORMATION_SIGN.getKey())
			.add(TFItems.MINING_SIGN.getKey())
			.add(TFItems.SORTING_SIGN.getKey());

		this.tag(Tags.Items.CHESTS_WOODEN)
			.add(TFBlocks.TWILIGHT_OAK_CHEST.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_CHEST.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_CHEST.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_CHEST.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_CHEST.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_CHEST.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_CHEST.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_CHEST.asItem().builtInRegistryHolder().key());

		this.tag(TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.asItem().builtInRegistryHolder().key());

		this.tag(Tags.Items.STORAGE_BLOCKS)
			.addTag(TFItemTags.STORAGE_BLOCKS_FIERY).addTag(TFItemTags.STORAGE_BLOCKS_ARCTIC_FUR)
			.addTag(TFItemTags.STORAGE_BLOCKS_CARMINITE).addTag(TFItemTags.STORAGE_BLOCKS_IRONWOOD)
			.addTag(TFItemTags.STORAGE_BLOCKS_KNIGHTMETAL).addTag(TFItemTags.STORAGE_BLOCKS_STEELEAF);

		this.tag(TFItemTags.TOWERWOOD)
			.add(TFBlocks.TOWERWOOD.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MOSSY_TOWERWOOD.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CRACKED_TOWERWOOD.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.INFESTED_TOWERWOOD.asItem().builtInRegistryHolder().key());

		this.tag(TFItemTags.BANISTERS)
			.add(TFBlocks.OAK_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SPRUCE_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.BIRCH_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.JUNGLE_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.ACACIA_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_OAK_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CRIMSON_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.WARPED_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.VANGROVE_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.BAMBOO_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CHERRY_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.PALE_OAK_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TWILIGHT_OAK_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CANOPY_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MANGROVE_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DARK_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TIME_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TRANSFORMATION_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MINING_BANISTER.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SORTING_BANISTER.asItem().builtInRegistryHolder().key());

		this.tag(TFItemTags.PAPER).add(Items.PAPER.builtInRegistryHolder().key());
		this.tag(Tags.Items.FEATHERS).add(TFItems.RAVEN_FEATHER.getKey());

		this.tag(TFItemTags.FIERY_VIAL).add(TFItems.FIERY_BLOOD.getKey(), TFItems.FIERY_TEARS.getKey());

		this.tag(TFItemTags.ARCTIC_FUR).add(TFItems.ARCTIC_FUR.getKey());
		this.tag(TFItemTags.CARMINITE_GEMS).add(TFItems.CARMINITE.getKey());
		this.tag(TFItemTags.FIERY_INGOTS).add(TFItems.FIERY_INGOT.getKey());
		this.tag(TFItemTags.IRONWOOD_INGOTS).add(TFItems.IRONWOOD_INGOT.getKey());
		this.tag(TFItemTags.KNIGHTMETAL_INGOTS).add(TFItems.KNIGHTMETAL_INGOT.getKey());
		this.tag(TFItemTags.STEELEAF_INGOTS).add(TFItems.STEELEAF_INGOT.getKey());
		this.tag(TFItemTags.WROUGHT_IRON_INGOTS).add(TFItems.WROUGHT_IRON_BAR.getKey());

		this.tag(Tags.Items.GEMS).addTag(TFItemTags.CARMINITE_GEMS);

		this.tag(Tags.Items.INGOTS)
			.addTag(TFItemTags.IRONWOOD_INGOTS).addTag(TFItemTags.FIERY_INGOTS)
			.addTag(TFItemTags.KNIGHTMETAL_INGOTS).addTag(TFItemTags.STEELEAF_INGOTS);

		this.tag(TFItemTags.RAW_MATERIALS_IRONWOOD).add(TFItems.RAW_IRONWOOD.getKey());
		this.tag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL).add(TFItems.ARMOR_SHARD_CLUSTER.getKey());
		this.tag(Tags.Items.RAW_MATERIALS).addTag(TFItemTags.RAW_MATERIALS_IRONWOOD).addTag(TFItemTags.RAW_MATERIALS_KNIGHTMETAL);

		this.tag(TFItemTags.PORTAL_ACTIVATOR).addTag(Tags.Items.GEMS_DIAMOND);

		this.tag(ItemTags.BOATS).add(
			TFItems.TWILIGHT_OAK_BOAT.getKey(), TFItems.CANOPY_BOAT.getKey(),
			TFItems.MANGROVE_BOAT.getKey(), TFItems.DARK_BOAT.getKey(),
			TFItems.TIME_BOAT.getKey(), TFItems.TRANSFORMATION_BOAT.getKey(),
			TFItems.MINING_BOAT.getKey(), TFItems.SORTING_BOAT.getKey()
		);

		this.tag(ItemTags.CHEST_BOATS).add(
			TFItems.TWILIGHT_OAK_CHEST_BOAT.getKey(), TFItems.CANOPY_CHEST_BOAT.getKey(),
			TFItems.MANGROVE_CHEST_BOAT.getKey(), TFItems.DARK_CHEST_BOAT.getKey(),
			TFItems.TIME_CHEST_BOAT.getKey(), TFItems.TRANSFORMATION_CHEST_BOAT.getKey(),
			TFItems.MINING_CHEST_BOAT.getKey(), TFItems.SORTING_CHEST_BOAT.getKey()
		);

		this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
			TFItems.FIERY_HELMET.getKey(),
			TFItems.FIERY_CHESTPLATE.getKey(),
			TFItems.FIERY_LEGGINGS.getKey(),
			TFItems.FIERY_BOOTS.getKey(),
			TFItems.ARCTIC_HELMET.getKey(),
			TFItems.ARCTIC_CHESTPLATE.getKey(),
			TFItems.ARCTIC_LEGGINGS.getKey(),
			TFItems.ARCTIC_BOOTS.getKey(),
			TFItems.YETI_HELMET.getKey(),
			TFItems.YETI_CHESTPLATE.getKey(),
			TFItems.YETI_LEGGINGS.getKey(),
			TFItems.YETI_BOOTS.getKey()
		);

		this.tag(TFItemTags.WIP).add(
			TFBlocks.AURORALIZED_GLASS.asItem().builtInRegistryHolder().key(),
			TFItems.QUEST_RAM_BANNER_PATTERN.getKey(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem().builtInRegistryHolder().key(),
			TFItems.CUBE_TALISMAN.getKey(),
			TFItems.CUBE_OF_ANNIHILATION.getKey(),
			TFBlocks.CINDER_FURNACE.asItem().builtInRegistryHolder().key(),
			TFBlocks.CINDER_LOG.asItem().builtInRegistryHolder().key(),
			TFBlocks.CINDER_WOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.SLIDER.asItem().builtInRegistryHolder().key(),
			TFBlocks.BRAZIER.asItem().builtInRegistryHolder().key(),
			TFBlocks.MAZE_SLIME_BLOCK.asItem().builtInRegistryHolder().key()
		);

		this.tag(TFItemTags.KOBOLD_PACIFICATION_BREADS).add(Items.BREAD.builtInRegistryHolder().key());
		this.tag(TFItemTags.BOAR_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).addTag(Tags.Items.CROPS_POTATO).addTag(Tags.Items.CROPS_BEETROOT);
		this.tag(TFItemTags.DEER_TEMPT_ITEMS).addTag(Tags.Items.CROPS_WHEAT).add(Items.APPLE.builtInRegistryHolder().key());
		this.tag(TFItemTags.DWARF_RABBIT_TEMPT_ITEMS).addTag(Tags.Items.CROPS_CARROT).add(Items.GOLDEN_CARROT.builtInRegistryHolder().key()).add(Items.DANDELION.builtInRegistryHolder().key());
		this.tag(TFItemTags.PENGUIN_TEMPT_ITEMS).addTag(ItemTags.FISHES);
		this.tag(TFItemTags.RAVEN_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		this.tag(TFItemTags.SQUIRREL_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);
		this.tag(TFItemTags.TINY_BIRD_TEMPT_ITEMS).addTag(Tags.Items.SEEDS);

		this.tag(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS).add(
			TFBlocks.INFESTED_TOWERWOOD.asItem().builtInRegistryHolder().key(),
			TFBlocks.HOLLOW_OAK_SAPLING.asItem().builtInRegistryHolder().key(),
			TFBlocks.TIME_SAPLING.asItem().builtInRegistryHolder().key(),
			TFBlocks.TRANSFORMATION_SAPLING.asItem().builtInRegistryHolder().key(),
			TFBlocks.MINING_SAPLING.asItem().builtInRegistryHolder().key(),
			TFBlocks.SORTING_SAPLING.asItem().builtInRegistryHolder().key(),
			TFItems.TRANSFORMATION_POWDER.getKey());

		this.tag(TFItemTags.BANNED_UNCRAFTABLES).add(TFBlocks.GIANT_LOG.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.UNCRAFTING_IGNORES_COST).addTag(Tags.Items.RODS_WOODEN);

		this.tag(TFItemTags.KEPT_ON_DEATH).add(TFItems.TOWER_KEY.getKey(), TFItems.PHANTOM_HELMET.getKey(), TFItems.PHANTOM_CHESTPLATE.getKey());

		this.tag(TFItemTags.SCEPTERS).add(TFItems.TWILIGHT_SCEPTER.getKey(), TFItems.LIFEDRAIN_SCEPTER.getKey(), TFItems.ZOMBIE_SCEPTER.getKey(), TFItems.FORTIFICATION_SCEPTER.getKey());

		this.tag(TFItemTags.IMMUNE_TO_THORNS).add(TFBlocks.THORN_LEAVES.asItem().builtInRegistryHolder().key(), TFBlocks.THORN_ROSE.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.PIGLIN_LOVED).add(TFItems.GOLDEN_MINOTAUR_AXE.getKey(), TFItems.CHARM_OF_KEEPING_3.getKey(), TFItems.CHARM_OF_LIFE_2.getKey(), TFItems.LAMP_OF_CINDERS.getKey());

		this.tag(ItemTags.SKULLS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.getKey(),
			TFItems.SKELETON_SKULL_CANDLE.getKey(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.getKey(),
			TFItems.CREEPER_SKULL_CANDLE.getKey(),
			TFItems.PLAYER_SKULL_CANDLE.getKey(),
			TFItems.PIGLIN_SKULL_CANDLE.getKey());

		this.tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(
			TFItems.ZOMBIE_SKULL_CANDLE.getKey(),
			TFItems.SKELETON_SKULL_CANDLE.getKey(),
			TFItems.WITHER_SKELETON_SKULL_CANDLE.getKey(),
			TFItems.CREEPER_SKULL_CANDLE.getKey(),
			TFItems.PLAYER_SKULL_CANDLE.getKey(),
			TFItems.PIGLIN_SKULL_CANDLE.getKey());

		this.tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(
			TFItems.IRONWOOD_HELMET.getKey(),
			TFItems.STEELEAF_HELMET.getKey(),
			TFItems.KNIGHTMETAL_HELMET.getKey(),
			TFItems.PHANTOM_HELMET.getKey(),
			TFItems.FIERY_HELMET.getKey(),
			TFItems.ARCTIC_HELMET.getKey(),
			TFItems.YETI_HELMET.getKey());

		this.tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(
			TFItems.NAGA_CHESTPLATE.getKey(),
			TFItems.IRONWOOD_CHESTPLATE.getKey(),
			TFItems.STEELEAF_CHESTPLATE.getKey(),
			TFItems.KNIGHTMETAL_CHESTPLATE.getKey(),
			TFItems.PHANTOM_CHESTPLATE.getKey(),
			TFItems.FIERY_CHESTPLATE.getKey(),
			TFItems.ARCTIC_CHESTPLATE.getKey(),
			TFItems.YETI_CHESTPLATE.getKey());

		this.tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(
			TFItems.NAGA_LEGGINGS.getKey(),
			TFItems.IRONWOOD_LEGGINGS.getKey(),
			TFItems.STEELEAF_LEGGINGS.getKey(),
			TFItems.KNIGHTMETAL_LEGGINGS.getKey(),
			TFItems.FIERY_LEGGINGS.getKey(),
			TFItems.ARCTIC_LEGGINGS.getKey(),
			TFItems.YETI_LEGGINGS.getKey());

		this.tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(
			TFItems.IRONWOOD_BOOTS.getKey(),
			TFItems.STEELEAF_BOOTS.getKey(),
			TFItems.KNIGHTMETAL_BOOTS.getKey(),
			TFItems.FIERY_BOOTS.getKey(),
			TFItems.ARCTIC_BOOTS.getKey(),
			TFItems.YETI_BOOTS.getKey());

		this.tag(ItemTags.SWORDS).add(
			TFItems.IRONWOOD_SWORD.getKey(),
			TFItems.STEELEAF_SWORD.getKey(),
			TFItems.KNIGHTMETAL_SWORD.getKey(),
			TFItems.FIERY_SWORD.getKey(),
			TFItems.GIANT_SWORD.getKey(),
			TFItems.ICE_SWORD.getKey(),
			TFItems.GLASS_SWORD.getKey());

		this.tag(ItemTags.PICKAXES).add(
			TFItems.IRONWOOD_PICKAXE.getKey(),
			TFItems.STEELEAF_PICKAXE.getKey(),
			TFItems.KNIGHTMETAL_PICKAXE.getKey(),
			TFItems.MAZEBREAKER_PICKAXE.getKey(),
			TFItems.FIERY_PICKAXE.getKey(),
			TFItems.GIANT_PICKAXE.getKey());

		this.tag(ItemTags.AXES).add(TFItems.IRONWOOD_AXE.getKey(), TFItems.STEELEAF_AXE.getKey(), TFItems.KNIGHTMETAL_AXE.getKey(), TFItems.GOLDEN_MINOTAUR_AXE.getKey(), TFItems.DIAMOND_MINOTAUR_AXE.getKey());
		this.tag(ItemTags.SHOVELS).add(TFItems.IRONWOOD_SHOVEL.getKey(), TFItems.STEELEAF_SHOVEL.getKey());
		this.tag(ItemTags.HOES).add(TFItems.IRONWOOD_HOE.getKey(), TFItems.STEELEAF_HOE.getKey());
		this.tag(Tags.Items.TOOLS_SHIELD).add(TFItems.KNIGHTMETAL_SHIELD.getKey());
		this.tag(Tags.Items.TOOLS_BOW).add(TFItems.TRIPLE_BOW.getKey(), TFItems.SEEKER_BOW.getKey(), TFItems.ICE_BOW.getKey(), TFItems.ENDER_BOW.getKey());

		this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
			TFItems.IRONWOOD_PICKAXE.getKey(),
			TFItems.STEELEAF_PICKAXE.getKey(),
			TFItems.KNIGHTMETAL_PICKAXE.getKey(),
			TFItems.MAZEBREAKER_PICKAXE.getKey(),
			TFItems.FIERY_PICKAXE.getKey(),
			TFItems.GIANT_PICKAXE.getKey());

		this.tag(ItemTags.TRIM_MATERIALS).add(TFItems.IRONWOOD_INGOT.getKey(), TFItems.STEELEAF_INGOT.getKey(), TFItems.KNIGHTMETAL_INGOT.getKey(), TFItems.NAGA_SCALE.getKey(), TFItems.CARMINITE.getKey(), TFItems.FIERY_INGOT.getKey());

		this.tag(TFItemTags.REPAIRS_IRONWOOD_TOOLS).addTag(TFItemTags.IRONWOOD_INGOTS);
		this.tag(TFItemTags.REPAIRS_STEELEAF_TOOLS).addTag(TFItemTags.STEELEAF_INGOTS);
		this.tag(TFItemTags.REPAIRS_KNIGHTMETAL_TOOLS).addTag(TFItemTags.KNIGHTMETAL_INGOTS);
		this.tag(TFItemTags.REPAIRS_FIERY_TOOLS).addTag(TFItemTags.FIERY_INGOTS);
		this.tag(TFItemTags.REPAIRS_GIANT_TOOLS).add(TFBlocks.GIANT_COBBLESTONE.asItem().builtInRegistryHolder().key());
		this.tag(TFItemTags.REPAIRS_ICE_TOOLS).add(Blocks.ICE.asItem().builtInRegistryHolder().key(), Blocks.PACKED_ICE.asItem().builtInRegistryHolder().key(), Blocks.BLUE_ICE.asItem().builtInRegistryHolder().key());

		this.tag(ItemTags.MEAT).add(TFItems.RAW_VENISON.getKey(), TFItems.COOKED_VENISON.getKey(), TFItems.RAW_MEEF.getKey(), TFItems.COOKED_MEEF.getKey(), TFItems.MEEF_STROGANOFF.getKey(), TFItems.EXPERIMENT_115.getKey(), TFItems.HYDRA_CHOP.getKey());
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).addTags(TFItemTags.IRONWOOD_INGOTS, TFItemTags.STEELEAF_INGOTS, TFItemTags.KNIGHTMETAL_INGOTS, TFItemTags.FIERY_INGOTS);

		this.tag(ItemTags.TRIMMABLE_ARMOR).remove(TFItems.YETI_HELMET.getKey());

		this.tag(ItemTags.HEAD_ARMOR).add(
			TFItems.IRONWOOD_HELMET.getKey(),
			TFItems.STEELEAF_HELMET.getKey(),
			TFItems.KNIGHTMETAL_HELMET.getKey(),
			TFItems.ARCTIC_HELMET.getKey(),
			TFItems.YETI_HELMET.getKey(),
			TFItems.FIERY_HELMET.getKey(),
			TFItems.PHANTOM_HELMET.getKey());

		this.tag(ItemTags.CHEST_ARMOR).add(
			TFItems.IRONWOOD_CHESTPLATE.getKey(),
			TFItems.STEELEAF_CHESTPLATE.getKey(),
			TFItems.KNIGHTMETAL_CHESTPLATE.getKey(),
			TFItems.ARCTIC_CHESTPLATE.getKey(),
			TFItems.YETI_CHESTPLATE.getKey(),
			TFItems.FIERY_CHESTPLATE.getKey(),
			TFItems.PHANTOM_CHESTPLATE.getKey(),
			TFItems.NAGA_CHESTPLATE.getKey());

		this.tag(ItemTags.LEG_ARMOR).add(
			TFItems.IRONWOOD_LEGGINGS.getKey(),
			TFItems.STEELEAF_LEGGINGS.getKey(),
			TFItems.KNIGHTMETAL_LEGGINGS.getKey(),
			TFItems.ARCTIC_LEGGINGS.getKey(),
			TFItems.YETI_LEGGINGS.getKey(),
			TFItems.FIERY_LEGGINGS.getKey(),
			TFItems.NAGA_LEGGINGS.getKey());

		this.tag(ItemTags.FOOT_ARMOR).add(
			TFItems.IRONWOOD_BOOTS.getKey(),
			TFItems.STEELEAF_BOOTS.getKey(),
			TFItems.KNIGHTMETAL_BOOTS.getKey(),
			TFItems.ARCTIC_BOOTS.getKey(),
			TFItems.YETI_BOOTS.getKey(),
			TFItems.FIERY_BOOTS.getKey());

		this.tag(TFItemTags.BLOCK_AND_CHAIN_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.getKey());
		this.tag(ItemTags.BOW_ENCHANTABLE).add(TFItems.TRIPLE_BOW.getKey(), TFItems.SEEKER_BOW.getKey(), TFItems.ICE_BOW.getKey(), TFItems.ENDER_BOW.getKey());
		this.tag(ItemTags.MINING_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.getKey());
		this.tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(TFItems.BLOCK_AND_CHAIN.getKey());
		this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(
			TFItems.TRIPLE_BOW.getKey(), TFItems.SEEKER_BOW.getKey(), TFItems.ICE_BOW.getKey(), TFItems.ENDER_BOW.getKey(),
			TFItems.BLOCK_AND_CHAIN.getKey(), TFItems.KNIGHTMETAL_SHIELD.getKey(), TFItems.ORE_MAGNET.getKey(),
			TFItems.PEACOCK_FEATHER_FAN.getKey(), TFItems.CRUMBLE_HORN.getKey());
		this.tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).remove(TFItems.FIERY_SWORD.getKey(), TFItems.ICE_SWORD.getKey());
		this.tag(ItemTags.VANISHING_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.getKey(), TFItems.PHANTOM_CHESTPLATE.getKey());
		this.tag(ItemTags.EQUIPPABLE_ENCHANTABLE).remove(TFItems.PHANTOM_HELMET.getKey(), TFItems.PHANTOM_CHESTPLATE.getKey());
		this.tag(ItemTags.BREAKS_DECORATED_POTS).add(TFItems.BLOCK_AND_CHAIN.getKey());

		this.tag(Tags.Items.FOODS_BERRY).add(TFItems.TORCHBERRIES.getKey());
		this.tag(Tags.Items.FOODS_RAW_MEAT).add(TFItems.RAW_VENISON.getKey(), TFItems.RAW_MEEF.getKey());
		this.tag(Tags.Items.FOODS_COOKED_MEAT).add(TFItems.COOKED_VENISON.getKey(), TFItems.COOKED_MEEF.getKey(), TFItems.HYDRA_CHOP.getKey());
		this.tag(Tags.Items.FOODS_SOUP).add(TFItems.MEEF_STROGANOFF.getKey());
		this.tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).add(TFItems.EXPERIMENT_115.getKey());
		this.tag(Tags.Items.ROPES).add(TFItems.ROPE.getKey());
		this.tag(Tags.Items.MUSHROOMS).add(TFBlocks.MUSHGLOOM.asItem().builtInRegistryHolder().key());
		this.tag(Tags.Items.MUSIC_DISCS).add(
			TFItems.MUSIC_DISC_RADIANCE.getKey(), TFItems.MUSIC_DISC_STEPS.getKey(), TFItems.MUSIC_DISC_SUPERSTITIOUS.getKey(),
			TFItems.MUSIC_DISC_HOME.getKey(), TFItems.MUSIC_DISC_WAYFARER.getKey(), TFItems.MUSIC_DISC_FINDINGS.getKey(),
			TFItems.MUSIC_DISC_MAKER.getKey(), TFItems.MUSIC_DISC_THREAD.getKey(), TFItems.MUSIC_DISC_MOTION.getKey()
		);

		// Sulfur Cube archetype tags - only full blocks (not slabs, stairs, doors, fences, etc.)
		// BOUNCY is already covered by ItemTags.PLANKS and ItemTags.LOGS (registered above)

		// SLOW_BOUNCY - stone-like full blocks
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY)
			// Nagastone full blocks (no stairs)
			.add(TFBlocks.NAGASTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.NAGASTONE_HEAD.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.ETCHED_NAGASTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.NAGASTONE_PILLAR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MOSSY_ETCHED_NAGASTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MOSSY_NAGASTONE_PILLAR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CRACKED_ETCHED_NAGASTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CRACKED_NAGASTONE_PILLAR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.SPIRAL_BRICKS.asItem().builtInRegistryHolder().key())
			// Mazestone, Deadrock (via tag - all full blocks)
			.add(TFBlocks.TWISTED_STONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TWISTED_STONE_PILLAR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.BOLD_STONE_PILLAR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TERRORCOTTA_ARCS.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TERRORCOTTA_CURVES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TERRORCOTTA_LINES.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TROLLSTEINN.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.TROPHY_PEDESTAL.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.STRONGHOLD_SHIELD.asItem().builtInRegistryHolder().key())
			// Underbrick full blocks
			.add(TFBlocks.UNDERBRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MOSSY_UNDERBRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CRACKED_UNDERBRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.UNDERBRICK_FLOOR.asItem().builtInRegistryHolder().key());

		// SLOW_FLAT - metal storage blocks
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT)
			.add(TFBlocks.KNIGHTMETAL_BLOCK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.FIERY_BLOCK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.STEELEAF_BLOCK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CARMINITE_BLOCK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.IRONWOOD_BLOCK.asItem().builtInRegistryHolder().key());

		// FAST_SLIDING - ice-like full blocks
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_SLIDING)
			.add(TFBlocks.AURORA_BLOCK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.AURORA_PILLAR.asItem().builtInRegistryHolder().key());

		// SLOW_SLIDING - mushroom/fungus full blocks
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_SLIDING)
			.add(TFBlocks.HUGE_MUSHGLOOM.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.HUGE_MUSHGLOOM_STEM.asItem().builtInRegistryHolder().key());

		// FAST_FLAT - organic/plant full blocks
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_FLAT)
			.add(TFBlocks.ROOT_BLOCK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.LIVEROOT_BLOCK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.HUGE_STALK.asItem().builtInRegistryHolder().key());

		// LIGHT - wool/fur-like full blocks
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_LIGHT)
			.add(TFBlocks.ARCTIC_FUR_BLOCK.asItem().builtInRegistryHolder().key());

		// REGULAR - dirt/soil full blocks
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR)
			.add(TFBlocks.UBEROUS_SOIL.asItem().builtInRegistryHolder().key());

		// Copy from block tags that contain only full blocks
		// Note: copy() may not work at datagen time because block tags aren't loaded yet,
		// so mazestone and deadrock are added directly below.
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY)
			// Mazestone
			.add(TFBlocks.MAZESTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MAZESTONE_BRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CRACKED_MAZESTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MOSSY_MAZESTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CUT_MAZESTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.DECORATIVE_MAZESTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MAZESTONE_MOSAIC.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MAZESTONE_BORDER.asItem().builtInRegistryHolder().key())
			// Deadrock
			.add(TFBlocks.DEADROCK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CRACKED_DEADROCK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.WEATHERED_DEADROCK.asItem().builtInRegistryHolder().key());
		// Castle bricks added individually (CASTLE_BLOCKS tag includes doors which are not full blocks)
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY)
			.add(TFBlocks.CASTLE_BRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.WORN_CASTLE_BRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CRACKED_CASTLE_BRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.MOSSY_CASTLE_BRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.CASTLE_ROOF_TILE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.THICK_CASTLE_BRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.ENCASED_CASTLE_BRICK_TILE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.BOLD_CASTLE_BRICK_TILE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.PINK_CASTLE_RUNE_BRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.BLUE_CASTLE_RUNE_BRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.asItem().builtInRegistryHolder().key());

		// SULFUR_CUBE_GIANT_BLOCKS - giant blocks that make the cube bigger when swallowed
		this.tag(TFItemTags.SULFUR_CUBE_GIANT_BLOCKS)
			.add(TFBlocks.GIANT_COBBLESTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.GIANT_LOG.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.GIANT_OBSIDIAN.asItem().builtInRegistryHolder().key());

		// Also add giant blocks to SLOW_BOUNCY so they can be swallowed by sulfur cubes
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY)
			.add(TFBlocks.GIANT_COBBLESTONE.asItem().builtInRegistryHolder().key())
			.add(TFBlocks.GIANT_OBSIDIAN.asItem().builtInRegistryHolder().key());
		this.tag(ItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY)
			.add(TFBlocks.GIANT_LOG.asItem().builtInRegistryHolder().key());
	}

	private void copy(HolderLookup.Provider provider, TagKey<Block> blockTag, TagKey<Item> itemTag) {
		TagAppender<Item> appender = this.tag(itemTag);
		provider.lookupOrThrow(Registries.BLOCK).listElements()
			.filter(holder -> holder.is(blockTag))
			.forEach(holder -> appender.add(holder.value().asItem().builtInRegistryHolder().key()));
	}

	@Override
	public String getName() {
		return "Twilight Forest Item Tags";
	}
}
