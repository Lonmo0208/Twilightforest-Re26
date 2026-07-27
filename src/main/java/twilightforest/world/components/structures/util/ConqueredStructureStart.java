package twilightforest.world.components.structures.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;

/**
 * Interface for structure starts that track a "conquered" state.
 * Implemented via Mixin on StructureStart since StructureStart is final.
 */
public interface ConqueredStructureStart {
	boolean isConquered();

	void setConquered(boolean flag, LevelAccessor level);

	void loadFromTag(CompoundTag nbt);

	void setStartY(int startY);

	int getStartY();
}