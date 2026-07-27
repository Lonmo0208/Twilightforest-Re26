package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import twilightforest.init.TFGameRules;
import twilightforest.network.EnforceProgressionStatusPacket;

public class EnforceProgressionStatusPacketClientHandler {

	public static void handle(EnforceProgressionStatusPacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			try {
				Object mc = Minecraft.getInstance();
				java.lang.reflect.Method method = mc.getClass().getMethod("getSingleplayerServer");
				Object serverObj = method.invoke(mc);
				if (serverObj instanceof MinecraftServer server) {
					server.getGameRules().set(TFGameRules.ENFORCED_PROGRESSION_RULE, message.enforce(), server);
				}
			} catch (Exception e) {
				// Dedicated server - IntegratedServer not available
			}
		});
	}
}
