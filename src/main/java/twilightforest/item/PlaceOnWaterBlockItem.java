package twilightforest.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Replacement for the vanilla PlaceOnWaterBlockItem, which was removed in 26.4.
 */
public class PlaceOnWaterBlockItem extends BlockItem {

	public PlaceOnWaterBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
		BlockHitResult blockAboveResult = hitResult.withPosition(hitResult.getBlockPos().above());
		return super.useOn(new UseOnContext(player, hand, blockAboveResult));
	}
}
