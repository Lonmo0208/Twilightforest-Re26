package twilightforest.dispenser;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DispenseBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.entity.projectile.MoonwormShot;
import twilightforest.init.TFSounds;

public class MoonwormShotDispenseBehavior extends DamageableStackDispenseBehavior {

	public static final MoonwormShotDispenseBehavior INSTANCE = new MoonwormShotDispenseBehavior();
	public static final MapCodec<MoonwormShotDispenseBehavior> CODEC = MapCodec.unit(INSTANCE);

	@Override
	protected Projectile getProjectileEntity(Level level, Position position, ItemStack stack) {
		return new MoonwormShot(level, position.x(), position.y(), position.z());
	}

	@Override
	protected int getDamageAmount() {
		return 2;
	}

	@Override
	protected SoundEvent getFiredSound() {
		return TFSounds.MOONWORM_SQUISH;
	}

	@Override
	public MapCodec<? extends DispenseBehavior> codec() {
		return CODEC;
	}
}
