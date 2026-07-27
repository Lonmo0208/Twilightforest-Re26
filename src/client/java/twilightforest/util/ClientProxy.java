package twilightforest.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import twilightforest.init.TFItems;

@Environment(EnvType.CLIENT)
public class ClientProxy {

	public static boolean isPlayerHoldingMagicBeans() {
		var player = Minecraft.getInstance().player;
		if (player == null) return false;
		return player.getMainHandItem().is(TFItems.MAGIC_BEANS) || player.getOffhandItem().is(TFItems.MAGIC_BEANS);
	}
}