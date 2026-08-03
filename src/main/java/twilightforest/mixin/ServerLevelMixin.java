package twilightforest.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.util.TFEntityExtensions;

/**
 * Fabric multipart entity-id lookup fix (server side).
 *
 * <p>The companion to {@link ProjectileUtilMixin}. Client ray-trace now reports
 * a TFPart sub-entity as the hit target; the client then fires a
 * {@code ServerboundAttackPacket} carrying that part's entity-id. The server
 * resolves that id through {@code ServerLevel#getEntityOrPart(int)} which only knows
 * about world-tracked entities and Ender Dragon parts — TFParts aren't tracked, so
 * the lookup returned {@code null} and the packet was discarded, undoing the client-side hit.
 *
 * <p>We scan every multipart owner in the world for a part whose id matches the
 * requested id when the vanilla lookup misses. Both {@code getEntity(int)} and
 * {@code getEntityOrPart(int)} are hooked because the attack handler uses the latter
 * in Minecraft 1.21.6.
 */
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

	@Inject(method = "getEntity(I)Lnet/minecraft/world/entity/Entity;", at = @At("RETURN"), cancellable = true)
	private void twilightforest$findMultipartPartById(int id, CallbackInfoReturnable<Entity> cir) {
		if (cir.getReturnValue() != null) return;
		Entity part = findPartById(id);
		if (part != null) cir.setReturnValue(part);
	}

	@Inject(method = "getEntityOrPart(I)Lnet/minecraft/world/entity/Entity;", at = @At("RETURN"), cancellable = true)
	private void twilightforest$findMultipartPartByIdOrPart(int id, CallbackInfoReturnable<Entity> cir) {
		if (cir.getReturnValue() != null) return;
		Entity part = findPartById(id);
		if (part != null) cir.setReturnValue(part);
	}

	private Entity findPartById(int id) {
		ServerLevel self = (ServerLevel) (Object) this;
		for (Entity owner : self.getAllEntities()) {
			if (owner instanceof TFEntityExtensions ext) {
				Entity[] parts = ext.twilightforest$getParts();
				if (parts == null) continue;
				for (Entity part : parts) {
					if (part != null && part.getId() == id) {
						return part;
					}
				}
			}
		}
		return null;
	}
}