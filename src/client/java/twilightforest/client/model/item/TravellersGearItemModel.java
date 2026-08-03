package twilightforest.client.model.item;

import com.google.common.collect.Maps;
import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.cuboid.ItemModelGenerator;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
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
// TODO: Port to Fabric - ComposedModelState is NeoForge-specific; use Fabric equivalent
// TODO: Port to Fabric - ComposedModelState is NeoForge-specific
// import net.neoforged.neoforge.client.model.ComposedModelState;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TravellersGearItemModel implements ItemModel {

	private static final Function<Float, Transformation> TRANSFORM = new Function<>() {
		@Override
		public Transformation apply(Float f) {
			return new Transformation(null, null, new Vector3f(1.0F + f), null);
		}
	};
	private static final ModelDebugName DEBUG_NAME = new ModelDebugName() {
		@Override
		public String debugName() {
			return "TravellersGearItemModel";
		}
	};

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
			List<Holder.Reference<TravellersModifier>> modifiers =
				TravellersModifiersManager.findAllInsertableModifiers(level, stack);
			if (!modifiers.isEmpty()) {
				String key = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath()
					+ this.getModifiersSuffix(modifiers);
				this.possibleCombos.computeIfAbsent(key, new Function<>() {
					@Override
					public ItemModel apply(String _key) {
						return TravellersGearItemModel.this.getModifiedGear(modifiers);
					}
				})
					.update(state, stack, resolver, context, level, owner, seed);
			}
		}
	}

	private ItemModel getModifiedGear(List<Holder.Reference<TravellersModifier>> modifiers) {
		ModelBaker baker = this.bakingContext.blockModelBaker();
		MaterialBaker materials = baker.materials();
		List<ItemModel> modelLayers = new ArrayList<>();
		int layers = 1;

		for (Holder.Reference<TravellersModifier> modifier : modifiers) {
			Material.Baked modSprite = this.getModifierSprite(modifier.key(), materials);
			if (!modSprite.sprite().contents().name().equals(MissingTextureAtlasSprite.getLocation())) {
				ModelRenderProperties overlayRenderProps =
					new ModelRenderProperties(false, modSprite, this.itemTransforms);
				// TODO: Port to Fabric - ComposedModelState is NeoForge-specific; use Fabric equivalent
				// QuadCollection overlayQuads = baker.compute(
				// 	new ItemModelGenerator.ItemLayerKey(modSprite,
				// 		new ComposedModelState(BlockModelRotation.IDENTITY,
				// 			TRANSFORM.apply(layers * 0.001F)), layers)
				// );
				// TODO: Port to Fabric - CuboidItemModelWrapper has private constructor in 26.1.2
			// Need to use builder/unbaked API. Skipping empty overlay for now.
			// QuadCollection overlayQuads = baker.compute(...);
			// modelLayers.add(new CuboidItemModelWrapper(List.of(), overlayQuads, overlayRenderProps, this.transformation));
			// layers++;
			}
		}
		return new CompositeModel(modelLayers);
	}

	private String getModifiersSuffix(List<Holder.Reference<TravellersModifier>> modifiers) {
		StringBuilder ret = new StringBuilder();
		for (var mod : modifiers) {
			ret.append("_").append(mod.key().identifier().getPath());
		}
		return ret.toString();
	}

	private Material.Baked getModifierSprite(ResourceKey<TravellersModifier> modifier,
	                                         MaterialBaker baker) {
		Identifier spriteId = modifier.identifier()
			.withPath(p -> "item/" + this.modifierDirectory.getPath() + "/" + p);
		return baker.get(new Material(spriteId), DEBUG_NAME);
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