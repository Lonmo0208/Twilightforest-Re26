package twilightforest.client.model.item;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TravellersGearItemModel implements ItemModel {

	private static final ModelDebugName DEBUG_NAME = () -> "TravellersGearItemModel";

	private final ItemModel baseModel;
	private final Identifier modifierDirectory;
	private final BakingContext bakingContext;
	private final Matrix4fc transformation;
	private final ItemTransforms itemTransforms;
	private final Map<String, ItemModel> possibleCombos = Maps.newHashMap();

	private TravellersGearItemModel(ItemModel baseModel, Identifier modifierDirectory,
	                                BakingContext bakingContext, Matrix4fc transformation) {
		this.baseModel = baseModel;
		this.modifierDirectory = modifierDirectory;
		this.bakingContext = bakingContext;
		this.transformation = transformation;

		var baseItemModel = bakingContext.blockModelBaker()
			.getModel(Identifier.withDefaultNamespace("item/generated"));
		this.itemTransforms = baseItemModel.getTopTransforms();
	}

	@Override
	public void update(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver,
	                   ItemDisplayContext context, @Nullable ClientLevel level,
	                   @Nullable ItemOwner owner, int seed) {
		this.baseModel.update(state, stack, resolver, context, level, owner, seed);

		if (stack.has(TFDataComponents.IS_TRAVELLERS_GEAR) && level != null) {
			List<Holder<TravellersModifier>> modifiers =
				TravellersModifiersManager.findAllInsertableModifiers(level, stack);
			if (!modifiers.isEmpty()) {
				String key = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath()
					+ this.getModifiersSuffix(modifiers);
				this.possibleCombos.computeIfAbsent(key, k ->
					TravellersGearItemModel.this.getModifiedGear(modifiers)
				).update(state, stack, resolver, context, level, owner, seed);
			}
		}
	}

	private ItemModel getModifiedGear(List<Holder<TravellersModifier>> modifiers) {
		ModelBaker baker = this.bakingContext.blockModelBaker();
		MaterialBaker materials = baker.materials();
		List<ItemModel> modelLayers = new ArrayList<>();
		int layerIndex = 0;

		for (Holder<TravellersModifier> modifier : modifiers) {
			Material.Baked modSprite = this.getModifierSprite(modifier.unwrapKey().get(), materials);
			if (!modSprite.sprite().contents().name().equals(MissingTextureAtlasSprite.getLocation())) {
				QuadCollection overlayQuads = ItemModelLayerHelper.computeItemLayer(
					baker, modSprite, BlockModelRotation.IDENTITY, layerIndex
				);
				boolean usesBlockLight = modSprite.sprite().atlasLocation() == TextureAtlas.LOCATION_BLOCKS;
				modelLayers.add(new SimpleItemModel(overlayQuads, modSprite, this.itemTransforms, this.transformation, usesBlockLight));
				layerIndex++;
			}
		}
		return new CompositeModel(modelLayers);
	}

	private String getModifiersSuffix(List<Holder<TravellersModifier>> modifiers) {
		StringBuilder ret = new StringBuilder();
		for (var mod : modifiers) {
			ret.append("_").append(mod.unwrapKey().get().identifier().getPath());
		}
		return ret.toString();
	}

	private Material.Baked getModifierSprite(ResourceKey<TravellersModifier> modifierKey,
	                                         MaterialBaker baker) {
		Identifier spriteId = modifierKey.identifier()
			.withPath(p -> "item/" + this.modifierDirectory.getPath() + "/" + p);
		return baker.get(new Material(spriteId), DEBUG_NAME);
	}

	/**
	 * Simple ItemModel implementation that renders a QuadCollection with given properties.
	 */
	private record SimpleItemModel(QuadCollection quads, Material.Baked particleMaterial,
	                               ItemTransforms itemTransforms, Matrix4fc localTransform,
	                               boolean usesBlockLight) implements ItemModel {

		@Override
		public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver,
		                   ItemDisplayContext displayContext, @Nullable ClientLevel level,
		                   @Nullable ItemOwner owner, int seed) {
			if (this.quads.getAll().isEmpty()) return;

			output.appendModelIdentityElement(this);
			ItemStackRenderState.LayerRenderState layer = output.newLayer();
			layer.setUsesBlockLight(this.usesBlockLight);
			layer.setParticleMaterial(this.particleMaterial);
			layer.setItemTransform(this.itemTransforms.getTransform(displayContext));
			layer.setLocalTransform(this.localTransform);
			layer.setQuads(ItemQuads.split(this.quads.getAll()));

			if (this.quads.hasMaterialFlag(2)) {
				output.setAnimated();
			}
		}
	}

	public record Unbaked(ItemModel.Unbaked baseModel, Identifier modifierDirectory)
		implements ItemModel.Unbaked {

		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
				ItemModels.CODEC.fieldOf("base_model").forGetter(Unbaked::baseModel),
				Identifier.CODEC.fieldOf("modifier_directory").forGetter(Unbaked::modifierDirectory)
			).apply(instance, Unbaked::new)
		);

		@Override
		public ItemModel bake(BakingContext context, Matrix4fc transformation) {
			return new TravellersGearItemModel(
				this.baseModel().bake(context, transformation),
				this.modifierDirectory(),
				context,
				transformation
			);
		}

		@Override
		public void resolveDependencies(Resolver resolver) {
			this.baseModel().resolveDependencies(resolver);
		}

		@Override
		public MapCodec<? extends ItemModel.Unbaked> type() {
			return MAP_CODEC;
		}
	}
}