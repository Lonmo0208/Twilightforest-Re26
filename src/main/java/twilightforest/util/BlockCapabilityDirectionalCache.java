package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

// Voidscape copy
public class BlockCapabilityDirectionalCache<R> {

	private final Map<BlockPosAndDirection, Object> data = new HashMap<>();

	@Nullable
	public R get(Object capability, ServerLevel level, BlockPos pos, Direction direction) {
		Object cache = this.data.get(new BlockPosAndDirection(pos, direction));
		if (cache == null) {
			cache = null;
			this.data.put(new BlockPosAndDirection(pos, direction), cache);
		}
		return null;
	}

	private record BlockPosAndDirection(BlockPos pos, Direction direction) {

	}
}
