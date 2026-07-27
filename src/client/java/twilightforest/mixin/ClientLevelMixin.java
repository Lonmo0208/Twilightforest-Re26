package twilightforest.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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

	private static class TfEntityIterable implements Iterable<Entity> {
		private final Iterable<Entity> delegate;

		TfEntityIterable(Iterable<Entity> delegate) {
			this.delegate = delegate;
		}

		@Override
		public Iterator<Entity> iterator() {
			return MultipartHooks.resolveEntitiesForRendering(delegate.iterator());
		}
	}
}
