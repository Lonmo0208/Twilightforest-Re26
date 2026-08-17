package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.chunk.ChunkGenerator;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeatureUtil;
import twilightforest.world.components.feature.config.RuinedFoundationConfig;

public class FoundationFeature implements Feature {

	public static final MapCodec<FoundationFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		RuinedFoundationConfig.RuinedFoundationDimensions.CODEC.forGetter(f -> f.dimensions),
		RuinedFoundationConfig.RuinedFoundationBlocks.CODEC.forGetter(f -> f.blocks),
		ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(f -> f.lootTable)
	).apply(instance, FoundationFeature::new));

	private final RuinedFoundationConfig.RuinedFoundationDimensions dimensions;
	private final RuinedFoundationConfig.RuinedFoundationBlocks blocks;
	private final ResourceKey<LootTable> lootTable;

	public FoundationFeature(RuinedFoundationConfig.RuinedFoundationDimensions dimensions, RuinedFoundationConfig.RuinedFoundationBlocks blocks, ResourceKey<LootTable> lootTable) {
		this.dimensions = dimensions;
		this.blocks = blocks;
		this.lootTable = lootTable;
	}

	public FoundationFeature() {
		this(RuinedFoundationConfig.withDefaultBlocks(false));
	}

	private FoundationFeature(RuinedFoundationConfig config) {
		this(config.dimensions(), config.blocks(), config.lootTable());
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return CODEC;
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
		IntProvider wallWidths = this.dimensions.wallWidth();
		int xWidth = wallWidths.sample(random);
		int zWidth = wallWidths.sample(random);

		if (!FeatureUtil.isAreaSuitable(level, pos.offset(1, 0, 1), xWidth - 1, 4, zWidth - 1)) {
			return false;
		}

		//okay!
		generateFoundation(level, random, pos, xWidth, zWidth, this.dimensions.wallHeights(), this.dimensions.placeFloorTest(), this.blocks.wallBlock(), this.blocks.wallTop(), this.blocks.decayedWall(), this.blocks.decayedTop(), this.blocks.floor());

		//TODO: chimney?

		int basementDepth = this.dimensions.basementHeight().sample(random);
		if (basementDepth > 0) {
			BlockPos basementCeilingPos = pos.offset(1, -3, 1);
			generateBasement(xWidth - 2, zWidth - 2, basementDepth, level, basementCeilingPos, random, this.dimensions.placeFloorTest(), this.blocks.floor(), this.blocks.basementPosts(), this.blocks.lootContainer(), this.lootTable);
		}

		return true;
	}

	private static void generateFoundation(WorldGenLevel world, RandomSource rand, BlockPos origin, int xWidth, int zWidth, IntProvider wallHeights, FloatProvider placeFloorTest, BlockStateProvider wallBlock, BlockStateProvider wallTop, BlockStateProvider decayedWall, BlockStateProvider decayedTop, BlockStateProvider floor) {
		for (int dX = 0; dX <= xWidth; dX++) {
			for (int dZ = 0; dZ <= zWidth; dZ++) {
				// stone on the edges
				Rotation wallRotation = FeatureLogic.wallVolumeRotation(rand, dX, dZ, xWidth, zWidth);
				if (wallRotation != null) {
					int height = wallHeights.sample(rand);

					for (int yBlock = 0; yBlock < height; yBlock++) {
						BlockPos placeAt = origin.offset(dX, yBlock - 1, dZ);
						setWallBlock(world, rand, wallBlock, decayedWall, yBlock, placeAt, wallRotation);
					}

					setWallBlock(world, rand, wallTop, decayedTop, height, origin.offset(dX, height - 1, dZ), wallRotation);
				} else if (placeFloorTest.sample(rand) <= 0) {
					// destroyed wooden plank floor
					setAndUpdate(world, rand, floor, origin.offset(dX, -1, dZ));
				}
			}
		}
	}

	private static void setWallBlock(WorldGenLevel world, RandomSource rand, BlockStateProvider main, BlockStateProvider decay, int yBlock, BlockPos placeAt, Rotation rotation) {
		setAndUpdate(world, rand, rollDecay(rand, yBlock, main, decay), placeAt, rotation);
	}

	public static BlockStateProvider rollDecay(RandomSource rand, int decayRarity, BlockStateProvider main, BlockStateProvider decay) {
		return rand.nextInt(decayRarity + 1) >= 1 ? main : decay;
	}

	private static void generateBasement(int xWidth, int zWidth, int depth, WorldGenLevel world, BlockPos ceilingPos, RandomSource rand, FloatProvider placeFloorTest, BlockStateProvider floor, BlockStateProvider basementPost, BlockStateProvider lootContainer, ResourceKey<LootTable> lootTable) {
		if (xWidth < 1 || zWidth < 1 || depth < 1) return;

		int chestX = rollChestCoord(xWidth, rand);
		int chestZ = rollChestCoord(zWidth, rand);

		// clear basement
		for (int dX = 0; dX <= xWidth; dX++) {
			for (int dZ = 0; dZ <= zWidth; dZ++) {
				int cornerOverlap = 0;
				if (dX == 0) cornerOverlap++;
				if (dZ == 0) cornerOverlap++;
				if (dX == xWidth) cornerOverlap++;
				if (dZ == zWidth) cornerOverlap++;

				boolean isInCorner = cornerOverlap > 1;

				for (int dY = 1 - depth; dY <= 0; dY++) {
					BlockPos placeAt = ceilingPos.offset(dX, dY, dZ);
					world.setBlock(placeAt, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
					if (isInCorner) setAndUpdate(world, rand, basementPost, placeAt);
				}

				if ((dX == chestX && dZ == chestZ) || (cornerOverlap == 0 && placeFloorTest.sample(rand) <= 0)) {
					// destroyed wooden plank floor, placed by chance or because a chest is going to generate above it
					setAndUpdate(world, rand, floor, ceilingPos.offset(dX, -depth, dZ));
				}
			}
		}

		// make chest
		BlockPos lootPos = ceilingPos.offset(chestX, 1 - depth, chestZ);
		world.setBlock(lootPos, lootContainer.getState(world, rand, lootPos), Block.UPDATE_ALL);
		if (world.getBlockEntity(lootPos) instanceof RandomizableContainerBlockEntity lootBE) {
			lootBE.setLootTable(lootTable, world.getSeed() * lootPos.getX() + lootPos.getY() ^ lootPos.getZ());
		}
	}

	private static int rollChestCoord(int width, RandomSource rand) {
		if (width < 3) // No room to not be on an edge
			return rand.nextInt(Math.max(0, width) + 1);

		return rand.nextInt(Math.max(0, width - 1) + 1) + 1;
	}

	private static void setAndUpdate(WorldGenLevel world, RandomSource rand, BlockStateProvider floor, BlockPos placeAt) {
		setAndUpdate(world, rand, floor, placeAt, Rotation.NONE);
	}

	private static void setAndUpdate(WorldGenLevel world, RandomSource rand, BlockStateProvider floor, BlockPos placeAt, Rotation rotation) {
		BlockState state = floor.getState(world, rand, placeAt).rotate(rotation);

		if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
			boolean hasWaterOrAbove = world.getFluidState(placeAt).is(FluidTags.WATER) || world.getFluidState(placeAt.above()).is(FluidTags.WATER);
			if (hasWaterOrAbove)
				state = state.setValue(BlockStateProperties.WATERLOGGED, true);
		}

		world.setBlock(placeAt, state, Block.UPDATE_ALL);

		world.getChunk(placeAt).markPosForPostProcessing(placeAt);
	}
}
