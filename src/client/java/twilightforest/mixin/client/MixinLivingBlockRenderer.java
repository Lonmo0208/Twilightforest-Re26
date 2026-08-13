package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.List;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.model.BlockStateModelWrapper;
import net.minecraft.client.renderer.entity.LivingBlockRenderer;
import net.minecraft.client.renderer.entity.state.LivingBlockRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.renderer.block.JarRenderer;
import twilightforest.components.item.JarLid;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;

@Mixin(LivingBlockRenderer.class)
public abstract class MixinLivingBlockRenderer {

	private static final Reference2ReferenceOpenHashMap<LivingBlockRenderState, LidRenderData> LID_DATA = new Reference2ReferenceOpenHashMap<>();

	private static boolean isJar(Item item) {
		ResourceKey<Item> key = item.builtInRegistryHolder().key();
		if (key == null) return false;
		Identifier id = key.identifier();
		if (!id.getNamespace().equals("twilightforest")) return false;
		String path = id.getPath();
		return path.equals("mason_jar") || path.equals("firefly_jar") || path.equals("cicada_jar");
	}

	@Inject(method = "extractRenderState", at = @At("TAIL"))
	private void tf$extractJarLidState(LivingBlock entity, LivingBlockRenderState state, float partialTicks, CallbackInfo ci) {
		ItemStack itemStack = entity.getItemStack();
		if (!itemStack.isEmpty() && isJar(itemStack.getItem())) {
			if (itemStack.get(TFDataComponents.JAR_LID) instanceof JarLid jarLid) {
				Item lid = jarLid.lid();
				if (JarRenderer.LID_KEYS.containsKey(lid)) {
					BlockStateModel lidBsm = JarRenderer.getLidModel(lid);
					if (lidBsm != null) {
						BlockState blockState = TFBlocks.MASON_JAR.defaultBlockState();
						LidRenderData data = LID_DATA.computeIfAbsent(state, s -> new LidRenderData());
						BlockModel lidModel = new BlockStateModelWrapper(lidBsm, List.of(), new Matrix4f().identity());
						lidModel.update(data.lidModel, blockState, BlockDisplayContext.create(), 42L);
						data.lid = lid;
						return;
					}
				}
			}
			LidRenderData existing = LID_DATA.get(state);
			if (existing != null) {
				existing.lid = null;
			}
		} else {
			LidRenderData existing = LID_DATA.get(state);
			if (existing != null) {
				existing.lid = null;
			}
		}
	}

	@Inject(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockModelRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V", shift = At.Shift.AFTER))
	private void tf$renderJarLid(LivingBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
		LidRenderData data = LID_DATA.get(state);
		if (data != null && data.lid != null && JarRenderer.LID_KEYS.containsKey(data.lid)) {
			int overlayCoords = OverlayTexture.pack(0.0F, state.hasRedOverlay);
			poseStack.pushPose();
			data.lidModel.submit(poseStack, submitNodeCollector, state.lightCoords, overlayCoords, state.outlineColor);
			poseStack.popPose();
		}
	}

	@Inject(method = "submit", at = @At("TAIL"))
	private void tf$cleanupState(LivingBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
		LID_DATA.remove(state);
	}

	private static class LidRenderData {
		final BlockModelRenderState lidModel = new BlockModelRenderState();
		Item lid = null;
	}
}
