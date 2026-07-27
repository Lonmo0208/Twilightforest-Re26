package twilightforest.util;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlayerHelper {

	@Nullable
	public static AdvancementHolder getAdvancement(Player player, Identifier advancementLocation) {
		if (player instanceof ServerPlayer serverPlayer) {
			ServerLevel world = serverPlayer.level();
			return world.getServer().getAdvancements().get(advancementLocation);
		}

		return null;
	}

	public static boolean doesPlayerHaveRequiredAdvancement(Player player, @Nullable AdvancementHolder holder) {
		if (player instanceof ServerPlayer) {
			return holder != null && ((ServerPlayer) player).getAdvancements().getOrStartProgress(holder).isDone();
		}
		return false;
	}

	public static boolean doesPlayerHaveRequiredAdvancements(Player player, List<Identifier> requiredAdvancements) {
		return PlayerHelper.playerHasRequiredAdvancements(player, requiredAdvancements);
	}

	public static boolean doesPlayerHaveRequiredAdvancements(Player player, Identifier... requiredAdvancements) {
		return PlayerHelper.playerHasRequiredAdvancements(player, List.of(requiredAdvancements));
	}

	public static boolean playerHasRequiredAdvancements(Player player, Iterable<Identifier> requiredAdvancements) {
		for (Identifier advancementLocation : requiredAdvancements) {
			if (player instanceof ServerPlayer sp) {
				ServerLevel world = (ServerLevel) player.level();
				AdvancementHolder adv = world.getServer().getAdvancements().get(advancementLocation);
				return adv != null && sp.getAdvancements().getOrStartProgress(adv).isDone();
			}
			return false;
		}
		return true;
	}
}
