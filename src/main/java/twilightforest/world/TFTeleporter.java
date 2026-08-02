package twilightforest.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.Nullable;
import twilightforest.block.TFPortalBlock;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDimension;
import twilightforest.tags.TFBlockTags;
import twilightforest.tags.TFStructureTags;
import twilightforest.util.Restriction;
import twilightforest.util.iterators.DiagonalSpiralIterator;
import twilightforest.util.iterators.XZQuadrantIterator;
import twilightforest.util.landmarks.LandmarkUtil;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.util.*;
import java.util.function.Predicate;

public class TFTeleporter {

	private static final Logger LOGGER = LogManager.getLogger();

	public static TeleportTransition createTransition(Entity entity, ServerLevel dest, BlockPos pos, boolean forcedEntry) {
		TeleporterCache cache = TeleporterCache.get(dest);
		TeleportTransition transition = placeInExistingPortal(cache, dest, entity, pos);

		if (transition == null) {
			LOGGER.debug("Did not find existing portal, making a new one.");
			transition = createPosition(dest, entity, pos, cache, forcedEntry);
		}

		if (transition != null)
			return transition;

		// loadSurroundingArea() synchronously prepares the destination area, so createPosition()
		// above should always have produced a portal. If it somehow still failed, defer and let
		// PortalProcessor retry rather than teleporting the player without a portal.
		LOGGER.info("Deferring portal transition for {}: no destination portal could be prepared", entity.getName().getString());
		return null;
	}

	@Nullable
	protected static TeleportTransition createPosition(ServerLevel dest, Entity entity, BlockPos destPos, TeleporterCache cache, boolean locked) {
		// Ensure the destination area is generated first (synchronous 3x3 load, mirrors vanilla
		// PortalForcer). Without this, moveToSafeCoords()'s getBiome()/getBlockState() calls would
		// synchronously load chunks on the server thread and trip the Purpur Watchdog (issue #4).
		if (!loadSurroundingArea(dest, Vec3.atCenterOf(destPos))) {
			return null;
		}

		Vec3 safePos = moveToSafeCoords(dest, entity, destPos);
		BlockPos portalPos = makePortal(cache, entity, dest, safePos, locked);
		if (portalPos == null) {
			return null;
		}
		return placeInExistingPortal(cache, dest, entity, portalPos);
	}

	@Nullable
	protected static TeleportTransition placeInExistingPortal(TeleporterCache cache, ServerLevel destDim, Entity entity, BlockPos pos) {
		boolean flag = true;
		BlockPos blockpos;
		ColumnPos columnPos = new ColumnPos(entity.blockPosition().getX(), entity.blockPosition().getZ()); // Must be the position from the src dim

		PortalPosition portalPosition = cache.getPortalPosition(destDim.dimension().identifier(), columnPos);
		if (portalPosition != null) {
			blockpos = portalPosition.pos;
			portalPosition.lastUpdateTime = destDim.getGameTime();
			flag = false;
			// Validate that the Portal still exists
			LOGGER.debug("Using cache, validating. {}", blockpos);
			// Don't call getBlockState() on a chunk that isn't loaded: it would synchronously
			// force-load an arbitrary (possibly distant) chunk on the server thread. A cached
			// portal in an unloaded chunk is treated as invalid so it gets recreated nearby.
			if (blockpos == null || !isChunkReady(destDim, ChunkPos.containing(blockpos)) || !destDim.getBlockState(blockpos).is(TFBlocks.TWILIGHT_PORTAL)) {
				// Portal was broken, we need to recreate it.
				LOGGER.debug("Portal Invalid, recreating.");
				blockpos = null;
				cache.removeInvalidPos(destDim.dimension().identifier(), columnPos);
			}
		} else {
			blockpos = getPortalPosition(destDim, pos);
		}

		if (blockpos == null) {
			return null;
		} else {
			if (flag) {
				LOGGER.debug("Caching Src Portal Blocks to {}", blockpos);
				Map<BlockPos, Boolean> portalBlocks = new HashMap<>();
				portalBlocks.put(entity.blockPosition(), true);
				TFPortalBlock.recursivelyValidatePortal(entity.level(), entity.blockPosition(), portalBlocks, new MutableInt(0), entity.level().getBlockState(entity.blockPosition()));
				BlockPos finalBlockpos = blockpos;
				portalBlocks.forEach((blockPos, b) -> {
					if (b) {
						LOGGER.debug("Caching {}", blockPos);
						cache.addBlockToCache(destDim.dimension(), new ColumnPos(blockPos.getX(), blockPos.getZ()), new PortalPosition(finalBlockpos, destDim.getGameTime()));
					}
				});
				destDim.getChunkSource().addTicketWithRadius(TicketType.PORTAL, ChunkPos.containing(blockpos), 3);
			}

			// replace with our own placement logic
			BlockPos[] portalBorder = getBoundaryPositions(destDim, blockpos).toArray(new BlockPos[0]);
			BlockPos borderPos;
			if (portalBorder.length > 0) {
				borderPos = portalBorder[destDim.getRandom().nextInt(portalBorder.length)];
			} else {
				borderPos = blockpos;
			}

			double portalX = borderPos.getX() + 0.5;
			double portalY = borderPos.getY() + 1.0;
			double portalZ = borderPos.getZ() + 0.5;

			return makeTransition(destDim, entity, safePosInColumn(destDim, entity, portalX, portalY, portalZ));
		}
	}

