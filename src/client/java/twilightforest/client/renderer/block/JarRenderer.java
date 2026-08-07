package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricModelManager;
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
	public static final Map<Item, BlockModel> LID_MODELS = new HashMap<>();

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

	public static @Nullable BlockModel getLidModel(Item lid) {
		if (lid == null) return null;
		// Check cached BlockModel first
		if (LID_MODELS.containsKey(lid)) {
			return LID_MODELS.get(lid);
		}
		ExtraModelKey<BlockStateModel> key = LID_MODEL_KEYS.get(lid);
		if (key == null) return null;
		FabricModelManager fmm = (FabricModelManager) Minecraft.getInstance().getModelManager();
		BlockStateModel bsm = fmm.getModel(key);
		if (bsm == null) return null;
		// Wrap BlockStateModel as BlockModel with identity transform and empty tints
		BlockModel result = new BlockStateModelWrapper(bsm, List.of(), new Matrix4f().identity());
		LID_MODELS.put(lid, result);
		return result;
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

		// Jar model
		this.blockResolver.update(state.jarModel, blockState, BlockDisplayContext.create());

		// Lid model - only set state.lid if we successfully loaded model
		populateLidKeys();
		state.lid = null;
		BlockModel lidModel = getLidModel(blockEntity.lid);
		if (lidModel != null) {
			lidModel.update(state.lidModel, blockState, BlockDisplayContext.create(), 42L);
			state.lid = blockEntity.lid;
		}

		// Wobble animation
		WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;
		if (wobbleStyle != null && blockEntity.getLevel() != null) {
			float f = (float) (blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTick;
			state.wobbleAmplitude = WOBBLE_AMPLITUDE;
			state.wobbleAmount = f / (float) wobbleStyle.duration;
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

		// Wobble
		if (state.wobbleAmount >= 0.0F && state.wobbleAmount <= 1.0F) {
			float f5 = Mth.sin(-state.wobbleAmount * 3.0F * (float) Math.PI) * state.wobbleAmplitude;
			float f6 = 1.0F - state.wobbleAmount;
			poseStack.rotateAround(Axis.YP.rotation(f5 * f6), 0.5F, 0.0F, 0.5F);
		}

		// Render lid model FIRST, so jar renders on top of lid base
		if (state.lid != null && LID_KEYS.containsKey(state.lid)) {
			state.lidModel.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		}

		// Render jar model
		state.jarModel.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

		// Render contents (for MasonJar)
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
