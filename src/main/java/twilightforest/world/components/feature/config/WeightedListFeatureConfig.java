package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Optional;
import java.util.stream.Stream;

public class WeightedListFeatureConfig  {
	public static final Codec<WeightedListFeatureConfig> CODEC = WeightedList.codec(PlacedFeature.CODEC).xmap(WeightedListFeatureConfig::new, c -> c.randomFeatures);

	private final WeightedList<Holder<PlacedFeature>> randomFeatures;

	public WeightedListFeatureConfig(WeightedList<Holder<PlacedFeature>> randomFeatures) {
		this.randomFeatures = randomFeatures;
	}

	public Optional<Holder<PlacedFeature>> getRandomFeature(RandomSource random) {
		return this.randomFeatures.getRandom(random);
	}

	public Stream<Holder<Feature>> getSubFeatures() {
		return this.randomFeatures.unwrap()
			.stream()
			.map(Weighted::value)
			.map(Holder::value)
			.map(PlacedFeature::feature)
			.map(Holder::value)
			.flatMap(Feature::getSubFeatures);
	}
}
