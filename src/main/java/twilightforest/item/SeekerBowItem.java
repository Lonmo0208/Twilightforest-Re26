package twilightforest.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.entity.projectile.SeekerArrow;

public class SeekerBowItem extends BowItem {

	public SeekerBowItem(Properties properties) {
		super(properties);
	}

	@Override
	protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack projectile, boolean isCrit) {
		Projectile projectileEntity = super.createProjectile(level, shooter, weapon, projectile, isCrit);
		if (projectileEntity instanceof AbstractArrow arrow) {
			return new SeekerArrow(arrow, projectile.copyWithCount(1), weapon);
		}
		return projectileEntity;
	}
}
