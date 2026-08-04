package twilightforest.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFKeyBinds;
import twilightforest.init.custom.TravellersModifiersManager;

@Mixin(Camera.class)
public class CameraMixin {

	@Shadow @Final private Minecraft minecraft;

	@Inject(method = "calculateFov", at = @At("HEAD"), cancellable = true)
	private void tf$applyGogglesZoomFov(float partialTicks, CallbackInfoReturnable<Float> cir) {
		LocalPlayer player = this.minecraft.player;
		if (player == null) return;
		if (player.isScoping()) return;
		if (!TFKeyBinds.ZOOM_KEY.isDown()) return;

		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		if (headStack.isEmpty()) return;

		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		if (zoomModifier == null) return;

		if (!TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ZOOM_ABILITY)) return;

		float baseFov = (float) this.minecraft.options.fov().get().intValue();
		float targetFov = baseFov * zoomModifier;
		cir.setReturnValue(targetFov);
	}
}