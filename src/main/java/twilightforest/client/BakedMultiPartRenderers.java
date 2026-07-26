package twilightforest.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.Identifier;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HydraHeadModel;
import twilightforest.client.model.entity.HydraNeckModel;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.client.renderer.entity.HydraHeadRenderer;
import twilightforest.client.renderer.entity.HydraNeckRenderer;
import twilightforest.client.renderer.entity.NagaSegmentRenderer;
import twilightforest.client.renderer.entity.SnowQueenIceShieldRenderer;
import twilightforest.entity.TFPart;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraNeck;
import twilightforest.entity.boss.NagaSegment;
import twilightforest.entity.boss.SnowQueenIceShield;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class BakedMultiPartRenderers {
	private static final Map<Identifier, EntityRenderer<?, ?>> renderers = new HashMap<>();
	private static boolean baked = false;

	public static void bakeMultiPartRenderers(EntityRendererProvider.Context context) {
		renderers.put(TFPart.RENDERER, new NoopRenderer<>(context));
		renderers.put(HydraHead.RENDERER, new HydraHeadRenderer(context, new HydraHeadModel(context.bakeLayer(TFModelLayers.HYDRA_HEAD))));
		renderers.put(HydraNeck.RENDERER, new HydraNeckRenderer(context, new HydraNeckModel(context.bakeLayer(TFModelLayers.HYDRA_NECK))));
		renderers.put(SnowQueenIceShield.RENDERER, new SnowQueenIceShieldRenderer(context));
		renderers.put(NagaSegment.RENDERER, new NagaSegmentRenderer(context, new NagaModel<>(context.bakeLayer(TFModelLayers.NAGA_BODY))));
		baked = true;
	}

	public static EntityRenderer<?, ?> lookup(Identifier location) {
		if (!baked)
			return null;
		return renderers.get(location);
	}
}
