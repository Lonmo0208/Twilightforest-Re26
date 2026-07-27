package twilightforest.datagen.data.tags.compat;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EntityType;
import twilightforest.init.TFEntities;
import twilightforest.tags.TFEntityTypeTags;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

public class ModdedEntityTypeTagGenerator extends FabricTagsProvider.EntityTypeTagsProvider {

	public ModdedEntityTypeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.valueLookupBuilder(TFEntityTypeTags.AC_RESISTS_ACID).add(TFEntities.HYDRA.get(), TFEntities.NAGA.get());
		this.valueLookupBuilder(TFEntityTypeTags.AC_RESISTS_MAGNETS).addTag(TFEntityTypeTags.BOSSES);
		this.valueLookupBuilder(TFEntityTypeTags.AC_RESISTS_TREMORSAURUS_ROAR).add(TFEntities.HYDRA.get(), TFEntities.UR_GHAST.get());

		this.valueLookupBuilder(TFEntityTypeTags.AETHER_DEFLECTABLE_PROJECTILES).add(
			TFEntities.NATURE_BOLT.get(),
			TFEntities.LICH_BOLT.get(),
			TFEntities.WAND_BOLT.get(),
			TFEntities.SLIME_BLOB.get(),
			TFEntities.ICE_SNOWBALL.get());

		this.valueLookupBuilder(TFEntityTypeTags.AETHER_FIRE_MOB).add(TFEntities.FIRE_BEETLE.get());
		this.valueLookupBuilder(TFEntityTypeTags.AETHER_PIGS).add(TFEntities.BOAR.get());

		this.valueLookupBuilder(TFEntityTypeTags.AN_JAR_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);
		this.valueLookupBuilder(TFEntityTypeTags.AN_JAR_RELEASE_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);

		this.valueLookupBuilder(TFEntityTypeTags.IE_SHADER_BLACKLIST).addTag(TFEntityTypeTags.BOSSES);
	}
}