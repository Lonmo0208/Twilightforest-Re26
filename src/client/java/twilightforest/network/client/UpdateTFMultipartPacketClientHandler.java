package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.Entity;
import twilightforest.entity.TFPart;
import twilightforest.network.UpdateTFMultipartPacket;
import twilightforest.util.TFEntityExtensions;

public class UpdateTFMultipartPacketClientHandler {

	public static void handle(UpdateTFMultipartPacket message, ClientPlayNetworking.Context ctx) {
		ctx.client().execute(() -> {
			int eId = message.entity() != null && message.entityId() <= 0 ? message.entity().getId() : message.entityId();
			Entity ent = ctx.player().level().getEntity(eId);
			if (ent instanceof TFEntityExtensions extensions && extensions.twilightforest$isMultipartEntity()) {
				Entity[] parts = extensions.twilightforest$getParts();
				if (parts == null)
					return;
				for (Entity part : parts) {
					if (part instanceof TFPart<?> tfPart) {
						if (message.data() == null && message.entity() != null) {
							for (Entity p : ((TFEntityExtensions) message.entity()).twilightforest$getParts()) {
								if (p instanceof TFPart<?> srcPart && p.getId() == part.getId()) {
									tfPart.readData(srcPart.writeData());
									break;
								}
							}
						} else if (message.data() != null) {
							UpdateTFMultipartPacket.PartDataHolder data = message.data().get(tfPart.getId());
							if (data != null)
								tfPart.readData(data);
						}
					}
				}
			}
		});
	}
}