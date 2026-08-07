package twilightforest.client.renderer.entity.layers;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import twilightforest.TwilightForestMod;

public class TFShieldState {

	public static final RenderStateDataKey<Integer> SHIELD_COUNT_KEY = RenderStateDataKey.create(() -> TwilightForestMod.ID + ":shield_count");

	public static void setShieldCount(LivingEntityRenderState state, int count) {
		state.setData(SHIELD_COUNT_KEY, count);
	}

	public static int getShieldCount(LivingEntityRenderState state) {
		Integer val = state.getData(SHIELD_COUNT_KEY);
		return val == null ? 0 : val;
	}
}
