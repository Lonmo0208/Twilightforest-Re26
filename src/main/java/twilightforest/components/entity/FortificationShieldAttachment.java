package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.*;
import twilightforest.network.ParticlePacket;
import twilightforest.network.PacketDistributor;

public class FortificationShieldAttachment {

	public static final MapCodec<FortificationShieldAttachment> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.INT.fieldOf("temporary_shields").forGetter(o -> o.temporaryShields),
			Codec.INT.fieldOf("permanent_shields").forGetter(o -> o.permanentShields))
		.apply(instance, FortificationShieldAttachment::new));

	public static final StreamCodec<FriendlyByteBuf, FortificationShieldAttachment> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, o -> o.temporaryShields,
		ByteBufCodecs.INT, o -> o.permanentShields,
		FortificationShieldAttachment::new
	);

	private int temporaryShields;
	private int permanentShields;
	private int timer;

	public FortificationShieldAttachment() {
		this(0, 0);
	}

	public FortificationShieldAttachment(int temporaryShields, int permanentShields) {
		this.temporaryShields = Math.max(temporaryShields, 0);
		this.permanentShields = Math.max(permanentShields, 0);
		this.resetTimer();
	}

	public void tick(LivingEntity entity) {
		if (this.temporaryShieldsLeft() > 0 && !(entity instanceof Player player && player.getAbilities().invulnerable)) {
			if (this.timer <= 0) {
				this.breakShield(entity, true);
			} else if (this.checkLichCrownBonus(entity)) {
				// Timer decay skipped after every 2 ticks so that shields last 50% longer with Lich Crown worn
				this.timer--;
			}
		}
	}

	private boolean checkLichCrownBonus(LivingEntity entity) {
		//return entity.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.LICH_CROWN) ? (entity.tickCount % 3) != 0 : true;
		// Simplified but same logic as the line above
		return !entity.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.MYSTIC_CROWN) || (entity.tickCount % 3) != 0;
	}

	public int shieldsLeft() {
		return this.temporaryShields + this.permanentShields;
	}

	public int temporaryShieldsLeft() {
		return this.temporaryShields;
	}

	public int permanentShieldsLeft() {
		return this.permanentShields;
	}

	public void breakShield(LivingEntity entity, boolean expired) {
		int newTemp = this.temporaryShields;
		int newPerm = this.permanentShields;

		if (this.temporaryShields > 0) {
			newTemp--;
		} else if (this.permanentShields > 0) {
			newPerm--;
		}

		if (entity instanceof ServerPlayer player && !expired) {
			player.awardStat(TFStats.TF_SHIELDS_BROKEN);
		}
		entity.level().playSound(null, entity.blockPosition(), SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS, 1.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.7F + 1.0F);
		entity.setAttached(TFDataAttachments.FORTIFICATION_SHIELDS, new FortificationShieldAttachment(newTemp, newPerm));
	}

	public static void addShieldBreakParticles(DamageSource src, LivingEntity entity) {
		ParticlePacket particlePacket = new ParticlePacket();

		Vec3 pos = src.getSourcePosition();
		if (src.getDirectEntity() instanceof LivingEntity living) pos = living.getEyePosition();
		if (src.getEntity() instanceof TraceableEntity traceable && traceable.getOwner() instanceof LivingEntity living) pos = living.getEyePosition();

		if (pos != null) {
			Vec3 lichPos = entity.position().add(0.0D, entity.getBbHeight() * 0.65D, 0.0D);
			Vec3 offset = pos.subtract(lichPos).multiply(1.0D, 0.0D, 1.0D).normalize();
			pos = lichPos.add(offset.scale(0.55D));

			double sizeRange = 0.85D;

			for (int j = 0; j < 16; ++j) {
				double horizontal = entity.getRandom().nextDouble() - 0.5D;
				double x = sizeRange * offset.z * horizontal;
				double y = sizeRange * (entity.getRandom().nextDouble() - 0.5D);
				double z = sizeRange * offset.x * -horizontal;
				particlePacket.queueParticle(TFParticleType.SHIELD_BREAK, pos.x + x, pos.y + y, pos.z + z, x * 0.5D, y * 0.5D, z * 0.5D);
			}
		} else {
			pos = entity.position().add(0.0D, entity.getBbHeight() * 0.65D, 0.0D);
			for (int j = 0; j < 16; ++j) {
				double x = (entity.getRandom().nextDouble() - 0.5D);
				double y = (entity.getRandom().nextDouble() - 0.5D) * 0.25D;
				double z = (entity.getRandom().nextDouble() - 0.5D);
				particlePacket.queueParticle(TFParticleType.SHIELD_BREAK, pos.x + x, pos.y + y, pos.z + z, x * 0.33D, y * 0.33D, z * 0.33D);
			}
		}

		PacketDistributor.sendToPlayersTrackingEntity(entity, particlePacket);
		if (entity instanceof ServerPlayer player) PacketDistributor.sendToPlayer(player, particlePacket);
	}

	public void setShields(LivingEntity entity, int amount, boolean temp) {
		int newTemp = temp ? Math.clamp(amount, 0, 115) : this.temporaryShields;
		int newPerm = !temp ? Math.clamp(amount, 0, 115) : this.permanentShields;
		entity.setAttached(TFDataAttachments.FORTIFICATION_SHIELDS, new FortificationShieldAttachment(newTemp, newPerm));
	}

	public void addShields(LivingEntity entity, int amount, boolean temp) {
		int newTemp;
		int newPerm;
		if (temp) {
			newTemp = Math.clamp(this.temporaryShields + amount, 0, 115);
			newPerm = this.permanentShields;
		} else {
			newTemp = this.temporaryShields;
			newPerm = Math.clamp(this.permanentShields + amount, 0, 115);
		}
		entity.setAttached(TFDataAttachments.FORTIFICATION_SHIELDS, new FortificationShieldAttachment(newTemp, newPerm));
	}

	private void resetTimer() {
		this.timer = 240;
	}
}
