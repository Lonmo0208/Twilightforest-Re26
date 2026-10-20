package twilightforest.asmhooks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import twilightforest.block.CloudBlock;
import twilightforest.block.SnowLoggable;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.custom.TravellersModifiersManager;

/**
 * Behaviour hooks that NeoForge applies through ASM transformers. On Fabric they are called from mixins;
 * the javadoc on each method names the transformer it replaces so the two stay in sync.
 */
@SuppressWarnings({"JavadocReference", "unused"})
public class BlockHooks {

	/**
	 * {@link twilightforest.asm.transformers.cloud.IsRainingAtTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.Level#isRainingAt(BlockPos)}
	 */
	public static boolean isRainingAt(boolean isRaining, Level level, BlockPos pos) {
		return isRaining || hasRainyCloudAbove(level, pos);
	}

	/**
	 * Replaces {@link twilightforest.asm.transformers.cloud.IsRainingAtTransformer} for the precipitation query that
	 * 26.3's client uses to decide where rain/snow particles and weather columns are drawn
	 * ({@code ClientLevel#getPrecipitationAt}).
	 */
	public static Biome.Precipitation precipitationAt(Biome.Precipitation original, Level level, BlockPos pos) {
		return original == Biome.Precipitation.RAIN || !hasRainyCloudAbove(level, pos) ? original : Biome.Precipitation.RAIN;
	}

	/**
	 * A rainy cloud block above {@code pos} acts like open sky: the block drips rain below itself even when the
	 * position is enclosed (troll caves, cloud platforms, ...). Scanning stops at the first opaque block so only
	 * positions with a clear line to the cloud are affected.
	 */
	private static boolean hasRainyCloudAbove(Level level, BlockPos pos) {
		int distance = TFConfig.commonCloudBlockPrecipitationDistance;
		if (distance <= 0) return false;
		if (!level.hasChunkAt(pos)) return false; // do NOT try to load chunks while checking rain, that deadlocks worldgen

		LevelChunk chunk = level.getChunkAt(pos);
		for (int y = pos.getY(); y < pos.getY() + distance; y++) {
			BlockPos scanPos = pos.atY(y);
			BlockState state = chunk.getBlockState(scanPos);
			if (state.getBlock() instanceof CloudBlock cloudBlock && cloudBlock.getCurrentPrecipitation(scanPos, level, level.getRainLevel(1.0F)).getLeft() == Biome.Precipitation.RAIN) {
				return true;
			}
			if (Heightmap.Types.MOTION_BLOCKING.isOpaque().test(state)) {
				return false;
			}
		}

		return false;
	}

	/**
	 * {@link twilightforest.asm.transformers.snow.KeepGrassSnowyForSnowloggableBlocksTransformer}
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.block.SnowyBlock#isSnowySetting(BlockState)}<br/>
	 * Targets: IRETURN
	 */
	public static boolean keepSnowyStateForSnowloggableBlocks(boolean original, BlockState state) {
		return original || (state.getBlock() instanceof SnowLoggable && state.getValue(SnowLoggable.SNOW_LAYERS) > 0);
	}

	/**
	 * {@link twilightforest.asm.transformers.lead.LeashFenceKnotSurvivesTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.decoration.LeashFenceKnotEntity#survives()}
	 */
	public static boolean leashFenceKnotSurvives(boolean original, LeashFenceKnotEntity entity) {
		if (original)
			return true; // Short-circuit to avoid an unnecessary #getBlockState call
		BlockState fenceState = entity.level().getBlockState(entity.getPos());
		return fenceState.is(TFBlocks.WROUGHT_IRON_FENCE) && fenceState.getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.NONE;
	}

	/**
	 * {@link twilightforest.asm.transformers.block.SlimeBlockMomentumTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.block.SlimeBlock#stepOn(Level, BlockPos, BlockState, Entity)}
	 * Targets: {@link Entity#isSteppingCarefully()}
	 */
	public static boolean resetSlimeMomentumWithUnrestrained(boolean original, Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER)) {
			return true; // dont return false here as the original check is looking that an entity is NOT stepping carefully
		}
		return original;
	}

	/**
	 * {@link twilightforest.asm.transformers.block.SlimeBlockBounceUpTransformer}<p/>
	 * <p>
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.block.SlimeBlock#bounceUp(Entity)}
	 */
	public static void stopBouncing(Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) && entity.getDeltaMovement().y() > -0.08)
			entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x, Math.max(0, entity.getDeltaMovement().y), entity.getDeltaMovement().z));
	}
}
