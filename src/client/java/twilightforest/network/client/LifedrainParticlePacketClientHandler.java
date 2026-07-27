package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.ClientProxy;
import twilightforest.network.LifedrainParticlePacket;

public class LifedrainParticlePacketClientHandler {

	public static void handle(LifedrainParticlePacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Entity entity = context.player().level().getEntity(packet.entityID());
			if (entity instanceof LivingEntity living) {
				if (ClientProxy.lifedrainTrailRenderer != null) ClientProxy.lifedrainTrailRenderer.makeRedMagicTrail(living.level(), living, packet.victimPos());
			}
		});
	}
}
