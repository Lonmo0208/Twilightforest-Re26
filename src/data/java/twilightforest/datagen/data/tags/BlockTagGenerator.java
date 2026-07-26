package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import twilightforest.datagen.data.tags.compat.ModdedBlockTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends ModdedBlockTagGenerator {

	public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, future);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		this.tag(TFBlockTags.TWILIGHT_OAK_LOGS).add(TFBlocks.TWILIGHT_OAK_LOG.getKey(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.getKey(), TFBlocks.TWILIGHT_OAK_WOOD.getKey(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.getKey());
		this.tag(TFBlockTags.CANOPY_LOGS).add(TFBlocks.CANOPY_LOG.getKey(), TFBlocks.STRIPPED_CANOPY_LOG.getKey(), TFBlocks.CANOPY_WOOD.getKey(), TFBlocks.STRIPPED_CANOPY_WOOD.getKey());
		this.tag(TFBlockTags.MANGROVE_LOGS).add(TFBlocks.MANGROVE_LOG.getKey(), TFBlocks.STRIPPED_MANGROVE_LOG.getKey(), TFBlocks.MANGROVE_WOOD.getKey(), TFBlocks.STRIPPED_MANGROVE_WOOD.getKey());
		this.tag(TFBlockTags.DARKWOOD_LOGS).add(TFBlocks.DARK_LOG.getKey(), TFBlocks.STRIPPED_DARK_LOG.getKey(), TFBlocks.DARK_WOOD.getKey(), TFBlocks.STRIPPED_DARK_WOOD.getKey());
		this.tag(TFBlockTags.TIME_LOGS).add(TFBlocks.TIME_LOG.getKey(), TFBlocks.STRIPPED_TIME_LOG.getKey(), TFBlocks.TIME_WOOD.getKey(), TFBlocks.STRIPPED_TIME_WOOD.getKey());
		this.tag(TFBlockTags.TRANSFORMATION_LOGS).add(TFBlocks.TRANSFORMATION_LOG.getKey(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.getKey(), TFBlocks.TRANSFORMATION_WOOD.getKey(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.getKey());
		this.tag(TFBlockTags.MINING_LOGS).add(TFBlocks.MINING_LOG.getKey(), TFBlocks.STRIPPED_MINING_LOG.getKey(), TFBlocks.MINING_WOOD.getKey(), TFBlocks.STRIPPED_MINING_WOOD.getKey());
		this.tag(TFBlockTags.SORTING_LOGS).add(TFBlocks.SORTING_LOG.getKey(), TFBlocks.STRIPPED_SORTING_LOG.getKey(), TFBlocks.SORTING_WOOD.getKey(), TFBlocks.STRIPPED_SORTING_WOOD.getKey());
		this.tag(TFBlockTags.TF_LOGS).addTags(TFBlockTags.TWILIGHT_OAK_LOGS, TFBlockTags.CANOPY_LOGS, TFBlockTags.MANGROVE_LOGS, TFBlockTags.DARKWOOD_LOGS, TFBlockTags.TIME_LOGS, TFBlockTags.TRANSFORMATION_LOGS, TFBlockTags.MINING_LOGS, TFBlockTags.SORTING_LOGS);
		this.tag(BlockTags.LOGS).addTag(TFBlockTags.TF_LOGS);

		this.tag(BlockTags.LEAVES).add(TFBlocks.RAINBOW_OAK_LEAVES.getKey(), TFBlocks.TWILIGHT_OAK_LEAVES.getKey(), TFBlocks.CANOPY_LEAVES.getKey(), TFBlocks.MANGROVE_LEAVES.getKey(), TFBlocks.DARK_LEAVES.getKey(), TFBlocks.TIME_LEAVES.getKey(), TFBlocks.TRANSFORMATION_LEAVES.getKey(), TFBlocks.MINING_LEAVES.getKey(), TFBlocks.SORTING_LEAVES.getKey(), TFBlocks.THORN_LEAVES.getKey(), TFBlocks.BEANSTALK_LEAVES.getKey());

		this.tag(BlockTags.PLANKS).add(TFBlocks.TWILIGHT_OAK_PLANKS.getKey(), TFBlocks.CANOPY_PLANKS.getKey(), TFBlocks.MANGROVE_PLANKS.getKey(), TFBlocks.DARK_PLANKS.getKey(), TFBlocks.TIME_PLANKS.getKey(), TFBlocks.TRANSFORMATION_PLANKS.getKey(), TFBlocks.MINING_PLANKS.getKey(), TFBlocks.SORTING_PLANKS.getKey()).addTag(TFBlockTags.TOWERWOOD);
		this.tag(BlockTags.WOODEN_SLABS).add(TFBlocks.TWILIGHT_OAK_SLAB.getKey(), TFBlocks.CANOPY_SLAB.getKey(), TFBlocks.MANGROVE_SLAB.getKey(), TFBlocks.DARK_SLAB.getKey(), TFBlocks.TIME_SLAB.getKey(), TFBlocks.TRANSFORMATION_SLAB.getKey(), TFBlocks.MINING_SLAB.getKey(), TFBlocks.SORTING_SLAB.getKey());
		this.tag(BlockTags.SLABS).add(TFBlocks.AURORA_SLAB.getKey());
		this.tag(BlockTags.WOODEN_STAIRS).add(TFBlocks.TWILIGHT_OAK_STAIRS.getKey(), TFBlocks.CANOPY_STAIRS.getKey(), TFBlocks.MANGROVE_STAIRS.getKey(), TFBlocks.DARK_STAIRS.getKey(), TFBlocks.TIME_STAIRS.getKey(), TFBlocks.TRANSFORMATION_STAIRS.getKey(), TFBlocks.MINING_STAIRS.getKey(), TFBlocks.SORTING_STAIRS.getKey());
		this.tag(BlockTags.STAIRS).add(TFBlocks.CASTLE_BRICK_STAIRS.getKey(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.getKey(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.getKey(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.getKey(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.getKey(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.getKey(), TFBlocks.NAGASTONE_STAIRS_LEFT.getKey(), TFBlocks.NAGASTONE_STAIRS_RIGHT.getKey(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.getKey(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.getKey(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.getKey(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.getKey());
		this.tag(BlockTags.WOODEN_FENCES).add(TFBlocks.TWILIGHT_OAK_FENCE.getKey(), TFBlocks.CANOPY_FENCE.getKey(), TFBlocks.MANGROVE_FENCE.getKey(), TFBlocks.DARK_FENCE.getKey(), TFBlocks.TIME_FENCE.getKey(), TFBlocks.TRANSFORMATION_FENCE.getKey(), TFBlocks.MINING_FENCE.getKey(), TFBlocks.SORTING_FENCE.getKey());
		this.tag(BlockTags.FENCE_GATES).add(TFBlocks.TWILIGHT_OAK_GATE.getKey(), TFBlocks.CANOPY_GATE.getKey(), TFBlocks.MANGROVE_GATE.getKey(), TFBlocks.DARK_GATE.getKey(), TFBlocks.TIME_GATE.getKey(), TFBlocks.TRANSFORMATION_GATE.getKey(), TFBlocks.MINING_GATE.getKey(), TFBlocks.SORTING_GATE.getKey());
		this.tag(Tags.Blocks.FENCE_GATES_WOODEN).add(TFBlocks.TWILIGHT_OAK_GATE.getKey(), TFBlocks.CANOPY_GATE.getKey(), TFBlocks.MANGROVE_GATE.getKey(), TFBlocks.DARK_GATE.getKey(), TFBlocks.TIME_GATE.getKey(), TFBlocks.TRANSFORMATION_GATE.getKey(), TFBlocks.MINING_GATE.getKey(), TFBlocks.SORTING_GATE.getKey());
		this.tag(BlockTags.WOODEN_BUTTONS).add(TFBlocks.TWILIGHT_OAK_BUTTON.getKey(), TFBlocks.CANOPY_BUTTON.getKey(), TFBlocks.MANGROVE_BUTTON.getKey(), TFBlocks.DARK_BUTTON.getKey(), TFBlocks.TIME_BUTTON.getKey(), TFBlocks.TRANSFORMATION_BUTTON.getKey(), TFBlocks.MINING_BUTTON.getKey(), TFBlocks.SORTING_BUTTON.getKey());
		this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(TFBlocks.TWILIGHT_OAK_PLATE.getKey(), TFBlocks.CANOPY_PLATE.getKey(), TFBlocks.MANGROVE_PLATE.getKey(), TFBlocks.DARK_PLATE.getKey(), TFBlocks.TIME_PLATE.getKey(), TFBlocks.TRANSFORMATION_PLATE.getKey(), TFBlocks.MINING_PLATE.getKey(), TFBlocks.SORTING_PLATE.getKey());

		this.tag(BlockTags.WOODEN_TRAPDOORS).add(TFBlocks.TWILIGHT_OAK_TRAPDOOR.getKey(), TFBlocks.CANOPY_TRAPDOOR.getKey(), TFBlocks.MANGROVE_TRAPDOOR.getKey(), TFBlocks.DARK_TRAPDOOR.getKey(), TFBlocks.TIME_TRAPDOOR.getKey(), TFBlocks.TRANSFORMATION_TRAPDOOR.getKey(), TFBlocks.MINING_TRAPDOOR.getKey(), TFBlocks.SORTING_TRAPDOOR.getKey());
		this.tag(BlockTags.WOODEN_DOORS).add(TFBlocks.TWILIGHT_OAK_DOOR.getKey(), TFBlocks.CANOPY_DOOR.getKey(), TFBlocks.MANGROVE_DOOR.getKey(), TFBlocks.DARK_DOOR.getKey(), TFBlocks.TIME_DOOR.getKey(), TFBlocks.TRANSFORMATION_DOOR.getKey(), TFBlocks.MINING_DOOR.getKey(), TFBlocks.SORTING_DOOR.getKey());

		this.tag(Tags.Blocks.CHESTS_WOODEN).add(TFBlocks.TWILIGHT_OAK_CHEST.getKey(), TFBlocks.CANOPY_CHEST.getKey(), TFBlocks.MANGROVE_CHEST.getKey(), TFBlocks.DARK_CHEST.getKey(), TFBlocks.TIME_CHEST.getKey(), TFBlocks.TRANSFORMATION_CHEST.getKey(), TFBlocks.MINING_CHEST.getKey(), TFBlocks.SORTING_CHEST.getKey());
		this.tag(Tags.Blocks.CHESTS_TRAPPED).add(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.getKey(), TFBlocks.CANOPY_TRAPPED_CHEST.getKey(), TFBlocks.MANGROVE_TRAPPED_CHEST.getKey(), TFBlocks.DARK_TRAPPED_CHEST.getKey(), TFBlocks.TIME_TRAPPED_CHEST.getKey(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.getKey(), TFBlocks.MINING_TRAPPED_CHEST.getKey(), TFBlocks.SORTING_TRAPPED_CHEST.getKey());

		this.tag(BlockTags.FLOWER_POTS).add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.getKey(), TFBlocks.POTTED_CANOPY_SAPLING.getKey(), TFBlocks.POTTED_MANGROVE_SAPLING.getKey(), TFBlocks.POTTED_DARKWOOD_SAPLING.getKey(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.getKey(), TFBlocks.POTTED_HOLLOW_OAK_SAPLING.getKey(), TFBlocks.POTTED_TIME_SAPLING.getKey(), TFBlocks.POTTED_TRANSFORMATION_SAPLING.getKey(), TFBlocks.POTTED_MINING_SAPLING.getKey(), TFBlocks.POTTED_SORTING_SAPLING.getKey(), TFBlocks.POTTED_MAYAPPLE.getKey(), TFBlocks.POTTED_FIDDLEHEAD.getKey(), TFBlocks.POTTED_MUSHGLOOM.getKey(), TFBlocks.POTTED_THORN.getKey(), TFBlocks.POTTED_GREEN_THORN.getKey(), TFBlocks.POTTED_DEAD_THORN.getKey());

		this.tag(BlockTags.WALLS).add(TFBlocks.WROUGHT_IRON_FENCE.getKey());

		this.tag(TFBlockTags.BANISTERS).add(
			TFBlocks.OAK_BANISTER.getKey(),
			TFBlocks.SPRUCE_BANISTER.getKey(),
			TFBlocks.BIRCH_BANISTER.getKey(),
			TFBlocks.JUNGLE_BANISTER.getKey(),
			TFBlocks.ACACIA_BANISTER.getKey(),
			TFBlocks.DARK_OAK_BANISTER.getKey(),
			TFBlocks.CRIMSON_BANISTER.getKey(),
			TFBlocks.WARPED_BANISTER.getKey(),
			TFBlocks.VANGROVE_BANISTER.getKey(),
			TFBlocks.BAMBOO_BANISTER.getKey(),
			TFBlocks.CHERRY_BANISTER.getKey(),
			TFBlocks.PALE_OAK_BANISTER.getKey(),

			TFBlocks.TWILIGHT_OAK_BANISTER.getKey(),
			TFBlocks.CANOPY_BANISTER.getKey(),
			TFBlocks.MANGROVE_BANISTER.getKey(),
			TFBlocks.DARK_BANISTER.getKey(),
			TFBlocks.TIME_BANISTER.getKey(),
			TFBlocks.TRANSFORMATION_BANISTER.getKey(),
			TFBlocks.MINING_BANISTER.getKey(),
			TFBlocks.SORTING_BANISTER.getKey()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS_HORIZONTAL).add(
			TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.getKey(),
			TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.getKey()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS_VERTICAL).add(
			TFBlocks.HOLLOW_OAK_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.getKey(),
			TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.getKey(),
			TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_DARK_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_TIME_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_MINING_LOG_VERTICAL.getKey(),
			TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.getKey()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS_CLIMBABLE).add(
			TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_PALE_OAK_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.getKey(),
			TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.getKey()
		);

		this.tag(TFBlockTags.HOLLOW_LOGS).addTags(TFBlockTags.HOLLOW_LOGS_HORIZONTAL, TFBlockTags.HOLLOW_LOGS_VERTICAL, TFBlockTags.HOLLOW_LOGS_CLIMBABLE);

		this.tag(BlockTags.STRIDER_WARM_BLOCKS).add(TFBlocks.FIERY_BLOCK.getKey());
		this.tag(BlockTags.PORTALS).add(TFBlocks.TWILIGHT_PORTAL.getKey());
		this.tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(TFBlocks.CANOPY_BOOKSHELF.getKey());
		this.tag(BlockTags.REPLACEABLE_BY_TREES).add(
			TFBlocks.HARDENED_DARK_LEAVES.getKey(),
			TFBlocks.MAYAPPLE.getKey(),
			TFBlocks.FIDDLEHEAD.getKey(),
			TFBlocks.MOSS_PATCH.getKey(),
			TFBlocks.CLOVER_PATCH.getKey(),
			TFBlocks.MUSHGLOOM.getKey(),
			TFBlocks.FIREFLY.getKey(),
			TFBlocks.FALLEN_LEAVES.getKey(),
			TFBlocks.TORCHBERRY_PLANT.getKey(),
			TFBlocks.ROOT_STRAND.getKey(),
			TFBlocks.ROOT_BLOCK.getKey());

		this.tag(BlockTags.CLIMBABLE).add(TFBlocks.IRON_LADDER.getKey(), TFBlocks.ROPE.getKey(), TFBlocks.ROOT_STRAND.getKey()).addTag(TFBlockTags.HOLLOW_LOGS_CLIMBABLE);

		this.tag(BlockTags.STANDING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_SIGN.getKey(), TFBlocks.CANOPY_SIGN.getKey(),
			TFBlocks.MANGROVE_SIGN.getKey(), TFBlocks.DARK_SIGN.getKey(),
			TFBlocks.TIME_SIGN.getKey(), TFBlocks.TRANSFORMATION_SIGN.getKey(),
			TFBlocks.MINING_SIGN.getKey(), TFBlocks.SORTING_SIGN.getKey());

		this.tag(BlockTags.WALL_SIGNS).add(
			TFBlocks.TWILIGHT_WALL_SIGN.getKey(), TFBlocks.CANOPY_WALL_SIGN.getKey(),
			TFBlocks.MANGROVE_WALL_SIGN.getKey(), TFBlocks.DARK_WALL_SIGN.getKey(),
			TFBlocks.TIME_WALL_SIGN.getKey(), TFBlocks.TRANSFORMATION_WALL_SIGN.getKey(),
			TFBlocks.MINING_WALL_SIGN.getKey(), TFBlocks.SORTING_WALL_SIGN.getKey());

		this.tag(BlockTags.CEILING_HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_HANGING_SIGN.getKey(), TFBlocks.CANOPY_HANGING_SIGN.getKey(),
			TFBlocks.MANGROVE_HANGING_SIGN.getKey(), TFBlocks.DARK_HANGING_SIGN.getKey(),
			TFBlocks.TIME_HANGING_SIGN.getKey(), TFBlocks.TRANSFORMATION_HANGING_SIGN.getKey(),
			TFBlocks.MINING_HANGING_SIGN.getKey(), TFBlocks.SORTING_HANGING_SIGN.getKey());

		this.tag(BlockTags.WALL_HANGING_SIGNS).add(
			TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.getKey(), TFBlocks.CANOPY_WALL_HANGING_SIGN.getKey(),
			TFBlocks.MANGROVE_WALL_HANGING_SIGN.getKey(), TFBlocks.DARK_WALL_HANGING_SIGN.getKey(),
			TFBlocks.TIME_WALL_HANGING_SIGN.getKey(), TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.getKey(),
			TFBlocks.MINING_WALL_HANGING_SIGN.getKey(), TFBlocks.SORTING_WALL_HANGING_SIGN.getKey());

		this.tag(TFBlockTags.TOWERWOOD).add(TFBlocks.TOWERWOOD.getKey(), TFBlocks.MOSSY_TOWERWOOD.getKey(), TFBlocks.CRACKED_TOWERWOOD.getKey(), TFBlocks.INFESTED_TOWERWOOD.getKey());

		this.tag(TFBlockTags.MAZESTONE).add(
			TFBlocks.MAZESTONE.getKey(), TFBlocks.MAZESTONE_BRICK.getKey(),
			TFBlocks.CRACKED_MAZESTONE.getKey(), TFBlocks.MOSSY_MAZESTONE.getKey(),
			TFBlocks.CUT_MAZESTONE.getKey(), TFBlocks.DECORATIVE_MAZESTONE.getKey(),
			TFBlocks.MAZESTONE_MOSAIC.getKey(), TFBlocks.MAZESTONE_BORDER.getKey());

		this.tag(TFBlockTags.CASTLE_BLOCKS).add(
			TFBlocks.CASTLE_BRICK.getKey(), TFBlocks.WORN_CASTLE_BRICK.getKey(),
			TFBlocks.CRACKED_CASTLE_BRICK.getKey(), TFBlocks.MOSSY_CASTLE_BRICK.getKey(),
			TFBlocks.CASTLE_ROOF_TILE.getKey(), TFBlocks.THICK_CASTLE_BRICK.getKey(),
			TFBlocks.BOLD_CASTLE_BRICK_TILE.getKey(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.getKey(),
			TFBlocks.ENCASED_CASTLE_BRICK_TILE.getKey(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.getKey(),
			TFBlocks.CASTLE_BRICK_STAIRS.getKey(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.getKey(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.getKey(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.getKey(),
			TFBlocks.PINK_CASTLE_RUNE_BRICK.getKey(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.getKey(),
			TFBlocks.BLUE_CASTLE_RUNE_BRICK.getKey(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.getKey(),
			TFBlocks.PINK_CASTLE_DOOR.getKey(), TFBlocks.YELLOW_CASTLE_DOOR.getKey(),
			TFBlocks.BLUE_CASTLE_DOOR.getKey(), TFBlocks.VIOLET_CASTLE_DOOR.getKey()
		);

		this.tag(TFBlockTags.MAZEBREAKER_ACCELERATED).addTag(TFBlockTags.MAZESTONE).addTag(TFBlockTags.CASTLE_BLOCKS);

		this.tag(TFBlockTags.STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.getKey());
		this.tag(TFBlockTags.STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.getKey());
		this.tag(TFBlockTags.STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.getKey());
		this.tag(TFBlockTags.STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.getKey());
		this.tag(TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.getKey());
		this.tag(TFBlockTags.STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.getKey());

		this.tag(BlockTags.BEACON_BASE_BLOCKS).addTags(TFBlockTags.STORAGE_BLOCKS_FIERY, TFBlockTags.STORAGE_BLOCKS_IRONWOOD, TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL, TFBlockTags.STORAGE_BLOCKS_STEELEAF);

		this.tag(Tags.Blocks.STORAGE_BLOCKS).addTags(TFBlockTags.STORAGE_BLOCKS_ARCTIC_FUR, TFBlockTags.STORAGE_BLOCKS_CARMINITE, TFBlockTags.STORAGE_BLOCKS_FIERY, TFBlockTags.STORAGE_BLOCKS_IRONWOOD, TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL, TFBlockTags.STORAGE_BLOCKS_STEELEAF);

		this.tag(BlockTags.DIRT).add(TFBlocks.UBEROUS_SOIL.getKey());
		this.tag(TFBlockTags.PORTAL_EDGE).add(Blocks.FARMLAND.builtInRegistryHolder().key(), Blocks.DIRT_PATH.builtInRegistryHolder().key()).addTags(BlockTags.DIRT, BlockTags.GRASS_BLOCKS);
		this.tag(TFBlockTags.PORTAL_POOL).add(Blocks.WATER.builtInRegistryHolder().key());
		this.tag(TFBlockTags.PORTAL_DECO).add(
				Blocks.BAMBOO.builtInRegistryHolder().key(),
				Blocks.SHORT_GRASS.builtInRegistryHolder().key(), Blocks.TALL_GRASS.builtInRegistryHolder().key(),
				Blocks.FERN.builtInRegistryHolder().key(), Blocks.LARGE_FERN.builtInRegistryHolder().key(),
				Blocks.DEAD_BUSH.builtInRegistryHolder().key(),
				Blocks.SUGAR_CANE.builtInRegistryHolder().key(),
				Blocks.CHORUS_PLANT.builtInRegistryHolder().key(), Blocks.CHORUS_FLOWER.builtInRegistryHolder().key(),
				Blocks.SWEET_BERRY_BUSH.builtInRegistryHolder().key(),
				Blocks.NETHER_WART.builtInRegistryHolder().key(),
				Blocks.COCOA.builtInRegistryHolder().key(),
				Blocks.VINE.builtInRegistryHolder().key(), Blocks.GLOW_LICHEN.builtInRegistryHolder().key(),
				Blocks.RED_MUSHROOM.builtInRegistryHolder().key(), Blocks.BROWN_MUSHROOM.builtInRegistryHolder().key(),
				Blocks.WARPED_FUNGUS.builtInRegistryHolder().key(), Blocks.CRIMSON_FUNGUS.builtInRegistryHolder().key(),
				Blocks.ATTACHED_MELON_STEM.builtInRegistryHolder().key(), Blocks.ATTACHED_PUMPKIN_STEM.builtInRegistryHolder().key(),
				Blocks.MOSS_CARPET.builtInRegistryHolder().key(),
				Blocks.PINK_PETALS.builtInRegistryHolder().key(),
				Blocks.BIG_DRIPLEAF.builtInRegistryHolder().key(),
				Blocks.BIG_DRIPLEAF_STEM.builtInRegistryHolder().key(),
				Blocks.SMALL_DRIPLEAF.builtInRegistryHolder().key(),
				TFBlocks.FIDDLEHEAD.getKey(),
				TFBlocks.MOSS_PATCH.getKey(),
				TFBlocks.MAYAPPLE.getKey(),
				TFBlocks.CLOVER_PATCH.getKey(),
				TFBlocks.MUSHGLOOM.getKey(),
				TFBlocks.FALLEN_LEAVES.getKey(),
				TFBlocks.GIANT_LEAVES.getKey(),
				TFBlocks.STEELEAF_BLOCK.getKey(),
				TFBlocks.HARDENED_DARK_LEAVES.getKey())
			.addTags(BlockTags.FLOWERS, BlockTags.LEAVES, BlockTags.CROPS);

		this.tag(TFBlockTags.GENERATED_PORTAL_DECO)
			.add(Blocks.BROWN_MUSHROOM.builtInRegistryHolder().key(), Blocks.RED_MUSHROOM.builtInRegistryHolder().key(),
				Blocks.SHORT_GRASS.builtInRegistryHolder().key(), Blocks.FERN.builtInRegistryHolder().key(),
				Blocks.BLUE_ORCHID.builtInRegistryHolder().key(), Blocks.AZURE_BLUET.builtInRegistryHolder().key(),
				Blocks.LILY_OF_THE_VALLEY.builtInRegistryHolder().key(), Blocks.OXEYE_DAISY.builtInRegistryHolder().key(),
				Blocks.ALLIUM.builtInRegistryHolder().key(), Blocks.CORNFLOWER.builtInRegistryHolder().key(),
				Blocks.WHITE_TULIP.builtInRegistryHolder().key(), Blocks.PINK_TULIP.builtInRegistryHolder().key(),
				Blocks.ORANGE_TULIP.builtInRegistryHolder().key(), Blocks.RED_TULIP.builtInRegistryHolder().key(),
				TFBlocks.MUSHGLOOM.getKey(),
				TFBlocks.MAYAPPLE.getKey(),
				TFBlocks.FIDDLEHEAD.getKey());

		this.tag(TFBlockTags.DARK_TOWER_ALLOWED_POTS)
			.add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.getKey(), TFBlocks.POTTED_CANOPY_SAPLING.getKey(), TFBlocks.POTTED_MANGROVE_SAPLING.getKey(),
				TFBlocks.POTTED_DARKWOOD_SAPLING.getKey(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.getKey(), TFBlocks.POTTED_MAYAPPLE.getKey(),
				TFBlocks.POTTED_FIDDLEHEAD.getKey(), TFBlocks.POTTED_MUSHGLOOM.getKey())
			.add(Blocks.FLOWER_POT.builtInRegistryHolder().key(), Blocks.POTTED_POPPY.builtInRegistryHolder().key(), Blocks.POTTED_BLUE_ORCHID.builtInRegistryHolder().key(), Blocks.POTTED_ALLIUM.builtInRegistryHolder().key(), Blocks.POTTED_AZURE_BLUET.builtInRegistryHolder().key(),
				Blocks.POTTED_RED_TULIP.builtInRegistryHolder().key(), Blocks.POTTED_ORANGE_TULIP.builtInRegistryHolder().key(), Blocks.POTTED_WHITE_TULIP.builtInRegistryHolder().key(), Blocks.POTTED_PINK_TULIP.builtInRegistryHolder().key(),
				Blocks.POTTED_OXEYE_DAISY.builtInRegistryHolder().key(), Blocks.POTTED_DANDELION.builtInRegistryHolder().key(), Blocks.POTTED_OAK_SAPLING.builtInRegistryHolder().key(), Blocks.POTTED_SPRUCE_SAPLING.builtInRegistryHolder().key(),
				Blocks.POTTED_BIRCH_SAPLING.builtInRegistryHolder().key(), Blocks.POTTED_JUNGLE_SAPLING.builtInRegistryHolder().key(), Blocks.POTTED_ACACIA_SAPLING.builtInRegistryHolder().key(), Blocks.POTTED_DARK_OAK_SAPLING.builtInRegistryHolder().key(),
				Blocks.POTTED_RED_MUSHROOM.builtInRegistryHolder().key(), Blocks.POTTED_BROWN_MUSHROOM.builtInRegistryHolder().key(), Blocks.POTTED_DEAD_BUSH.builtInRegistryHolder().key(), Blocks.POTTED_FERN.builtInRegistryHolder().key(),
				Blocks.POTTED_CACTUS.builtInRegistryHolder().key(), Blocks.POTTED_CORNFLOWER.builtInRegistryHolder().key(), Blocks.POTTED_LILY_OF_THE_VALLEY.builtInRegistryHolder().key(), Blocks.POTTED_WITHER_ROSE.builtInRegistryHolder().key(),
				Blocks.POTTED_BAMBOO.builtInRegistryHolder().key(), Blocks.POTTED_CRIMSON_FUNGUS.builtInRegistryHolder().key(), Blocks.POTTED_WARPED_FUNGUS.builtInRegistryHolder().key(), Blocks.POTTED_CRIMSON_ROOTS.builtInRegistryHolder().key(),
				Blocks.POTTED_WARPED_ROOTS.builtInRegistryHolder().key(), Blocks.POTTED_AZALEA.builtInRegistryHolder().key(), Blocks.POTTED_FLOWERING_AZALEA.builtInRegistryHolder().key(), Blocks.POTTED_MANGROVE_PROPAGULE.builtInRegistryHolder().key());

		this.tag(BlockTags.FROG_PREFER_JUMP_TO).add(TFBlocks.HUGE_LILY_PAD.getKey());

		this.tag(TFBlockTags.TROPHIES)
			.add(TFBlocks.NAGA_TROPHY.getKey(), TFBlocks.NAGA_WALL_TROPHY.getKey())
			.add(TFBlocks.LICH_TROPHY.getKey(), TFBlocks.LICH_WALL_TROPHY.getKey())
			.add(TFBlocks.MINOSHROOM_TROPHY.getKey(), TFBlocks.MINOSHROOM_WALL_TROPHY.getKey())
			.add(TFBlocks.HYDRA_TROPHY.getKey(), TFBlocks.HYDRA_WALL_TROPHY.getKey())
			.add(TFBlocks.KNIGHT_PHANTOM_TROPHY.getKey(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.getKey())
			.add(TFBlocks.UR_GHAST_TROPHY.getKey(), TFBlocks.UR_GHAST_WALL_TROPHY.getKey())
			.add(TFBlocks.ALPHA_YETI_TROPHY.getKey(), TFBlocks.ALPHA_YETI_WALL_TROPHY.getKey())
			.add(TFBlocks.SNOW_QUEEN_TROPHY.getKey(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.getKey())
			.add(TFBlocks.QUEST_RAM_TROPHY.getKey(), TFBlocks.QUEST_RAM_WALL_TROPHY.getKey());

		this.tag(TFBlockTags.FIRE_JET_FUEL).add(Blocks.LAVA.builtInRegistryHolder().key());

		this.tag(TFBlockTags.ICE_BOMB_REPLACEABLES)
			.add(TFBlocks.MAYAPPLE.getKey(), TFBlocks.FIDDLEHEAD.getKey(), Blocks.SHORT_GRASS.builtInRegistryHolder().key(), Blocks.TALL_GRASS.builtInRegistryHolder().key(), Blocks.FERN.builtInRegistryHolder().key(), Blocks.LARGE_FERN.builtInRegistryHolder().key())
			.addTag(BlockTags.FLOWERS);

		this.tag(TFBlockTags.PLANTS_HANG_ON)
			.addTag(BlockTags.DIRT)
			.add(Blocks.MOSS_BLOCK.builtInRegistryHolder().key(), TFBlocks.MANGROVE_ROOT.getKey(), TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey());

		this.tag(TFBlockTags.COMMON_PROTECTIONS).add( // For any blocks that absolutely should not be meddled with
			TFBlocks.NAGA_BOSS_SPAWNER.getKey(),
			TFBlocks.LICH_BOSS_SPAWNER.getKey(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.getKey(),
			TFBlocks.HYDRA_BOSS_SPAWNER.getKey(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.getKey(),
			TFBlocks.UR_GHAST_BOSS_SPAWNER.getKey(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.getKey(),
			TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.getKey(),
			TFBlocks.FINAL_BOSS_BOSS_SPAWNER.getKey(),
			TFBlocks.STRONGHOLD_SHIELD.getKey(),
			TFBlocks.UNBREAKABLE_VANISHING_BLOCK.getKey(),
			TFBlocks.LOCKED_VANISHING_BLOCK.getKey(),
			TFBlocks.PINK_FORCE_FIELD.getKey(),
			TFBlocks.ORANGE_FORCE_FIELD.getKey(),
			TFBlocks.GREEN_FORCE_FIELD.getKey(),
			TFBlocks.BLUE_FORCE_FIELD.getKey(),
			TFBlocks.VIOLET_FORCE_FIELD.getKey(),
			TFBlocks.SKULL_CHEST.getKey(),
			TFBlocks.KEEPSAKE_CASKET.getKey(),
			TFBlocks.TROPHY_PEDESTAL.getKey()
		).add( // [VanillaCopy] WITHER_IMMUNE - Do NOT include that tag in this tag
			Blocks.BARRIER.builtInRegistryHolder().key(),
			Blocks.BEDROCK.builtInRegistryHolder().key(),
			Blocks.END_PORTAL.builtInRegistryHolder().key(),
			Blocks.END_PORTAL_FRAME.builtInRegistryHolder().key(),
			Blocks.END_GATEWAY.builtInRegistryHolder().key(),
			Blocks.COMMAND_BLOCK.builtInRegistryHolder().key(),
			Blocks.REPEATING_COMMAND_BLOCK.builtInRegistryHolder().key(),
			Blocks.CHAIN_COMMAND_BLOCK.builtInRegistryHolder().key(),
			Blocks.STRUCTURE_BLOCK.builtInRegistryHolder().key(),
			Blocks.JIGSAW.builtInRegistryHolder().key(),
			Blocks.MOVING_PISTON.builtInRegistryHolder().key(),
			Blocks.LIGHT.builtInRegistryHolder().key(),
			Blocks.REINFORCED_DEEPSLATE.builtInRegistryHolder().key()
		);

		this.tag(BlockTags.DRAGON_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.GIANT_OBSIDIAN.getKey(), TFBlocks.FAKE_DIAMOND.getKey(), TFBlocks.FAKE_GOLD.getKey());

		this.tag(BlockTags.WITHER_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.FAKE_DIAMOND.getKey(), TFBlocks.FAKE_GOLD.getKey());

		this.tag(TFBlockTags.CARMINITE_REACTOR_IMMUNE).addTag(TFBlockTags.COMMON_PROTECTIONS);

		this.tag(TFBlockTags.CARMINITE_REACTOR_ORES).add(Blocks.NETHER_QUARTZ_ORE.builtInRegistryHolder().key(), Blocks.NETHER_GOLD_ORE.builtInRegistryHolder().key());

		this.tag(TFBlockTags.DEADROCK).add(TFBlocks.DEADROCK.getKey(), TFBlocks.CRACKED_DEADROCK.getKey(), TFBlocks.WEATHERED_DEADROCK.getKey());

		this.tag(TFBlockTags.ANNIHILATION_INCLUSIONS) // This is NOT a blacklist! This is a whitelist
			.add(Blocks.NETHER_PORTAL.builtInRegistryHolder().key())
			.addTag(TFBlockTags.DEADROCK)
			.add(TFBlocks.CASTLE_BRICK.getKey(), TFBlocks.THICK_CASTLE_BRICK.getKey(), TFBlocks.MOSSY_CASTLE_BRICK.getKey(), TFBlocks.CASTLE_ROOF_TILE.getKey(), TFBlocks.WORN_CASTLE_BRICK.getKey())
			.add(TFBlocks.BLUE_CASTLE_RUNE_BRICK.getKey(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.getKey(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.getKey(), TFBlocks.PINK_CASTLE_RUNE_BRICK.getKey())
			.add(TFBlocks.PINK_FORCE_FIELD.getKey(), TFBlocks.ORANGE_FORCE_FIELD.getKey(), TFBlocks.GREEN_FORCE_FIELD.getKey(), TFBlocks.BLUE_FORCE_FIELD.getKey(), TFBlocks.VIOLET_FORCE_FIELD.getKey())
			.add(TFBlocks.BROWN_THORNS.getKey(), TFBlocks.GREEN_THORNS.getKey());

		this.tag(TFBlockTags.ANTIBUILDER_IGNORES).add(
			Blocks.REDSTONE_LAMP.builtInRegistryHolder().key(),
			Blocks.TNT.builtInRegistryHolder().key(),
			Blocks.WATER.builtInRegistryHolder().key(),
			TFBlocks.ANTIBUILDER.getKey(),
			TFBlocks.CARMINITE_BUILDER.getKey(),
			TFBlocks.BUILT_BLOCK.getKey(),
			TFBlocks.REACTOR_DEBRIS.getKey(),
			TFBlocks.CARMINITE_REACTOR.getKey(),
			TFBlocks.REAPPEARING_BLOCK.getKey(),
			TFBlocks.GHAST_TRAP.getKey(),
			TFBlocks.FAKE_DIAMOND.getKey(),
			TFBlocks.FAKE_GOLD.getKey()
		).addTag(TFBlockTags.COMMON_PROTECTIONS);//.addOptional(Identifier.parse("gravestone:gravestone"));

		this.tag(TFBlockTags.STRUCTURE_BANNED_INTERACTIONS).add(Blocks.LEVER.builtInRegistryHolder().key()).add(TFBlocks.ANTIBUILDER.getKey()).addTags(BlockTags.BUTTONS, Tags.Blocks.CHESTS);

		// TODO add more grave mods to this list
		this.tag(TFBlockTags.PROGRESSION_ALLOW_BREAKING)
			.add(TFBlocks.SKULL_CHEST.getKey())
			.add(TFBlocks.KEEPSAKE_CASKET.getKey());
			//.addOptional(Identifier.fromNamespaceAndPath("gravestone", "gravestone"))

		this.tag(TFBlockTags.CANNOT_TROLL_CAVE_HOLLOW)
			.add(Blocks.RED_MUSHROOM_BLOCK.builtInRegistryHolder().key())
			.add(Blocks.BROWN_MUSHROOM_BLOCK.builtInRegistryHolder().key())
			.add(TFBlocks.HUGE_MUSHGLOOM.getKey());

		this.tag(TFBlockTags.ORE_MAGNET_SAFE_REPLACE_BLOCK).addTags(
			BlockTags.DIRT,
			Tags.Blocks.GRAVELS,
			Tags.Blocks.SANDS,
			BlockTags.NYLIUM,
			BlockTags.BASE_STONE_OVERWORLD,
			BlockTags.BASE_STONE_NETHER,
			Tags.Blocks.END_STONES,
			BlockTags.DEEPSLATE_ORE_REPLACEABLES,
			BlockTags.STONE_ORE_REPLACEABLES,
			TFBlockTags.ROOT_GROUND
		);

		this.tag(TFBlockTags.MINING_CORE_EXCLUDED);

		this.tag(TFBlockTags.ROOT_GROUND).add(TFBlocks.ROOT_BLOCK.getKey());
		this.tag(TFBlockTags.ROOT_ORES).add(TFBlocks.LIVEROOT_BLOCK.getKey());

		this.tag(TFBlockTags.CLOUDS).add(TFBlocks.FLUFFY_CLOUD.getKey(), TFBlocks.WISPY_CLOUD.getKey(), TFBlocks.RAINY_CLOUD.getKey(), TFBlocks.SNOWY_CLOUD.getKey());

		this.tag(TFBlockTags.TF_CHESTS).add(
			TFBlocks.TWILIGHT_OAK_CHEST.getKey(),
			TFBlocks.CANOPY_CHEST.getKey(),
			TFBlocks.MANGROVE_CHEST.getKey(),
			TFBlocks.DARK_CHEST.getKey(),
			TFBlocks.TIME_CHEST.getKey(),
			TFBlocks.TRANSFORMATION_CHEST.getKey(),
			TFBlocks.MINING_CHEST.getKey(),
			TFBlocks.SORTING_CHEST.getKey());

		this.tag(BlockTags.DAMPENS_VIBRATIONS).addTag(TFBlockTags.CLOUDS).add(TFBlocks.ARCTIC_FUR_BLOCK.getKey());
		this.tag(BlockTags.OCCLUDES_VIBRATION_SIGNALS).add(TFBlocks.ARCTIC_FUR_BLOCK.getKey());

		this.tag(BlockTags.SUPPORTS_SMALL_DRIPLEAF).add(TFBlocks.UBEROUS_SOIL.getKey());

		this.tag(BlockTags.FEATURES_CANNOT_REPLACE).addTag(TFBlockTags.COMMON_PROTECTIONS).add(TFBlocks.LIVEROOT_BLOCK.getKey(), TFBlocks.MANGROVE_ROOT.getKey(), TFBlocks.SINISTER_SPAWNER.getKey());
		// For anything that permits replacement during Worldgen
		this.tag(TFBlockTags.WORLDGEN_REPLACEABLES).addTags(BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.REPLACEABLE_BY_TREES);

		this.tag(TFBlockTags.ROOT_TRACE_SKIP).addTag(BlockTags.LOGS).add(TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey(), TFBlocks.MANGROVE_ROOT.getKey(), TFBlocks.TIME_WOOD.getKey()).addTags(BlockTags.FEATURES_CANNOT_REPLACE);

		this.tag(TFBlockTags.DRUID_PROJECTILE_REPLACEABLE).addTags(BlockTags.LEAVES, BlockTags.LOGS, BlockTags.PLANKS, BlockTags.OVERWORLD_CARVER_REPLACEABLES, BlockTags.NETHER_CARVER_REPLACEABLES, BlockTags.REPLACEABLE_BY_TREES, BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.SCULK_REPLACEABLE, Tags.Blocks.ORES);

		this.tag(TFBlockTags.HUGE_MUSHGLOOM_PLACEABLE).addTag(BlockTags.SUBSTRATE_OVERWORLD).add(Blocks.MYCELIUM.builtInRegistryHolder().key()).add(Blocks.PODZOL.builtInRegistryHolder().key()).add(Blocks.CRIMSON_NYLIUM.builtInRegistryHolder().key()).add(Blocks.WARPED_NYLIUM.builtInRegistryHolder().key());

		this.tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(TFBlocks.TROLLSTEINN.getKey());

		this.tag(TFBlockTags.TIME_CORE_EXCLUDED).add(Blocks.NETHER_PORTAL.builtInRegistryHolder().key());

		this.tag(TFBlockTags.ORE_METER_TARGETABLE)
			.addTag(Tags.Blocks.ORES)
			.addTag(BlockTags.BASE_STONE_OVERWORLD)
			.addTag(BlockTags.BASE_STONE_NETHER)
			.addTag(BlockTags.DIRT)
			.addTag(Tags.Blocks.SANDS)
			.addTag(Tags.Blocks.SANDSTONE_BLOCKS)
			.addTag(BlockTags.TERRACOTTA)
			.addTag(Tags.Blocks.GRAVELS)
			.addTag(BlockTags.NYLIUM)
			.addTag(TFBlockTags.ROOT_ORES)
			.add(Blocks.BUDDING_AMETHYST.builtInRegistryHolder().key())
			.add(Blocks.CALCITE.builtInRegistryHolder().key())
			.add(Blocks.SOUL_SAND.builtInRegistryHolder().key())
			.add(Blocks.SOUL_SOIL.builtInRegistryHolder().key());

		this.tag(TFBlockTags.PENGUINS_SPAWNABLE_ON).addTag(BlockTags.ICE);
		this.tag(TFBlockTags.GIANTS_SPAWNABLE_ON).addTag(TFBlockTags.CLOUDS);

		this.tag(BlockTags.MINEABLE_WITH_AXE).add(
			TFBlocks.HEDGE.getKey(),
			TFBlocks.ROOT_BLOCK.getKey(),
			TFBlocks.LIVEROOT_BLOCK.getKey(),
			TFBlocks.MANGROVE_ROOT.getKey(),
			TFBlocks.UNCRAFTING_TABLE.getKey(),
			TFBlocks.ENCASED_SMOKER.getKey(),
			TFBlocks.ENCASED_FIRE_JET.getKey(),
			TFBlocks.TIME_LOG_CORE.getKey(),
			TFBlocks.TRANSFORMATION_LOG_CORE.getKey(),
			TFBlocks.MINING_LOG_CORE.getKey(),
			TFBlocks.SORTING_LOG_CORE.getKey(),
			TFBlocks.REAPPEARING_BLOCK.getKey(),
			TFBlocks.VANISHING_BLOCK.getKey(),
			TFBlocks.ANTIBUILDER.getKey(),
			TFBlocks.CARMINITE_REACTOR.getKey(),
			TFBlocks.CARMINITE_BUILDER.getKey(),
			TFBlocks.GHAST_TRAP.getKey(),
			TFBlocks.HUGE_STALK.getKey(),
			TFBlocks.HUGE_MUSHGLOOM.getKey(),
			TFBlocks.HUGE_MUSHGLOOM_STEM.getKey(),
			TFBlocks.CINDER_LOG.getKey(),
			TFBlocks.CINDER_WOOD.getKey(),
			TFBlocks.IRONWOOD_BLOCK.getKey(),
			TFBlocks.CHISELED_CANOPY_BOOKSHELF.getKey(),
			TFBlocks.CANOPY_BOOKSHELF.getKey(),
			TFBlocks.TWILIGHT_OAK_CHEST.getKey(),
			TFBlocks.CANOPY_CHEST.getKey(),
			TFBlocks.MANGROVE_CHEST.getKey(),
			TFBlocks.DARK_CHEST.getKey(),
			TFBlocks.TIME_CHEST.getKey(),
			TFBlocks.TRANSFORMATION_CHEST.getKey(),
			TFBlocks.MINING_CHEST.getKey(),
			TFBlocks.SORTING_CHEST.getKey(),
			TFBlocks.HUGE_LILY_PAD.getKey()
		).addTags(TFBlockTags.BANISTERS, TFBlockTags.HOLLOW_LOGS, TFBlockTags.TOWERWOOD);

		this.tag(BlockTags.MINEABLE_WITH_HOE).add(
			//vanilla doesnt use the leaves tag
			TFBlocks.TWILIGHT_OAK_LEAVES.getKey(),
			TFBlocks.CANOPY_LEAVES.getKey(),
			TFBlocks.MANGROVE_LEAVES.getKey(),
			TFBlocks.DARK_LEAVES.getKey(),
			TFBlocks.RAINBOW_OAK_LEAVES.getKey(),
			TFBlocks.TIME_LEAVES.getKey(),
			TFBlocks.TRANSFORMATION_LEAVES.getKey(),
			TFBlocks.MINING_LEAVES.getKey(),
			TFBlocks.SORTING_LEAVES.getKey(),
			TFBlocks.THORN_LEAVES.getKey(),
			TFBlocks.THORN_ROSE.getKey(),
			TFBlocks.BEANSTALK_LEAVES.getKey(),
			TFBlocks.STEELEAF_BLOCK.getKey(),
			TFBlocks.ARCTIC_FUR_BLOCK.getKey()
		);

		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
			TFBlocks.NAGASTONE.getKey(),
			TFBlocks.NAGASTONE_HEAD.getKey(),
			TFBlocks.STRONGHOLD_SHIELD.getKey(),
			TFBlocks.TROPHY_PEDESTAL.getKey(),
			TFBlocks.AURORA_PILLAR.getKey(),
			TFBlocks.AURORA_SLAB.getKey(),
			TFBlocks.UNDERBRICK.getKey(),
			TFBlocks.MOSSY_UNDERBRICK.getKey(),
			TFBlocks.CRACKED_UNDERBRICK.getKey(),
			TFBlocks.UNDERBRICK_FLOOR.getKey(),
			TFBlocks.TROLLSTEINN.getKey(),
			TFBlocks.GIANT_LEAVES.getKey(),
			TFBlocks.GIANT_OBSIDIAN.getKey(),
			TFBlocks.GIANT_COBBLESTONE.getKey(),
			TFBlocks.GIANT_LOG.getKey(),
			TFBlocks.CINDER_FURNACE.getKey(),
			TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.getKey(),
			TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.getKey(),
			TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.getKey(),
			//TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.getKey(),
			TFBlocks.KNIGHTMETAL_BLOCK.getKey(),
			TFBlocks.IRONWOOD_BLOCK.getKey(),
			TFBlocks.FIERY_BLOCK.getKey(),
			TFBlocks.CARMINITE_BLOCK.getKey(),
			TFBlocks.SPIRAL_BRICKS.getKey(),
			TFBlocks.ETCHED_NAGASTONE.getKey(),
			TFBlocks.NAGASTONE_PILLAR.getKey(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.MOSSY_ETCHED_NAGASTONE.getKey(),
			TFBlocks.MOSSY_NAGASTONE_PILLAR.getKey(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.CRACKED_ETCHED_NAGASTONE.getKey(),
			TFBlocks.CRACKED_NAGASTONE_PILLAR.getKey(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.IRON_LADDER.getKey(),
			TFBlocks.TWISTED_STONE.getKey(),
			TFBlocks.TWISTED_STONE_PILLAR.getKey(),
			TFBlocks.SKULL_CHEST.getKey(),
			TFBlocks.KEEPSAKE_CASKET.getKey(),
			TFBlocks.BOLD_STONE_PILLAR.getKey(),
			TFBlocks.TERRORCOTTA_CURVES.getKey(),
			TFBlocks.TERRORCOTTA_LINES.getKey(),
			TFBlocks.TERRORCOTTA_ARCS.getKey(),
			TFBlocks.SINISTER_SPAWNER.getKey()
		).addTags(TFBlockTags.MAZESTONE, TFBlockTags.CASTLE_BLOCKS, TFBlockTags.DEADROCK);

		this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
			TFBlocks.SMOKER.getKey(),
			TFBlocks.FIRE_JET.getKey(),
			TFBlocks.UBEROUS_SOIL.getKey()
		);

		this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(
			TFBlocks.NAGASTONE.getKey(),
			TFBlocks.NAGASTONE_HEAD.getKey(),
			TFBlocks.ETCHED_NAGASTONE.getKey(),
			TFBlocks.CRACKED_ETCHED_NAGASTONE.getKey(),
			TFBlocks.MOSSY_ETCHED_NAGASTONE.getKey(),
			TFBlocks.NAGASTONE_PILLAR.getKey(),
			TFBlocks.CRACKED_NAGASTONE_PILLAR.getKey(),
			TFBlocks.MOSSY_NAGASTONE_PILLAR.getKey(),
			TFBlocks.NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.getKey(),
			TFBlocks.NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.getKey(),
			TFBlocks.SPIRAL_BRICKS.getKey(),
			TFBlocks.TWISTED_STONE.getKey(),
			TFBlocks.TWISTED_STONE_PILLAR.getKey(),
			TFBlocks.BOLD_STONE_PILLAR.getKey(),
			TFBlocks.TERRORCOTTA_CURVES.getKey(),
			TFBlocks.TERRORCOTTA_LINES.getKey(),
			TFBlocks.TERRORCOTTA_ARCS.getKey(),
			TFBlocks.AURORA_PILLAR.getKey(),
			TFBlocks.AURORA_SLAB.getKey(),
			TFBlocks.TROLLSTEINN.getKey()
		);

		this.tag(BlockTags.NEEDS_STONE_TOOL).add(
			TFBlocks.UNDERBRICK.getKey(),
			TFBlocks.CRACKED_UNDERBRICK.getKey(),
			TFBlocks.MOSSY_UNDERBRICK.getKey(),
			TFBlocks.UNDERBRICK_FLOOR.getKey(),
			TFBlocks.IRON_LADDER.getKey()
		);

		this.tag(BlockTags.NEEDS_IRON_TOOL).add(
			TFBlocks.FIERY_BLOCK.getKey(),
			TFBlocks.KNIGHTMETAL_BLOCK.getKey()
		);

		this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(TFBlocks.AURORA_BLOCK.getKey()).addTags(TFBlockTags.CASTLE_BLOCKS, TFBlockTags.MAZESTONE, TFBlockTags.DEADROCK);

		this.tag(BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON).add(TFBlocks.UBEROUS_SOIL.getKey());
		this.tag(BlockTags.HUGE_RED_MUSHROOM_CAN_PLACE_ON).add(TFBlocks.UBEROUS_SOIL.getKey());

		this.tag(BlockTags.MOSS_REPLACEABLE).add(TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey(), TFBlocks.TROLLSTEINN.getKey());

		this.tag(BlockTags.INVALID_SPAWN_INSIDE).add(TFBlocks.TWILIGHT_PORTAL.getKey());

		this.tag(Tags.Blocks.RELOCATION_NOT_SUPPORTED).add(TFBlocks.TWILIGHT_PORTAL.getKey(), TFBlocks.STRONGHOLD_SHIELD.getKey(),
			TFBlocks.TIME_LOG_CORE.getKey(), TFBlocks.TRANSFORMATION_LOG_CORE.getKey(),
			TFBlocks.MINING_LOG_CORE.getKey(), TFBlocks.SORTING_LOG_CORE.getKey(),
			TFBlocks.ANTIBUILDER.getKey(), TFBlocks.BUILT_BLOCK.getKey(),
			TFBlocks.FAKE_DIAMOND.getKey(), TFBlocks.FAKE_GOLD.getKey(),
			TFBlocks.REACTOR_DEBRIS.getKey(), TFBlocks.LOCKED_VANISHING_BLOCK.getKey(), TFBlocks.VANISHING_BLOCK.getKey(),
			TFBlocks.UNBREAKABLE_VANISHING_BLOCK.getKey(), TFBlocks.REAPPEARING_BLOCK.getKey(),
			TFBlocks.BEANSTALK_GROWER.getKey(), TFBlocks.GIANT_COBBLESTONE.getKey(),
			TFBlocks.GIANT_LOG.getKey(), TFBlocks.GIANT_LEAVES.getKey(),
			TFBlocks.GIANT_OBSIDIAN.getKey(), TFBlocks.BROWN_THORNS.getKey(),
			TFBlocks.GREEN_THORNS.getKey(), TFBlocks.BURNT_THORNS.getKey(),
			TFBlocks.PINK_FORCE_FIELD.getKey(), TFBlocks.ORANGE_FORCE_FIELD.getKey(),
			TFBlocks.GREEN_FORCE_FIELD.getKey(), TFBlocks.BLUE_FORCE_FIELD.getKey(),
			TFBlocks.VIOLET_FORCE_FIELD.getKey(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.getKey(),
			TFBlocks.NAGA_BOSS_SPAWNER.getKey(), TFBlocks.LICH_BOSS_SPAWNER.getKey(),
			TFBlocks.MINOSHROOM_BOSS_SPAWNER.getKey(), TFBlocks.HYDRA_BOSS_SPAWNER.getKey(),
			TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.getKey(), TFBlocks.UR_GHAST_BOSS_SPAWNER.getKey(),
			TFBlocks.ALPHA_YETI_BOSS_SPAWNER.getKey(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.getKey());

		this.tag(TFBlockTags.SUPPORTS_STALAGMITES).addTag(TFBlockTags.DEADROCK).add(Blocks.PACKED_ICE.builtInRegistryHolder().key());

		this.tag(TFBlockTags.CARVER_REPLACEABLES).addTag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(Blocks.SNOW_BLOCK.builtInRegistryHolder().key());

		this.tag(TFBlockTags.INCORRECT_FOR_IRONWOOD_TOOL).addTag(BlockTags.INCORRECT_FOR_IRON_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_FIERY_TOOL).addTag(BlockTags.INCORRECT_FOR_NETHERITE_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_STEELEAF_TOOL).addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_KNIGHTMETAL_TOOL).addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_GIANT_TOOL).addTag(BlockTags.INCORRECT_FOR_STONE_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_ICE_TOOL).addTag(BlockTags.INCORRECT_FOR_WOODEN_TOOL);
		this.tag(TFBlockTags.INCORRECT_FOR_GLASS_TOOL).addTag(BlockTags.INCORRECT_FOR_WOODEN_TOOL);

		this.tag(Tags.Blocks.GLASS_BLOCKS).add(TFBlocks.AURORALIZED_GLASS.getKey());
		this.tag(Tags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES).add(TFBlocks.UNCRAFTING_TABLE.getKey());
		this.tag(Tags.Blocks.ROPES).add(TFBlocks.ROPE.getKey());

		this.tag(TFBlockTags.MINEABLE_WITH_BLOCK_AND_CHAIN).addTags(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE,
			BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_HOE);

		this.tag(TFBlockTags.BLOCK_AND_CHAIN_NEVER_BREAKS).addTags(TFBlockTags.MAZESTONE, TFBlockTags.CASTLE_BLOCKS, TFBlockTags.DEADROCK, BlockTags.WITHER_IMMUNE)
			.add(TFBlocks.TIME_LOG_CORE.getKey(), TFBlocks.TRANSFORMATION_LOG_CORE.getKey(), TFBlocks.MINING_LOG_CORE.getKey(), TFBlocks.SORTING_LOG_CORE.getKey())
			.add(TFBlocks.GIANT_OBSIDIAN.getKey());

		this.tag(TFBlockTags.SMALL_LAKES_DONT_REPLACE).addTags(BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.LOGS, BlockTags.LEAVES)
			.add(TFBlocks.ROOT_BLOCK.getKey(), TFBlocks.LIVEROOT_BLOCK.getKey(), Blocks.MUSHROOM_STEM.builtInRegistryHolder().key());

		this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
			.add(TFBlocks.HUGE_LILY_PAD.getKey());

		this.tag(BlockTags.SWORD_EFFICIENT)
			.add(TFBlocks.HUGE_LILY_PAD.getKey());

		this.tag(Tags.Blocks.BOOKSHELVES)
			.add(TFBlocks.CANOPY_BOOKSHELF.getKey());

		this.tag(BlockTags.WOOL_CARPETS)
			.add(TFBlocks.CORONATION_CARPET.getKey());

		this.tag(BlockTags.FIRE)
			.add(TFBlocks.OMINOUS_FIRE.getKey());
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Tags";
	}
}