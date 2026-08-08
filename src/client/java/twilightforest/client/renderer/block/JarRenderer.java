package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricModelManager;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.BlockStateModelWrapper;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.registries.BuiltInRegistries;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;
import twilightforest.beanification.Configurable;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.client.state.block.JarRenderState;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;
import twilightforest.init.TFBlocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JarRenderer<T extends JarBlockEntity> implements BlockEntityRenderer<T, JarRenderState> {
	public static final Map<Item, Identifier> LID_KEYS = new HashMap<>();
	public static final Map<Item, ExtraModelKey<BlockStateModel>> LID_MODEL_KEYS = new HashMap<>();
	public static final Map<Item, BlockStateModel> LIDS = new HashMap<>();

	public static final ResourceManagerReloadListener LID_MODEL_CACHE_RELOAD_LISTENER = new ResourceManagerReloadListener() {
		@Override
		public void onResourceManagerReload(ResourceManager manager) {
			LIDS.clear();
			FabricModelManager fmm = (FabricModelManager) Minecraft.getInstance().getModelManager();
			for (LidResource lidResource : LID_LOCATION_LIST) {
				ExtraModelKey<BlockStateModel> key = LID_MODEL_KEYS.get(lidResource.lid());
				if (key != null) {
					BlockStateModel model = fmm.getModel(key);
					if (model != null) {
						LIDS.put(lidResource.lid(), model);
					}
				}
			}
		}
	};

	public record LidResource(Item lid, Identifier identifier, @Nullable String customPath) {
		public LidResource(Item lid) {
			this(lid, lid == null ? Identifier.withDefaultNamespace("air") : BuiltInRegistries.ITEM.getKey(lid), null);
		}

		public LidResource(Item item, String path) {
			this(item, Identifier.fromNamespaceAndPath("minecraft", path), null);
		}

		public LidResource(Item item, String path, String customPath) {
			this(item, Identifier.fromNamespaceAndPath("minecraft", path), customPath);
		}

		public Identifier getModelId() {
			String name = this.identifier.getPath();
			if (this.customPath() != null) name = this.customPath();
			return TwilightForestMod.prefix("block/lid/" + name);
		}
	}

	public static final List<LidResource> LID_LOCATION_LIST = List.of(
		new LidResource(TFBlocks.MANGROVE_LOG.asItem()),
		new LidResource(TFBlocks.CANOPY_LOG.asItem()),
		new LidResource(TFBlocks.DARK_LOG.asItem()),
		new LidResource(TFBlocks.MINING_LOG.asItem()),
		new LidResource(TFBlocks.SORTING_LOG.asItem()),
		new LidResource(TFBlocks.TIME_LOG.asItem()),
		new LidResource(TFBlocks.TRANSFORMATION_LOG.asItem()),
		new LidResource(TFBlocks.TWILIGHT_OAK_LOG.asItem()),
		new LidResource(Items.ACACIA_LOG, "acacia_log"),
		new LidResource(Items.BIRCH_LOG, "birch_log"),
		new LidResource(Items.CHERRY_LOG, "cherry_log"),
		new LidResource(Items.DARK_OAK_LOG, "dark_oak_log"),
		new LidResource(Items.JUNGLE_LOG, "jungle_log"),
		new LidResource(Items.MANGROVE_LOG, "mangrove_log", "vanilla_mangrove_log"),
		new LidResource(Items.OAK_LOG, "oak_log"),
		new LidResource(Items.SPRUCE_LOG, "spruce_log"),
		new LidResource(Items.CRIMSON_STEM, "crimson_stem"),
		new LidResource(Items.WARPED_STEM, "warped_stem"),
		new LidResource(Items.PALE_OAK_LOG, "pale_oak_log"),
		new LidResource(TFBlocks.STRIPPED_MANGROVE_LOG.asItem()),
		new LidResource(TFBlocks.STRIPPED_CANOPY_LOG.asItem()),
		new LidResource(TFBlocks.STRIPPED_DARK_LOG.asItem()),
		new LidResource(TFBlocks.STRIPPED_MINING_LOG.asItem()),
		new LidResource(TFBlocks.STRIPPED_SORTING_LOG.asItem()),
		new LidResource(TFBlocks.STRIPPED_TIME_LOG.asItem()),
		new LidResource(TFBlocks.STRIPPED_TRANSFORMATION_LOG.asItem()),
		new LidResource(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.asItem()),
		new LidResource(Items.STRIPPED_ACACIA_LOG, "stripped_acacia_log"),
		new LidResource(Items.STRIPPED_BIRCH_LOG, "stripped_birch_log"),
		new LidResource(Items.STRIPPED_CHERRY_LOG, "stripped_cherry_log"),
		new LidResource(Items.STRIPPED_DARK_OAK_LOG, "stripped_dark_oak_log"),
		new LidResource(Items.STRIPPED_JUNGLE_LOG, "stripped_jungle_log"),
		new LidResource(Items.STRIPPED_MANGROVE_LOG, "stripped_mangrove_log", "vanilla_stripped_mangrove_log"),
		new LidResource(Items.STRIPPED_OAK_LOG, "stripped_oak_log"),
		new LidResource(Items.STRIPPED_SPRUCE_LOG, "stripped_spruce_log"),
		new LidResource(Items.STRIPPED_CRIMSON_STEM, "stripped_crimson_stem"),
		new LidResource(Items.STRIPPED_WARPED_STEM, "stripped_warped_stem"),
		new LidResource(Items.STRIPPED_PALE_OAK_LOG, "stripped_pale_oak_log"),
		new LidResource(TFBlocks.CINDER_LOG.asItem()),
		new LidResource(Items.PUMPKIN, "pumpkin"),
		new LidResource(Items.BAMBOO_BLOCK, "bamboo_block"),
		new LidResource(Items.STRIPPED_BAMBOO_BLOCK, "stripped_bamboo_block")
	);

	protected final BlockModelResolver blockResolver;
	protected static final float WOBBLE_AMPLITUDE = 0.125F;

	public JarRenderer(BlockEntityRendererProvider.Context context) {
		this.blockResolver = context.blockModelResolver();
	}

	public static void populateLidKeys() {
		if (!LID_KEYS.isEmpty()) return;
		for (LidResource lidResource : LID_LOCATION_LIST) {
			LID_KEYS.put(lidResource.lid(), lidResource.getModelId());
		}
	}

	public static void registerReloadListener() {
		ResourceLoader.get(PackType.CLIENT_RESOURCES)
			.registerReloadListener(TwilightForestMod.prefix("lid_model_cache"), LID_MODEL_CACHE_RELOAD_LISTENER);
	}

	public static @Nullable BlockStateModel getLidModel(Item lid) {
		if (lid == null) return null;
		// Fast path: ResourceManagerReloadListener already cached the model.
		BlockStateModel cached = LIDS.get(lid);
		if (cached != null) return cached;
		// Fallback: Reload listener may not have fired yet, or the LID_MODEL_KEYS
		// was populated after models were baked. Look up directly from the
		// FabricModelManager using the registered ExtraModelKey.
		ExtraModelKey<BlockStateModel> key = LID_MODEL_KEYS.get(lid);
		if (key == null) return null;
		FabricModelManager fmm = (FabricModelManager) Minecraft.getInstance().getModelManager();
		BlockStateModel model = fmm.getModel(key);
		if (model != null) {
			LIDS.put(lid, model);
		}
		return model;
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public JarRenderState createRenderState() {
		return new JarRenderState();
	}

	@Override
	public void extractRenderState(T blockEntity, JarRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPosition, breakProgress);

		BlockState blockState = blockEntity.getBlockState();

		this.blockResolver.update(state.jarModel, blockState, BlockDisplayContext.create());

		populateLidKeys();
		state.lid = null;
		state.wobbleStyle = null;
		BlockStateModel lidBsm = getLidModel(blockEntity.lid);
		if (lidBsm != null) {
			BlockModel lidModel = new BlockStateModelWrapper(lidBsm, List.of(), new Matrix4f().identity());
			lidModel.update(state.lidModel, blockState, BlockDisplayContext.create(), 42L);
			state.lid = blockEntity.lid;
		}

		WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;
		if (wobbleStyle != null && blockEntity.getLevel() != null) {
			float f = (float) (blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTick;
			state.wobbleAmplitude = WOBBLE_AMPLITUDE;
			state.wobbleAmount = f / (float) wobbleStyle.duration;
			state.wobbleStyle = wobbleStyle;
		} else {
			state.wobbleAmount = -1.0F;
		}
	}

	@Override
	public void submit(JarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.translate(0.5, 0.0, 0.5);
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		poseStack.translate(-0.5, 0.0, -0.5);

		if (state.wobbleAmount >= 0.0F && state.wobbleAmount <= 1.0F) {
			if (state.wobbleStyle == WobbleStyle.POSITIVE) {
				float amplitude = 0.015625F;
				float deltaTime = state.wobbleAmount * (float) (Math.PI * 2);
				float tiltX = -1.5F * (Mth.cos(deltaTime) + 0.5F) * Mth.sin(deltaTime / 2.0F);
				poseStack.rotateAround(Axis.XP.rotation(tiltX * amplitude), 0.5F, 0.0F, 0.5F);
				float tiltZ = Mth.sin(deltaTime);
				poseStack.rotateAround(Axis.ZP.rotation(tiltZ * amplitude), 0.5F, 0.0F, 0.5F);
			} else {
				float f5 = Mth.sin(-state.wobbleAmount * 3.0F * (float) Math.PI) * state.wobbleAmplitude;
				float f6 = 1.0F - state.wobbleAmount;
				poseStack.rotateAround(Axis.YP.rotation(f5 * f6), 0.5F, 0.0F, 0.5F);
			}
		}

		if (state.lid != null && LID_KEYS.containsKey(state.lid)) {
			state.lidModel.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		}

		state.jarModel.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

		if (state.itemStack != null && !state.itemStack.isEmpty()) {
			poseStack.pushPose();
			poseStack.translate(0.5D, 0.4375D, 0.5D);
			poseStack.mulPose(Axis.YN.rotationDegrees(RotationSegment.convertToDegrees(state.itemRotation)));
			poseStack.scale(0.5F, 0.5F, 0.5F);
			state.itemStack.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}

		poseStack.popPose();
	}

	@Configurable
	public static class MasonJarRenderer extends JarRenderer<MasonJarBlockEntity> {

		private TFItemDisplayContextEnumExtension itemDisplayContextEnumExtension = new TFItemDisplayContextEnumExtension();

		protected final ItemModelResolver itemModelResolver;
		protected final EntityRenderDispatcher entityRender;
		protected final Font font;

		public MasonJarRenderer(BlockEntityRendererProvider.Context context) {
			super(context);
			this.entityRender = context.entityRenderer();
			this.itemModelResolver = context.itemModelResolver();
			this.font = context.font();
		}

		@Override
		public void extractRenderState(MasonJarBlockEntity blockEntity, JarRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
			super.extractRenderState(blockEntity, state, partialTick, cameraPosition, breakProgress);

			ItemStack stack = blockEntity.getItemHandler().getItem();
			if (!stack.isEmpty()) {
				if (state.itemStack == null) {
					state.itemStack = new ItemStackRenderState();
				}
				this.itemModelResolver.updateForTopItem(state.itemStack, stack, itemDisplayContextEnumExtension.JARRED, null, null, 0);
				state.itemRotation = blockEntity.getItemRotation();
			} else {
				state.itemStack = null;
			}
		}
	}
}
