package twilightforest.block;

import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFConfiguredFeatures;
import twilightforest.init.TFStructures;
import twilightforest.world.components.structures.TreeGrowerStartable;

import java.util.Optional;

public class HollowOakSaplingBlock extends SaplingBlock {

	public HollowOakSaplingBlock(BlockBehaviour.Properties properties) {
		// The TreeGrower is never used: advanceTree() is fully overridden to grow the HOLLOW_TREE structure instead
		super(new TreeGrower("hollow_oak", WeightedList.of(TFConfiguredFeatures.TWILIGHT_OAK_TREE), WeightedList.of(), WeightedList.of(), TFConfiguredFeatures.TWILIGHT_OAK_TREE), properties);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(BlockTags.SUPPORTS_VEGETATION) || state.is(TFBlocks.UBEROUS_SOIL);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, BonemealSource source) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state, BonemealSource source) {
		return level.getRandom().nextFloat() < 0.45F;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, BonemealSource source) {
		TwilightForestMod.LOGGER.debug("[HollowOak] performBonemeal @ pos={} stage={}", pos, state.getValue(STAGE));
		this.advanceTree(level, pos, state, random);
	}

	@Override
	public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
		int stage = state.getValue(STAGE);
		TwilightForestMod.LOGGER.debug("[HollowOak] advanceTree entered @ pos={} current stage={}", pos, stage);
		if (stage == 0) {
			BlockState newState = state.cycle(STAGE);
			boolean updated = level.setBlock(pos, newState, 260);
			TwilightForestMod.LOGGER.debug("[HollowOak] advanceTree stage 0->1 @ pos={} setBlock ok={} newStage={}",
				pos, updated, newState.getValue(STAGE));
		} else {
			ChunkGenerator generator = level.getChunkSource().getGenerator();
			Holder.Reference<Structure> structureHolder = level.registryAccess().lookupOrThrow(Registries.STRUCTURE).getOrThrow(TFStructures.HOLLOW_TREE);
			Structure structure = structureHolder.value();
			TwilightForestMod.LOGGER.debug("[HollowOak] advanceTree @ pos={} unwrapped Structure class={}",
				pos, structure.getClass().getName());

			if (!(structure instanceof TreeGrowerStartable treeGrowerStartable)) {
				TwilightForestMod.LOGGER.debug("[HollowOak] advanceTree @ pos={} aborted: Structure is not TreeGrowerStartable (actual={})",
					pos, structure.getClass().getName());
				return;
			}

			if (!treeGrowerStartable.checkSaplingClearance(level, pos)) {
				TwilightForestMod.LOGGER.debug("[HollowOak] advanceTree @ pos={} aborted: checkSaplingClearance returned false (see DEBUG logs above for blocking block)",
					pos);
				return;
			}
			TwilightForestMod.LOGGER.debug("[HollowOak] advanceTree @ pos={} checkSaplingClearance passed, generating structure...", pos);

			StructureStart structurestart = treeGrowerStartable.generateFromSapling(level.registryAccess(), generator, generator.getBiomeSource(), level.getChunkSource().randomState(), level.getServer().getStructureTemplateManager(), level.getSeed(), pos, level);

			if (!structurestart.isValid()) {
				TwilightForestMod.LOGGER.debug("[HollowOak] advanceTree @ pos={} aborted: structurestart.isValid()=false StructureStart={}",
					pos, structurestart);
				return;
			}

			BoundingBox boundingbox = structurestart.getBoundingBox();
			ChunkPos start = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
			ChunkPos end = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));

			if (ChunkPos.rangeClosed(start, end).noneMatch(currentChunkPos -> level.isLoaded(currentChunkPos.getWorldPosition()))) {
				TwilightForestMod.LOGGER.debug("[HollowOak] advanceTree @ pos={} aborted: chunks in range not all loaded startChunk={} endChunk={} BoundingBox={}",
					pos, start, end, boundingbox);
				return;
			}

			TwilightForestMod.LOGGER.debug("[HollowOak] advanceTree @ pos={} all checks passed, placing HollowTree! BoundingBox={}",
				pos, boundingbox);

			level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);

			ChunkPos.rangeClosed(start, end).forEach(
				chunkPos -> structurestart.placeInChunk(
					level,
					level.structureManager(),
					generator,
					level.getRandom(),
					new BoundingBox(
						chunkPos.getMinBlockX(),
						level.getMinY(),
						chunkPos.getMinBlockZ(),
						chunkPos.getMaxBlockX(),
						level.getMaxY(),
						chunkPos.getMaxBlockZ()
					),
					chunkPos
				)
			);

			TwilightForestMod.LOGGER.debug("[HollowOak] advanceTree @ pos={} HollowTree placement done!", pos);
		}
	}
}
