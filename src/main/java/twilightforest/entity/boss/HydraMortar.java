package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.gamerules.GameRules;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;
import twilightforest.tags.TFBlockTags;

public class HydraMortar extends ThrowableProjectile {

	private static final int BURN_FACTOR = 5;
	private static final int DIRECT_DAMAGE = 18;

	public int fuse = 80;
	private boolean megaBlast = false;

	public HydraMortar(EntityType<? extends HydraMortar> type, Level world) {
		super(type, world);
	}

	@SuppressWarnings("this-escape")
	public HydraMortar(EntityType<? extends HydraMortar> type, Level world, HydraHead head) {
		// 26.1: ThrowableProjectile no longer has a (type, owner, level) constructor.
		// Compute the spawn position offset manually (3.5 blocks out on the look vector,
		// +1 on Y) then call the (type, x, y, z, level) ctor. setOwner(head) is called
		// explicitly because the owner-bearing overload was removed.
		super(type,
			head.getX() + head.getLookAngle().x() * 3.5,
			head.getY() + 1 + head.getLookAngle().y() * 3.5,
			head.getZ() + head.getLookAngle().z() * 3.5,
			world);
		// these were being set to extreme numbers when we got here, why?
		head.setDeltaMovement(Vec3.ZERO);
		this.setOwner(head);



		this.shootFromRotation(head, head.getXRot(), head.getYRot(), -20.0F, 0.84F, 1F);

		//TwilightForestMod.LOGGER.debug("Launching mortar! Current head motion is {}, {}", head.getDeltaMovement().x(), head.getDeltaMovement().z());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	@Override
	public void tick() {
		super.tick();

		if (this.onGround()) {
			this.getDeltaMovement().multiply(0.9D, 0.9D, 0.9D);

			if (!this.level().isClientSide() && this.fuse-- <= 0) {
				this.detonate();
			}
		}
	}

	public void setToBlasting() {
		this.megaBlast = true;
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.megaBlast) {
			//if we hit a wall, explode
			if (result.getDirection() != Direction.UP) this.detonate();
			// we hit the ground
			this.setDeltaMovement(this.getDeltaMovement().x(), 0.0D, this.getDeltaMovement().z());
			this.setOnGround(true);
		} else {
			this.detonate();
		}
	}

