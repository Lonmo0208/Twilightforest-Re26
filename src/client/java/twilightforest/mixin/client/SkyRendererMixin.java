package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.world.level.MoonPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFDimension;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {

	// Hide the sun in the Twilight Forest dimension; the sky disc, stars and dusk glow remain untouched.
	@Inject(method = "renderSun", at = @At("HEAD"), cancellable = true)
	private void twilight$hideSun(float rainBrightness, PoseStack poseStack, CallbackInfo ci) {
		if (isInTwilightForest()) {
			ci.cancel();
		}
	}

	// Hide the moon in the Twilight Forest dimension; the sky disc, stars and dusk glow remain untouched.
	@Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
	private void twilight$hideMoon(MoonPhase moonPhase, float rainBrightness, PoseStack poseStack, CallbackInfo ci) {
		if (isInTwilightForest()) {
			ci.cancel();
		}
	}

	private static boolean isInTwilightForest() {
		ClientLevel level = Minecraft.getInstance().level;
		return level != null && TFDimension.isTwilightWorldOnClient(level);
	}
}
