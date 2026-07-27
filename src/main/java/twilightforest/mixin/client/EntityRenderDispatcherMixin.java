package twilightforest.mixin.client;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.entity.TFPart;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private <E extends Entity> void tf$redirectPartEntityShouldRender(E entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof TFPart<?> part) {
			EntityRenderer partRenderer = BakedMultiPartRenderers.lookup(part.renderer());
			if (partRenderer != null) {
				cir.setReturnValue(partRenderer.shouldRender(entity, frustum, x, y, z));
			} else {
				cir.setReturnValue(false);
			}
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Inject(method = "extractEntity", at = @At("HEAD"), cancellable = true)
	private <E extends Entity> void tf$redirectPartEntityExtract(E entity, float partialTick, CallbackInfoReturnable<EntityRenderState> cir) {
		if (entity instanceof TFPart<?> part) {
			EntityRenderer partRenderer = BakedMultiPartRenderers.lookup(part.renderer());
			if (partRenderer != null) {
				cir.setReturnValue(partRenderer.createRenderState(entity, partialTick));
			} else {
				cir.setReturnValue(null);
			}
		}
	}
}
