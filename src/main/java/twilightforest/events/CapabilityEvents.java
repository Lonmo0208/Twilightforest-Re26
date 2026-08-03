package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.LevelData;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDimension;
import twilightforest.util.TFEntityExtensions;
import twilightforest.world.TFTeleporter;
import twilightforest.world.NoReturnTeleporter;
import net.minecraft.util.Unit;
import twilightforest.components.entity.FortificationShieldAttachment;

//TODO: Make sure all the events are properly registered in the main mod class
public class CapabilityEvents {

	/*
	private void markPlayerAsRespawned(FabricEvents.PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			((TFEntityExtensions) player).getPersistentData().putBoolean(Player.PERSISTED_NBT_TAG, true);
		}
	}
	*/

	private void updateShields(FabricEvents.EntityTickEvent.Post event) {
		if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide() && ((TFEntityExtensions) living).twilightforest$hasData(TFDataAttachments.FORTIFICATION_SHIELDS)) {
			((TFEntityExtensions) event.getEntity()).twilightforest$getData(TFDataAttachments.FORTIFICATION_SHIELDS).tick(living);
		}
	}

	private void updatePlayerCaps(FabricEvents.PlayerTickEvent.Post event) {
		if (((TFEntityExtensions) event.getEntity()).twilightforest$getData(TFDataAttachments.FEATHER_FAN)) {
			event.getEntity().setIgnoreFallDamageFromCurrentImpulse(true, event.getEntity().position());
			event.getEntity().currentImpulseImpactPos = event.getEntity().position();

			if (event.getEntity().onGround() || event.getEntity().isSwimming() || event.getEntity().isInWater()) {
				((TFEntityExtensions) event.getEntity()).twilightforest$setData(TFDataAttachments.FEATHER_FAN, false);
			}
		}
		((TFEntityExtensions) event.getEntity()).twilightforest$getData(TFDataAttachments.YETI_THROWING).tick(event.getEntity());
		((TFEntityExtensions) event.getEntity()).twilightforest$getData(TFDataAttachments.TF_PORTAL_COOLDOWN).tick(event.getEntity());
	}

	private void absorbShieldHits(FabricEvents.LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();
		// shields
		if (!living.level().isClientSide() && !event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
            FortificationShieldAttachment attachment = ((TFEntityExtensions) living).twilightforest$getData(TFDataAttachments.FORTIFICATION_SHIELDS);
			if (attachment.shieldsLeft() > 0) {
				if (living.invulnerableTime <= 0) {
					attachment.breakShield(living, false);
					FortificationShieldAttachment.addShieldBreakParticles(event.getSource(), living);
					living.invulnerableTime = 20;
				}
				event.setCanceled(true);
			}
		}
	}

	private void spawnInTFIfNecessary(FabricEvents.PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

		if (serverPlayer.getRespawnConfig() == null) {
			newSpawnInTwilightForest(serverPlayer);
		}
	}

	/**
	 * When player logs in, report conflict status, set progression status
	 */
	public void playerLogsIn(FabricEvents.PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity().level().isClientSide() || !(event.getEntity() instanceof ServerPlayer player))
			return;
		dataFixLegacyBanish(player);
		if (!((TFEntityExtensions) player).twilightforest$hasData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			newSpawnInTwilightForest(player);
	}

	private static void newSpawnInTwilightForest(ServerPlayer player) {
		if (!TFConfig.newPlayersSpawnInTF)
			return;
		ServerLevel level = player.level().getServer().getLevel(TFDimension.DIMENSION_KEY);
		if (level == null)
			return;

		BlockPos newDefaultSpawn = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, player.blockPosition());

		player.teleport(TFConfig.portalForNewPlayerSpawn ?
			TFTeleporter.createTransition(player, level, newDefaultSpawn, true) :
			NoReturnTeleporter.createNoPortalTransition(level, player, newDefaultSpawn));
		player.setRespawnPosition(new ServerPlayer.RespawnConfig(LevelData.RespawnData.of(TFDimension.DIMENSION_KEY, newDefaultSpawn, player.getYRot(), 0.0F), true), false);

		((TFEntityExtensions) player).twilightforest$setData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}

	private static void dataFixLegacyBanish(ServerPlayer player) {
		CompoundTag tagCompound = ((TFEntityExtensions) player).twilightforest$getPersistentData();
		final String PERSISTED_NBT_TAG = "PlayerPersisted";
		if (!tagCompound.contains(PERSISTED_NBT_TAG))
			return;
		CompoundTag playerData = tagCompound.getCompound(PERSISTED_NBT_TAG).orElse(new CompoundTag());
		if (!playerData.contains("twilightforest_banished"))
			return;

		playerData.remove("twilightforest_banished");
		tagCompound.put(PERSISTED_NBT_TAG, playerData);

		if (((TFEntityExtensions) player).twilightforest$hasData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			return;

		((TFEntityExtensions) player).twilightforest$setData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}
}
