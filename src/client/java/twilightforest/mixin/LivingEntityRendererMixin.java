package twilightforest.mixin;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.renderer.entity.layers.TFShieldState;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.init.TFDataAttachments;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
	private void tf$extractShieldCount(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CallbackInfo ci) {
		FortificationShieldAttachment attachment = entity.getAttached(TFDataAttachments.FORTIFICATION_SHIELDS);
		if (attachment != null) {
			TFShieldState.setShieldCount(state, attachment.shieldsLeft());
		}
	}
}