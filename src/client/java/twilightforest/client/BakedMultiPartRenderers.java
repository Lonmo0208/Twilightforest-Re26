package twilightforest.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
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
import java.util.WeakHashMap;

@SuppressWarnings("deprecation")
public class BakedMultiPartRenderers {
	private static final Map<Identifier, EntityRenderer<?, ?>> renderers = new HashMap<>();
	/**
	 * Map for non-PartEntityState render states (e.g. FallingBlockRenderState from SnowQueenIceShield)
	 * that still need renderer redirection in the submit phase.
	 */
	private static final Map<EntityRenderState, Identifier> stateRenderers = new WeakHashMap<>();
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

	/**
	 * Register a renderer override for a non-PartEntityState render state.
	 * Used by renderers like SnowQueenIceShieldRenderer that create FallingBlockRenderState
	 * but are rendered through the PartEntity system.
	 */
	public static void registerStateRenderer(EntityRenderState state, Identifier rendererId) {
		stateRenderers.put(state, rendererId);
	}

	/**
	 * Look up a renderer for an EntityRenderState, handling both PartEntityState
	 * (with partRendererId) and non-PartEntityState (via stateRenderers map).
	 */
	public static EntityRenderer<?, ?> lookupByState(EntityRenderState state) {
		if (!baked)
			return null;
		// Check non-PartEntityState renderers first (e.g. SnowQueenIceShieldRenderer using FallingBlockRenderState)
		Identifier rendererId = stateRenderers.remove(state);
		if (rendererId != null) {
			return renderers.get(rendererId);
		}
		return null;
	}
}
