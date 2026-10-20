package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.BlockHooks;

/**
 * Mixin to add NeoForge-compatible methods to Level.
 */
@Mixin(Level.class)
public class LevelMixin {

	@Unique
	public boolean isAreaLoaded(BlockPos pos, int range) {
		return ((Level) (Object) this).hasChunksAt(pos.offset(-range, -range, -range), pos.offset(range, range, range));
	}

	/**
	 * Rainy cloud blocks act as a source of precipitation for whatever is below them, even when that spot is
	 * enclosed and the sky check fails. This restores the NeoForge {@code IsRainingAtTransformer}.
	 */
	@Inject(method = "precipitationAt", at = @At("RETURN"), cancellable = true)
	private void tf$rainFromCloudBlocks(BlockPos pos, CallbackInfoReturnable<Biome.Precipitation> cir) {
		cir.setReturnValue(BlockHooks.precipitationAt(cir.getReturnValue(), (Level) (Object) this, pos));
	}
}
