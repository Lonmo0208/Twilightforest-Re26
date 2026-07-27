package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.decoration.painting.PaintingVariants;
import twilightforest.tags.TFPaintingVariantTags;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

public class PaintingVariantTagGenerator extends FabricTagsProvider<PaintingVariant> {

	public PaintingVariantTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.PAINTING_VARIANT, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		// Every single painting except for Humble
		this.builder(TFPaintingVariantTags.LICH_TOWER_PAINTINGS).add(
			PaintingVariants.KEBAB,
			PaintingVariants.AZTEC,
			PaintingVariants.ALBAN,
			PaintingVariants.AZTEC2,
			PaintingVariants.BOMB,
			PaintingVariants.PLANT,
			PaintingVariants.WASTELAND,
			PaintingVariants.POOL,
			PaintingVariants.COURBET,
			PaintingVariants.SEA,
			PaintingVariants.SUNSET,
			PaintingVariants.CREEBET,
			PaintingVariants.WANDERER,
			PaintingVariants.GRAHAM,
			PaintingVariants.MATCH,
			PaintingVariants.BUST,
			PaintingVariants.STAGE,
			PaintingVariants.VOID,
			PaintingVariants.SKULL_AND_ROSES,
			PaintingVariants.WITHER,
			PaintingVariants.FIGHTERS,
			PaintingVariants.POINTER,
			PaintingVariants.PIGSCENE,
			PaintingVariants.BURNING_SKULL,
			PaintingVariants.SKELETON,
			PaintingVariants.DONKEY_KONG
		);

		this.builder(TFPaintingVariantTags.LICH_BOSS_PAINTINGS).add(
			PaintingVariants.KEBAB,
			PaintingVariants.AZTEC,
			PaintingVariants.ALBAN,
			PaintingVariants.AZTEC2,
			PaintingVariants.BOMB,
			PaintingVariants.PLANT,
			PaintingVariants.WASTELAND,
			PaintingVariants.POOL,
			PaintingVariants.COURBET,
			PaintingVariants.SEA,
			PaintingVariants.SUNSET,
			PaintingVariants.CREEBET,
			PaintingVariants.WANDERER,
			PaintingVariants.GRAHAM,
			PaintingVariants.MATCH,
			PaintingVariants.BUST,
			PaintingVariants.STAGE,
			PaintingVariants.VOID,
			PaintingVariants.SKULL_AND_ROSES,
			PaintingVariants.WITHER,
			PaintingVariants.FIGHTERS,
			PaintingVariants.POINTER,
			PaintingVariants.PIGSCENE,
			PaintingVariants.BURNING_SKULL,
			PaintingVariants.SKELETON,
			PaintingVariants.DONKEY_KONG
		);
	}

	@Override
	public String getName() {
		return "Twilight Forest Painting Variant Tags";
	}
}