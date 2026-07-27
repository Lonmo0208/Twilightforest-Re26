package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import twilightforest.init.TFBannerPatterns;
import twilightforest.tags.TFBannerPatternTags;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

public class BannerPatternTagGenerator extends FabricTagsProvider<BannerPattern> {

	public BannerPatternTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.BANNER_PATTERN, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.builder(TFBannerPatternTags.NAGA_BANNER_PATTERN).add(TFBannerPatterns.NAGA);
		this.builder(TFBannerPatternTags.LICH_BANNER_PATTERN).add(TFBannerPatterns.LICH);
		this.builder(TFBannerPatternTags.MINOSHROOM_BANNER_PATTERN).add(TFBannerPatterns.MINOSHROOM);
		this.builder(TFBannerPatternTags.HYDRA_BANNER_PATTERN).add(TFBannerPatterns.HYDRA);
		this.builder(TFBannerPatternTags.KNIGHT_PHANTOM_BANNER_PATTERN).add(TFBannerPatterns.KNIGHT_PHANTOM);
		this.builder(TFBannerPatternTags.UR_GHAST_BANNER_PATTERN).add(TFBannerPatterns.UR_GHAST);
		this.builder(TFBannerPatternTags.ALPHA_YETI_BANNER_PATTERN).add(TFBannerPatterns.ALPHA_YETI);
		this.builder(TFBannerPatternTags.SNOW_QUEEN_BANNER_PATTERN).add(TFBannerPatterns.SNOW_QUEEN);
		this.builder(TFBannerPatternTags.QUESTING_RAM_BANNER_PATTERN).add(TFBannerPatterns.QUESTING_RAM);
	}

	@Override
	public String getName() {
		return "Twilight Forest Banner Pattern Tags";
	}
}