	@Nullable
	private static BlockPos getPortalPosition(ServerLevel destDim, BlockPos pos) {
		int i = 200; // scan radius up to 200, and also un-inline this variable back into below
		double d0 = Double.MAX_VALUE;
		BlockPos result = null;

		for (int i1 = -i; i1 <= i; ++i1) {
			BlockPos blockpos2;

			for (int j1 = -i; j1 <= i; ++j1) {

				// skip positions outside current world border (MC-114796)
				if (!destDim.getWorldBorder().isWithinBounds(pos.offset(i1, 0, j1))) {
					continue;
				}

				ChunkPos chunkPos = ChunkPos.containing(pos.offset(i1, 0, j1));
				LevelChunk chunk = destDim.getChunkSource().getChunkNow(chunkPos.x(), chunkPos.z());

				// skip chunks that aren't generated
				if (chunk == null || chunk.getFullStatus() == FullChunkStatus.INACCESSIBLE) {
					continue;
				}

				for (BlockPos blockpos1 = pos.offset(i1, getScanHeight(destDim, pos) - pos.getY(), j1); blockpos1.getY() >= destDim.getMinY(); blockpos1 = blockpos2) {
					blockpos2 = blockpos1.below();

					// don't lookup state if inner condition would fail
					if (d0 >= 0.0D && blockpos1.distSqr(pos) >= d0) {
						continue;
					}

					// use our portal block
					if (isPortal(chunk.getBlockState(blockpos1))) {
						for (blockpos2 = blockpos1.below(); isPortal(chunk.getBlockState(blockpos2)); blockpos2 = blockpos2.below()) {
							blockpos1 = blockpos2;
						}

						float d1 = (float) blockpos1.distSqr(pos);
						if (d0 < 0.0D || d1 < d0) {
							d0 = d1;
							result = blockpos1;
							// restrict search radius to new distance
							i = Mth.ceil(Mth.sqrt(d1));
						}
					}
				}
			}
		}
		return result;
	}

	private static int getScanHeight(ServerLevel world, BlockPos pos) {
		return getScanHeight(world, pos.getX(), pos.getZ());
	}

	private static int getScanHeight(ServerLevel world, int x, int z) {
		int worldHeight = world.getMaxY() - 1;
		// Use getChunkNow: getChunk() synchronously generates the chunk, and findPortalCoords()
		// calls this per-column (up to 1024x), blocking the server thread for a very long time
		// on Moonrise/Purpur where a single stuck generation task hangs syncLoad forever.
		LevelChunk chunk = world.getChunkSource().getChunkNow(x >> 4, z >> 4);
		if (chunk == null) {
			return worldHeight;
		}
		//FIXME find an alternative to getHighestSectionPosition, its marked for removal
		@SuppressWarnings("removal")
		int chunkHeight = chunk.getHighestSectionPosition() + 15;
		return Math.min(worldHeight, chunkHeight);
	}

