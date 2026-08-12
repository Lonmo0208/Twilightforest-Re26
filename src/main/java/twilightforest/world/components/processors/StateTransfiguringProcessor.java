package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import twilightforest.util.features.FeaturePlacers;

import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

public class StateTransfiguringProcessor implements StructureProcessor {
	public static final MapCodec<StateTransfiguringProcessor> MAP_CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules").xmap(StateTransfiguringProcessor::new, p -> p.rules);
	private final List<ProcessorRule> rules;

	public StateTransfiguringProcessor(List<? extends ProcessorRule> rules) {
		this.rules = Collections.unmodifiableList(rules);
	}

	private static final ThreadLocal<XoroshiroRandomSource> REUSABLE_RANDOM = ThreadLocal.withInitial(() -> new XoroshiroRandomSource(0L));

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos origin, BlockPos centerBottom, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings) {
		XoroshiroRandomSource random = REUSABLE_RANDOM.get();
		random.setSeed(Mth.getSeed(modifiedBlockInfo.pos()));
		long i = random.nextLong();
		for (ProcessorRule processorRule : this.rules) {
			random.setSeed(i * 3);
			i += 115;

			if (processorRule.test(level, modifiedBlockInfo.state(), templateRelativePos, modifiedBlockInfo.pos(), centerBottom, random))
				return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(modifiedBlockInfo.state(), processorRule.getOutputState()), processorRule.getOutputTag(random, modifiedBlockInfo.nbt()));
		}

		return modifiedBlockInfo;
	}

	@Override
	public MapCodec<? extends StructureProcessor> codec() {
		return MAP_CODEC;
	}
}