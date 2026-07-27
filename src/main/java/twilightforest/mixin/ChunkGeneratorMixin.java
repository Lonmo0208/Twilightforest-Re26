package twilightforest.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.util.WorldUtil;

/**
 * Fallback for vanilla's findNearestMapStructure when structures use custom LandmarkGridPlacement.
 * Vanilla's locate algorithm relies on spacing/separation from StructurePlacement to calculate
 * grid positions, but LandmarkGridPlacement uses Vec3i.ZERO spacing and a custom landmark-based
 * placement system. This mixin provides a fallback that directly searches landmark centers.
 */
@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {

	@Inject(method = "findNearestMapStructure", at = @At("RETURN"), cancellable = true)
	private void twilightforest$findNearestMapLandmark(ServerLevel level, HolderSet<Structure> targetStructures, BlockPos pos, int searchRadius, boolean skipKnownStructures, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir) {
		if (cir.getReturnValue() == null) {
			WorldUtil.findNearestMapLandmark(level, targetStructures, pos, searchRadius, skipKnownStructures).ifPresent(cir::setReturnValue);
		}
	}
}