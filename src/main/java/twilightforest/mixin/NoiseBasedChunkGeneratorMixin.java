package twilightforest.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFDimension;

import java.util.Set;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin {

	@Inject(method = "buildSurface", at = @At("HEAD"), cancellable = true)
	private void twilightforest$buildSurface(WorldGenRegion region, StructureManager structureManager,
			RandomState randomState, ChunkAccess protoChunk, CallbackInfo ci) {
		if (TFDimension.isTwilightPortalDestination(region.getLevel())) {
			if (!SharedConstants.debugVoidTerrain(protoChunk.getPos()) && !SharedConstants.DEBUG_DISABLE_SURFACE) {
				NoiseBasedChunkGenerator self = (NoiseBasedChunkGenerator) (Object) this;
				WorldGenerationContext context = new WorldGenerationContext(self, region);
				self.buildSurface(protoChunk, context, randomState, structureManager,
						region.getBiomeManager(), Blender.of(region), null);
			}
			ci.cancel();
		}
	}
}