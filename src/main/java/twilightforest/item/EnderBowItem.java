package twilightforest.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.init.TFDataAttachments;

public class EnderBowItem extends BowItem {
	public static final String KEY = "twilightforest:ender";

	public EnderBowItem(Properties properties) {
		super(properties);
	}

	@Override
	protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack projectile, boolean isCrit) {
		Projectile proj = super.createProjectile(level, shooter, weapon, projectile, isCrit);
		proj.setAttached(TFDataAttachments.ENDER_BOW_ARROW, true);
		return proj;
	}
}
