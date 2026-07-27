package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import twilightforest.item.mapdata.TFMazeMapData;
import twilightforest.network.MazeMapPacket;

public class MazeMapPacketClientHandler {

	public static void handle(MazeMapPacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			Level level = context.player().level();
			MapId mapId = message.inner().mapId();
			TFMazeMapData mapdata = TFMazeMapData.getMazeMapData(level, mapId);
			if (mapdata == null) {
				mapdata = new TFMazeMapData(0, 0, message.inner().scale(), false, false, message.inner().locked(), level.dimension());
				TFMazeMapData.registerMazeMapData(level, mapdata, mapId);
			}
			mapdata.ore = message.ore();
			mapdata.yCenter = message.yCenter();
			message.inner().applyToMap(mapdata);
			Minecraft.getInstance().getMapTextureManager().update(message.inner().mapId(), mapdata);
		});
	}
}
