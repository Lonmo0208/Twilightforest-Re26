package twilightforest.mixin.client;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFKeyBinds;
import twilightforest.init.custom.TravellersModifiersManager;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	@Shadow @Final private Minecraft minecraft;
	@Shadow private double accumulatedDX;
	@Shadow private double accumulatedDY;

	@Inject(method = "turnPlayer", at = @At("HEAD"))
	private void tf$reduceSensitivityWhenZooming(double mousea, CallbackInfo ci) {
		if (this.minecraft.player == null) return;

		ItemStack headStack = this.minecraft.player.getItemBySlot(EquipmentSlot.HEAD);
		if (headStack == null || headStack.isEmpty()) return;

		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		if (zoomModifier == null) return;

		if (!TravellersModifiersManager.isModifierActive(this.minecraft.player, headStack, TravellersModifiersManager.ZOOM_ABILITY)) return;
		if (!TFKeyBinds.ZOOM_KEY.isDown() || this.minecraft.player.isScoping()) return;

		double sensitivityFactor = zoomModifier + 0.05;
		this.accumulatedDX *= sensitivityFactor;
		this.accumulatedDY *= sensitivityFactor;
	}
}