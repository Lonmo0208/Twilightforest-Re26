package twilightforest.mixin;

import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.chunk.status.ChunkStep;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.WorldgenHooks;
import twilightforest.init.custom.ChunkBlanketProcessors;

import java.util.concurrent.CompletableFuture;

/**
 * Replaces the NeoForge-only ASM hook (ChunkStatusTaskTransformer) which called
 * {@link WorldgenHooks#chunkBlanketing} after {@code ChunkGenerator.buildSurface}.
 * <p>
 * Without this, the canopy/glacier chunk blanket processors (dark forest canopy,
 * glacier packed-ice blanket) never run, so e.g. the Aurora Palace's glacier is
 * missing.
 */
@Mixin(ChunkStatusTasks.class)
public class ChunkStatusTasksMixin {

	@Inject(method = "generateSurface", at = @At("RETURN"))
	private static void tf$chunkBlanketing(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir) {
		// Rebuild the WorldGenRegion exactly as generateSurface does internally
		// (new WorldGenRegion(context.level(), chunks, step, chunk)).
		WorldGenRegion region = new WorldGenRegion(context.level(), chunks, step, chunk);
		ChunkBlanketProcessors.chunkBlanketing(chunk, region);
	}
}
