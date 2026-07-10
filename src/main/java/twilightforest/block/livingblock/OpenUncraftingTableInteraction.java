package twilightforest.block.livingblock;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.interact.OnInteract;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.phys.Vec3;
import twilightforest.inventory.UncraftingMenu;

public class OpenUncraftingTableInteraction implements OnInteract {
	private static final Component CONTAINER_TITLE = Component.translatable("container.twilightforest.uncrafting_table");

	@Override
	public InteractionResult apply(Player player, InteractionHand hand, Vec3 vec3, LivingBlock entity) {
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.openMenu(new SimpleMenuProvider(
				(id, inv, p) -> new UncraftingMenu(id, inv, p.level(), ContainerLevelAccess.create(entity.level(), entity.blockPosition()), entity),
				CONTAINER_TITLE
			));
			serverPlayer.awardStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
		}
		return InteractionResult.SUCCESS;
	}
}
