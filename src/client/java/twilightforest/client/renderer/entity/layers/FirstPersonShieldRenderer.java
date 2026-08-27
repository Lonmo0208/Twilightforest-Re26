package twilightforest.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

public class FirstPersonShieldRenderer {

	private static final Identifier SHIELD_FRAME = TwilightForestMod.prefix("textures/item/lich_shield_frame.png");
	private static final Identifier SHIELD_FILL = TwilightForestMod.prefix("textures/item/lich_shield_fill.png");
	private static final net.minecraft.client.renderer.rendertype.RenderType FRAME_RENDER_TYPE = RenderTypes.entityCutout(SHIELD_FRAME);
	private static final net.minecraft.client.renderer.rendertype.RenderType FILL_RENDER_TYPE = RenderTypes.entityTranslucent(SHIELD_FILL);

	public static void render(LevelRenderContext context) {
		if (!TFConfig.firstPersonShieldScepterRenderer) return;
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null) return;
		if (!minecraft.options.getCameraType().isFirstPerson()) return;

		Player player = minecraft.player;
		FortificationShieldAttachment attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.FORTIFICATION_SHIELDS, twilightforest.components.entity.FortificationShieldAttachment::new);
		if (attachment == null) return;

		int count = attachment.shieldsLeft();
		if (count <= 0) return;

		SubmitNodeCollector collector = context.submitNodeCollector();
		PoseStack stack = context.poseStack();

		Vec3 cameraPos = context.levelState().cameraRenderState.pos;
		Vec3 playerEyePos = player.position().add(0, player.getEyeHeight(), 0);

		float partialTick = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
		float age = player.tickCount + partialTick;

		stack.pushPose();
		stack.translate(
			playerEyePos.x - cameraPos.x,
			playerEyePos.y - cameraPos.y,
			playerEyePos.z - cameraPos.z
		);
		stack.scale(1.0F, -1.0F, -1.0F);

		float rotateAngleY = age / -5.5F;
		float rotateAngleX = Mth.sin(age / 5.5F) / 4.0F;
		float rotateAngleZ = Mth.cos(age / 5.5F) / 4.0F;

		for (int c = 0; c < count; c++) {
			stack.pushPose();

			stack.mulPose(new org.joml.Matrix4f().rotation(Axis.YP.rotationDegrees(rotateAngleY * (180.0F / Mth.PI) + (c * (360.0F / count)))));
			stack.mulPose(new org.joml.Matrix4f().rotation(Axis.XP.rotationDegrees(rotateAngleX * (180.0F / Mth.PI))));
			stack.mulPose(new org.joml.Matrix4f().rotation(Axis.ZP.rotationDegrees(rotateAngleZ * (180.0F / Mth.PI))));

			stack.translate(0.0F, 0.4F, -0.7F);

			renderShieldQuad(stack, collector, FILL_RENDER_TYPE, 0xF000F0);
			renderShieldQuad(stack, collector, FRAME_RENDER_TYPE, 0xF000F0);

			stack.popPose();
		}

		stack.popPose();
	}

	private static void renderShieldQuad(PoseStack stack, SubmitNodeCollector collector, net.minecraft.client.renderer.rendertype.RenderType renderType, int light) {
		collector.submitCustomGeometry(stack, renderType, (pose, buffer) -> {
			buffer.addVertex(pose, -0.5F, 0.5F, 0.0F).setColor(-1).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, 1.0F);
			buffer.addVertex(pose, 0.5F, 0.5F, 0.0F).setColor(-1).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, 1.0F);
			buffer.addVertex(pose, 0.5F, -0.5F, 0.0F).setColor(-1).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, 1.0F);
			buffer.addVertex(pose, -0.5F, -0.5F, 0.0F).setColor(-1).setUv(0.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, 1.0F);

			buffer.addVertex(pose, -0.5F, -0.5F, 0.0F).setColor(-1).setUv(0.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
			buffer.addVertex(pose, 0.5F, -0.5F, 0.0F).setColor(-1).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
			buffer.addVertex(pose, 0.5F, 0.5F, 0.0F).setColor(-1).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
			buffer.addVertex(pose, -0.5F, 0.5F, 0.0F).setColor(-1).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 0.0F, -1.0F);
		});
	}
}
