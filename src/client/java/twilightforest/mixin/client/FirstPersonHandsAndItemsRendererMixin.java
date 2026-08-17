package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.event.TravellersClientEvents;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class FirstPersonHandsAndItemsRendererMixin {

	@Inject(method = "renderPlayerHand", at = @At("HEAD"), cancellable = true)
	private void tf$renderTravellersGlove(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, HumanoidArm arm, PlayerRenderState playerRenderState, CallbackInfo ci) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null && TravellersClientEvents.renderGlovesInFirstPerson(player, arm, collector, poseStack, lightCoords)) {
			ci.cancel();
		}
	}
}
