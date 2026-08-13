package twilightforest.block.livingblock;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.entity.livingblock.interact.OnInteract;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.phys.Vec3;
import twilightforest.inventory.UncraftingMenu;

/**
 * LivingBlock interaction for the Uncrafting Table.
 * <p>
 * Mirrors {@link twilightforest.block.UncraftingTableBlock#getMenuProvider} except that,
 * because a LivingBlock has no fixed block position, we pass the LivingBlock itself into
 * the menu so that {@code stillValid} can check {@code livingBlock.isAlive()} instead of
 * verifying the block at a position.
 */
public class OpenUncraftingTableInteraction implements OnInteract {
	private static final Component CONTAINER_TITLE = Component.translatable("container.twilightforest.uncrafting_table");

	@Override
	public InteractionResult apply(Player player, InteractionHand hand, Vec3 loc, LivingBlock entity) {
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.openMenu(new SimpleMenuProvider(
				(id, inventory, p) -> new UncraftingMenu(id, inventory, p.level(), ContainerLevelAccess.create(entity.level(), entity.blockPosition()), entity),
				CONTAINER_TITLE
			));
			serverPlayer.awardStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
		}
		return InteractionResult.SUCCESS;
	}
}