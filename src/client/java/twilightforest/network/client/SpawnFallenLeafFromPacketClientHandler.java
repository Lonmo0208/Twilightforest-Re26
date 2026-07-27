package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import twilightforest.init.TFParticleType;
import twilightforest.network.SpawnFallenLeafFromPacket;

import java.util.Random;

public class SpawnFallenLeafFromPacketClientHandler {

	public static void handle(SpawnFallenLeafFromPacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Level level = context.player().level();
			Random rand = new Random();
			int color = -1;
			int r = Mth.clamp(((color >> 16) & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int g = Mth.clamp(((color >> 8) & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int b = Mth.clamp((color & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
			level.addParticle(ColorParticleOption.create(TFParticleType.FALLEN_LEAF, r, g, b),
				message.pos().getX() + level.getRandom().nextFloat(),
				message.pos().getY(),
				message.pos().getZ() + level.getRandom().nextFloat(),
				(level.getRandom().nextFloat() * -0.5F) * message.motion().x(),
				level.getRandom().nextFloat() * 0.5F + 0.25F,
				(level.getRandom().nextFloat() * -0.5F) * message.motion().z()
			);
		});
	}
}
