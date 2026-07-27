package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.network.SetMasonJarItemPacket;

public class SetMasonJarItemPacketClientHandler {

	public static void handle(SetMasonJarItemPacket packet, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			if (context.player().level() instanceof ClientLevel level && level.getBlockEntity(packet.pos()) instanceof MasonJarBlockEntity blockEntity) {
				blockEntity.getItemHandler().setItem(packet.stack());
				blockEntity.setItemRotation(packet.rotation());
				blockEntity.setChanged();
			}
		});
	}
}
