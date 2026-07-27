package twilightforest.datagen.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

public class ModdedBlockTagGenerator extends FabricTagsProvider.BlockTagsProvider {

	public ModdedBlockTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, future);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.valueLookupBuilder(TFBlockTags.AC_FERROMAGNETIC_BLOCKS).addTag(TFBlockTags.STORAGE_BLOCKS_IRONWOOD).addTag(TFBlockTags.STORAGE_BLOCKS_STEELEAF).addTag(TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.CANDELABRA).add(TFBlocks.WROUGHT_IRON_FENCE);
		this.valueLookupBuilder(TFBlockTags.AC_GLOOMOTH_LIGHT_SOURCES).add(TFBlocks.FIREFLY_SPAWNER, TFBlocks.FIREFLY_JAR);
		this.valueLookupBuilder(TFBlockTags.AC_UNDERZEALOT_LIGHT_SOURCES).add(TFBlocks.FIREFLY, TFBlocks.MOONWORM);

		this.valueLookupBuilder(TFBlockTags.ARTIFACTS_CAMPSITE_CHESTS).addTag(TFBlockTags.TF_CHESTS);

		this.valueLookupBuilder(TFBlockTags.FD_COMPOST_ACTIVATORS).add(TFBlocks.UBEROUS_SOIL, TFBlocks.MUSHGLOOM);
		this.valueLookupBuilder(TFBlockTags.FD_HEAT_SOURCES).addTag(TFBlockTags.STORAGE_BLOCKS_FIERY);
	}
}