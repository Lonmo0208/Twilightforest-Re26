package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.init.TFDimension;
import twilightforest.network.StructureProtectionPacket;

public class StructureProtectionPacketClientHandler {

	public static void handle(StructureProtectionPacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			if (TFDimension.DIMENSION_KEY.equals(context.player().level().dimension())) {
				TFWeatherRenderer.setProtectedBoxes(message.boxes().orElse(null));
			}
		});
	}
}
