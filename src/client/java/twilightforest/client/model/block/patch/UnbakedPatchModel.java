package twilightforest.client.model.block.patch;

import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.util.context.ContextMap;
import org.jetbrains.annotations.Nullable;

public class UnbakedPatchModel implements UnbakedModel {

	private static final ModelDebugName DEBUG_NAME = () -> "twilightforest:patch";

	private final boolean shaggify;
	private final TextureSlots.Data textureSlots;

	public UnbakedPatchModel(boolean shaggify, TextureSlots.Data textureSlots) {
		this.shaggify = shaggify;
		this.textureSlots = textureSlots;
	}

	@Override
	public TextureSlots.Data textureSlots() {
		return this.textureSlots;
	}

	// Fabric bake entry point (used when this model is baked directly, e.g. by SimpleModelWrapper fallback paths).
	// The level-aware collectParts logic is handled by UnbakedPatchBlockStateModel.bake() -> bakeInternal().
	public BlockStateModel bake(ModelBaker baker) {
		TextureSlots resolvedSlots = new TextureSlots.Resolver().addLast(this.textureSlots).resolve(DEBUG_NAME);
		return bakeInternal(resolvedSlots, baker, BlockModelRotation.IDENTITY, true, true, null, null);
	}

	public BlockStateModel bakeInternal(TextureSlots textures, ModelBaker baker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties) {
		// The incoming textures are already resolved TextureSlots - use them directly for material lookups.
		Material textureMaterial = textures.getMaterial("texture");
		Material particleMaterial = textures.getMaterial("particle");

		// Fallback: if not found in incoming textures, resolve our own slot definitions
		if (textureMaterial == null || particleMaterial == null) {
			TextureSlots ourResolved = new TextureSlots.Resolver().addLast(this.textureSlots).resolve(DEBUG_NAME);
			if (textureMaterial == null) {
				textureMaterial = ourResolved.getMaterial("texture");
			}
			if (particleMaterial == null) {
				particleMaterial = ourResolved.getMaterial("particle");
			}
		}

		Material.Baked texture = textureMaterial != null ? baker.materials().get(textureMaterial, DEBUG_NAME) : null;
		Material.Baked particle = particleMaterial != null ? baker.materials().get(particleMaterial, DEBUG_NAME) : texture;

		return new PatchModel(texture, this.shaggify, particle, useAmbientOcclusion, usesBlockLight, itemTransforms);
	}

	@Nullable
	public ItemTransforms transforms() {
		return null;
	}
}
