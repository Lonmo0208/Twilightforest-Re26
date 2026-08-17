package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import twilightforest.world.components.feature.config.WeightedListFeatureConfig;

import java.util.Optional;

/**
 * While seemingly similar to RandomFeatureConfiguration, this uses a weighted list and a single random value sample.
 * The goal is to produced pseudorandom result distribution that better match expectations defined by a weighted list.
 */
public class WeightedListFeature implements Feature {

	private final WeightedListFeatureConfig config;

	public WeightedListFeature(WeightedListFeatureConfig config) {
		this.config = config;
	}

	public WeightedListFeature() {
		this(new WeightedListFeatureConfig(WeightedList.of()));
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return MapCodec.unit(this);
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
		Optional<Holder<PlacedFeature>> randomFeature = this.config.getRandomFeature(random);

		//noinspection OptionalIsPresent
		if (randomFeature.isEmpty())
			return false;

		return randomFeature.get().value().place(level, chunkGenerator, random, pos);
	}
}
