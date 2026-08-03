package twilightforest.util;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

/**
 * Interface for methods added to Entity via mixin.
 * Cast Entity to this interface to call these methods at compile time.
 */
public interface TFEntityExtensions {

	<T> T twilightforest$getData(AttachmentType<T> type);

	<T> void twilightforest$setData(AttachmentType<T> type, T value);

	boolean twilightforest$hasData(AttachmentType<?> type);

	void twilightforest$removeData(AttachmentType<?> type);

	CompoundTag twilightforest$getPersistentData();

	Entity[] twilightforest$getParts();

	boolean twilightforest$isMultipartEntity();

	boolean twilightforest$canFitInsideContainerItems();

	void twilightforest$breakItem(ItemStack stack);

	net.minecraft.world.entity.ai.goal.GoalSelector getGoalSelector();
}
