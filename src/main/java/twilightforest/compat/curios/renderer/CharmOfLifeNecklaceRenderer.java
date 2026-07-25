package twilightforest.compat.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.curios.model.CharmOfLifeNecklaceModel;

public class CharmOfLifeNecklaceRenderer implements ICurioRenderer {

	private final CharmOfLifeNecklaceModel model;
	private final int necklaceColor;

	public CharmOfLifeNecklaceRenderer(int necklaceColor) {
		this.model = new CharmOfLifeNecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_LIFE));
		this.necklaceColor = necklaceColor;
	}

	@Override
	public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(ItemStack item, SlotContext slotContext, PoseStack poseStack, SubmitNodeCollector collector, int light, S state, RenderLayerParent<S, M> renderLayerParent, EntityRendererProvider.Context context, float partialTicks, float ageInTicks) {
		if (renderLayerParent.getModel() instanceof HumanoidModel<?> model) {
			poseStack.pushPose();
			model.body.translateAndRotate(poseStack);
			poseStack.translate(-0.0D, 0.23D, -0.135D);
			poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
			poseStack.scale(-0.4F, -0.4F, 0.4F);
			ItemStackRenderState itemState = new ItemStackRenderState();
			context.getItemModelResolver().updateForNonLiving(itemState, item, ItemDisplayContext.FIXED, slotContext.entity());
			itemState.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, -1);
			poseStack.popPose();
		}
		ICurioRenderer.setupHumanoidAnimations(this.model, state);
		MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
		VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderTypes.entityCutout(TwilightForestMod.getModelTexture("charm_of_life_necklace.png")));
		this.model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, this.necklaceColor);
	}
}