	@Override
	protected void onHit(HitResult result) {
		HitResult.Type hitresult$type = result.getType();
		if (hitresult$type == HitResult.Type.ENTITY) {
			this.onHitEntity((EntityHitResult) result);
		} else if (hitresult$type == HitResult.Type.BLOCK) {
			this.onHitBlock((BlockHitResult) result);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		Entity entity = result.getEntity();
		if (!this.level().isClientSide() && this.getOwner() != null) {
			if ((!(entity instanceof HydraMortar mortar) || mortar.getOwner().is(this.getOwner())) && !entity.is(this.getOwner()) && !this.isPartOfHydra(entity)) {
				this.detonate();
			}
		}
	}

	private boolean isPartOfHydra(Entity entity) {
		return this.getOwner() instanceof Hydra && entity instanceof HydraPart part && part.getParent().is(this.getOwner());
	}

	/**
	 * Returns true when the nearby {@code entity} belongs to the same hydra that
	 * originally fired this mortar (the hydra we'd damage if we let the direct
	 * damage loop run against it). This covers:
	 *   - the hydra body entity itself (LivingEntity owner → parent via getParent())
	 *   - any HydraPart (head, neck, body, legs) whose parent matches that hydra
	 *   - the owning HydraHead that shot us directly
	 * Used to skip the detonate direct-hurt loop for self-shot mortars so the hydra
	 * can't accidentally blow itself up.
	 */
	private boolean isSameHydraFamily(Entity entity) {
		Entity owner = this.getOwner();
		if (owner == null) return false;
		Hydra ownerHydra =
			owner instanceof Hydra h ? h :
			owner instanceof HydraPart hp ? hp.getParent() :
			null;
		if (ownerHydra == null) return false;
		if (entity.is(ownerHydra) || entity == ownerHydra) return true;
		if (entity == owner) return true;
		if (entity instanceof HydraPart hp && hp.getParent() == ownerHydra) return true;
		return false;
	}

	/**
	 * A mortar is "reflected" when its current owner is no longer the original
	 * hydra/head that fired it. HydraMortar.hurtServer() reassigns
	 * {@link #setOwner(Entity)} to the reflect source when the player hits the
	 * mortar with a shield/sword, so checking "is owner a non-hydra living entity"
	 * cleanly identifies reflected projectiles.
	 *
	 * Only reflected mortars get to bypass the hydra's self-damage guards and
	 * distance check in Hydra.attackEntityFromPart, and only reflected mortars are
	 * allowed to deal direct damage back to the firing hydra's parts during
	 * detonate(). Self-fired mortars always skip damaging themselves even if they
	 * land at our feet.
	 */
	private boolean isReflected() {
		Entity owner = this.getOwner();
		if (owner == null) return false;
		if (owner instanceof Hydra) return false;
		if (owner instanceof HydraPart hp) return hp.getParent() == null;
		return true;
	}

	@Override
	public float getBlockExplosionResistance(Explosion explosion, BlockGetter getter, BlockPos pos, BlockState state, FluidState fluid, float idk) {
		float resistance = super.getBlockExplosionResistance(explosion, getter, pos, state, fluid, idk);

		if (this.megaBlast && !state.is(TFBlockTags.COMMON_PROTECTIONS)) {
			resistance = Math.min(0.8F, resistance);
		}

		return resistance;
	}

	private void detonate() {
		float explosionPower = megaBlast ? 4.0F : 0.1F;

		boolean flag = this.megaBlast && this.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(GameRules.MOB_GRIEFING);
		this.level().explode(this, this.getX(), this.getY(), this.getZ(), explosionPower, flag, Level.ExplosionInteraction.MOB);

		if (this.level() instanceof ServerLevel serverLevel) {
			// Bumped nearby AOE sweep from inflate(1.0D) to inflate(2.5D). The hydra is a
			// big mob: a reflected mortar that visibly splashes right next to a head 3~4
			// blocks away from the projectile's origin used to fall *outside* the 1.0D
			// inflated box, so the explode splash showed on screen but the direct-damage
			// loop never picked that head/neck up and the hit looked like it did 0 damage.
			// 2.5D gives us roughly a 6-block diameter sphere around the explosion which
			// comfortably covers every head/neck/body part even on a fully-grown 7-head
			// hydra, so any on-screen hit lands the expected damage.
			boolean reflected = this.isReflected();
			for (Entity nearby : this.level().getEntities(this, this.getBoundingBox().inflate(2.5D, 2.5D, 2.5D))) {
				// For self-shot (non-reflected) mortars: never damage our own hydra or any
				// of its child parts, even if the shot falls directly under our feet.
				if (!reflected && this.isSameHydraFamily(nearby)) {
					continue;
				}
				// For reflected mortars we intentionally keep the original
				// `!fireImmune() || Hydra/HydraPart` bypass so reflected shots still
				// damage the hydra even if its fire immunity would skip the hurt. Self
				// shots intentionally do not get this bypass, combined with the
				// isSameHydraFamily skip above = zero self damage.
				if ((reflected && (!nearby.fireImmune() || nearby instanceof Hydra || nearby instanceof HydraPart) ||
					 (!reflected && !nearby.fireImmune()))
					&& nearby.hurtServer(serverLevel, TFDamageTypes.getIndirectEntityDamageSource(this.level(), TFDamageTypes.HYDRA_MORTAR, this, this.getOwner(), TFEntities.HYDRA.get()), DIRECT_DAMAGE)) {
					nearby.igniteForSeconds(BURN_FACTOR);
				}
			}
		}

		this.discard();
	}

	@Override
	public boolean hurtServer(ServerLevel server, DamageSource source, float amount) {
		super.hurtServer(server, source, amount);

		if (source.getEntity() != null && !this.level().isClientSide()) {
			Vec3 vec3d = source.getEntity().getLookAngle();
			if (vec3d != null) {
				// reflect faster and more accurately
				this.shoot(vec3d.x(), vec3d.y(), vec3d.z(), 1.5F, 0.1F);  // reflect faster and more accurately
				this.setOnGround(false);
				this.fuse += 20;
			}

			if (source.getEntity() instanceof LivingEntity) {
				this.setOwner(source.getEntity());
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isOnFire() {
		return true;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	/**
	 * We need to set this so that the player can attack and reflect the bolt
	 */
	@Override
	public float getPickRadius() {
		return 1.5F;
	}

	@Override
	protected double getDefaultGravity() {
		return 0.05D;
	}
}