	private static boolean isPortal(BlockState state) {
		return state.is(TFBlocks.TWILIGHT_PORTAL);
	}

	// from the start point, builds a set of all directly adjacent non-portal blocks
	private static Set<BlockPos> getBoundaryPositions(ServerLevel world, BlockPos start) {
		Set<BlockPos> result = new HashSet<>(), checked = new HashSet<>();
		checked.add(start);
		checkAdjacent(world, start, checked, result);
		return result;
	}

	private static void checkAdjacent(ServerLevel world, BlockPos pos, Set<BlockPos> checked, Set<BlockPos> result) {
		for (Direction facing : Direction.Plane.HORIZONTAL) {
			BlockPos offset = pos.relative(facing);
			if (!checked.add(offset))
				continue;
			BlockState checkState = world.getBlockState(offset);
			if (isPortal(checkState)) {
				checkAdjacent(world, offset, checked, result);
			} else {
				if (Block.isFaceFull(checkState.getCollisionShape(world, offset), Direction.UP) && world.getBlockState(offset.above()).getCollisionShape(world, offset.above()).isEmpty()) {
					result.add(offset);
				}
			}
		}
	}

	protected static boolean isPortalAt(ServerLevel world, BlockPos pos) {
		return isPortal(world.getBlockState(pos));
	}

	// Scale the coords based on the dimension type coordinate_scale
	protected static double getHorizontalScale(ServerLevel destination) {
		ServerLevel tfDim = destination.getServer().getLevel(TFDimension.DIMENSION_KEY);
		double scale = tfDim == null ? 0.125D : tfDim.dimensionType().coordinateScale();
		return destination.dimension().equals(TFDimension.DIMENSION_KEY) ? 1F / scale : scale;
	}

	protected static Vec3 moveToSafeCoords(ServerLevel level, Entity entity, BlockPos pos) {
		// if we're in enforced progression mode, check the biomes for safety
		boolean checkProgression = LandmarkUtil.isProgressionEnforced(level);

		if (isSafeAround(level, pos, entity, checkProgression)) {
			LOGGER.debug("Portal destination looks safe!");
			return safePosInColumn(level, entity, Vec3.atCenterOf(pos));
		}

		LOGGER.debug("Portal destination looks unsafe, rerouting!");

		BlockPos newPos = scanIntoSafeBiomes(level, pos, entity, checkProgression);
		if (newPos != null) {
			LOGGER.debug("Successfully found safe biome");
			return safePosInColumn(level, entity, Vec3.atCenterOf(newPos));
		}

		LOGGER.warn("Did not find a safe portal spot.");

		return safePosInColumn(level, entity, Vec3.atCenterOf(pos));
	}

	@Nullable
	private static BlockPos scanIntoSafeBiomes(ServerLevel level, BlockPos pos, Entity entity, boolean checkProgression) {
		Iterable<BlockPos> biomeCenterGrid = new DiagonalSpiralIterator<>(pos.getX() >> 4, pos.getZ() >> 4, false, 128, 16, LegacyLandmarkPlacements::getNearestCenterXZ);
		// Iterator loops over a 9x9 grid of biomes' random centers, maintaining an approximate order of proximity
		for (BlockPos biomeCenter : biomeCenterGrid) {
			// Check biome overlapping structure center
			if (checkProgression && biomeUnsafe(level, biomeCenter, entity)) {
				continue;
			}

			// Searches every chunk in a 17x17 grid around the center, inside the biome cell
			Iterable<BlockPos> gridAroundLandmark = new XZQuadrantIterator<>(biomeCenter.getX(), biomeCenter.getZ(), true, 128, 16, (x, z) -> new BlockPos(x, 4, z));
			for (BlockPos posInBiome : gridAroundLandmark) {
				if (isSafeAround(level, posInBiome, entity, checkProgression)) {
					LOGGER.debug("Found {} in biome-scanning for safe portal placement", posInBiome.toShortString());
					return posInBiome;
				}
			}
		}

		return null;
	}

