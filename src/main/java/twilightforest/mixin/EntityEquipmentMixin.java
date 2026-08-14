package twilightforest.mixin;

import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.events.CharmEvents;

/**
 * Injects at the HEAD of {@link EntityEquipment#dropAll(LivingEntity)} — the absolute
 * lowest-level exit point through which every piece of worn equipment passes before it
 * is spawned as an ItemEntity on the ground.
 * <p>
 * Typical call chain for a player death:
 * <pre>
 *   ServerPlayer.die()
 *     → dropAllDeathLoot()
 *       → Player.dropEquipment(ServerLevel)
 *         → Player.inventory.dropAll()
 *           → EntityEquipment.dropAll(dropper)   ← we intercept here
 *             → for each item: dropper.drop(item)  // the actual ItemEntity spawn
 * </pre>
 * By the time we reach {@code dropAll} we cannot be beaten by any other clearing
 * strategy: we look directly into the equipment map that is about to be iterated and
 * remove any KEPT_ON_DEATH stack from it (after persisting it via CharmEvents so the
 * respawn handler can give it back).
 */
@Mixin(EntityEquipment.class)
public abstract class EntityEquipmentMixin {

	@Inject(method = "dropAll", at = @At("HEAD"))
	private void tf$stripKeptOnDeathBeforeEquipmentDropAll(LivingEntity dropper, CallbackInfo ci) {
		CharmEvents.interceptKeptOnDeathAtEquipmentDropAll(dropper, (EntityEquipment) (Object) this);
	}
}
