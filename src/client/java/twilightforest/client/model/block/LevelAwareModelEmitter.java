package twilightforest.client.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModelPart;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fabric (FRAPI) rendering entry point for {@link LevelAwareBlockStateModel}s.
 * <p>
 * Fabric/Indigo renders world blocks through
 * {@code BlockStateModel.emitQuads(QuadEmitter, BlockAndTintGetter, BlockPos, BlockState, RandomSource, Predicate)}
 * instead of the vanilla {@code ModelBlockRenderer.tesselateBlock} path, so models whose geometry depends on the
 * surroundings must override {@code emitQuads} and route through the level-aware {@code collectParts} overload.
 */
public final class LevelAwareModelEmitter {

	private LevelAwareModelEmitter() {
	}

	public static void emitQuads(LevelAwareBlockStateModel model, QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, Predicate<Direction> cullTest) {
		List<BlockStateModelPart> parts = new ArrayList<>();
		model.collectParts(level, pos, state, random, parts);
		for (BlockStateModelPart part : parts) {
			((FabricBlockStateModelPart) part).emitQuads(emitter, cullTest);
		}
	}
}
