package twilightforest.item.travellers_gear;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;


import twilightforest.TwilightForestMod;
import twilightforest.components.entity.SlimySolesAttachment;
import twilightforest.components.entity.TravellersWingsAttachment;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.network.ParticlePacket;
import twilightforest.network.TravellersWingsStatePacket;
import twilightforest.util.TFEntityExtensions;
import twilightforest.util.TFMathUtil;

import java.util.Collections;
import java.util.function.Consumer;
import twilightforest.network.PacketDistributor;

public class TravellersGearLogic {

	public static final double WATER_WALKING_MAX_SUBMERGED_HEIGHT = 0.4;
	private static final double AUTO_REPAIR_SUNLIGHT_BOOST = 3;
	private static final double AUTO_REPAIR_TWILIGHT_BOOST = AUTO_REPAIR_SUNLIGHT_BOOST / 2;

	public static void travellersStealth(Player player, Consumer<Player> invisibilityHandler) {
		if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.STEALTH_MODIFIER))
			return;

		if (player.isCrouching()) {
			invisibilityHandler.accept(player);
		} else {
			MobEffectInstance invisibilityEffect = player.getEffect(MobEffects.INVISIBILITY);
			if (invisibilityEffect != null && invisibilityEffect.getDuration() < 2)
				player.setInvisible(false);
		}
	}

	public static void waterWalkingSplashEffect(LivingEntity livingEntity) {
		long lastTickWaterWalking = java.util.Objects.requireNonNullElse(TFDataAttachments.getOrCreate(livingEntity, TFDataAttachments.LAST_TICK_WATER_WALKING, () -> 0L), 0L);
		Level level = livingEntity.level();
		Vec3 livingEntityVelocity = livingEntity.getKnownMovement();
		if (lastTickWaterWalking + 1 == level.getGameTime() || livingEntityVelocity.horizontalDistance() < 0.01)
			return;

		livingEntity.setAttached(TFDataAttachments.LAST_TICK_WATER_WALKING, livingEntity.level().getGameTime());

		ParticlePacket particlePacket = new ParticlePacket();  // we have to create it on client to avoid networking delays
		for (int particleNumber = 0; particleNumber < livingEntity.dimensions.width(); particleNumber++) {
			double dx = (level.getRandom().nextDouble() * 2.0 - 1.0) * (double) livingEntity.dimensions.width() / 2D;
			double dz = (level.getRandom().nextDouble() * 2.0 - 1.0) * (double) livingEntity.dimensions.width() / 2D;
			Vec3 particlePos = new Vec3(livingEntity.getX() + dx, livingEntity.getY() + WATER_WALKING_MAX_SUBMERGED_HEIGHT, livingEntity.getZ() + dz);
			Vec3 particleVelocity = new Vec3(-livingEntityVelocity.x, 0.5, -livingEntityVelocity.z);
			if (level.isClientSide()) {
				level.addParticle(ParticleTypes.SPLASH, particlePos.x(), particlePos.y(), particlePos.z(), particleVelocity.x(), particleVelocity.y(), particleVelocity.z());
			} else {
				particlePacket.queueParticle(ParticleTypes.SPLASH, particlePos, particleVelocity);
			}
		}

		if (!level.isClientSide())
			PacketDistributor.sendToPlayersTrackingEntity(livingEntity, particlePacket);
	}

	public static boolean isBelowMaxWaterWalkingSubmergedHeight(LivingEntity livingEntity) {
		double waterHeight = livingEntity.getFluidHeight(FluidTags.WATER);
		return waterHeight <= WATER_WALKING_MAX_SUBMERGED_HEIGHT;
	}

	public static double getWaterFluidHeight(LivingEntity livingEntity) {
		return livingEntity.getFluidHeight(FluidTags.WATER);
	}

	private static final double EPSILON = 0.001D;

	/**
	 * Scan upward from the entity's feet to find the actual water surface top.
	 * Works correctly even when the entity is fully submerged (e.g. inside 2+ deep
	 * water) 閳?it keeps climbing until it exits the continuous WATER column.
	 *
	 * @return the Y coordinate of the water surface top (e.g. -1 + 8/9 for a
	 *         source block at y=-1), or {@code Double.NaN} if no water is nearby.
	 */
	public static double findWaterSurfaceY(Level level, BlockPos feetPos) {
		BlockPos.MutableBlockPos cursor = feetPos.mutable();
		FluidState fs = level.getFluidState(cursor);
		if (!fs.is(FluidTags.WATER)) {
			cursor.move(0, -1, 0);
			fs = level.getFluidState(cursor);
			if (!fs.is(FluidTags.WATER)) return Double.NaN;
			return cursor.getY() + fs.getHeight(level, cursor);
		}
		int highestWaterY = cursor.getY();
		FluidState highestFs = fs;
		while (true) {
			cursor.move(0, 1, 0);
			FluidState next = level.getFluidState(cursor);
			if (!next.is(FluidTags.WATER)) break;
			highestWaterY = cursor.getY();
			highestFs = next;
		}
		return highestWaterY + highestFs.getHeight(level, BlockPos.containing(cursor.getX(), highestWaterY, cursor.getZ()));
	}

	/**
	 * Lift a water-walking entity back onto the water surface.
	 *
	 * Fabric (unlike NeoForge) does NOT automatically snap an entity to the fluid
	 * top during collision processing 閳?canStandOnFluid only matters when the
	 * entity is already very close to the surface (within the AABB sweep).
	 * We therefore have to perform the lift ourselves, and crucially we have to
	 * do it in a way that survives client閳姱erver position sync for real players.
	 *
	 * Strategy:
	 *   1. Find the actual continuous water surface Y via column scan.
	 *   2. Apply an upward velocity via {@code setDeltaMovement} so that both
	 *      client and server physics move the entity up consistently.
	 *   3. If the entity is still far below the surface, snap its position via
	 *      {@code setPos}; for {@code ServerPlayer} additionally call
	 *      {@code teleportTo} so the authoritative position is actually sent
	 *      to the client instead of being overwritten by the underwater move
	 *      packet the client sends a moment later.
	 *   4. Once close to the surface, zero out the downward velocity so the
	 *      vanilla collision system + canStandOnFluid take over and keep the
	 *      entity standing stably.
	 */
	public static void waterWalkingTick(LivingEntity livingEntity) {
		if (!TravellersModifiersManager.isModifierActive(livingEntity, TravellersModifiersManager.WATER_WALK_MODIFIER))
			return;
		if (livingEntity.isShiftKeyDown())
			return;

		Level level = livingEntity.level();
		BlockPos feetPos = livingEntity.blockPosition();
		double surfaceY = findWaterSurfaceY(level, feetPos);
		if (Double.isNaN(surfaceY))
			return;

		double currentY = livingEntity.getY();
		Vec3 velocity = livingEntity.getDeltaMovement();

		double diff = surfaceY - currentY;

		// 閳光偓閳光偓 Guard: only handle SHALLOW water 閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓
		// NeoForge official cap is WATER_WALKING_MAX_SUBMERGED_HEIGHT = 0.4:
		// the modifier is only active while the entity is AT MOST 0.4 blocks into
		// the fluid (toes dipped).  If the entity is deeper (e.g. the player
		// held Shift to intentionally submerge) we MUST leave vanilla water
		// physics alone 閳?no upward velocity, no snap, nothing.  The player then
		// swims / floats up normally; once they naturally surface into the
		// shallow zone below we re-engage and "catch" them onto the top.
		// Without this guard the infamous "release Shift 閳?get launched out of
		// the water" bug happens because our code was trying to yank the player
		// from 2+ blocks deep straight to the surface every tick.
		if (diff > WATER_WALKING_MAX_SUBMERGED_HEIGHT + 0.1D)
			return;

		if (diff > EPSILON) {
			// --- Shallow submerged (toes dipped, diff <= 0.5): gently rise to top ---
			double upVel = Math.max(Math.min(diff * 0.9D, 0.2D), 0.04D);
			if (velocity.y() < upVel) {
				livingEntity.setDeltaMovement(velocity.x(), upVel, velocity.z());
			}

			if (!level.isClientSide() && Math.abs(currentY - surfaceY) > 0.05D) {
				livingEntity.setPos(livingEntity.getX(), surfaceY, livingEntity.getZ());
				livingEntity.resetFallDistance();
				if (livingEntity instanceof net.minecraft.server.level.ServerPlayer sp
					&& Math.abs(sp.getY() - surfaceY) > 0.25D) {
					sp.teleportTo(sp.getX(), surfaceY, sp.getZ());
				}
			}
		} else if (diff > -0.15D) {
			// --- Within 0.15 blocks of the surface (either side): stand on top ---
			if (velocity.y() < 0) {
				livingEntity.setDeltaMovement(velocity.x(), 0.0, velocity.z());
			}

			// Official: sprinting is disabled while water-walking, even though
			// normal walking is allowed.  Mirror NeoForge's behaviour here.
			livingEntity.setSprinting(false);

			// 閳光偓閳光偓 Speed alignment: two-part correction (see also travel()@HEAD mixin)
			// Part 1 (Mixin): seed onGround=true at travel() HEAD so that
			// moveRelative applies the FULL ground-mode input acceleration.
			// Part 2 (here): after travel() finishes, collision resolution has
			// overwritten onGround=false and applied air-drag (horizVel *= 0.91).
			// The correct ground-mode terminal friction is slipperiness=0.6 times
			// that same 0.91 閳?0.6 * 0.91 = 0.546 total.  To bring the air-drag
			// value (0.91) down to the target (0.546) we multiply by 0.6 here.
			// Net: 0.91 * 0.6 = 0.546 閳?ground friction, plus the ground-mode
			// acceleration we already got from the travel() HEAD mixin yields a
			// walk speed & feel IDENTICAL to walking on grass blocks.
			if (!livingEntity.onGround()) {
				// Only apply when travel's collision resolver actually left us
				// airborne (flag untouched = travel did real ground friction).
				Vec3 v = livingEntity.getDeltaMovement();
				final double groundFrictionAdjustment = 0.6D; // (0.6*0.91)/0.91
				livingEntity.setDeltaMovement(v.x() * groundFrictionAdjustment, v.y(), v.z() * groundFrictionAdjustment);
			}
			livingEntity.setOnGround(true);

			livingEntity.resetFallDistance();
			// Server-side authoritative correction ONLY when the error is clearly beyond
			// normal physics drift (< 0.05 setPos, < 0.25 skip teleport entirely). Experience
			// 1248819: teleportTo must be a corrector, not a driver 閳?using it every tick
			// causes the exact "position ping-pong" jitter the user is reporting.
			if (!level.isClientSide()) {
				if (Math.abs(currentY - surfaceY) > 0.05D) {
					livingEntity.setPos(livingEntity.getX(), surfaceY, livingEntity.getZ());
				}
				if (livingEntity instanceof net.minecraft.server.level.ServerPlayer sp
					&& Math.abs(sp.getY() - surfaceY) > 0.25D) {
					sp.teleportTo(sp.getX(), surfaceY, sp.getZ());
				}
			}
		}
	}

	public static void travellersBootsStraightAhead(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = leggingsStack.get(TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER);
		AttributeInstance attributeInstance = livingEntity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;
		if (multiplier == null)
			multiplier = 1D;
		boolean hasModifier = TravellersModifiersManager.isModifierActive(livingEntity, leggingsStack, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER) && multiplier != 1;
		if (hasModifier == attributeInstance.hasModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION))
			return;
		if (hasModifier) {
			attributeInstance.addOrUpdateTransientModifier(new AttributeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		} else {
			attributeInstance.removeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION);
		}

	}

	public static void travellersWingsSidestepCooldownSound(Player player) {
		ItemStack leggingsStack = player.getItemBySlot(EquipmentSlot.LEGS);
		Long cooldown = leggingsStack.get(TFDataComponents.SIDESTEP_COOLDOWN);
		if (cooldown == null)
			return;
		TravellersWingsAttachment attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.TRAVELLERS_WINGS, twilightforest.components.entity.TravellersWingsAttachment::new);
		long dt = player.level().getGameTime() - attachment.lastSidestepTime;
		if (TravellersModifiersManager.isModifierActive(player, leggingsStack, TravellersModifiersManager.SIDESTEP_MODIFIER) && dt > cooldown && attachment.shouldPlaySideStepCooldownSound) {
			player.level().playLocalSound(player.blockPosition(), TFSounds.SIDE_STEP_CHARGED, player.getSoundSource(), 1F, player.getVoicePitch(), false);
			attachment.shouldPlaySideStepCooldownSound = false;
		}
	}

	public static void travellersWingsGradualGlide(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
		Float multiplier = leggingsStack.get(TFDataComponents.GRADUALLY_GLIDING_MULTIPLIER);
		Vec3 deltaMovement = livingEntity.getDeltaMovement();

		boolean modActive = TravellersModifiersManager.isModifierActive(livingEntity, leggingsStack, TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER);
		boolean attachmentGlide = !(livingEntity instanceof Player player) || Boolean.TRUE.equals(TFDataAttachments.getOrCreate(player, TFDataAttachments.IS_GRADUALLY_GLIDING, () -> false));

		// 閳光偓閳光偓 Exact NeoForge official guards 閳光偓閳光偓
		// 1) Modifier present & active
		// 2) Glide multiplier configured on the leggings stack
		// 3) Actually FALLING (deltaMovement.y() < 0) 閳?useless otherwise
		// 4) Not already elytra-flying (two flight systems must not stack)
		// 5) For players specifically: the IS_GRADUALLY_GLIDING attachment must be TRUE.
		//    Non-player mobs wearing travellers leggings always glide when eligible
		//    (they have no client-side Shift toggle to drive the attachment).
		if (!modActive || multiplier == null || deltaMovement.y() >= 0 || livingEntity.isFallFlying())
			return;
		if (!attachmentGlide)
			return;

		double newDeltaMovementY = deltaMovement.y() * multiplier;
		livingEntity.setDeltaMovement(
			deltaMovement.x(),
			newDeltaMovementY,
			deltaMovement.z()
		);

		livingEntity.fallDistance = (float) (Math.pow(newDeltaMovementY, 2) / 2 / livingEntity.getGravity());
	}

	public static void travellersGearAutoRepair(LivingEntity livingEntity) {
		long lastHitTime = java.util.Objects.requireNonNullElse(TFDataAttachments.getOrCreate(livingEntity, TFDataAttachments.LAST_DAMAGE_ARMOR_TIME, () -> 0L), 0L);
		if (livingEntity.level().getGameTime() - lastHitTime <= 10 * 20)  // 10 seconds
			return;

		for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR.slots()) {
			ItemStack stack = livingEntity.getItemBySlot(slot);

			Float probability = stack.get(TFDataComponents.AUTO_REPAIR_PROBABILITY);
			if (probability == null || !TravellersModifiersManager.isModifierActive(livingEntity, stack, TravellersModifiersManager.AUTO_REPAIR_MODIFIER))
				return;
			Level level = livingEntity.level();
			double boostedProbability = getAutoRepairChance(probability, level, livingEntity.blockPosition());

			if (boostedProbability > level.getRandom().nextFloat())
				stack.setDamageValue(Math.max(stack.getDamageValue() - 1, 0));

		}
	}

	private static double getAutoRepairChance(double baseProb, Level level, BlockPos pos) {
		if (!level.canSeeSky(pos))
			return baseProb;

		double boostFactor;  // 1 tick in boost boostFactor ticks without boost
		if (level.dimensionTypeRegistration().is(TFDimensionData.TWILIGHT_DIM_TYPE))
			boostFactor = AUTO_REPAIR_TWILIGHT_BOOST;
		else if (level.isBrightOutside())
			boostFactor = AUTO_REPAIR_SUNLIGHT_BOOST;
		else
			return baseProb;
		return TFMathUtil.probabilityOfAtLeastOneSuccess(baseProb, boostFactor);
	}

	public static void travellersWingsHighJump(LivingEntity livingEntity) {
		ItemStack leggingsStack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
		Integer amplifier = leggingsStack.get(TFDataComponents.HIGH_JUMP_AMPLIFIER);
		if (TravellersModifiersManager.isModifierActive(livingEntity, leggingsStack, TravellersModifiersManager.HIGH_JUMP_ABILITY) && amplifier != null)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 2, amplifier, false, false, false));
	}

	public static void travellersVestHaste(LivingEntity livingEntity) {
		ItemStack chestStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		Integer amplifier = chestStack.get(TFDataComponents.HASTE_AMPLIFIER);
		if (TravellersModifiersManager.isModifierActive(livingEntity, chestStack, TravellersModifiersManager.HASTE_MODIFIER) && amplifier != null)
			livingEntity.addEffect(new MobEffectInstance(MobEffects.HASTE, 2, amplifier, false, false, false));
	}

	public static void travellersBootsUnrestrained(LivingEntity livingEntity) {
		if (TravellersModifiersManager.isModifierActive(livingEntity, TravellersModifiersManager.UNRESTRAINED_MODIFIER))
			livingEntity.makeStuckInBlock(Blocks.AIR.defaultBlockState(), Vec3.ZERO);
	}

	public static boolean tryPerformSidestep(Player player, boolean isLeftSidestep) {
		TravellersWingsAttachment attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.TRAVELLERS_WINGS, twilightforest.components.entity.TravellersWingsAttachment::new);
		long lastSidestepTime = attachment.lastSidestepTime;
		ItemStack leggingsStack = player.getItemBySlot(EquipmentSlot.LEGS);
		Long cooldown = leggingsStack.get(TFDataComponents.SIDESTEP_COOLDOWN);
		long currentTime = player.level().getGameTime();
		if (TravellersModifiersManager.isModifierActive(player, leggingsStack, TravellersModifiersManager.SIDESTEP_MODIFIER) && cooldown != null && currentTime - lastSidestepTime > cooldown && !player.isFallFlying() && player.onGround() && !player.isCrouching()) {
			TravellersGearLogic.performSidestep(player, isLeftSidestep);
			attachment.lastSidestepTime = currentTime;
			attachment.shouldPlaySideStepCooldownSound = true;
			return true;
		}
		return false;
	}

	public static void performSidestep(Player player, boolean isLeftSidestep) {
		float angle = player.getYRot();
		double rot = isLeftSidestep ? -Math.PI / 2 : Math.PI / 2;
		Vec3 dashDirection = new Vec3(-Math.sin(Math.toRadians(angle) + rot), 0, Math.cos(Math.toRadians(angle) + rot));
		player.push(dashDirection.scale(1.6));  // 5 blocks
		player.playSound(TFSounds.SIDE_STEP, 1.0F, player.getVoicePitch());

		TravellersWingsAttachment attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.TRAVELLERS_WINGS, twilightforest.components.entity.TravellersWingsAttachment::new);
		TravellersWingsAttachment.WingState newState = TravellersWingsAttachment.WingState.SIDESTEP;
		attachment.state = newState;
		attachment.sidestepLeft = isLeftSidestep;
		attachment.sidestepTimer = 0;

		if (player.level() instanceof ServerLevel) {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new TravellersWingsStatePacket(player.getId(), newState, isLeftSidestep, attachment.doubleJumpTimer, attachment.sidestepTimer));
		}
	}

	public static boolean performDoubleJump(Player player) {
		boolean hasDoubleJump = Boolean.TRUE.equals(TFDataAttachments.getOrCreate(player, TFDataAttachments.HAS_DOUBLE_JUMP, () -> false));
		
		
		
		Vec3 velocity = player.getDeltaMovement();
		boolean fakeGround = player.onGround() && velocity.y() > 0.1D;
		boolean conditionFailed = !hasDoubleJump || player.isFallFlying() || player.onClimbable()
			|| player.isSwimming() || player.getAbilities().flying || player.isInLiquid() || player.isPassenger()
			|| (player.onGround() && !fakeGround);
		if (conditionFailed)
			return false;
		double velYBefore = velocity.y();
		player.jumpFromGround();
		double velYAfterJump = player.getDeltaMovement().y();
		
		
		
		
		
		if (Math.abs(velYAfterJump - velYBefore) < 0.0001D) {
			float jumpPower = 0.42F;
			if (player.hasEffect(MobEffects.JUMP_BOOST)) {
				var effect = player.getEffect(MobEffects.JUMP_BOOST);
				if (effect != null)
					jumpPower += 0.1F * (effect.getAmplifier() + 1);
			}
			Vec3 v = player.getDeltaMovement();
			player.setDeltaMovement(v.x(), velYBefore + jumpPower, v.z());
		}
		velocity = player.getDeltaMovement();
		var bounce = TFDataAttachments.getOrCreate(player, TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, twilightforest.components.entity.SlimySolesAttachment::new);
		double boostVelocity = bounce.doubleJumpBoostVelocity;
		if (boostVelocity != 0) {
			player.setDeltaMovement(velocity.x(), Math.sqrt(Math.pow(velocity.y(), 2) + Math.pow(boostVelocity, 2)), velocity.z());
			bounce.doubleJumpBoostVelocity = 0;
		}
		player.resetFallDistance();
		float pitchShift = 0.1F;
		player.playSound(TFSounds.DOUBLE_JUMP, 1.5F, (player.getVoicePitch() - 1) * (1 + pitchShift) + (1 - pitchShift * 0.2F));
		player.setAttached(TFDataAttachments.HAS_DOUBLE_JUMP, false);
		player.setAttached(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
		AttributeInstance instance = player.getAttribute(Attributes.SAFE_FALL_DISTANCE);
		if (instance != null) // Increase safe fall distance so the player can land up to 2 blocks below their starting height after performing a double jump at peak height without taking fall damage
			instance.addOrUpdateTransientModifier(TFAttributeModifiers.TRAVELLERS_DOUBLE_JUMP_SAFE_FALL_DISTANCE);

		if (player.getItemBySlot(EquipmentSlot.LEGS).is(TFItems.TRAVELLERS_WINGS)) {
			TravellersWingsAttachment attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.TRAVELLERS_WINGS, twilightforest.components.entity.TravellersWingsAttachment::new);
			attachment.state = TravellersWingsAttachment.WingState.DOUBLE_JUMP;
			attachment.doubleJumpTimer = 0;
		}

		if (player.level() instanceof ServerLevel serverLevel && player.getItemBySlot(EquipmentSlot.LEGS).is(TFItems.TRAVELLERS_WINGS)) {
			ParticlePacket particlePacket = new ParticlePacket();
			Vec3 deltaMovement = player.getDeltaMovement();
			for (int particleNumber = 0; particleNumber < 10; particleNumber++) {
				Vec3 particleVelocity = new Vec3(
					(serverLevel.getRandom().nextDouble() - 0.5),
					serverLevel.getRandom().nextDouble() + 1,
					(serverLevel.getRandom().nextDouble() - 0.5)
				);
				ParticleOptions type = TFParticleType.DOUBLE_JUMP;
				Vec3 wingsPosition = player.position().add(Math.sin(Math.toRadians(player.yBodyRot)) / 3, 1.2, -Math.cos(Math.toRadians(player.yBodyRot)) / 3);
				particlePacket.queueParticle(type, wingsPosition, particleVelocity.multiply(0.25, -0.5, 0.25).add(deltaMovement));
			}
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, particlePacket);
			TravellersWingsAttachment attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.TRAVELLERS_WINGS, twilightforest.components.entity.TravellersWingsAttachment::new);
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new TravellersWingsStatePacket(player.getId(), TravellersWingsAttachment.WingState.DOUBLE_JUMP, attachment.sidestepLeft, attachment.doubleJumpTimer, attachment.sidestepTimer));
		}
		return true;
	}

	public static void travellersBootsSlimySolesBounce(LivingEntity livingEntity) {
		SlimySolesAttachment slimySolesAttachment = TFDataAttachments.getOrCreate(livingEntity, TFDataAttachments.SLIMY_SOLES_BOUNCE_INFO, twilightforest.components.entity.SlimySolesAttachment::new);
		if (slimySolesAttachment.bounceVelocity == 0 || slimySolesAttachment.hasBounced)
			return;
		Vec3 velocity = livingEntity.getDeltaMovement();
		livingEntity.playSound(SoundEvents.SLIME_JUMP, 0.5F, 1F);
		travellersBootsSlimySolesParticles(livingEntity, slimySolesAttachment);
		slimySolesAttachment.hasBounced = true;
		livingEntity.setDeltaMovement(velocity.x(), Math.sqrt(Math.pow(velocity.y(), 2) + Math.pow(slimySolesAttachment.bounceVelocity, 2)), velocity.z());
		slimySolesAttachment.forceBounce = Math.abs(livingEntity.getDeltaMovement().y()) > 0.25;
	}

	public static void travellersBootsSlimySolesParticles(LivingEntity entity, SlimySolesAttachment attachment) {
		if (!(entity.level() instanceof ServerLevel level) || attachment.bounceVelocity <= 0)
			return;

		double particleX = entity.getX();
		double particleY = entity.getY();
		double particleZ = entity.getZ();
		double intensity = Math.min(0.2 + attachment.bounceVelocity, 2.5);
		int count = (int) (40 * intensity);

		if (count > 0) {
			level.sendParticles(
				new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()),
				particleX, particleY, particleZ,
				count,
				0.0, 0.0, 0.0, 0.15F
			);
		}
	}

	private static void validateMovement(ServerPlayer serverPlayer,
										 AttachmentType<Integer> validator,
										 AttachmentType<Integer> lastCheck,
										 String movementType) {
		MinecraftServer server = null;
		if (server == null || !server.isDedicatedServer())
			return;
		int count = serverPlayer.getAttached(validator);
		int lastTick = serverPlayer.getAttached(lastCheck);
		int currentTick = serverPlayer.tickCount;
		int diff = currentTick - lastTick;
		TwilightForestMod.LOGGER.debug("{} {} check: count={}, lastTick={}, currentTick={}, diff={}",
			serverPlayer.getName().getString(), movementType, count, lastTick, currentTick, diff);

		if (diff >= 45 && !serverPlayer.isFallFlying()) {
			count = -1;
		}

		serverPlayer.setAttached(lastCheck, currentTick);

		if (count >= 5) {
			serverPlayer.connection.disconnect(new DisconnectionDetails(Component.translatable("multiplayer.disconnect.flying")));
			return;
		}

		serverPlayer.setAttached(validator, count + 1);

		if (count > 1) {
			TwilightForestMod.LOGGER.warn("{} illegal {}", serverPlayer.getName().getString(), movementType);
			serverPlayer.absSnapTo(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
				serverPlayer.getYRot(), serverPlayer.getXRot());
			serverPlayer.connection.send(ClientboundPlayerPositionPacket.of(serverPlayer.getId(),
				new PositionMoveRotation(serverPlayer.position(), Vec3.ZERO,
				serverPlayer.getYRot(), serverPlayer.getXRot()), Collections.emptySet()));
		}
	}

	public static void handleSidestepAbuse(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			validateMovement(serverPlayer,
				TFDataAttachments.SIDESTEP_VALIDATOR,
				TFDataAttachments.SIDESTEP_VALIDATOR_LAST_CHECK,
				"sidestep");
		}
	}

	public static void handleDoubleJumpAbuse(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			validateMovement(serverPlayer,
				TFDataAttachments.DOUBLE_JUMP_VALIDATOR,
				TFDataAttachments.DOUBLE_JUMP_VALIDATOR_LAST_CHECK,
				"double jump");
		}
	}

	public static void determineWingState(LivingEntity livingEntity) {
		TravellersWingsAttachment attachment = TFDataAttachments.getOrCreate(livingEntity, TFDataAttachments.TRAVELLERS_WINGS, twilightforest.components.entity.TravellersWingsAttachment::new);
		TravellersWingsAttachment.WingState newState = TravellersWingsAttachment.WingState.IDLE;

		boolean isLocked = false;
		if (attachment.state == TravellersWingsAttachment.WingState.DOUBLE_JUMP) {
			attachment.doubleJumpTimer++;
			if (attachment.doubleJumpTimer < TravellersWingsAttachment.DOUBLE_JUMP_DURATION) {
				isLocked = true;
				newState = TravellersWingsAttachment.WingState.DOUBLE_JUMP;
			}
		} else if (attachment.state == TravellersWingsAttachment.WingState.SIDESTEP) {
			attachment.sidestepTimer++;
			if (attachment.sidestepTimer < TravellersWingsAttachment.SIDESTEP_DURATION) {
				isLocked = true;
				newState = attachment.state;
			}
		} else {
			attachment.doubleJumpTimer = 0;
			attachment.sidestepTimer = 0;
		}

		if (!isLocked) {
			if (livingEntity.isPassenger()) {
				newState = TravellersWingsAttachment.WingState.RIDE;
			} else if (livingEntity.isSwimming()) {
				newState = TravellersWingsAttachment.WingState.SWIM;
			} else if (!livingEntity.onGround() && !livingEntity.isInLiquid() && livingEntity.fallDistance < 2.3F && (!(livingEntity instanceof Player p) || !p.getAbilities().flying)) {
				newState = TravellersWingsAttachment.WingState.FALL_SLOW;
			} else if (livingEntity.getDeltaMovement().y < 0 && livingEntity.fallDistance > 2.3F) {
				newState = TravellersWingsAttachment.WingState.FALL_FAST;
			} else if (livingEntity.isSprinting()) {
				newState = TravellersWingsAttachment.WingState.SPRINT;
			} else if (livingEntity.walkAnimation.speed() > 0.1) {
				newState = TravellersWingsAttachment.WingState.WALK;
			}
		}

		if (newState != attachment.state) {
			attachment.state = newState;
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, new TravellersWingsStatePacket(livingEntity.getId(), newState, attachment.sidestepLeft, attachment.doubleJumpTimer, attachment.sidestepTimer));
		}
	}
}
