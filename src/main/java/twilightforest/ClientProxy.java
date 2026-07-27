package twilightforest;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Client-side proxy that provides safe access to client-only functionality.
 * Fields are initialized by ClientProxyInitializer in the client source set.
 */
public class ClientProxy {

	@FunctionalInterface
	public interface UberousSoilAnimator {
		void animate(BlockState state, Level level, BlockPos pos, RandomSource rand);
	}

	public interface LifedrainTrailRenderer {
		void makeRedMagicTrail(Level level, LivingEntity source, Vec3 target);
		Vec3 getPlayerHandPos(LivingEntity living, float partialTicks);
	}

	@FunctionalInterface
	public interface PortalAttachmentHandler {
		boolean handlePortal(Player player);
	}

	@FunctionalInterface
	public interface TFBiomeMapGenerator {
		void createMap(CommandSourceStack source, int width, int height, boolean show);
	}

	public interface AdvancementChecker {
		Object getAdvancement(Object player, Object advancementLocation);
		boolean doesPlayerHaveRequiredAdvancement(Object player, Object holder);
		boolean playerHasRequiredAdvancements(Object player, Iterable<Object> requiredAdvancements);
	}

	public static UberousSoilAnimator uberousSoilAnimator = (state, level, pos, rand) -> {};
	public static LifedrainTrailRenderer lifedrainTrailRenderer = null;
	public static PortalAttachmentHandler portalAttachmentHandler = player -> false;
	public static TFBiomeMapGenerator biomeMapGenerator = (source, w, h, show) -> {};
	public static AdvancementChecker advancementChecker = null;
	public static boolean urGhastAlive = false;

	/**
	 * Client-side check for whether the player is holding Magic Beans.
	 * Set by the client initializer. Returns false on the server.
	 */
	public static boolean isPlayerHoldingMagicBeans() {
		return false;
	}
}