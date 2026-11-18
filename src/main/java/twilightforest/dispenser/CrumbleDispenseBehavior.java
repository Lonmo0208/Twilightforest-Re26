package twilightforest.dispenser;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFDataMaps;
import twilightforest.util.datamaps.DataMapType;

public class CrumbleDispenseBehavior implements DispenseBehavior {

	public static final CrumbleDispenseBehavior INSTANCE = new CrumbleDispenseBehavior();
	public static final MapCodec<CrumbleDispenseBehavior> CODEC = MapCodec.unit(INSTANCE);

	@Override
	public DispenseResult execute(BlockSource source, ItemStack stack) {
		ServerLevel level = source.level();
		BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		BlockState state = level.getBlockState(pos);
		if (!(stack.getMaxDamage() == stack.getDamageValue() + 1)) {
			var resultBlock = DataMapType.getData(state.getBlock().builtInRegistryHolder(), TFDataMaps.CRUMBLE_HORN);
			if (resultBlock != null) {
				if (resultBlock.result() == Blocks.AIR) {
					level.destroyBlock(pos, true);
				} else {
					level.setBlock(pos, resultBlock.result().withPropertiesOf(state), Block.UPDATE_ALL);
					level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				}

				stack.hurtAndBreak(1, level, null, item -> {});
				return DispenseResult.success(stack);
			}
		}
		return DispenseResult.failure();
	}

	@Override
	public MapCodec<? extends DispenseBehavior> codec() {
		return CODEC;
	}
}
