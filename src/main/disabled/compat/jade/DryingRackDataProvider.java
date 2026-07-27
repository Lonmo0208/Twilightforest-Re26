package twilightforest.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.DryingRackBlockEntity;

public enum DryingRackDataProvider implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		BlockEntity entity = accessor.getBlockEntity();
		if (entity instanceof DryingRackBlockEntity rack && rack.isDrying()) {
			CompoundTag tag = rack.saveWithoutMetadata(accessor.getLevel().registryAccess());
			data.putInt("progress", tag.getInt("dry_time").orElse(0));
			data.putInt("total", tag.getInt("total_dry_time").orElse(0));
		}
	}

	@Override
	public Identifier getUid() {
		return TwilightForestMod.prefix("drying_rack");
	}
}