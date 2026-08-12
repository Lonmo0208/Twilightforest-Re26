package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.item.GiantPickItem;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Inject(at = @At("TAIL"), method = "submitBlockOutline")
	private void twilight$renderGiantPickOutline(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LevelRenderState levelRenderState, CallbackInfo ci) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || !(mc.player.getMainHandItem().getItem() instanceof GiantPickItem)) {
			return;
		}

		BlockOutlineRenderState state = levelRenderState.blockOutlineRenderState;
		if (state == null) {
			return;
		}

		BlockPos pos = state.pos();
		Vec3 cameraPos = levelRenderState.cameraRenderState.pos;

		// Create a 4x4x4 bounding box shape
		int minX = pos.getX() & ~0b11;
		int minY = pos.getY() & ~0b11;
		int minZ = pos.getZ() & ~0b11;

		// Build a merged VoxelShape for the 4x4x4 volume
		VoxelShape giantShape = Shapes.empty();
		for (int dx = 0; dx < 4; dx++) {
			for (int dy = 0; dy < 4; dy++) {
				for (int dz = 0; dz < 4; dz++) {
					VoxelShape blockShape = Shapes.box(
						(double) dx, (double) dy, (double) dz,
						(double) (dx + 1), (double) (dy + 1), (double) (dz + 1)
					);
					giantShape = Shapes.or(giantShape, blockShape);
				}
			}
		}

		int outlineColor = state.highContrast() ? -11010079 : 0x66000000;
		float lineWidth = mc.gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth;

		poseStack.pushPose();
		poseStack.translate(minX - cameraPos.x, minY - cameraPos.y, minZ - cameraPos.z);
		submitNodeCollector.submitShapeOutline(poseStack, giantShape, RenderTypes.lines(), outlineColor, lineWidth, state.isTranslucent());
		poseStack.popPose();
	}
}