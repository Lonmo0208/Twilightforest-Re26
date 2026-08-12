package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;
import twilightforest.util.RotationUtil;

public class SmartGrassProcessor implements StructureProcessor {
	public static final SmartGrassProcessor INSTANCE = new SmartGrassProcessor();
	public static final MapCodec<SmartGrassProcessor> MAP_CODEC = MapCodec.unit(() -> INSTANCE);

	private SmartGrassProcessor() {
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos origin, BlockPos centerBottom, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings) {
		if (modifiedBlockInfo.state().getBlock() != Blocks.GRASS_BLOCK)
			return modifiedBlockInfo;

		BlockPos worldPos = modifiedBlockInfo.pos();
		// During world generation the level is a WorldGenRegion whose bounds only cover the chunk
		// being generated plus a small border. Querying a block outside those bounds throws
		// IllegalStateException in 26.2, so skip processing near the region edge.
		if (!level.hasChunkAt(worldPos) || !level.hasChunkAt(worldPos.above()))
			return modifiedBlockInfo;

		if (level.getBlockState(worldPos).is(BlockTags.DIRT) || !level.isEmptyBlock(worldPos.above()))
			return null;

		for (Direction direction : RotationUtil.CARDINALS) {
			BlockPos neighbor = worldPos.relative(direction);
			if (!level.hasChunkAt(neighbor)) continue;
			BlockState stateAt = level.getBlockState(neighbor);

			if (stateAt.getBlock() == Blocks.PODZOL) return new StructureTemplate.StructureBlockInfo(worldPos, Blocks.PODZOL.defaultBlockState(), null);
			if (stateAt.getBlock() == Blocks.GRASS_BLOCK) return modifiedBlockInfo;
			if (stateAt.getBlock() == Blocks.MYCELIUM) return new StructureTemplate.StructureBlockInfo(worldPos, Blocks.MYCELIUM.defaultBlockState(), null);
			if (stateAt.getBlock() == Blocks.DIRT_PATH) return new StructureTemplate.StructureBlockInfo(worldPos, Blocks.DIRT_PATH.defaultBlockState(), null);
			if (stateAt.getBlock() == Blocks.COARSE_DIRT) return new StructureTemplate.StructureBlockInfo(worldPos, Blocks.COARSE_DIRT.defaultBlockState(), null);
			if (stateAt.getBlock() == TFBlocks.UBEROUS_SOIL) return new StructureTemplate.StructureBlockInfo(worldPos, TFBlocks.UBEROUS_SOIL.defaultBlockState(), null);
		}

		return modifiedBlockInfo;
	}

	@Override
	public MapCodec<? extends StructureProcessor> codec() {
		return MAP_CODEC;
	}
}
