package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.init.TFDataAttachments;
import twilightforest.network.TravellersWingsStatePacket;

public class TravellersWingsStatePacketClientHandler {

	public static void handle(TravellersWingsStatePacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Player player = (Player) context.player();
			if (player != null && player.level() != null) {
				Entity entity = player.level().getEntity(message.entityId);
				if (entity instanceof LivingEntity livingEntity) {
					var attachment = TFDataAttachments.getOrCreate(livingEntity, TFDataAttachments.TRAVELLERS_WINGS, twilightforest.components.entity.TravellersWingsAttachment::new);
					if (attachment != null) {
						attachment.state = message.state;
						attachment.sidestepLeft = message.sidestepLeft;
						attachment.doubleJumpTimer = message.doubleJumpTimer;
						attachment.sidestepTimer = message.sidestepTimer;
					}
				}
			}
		});
	}
}
