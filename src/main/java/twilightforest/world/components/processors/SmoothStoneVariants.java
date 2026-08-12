package twilightforest.world.components.processors;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.util.features.FeaturePlacers;

public class SmoothStoneVariants implements StructureProcessor {
	public static final SmoothStoneVariants INSTANCE = new SmoothStoneVariants();
	public static final MapCodec<SmoothStoneVariants> MAP_CODEC = MapCodec.unit(() -> INSTANCE);

	private SmoothStoneVariants() {
	}

	@Nullable

	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos origin, BlockPos centerBottom, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings) {
		RandomSource random = settings.getRandom(modifiedBlockInfo.pos());

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 4);

		if (modifiedBlockInfo.state().is(Blocks.SMOOTH_STONE_SLAB) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(modifiedBlockInfo.state(), Blocks.COBBLESTONE_SLAB), null);

		if (modifiedBlockInfo.state().is(Blocks.SMOOTH_STONE) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), Blocks.COBBLESTONE.defaultBlockState(), null);

		return modifiedBlockInfo;
	}

	@Nullable

	@Override
	public MapCodec<? extends StructureProcessor> codec() {
		return MAP_CODEC;
	}
}
