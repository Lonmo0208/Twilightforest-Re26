package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.network.UpdateDeathTimePacket;

public class UpdateDeathTimePacketClientHandler {

	public static void handle(UpdateDeathTimePacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			ClientLevel level = Minecraft.getInstance().level;
			if (level != null && level.getEntity(message.entityID()) instanceof LivingEntity living) {
				living.deathTime = message.deathTime();
			}
		});
	}
}
