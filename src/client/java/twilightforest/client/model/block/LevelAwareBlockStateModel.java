package twilightforest.client.model.block;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Fabric port of NeoForge's DynamicBlockStateModel.
 * <p>
 * Vanilla only calls {@code BlockStateModel.collectParts(RandomSource, List)} which cannot access the
 * level/position. Models whose shape or variant depends on the block's surroundings (force fields,
 * giant blocks, connected textures, patches, ...) implement this interface; a mixin on
 * {@link net.minecraft.client.renderer.block.ModelBlockRenderer} dispatches to the level-aware overload.
 */
public interface LevelAwareBlockStateModel {

	void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts);
}