	public static boolean isSafeAround(Level world, BlockPos pos, Entity entity, boolean checkProgression) {
		// Never claim safety for chunks that aren't loaded: the biome lookup below would
		// synchronously force-load an arbitrary distant chunk (Purpur Watchdog). The biome-safety
		// scan then naturally confines itself to the already-loaded destination area.
		if (world instanceof ServerLevel serverLevel) {
			LevelChunk chunk = serverLevel.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4);
			if (chunk == null || chunk.getFullStatus() == FullChunkStatus.INACCESSIBLE) {
				return false;
			}
		}

		if (isUnsafe(world, pos, entity, checkProgression)) {
			return false;
		}

		for (Direction facing : Direction.Plane.HORIZONTAL) {
			if (isUnsafe(world, pos.relative(facing, 16), entity, checkProgression)) {
				return false;
			}
		}

		return true;
	}

	private static boolean isUnsafe(Level world, BlockPos pos, Entity entity, boolean checkProgression) {
		if (!world.dimension().equals(TFDimension.DIMENSION_KEY)) {
			return false;
		}

		if (isOutsideBorder(world, pos)) {
			return true;
		}

		if (checkProgression && biomeUnsafe(world, pos, entity)) {
			return true;
		}

		return posOverlapsRestrictedStructureChunk(world, pos);
	}

	private static boolean isOutsideBorder(Level world, BlockPos pos) {
		return !world.getWorldBorder().isWithinBounds(pos);
	}

	public static boolean posOverlapsRestrictedStructureChunk(Level destLevel, BlockPos pos) {
		Iterator<Holder<Structure>> landmarksInChunk = destLevel.registryAccess().lookup(Registries.STRUCTURE)
			.flatMap(r -> r.get(TFStructureTags.LANDMARK))
			.map(HolderSet.ListBacked::iterator)
			.orElse(Collections.emptyIterator());

		// Use getChunkNow instead of getChunkAt: the latter synchronously loads the chunk and can
		// block the server thread indefinitely if the chunk system is unhealthy (Moonrise), which
		// caused a Watchdog timeout (GitHub issue #4). Missing chunks are treated as safe.
		LevelChunk chunkAt = destLevel.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4);
		if (chunkAt == null) {
			return false;
		}

		while (landmarksInChunk.hasNext()) {
			Holder<Structure> structureHolder = landmarksInChunk.next();
			if (!chunkAt.getReferencesForStructure(structureHolder.value()).isEmpty()) {
				return true;
			}
		}

		return false;
	}

	private static boolean biomeUnsafe(Level world, BlockPos pos, Entity entity) {
		// Don't force-load a distant chunk just to check its biome: getBiome() would synchronously
		// generate it on the server thread (Purpur Watchdog). Unloaded chunks count as safe, and
		// isSafeAround() separately rejects unloaded positions so the safety scan stays local.
		if (world instanceof ServerLevel serverLevel) {
			LevelChunk chunk = serverLevel.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4);
			if (chunk == null || chunk.getFullStatus() == FullChunkStatus.INACCESSIBLE) {
				return false;
			}
		}
		return !Restriction.isBiomeSafeFor(world.getBiome(pos).value(), entity);
	}

	protected static BlockPos makePortal(TeleporterCache cache, Entity entity, ServerLevel world, Vec3 pos, boolean locked) {
		ServerLevel src = entity.level() instanceof ServerLevel serverLevel ? serverLevel : null;

		// ensure area is populated first - non-blocking; if chunks aren't ready yet, defer to next tick
		if (!loadSurroundingArea(world, pos)) {
			LOGGER.debug("Surrounding chunks not ready yet, deferring portal creation for {}", entity.getName().getString());
			return null;
		}

		BlockPos spot = findPortalCoords(world, pos, blockPos -> isPortalAt(world, blockPos));
		String name = entity.getName().getString();

		if (spot != null) {
			LOGGER.debug("Found existing portal for {} at {}", name, spot);
			cacheNewPortalCoords(cache, src, spot, entity.blockPosition());
			return spot;
		}

		spot = findPortalCoords(world, pos, blockpos -> isIdealForPortal(world, blockpos));

		if (spot != null) {
			LOGGER.debug("Found ideal portal spot for {} at {}", name, spot);
			cacheNewPortalCoords(cache, src, makePortalAt(world, spot, locked), entity.blockPosition());
			return spot;
		}

		LOGGER.debug("Did not find ideal portal spot, shooting for okay one for {}", name);
		spot = findPortalCoords(world, pos, blockPos -> isOkayForPortal(world, blockPos));

		if (spot != null) {
			LOGGER.debug("Found okay portal spot for {} at {}", name, spot);
			cacheNewPortalCoords(cache, src, makePortalAt(world, spot, locked), entity.blockPosition());
			return spot;
		}

		LOGGER.debug("Did not even find an okay portal spot, just making a fallback one for {}", name);

		spot = findPortalCoords(world, pos, blockpos -> isOkayForFallbackPortal(world, blockpos), true);
		if (spot != null) {
			LOGGER.debug("Found fallback portal spot for {} at {}", name, spot);
			cacheNewPortalCoords(cache, src, makePortalAt(world, spot, locked), entity.blockPosition());
			return spot;
		}

		// well I don't think we can actually just return and fail here
		LOGGER.debug("Did not even find a fallback portal spot, just making a random one for {}", name);

		BlockPos horizontallyScaled = BlockPos.containing(entity.getX() * getHorizontalScale(world), entity.getY(), entity.getZ() * getHorizontalScale(world));
		spot = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, horizontallyScaled);
		// Search downward for a grass or dirt surface through trees
		int searchLimit = 100;
		while (searchLimit-- > 0 && spot.getY() > world.getMinY() && !world.getBlockState(spot.below()).is(BlockTags.DIRT) && !world.getBlockState(spot).is(BlockTags.DIRT)) {
			spot = spot.below();
		}
		// Ensure we are not on a tree - skip down past any leaves or logs
		while (spot.getY() > world.getMinY() && (world.getBlockState(spot).is(BlockTags.LEAVES) || world.getBlockState(spot).is(BlockTags.LOGS) || world.getBlockState(spot.below()).is(BlockTags.LEAVES) || world.getBlockState(spot.below()).is(BlockTags.LOGS))) {
			spot = spot.below();
		}
		cacheNewPortalCoords(cache, src, makePortalAt(world, spot, locked), entity.blockPosition());
		return spot;
	}

	/**
	 * Ensures the destination area is generated so the portal can be placed and scanned.
	 * <p>
	 * The 3x3 chunk area around {@code pos} is loaded synchronously, mirroring what vanilla
	 * {@code PortalForcer} does for nether portals: on Moonrise/Purpur this blocks the server
	 * thread until generation completes, but generation runs on the worker threads and finishes
	 * in a bounded time (typically a few hundred ms to a couple of seconds). The previous
	 * "one chunk per tick" approach was catastrophic because vanilla applies a 300-tick portal
	 * cooldown whenever {@code getPortalDestination} returns {@code null}, so every deferred
	 * attempt cost ~15 seconds of waiting — teleports took minutes or never completed.
	 * <p>
	 * The surrounding 5x5 ring is only requested via background tickets: missing chunks there
	 * are treated as safe by the (now non-blocking) biome/structure scans.
	 *
	 * @return {@code true} if the core area is ready (or {@code false} only on a generation failure)
	 */
	protected static boolean loadSurroundingArea(ServerLevel world, Vec3 pos) {

		int x = Mth.floor(pos.x()) >> 4;
		int z = Mth.floor(pos.z()) >> 4;

		int missing = 0;
		try {
			for (int dx = -1; dx <= 1; dx++) {
				for (int dz = -1; dz <= 1; dz++) {
					ChunkPos chunkPos = new ChunkPos(x + dx, z + dz);
					if (!isChunkReady(world, chunkPos)) {
						missing++;
						world.getChunk(chunkPos.x(), chunkPos.z());
					}
				}
			}
		} catch (Exception e) {
			// A failing generation task (e.g. a mod conflict) must not crash the server tick;
			// defer the transition so PortalProcessor retries with the portal cooldown instead.
			LOGGER.error("Failed to load portal destination area around {} in {}; deferring transition", pos, world.dimension().identifier(), e);
			return false;
		}

		if (missing > 0) {
			LOGGER.info("Loaded {} chunk(s) for portal destination at ({}, {}) in {}", missing, x * 16, z * 16, world.dimension().identifier());
		}

		// Kick background generation for the outer ring (non-blocking; used only by scans that
		// treat missing chunks as safe).
		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				if (Math.abs(dx) <= 1 && Math.abs(dz) <= 1) {
					continue;
				}
				ChunkPos chunkPos = new ChunkPos(x + dx, z + dz);
				if (!isChunkReady(world, chunkPos)) {
					world.getChunkSource().addTicketWithRadius(TicketType.PORTAL, chunkPos, 2);
				}
			}
		}

		return true;
	}

	/**
	 * A chunk is "ready" once it is present in the cache AND has advanced past the INACCESSIBLE
	 * status; an INACCESSIBLE chunk would still trigger a synchronous load on the next
	 * getBiome/getBlockState call.
	 */
	private static boolean isChunkReady(ServerLevel world, ChunkPos chunkPos) {
		LevelChunk chunk = world.getChunkSource().getChunkNow(chunkPos.x(), chunkPos.z());
		return chunk != null && chunk.getFullStatus() != FullChunkStatus.INACCESSIBLE;
	}

	@Nullable
	protected static BlockPos findPortalCoords(ServerLevel world, Vec3 loc, Predicate<BlockPos> predicate) {
		return findPortalCoords(world, loc, predicate, false);
	}

	@Nullable
	protected static BlockPos findPortalCoords(ServerLevel world, Vec3 loc, Predicate<BlockPos> predicate, boolean makePortalInAir) {
		// adjust the height based on what world we're traveling to
		double yFactor = getYFactor(world);
		// modified copy of base Teleporter method:
		int entityX = Mth.floor(loc.x());
		int entityZ = Mth.floor(loc.z());

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		double spotWeight = -1D;
		BlockPos spot = null;

		int range = 16;
		for (int rx = entityX - range; rx <= entityX + range; rx++) {
			double xWeight = (rx + 0.5D) - loc.x();
			for (int rz = entityZ - range; rz <= entityZ + range; rz++) {
				double zWeight = (rz + 0.5D) - loc.z();

				for (int ry = getScanHeight(world, rx, rz); ry >= world.getMinY(); ry--) {


					pos.set(rx, ry, rz);
					if (!makePortalInAir && !world.isEmptyBlock(pos)) {
						continue;
					}

					if (makePortalInAir) {
						while (ry > world.getMinY() && world.isEmptyBlock(pos.set(rx, ry - 1, rz)) && predicate.test(pos)) {
							ry--;
						}
						pos.set(rx, ry, rz);
					} else {
						while (ry > world.getMinY() && world.isEmptyBlock(pos.set(rx, ry - 1, rz))) {
							ry--;
						}
					}

					double yWeight = (ry + 0.5D) - loc.y() * yFactor;
					double rPosWeight = xWeight * xWeight + yWeight * yWeight + zWeight * zWeight;

					if (spotWeight < 0.0D || rPosWeight < spotWeight) {
						// check from the "in ground" pos
						if (predicate.test(pos)) {
							spotWeight = rPosWeight;
							spot = pos.immutable();
						}
					}
				}
			}
		}

		return spot;
	}

	protected static double getYFactor(ServerLevel world) {
		return world.dimension().equals(Level.OVERWORLD) ? 2.0 : 0.5;
	}

	private static void cacheNewPortalCoords(TeleporterCache cache, @Nullable ServerLevel srcDim, BlockPos pos, BlockPos srcPos) {
		// src/dest is backwards logic because we're caching the opposite direction
		if (srcDim == null)
			return;
		BlockPos exitPos = getPortalPosition(srcDim, srcPos);
		if (exitPos == null)
			return;
		LOGGER.debug("Caching Dest Portal Blocks to {}", exitPos);
		cache.addBlockToCache(srcDim.dimension(), new ColumnPos(pos.getX(), pos.getZ()), new TFTeleporter.PortalPosition(exitPos, srcDim.getGameTime()));
		cache.addBlockToCache(srcDim.dimension(), new ColumnPos(pos.south().getX(), pos.south().getZ()), new TFTeleporter.PortalPosition(exitPos, srcDim.getGameTime()));
		cache.addBlockToCache(srcDim.dimension(), new ColumnPos(pos.east().getX(), pos.east().getZ()), new TFTeleporter.PortalPosition(exitPos, srcDim.getGameTime()));
		cache.addBlockToCache(srcDim.dimension(), new ColumnPos(pos.south().east().getX(), pos.south().east().getZ()), new TFTeleporter.PortalPosition(exitPos, srcDim.getGameTime()));
	}

	protected static boolean isIdealForPortal(ServerLevel world, BlockPos pos) {
		for (int potentialZ = 0; potentialZ < 4; potentialZ++) {
			for (int potentialX = 0; potentialX < 4; potentialX++) {
				for (int potentialY = 0; potentialY < 6; potentialY++) {
					BlockPos tPos = pos.offset(potentialX - 1, potentialY, potentialZ - 1);
					BlockState state = world.getBlockState(tPos);

					// all blocks mustn't be bedrock, end portal frame, etc.; and other conditions for layers >= 0
					if (state.is(BlockTags.FEATURES_CANNOT_REPLACE) || potentialY == 0 && !state.is(BlockTags.DIRT) || potentialY >= 1 && !state.canBeReplaced()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected static BlockPos makePortalAt(Level world, BlockPos pos, boolean locked) {
		LOGGER.info("Placing Twilight portal at {} in {}", pos, world.dimension().identifier());
		// grass all around it
		BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();

		world.setBlockAndUpdate(pos.west().north(), grass);
		world.setBlockAndUpdate(pos.north(), grass);
		world.setBlockAndUpdate(pos.east().north(), grass);
		world.setBlockAndUpdate(pos.east(2).north(), grass);

		world.setBlockAndUpdate(pos.west(), grass);
		world.setBlockAndUpdate(pos.east(2), grass);

		world.setBlockAndUpdate(pos.west().south(), grass);
		world.setBlockAndUpdate(pos.east(2).south(), grass);

		world.setBlockAndUpdate(pos.west().south(2), grass);
		world.setBlockAndUpdate(pos.south(2), grass);
		world.setBlockAndUpdate(pos.east().south(2), grass);
		world.setBlockAndUpdate(pos.east(2).south(2), grass);

		BlockPos[] positions = new BlockPos[4];
		positions[0] = pos.below();
		positions[1] = pos.east().below();
		positions[2] = pos.south().below();
		positions[3] = pos.east().south().below();

		// dirt under it
		BlockState dirt = Blocks.DIRT.defaultBlockState();
		for (BlockPos blockpos : positions) {
			BlockState state = world.getBlockState(blockpos);
			if (state.is(BlockTags.DIRT) || state.is(BlockTags.REPLACEABLE) || state.is(BlockTags.AIR))
				world.setBlockAndUpdate(blockpos, dirt);
		}

		// portal in it
		BlockState portal = TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState().setValue(TFPortalBlock.DISALLOW_RETURN, (locked || !TFConfig.shouldReturnPortalBeUsable));

		world.setBlock(pos, portal, Block.UPDATE_CLIENTS);
		world.setBlock(pos.east(), portal, Block.UPDATE_CLIENTS);
		world.setBlock(pos.south(), portal, Block.UPDATE_CLIENTS);
		world.setBlock(pos.east().south(), portal, Block.UPDATE_CLIENTS);

		// meh, let's just make a bunch of air over it for 4 squares
		for (int dx = -1; dx <= 2; dx++) {
			for (int dz = -1; dz <= 2; dz++) {
				for (int dy = 1; dy <= 5; dy++) {
					world.removeBlock(pos.offset(dx, dy, dz), false);
				}
			}
		}

		// finally, "nature decorations"!
		world.setBlock(pos.west().north().above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);
		world.setBlock(pos.north().above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);
		world.setBlock(pos.east().north().above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);
		world.setBlock(pos.east(2).north().above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);

		world.setBlock(pos.west().above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);
		world.setBlock(pos.east(2).above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);

		world.setBlock(pos.west().south().above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);
		world.setBlock(pos.east(2).south().above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);

		world.setBlock(pos.west().south(2).above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);
		world.setBlock(pos.south(2).above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);
		world.setBlock(pos.east().south(2).above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);
		world.setBlock(pos.east(2).south(2).above(), randNatureBlock(world.getRandom()), Block.UPDATE_CLIENTS);

		return pos;
	}

	private static BlockState randNatureBlock(RandomSource random) {
		Optional<Block> optional = BuiltInRegistries.BLOCK
			.get(TFBlockTags.GENERATED_PORTAL_DECO)
			.flatMap(tag -> tag.getRandomElement(random))
			.map(Holder::value);
		return optional.map(Block::defaultBlockState).orElseGet(Blocks.SHORT_GRASS::defaultBlockState);
	}

	protected static boolean isOkayForPortal(ServerLevel world, BlockPos pos) {
		for (int potentialZ = 0; potentialZ < 4; potentialZ++) {
			for (int potentialX = 0; potentialX < 4; potentialX++) {
				for (int potentialY = 0; potentialY < 6; potentialY++) {
					BlockPos tPos = pos.offset(potentialX - 1, potentialY, potentialZ - 1);
					BlockState state = world.getBlockState(tPos);

					// all blocks mustn't be bedrock, end portal frame, etc.; and other conditions for layers >= 0
					if (state.is(BlockTags.FEATURES_CANNOT_REPLACE) || potentialY == 0 && !state.is(BlockTags.DIRT) || potentialY >= 1 && !state.canBeReplaced()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected static boolean isOkayForFallbackPortal(ServerLevel world, BlockPos pos) {
		for (int potentialZ = 0; potentialZ < 4; potentialZ++) {
			for (int potentialX = 0; potentialX < 4; potentialX++) {
				for (int potentialY = 0; potentialY < 6; potentialY++) {
					BlockPos tPos = pos.offset(potentialX - 1, potentialY, potentialZ - 1);
					BlockState state = world.getBlockState(tPos);

					// all blocks mustn't be bedrock, end portal frame, etc.;
					if (state.is(BlockTags.FEATURES_CANNOT_REPLACE) || potentialY >= 1 && !state.canBeReplaced()) {
						return false;
					}
				}
				// Check that the ground below the portal is not a tree block
				BlockPos belowPos = pos.offset(potentialX - 1, -1, potentialZ - 1);
				BlockState belowState = world.getBlockState(belowPos);
				if (belowState.is(BlockTags.LOGS) || belowState.is(BlockTags.LEAVES)) {
					return false;
				}
			}
		}
		return true;
	}

	protected static Vec3 safePosInColumn(ServerLevel level, Entity entity, double x, double y, double z) {
		return safePosInColumn(level, entity, new Vec3(x, y, z));
	}

	protected static Vec3 safePosInColumn(ServerLevel level, Entity entity, Vec3 pos) {
		AABB aabb = entity.dimensions.makeBoundingBox(pos);

		if (level.noCollision(aabb)) {
			return pos;
		}

		int height = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) Math.round(pos.x), (int) Math.round(pos.z));
		return pos.with(Direction.Axis.Y, height);
	}

	protected static TeleportTransition makeTransition(ServerLevel level, Entity entity, Vec3 pos) {
		return new TeleportTransition(level, pos, Vec3.ZERO, entity.getYRot(), entity.getXRot(), TeleportTransition.PLACE_PORTAL_TICKET);
	}

	static class PortalPosition {
		public final BlockPos pos;
		long lastUpdateTime;

		PortalPosition(BlockPos pos, long time) {
			this.pos = pos;
			this.lastUpdateTime = time;
		}
	}
}
