package twilightforest.world.components.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

// Ideally, you should not be mixing this with other decorators unless you know what you're doing
// This litters memory with 256 block positions for each chunk. USE SPARINGLY
public class ChunkBlanketingModifier implements PlacementModifier {
	private static final Logger LOGGER = LoggerFactory.getLogger("twilightforest");

	public static final MapCodec<ChunkBlanketingModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.floatRange(0.0f, 1.0f).fieldOf("integrity").forGetter(o -> o.integrity),
		Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(o -> o.heightmap),
		RegistryCodecs.holderSet(Registries.BIOME).optionalFieldOf("biome_lock").forGetter(o -> o.biomeRLOptional)
	).apply(instance, ChunkBlanketingModifier::new));

	public final float integrity;
	public final Heightmap.Types heightmap;
	public final Optional<HolderSet<Biome>> biomeRLOptional;

	public ChunkBlanketingModifier(float integrity, Heightmap.Types heightmap, Optional<HolderSet<Biome>> biomeRLOptional) {
		this.integrity = integrity;
		this.heightmap = heightmap;
		this.biomeRLOptional = biomeRLOptional;
	}

	public static ChunkBlanketingModifier addThorns(HolderSet<Biome> thorns) {
		return new ChunkBlanketingModifier(1.0f, Heightmap.Types.WORLD_SURFACE_WG, Optional.of(thorns));
	}

	@Override
	public void modify(PlacementContext context, RandomSource random, BlockPos placement, Consumer<BlockPos> collector) {
		WorldGenLevel level = context.getLevel();
		ChunkAccess chunk = level.getChunk(placement);

		int chunkOriginX = chunk.getPos().getMinBlockX();
		int chunkOriginZ = chunk.getPos().getMinBlockZ();

		int count = 0;
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
			for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
				if (random.nextFloat() > this.integrity)
					continue;

				// Manually scan for the surface height instead of using chunk.getHeight
				// This is needed because the twilight forest dimension's custom surface building
				// may not properly populate the heightmap in 26.3
				int surfaceY = findSurfaceHeight(chunk, level, chunkOriginX + xInChunk, chunkOriginZ + zInChunk);
				BlockPos pos = new BlockPos(chunkOriginX + xInChunk, surfaceY + 1, chunkOriginZ + zInChunk);

				if (this.biomeRLOptional.isEmpty() || this.biomeRLOptional.get().contains(level.getBiome(pos))) {
					collector.accept(pos);
					count++;
				}
			}
		}

		if (count > 0) {
			LOGGER.info("[ThornsDebug] ChunkBlanketingModifier placed {} positions at chunk {} (integrity={}, biomeLock={})", count, chunk.getPos(), this.integrity, this.biomeRLOptional.isPresent());
		}
	}

	private int findSurfaceHeight(ChunkAccess chunk, WorldGenLevel level, int blockX, int blockZ) {
		int maxY = level.getMaxY();
		int minY = level.getMinY();

		// Scan from top down to find the highest non-air block
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		for (int y = maxY; y >= minY; y--) {
			mutablePos.set(blockX, y, blockZ);
			if (!chunk.getBlockState(mutablePos).isAir()) {
				return y;
			}
		}
		return minY;
	}

	@Override
	public MapCodec<? extends PlacementModifier> codec() {
		return CODEC;
	}
}