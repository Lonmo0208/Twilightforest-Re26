package twilightforest.mixin;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.TFPart;
import twilightforest.util.TFEntityExtensions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Mixin to add NeoForge-compatible methods to Entity.
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements TFEntityExtensions {

	@Accessor("dimensions")
	public abstract EntityDimensions getDimensions();

	@Accessor("dimensions")
	public abstract void setDimensions(EntityDimensions dimensions);

	@Accessor("ENTITY_COUNTER")
	public static AtomicInteger getEntityCounter() {
		throw new UnsupportedOperationException();
	}

	@Unique
	private final Map<Supplier<?>, Object> tfAttachments = new ConcurrentHashMap<>();

	@Override
	@Unique
	@SuppressWarnings("unchecked")
	public <T> T getData(Supplier<? extends AttachmentType<T>> type) {
		Object value = tfAttachments.get(type);
		if (value != null) {
			return (T) value;
		}
		Supplier<T> initializer = type.get().initializer();
		return initializer != null ? initializer.get() : null;
	}

	@Override
	@Unique
	public <T> void setData(Supplier<? extends AttachmentType<T>> type, T value) {
		tfAttachments.put(type, value);
	}

	@Override
	@Unique
	public boolean hasData(Supplier<? extends AttachmentType<?>> type) {
		return tfAttachments.containsKey(type);
	}

	@Override
	@Unique
	public void removeData(Supplier<? extends AttachmentType<?>> type) {
		tfAttachments.remove(type);
	}

	@Override
	@Unique
	public CompoundTag getPersistentData() {
		return new CompoundTag();
	}

	@Override
	@Unique
	public Entity[] getParts() {
		return new Entity[0];
	}

	@Override
	@Unique
	public boolean isMultipartEntity() {
		return false;
	}

	@Override
	@Unique
	public boolean canFitInsideContainerItems() {
		return true;
	}

	@Override
	@Unique
	public void breakItem(ItemStack stack) {
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
		if (self instanceof TFEntityExtensions ext && ext.isMultipartEntity()) {
			TFPart.assignPartIDs(self);
		}
	}
}
