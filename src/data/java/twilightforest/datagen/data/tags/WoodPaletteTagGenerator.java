package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import twilightforest.TFRegistries;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.tags.TFWoodPaletteTags;
import twilightforest.util.woods.WoodPalette;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

public class WoodPaletteTagGenerator extends FabricTagsProvider<WoodPalette> {

	public WoodPaletteTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, TFRegistries.Keys.WOOD_PALETTES, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.builder(TFWoodPaletteTags.WELL_SWIZZLE_MASK).add(WoodPalettes.OAK);
		this.builder(TFWoodPaletteTags.DRUID_HUT_SWIZZLE_MASK).add(WoodPalettes.OAK, WoodPalettes.SPRUCE, WoodPalettes.BIRCH);

		this.builder(TFWoodPaletteTags.COMMON_PALETTES).add(WoodPalettes.SPRUCE, WoodPalettes.CANOPY);
		this.builder(TFWoodPaletteTags.UNCOMMON_PALETTES).add(WoodPalettes.OAK, WoodPalettes.DARKWOOD, WoodPalettes.TWILIGHT_OAK);
		this.builder(TFWoodPaletteTags.RARE_PALETTES).add(WoodPalettes.BIRCH, WoodPalettes.JUNGLE, WoodPalettes.MANGROVE);
		this.builder(TFWoodPaletteTags.TREASURE_PALETTES).add(WoodPalettes.TIMEWOOD, WoodPalettes.TRANSWOOD, WoodPalettes.MINEWOOD, WoodPalettes.SORTWOOD);
	}

	@Override
	public String getName() {
		return "Twilight Forest Wood Palette Tags";
	}
}