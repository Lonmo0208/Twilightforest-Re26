package twilightforest.network;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Fabric networking helper, replacing NeoForge's PacketDistributor.
 */
public class PacketDistributor {
	public static void sendToPlayer(ServerPlayer player, CustomPacketPayload packet) {
		ServerPlayNetworking.send(player, packet);
	}

	public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload packet) {
		for (ServerPlayer player : PlayerLookup.tracking(entity)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload packet) {
		for (ServerPlayer player : PlayerLookup.tracking(entity)) {
			ServerPlayNetworking.send(player, packet);
		}
		if (entity instanceof ServerPlayer serverPlayer) {
			ServerPlayNetworking.send(serverPlayer, packet);
		}
	}

	public static void sendToPlayersNear(ServerLevel level, ServerPlayer excluded, double x, double y, double z, double radius, CustomPacketPayload packet) {
		for (ServerPlayer player : PlayerLookup.around(level, new BlockPos((int) x, (int) y, (int) z), radius)) {
			if (player != excluded) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	public static void sendToPlayersTrackingChunk(Level level, ChunkPos chunkPos, CustomPacketPayload packet) {
		if (level instanceof ServerLevel serverLevel) {
			for (ServerPlayer player : PlayerLookup.tracking(serverLevel, chunkPos)) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	public static void sendToAllPlayers(CustomPacketPayload packet) {
		// Not directly available; caller should iterate players themselves
		throw new UnsupportedOperationException("Use ServerPlayNetworking directly for global broadcast");
	}
}
