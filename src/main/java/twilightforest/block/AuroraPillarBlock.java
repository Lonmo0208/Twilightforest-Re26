package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AuroraPillarBlock extends RotatedPillarBlock {
	public AuroraPillarBlock(Properties properties) {
		super(properties);
	}

	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
		return AuroraBrickBlock.hasGlacierProgression(player) ? 0.1F : super.getDestroyProgress(state, player, getter, pos);
	}
}