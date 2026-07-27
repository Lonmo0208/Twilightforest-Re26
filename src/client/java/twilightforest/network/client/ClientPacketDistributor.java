package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Client-side packet distributor for sending packets from client to server.
 */
public class ClientPacketDistributor {
	public static void sendToServer(CustomPacketPayload packet) {
		ClientPlayNetworking.send(packet);
	}
}
