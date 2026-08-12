package twilightforest.world.components.processors;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public final class TargetedRotProcessor extends BlockRotProcessor {
	public static final MapCodec<TargetedRotProcessor> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC.listOf().xmap(ImmutableSet::copyOf, ArrayList::new).fieldOf("blocks_to_rot").forGetter(p -> p.blocksToRot),
		Codec.FLOAT.fieldOf("integrity").orElse(1.0f).forGetter(p -> p.integrity)
	).apply(instance, TargetedRotProcessor::new));

	private final ImmutableSet<BlockState> blocksToRot;

	public TargetedRotProcessor(ImmutableSet<BlockState> blocksToRot, float integrity) {
		super(integrity);
		this.blocksToRot = blocksToRot;
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos origin, BlockPos centerBottom, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings) {
		if (!this.blocksToRot.contains(modifiedBlockInfo.state())) return modifiedBlockInfo;
		return super.processBlock(level, origin, centerBottom, templateRelativePos, modifiedBlockInfo, settings);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MapCodec<BlockRotProcessor> codec() {
		return (MapCodec<BlockRotProcessor>) (MapCodec<?>) MAP_CODEC;
	}
}