package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

// TODO: Port to Fabric - Previously implemented IMapDecorationRenderer (NeoForge-specific); re-implement using Fabric map API
public class MagicMapPlayerIconRenderer {

	//[VanillaCopy] of MapRenderer.RenderInstance.draw, but with a set depth offset instead of relying on index.
	//this allows the icon to render on top of everything else instead of sometimes on top, sometimes behind
	//Only TF magic maps add this decoration type, so no map type check is needed.
	public boolean render(MapRenderState.MapDecorationRenderState decorationRenderState, PoseStack stack, SubmitNodeCollector submitNodeCollector, MapRenderState mapRenderState, TextureAtlas decorationSprites, boolean inItemFrame, int packedLight, int index) {
		stack.pushPose();
		stack.translate(0.0F + (float) decorationRenderState.x / 2.0F + 64.0F, 0.0F + (float) decorationRenderState.y / 2.0F + 64.0F, -0.02F);
		// The player icon sprite is stored with the arrow pointing down in the atlas (vanilla convention).
		// Our custom map renderer uses flipped V coordinates for TF structure icons (TF textures are authored
		// V-flipped), but the player icon uses the vanilla "minecraft:player" sprite, so the flipped V
		// orientation turns the icon 180 degrees off. Rotate the icon by 180 degrees to compensate.
		stack.mulPose(Axis.ZP.rotationDegrees((float) ((decorationRenderState.rot & 15) * 360) / 16.0F + 180.0F));
		stack.scale(4.0F, 4.0F, 3.0F);
		stack.translate(-0.125F, 0.125F, 0.0F);
		TextureAtlasSprite textureatlassprite = decorationRenderState.atlasSprite;
		float f2 = textureatlassprite.getU0();
		float f3 = textureatlassprite.getV0();
		float f4 = textureatlassprite.getU1();
		float f5 = textureatlassprite.getV1();
		submitNodeCollector.submitCustomGeometry(stack, net.minecraft.client.renderer.rendertype.RenderTypes.text(textureatlassprite.atlasLocation()), (pose, buffer) -> {
			buffer.addVertex(pose, -1.0F, 1.0F, -0.3F).setColor(-1).setUv(f2, f5).setLight(packedLight);
			buffer.addVertex(pose, 1.0F, 1.0F, -0.3F).setColor(-1).setUv(f4, f5).setLight(packedLight);
			buffer.addVertex(pose, 1.0F, -1.0F, -0.3F).setColor(-1).setUv(f4, f3).setLight(packedLight);
			buffer.addVertex(pose, -1.0F, -1.0F, -0.3F).setColor(-1).setUv(f2, f3).setLight(packedLight);
		});
		stack.popPose();
		return true;
	}
}
