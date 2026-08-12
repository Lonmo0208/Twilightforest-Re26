package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import twilightforest.client.MissingAdvancementToast;
import twilightforest.network.MissingAdvancementToastPacket;

public class MissingAdvancementToastPacketClientHandler {

	public static void handle(MissingAdvancementToastPacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Minecraft.getInstance().gui.toastManager().addToast(new MissingAdvancementToast(packet.title(), packet.icon()));
		});
	}
}
