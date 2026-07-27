package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import twilightforest.item.mapdata.TFMagicMapData;
import twilightforest.network.MagicMapPacket;

public class MagicMapPacketClientHandler {

	public static void handle(MagicMapPacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Level level = context.player().level();
			MapId mapId = message.inner().mapId();
			TFMagicMapData mapdata = TFMagicMapData.getMagicMapData(level, mapId);
			if (mapdata == null) {
				mapdata = new TFMagicMapData(0, 0, message.inner().scale(), false, false, message.inner().locked(), level.dimension());
				TFMagicMapData.registerMagicMapData(level, mapdata, mapId);
			}
			message.inner().applyToMap(mapdata);
			mapdata.conqueredStructures.clear();
			mapdata.conqueredStructures.addAll(message.conqueredStructures());
			Minecraft.getInstance().getMapTextureManager().update(message.inner().mapId(), mapdata);
		});
	}
}
