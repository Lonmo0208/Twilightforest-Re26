package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.client.state.entity.PartEntityState;
import twilightforest.entity.TFPart;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@Unique
	private static final Logger TF_LOGGER = LoggerFactory.getLogger("twilightforest");

	@Shadow @Final private BlockModelResolver blockModelResolver;
	@Shadow @Final private ItemModelResolver itemModelResolver;
	@Shadow @Final private MapRenderer mapRenderer;
	@Shadow @Final private AtlasManager atlasManager;
	@Shadow @Final private EquipmentAssetManager equipmentAssets;
	@Shadow @Final private PlayerSkinRenderCache playerSkinRenderCache;

	/**
	 * Bake multi-part renderers after the EntityRenderDispatcher reloads its renderers.
	 * This is the Fabric equivalent of the NeoForge EntityRenderersEvent.AddLayers event.
	 */
	@Inject(method = "onResourceManagerReload", at = @At("TAIL"))
	private void tf$bakeMultiPartRenderers(ResourceManager resourceManager, CallbackInfo ci) {
		EntityRenderDispatcher self = (EntityRenderDispatcher) (Object) this;
		BakedMultiPartRenderers.bakeMultiPartRenderers(
			new EntityRendererProvider.Context(
				self,
				this.blockModelResolver,
				this.itemModelResolver,
				this.mapRenderer,
				resourceManager,
				Minecraft.getInstance().getEntityModels(),
				this.equipmentAssets,
				this.atlasManager,
				Minecraft.getInstance().font,
				this.playerSkinRenderCache
			)
		);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private <E extends Entity> void tf$redirectPartEntityShouldRender(E entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof TFPart<?> part) {
			EntityRenderer partRenderer = BakedMultiPartRenderers.lookup(part.renderer());
			if (partRenderer != null) {
				cir.setReturnValue(partRenderer.shouldRender(entity, frustum, x, y, z));
			}
			// If not found in BakedMultiPartRenderers, let the standard Fabric registry handle it
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Inject(method = "extractEntity", at = @At("HEAD"), cancellable = true)
	private <E extends Entity> void tf$redirectPartEntityExtract(E entity, float partialTick, CallbackInfoReturnable<EntityRenderState> cir) {
		if (entity instanceof TFPart<?> part) {
			EntityRenderer partRenderer = BakedMultiPartRenderers.lookup(part.renderer());
			if (partRenderer != null) {
				cir.setReturnValue(partRenderer.createRenderState(entity, partialTick));
			}
			// If not found in BakedMultiPartRenderers, let the standard Fabric registry handle it
		}
	}

	/**
	 * Redirect getRenderer(Entity) to return the correct PartEntity renderer.
	 * This is the Fabric equivalent of the ASM ResolveEntityRendererTransformer.
	 * Without this, TFPart entities get the parent entity's renderer, causing
	 * incorrect rendering or crashes.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Inject(method = "getRenderer(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;", at = @At("HEAD"), cancellable = true)
	private <E extends Entity> void tf$redirectPartEntityGetRenderer(E entity, CallbackInfoReturnable<EntityRenderer<? super E, ?>> cir) {
		if (entity instanceof TFPart<?> part) {
			EntityRenderer partRenderer = BakedMultiPartRenderers.lookup(part.renderer());
			if (partRenderer != null) {
				cir.setReturnValue(partRenderer);
			}
			// If not found in BakedMultiPartRenderers, let the standard Fabric registry handle it
		}
	}

	/**
	 * Redirect getRenderer(EntityRenderState) to return the correct PartEntity renderer
	 * during the submit phase. This is the Fabric equivalent of the ASM
	 * ResolveEntityStateRendererTransformer.
	 * In 26.1.2, the submit phase looks up the renderer by entityRenderState.entityType,
	 * which for PartEntities equals the parent's type, causing the wrong renderer to be used.
	 * Also handles non-PartEntityState renderers (e.g. SnowQueenIceShieldRenderer using
	 * FallingBlockRenderState) via BakedMultiPartRenderers.lookupByState().
	 */
	@Inject(method = "getRenderer(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)Lnet/minecraft/client/renderer/entity/EntityRenderer;", at = @At("HEAD"), cancellable = true)
	private void tf$redirectPartStateGetRenderer(EntityRenderState state, CallbackInfoReturnable<EntityRenderer<?, ?>> cir) {
		// Check PartEntityState first (Naga, Hydra, etc.)
		if (state instanceof PartEntityState partState && partState.partRendererId != null) {
			EntityRenderer<?, ?> partRenderer = BakedMultiPartRenderers.lookup(partState.partRendererId);
			if (partRenderer != null) {
				cir.setReturnValue(partRenderer);
				return;
			}
			// If not found in BakedMultiPartRenderers, let the standard Fabric registry handle it
		}
		// Check non-PartEntityState renderers (SnowQueenIceShield using FallingBlockRenderState)
		EntityRenderer<?, ?> stateRenderer = BakedMultiPartRenderers.lookupByState(state);
		if (stateRenderer != null) {
			cir.setReturnValue(stateRenderer);
		}
		// If stateRenderer is null (e.g. normal LivingEntityRenderState from spawner),
		// do NOT call cir.setReturnValue - fallback to standard Fabric registry lookup,
		// which would be overwritten to null and cause NPE in submit().
	}

	/**
	 * Last-resort defensive guard against null renderers in submit().
	 * If neither the custom BakedMultiPartRenderers nor the standard Fabric registry
	 * produced a renderer (can happen with unregistered entity types from spawners
	 * or mod compatibility scenarios), cancel the submit instead of crashing with NPE.
	 * Also logs a warning once to aid debugging.
	 */
	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/client/renderer/state/level/CameraRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	private <S extends EntityRenderState> void tf$submitGuardAgainstNullRenderer(
		S renderState, CameraRenderState camera, double x, double y, double z,
		PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CallbackInfo ci
	) {
		EntityRenderDispatcher self = (EntityRenderDispatcher) (Object) this;
		EntityRenderer<?, ? super S> renderer = self.getRenderer(renderState);
		if (renderer == null) {
			TF_LOGGER.warn(
				"[TF-RenderGuard] Skipping entity render submit because no renderer was found! " +
					"stateClass={}, entityType={}. This indicates a renderer registration mismatch or " +
					"a spawner trying to render an unregistered entity type. Preventing NPE crash.",
				renderState == null ? "null" : renderState.getClass().getName(),
				renderState == null ? "null" : renderState.entityType
			);
			ci.cancel();
		}
	}
}
