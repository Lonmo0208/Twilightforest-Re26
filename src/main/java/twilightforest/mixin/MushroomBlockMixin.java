package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.init.TFBlocks;

/**
 * Allows mushrooms to survive on blocks near Twilight Portals.
 * In Fabric 26.1.2, BlockState#canSustainPlant no longer exists, so we
 * intercept MushroomBlock#canSurvive directly instead.
 */
@Mixin(MushroomBlock.class)
public class MushroomBlockMixin {

	@Inject(
		method = "canSurvive",
		at = @At("RETURN"),
		cancellable = true
	)
	private void twilightforest$modifySoilDecision(
		BlockState state,
		LevelReader level,
		BlockPos pos,
		CallbackInfoReturnable<Boolean> cir
	) {
		if (!cir.getReturnValue()) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					if (x == 0 && z == 0)
						continue;
					BlockPos neighbor = pos.offset(x, -1, z);
					// During world generation the level is a WorldGenRegion whose
					// getBlockState throws "Requested chunk unavailable during world
					// generation" for positions whose chunk lies outside the region
					// cache. Guard the access so mushrooms placed at the edge of a
					// generated chunk do not crash the server (seen with C2ME).
					if (level.hasChunk(SectionPos.blockToSectionCoord(neighbor.getX()), SectionPos.blockToSectionCoord(neighbor.getZ()))
						&& level.getBlockState(neighbor).is(TFBlocks.TWILIGHT_PORTAL)) {
						cir.setReturnValue(true);
						return;
					}
				}
			}
		}
	}
}
