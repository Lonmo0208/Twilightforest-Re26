package twilightforest.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.block.JarRenderer;

@Environment(EnvType.CLIENT)
public class TFModelLoadingPlugin implements ModelLoadingPlugin {
	@Override
	public void initialize(Context pluginContext) {
		// Populate LID_KEYS first so we have model IDs available
		JarRenderer.populateLidKeys();

		// Register all lid models as extra models
		for (JarRenderer.LidResource lidResource : JarRenderer.LID_LOCATION_LIST) {
			var modelId = lidResource.getModelId();
			var lid = lidResource.lid();
			var key = ExtraModelKey.<BlockStateModel>create(modelId::toString);
			JarRenderer.LID_MODEL_KEYS.put(lid, key);
			pluginContext.addModel(key, SimpleUnbakedExtraModel.blockStateModel(modelId));
		}
	}
}
