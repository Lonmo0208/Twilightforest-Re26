package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.init.TFBlockEntities;
import twilightforest.tags.TFBlockEntityTypeTags;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

public class BlockEntityTypeTagGenerator extends FabricTagsProvider.BlockEntityTypeTagsProvider {

	public BlockEntityTypeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.valueLookupBuilder(TFBlockEntityTypeTags.RELOCATION_NOT_SUPPORTED).add(
			TFBlockEntities.ANTIBUILDER,
			TFBlockEntities.BEANSTALK_GROWER,
			TFBlockEntities.NAGA_SPAWNER,
			TFBlockEntities.LICH_SPAWNER,
			TFBlockEntities.MINOSHROOM_SPAWNER,
			TFBlockEntities.HYDRA_SPAWNER,
			TFBlockEntities.KNIGHT_PHANTOM_SPAWNER,
			TFBlockEntities.UR_GHAST_SPAWNER,
			TFBlockEntities.ALPHA_YETI_SPAWNER,
			TFBlockEntities.SNOW_QUEEN_SPAWNER,
			TFBlockEntities.FINAL_BOSS_SPAWNER);

		this.valueLookupBuilder(TFBlockEntityTypeTags.IMMOVABLE).add(
			TFBlockEntities.ANTIBUILDER,
			TFBlockEntities.BEANSTALK_GROWER,
			TFBlockEntities.NAGA_SPAWNER,
			TFBlockEntities.LICH_SPAWNER,
			TFBlockEntities.MINOSHROOM_SPAWNER,
			TFBlockEntities.HYDRA_SPAWNER,
			TFBlockEntities.KNIGHT_PHANTOM_SPAWNER,
			TFBlockEntities.UR_GHAST_SPAWNER,
			TFBlockEntities.ALPHA_YETI_SPAWNER,
			TFBlockEntities.SNOW_QUEEN_SPAWNER,
			TFBlockEntities.FINAL_BOSS_SPAWNER);
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Entity Tags";
	}
}