package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.entity.LivingBlockRenderer;
import net.minecraft.client.renderer.entity.state.LivingBlockRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
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

	private static class LidRenderData {
		final BlockModelRenderState lidModel = new BlockModelRenderState();
		Item lid = null;
	}

	@Inject(method = "extractRenderState", at = @At("TAIL"))
	private void twilightforest$extractJarLid(LivingBlock entity, LivingBlockRenderState state, float partialTicks, CallbackInfo ci) {
		ItemStack itemStack = entity.getItemStack();
		if (!itemStack.isEmpty()) {
			JarLid jarLid = itemStack.get(TFDataComponents.JAR_LID.get());
			if (jarLid != null) {
				Item lid = jarLid.lid();
				if (JarRenderer.LID_KEYS.containsKey(lid)) {
					StandaloneModelKey<BlockModel> key = JarRenderer.LID_KEYS.get(lid);
					BlockModel lidModel = Minecraft.getInstance().getModelManager().getStandaloneModel(key);
					if (lidModel != null) {
						BlockState blockState = TFBlocks.MASON_JAR.get().defaultBlockState();
						LidRenderData data = LID_DATA.computeIfAbsent(state, s -> new LidRenderData());
						lidModel.update(data.lidModel, blockState, BlockDisplayContext.create(), 42L);
						data.lid = lid;
						return;
					}
				}
			}
		}
		LidRenderData data = LID_DATA.get(state);
		if (data != null) {
			data.lid = null;
		}
	}

	@Inject(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockModelRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V", shift = At.Shift.AFTER))
	private void twilightforest$renderJarLid(LivingBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
		LidRenderData data = LID_DATA.get(state);
		if (data != null && data.lid != null && JarRenderer.LID_KEYS.containsKey(data.lid)) {
			int overlayCoords = OverlayTexture.pack(0.0F, state.hasRedOverlay);
			data.lidModel.submit(poseStack, submitNodeCollector, state.lightCoords, overlayCoords, state.outlineColor);
		}
	}
}
