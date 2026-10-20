package twilightforest.mixin;

import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.BlockHooks;

/**
 * Leash knots normally only survive on vanilla fences; wrought iron fences must hold them too.
 * Replaces the NeoForge {@code LeashFenceKnotSurvivesTransformer}.
 */
@Mixin(LeashFenceKnotEntity.class)
public class LeashFenceKnotEntityMixin {

	@Inject(method = "survives", at = @At("RETURN"), cancellable = true)
	private void tf$wroughtIronFenceHoldsKnot(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(BlockHooks.leashFenceKnotSurvives(cir.getReturnValueZ(), (LeashFenceKnotEntity) (Object) this));
	}
}
