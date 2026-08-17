package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import twilightforest.config.TFConfig;
import twilightforest.entity.CharmEffect;
import twilightforest.init.TFEntities;
import twilightforest.network.SpawnCharmPacket;
import twilightforest.util.ClientEntityIdProvider;

public class SpawnCharmPacketClientHandler {
	public static void handle(SpawnCharmPacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Player player = (Player) context.player();
			ClientLevel level = (ClientLevel) player.level();
			Entity camera = Minecraft.getInstance().getCameraEntity();
			// 26.3: gameRenderer.displayItemActivation() removed; use the charm effect entity animation instead
			CharmEffect effect = new CharmEffect(TFEntities.CHARM_EFFECT.get(), player.level(), player, packet.charm());
			effect.offset = (float) Math.PI;
			ClientEntityIdProvider.assignLocalId(effect);
			level.addEntity(effect);
			SoundEvent event = BuiltInRegistries.SOUND_EVENT.getValue(packet.event());
			if (camera != null && event != null) {
				level.playLocalSound(camera.getX(), camera.getY(), camera.getZ(), event, player.getSoundSource(), 1.5F, 1.0F, false);
			}
		});
	}
}
