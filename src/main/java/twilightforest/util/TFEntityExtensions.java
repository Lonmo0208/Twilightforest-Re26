package twilightforest.util;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * Interface for methods added to Entity via mixin.
 * Cast Entity to this interface to call these methods at compile time.
 */
public interface TFEntityExtensions {

	<T> T getData(Supplier<? extends AttachmentType<T>> type);

	<T> void setData(Supplier<? extends AttachmentType<T>> type, T value);

	boolean hasData(Supplier<? extends AttachmentType<?>> type);

	void removeData(Supplier<? extends AttachmentType<?>> type);

	CompoundTag getPersistentData();

	Entity[] getParts();

	boolean isMultipartEntity();

	boolean canFitInsideContainerItems();

	void breakItem(ItemStack stack);

	net.minecraft.world.entity.ai.goal.GoalSelector getGoalSelector();
}
