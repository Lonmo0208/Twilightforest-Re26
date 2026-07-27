package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.client.MovingCicadaSoundInstance;
import twilightforest.network.CreateMovingCicadaSoundPacket;

public class CreateMovingCicadaSoundPacketClientHandler {

	public static void handle(CreateMovingCicadaSoundPacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Entity entity = context.player().level().getEntity(message.entityID());
			if (entity instanceof LivingEntity living) {
				Minecraft.getInstance().getSoundManager().queueTickingSound(new MovingCicadaSoundInstance(living));
			}
		});
	}
}
