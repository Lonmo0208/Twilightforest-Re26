package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.TFTrappedChestBlockEntity;
import twilightforest.init.TFBlockEntities;

public class TFTrappedChestBlock extends ChestBlock {

	public TFTrappedChestBlock(Properties properties) {
		super(() -> TFBlockEntities.TF_TRAPPED_CHEST, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, properties);
	}

	@Override
	public BlockEntityType<? extends ChestBlockEntity> blockEntityType() {
		return TFBlockEntities.TF_TRAPPED_CHEST;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TFTrappedChestBlockEntity(pos, state);
	}

	@Override
	protected Stat<Identifier> getOpenChestStat() {
		return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
	}

	@Override
	protected boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return Mth.clamp(ChestBlockEntity.getOpenCount(level, pos), 0, 15);
	}

	@Override
	protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return direction == Direction.UP ? state.getSignal(level, pos, direction) : 0;
	}
}