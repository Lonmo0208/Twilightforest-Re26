package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.util.PlayerHelper;

public class AuroraBrickBlock extends Block {
	public AuroraBrickBlock(Properties properties) {
		super(properties);
	}

	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
		return AuroraBrickBlock.hasGlacierProgression(player) ? 0.1F : super.getDestroyProgress(state, player, getter, pos);
	}

	// Shared so pillars and slabs from the Aurora Palace also become easy to mine after the glacier progression
	public static boolean hasGlacierProgression(Player player) {
		return PlayerHelper.doesPlayerHaveRequiredAdvancements(player, TwilightForestMod.prefix("progress_glacier"));
	}
}
