package twilightforest.client.renderer.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class TFArmorRenderer {
	public static final List<TFArmorRenderer> INSTANCES = new ArrayList<>();
	private final ModelLayerLocation[] layerLocations;
	protected final Map<ModelLayerLocation, Supplier<ModelPart>> ARMOR_MODELS = new HashMap<>();

	public TFArmorRenderer(ModelLayerLocation... layerLocations) {
		this.layerLocations = layerLocations;
		reloadModels();
	}

	private void reloadModels() {
		ARMOR_MODELS.clear();
		for (ModelLayerLocation layerLocation : layerLocations) {
			ARMOR_MODELS.put(layerLocation, () -> Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation));
		}
	}

	public void resetModelCache() {
		reloadModels();
	}

	public static void resetAllModelCache() {
		INSTANCES.forEach(TFArmorRenderer::resetModelCache);
	}

	protected ModelPart getModelPart(ModelLayerLocation layerLocation) {
		return ARMOR_MODELS.get(layerLocation).get();
	}

	public static final class ResourceReloadListener implements ResourceManagerReloadListener {
		@Override
		public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
			TFArmorRenderer.resetAllModelCache();
		}
	}
}
