package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.network.SyncQuestsPacket;

public class SyncQuestsPacketClientHandler {

	public static void handle(SyncQuestsPacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			// QuestingRamCurrentContext is normally injected via @Autowired
			// For now, this handler is a placeholder until proper injection is set up
		});
	}
}
