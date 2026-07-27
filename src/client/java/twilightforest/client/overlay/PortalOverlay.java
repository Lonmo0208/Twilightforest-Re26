package twilightforest.client.overlay;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import twilightforest.components.entity.TFPortalAttachment;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;

public class PortalOverlay {

	public static void render(GuiGraphicsExtractor graphics, Minecraft minecraft, Player player) {
		Window window = minecraft.getWindow();
		if (player != null) {
			TFPortalAttachment portal = player.getAttached(TFDataAttachments.TF_PORTAL_COOLDOWN);
			if (portal.getPortalTimer() > 0) {
				float alpha = (float) portal.getPortalTimer() / (float) TFPortalAttachment.MAX_TICKS;
				var model = minecraft.getModelManager().getBlockStateModelSet().get(TFBlocks.TWILIGHT_PORTAL.defaultBlockState());
				var particleMaterial = model.particleMaterial();
				int color = ARGB.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F);
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, particleMaterial.sprite(), 0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), color);
			}
		}
	}
}