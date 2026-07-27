package twilightforest.mixin;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.model.block.aurorablock.NoiseVaryingModel;

/**
 * Mixin to support level-aware block model variant selection.
 * The vanilla ModelBlockRenderer only calls collectParts(RandomSource, ...),
 * but NoiseVaryingModel needs BlockPos to select the correct variant via simplex noise.
 */
@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {

	/**
	 * Redirect collectParts to use the level-aware version when the model is a NoiseVaryingModel.
	 * Intercepts after the seed is set but before collectParts is called.
	 */
	@Inject(
		method = "tesselateBlock(Lnet/minecraft/client/renderer/block/BlockQuadOutput;FFFLnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/renderer/block/dispatch/BlockStateModel;J)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/block/dispatch/BlockStateModel;collectParts(Lnet/minecraft/util/RandomSource;Ljava/util/List;)V"
		),
		cancellable = true
	)
	private void tf$useLevelAwareCollectParts(
		net.minecraft.client.renderer.block.BlockQuadOutput output,
		float x, float y, float z,
		BlockAndTintGetter level,
		BlockPos pos,
		BlockState blockState,
		net.minecraft.client.renderer.block.dispatch.BlockStateModel model,
		long seed,
		CallbackInfo ci
	) {
		if (model instanceof NoiseVaryingModel noiseModel) {
			// Use level-aware variant selection based on BlockPos
			noiseModel.collectParts(level, pos, blockState, RandomSource.create(seed), ((ModelBlockRendererAccessor) this).getParts());
			// Cancel vanilla collectParts call since we handled it
			ci.cancel();
		}
		// If not NoiseVaryingModel, let vanilla code run normally
	}
}