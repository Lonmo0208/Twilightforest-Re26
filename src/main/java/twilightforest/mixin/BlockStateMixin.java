package twilightforest.mixin;

import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric BlockState extensions.
 * Note: NeoForge-compatible methods (canSustainPlant, etc.) have been removed as they are
 * NeoForge-only APIs that do not exist in Fabric 26.1.2.
 * Vegetation/support checks go through vanilla BlockTags.SUPPORTS_VEGETATION and
 * VegetationBlock#mayPlaceOn instead.
 */
@Mixin(BlockState.class)
public class BlockStateMixin {
}
