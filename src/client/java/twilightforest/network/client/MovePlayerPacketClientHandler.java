package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.network.MovePlayerPacket;

public class MovePlayerPacketClientHandler {

	public static void handle(MovePlayerPacket message, ClientPlayNetworking.Context context) {
		// Push is handled by entity sync (needsSync=true in Entity.push())
		// Do NOT apply push here again - it causes double velocity on client
	}
}
