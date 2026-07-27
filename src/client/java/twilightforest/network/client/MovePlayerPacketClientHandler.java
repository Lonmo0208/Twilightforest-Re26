package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.network.MovePlayerPacket;

public class MovePlayerPacketClientHandler {

	public static void handle(MovePlayerPacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> context.player().push(message.motionX(), message.motionY(), message.motionZ()));
	}
}
