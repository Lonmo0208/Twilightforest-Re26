package twilightforest.dispenser;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import twilightforest.util.TFItemStackUtils;

/**
 * Shoots a projectile from a dispenser but damages the stack instead of consuming it per shot.
 */
public abstract class DamageableStackDispenseBehavior implements DispenseBehavior {

	@Override
	public DispenseResult execute(BlockSource source, ItemStack stack) {
		ServerLevel level = source.level();
		Position pos = DispenserBlock.getDispensePosition(source);
		Direction direction = source.state().getValue(DispenserBlock.FACING);
		if (stack.getMaxDamage() >= stack.getDamageValue() + this.getDamageAmount()) {
			Projectile projectile = this.getProjectileEntity(level, pos, stack);
			projectile.shoot(direction.getStepX(), (float) direction.getStepY() + 0.1F, direction.getStepZ(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
			level.addFreshEntity(projectile);
			TFItemStackUtils.hurtButDontBreak(stack, 1, level, null);
			return DispenseResult.success(stack);
		}
		return DispenseResult.failure();
	}

	@Override
	public void playSuccessSound(BlockSource source) {
		source.level().playSound(null, source.center().x(), source.center().y(), source.center().z(), this.getFiredSound(), SoundSource.NEUTRAL, 1.0F, 1.0F);
	}

	protected abstract Projectile getProjectileEntity(Level level, Position position, ItemStack stack);

	protected abstract int getDamageAmount();

	protected abstract SoundEvent getFiredSound();

	//bigger inaccuracy is a good thing in this case, right?
	protected float getProjectileInaccuracy() {
		return 18.0F;
	}

	protected float getProjectileVelocity() {
		return 1.1F;
	}
}
