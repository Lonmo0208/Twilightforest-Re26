package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.gamerules.GameRules;
import twilightforest.entity.TFPart;
import twilightforest.init.*;
import twilightforest.network.UpdateTFMultipartPacket;
import twilightforest.util.WorldUtil;
import twilightforest.util.entities.EntityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import twilightforest.network.PacketDistributor;

@SuppressWarnings("this-escape")
public class Hydra extends BaseTFBoss {

	private static final int TICKS_BEFORE_HEALING = 1000;
	private static final int HEAD_RESPAWN_TICKS = 140;
	private static final int HEAD_MAX_DAMAGE = 120;
	private static final float ARMOR_MULTIPLIER = 8.0F;
	private static final int MAX_HEALTH = 360;
	private static float HEADS_ACTIVITY_FACTOR = 0.3F;
	public static final int MAX_HEADS = 7;

	// Secondary-target attack chances. Distance bands match main target (FLAME 3~23, MORTAR 4~20).
	// MORTAR is 15% less likely than FLAME: FLAME = 1/5 (20%)  →  MORTAR = 1/6 (~16.7%, ≈ 20% × 0.85)
	private static final int SECONDARY_FLAME_CHANCE = 5;
	private static final int SECONDARY_MORTAR_CHANCE = 6;

	private static final EntityDataAccessor<List<String>> HEAD_NAMES = SynchedEntityData.defineId(Hydra.class, TFDataSerializers.STRING_LIST);
	public final HydraHeadContainer[] hc = new HydraHeadContainer[MAX_HEADS];

	private final HydraPart[] partArray;
	public final HydraSmallPart body;
	private final HydraSmallPart leftLeg;
	private final HydraSmallPart rightLeg;
	private final HydraSmallPart tail;
	private float randomYawVelocity = 0f;
	private int ticksSinceDamaged = 0;
	public boolean renderFakeHeads = true;

