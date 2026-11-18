package twilightforest.dispenser;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseBehavior;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import twilightforest.item.TransformPowderItem;

public class TransformationDispenseBehavior implements DispenseBehavior {

	public static final TransformationDispenseBehavior INSTANCE = new TransformationDispenseBehavior();
	public static final MapCodec<TransformationDispenseBehavior> CODEC = MapCodec.unit(INSTANCE);

	@Override
	public DispenseResult execute(BlockSource source, ItemStack stack) {
		Level level = source.level();
		BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		boolean fired = false;
		for (LivingEntity livingentity : level.getEntitiesOfClass(LivingEntity.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS)) {
			if (TransformPowderItem.transformEntityIfPossible(livingentity, null, stack, true)) {
				fired = true;
			}
		}
		return fired ? DispenseResult.success(stack) : DispenseResult.failure();
	}

	@Override
	public MapCodec<? extends DispenseBehavior> codec() {
		return CODEC;
	}
}
