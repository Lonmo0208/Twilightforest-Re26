package twilightforest.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;

import org.jetbrains.annotations.NotNull;

import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import twilightforest.entity.TFPart;
import twilightforest.util.TFEntityExtensions;

/**
 * Mixin adding Twilight Forest-specific capabilities to the base Entity class.
 * <p>
 * Only methods/hooks that are genuinely missing from Fabric API (or vanilla) are
 * implemented here. Specifically:
 * <ul>
 *   <li>MultiPartEntity support — required for Hydra, Naga, and Snow Queen bosses,
 *       which consist of multiple sub-parts with independent hitboxes and damage
 *       calculations. Fabric has no equivalent of Forge/NeoForge's built-in
 *       Entity#isMultipartEntity and Entity#getParts methods on the Entity base
 *       class, so these HAVE to live in a Mixin.</li>
 *   <li>Misc small Forge-style convenience hooks that Fabric does not expose as
 *       direct Entity methods.</li>
 *   <li>Client-side multipart-ID reassignment injection, so the client references
 *       the same part-entity IDs as the server after receiving a spawn packet.</li>
 * </ul>
 *
 * Previously, this Mixin also provided two parallel attachment stores which have
 * since been retired in favor of Fabric's official Attachment API:
 * <ul>
 *   <li>A {@code ConcurrentHashMap<AttachmentType<?>, Object> tfAttachments}
 *       backing getData/setData/hasData/removeData. Callers now use
 *       {@code Entity.getAttached()}/{@code setAttached()} directly.</li>
 *   <li>A free-form {@code CompoundTag tfPersistentData} backing
 *       getPersistentData(). Callers now use
 *       {@code Entity.getAttached(TFDataAttachments.TF_PERSISTENT_DATA)} which
 *       is registered as a persistent Fabric Attachment.</li>
 * </ul>
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements TFEntityExtensions {

	@Override
	@Unique
	public Entity @NotNull [] twilightforest$getParts() {
		return new Entity[0];
	}

	@Override
	@Unique
	public boolean twilightforest$isMultipartEntity() {
		return false;
	}

	@Override
	@Unique
	public boolean twilightforest$canFitInsideContainerItems() {
		// Original Forge default: allow items inside containers. Sub-entities or
		// boss multiparts may override this (e.g. Hydra heads are not pick-uppable).
		return ((Entity) (Object) this) instanceof LivingBlock;
	}

	@Override
	@Unique
	public void twilightforest$breakItem(@NotNull ItemStack stack) {
		// Default no-op. LivingEntityMixin overrides this to play the break sound
		// and shrink the stack count, just like Forge's LivingEntity#breakItem.
	}

	@Override
	@Unique
	public @NonNull GoalSelector twilightforest$getGoalSelector() {
		// Default no-op for non-Mob entities. MobMixin overrides this to return the
		// real GoalSelector so callers don't have to cast/instanceof-check first.
		return null;
	}

	/**
	 * After the client receives an entity spawn packet and sets the entity's ID
	 * via {@code recreateFromPacket}, synchronize the IDs of any multipart
	 * sub-entities (Hydra heads/necks, Naga segments, etc.) so they match the
	 * server-side IDs. Without this, attack packets sent by the client reference
	 * client-side part IDs that the server cannot resolve.
	 */
	@Inject(method = "recreateFromPacket", at = @At("TAIL"))
	private void twilightforest$assignPartIdsOnRecreate(ClientboundAddEntityPacket packet, CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		if (self instanceof TFEntityExtensions ext && ext.twilightforest$isMultipartEntity()) {
			TFPart.assignPartIDs(self);
		}
	}
}