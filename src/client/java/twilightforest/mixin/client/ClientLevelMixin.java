package twilightforest.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.BlockHooks;
import twilightforest.asmhooks.MultipartHooks;

import java.util.Iterator;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

	@Inject(method = "entitiesForRendering", at = @At("RETURN"), cancellable = true)
	private void tf$injectTFPartEntities(CallbackInfoReturnable<Iterable<Entity>> cir) {
		Iterable<Entity> original = cir.getReturnValue();
		if (original == null) return;
		cir.setReturnValue(new TfEntityIterable(original));
	}

	/**
	 * 26.3 draws rain/snow particles and weather columns from {@code ClientLevel#getPrecipitationAt}, not from
	 * {@code Level#isRainingAt}. Cloud blocks have to report their precipitation here so rain falls below them.
	 */
	@Inject(method = "getPrecipitationAt", at = @At("RETURN"), cancellable = true)
	private void tf$rainFromCloudBlocks(BlockPos pos, CallbackInfoReturnable<Biome.Precipitation> cir) {
		cir.setReturnValue(BlockHooks.precipitationAt(cir.getReturnValue(), (Level) (Object) this, pos));
	}

	private static class TfEntityIterable implements Iterable<Entity> {
		private final Iterable<Entity> delegate;

		TfEntityIterable(Iterable<Entity> delegate) {
			this.delegate = delegate;
		}

		@Override
		public @NonNull Iterator<Entity> iterator() {
			return MultipartHooks.resolveEntitiesForRendering(delegate.iterator());
		}
	}
}
