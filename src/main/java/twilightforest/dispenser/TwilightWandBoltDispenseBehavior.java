package twilightforest.dispenser;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DispenseBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.entity.projectile.TwilightWandBolt;

public class TwilightWandBoltDispenseBehavior extends DamageableStackDispenseBehavior {

	public static final TwilightWandBoltDispenseBehavior INSTANCE = new TwilightWandBoltDispenseBehavior();
	public static final MapCodec<TwilightWandBoltDispenseBehavior> CODEC = MapCodec.unit(INSTANCE);

	@Override
	protected Projectile getProjectileEntity(Level level, Position position, ItemStack stack) {
		return new TwilightWandBolt(level, position.x(), position.y(), position.z());
	}

	@Override
	protected int getDamageAmount() {
		return 1;
	}

	@Override
	protected SoundEvent getFiredSound() {
		return SoundEvents.GHAST_SHOOT;
	}

	@Override
	protected float getProjectileInaccuracy() {
		return 6.0F;
	}

	@Override
	public MapCodec<? extends DispenseBehavior> codec() {
		return CODEC;
	}
}