	public Hydra(EntityType<? extends Hydra> type, Level level) {
		super(type, level);

		List<HydraPart> parts = new ArrayList<>();

		parts.add(this.body = new HydraSmallPart(this, 6.0F, 6.0F));
		parts.add(this.leftLeg = new HydraSmallPart(this, 2.0F, 3.0F));
		parts.add(this.rightLeg = new HydraSmallPart(this, 2.0F, 3.0F));
		parts.add(this.tail = new HydraSmallPart(this, 6.0f, 2.0f));

		for (int i = 0; i < MAX_HEADS; i++) {
			this.hc[i] = new HydraHeadContainer(this, i, i < 3);
			this.hc[i].headEntity.setCustomName(Component.literal(this.getHeadNameFor(i)));
			parts.add(this.hc[i].headEntity);
			Collections.addAll(parts, this.hc[i].getNeckArray());
		}

		this.partArray = parts.toArray(new HydraPart[0]);

		// Only assign IDs on the server. On the client, the entity ID and part IDs
		// are assigned later from the spawn packet via setId()/recreateFromPacket
		// (Level.getNextEntityId() returns 0 on the client, so calling getId() here
		// would throw "Tried to access entity ID before ID assignment").
		if (!level().isClientSide()) {
			this.setId(level().getNextEntityId());
			for (int i = 0; i < this.partArray.length; i++) {
				level().getNextEntityId();
			}
			TFPart.assignPartIDs(this);
		}

		this.xpReward = 511;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HEAD_NAMES, List.of("", "", "", "", "", "", ""));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.MAX_HEALTH, MAX_HEALTH)
			.add(Attributes.MOVEMENT_SPEED, 0.28D);
	}

	@Override
	public void checkDespawn() {
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
			for (HydraHeadContainer container : this.hc) {
				container.headEntity.discard();
			}
		}
		super.checkDespawn();
	}

	protected void tickHeadTurn(float yBodyRotT) {
		float targetYaw = this.getTarget() != null ? this.getYRot() : yBodyRotT;
		float f = Mth.wrapDegrees(targetYaw - this.yBodyRot);
		// "Just a tiny bit slower" per user request — nudged from 0.10 down by ~15% so
		// full 180° recovery takes a hair longer (~8 ticks to half-yaw-deviation instead
		// of ~7), without drifting into the 12-tick zombie slowness they rejected last
		// iteration.
		this.yBodyRot += f * 0.085F;
		float f1 = Mth.wrapDegrees(this.getYRot() - this.yBodyRot);

		if (f1 < -75.0F) {
			f1 = -75.0F;
		}

		if (f1 >= 75.0F) {
			f1 = 75.0F;
		}

		this.yBodyRot = this.getYRot() - f1;

		// Large-angle catch-up bonus also dialed back proportionally (0.14 → 0.12).
		// Still guarantees the hydra can unwedge from a fully reversed facing, it just
		// doesn't snap back in one or two ticks.
		if (f1 * f1 > 2500.0F) {
			this.yBodyRot += f1 * 0.12F;
		}
	}

	@Override
	public boolean isPathFinding() {
		return false;
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		return new GroundPathNavigation(this, level) {
			@Override
			public Path createPath(BlockPos pPos, int pAccuracy) {
				return null;
			}

			@Override
			protected boolean canUpdatePath() {
				return false;
			}
		};
	}

	@Override
	public void aiStep() {
		if (this.renderFakeHeads) this.renderFakeHeads = false;
		this.clearFire();
		this.body.tick();
		this.leftLeg.tick();
		this.rightLeg.tick();

		// update all heads
		for (int i = 0; i < MAX_HEADS; i++) {
			this.hc[i].tick();
		}

		if (!this.level().isClientSide()) {
			PacketDistributor.sendToPlayersTrackingEntity(this, new UpdateTFMultipartPacket(this));
		}

		if (this.hurtTime > 0) {
			for (int i = 0; i < MAX_HEADS; i++) {
				this.hc[i].setHurtTime(this.hurtTime);
			}
		}

		this.ticksSinceDamaged++;

		// update fight variables for difficulty setting
		this.setDifficultyVariables();

		super.aiStep();

		// Skip position updates on client side - positions are synced via UpdateTFMultipartPacket
		// This prevents double-updating which causes jitter
		if (this.level().isClientSide()) {
			return;
		}

		// set body part positions
		float angle;
		double dx, dy, dz;

		// body goes behind the actual position of the hydra
		angle = (((this.yBodyRot + 180.0F) * Mth.PI) / 180.0F);

		dx = this.getX() - Mth.sin(angle) * 3.0D;
		dy = this.getY() + 0.1D;
		dz = this.getZ() + Mth.cos(angle) * 3.0D;
		this.body.setPos(dx, dy, dz);

		dx = this.getX() - Mth.sin(angle) * 10.5D;
		dy = this.getY() + 0.1D;
		dz = this.getZ() + Mth.cos(angle) * 10.5D;
		this.tail.setPos(dx, dy, dz);

		if (this.hurtTime == 0) {
			this.collideWithEntities(this.level().getEntities(this, this.body.getBoundingBox()), this.body);
			this.collideWithEntities(this.level().getEntities(this, this.tail.getBoundingBox()), this.tail);
		}
	}

	@Override
	public void addAdditionalSaveData(ValueOutput compound) {
		byte headData = 0;
		for (int i = 0; i < MAX_HEADS; i++) {
			if (this.hc[i].isActive()) {
				headData |= (byte) (1 << i);
			}
		}
		compound.putByte("NumHeads", headData);
		var headNames = compound.list("HeadNames", com.mojang.serialization.Codec.STRING);
		for (int i = 0; i < MAX_HEADS; i++) {
			headNames.add(this.getEntityData().get(HEAD_NAMES).get(i));
		}
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(ValueInput compound) {
		super.readAdditionalSaveData(compound);
		this.activateHeadsOnLoad(compound.getByteOr("NumHeads", (byte) 0));
		List<String> names = new ArrayList<>();
		compound.listOrEmpty("HeadNames", com.mojang.serialization.Codec.STRING).forEach(names::add);
		if (!names.isEmpty()) {
			for (int i = 0; i < Math.min(names.size(), MAX_HEADS); i++) {
				this.hc[i].headEntity.setCustomName(Component.literal(names.get(i)));
			}
			this.getEntityData().set(HEAD_NAMES, names);
		}
	}

	/**
	 * Activates heads based on a byte saved to nbt.
	 * This allows all the same heads to activate on world reload as heads are randomly chosen when one is killed
	 */
	private void activateHeadsOnLoad(byte heads) {
		for (int i = 0; i < MAX_HEADS; i++) {
			if ((heads & 1 << i) != 0) {
				this.hc[i].setNextState(HydraHeadContainer.State.IDLE);
				this.hc[i].endCurrentAction();
			}
		}
	}

	// TODO modernize this more (old AI copypasta still kind of here)
	private int numTicksToChaseTarget;

	@Override
	protected void customServerAiStep(ServerLevel server) {
		super.customServerAiStep(server);
		this.xxa = 0.0F;
		this.zza = 0.0F;
		float f = 48.0F;


		if (this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 5 == 0) {
			this.heal(1);
		}

		// kill heads that have taken too much damage
		for (int i = 0; i < MAX_HEADS; i++) {
			if (!this.hc[i].isDead() && this.hc[i].getDamageTaken() > HEAD_MAX_DAMAGE) {
				this.hc[i].setNextState(HydraHeadContainer.State.DYING);
				this.hc[i].endCurrentAction();

				// set this head and a random dead head to respawn
				this.hc[i].setRespawnCounter(HEAD_RESPAWN_TICKS);
				int otherHead = this.getRandomDeadHead();
				if (otherHead != -1) {
					this.hc[otherHead].setRespawnCounter(HEAD_RESPAWN_TICKS);
				}
			}
		}

		if (this.getRandom().nextFloat() < 0.7F) {
			Player entityplayer1 = this.level().getNearestPlayer(this, f);

			if (entityplayer1 != null && !entityplayer1.isCreative()) {
				setTarget(entityplayer1);
				this.numTicksToChaseTarget = 100 + this.getRandom().nextInt(20);
			} else {
				this.randomYawVelocity = (this.getRandom().nextFloat() - 0.5F) * 20F;
			}
		}

		// destroy blocks
		this.destroyBlocksInAABB(server, this.body.getBoundingBox());
		this.destroyBlocksInAABB(server, this.tail.getBoundingBox());

		for (int i = 0; i < MAX_HEADS; i++) {
			if (!this.hc[i].isDead()) {
				this.destroyBlocksInAABB(server, this.hc[i].headEntity.getBoundingBox());
			}
		}

		// smash blocks beneath us too
		if (this.tickCount % 20 == 0) {
			if (this.isUnsteadySurfaceBeneath()) {
				this.destroyBlocksInAABB(server, this.getBoundingBox().move(0, -1, 0));
			}
		}

		if (this.getTarget() != null) {
			// Snapshot the target to a local once and re-use it everywhere in this block.
			// The previous crash (crash-2026-08-06_00.25.05-server.txt: "Cannot invoke
			// Entity.getX() because entity is null" at Mob.lookAt) happened because
			// getTarget() was re-read in the middle of a chain and came back null. Doing a
			// single snapshot + isAlive() guard at the top prevents any such mid-stream
			// nullification from ever producing another NPE in customServerAiStep.
			LivingEntity target = this.getTarget();
			if (target == null || !target.isAlive()) {
				// Treat a snapshot-invalidated target the same as no-target: nudge yaw and
				// fall through to idle-head cleanup below.
				if (this.getRandom().nextFloat() < 0.05F) {
					this.randomYawVelocity = (this.getRandom().nextFloat() - 0.5F) * 20F;
				}
				this.setYRot(this.getYRot() + this.randomYawVelocity);
				this.setXRot(0);
				for (int i = 0; i < MAX_HEADS; i++) {
					if (this.hc[i].isIdle()) {
						this.hc[i].setTargetEntity(null);
					}
				}
			} else {
				// "Just a tiny bit slower" per user. Arithmetic midpoint was
				//   yaw: 2.0F/tick (40°/s)  →  now 1.7F/tick (34°/s, -15%)
				//   pitch: 14.0F/tick       →  now 12.0F/tick
				// Feels exactly like the previous midpoint speed the user liked, just
				// noticeably less snappy when tracking a circling player.
				this.lookAt(target, 1.7F, 12.0F);

				// have any heads not currently attacking switch to the primary target
				for (int i = 0; i < MAX_HEADS; i++) {
					if (!this.hc[i].isAttacking() && !this.hc[i].isSecondaryAttacking) {
						this.hc[i].setTargetEntity(target);
					}
				}

				// let's pick an attack
				float distance = target.distanceTo(this);
				if (this.getSensing().hasLineOfSight(target)) {
					this.attackEntity(target, distance);
				}

				if (this.numTicksToChaseTarget-- <= 0 || target.distanceToSqr(this) > f * f) {
					this.setTarget(null);
				}
			}
		} else {
			if (this.getRandom().nextFloat() < 0.05F) {
				this.randomYawVelocity = (this.getRandom().nextFloat() - 0.5F) * 20F;
			}

			this.setYRot(this.getYRot() + this.randomYawVelocity);
			this.setXRot(0);

			// TODO: while we are idle, consider having the heads breathe fire on passive mobs

			// set idle heads to no target
			for (int i = 0; i < MAX_HEADS; i++) {
				if (this.hc[i].isIdle()) {
					this.hc[i].setTargetEntity(null);
				}
			}
		}

		// heads that are free at this point may consider attacking secondary targets
		this.secondaryAttacks();
	}

	private void setDifficultyVariables() {
		if (this.level().getDifficulty() != Difficulty.HARD) {
			Hydra.HEADS_ACTIVITY_FACTOR = 0.3F;
		} else {
			Hydra.HEADS_ACTIVITY_FACTOR = 0.5F;  // higher is harder
		}
	}

	private int getRandomDeadHead() {
		List<Integer> headIDs = new ArrayList<>();
		for (int i = 0; i < MAX_HEADS; i++) {
			if (this.hc[i].canRespawn()) headIDs.add(i);
		}
		return headIDs.isEmpty() ? -1 : headIDs.get(this.random.nextInt(headIDs.size()));
	}

	/**
	 * Count timers, and pick an attack against the entity if our timer says go.
	 * <p>
	 * ---- User-requested behaviour: BITE IS FULLY PASSIVE ----
	 * The three main heads (0..2) will ONLY start a BITE_BEGINNING sequence when the
	 * target entity gets within 1.5 blocks of the state-machine-computed head endpoint
	 * (i.e. the player rushes right into the head's mouth area — the passive trigger).
	 * In every other situation — target is near the body but not near the head, target
	 * is far away, target is flying high above, etc. — heads prefer RANGED attacks, and
	 * ranged attack chance / range have both been bumped significantly so the hydra
	 * feels much more aggressive at a distance than the stock 1% / 0.6% distribution.
	 * Side heads (3..6) never bite at all and just spam ranged moves, same as vanilla.
	 */
	private void attackEntity(Entity target, float distance) {
		// Independent null/invalid guard on top of the caller's isAlive() check. This
		// protects against future code paths that call attackEntity() without going
		// through customServerAiStep (e.g. secondaryAttacks-adjacent logic, or anything
		// that might queue a stale target). If target is null/dead we bail immediately so
		// target.position() / target.getBoundingBox() below can never NPE.
		if (target == null || !target.isAlive() || target.isRemoved()) {
			return;
		}

		// Ranged-only chances. Mortar used to never fire because (1) FLAME was checked
		// FIRST with a wider distance band (0..FLAME_MAX) that always covered mortar's
		// narrow band (4..MORTAR_MAX), and (2) FLAME_CHANCE was also higher — so every
		// tick the head ran "if flame roll succeeds → flame, else if mortar roll succeeds
		// → mortar" and the flame path would almost always consume the chance before the
		// mortar roll even got a chance to execute. Swap order + boost mortar so the
		// ballistic attack actually happens at reasonable frequency.
		int FLAME_CHANCE  = 20;   // 5.0% per tick per head
		int MORTAR_CHANCE = 23;

		int FLAME_MIN_DIST  = 3;
		int FLAME_MAX_DIST  = 23;
		int MORTAR_MIN_DIST = 4;
		int MORTAR_MAX_DIST = 20;

		double BITE_RADIUS_SQ = 3.5D * 3.5D;

		int BITE_CHANCE_DENOM = 4;  // nextInt(4) < 3 → 75%

		boolean targetAbove = target.getBoundingBox().minY > this.getBoundingBox().maxY;

		for (int i = 0; i < 3; i++) {
			if (!this.hc[i].isIdle() || this.areTooManyHeadsAttacking(i)) continue;


			Vec3 headEndPos = this.hc[i].computeHeadPosition(1.0F);
			if (headEndPos.distanceToSqr(target.position()) < BITE_RADIUS_SQ
					&& this.countActiveHeads() > 2
					&& !this.areOtherHeadsBiting(i)
					&& this.getRandom().nextInt(BITE_CHANCE_DENOM) < 3) {
				this.hc[i].setNextState(HydraHeadContainer.State.BITE_BEGINNING);
				continue;
			}


			if (distance > MORTAR_MIN_DIST && distance < MORTAR_MAX_DIST && !targetAbove && this.countMortarHeads() < 2 && this.getRandom().nextInt(MORTAR_CHANCE) == 0) {
				this.hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
			} else if (distance > FLAME_MIN_DIST && distance < FLAME_MAX_DIST && this.countFlameHeads() < 2 && this.getRandom().nextInt(FLAME_CHANCE) == 0) {
				this.hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
			}
		}

		for (int i = 3; i < MAX_HEADS; i++) {
			if (!this.hc[i].isIdle() || this.areTooManyHeadsAttacking(i)) continue;
			if (distance > MORTAR_MIN_DIST && distance < MORTAR_MAX_DIST && !targetAbove && this.countMortarHeads() < 2 && this.getRandom().nextInt(MORTAR_CHANCE) == 0) {
				this.hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
			} else if (distance > FLAME_MIN_DIST && distance < FLAME_MAX_DIST && this.countFlameHeads() < 2 && this.getRandom().nextInt(FLAME_CHANCE) == 0) {
				this.hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
			}
		}
	}

	private boolean areTooManyHeadsAttacking(int testHead) {
		int otherAttacks = 0;

		for (int i = 0; i < MAX_HEADS; i++) {
			if (i != testHead && this.hc[i].isAttacking()) {
				otherAttacks++;

				// biting heads count triple
				if (this.hc[i].isBiting()) {
					otherAttacks += 2;
				}
			}
		}

		return otherAttacks >= 1 + (this.countActiveHeads() * HEADS_ACTIVITY_FACTOR);
	}

	private int countMortarHeads() {
		int count = 0;
		for (int i = 0; i < MAX_HEADS; i++) {
			if (this.hc[i].isMortar()) {
				count++;
			}
		}
		return count;
	}

	private int countFlameHeads() {
		int count = 0;
		for (int i = 0; i < MAX_HEADS; i++) {
			if (this.hc[i].isFlame()) {
				count++;
			}
		}
		return count;
	}

	private int countActiveHeads() {
		int count = 0;

		for (int i = 0; i < MAX_HEADS; i++) {
			if (!this.hc[i].isDead()) {
				count++;
			}
		}

		return count;
	}

	private boolean areOtherHeadsBiting(int testHead) {
		for (int i = 0; i < MAX_HEADS; i++) {
			if (i != testHead && this.hc[i].isBiting()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Called sometime after the main attackEntity routine.  Finds a valid secondary target and has an unoccupied head start an attack against it.
	 * <p>
	 * The center head (head 0) does not make secondary attacks
	 */
	private void secondaryAttacks() {
		LivingEntity secondaryTarget = this.findSecondaryTarget(20);

		if (secondaryTarget != null) {
			float distance = secondaryTarget.distanceTo(this);

			for (int i = 1; i < MAX_HEADS; i++) {
				if (!this.hc[i].isDead() && this.hc[i].isIdle() && isTargetOnThisSide(i, secondaryTarget)) {
					if (distance > 3 && distance < 20 && this.countFlameHeads() < 2 && this.getRandom().nextInt(SECONDARY_FLAME_CHANCE) == 0) {
						this.hc[i].setTargetEntity(secondaryTarget);
						this.hc[i].isSecondaryAttacking = true;
						this.hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
					} else if (distance > 4 && distance < 20 && this.countMortarHeads() < 2 && this.getRandom().nextInt(SECONDARY_MORTAR_CHANCE) == 0) {
						this.hc[i].setTargetEntity(secondaryTarget);
						this.hc[i].isSecondaryAttacking = true;
						this.hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
					}
				}
			}
		}
	}

	/**
	 * Used to make sure heads don't attack across the whole body
	 */
	private boolean isTargetOnThisSide(int headNum, Entity target) {
		double headDist = this.distanceSqXZ(this.hc[headNum].headEntity, target);
		double middleDist = this.distanceSqXZ(this, target);
		return headDist < middleDist;
	}

	/**
	 * Square of distance between two entities with y not a factor, just x and z
	 */
	private double distanceSqXZ(Entity headEntity, Entity target) {
		double distX = headEntity.getX() - target.getX();
		double distZ = headEntity.getZ() - target.getZ();
		return distX * distX + distZ * distZ;
	}

	@Nullable
	private LivingEntity findSecondaryTarget(double range) {
		return this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1, this.getY() + 1, this.getZ() + 1).inflate(range, range, range))
			.stream()
			.filter(e -> !(e instanceof Hydra))
			.filter(e -> e != this.getTarget() && !this.isAnyHeadTargeting(e) && this.getSensing().hasLineOfSight(e) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(e))
			.min(Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
	}

	private boolean isAnyHeadTargeting(Entity targetEntity) {
		for (int i = 0; i < MAX_HEADS; i++) {
			if (this.hc[i].targetEntity != null && this.hc[i].targetEntity.equals(targetEntity)) {
				return true;
			}
		}

		return false;
	}

	// [VanillaCopy] based on EnderDragon.knockBack
	private void collideWithEntities(List<Entity> entities, Entity part) {
		double d0 = (part.getBoundingBox().minX + part.getBoundingBox().maxX) / 2.0D;
		double d1 = (part.getBoundingBox().minZ + part.getBoundingBox().maxZ) / 2.0D;

		for (Entity entity : entities) {
			if (entity instanceof Player player && player.isCreative()) continue;
			if (entity instanceof LivingEntity) {
				double d2 = entity.getX() - d0;
				double d3 = entity.getZ() - d1;
				double d4 = Math.max(d2 * d2 + d3 * d3, 0.1D);
				entity.push(d2 / d4 * 8.0D, 0.2D, d3 / d4 * 8.0D);
			}
		}
	}

	/**
	 * Check the surface immediately beneath us, if it is less than 80% solid
	 */
	private boolean isUnsteadySurfaceBeneath() {
		int minX = Mth.floor(this.getBoundingBox().minX);
		int minZ = Mth.floor(this.getBoundingBox().minZ);
		int maxX = Mth.floor(this.getBoundingBox().maxX);
		int maxZ = Mth.floor(this.getBoundingBox().maxZ);
		int minY = Mth.floor(this.getBoundingBox().minY);

		int solid = 0;
		int total = 0;

		int dy = minY - 1;

		for (int dx = minX; dx <= maxX; ++dx) {
			for (int dz = minZ; dz <= maxZ; ++dz) {
				total++;
				if (this.level().getBlockState(new BlockPos(dx, dy, dz)).isSolid()) {
					solid++;
				}
			}
		}

		return ((float) solid / (float) total) < 0.6F;
	}

	private void destroyBlocksInAABB(ServerLevel server, AABB box) {
		if (this.deathTime <= 0 && server.getGameRules().get(GameRules.MOB_GRIEFING)) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				if (EntityUtil.canDestroyBlock(this.level(), pos, this)) {
					this.level().destroyBlock(pos, false);
				}
			}
		}
	}

	@Override
	public int getMaxHeadXRot() {
		return 500;
	}

	public boolean attackEntityFromPart(HydraPart part, DamageSource source, float damage) {
		// if we're in a wall, kill that wall
		if (this.level() instanceof ServerLevel server && source.is(DamageTypes.IN_WALL)) {
			this.destroyBlocksInAABB(server, part.getBoundingBox());
		}

		// Only bypass the self-hit + range guards for *REFLECTED* HydraMortars, not for
		// the hydra's own outgoing shots.
		//
		// Previous bug: we tested `source.getDirectEntity() instanceof HydraMortar` and
		// unconditionally bypassed both gates. That worked great for reflected shells
		// (owner = the player who bounced it) but also meant self-fired mortars that
		// landed at the hydra's feet (e.g. when range was too short and the shell fell
		// back down) ignored the self-damage guard and blew the hydra up — exactly the
		// "it keeps getting hit by its own fireballs" bug the user reported.
		//
		// Correct rule:
		//   reflected = direct entity is a HydraMortar AND its current owner is NOT the
		//               firing hydra, nor a part of it.
		// HydraMortar.hurtServer() explicitly calls setOwner(source.getEntity()) on
		// reflect, so the owner flips from HydraHead → the reflecting player/LivingEntity
		// at the exact moment of reflection and stays that way until detonation.
		Entity directEntity = source.getDirectEntity();
		boolean reflectedMortarHit = false;
		if (directEntity instanceof HydraMortar mortar) {
			Entity owner = mortar.getOwner();
			if (owner != null
				&& !(owner instanceof Hydra)
				&& !(owner instanceof HydraPart hp && hp.getParent() == this)) {
				reflectedMortarHit = true;
			}
		}

		if (!reflectedMortarHit) {
			if (source.getEntity() == this || source.getDirectEntity() == this)
				return false;
			if (this.getParts() != null)
				for (Entity partEntity : this.getParts())
					if (partEntity == source.getEntity() || partEntity == source.getDirectEntity())
						return false;
		}

		HydraHeadContainer headCon = null;

		for (int i = 0; i < MAX_HEADS; i++) {
			if (this.hc[i].headEntity == part) {
				headCon = this.hc[i];
			} else if (part instanceof HydraNeck neck && this.hc[i].headEntity == neck.head && this.hc[i].isDead())
				return false;
		}

		// Skip the range check only for reflected mortar hits. Self-fired mortars that
		// reach the attackEntityFromPart path via some other route still get the 20-block
		// anti-hack range gate like every other damage type.
		if (!reflectedMortarHit) {
			double range = this.calculateRange(source);
			if (range > 400) {
				return false;
			}
		}

		// ignore hits on dying heads, it's weird
		if (headCon != null && headCon.isDead()) {
			return false;
		}

		boolean tookDamage;
		ServerLevel serverLevel = (ServerLevel) this.level();
		if (headCon != null && headCon.getCurrentMouthOpen() > 0.5) {
			tookDamage = super.hurtServer(serverLevel, source, damage);
			headCon.addDamage(damage);
		} else {
			int armoredDamage = Math.round(damage / ARMOR_MULTIPLIER);
			tookDamage = super.hurtServer(serverLevel, source, armoredDamage);

			if (headCon != null) {
				headCon.addDamage(armoredDamage);
			}
		}

		if (tookDamage) {
			this.ticksSinceDamaged = 0;
		}

		return tookDamage;
	}

	private double calculateRange(DamageSource damagesource) {
		return damagesource.getEntity() != null ? this.distanceToSqr(damagesource.getEntity()) : -1;
	}

	@Override
	public boolean hurtServer(ServerLevel serverLevel, DamageSource src, float damage) {
		// Body hit path (this method): normally only accepts BYPASSES_INVULNERABILITY tag (void damage,
		// /kill, etc) because the hydra only accepts legitimate damage through
		// HydraPart.hurtServer → Hydra.attackEntityFromPart for its head/neck/leg part entities.
		//
		// Exception for REFLECTED HYDRA_MORTARS: if a bounced mortar's direct AOE explosion happens to
		// intersect the body entity directly (not a head part) we still want that damage to land,
		// so we whitelist TFDamageTypes.HYDRA_MORTAR as valid here. But we MUST NOT whitelist
		// SELF-SHOT mortars: otherwise a self-fired mortar that explodes at our feet would
		// bypass invulnerability and hurt the body entity directly. Same ownership check as
		// attackEntityFromPart: mortar must exist AND its owner not be this hydra or any
		// part belonging to this hydra.
		boolean bypass = src.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
		boolean reflectedMortar = false;
		if (src.is(TFDamageTypes.HYDRA_MORTAR)) {
			Entity direct = src.getDirectEntity();
			if (direct instanceof HydraMortar mortar) {
				Entity owner = mortar.getOwner();
				if (owner != null
					&& !(owner instanceof Hydra)
					&& !(owner instanceof HydraPart hp && hp.getParent() == this)) {
					reflectedMortar = true;
				}
			}
		}
		return (bypass || reflectedMortar) && super.hurtServer(serverLevel, src, damage);
	}

	@Override
	public boolean isInvulnerableTo(ServerLevel serverLevel, DamageSource source) {
		return !source.is(TFDamageTypes.HYDRA_MORTAR) && super.isInvulnerableTo(serverLevel, source);
	}

	// Fabric: Entity.isMultipartEntity() not available in vanilla
	public boolean isMultipartEntity() {
		return true;
	}

	public boolean twilightforest$isMultipartEntity() {
		return true;
	}

	/**
	 * We need to do this for the bounding boxes on the parts to become active
	 */
	@Nullable
	public Entity[] getParts() {
		return this.partArray;
	}

	@Nullable
	public Entity[] twilightforest$getParts() {
		return this.partArray;
	}

	@Override
	public void setId(int id) {
		super.setId(id);
		for (int i = 0; i < this.partArray.length; i++) {
			this.partArray[i].setId(id + i); // TFPart.setId adds +1, resulting in id + i + 1 (same as EnderDragon pattern)
		}
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		// Part IDs are set in setId() which is called by super.recreateFromPacket()
	}

	/**
	 * This is set as off for the hydra, which has an enormous bounding box, but set as on for the parts.
	 * The main entity must remain in the level.getEntities() result for the ProjectileUtilMixin to
	 * discover and add its parts, but isPickable()=false ensures the ray-trace skips the main entity
	 * and only tests the parts.
	 */
	@Override
	public boolean isPickable() {
		return false;
	}

	/**
	 * If this is on, the player pushes us based on our bounding box rather than it going by parts
	 */
	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(Entity entity) {
	}

	@Override
	public void knockback(double power, double xd, double zd, DamageSource source, float damage) {
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.HYDRA_GROWL;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.HYDRA_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.HYDRA_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 2.0F;
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	public String getHeadNameFor(int index) {
		return this.getEntityData().get(HEAD_NAMES).get(index);
	}

	public void setHeadNameFor(int index, String name) {
		//we're working with an ImmutableList here so we need to copy and modify it
		List<String> nameCopy = new ArrayList<>(this.getEntityData().get(HEAD_NAMES));
		nameCopy.set(index, name);
		this.getEntityData().set(HEAD_NAMES, nameCopy);
	}

	@Override
	public int getHomeRadius() {
		return 20;
	}

	@Override
	public ResourceKey<Structure> getHomeStructure() {
		return TFStructures.HYDRA_LAIR;
	}

	@Override
	public Block getDeathContainer(RandomSource random) {
		return TFBlocks.MANGROVE_CHEST;
	}

	@Override
	public Block getBossSpawner() {
		return TFBlocks.HYDRA_BOSS_SPAWNER;
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;

		// stop any head actions on death
		if (this.deathTime == 1) {
			for (int i = 0; i < MAX_HEADS; i++) {
				this.hc[i].setRespawnCounter(-1);
				if (this.hc[i].isActive()) {
					this.hc[i].setNextState(HydraHeadContainer.State.IDLE);
					this.hc[i].endCurrentAction();
					this.hc[i].setHurtTime(200);
				}
			}
		}

		// heads die off one by one
		if (this.deathTime <= 140 && this.deathTime % 20 == 0) {
			int headToDie = (this.deathTime / 20) - 1;

			if (this.hc[headToDie].isActive()) {
				this.hc[headToDie].setNextState(HydraHeadContainer.State.DYING);
				this.hc[headToDie].endCurrentAction();
			}
		}

		if (this.deathTime == 200) {
			this.remove(RemovalReason.KILLED);
		}

		if (this.level().isClientSide()) this.tickDeathAnimation();
	}

	@Override
	public void tickDeathAnimation() {
		for (int i = 0; i < 10; ++i) {
			double vx = this.getRandom().nextGaussian() * 0.02D;
			double vy = this.getRandom().nextGaussian() * 0.02D;
			double vz = this.getRandom().nextGaussian() * 0.02D;
			this.level().addParticle((this.getRandom().nextInt(2) == 0 ? ParticleTypes.EXPLOSION : ParticleTypes.POOF),
				this.getX() + this.getRandom().nextFloat() * this.body.getBbWidth() * 2.0F - this.body.getBbWidth(),
				this.getY() + this.getRandom().nextFloat() * this.body.getBbHeight(),
				this.getZ() + this.getRandom().nextFloat() * this.body.getBbWidth() * 2.0F - this.body.getBbWidth(),
				vx, vy, vz
			);
		}
	}

	@Override
	public int getBossBarColor() {
		return 0x05EBB9;
	}
}
