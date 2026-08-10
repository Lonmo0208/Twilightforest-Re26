package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import twilightforest.init.TFDataAttachments;
import twilightforest.network.UpdateThrownPacket;

public class UpdateThrownPacketClientHandler {

	public static void handle(UpdateThrownPacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Level level = context.player().level();
			Entity entity = level.getEntity(message.entityID());
			if (entity instanceof Player player) {
				var attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.YETI_THROWING, twilightforest.components.entity.YetiThrowAttachment::new);
				LivingEntity thrower = message.thrower() != 0 ? (LivingEntity) level.getEntity(message.thrower()) : null;
				if (attachment != null) {
					attachment.setThrown(player, message.thrown(), thrower);
					attachment.setThrowCooldown(player, message.throwCooldown());
				}
			}
		});
	}
}
