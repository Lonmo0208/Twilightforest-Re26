package twilightforest.client.overlay;

import com.mojang.blaze3d.platform.Window;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;
import twilightforest.components.entity.TFPortalAttachment;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;

public class PortalOverlay implements HudElement {

	@Override
	public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
		Minecraft minecraft = Minecraft.getInstance();
		render(graphics, minecraft, minecraft.player);
	}

	public static void render(GuiGraphicsExtractor graphics, Minecraft minecraft, Player player) {
		Window window = minecraft.getWindow();
		if (player != null) {
			TFPortalAttachment portal = TFDataAttachments.getOrCreate(player, TFDataAttachments.TF_PORTAL_COOLDOWN, twilightforest.components.entity.TFPortalAttachment::new);
			if (portal != null && portal.getPortalTimer() > 0) {
				float alpha = (float) portal.getPortalTimer() / (float) TFPortalAttachment.MAX_TICKS;
				var model = minecraft.getModelManager().getBlockStateModelSet().get(TFBlocks.TWILIGHT_PORTAL.defaultBlockState());
				var particleMaterial = model.particleMaterial();
				int color = ARGB.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F);
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, particleMaterial.sprite(), 0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), color);
			}
		}
	}
}