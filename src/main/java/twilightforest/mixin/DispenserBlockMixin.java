package twilightforest.mixin;

import net.minecraft.core.dispenser.DispenseBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.dispenser.CandleDispenseBehavior;

import java.util.ArrayList;
import java.util.List;

/**
 * Vanilla candles have no Twilight Forest item to attach a dispense behavior list to, so the
 * skull-candle/candelabra behavior is prepended to the vanilla {@code #minecraft:candles} list
 * (which still falls back to dropping the candle).
 */
@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {

	@Inject(method = "getDispenseMethods", at = @At("RETURN"), cancellable = true)
	private void tf$addCandleDispenseBehavior(ServerLevel level, ItemStack stack, CallbackInfoReturnable<List<DispenseBehavior>> cir) {
		if (stack.is(ItemTags.CANDLES)) {
			List<DispenseBehavior> original = cir.getReturnValue();
			List<DispenseBehavior> behaviors = new ArrayList<>(original.size() + 1);
			behaviors.add(CandleDispenseBehavior.INSTANCE);
			behaviors.addAll(original);
			cir.setReturnValue(behaviors);
		}
	}
}
