package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.inventory.UncraftingMenu;
import twilightforest.network.SyncUncraftingCostsPacket;

public class SyncUncraftingCostsPacketClientHandler {

	public static void handle(SyncUncraftingCostsPacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			if (context.client().player != null && context.client().player.containerMenu instanceof UncraftingMenu uncrafting) {
				uncrafting.setClientCosts(packet.uncraftingCost(), packet.recraftingCost());
			}
		});
	}
}
