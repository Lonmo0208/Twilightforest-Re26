package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import twilightforest.init.TFBlocks;

public class LampostFeature implements Feature {

	public static final MapCodec<LampostFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC.fieldOf("state").forGetter(f -> f.state)
	).apply(instance, LampostFeature::new));

	private static final Rotation[] ROTATIONS = Rotation.values();
	private final BlockState state;

	public LampostFeature(BlockState state) {
		this.state = state;
	}

	public LampostFeature() {
		this(TFBlocks.CICADA_JAR.defaultBlockState());
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return CODEC;
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
		// we should start on a grass block
		if (level.getBlockState(pos.below()).getBlock() != Blocks.GRASS_BLOCK) {
			return false;
		}

		// generate a height
		int height = 1 + random.nextInt(4);

		// is it air or replaceable above our grass block
		for (int dy = 0; dy <= height; dy++) {
			BlockState state = level.getBlockState(pos.above(dy));
			if (!state.isAir() && !state.canBeReplaced()) {
				return false;
			}
		}

		// generate lamp
		for (int dy = 0; dy < height; dy++) {
			level.setBlock(pos.above(dy), TFBlocks.CANOPY_FENCE.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
		}
		level.setBlock(pos.above(height), this.state.rotate(ROTATIONS[random.nextInt(ROTATIONS.length)]), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
		return true;
	}
}
