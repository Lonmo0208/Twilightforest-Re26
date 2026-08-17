package twilightforest.client.model.block.forcefield;

import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import org.joml.Vector3fc;
import net.minecraft.util.context.ContextMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

// TODO: Port to Fabric - Previously extended AbstractUnbakedModel and implemented CustomUnbakedBlockStateModel (NeoForge-specific)
public class UnbakedForceFieldModel implements UnbakedModel {

	private static final ModelDebugName DEBUG_NAME = () -> "twilightforest:force_field";

	private final Map<CuboidModelElement, ForceFieldModelLoader.Condition> elementsAndConditions;
	private final TextureSlots.Data textureSlots;

	public UnbakedForceFieldModel(Map<CuboidModelElement, ForceFieldModelLoader.Condition> elementsAndConditions, TextureSlots.Data textureSlots) {
		this.elementsAndConditions = elementsAndConditions;
		this.textureSlots = textureSlots;
	}

	@Override
	public TextureSlots.Data textureSlots() {
		return this.textureSlots;
	}

	// Fabric bake entry point (called when this model is baked directly, e.g. by SimpleModelWrapper fallback paths).
	// The level-aware connection logic is handled by UnbakedForceFieldBlockStateModel.bake() -> bakeInternal(),
	// so here we bake with the texture slots parsed from the JSON "textures" map.
	public BlockStateModel bake(ModelBaker baker) {
		TextureSlots resolvedSlots = new TextureSlots.Resolver().addLast(this.textureSlots).resolve(DEBUG_NAME);
		return bakeInternal(resolvedSlots, baker, BlockModelRotation.IDENTITY, true, true, null, null);
	}

	public BlockStateModel bakeInternal(TextureSlots textures, ModelBaker baker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties) {
		return new ForceFieldModel(this.elementsAndConditions, textures, baker, modelState, useAmbientOcclusion, usesBlockLight, itemTransforms);
	}

	private static CuboidFace.UVs computeDefaultUVs(Vector3fc from, Vector3fc to, Direction facing) {
		return switch (facing) {
			case DOWN -> new CuboidFace.UVs(from.x(), 16.0F - to.z(), to.x(), 16.0F - from.z());
			case UP -> new CuboidFace.UVs(from.x(), from.z(), to.x(), to.z());
			case NORTH -> new CuboidFace.UVs(16.0F - to.x(), 16.0F - to.y(), 16.0F - from.x(), 16.0F - from.y());
			case SOUTH -> new CuboidFace.UVs(from.x(), 16.0F - to.y(), to.x(), 16.0F - from.y());
			case WEST -> new CuboidFace.UVs(from.z(), 16.0F - to.y(), to.z(), 16.0F - from.y());
			case EAST -> new CuboidFace.UVs(16.0F - to.z(), 16.0F - to.y(), 16.0F - from.z(), 16.0F - from.y());
		};
	}

	@Nullable
	public UnbakedGeometry geometry() {
		return (textureSlots, baker, state, name) -> {
			QuadCollection.Builder builder = new QuadCollection.Builder();
			ModelState modelState = BlockModelRotation.IDENTITY;

			for (Map.Entry<CuboidModelElement, ForceFieldModelLoader.Condition> entry : this.elementsAndConditions.entrySet()) {
				CuboidModelElement element = entry.getKey();

				for (Direction side : Direction.values()) {
					CuboidFace face = element.faces().get(side);
					if (face == null) continue;

					CuboidFace.UVs uvs;
					if (face.uvs() != null) {
						uvs = new CuboidFace.UVs(face.uvs().minU(), face.uvs().minV(), face.uvs().maxU(), face.uvs().maxV());
					} else {
						uvs = computeDefaultUVs(element.from(), element.to(), side);
					}

					CuboidFace cuboidFace = new CuboidFace(
						face.cullForDirection(),
						face.tintIndex(),
						face.texture(),
						uvs,
						face.rotation()
					);

					Material material = textureSlots.getMaterial(face.texture());
					if (material == null) continue;
					Material.Baked baked = baker.materials().get(material, name);

					BakedQuad quad = FaceBakery.bakeQuad(
						baker, element.from(), element.to(), cuboidFace, baked,
						side, modelState, element.rotation(), element.shadeDirectionOverride(), element.lightEmission()
					);

					Direction cullDir = face.cullForDirection();
					if (cullDir != null) {
						builder.addCulledFace(cullDir, quad);
					} else {
						builder.addUnculledFace(quad);
					}
				}
			}

			return builder.build();
		};
	}

	// TODO: Port to Fabric - codec() is not part of BlockStateModel.Unbaked in Fabric; kept for compatibility
	public com.mojang.serialization.MapCodec<? extends BlockStateModel.Unbaked> codec() {
		throw new UnsupportedOperationException("UnbakedForceFieldModel does not support codec serialization");
	}
}
