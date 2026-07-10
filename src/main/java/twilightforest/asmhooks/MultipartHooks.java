package twilightforest.asmhooks;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import twilightforest.asm.transformers.multipart.ResolveEntityStateRendererTransformer;
import twilightforest.asm.transformers.multipart.SendDirtyEntityDataTransformer;
import twilightforest.util.multiparts.MultipartEntityUtil;

import java.util.Iterator;

@SuppressWarnings({"JavadocReference", "unused"})
public class MultipartHooks {

	@Autowired
	private static MultipartEntityUtil multipartEntityUtil;

	/**
	 * {@link twilightforest.mixin.ClientLevelMixin}<p/>
	 *
	 * Wraps an {@link java.util.Iterator} of entities to inject TFPart entities
	 * (HydraHead, HydraNeck, NagaSegment, SnowQueenIceShield) into the render pipeline.
	 * <p/>
	 * This is called by the Mixin {@link twilightforest.mixin.ClientLevelMixin#tf$injectTFPartEntities}
	 * which modifies {@link net.minecraft.client.multiplayer.ClientLevel#entitiesForRendering()}
	 * to wrap its return value.
	 */
	public static Iterator<Entity> resolveEntitiesForRendering(Iterator<Entity> iter) {
		if (multipartEntityUtil != null) {
			return multipartEntityUtil.injectTFPartEntities(iter);
		}
		return iter;
	}

	/**
	 * {@link twilightforest.asm.transformers.multipart.ResolveEntityRendererTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#getRenderer(Entity)}<br/>
	 * Targets: {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#renderers}
	 */
	@Nullable
	public static EntityRenderer<?, ?> resolveEntityRenderer(@Nullable EntityRenderer<?, ?> renderer, Entity entity) {
		if (multipartEntityUtil != null) {
			return multipartEntityUtil.tryLookupTFPartRenderer(renderer, entity);
		}
		return renderer;
	}

	/**
	 * {@link ResolveEntityStateRendererTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#getRenderer(EntityRenderState)}<br/>
	 * Targets: the renderer lookup result for PartEntity render states in the submit phase
	 */
	@Nullable
	public static EntityRenderer<?, ?> resolveEntityStateRenderer(@Nullable EntityRenderer<?, ?> renderer, EntityRenderState state) {
		if (multipartEntityUtil != null) {
			return multipartEntityUtil.tryLookupPartStateRenderer(renderer, state);
		}
		return renderer;
	}

	/**
	 * Register a renderer override for a non-PartEntityState render state.
	 * Call this from extractRenderState() for renderers that don't extend TFPartRenderer
	 * but still need PartEntity renderer redirection in the submit phase.
	 */
	public static void registerStateRenderer(EntityRenderState state, Identifier rendererId) {
		if (multipartEntityUtil != null) {
			multipartEntityUtil.registerStateRenderer(state, rendererId);
		}
	}

	/**
	 * {@link SendDirtyEntityDataTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.server.level.ServerEntity#sendDirtyEntityData}
	 */
	public static void sendDirtyEntityData(Entity entity) {
		if (multipartEntityUtil != null) {
			multipartEntityUtil.sendDirtyMultipartEntityData(entity);
		}
	}

}
