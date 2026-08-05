package twilightforest.mixin;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
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
	private final Map<AttachmentType<?>, Object> tfAttachments = new ConcurrentHashMap<>();

	@Override
	@Unique
	@SuppressWarnings("unchecked")
	public <T> @NonNull T twilightforest$getData(@NonNull AttachmentType<T> type) {
		Object value = tfAttachments.get(type);
		if (value != null) {
			return (T) value;
		}
		java.util.function.Supplier<T> initializer = type.initializer();
		if (initializer != null) {
			T newValue = initializer.get();
			// Save the new instance to the map so it persists
			tfAttachments.put(type, newValue);
			return newValue;
		}
		return null;
	}

	@Override
	@Unique
	public <T> void twilightforest$setData(@NonNull AttachmentType<T> type, @NonNull T value) {
		tfAttachments.put(type, value);
	}

	@Override
	@Unique
	public boolean twilightforest$hasData(@NonNull AttachmentType<?> type) {
		return tfAttachments.containsKey(type);
	}

	@Override
	@Unique
	public void twilightforest$removeData(@NonNull AttachmentType<?> type) {
		tfAttachments.remove(type);
	}

	/**
	 * Persistent data stored directly on the Entity instance.
	 * This field is preserved across the Entity's lifetime (e.g. ServerPlayer across death/respawn).
	 * It is also written to / read from the entity's NBT via
	 * {@link #tf$writePersistentData(CompoundTag, CallbackInfo)} and
	 * {@link #tf$readPersistentData(CompoundTag, CallbackInfo)} so that data survives
	 * server restarts and log-out / log-in cycles.
	 */
	@Unique
	private CompoundTag tfPersistentData = null;

	@Override
	@Unique
	public @NonNull CompoundTag twilightforest$getPersistentData() {
		if (this.tfPersistentData == null) {
			this.tfPersistentData = new CompoundTag();
		}
		return this.tfPersistentData;
	}

	/**
	 * Save {@link #tfPersistentData} into the entity's NBT under the key {@code "TFData"}.
	 * Injects at RETURN of {@link Entity#saveWithoutId(ValueOutput)} so the custom
	 * data is appended after all vanilla data (including addAdditionalSaveData).
	 */
	@Inject(method = "saveWithoutId", at = @At("RETURN"))
	private void tf$writePersistentData(ValueOutput output, CallbackInfo ci) {
		if (this.tfPersistentData != null && !this.tfPersistentData.isEmpty()) {
			output.store("TFData", CompoundTag.CODEC, this.tfPersistentData.copy());
		}
	}

	/**
	 * Load {@link #tfPersistentData} from the entity's NBT under the key {@code "TFData"}.
	 * Injects at RETURN of {@link Entity#load(ValueInput)} so the custom
	 * data is restored after all vanilla data has been parsed (including readAdditionalSaveData).
	 */
	@Inject(method = "load", at = @At("RETURN"))
	private void tf$readPersistentData(ValueInput input, CallbackInfo ci) {
		input.read("TFData", CompoundTag.CODEC).ifPresent(tag -> {
			this.tfPersistentData = tag;
		});
	}

	@Override
	@Unique
	public Entity @NonNull [] twilightforest$getParts() {
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
		return true;
	}

	@Override
	@Unique
	public void twilightforest$breakItem(@NonNull ItemStack stack) {
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
