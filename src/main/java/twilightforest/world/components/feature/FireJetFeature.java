package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeatureUtil;

public class FireJetFeature implements Feature {

	public static final MapCodec<FireJetFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC.fieldOf("state").forGetter(f -> f.state)
	).apply(instance, FireJetFeature::new));

	private final BlockState state;

	public FireJetFeature(BlockState state) {
		this.state = state;
	}

	public FireJetFeature() {
		this(TFBlocks.FIRE_JET.defaultBlockState());
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return CODEC;
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
		if (!FeatureUtil.isAreaSuitable(level, pos, 5, 2, 5)) return false;

		for (int i = 0; i < 4; ++i) {
			BlockPos dPos = pos.offset(
				random.nextInt(8) - random.nextInt(8),
				random.nextInt(4) - random.nextInt(4),
				random.nextInt(8) - random.nextInt(8)
			);

			if (level.isEmptyBlock(dPos) && level.canSeeSkyFromBelowWater(dPos) && level.getBlockState(dPos.below()).is(BlockTags.DIRT)
				&& level.getBlockState(dPos.east().below()).is(BlockTags.DIRT) && level.getBlockState(dPos.west().below()).is(BlockTags.DIRT)
				&& level.getBlockState(dPos.south().below()).is(BlockTags.DIRT) && level.getBlockState(dPos.north().below()).is(BlockTags.DIRT)) {

				//create blocks around the jet/smoker, just in case
				for (int gx = -2; gx <= 2; gx++) {
					for (int gz = -2; gz <= 2; gz++) {
						BlockPos grassPos = dPos.offset(gx, -1, gz);
						level.setBlock(grassPos, Blocks.GRASS_BLOCK.defaultBlockState(), 0);
					}
				}

				// jet
				level.setBlock(dPos.below(), this.state, 0);

				// create reservoir with stone walls
				for (int rx = -2; rx <= 2; rx++) {
					for (int rz = -2; rz <= 2; rz++) {
						BlockPos dPos2 = dPos.offset(rx, -2, rz);
						if ((rx == 1 || rx == 0 || rx == -1) && (rz == 1 || rz == 0 || rz == -1)) {
							// lava reservoir
							level.setBlock(dPos2, Blocks.LAVA.defaultBlockState(), 0);
						} else if (!level.getBlockState(dPos2).is(Blocks.LAVA)) {
							// only stone where there is no lava
							level.setBlock(dPos2, Blocks.STONE.defaultBlockState(), 0);
						}
						level.setBlock(dPos2.below(), Blocks.STONE.defaultBlockState(), 0);
					}
				}
			}
		}

		return true;
	}
}
