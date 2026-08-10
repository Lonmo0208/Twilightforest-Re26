package twilightforest.client.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

public class ShieldOverlay implements HudElement {

	private static final Identifier FORTIFICATION_SHIELD_SPRITE = TwilightForestMod.prefix("fortification_shield");

	@Override
	public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		if (player == null) {
			return;
		}

		boolean showInCreative = TFConfig.showFortificationShieldIndicatorInCreative;
		boolean canHurt = minecraft.gameMode != null && minecraft.gameMode.canHurtPlayer();
		if (!canHurt && !showInCreative) {
			return;
		}

		if (!TFConfig.showFortificationShieldIndicator) {
			return;
		}

		FortificationShieldAttachment attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.FORTIFICATION_SHIELDS, twilightforest.components.entity.FortificationShieldAttachment::new);
		if (attachment == null) return;
		int shieldCount = attachment.shieldsLeft();
		if (shieldCount <= 0) return;

		int y = graphics.guiHeight() - 49 - 10;
		if (player.getArmorValue() <= 0) {
			y += 10;
		}
		for (int i = 0; i < Math.min(shieldCount, 10); i++) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FORTIFICATION_SHIELD_SPRITE, graphics.guiWidth() / 2 - 91 + (i * 8), y, 9, 9);
		}
	}
}