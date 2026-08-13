package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ShieldItem;
import twilightforest.entity.boss.KnightPhantom;

public class PhantomWatchAndAttackGoal extends Goal {

	private final KnightPhantom boss;
	private int attackTime;
	private int guardCoolDownTime;
	private boolean isGuard;

	public PhantomWatchAndAttackGoal(KnightPhantom entity) {
		this.boss = entity;
	}

	@Override
	public boolean canUse() {
		return this.boss.getTarget() != null;
	}

	@Override
	public void tick() {
		LivingEntity target = (LivingEntity) this.boss.getTarget();
		if (target != null) {
			this.boss.lookAt(target, 10.0F, 500.0F);

			if (target.isAlive()) {
				float f1 = target.distanceTo(this.boss);

				if (this.boss.getSensing().hasLineOfSight(target)) {
					if (attackTime-- <= 0 && f1 < 2.0F && target.getBoundingBox().maxY > this.boss.getBoundingBox().minY && this.boss.getTarget().getBoundingBox().minY < this.boss.getBoundingBox().maxY) {
						attackTime = 20;
						this.boss.doHurtTarget((ServerLevel) this.boss.level(), target);
					}
				}

				// Block with shield only when:
				// 1. Not attacking (ATTACK_PLAYER_ATTACK formation)
				// 2. Guard state is active (set by hurtServer when the phantom is hit)
				boolean shouldBlock = false;
				if (this.boss.getOffhandItem().getItem() instanceof ShieldItem && this.boss.getCurrentFormation() != KnightPhantom.Formation.ATTACK_PLAYER_ATTACK) {
					if (this.isGuard) {
						shouldBlock = true;
					}
				}

				if (shouldBlock) {
					this.boss.startUsingItem(InteractionHand.OFF_HAND);
				} else {
					this.boss.stopUsingItem();
				}

				// Guard cycle: when guard is active, it lasts for 60 ticks (3 seconds)
				// After that, the shield drops and won't be raised again until next hit
				if (this.isGuard) {
					if (this.guardCoolDownTime <= 60) {
						++this.guardCoolDownTime;
					} else {
						this.isGuard = false;
						this.guardCoolDownTime = 0;
					}
				}
			}
		}
	}

	/**
	 * Called from KnightPhantom.hurtServer to trigger the guard state when the phantom is hit.
	 * This makes the phantom raise its shield after being attacked.
	 */
	public void updateGuard() {
		this.isGuard = true;
		this.guardCoolDownTime = 0;
	}
}