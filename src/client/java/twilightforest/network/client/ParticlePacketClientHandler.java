package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.network.ParticlePacket;

public class ParticlePacketClientHandler {

	public static void handle(ParticlePacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			for (var queuedParticle : message.queuedParticles()) {
				context.player().level().addParticle(queuedParticle.particleOptions(), queuedParticle.x(), queuedParticle.y(), queuedParticle.z(), queuedParticle.x2(), queuedParticle.y2(), queuedParticle.z2());
			}
		});
	}
}
