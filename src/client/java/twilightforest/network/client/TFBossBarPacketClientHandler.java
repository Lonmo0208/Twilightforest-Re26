package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import twilightforest.entity.boss.bar.ClientTFBossBar;
import twilightforest.network.TFBossBarPacket;

public class TFBossBarPacketClientHandler {

	public static void handleAdd(TFBossBarPacket.AddTFBossBarPacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.gui.hud.getBossOverlay().events.put(packet.id, new ClientTFBossBar(packet.id, packet.name, packet.progress, packet.color, packet.overlay, packet.darkenScreen, packet.playMusic, packet.createWorldFog));
		});
	}

	public static void handleUpdateStyle(TFBossBarPacket.UpdateTFBossBarStylePacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.gui.hud.getBossOverlay().events.get(packet.id) instanceof ClientTFBossBar bossEvent) {
				bossEvent.setBarColor(packet.color);
				bossEvent.setOverlay(packet.overlay);
				if (!packet.allowLerp) bossEvent.setSetTime(bossEvent.getSetTime() - 200L); // Boss bars lerp over 100 milliseconds, we sometimes don't want that
			}
		});
	}
}
