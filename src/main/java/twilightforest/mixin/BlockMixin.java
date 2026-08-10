package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric Block extensions.
 * Note: NeoForge-compatible methods (getToolModifiedState, onDestroyedByPlayer, etc.)
 * have been removed as they are NeoForge-only APIs that do not exist in Fabric 26.1.2.
 */
@Mixin(Block.class)
public class BlockMixin {
}
