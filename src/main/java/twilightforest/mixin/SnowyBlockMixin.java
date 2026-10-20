package twilightforest.mixin;

import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.BlockHooks;

/**
 * Grass-like blocks that use the snow loggable layers property should keep their snowy appearance instead of
 * turning back to plain grass when a thin snow layer sits on them.
 * Replaces the NeoForge {@code KeepGrassSnowyForSnowloggableBlocksTransformer}.
 */
@Mixin(SnowyBlock.class)
public class SnowyBlockMixin {

	@Inject(method = "isSnowySetting", at = @At("RETURN"), cancellable = true)
	private static void tf$snowyForSnowLoggableBlocks(BlockState aboveState, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(BlockHooks.keepSnowyStateForSnowloggableBlocks(cir.getReturnValueZ(), aboveState));
	}
}
