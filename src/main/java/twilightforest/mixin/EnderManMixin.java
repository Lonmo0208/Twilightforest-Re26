package twilightforest.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Unique;
import twilightforest.init.custom.TravellersModifiersManager;

@Mixin(Enderman.class)
public abstract class EnderManMixin {

	@Inject(method = "isBeingStaredBy", at = @At("HEAD"), cancellable = true)
	private void tf$allNightGogglesPreventStare(Player player, CallbackInfoReturnable<Boolean> cir) {
		if (TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
	private void tf$allNightGogglesPreventTarget(LivingEntity target, CallbackInfo ci) {
		if (target instanceof Player player && TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)) {
			Enderman self = (Enderman) (Object) this;
			self.setTarget(null);
			self.setTimeToRemainAngry(0);
			self.setPersistentAngerTarget(null);
			ci.cancel();
		}
	}
}
