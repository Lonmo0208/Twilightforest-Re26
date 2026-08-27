package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class MagicMapPlayerIconRenderer {

	public boolean render(MapRenderState.MapDecorationRenderState decorationRenderState, PoseStack stack, SubmitNodeCollector submitNodeCollector, MapRenderState mapRenderState, TextureAtlas decorationSprites, boolean inItemFrame, int packedLight, int index) {
		stack.pushPose();
		stack.translate(decorationRenderState.x / 2.0F + 64.0F, decorationRenderState.y / 2.0F + 64.0F, -0.02F);
		// 180° flip for player icon (user requested)
		stack.mulPose(new org.joml.Matrix4f().rotation(Axis.ZP.rotationDegrees(decorationRenderState.rot * 360.0F / 16.0F + 180.0F)));
		stack.scale(4.0F, 4.0F, 3.0F);
		stack.translate(-0.125F, 0.125F, 0.0F);
		TextureAtlasSprite textureatlassprite = decorationRenderState.atlasSprite;
		if (textureatlassprite != null) {
			float f2 = textureatlassprite.getU0();
			float f3 = textureatlassprite.getV0();
			float f4 = textureatlassprite.getU1();
			float f5 = textureatlassprite.getV1();
			float z = -0.3F;
			submitNodeCollector.submitCustomGeometry(stack, net.minecraft.client.renderer.rendertype.RenderTypes.text(textureatlassprite.atlasLocation()), (pose, buffer) -> {
				buffer.addVertex(pose, -1.0F, 1.0F, z).setColor(-1).setUv(f2, f5).setLight(packedLight);
				buffer.addVertex(pose, 1.0F, 1.0F, z).setColor(-1).setUv(f4, f5).setLight(packedLight);
				buffer.addVertex(pose, 1.0F, -1.0F, z).setColor(-1).setUv(f4, f3).setLight(packedLight);
				buffer.addVertex(pose, -1.0F, -1.0F, z).setColor(-1).setUv(f2, f3).setLight(packedLight);
			});
		}
		stack.popPose();
		return true;
	}
}