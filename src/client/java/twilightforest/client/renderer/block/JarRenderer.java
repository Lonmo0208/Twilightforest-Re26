package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
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
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jspecify.annotations.Nullable;
import twilightforest.beanification.Configurable;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.client.state.block.JarRenderState;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JarRenderer<T extends JarBlockEntity> implements BlockEntityRenderer<T, JarRenderState> {
	// TODO: Port to Fabric - StandaloneModelKey and Lazy are NeoForge-specific
	public static final Map<Item, Object> LID_KEYS = new HashMap<>();

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

		public Object createKey() {
			// TODO: Port to Fabric - StandaloneModelKey is NeoForge-specific
			return new Object();
		}
	}

	// TODO: Port to Fabric - DeferredBlock references removed, LID_LOCATION_LIST needs item-based registration
	public static final List<LidResource> LID_LOCATION_LIST = List.of();

	protected final BlockModelResolver blockResolver;
	protected static final float WOBBLE_AMPLITUDE = 0.125F;

	public JarRenderer(BlockEntityRendererProvider.Context context) {
		this.blockResolver = context.blockModelResolver();
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

		// Lid model
		// TODO: Port to Fabric - Replace StandaloneModelKey with Fabric equivalent
		/*if (LID_KEYS.containsKey(blockEntity.lid)) {
			StandaloneModelKey<BlockModel> key = LID_KEYS.get(blockEntity.lid);
			BlockModel lidModel = Minecraft.getInstance().getModelManager().getStandaloneModel(key);
			if (lidModel != null) {
				lidModel.update(state.lidModel, blockState, BlockDisplayContext.create(), 42L);
			}
		}*/
		state.lid = blockEntity.lid;

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

		// Render jar model
		state.jarModel.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

		// Render lid model
		if (state.lid != null && LID_KEYS.containsKey(state.lid)) {
			state.lidModel.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		}

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

		// TODO: Port to Fabric - @Autowired(dist = Dist.CLIENT) uses NeoForge's Dist
		private TFItemDisplayContextEnumExtension itemDisplayContextEnumExtension;

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
