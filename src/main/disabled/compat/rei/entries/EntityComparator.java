package twilightforest.compat.rei.entries;

import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import me.shedaniel.rei.api.common.entry.comparison.EntryComparatorRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.TagValueOutput;

public interface EntityComparator extends EntryComparatorRegistry<Entity, EntityType<?>> {

	static EntryComparator<Entity> entityTypeNbt() {
		EntryComparator<Tag> nbt = new NbtHasher(new String[0]);
		return (context, entity) -> {
			TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
			entity.saveWithoutId(output);
			CompoundTag tag = output.buildResult();
			return tag.isEmpty() ? 0L : nbt.hash(context, tag);
		};
	}
}
