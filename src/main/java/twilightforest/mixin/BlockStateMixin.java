package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.TriState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Mixin to add NeoForge-compatible methods to BlockState.
 */
@Mixin(BlockState.class)
public class BlockStateMixin {

	@Unique
	public TriState canSustainPlant(LevelReader level, BlockPos pos, Direction direction, BlockState state) {
		return TriState.DEFAULT;
	}
}