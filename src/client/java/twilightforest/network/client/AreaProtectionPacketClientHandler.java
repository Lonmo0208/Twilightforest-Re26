package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import twilightforest.entity.ProtectionBox;
import twilightforest.init.TFParticleType;
import twilightforest.network.AreaProtectionPacket;

public class AreaProtectionPacketClientHandler {

	public static void handle(AreaProtectionPacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			ClientLevel level = context.player().level() instanceof ClientLevel clientLevel ? clientLevel : Minecraft.getInstance().level;
			message.sbb.forEach(box -> {
				for (Entity entity : level.entitiesForRendering()) {
					if (entity instanceof ProtectionBox prot) {
						if (prot.lifeTime > 0 && prot.matches(box)) {
							prot.resetLifetime();
							return;
						}
					}
				}
				level.addEntity(new ProtectionBox(level, box));
			});
			for (int i = 0; i < 20; i++) {
				double vx = level.getRandom().nextGaussian() * 0.02D;
				double vy = level.getRandom().nextGaussian() * 0.02D;
				double vz = level.getRandom().nextGaussian() * 0.02D;
				double x = message.pos.getX() + 0.5D + level.getRandom().nextFloat() - level.getRandom().nextFloat();
				double y = message.pos.getY() + 0.5D + level.getRandom().nextFloat() - level.getRandom().nextFloat();
				double z = message.pos.getZ() + 0.5D + level.getRandom().nextFloat() - level.getRandom().nextFloat();
				level.addParticle(TFParticleType.PROTECTION, x, y, z, vx, vy, vz);
			}
		});
	}
}
