package twilightforest.client.model.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.resources.Identifier;

/**
 * Unbaked block state model for {@link ReactorDebrisModel}.
 * Wraps the standard block model with dynamic particle texture selection.
 */
public record UnbakedReactorDebrisBlockStateModel(Identifier modelId)
	implements CustomUnbakedBlockStateModel {

	public static final MapCodec<UnbakedReactorDebrisBlockStateModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
			Identifier.CODEC.fieldOf("model").forGetter(UnbakedReactorDebrisBlockStateModel::modelId)
		).apply(instance, UnbakedReactorDebrisBlockStateModel::new)
	);

	@Override
	public BlockStateModel bake(ModelBaker baker) {
		return new ReactorDebrisModel(new SingleVariant(SimpleModelWrapper.bake(baker, this.modelId, BlockModelRotation.IDENTITY)));
	}

	@Override
	public void resolveDependencies(Resolver resolver) {
		resolver.markDependency(this.modelId);
	}

	// TODO: Port to Fabric - codec() return type was CustomUnbakedBlockStateModel (NeoForge-specific)
	@Override
	public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		return MAP_CODEC;
	}
}