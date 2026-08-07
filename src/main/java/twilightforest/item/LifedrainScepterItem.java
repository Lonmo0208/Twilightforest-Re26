package twilightforest.item;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;
import twilightforest.network.LifedrainParticlePacket;
import twilightforest.network.ParticlePacket;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.util.entities.EntityUtil;

import java.util.List;
import java.util.Optional;
import twilightforest.network.PacketDistributor;

public class LifedrainScepterItem extends ScepterItem {

	public LifedrainScepterItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult performScepterAction(Level level, ItemStack stack, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResult.SUCCESS;
	}

	/**
	 * Animates the target falling apart into a rain of shatter particles
	 */
	public static void animateTargetShatter(ServerLevel level, LivingEntity target) {
		ParticleOptions options = new ItemParticleOption(ParticleTypes.ITEM, Items.ROTTEN_FLESH);
		boolean big = level.getRandom().nextInt(100) == 0;
		double explosionPower = big ? 1.0D : 0.3D;

		ParticlePacket particlePacket = new ParticlePacket();
		double gaussFactor = 5.0D;

		for (int i = 0; i < 50 + ((int) target.getBbWidth() * (big ? 75 : 25)); ++i) {
			double gaussX = level.getRandom().nextGaussian() * 0.01D;
			double gaussY = level.getRandom().nextGaussian() * 0.01D;
			double gaussZ = level.getRandom().nextGaussian() * 0.01D;
			double speed = level.getRandom().nextFloat() * explosionPower;
			double x = level.getRandom().nextFloat() * target.getBbWidth() * 1.5F - target.getBbWidth() - gaussX * gaussFactor + (level.getRandom().nextGaussian() * gaussX);
			double y = level.getRandom().nextFloat() * target.getBbHeight() - gaussY * gaussFactor + (level.getRandom().nextGaussian() * gaussY);
			double z = level.getRandom().nextFloat() * target.getBbWidth() * 1.5F - target.getBbWidth() - gaussZ * gaussFactor + (level.getRandom().nextGaussian() * gaussZ);

			particlePacket.queueParticle(options, target.getX() + x, target.getY() + y, target.getZ() + z, x * speed, y * speed, z * speed);
		}

		PacketDistributor.sendToPlayersTrackingEntity(target, particlePacket);
	}

	/**
	 * What, if anything, is the player currently looking at?
	 */
	@Nullable
	private Entity getPlayerLookTarget(Level level, LivingEntity living) {
		Entity pointedEntity = null;
		double range = 20.0D;
		Vec3 srcVec = living.getEyePosition();
		Vec3 lookVec = living.getViewVector(1.0F);
		Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
		float var9 = 1.0F;
		List<Entity> possibleList = level.getEntities(living, living.getBoundingBox().expandTowards(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range).inflate(var9, var9, var9));
		double hitDist = 0;

		for (Entity possibleEntity : possibleList) {

			if (possibleEntity.isPickable()) {
				float borderSize = possibleEntity.getPickRadius();
				AABB collisionBB = possibleEntity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
				Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);

				if (collisionBB.contains(srcVec)) {
					if (0.0D < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = 0.0D;
					}
				} else if (interceptPos.isPresent()) {
					double possibleDist = srcVec.distanceTo(interceptPos.get());

					if (possibleDist < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = possibleDist;
					}
				}
			}
		}
		return pointedEntity;
	}

	@Override
	public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
		if (stack.nextDamageWillBreak()) {
			living.stopUsingItem();
			return;
		}

		if (count % 5 == 0 && level instanceof ServerLevel serverLevel) {
			Entity pointedEntity = this.getPlayerLookTarget(level, living);

			if (pointedEntity instanceof LivingEntity target && !(target instanceof ArmorStand) && target.isPickable()) {
					if (!target.isDeadOrDying()) {
						PacketDistributor.sendToPlayersTrackingEntityAndSelf(living, new LifedrainParticlePacket(living.getId(), target.getEyePosition()));

					}

				DamageSource damageSource = TFDamageTypes.getEntityDamageSource(level, TFDamageTypes.LIFEDRAIN, living);
				if (target.hurtServer(serverLevel, damageSource, 1)) {
					if (target.getHealth() <= 1 && !target.is(TFEntityTypeTags.BOSSES)) {
						if (!target.is(TFEntityTypeTags.LIFEDRAIN_DROPS_NO_FLESH) && living instanceof Player player) {
							LootParams ctx = new LootParams.Builder(serverLevel)
								.withParameter(LootContextParams.THIS_ENTITY, target)
								.withParameter(LootContextParams.ORIGIN, target.getEyePosition())
								.withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
								.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
								.withParameter(LootContextParams.ATTACKING_ENTITY, player)
								.withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, player).create(LootContextParamSets.ENTITY);
							serverLevel.getServer().reloadableRegistries().getLootTable(TFLootTables.LIFEDRAIN_SCEPTER_KILL_BONUS).getRandomItems(ctx).forEach(stack1 -> target.spawnAtLocation(serverLevel, stack1));
							animateTargetShatter(serverLevel, target);
						}

						if (target instanceof Mob mob) {
							mob.spawnAnim();
						}
						SoundEvent deathSound = EntityUtil.getDeathSound(target);
						if (deathSound != null) {
							level.playSound(null, target.blockPosition(), deathSound, SoundSource.HOSTILE, 1.0F, target.getVoicePitch());
						}
						if (!target.isDeadOrDying()) {
							if (target instanceof Player) {
								target.hurtServer(serverLevel, TFDamageTypes.getEntityDamageSource(level, TFDamageTypes.LIFEDRAIN, living), Float.MAX_VALUE);
							} else {
								target.die(TFDamageTypes.getEntityDamageSource(level, TFDamageTypes.LIFEDRAIN, living));
								target.discard();
							}
						}
					} else {
						target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20, 2));
						if (count % 10 == 0) {
							living.heal(1.0F);
							if (living instanceof Player player)
								player.getFoodData().eat(1, 0.1F);
						}
					}

					if (living instanceof Player player && !player.isCreative() && (!player.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.MYSTIC_CROWN) || level.getRandom().nextFloat() > 0.05f)) {
						stack.hurtWithoutBreaking(1, player);
					}
				}

				if (!level.isClientSide() && target.getHealth() <= living.getHealth()) {
					target.setDeltaMovement(0, 0.15D, 0);
				}
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}
}
