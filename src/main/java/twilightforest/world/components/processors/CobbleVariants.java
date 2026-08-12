package twilightforest.world.components.processors;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.util.features.FeaturePlacers;

public final class CobbleVariants implements StructureProcessor {
	public static final CobbleVariants INSTANCE = new CobbleVariants();
	public static final MapCodec<CobbleVariants> MAP_CODEC = MapCodec.unit(() -> INSTANCE);

	private CobbleVariants() {
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldReaderIn, BlockPos pos, BlockPos piecepos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings) {
		RandomSource random = settings.getRandom(modifiedBlockInfo.pos());

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 2);

		BlockState state = modifiedBlockInfo.state();
		Block block = state.getBlock();

		if (block == Blocks.COBBLESTONE && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), null);

		if (block == Blocks.COBBLESTONE_STAIRS && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_STAIRS), null);

		if (block == Blocks.COBBLESTONE_SLAB && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_SLAB), null);

		if (block == Blocks.COBBLESTONE_WALL && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(state, Blocks.MOSSY_COBBLESTONE_WALL), null);

		return modifiedBlockInfo;
	}

	@Override
	public MapCodec<? extends StructureProcessor> codec() {
		return MAP_CODEC;
	}
}
