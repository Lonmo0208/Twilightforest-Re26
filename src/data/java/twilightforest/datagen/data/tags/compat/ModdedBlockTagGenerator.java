package twilightforest.datagen.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;

import java.util.concurrent.CompletableFuture;

public class ModdedBlockTagGenerator extends TagsProvider<Block> {

	public ModdedBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, Registries.BLOCK, future, TwilightForestMod.ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(TFBlockTags.AC_FERROMAGNETIC_BLOCKS).addTag(TFBlockTags.STORAGE_BLOCKS_IRONWOOD).addTag(TFBlockTags.STORAGE_BLOCKS_STEELEAF).addTag(TFBlockTags.STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.CANDELABRA.getKey()).add(TFBlocks.WROUGHT_IRON_FENCE.getKey());
		this.tag(TFBlockTags.AC_GLOOMOTH_LIGHT_SOURCES).add(TFBlocks.FIREFLY_SPAWNER.getKey(), TFBlocks.FIREFLY_JAR.getKey());
		this.tag(TFBlockTags.AC_UNDERZEALOT_LIGHT_SOURCES).add(TFBlocks.FIREFLY.getKey(), TFBlocks.MOONWORM.getKey());

		this.tag(TFBlockTags.ARTIFACTS_CAMPSITE_CHESTS).addTag(TFBlockTags.TF_CHESTS);

		this.tag(TFBlockTags.FD_COMPOST_ACTIVATORS).add(TFBlocks.UBEROUS_SOIL.getKey(), TFBlocks.MUSHGLOOM.getKey());
		this.tag(TFBlockTags.FD_HEAT_SOURCES).addTag(TFBlockTags.STORAGE_BLOCKS_FIERY);
	}
}
