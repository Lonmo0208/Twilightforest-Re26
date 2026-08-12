package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.tags.TFBlockTags;

public final class SoftReplaceProcessor implements StructureProcessor {
	public static final SoftReplaceProcessor INSTANCE = new SoftReplaceProcessor();
	public static final MapCodec<SoftReplaceProcessor> MAP_CODEC = MapCodec.unit(INSTANCE);

	private SoftReplaceProcessor() {
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos piecePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo modifiedInfo, StructurePlaceSettings placeSettings) {
		BlockState blockAt = level.getBlockState(modifiedInfo.pos());

		boolean isReplaceableAt = blockAt.canBeReplaced() || blockAt.is(TFBlockTags.WORLDGEN_REPLACEABLES);

		if (isReplaceableAt) {
			return modifiedInfo;
		}

		// Replace partial blocks such as slabs or fences, if the replacement is a solid block
		if (!this.isFullBlock(blockAt) && this.isFullBlock(modifiedInfo.state())) {
			return modifiedInfo;
		}

		return null;
	}

	@Override
	public MapCodec<? extends StructureProcessor> codec() {
		return MAP_CODEC;
	}

	private boolean isFullBlock(BlockState state) {
		Block block = state.getBlock();
		return !(block instanceof FenceBlock || block instanceof WallBlock || block instanceof SlabBlock || block instanceof StairBlock
			|| block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock
			|| block instanceof IronBarsBlock || block instanceof StainedGlassBlock);
	}
}
