package twilightforest.compat.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CurioHeadRenderer implements ICurioRenderer {

	@Override
	public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, SubmitNodeCollector collector, int light, S state, RenderLayerParent<S, M> renderLayerParent, EntityRendererProvider.Context context, float partialTicks, float ageInTicks) {
		if (renderLayerParent.getModel() instanceof HeadedModel headModel) {
			poseStack.pushPose();
			headModel.getHead().translateAndRotate(poseStack);
			poseStack.translate(0.0D, -0.25D, 0.0D);
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			poseStack.scale(0.625F, -0.625F, -0.625F);
			ItemStackRenderState itemState = new ItemStackRenderState();
			context.getItemModelResolver().updateForNonLiving(itemState, stack, ItemDisplayContext.HEAD, slotContext.entity());
			itemState.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, -1);
			poseStack.popPose();
		}
	}
}