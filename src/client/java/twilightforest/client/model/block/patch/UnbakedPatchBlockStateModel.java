package twilightforest.client.model.block.patch;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;

public record UnbakedPatchBlockStateModel(Identifier modelId) implements CustomUnbakedBlockStateModel {

	public static final MapCodec<UnbakedPatchBlockStateModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Identifier.CODEC.fieldOf("model").forGetter(UnbakedPatchBlockStateModel::modelId)
	).apply(instance, UnbakedPatchBlockStateModel::new));

	@Override
	public BlockStateModel bake(ModelBaker baker) {
		ResolvedModel resolved = baker.getModel(this.modelId);
		UnbakedModel wrapped = resolved.wrapped();
		if (wrapped instanceof UnbakedPatchModel patch) {
			return patch.bakeInternal(resolved.getTopTextureSlots(), baker, BlockModelRotation.IDENTITY, true, true, null, null);
		}
		return new SingleVariant(SimpleModelWrapper.bake(baker, this.modelId, BlockModelRotation.IDENTITY));
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