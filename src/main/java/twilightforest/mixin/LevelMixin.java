package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Mixin to add NeoForge-compatible methods to Level.
 */
@Mixin(Level.class)
public class LevelMixin {

	@Unique
	public boolean isAreaLoaded(BlockPos pos, int range) {
		return ((Level) (Object) this).hasChunksAt(pos.offset(-range, -range, -range), pos.offset(range, range, range));
	}
}