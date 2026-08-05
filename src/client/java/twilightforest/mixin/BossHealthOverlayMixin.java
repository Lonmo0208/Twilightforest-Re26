package twilightforest.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.bar.ClientTFBossBar;

import java.util.Map;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayMixin {

	@Shadow @Final private Minecraft minecraft;
	@Shadow @Final private Map<UUID, LerpingBossEvent> events;

	@Unique
	private static final int TF_BAR_WIDTH = 182;
	@Unique
	private static final int TF_BAR_HEIGHT = 5;
	@Unique
	private static final int TF_BAR_SPACING = 19; // 10 + 9 matching vanilla spacing

	// Mirror of vanilla private sprite arrays — exact copy of BossHealthOverlay statics so
	// we don't need to @Shadow private static final fields across different name mappings.
	@Unique
	private static final Identifier[] TF_BAR_BACKGROUNDS = new Identifier[]{
		Identifier.withDefaultNamespace("boss_bar/pink_background"),
		Identifier.withDefaultNamespace("boss_bar/blue_background"),
		Identifier.withDefaultNamespace("boss_bar/red_background"),
		Identifier.withDefaultNamespace("boss_bar/green_background"),
		Identifier.withDefaultNamespace("boss_bar/yellow_background"),
		Identifier.withDefaultNamespace("boss_bar/purple_background"),
		Identifier.withDefaultNamespace("boss_bar/white_background")
	};
	@Unique
	private static final Identifier[] TF_BAR_PROGRESS = new Identifier[]{
		Identifier.withDefaultNamespace("boss_bar/pink_progress"),
		Identifier.withDefaultNamespace("boss_bar/blue_progress"),
		Identifier.withDefaultNamespace("boss_bar/red_progress"),
		Identifier.withDefaultNamespace("boss_bar/green_progress"),
		Identifier.withDefaultNamespace("boss_bar/yellow_progress"),
		Identifier.withDefaultNamespace("boss_bar/purple_progress"),
		Identifier.withDefaultNamespace("boss_bar/white_progress")
	};
	@Unique
	private static final Identifier[] TF_OVERLAY_BACKGROUNDS = new Identifier[]{
		Identifier.withDefaultNamespace("boss_bar/notched_6_background"),
		Identifier.withDefaultNamespace("boss_bar/notched_10_background"),
		Identifier.withDefaultNamespace("boss_bar/notched_12_background"),
		Identifier.withDefaultNamespace("boss_bar/notched_20_background")
	};
	@Unique
	private static final Identifier[] TF_OVERLAY_PROGRESS = new Identifier[]{
		Identifier.withDefaultNamespace("boss_bar/notched_6_progress"),
		Identifier.withDefaultNamespace("boss_bar/notched_10_progress"),
		Identifier.withDefaultNamespace("boss_bar/notched_12_progress"),
		Identifier.withDefaultNamespace("boss_bar/notched_20_progress")
	};

	// Replace the vanilla render loop so Twilight Forest custom boss bars (ClientTFBossBar)
	// use their own int-color tinted render path (renderBossBar) instead of vanilla's
	// static BossBarColor enum sprites. This is the Fabric equivalent of NeoForge's
	// CustomizeGuiOverlayEvent.BossEventProgress.
	@Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
	private void twilightforest$renderCustomBossBars(GuiGraphicsExtractor graphics, CallbackInfo ci) {
		if (this.events.isEmpty()) return;

		graphics.nextStratum();
		ProfilerFiller profiler = Profiler.get();
		profiler.push("bossHealth");
		int screenWidth = graphics.guiWidth();
		int yOffset = 12;

		for (LerpingBossEvent event : this.events.values()) {
			int xLeft = screenWidth / 2 - TF_BAR_WIDTH / 2;

			if (event instanceof ClientTFBossBar tfBossBar) {
				// Full custom TF rendering — background tinted by ClientTFBossBar.color,
				// overlays, progress tinted by same color, and title all drawn here.
				tfBossBar.renderBossBar(graphics, xLeft, yOffset);
			} else {
				// Vanilla path for non-TF bosses (dragon, wither, modded vanilla-style bars)
				// exactly mirrors BossHealthOverlay.extractBar + title drawing.
				this.twilightforest$drawVanillaBar(graphics, xLeft, yOffset, event);
			}

			yOffset += TF_BAR_SPACING;
			if (yOffset >= graphics.guiHeight() / 3) break;
		}

		profiler.pop();
		ci.cancel();
	}

	// Exact inlined copy of vanilla extractRenderState rendering for a single bar.
	// Matches BossHealthOverlay lines 69-77 + extractBar helpers line for line.
	@Unique
	private void twilightforest$drawVanillaBar(GuiGraphicsExtractor graphics, int x, int y, BossEvent event) {
		// background layer + overlay background
		this.twilightforest$blitBarLayer(graphics, x, y, TF_BAR_WIDTH,
			TF_BAR_BACKGROUNDS[event.getColor().ordinal()],
			TF_OVERLAY_BACKGROUNDS, event);

		// progress layer + overlay progress (only if progress > 0)
		int progress = Mth.lerpDiscrete(event.getProgress(), 0, TF_BAR_WIDTH);
		if (progress > 0) {
			this.twilightforest$blitBarLayer(graphics, x, y, progress,
				TF_BAR_PROGRESS[event.getColor().ordinal()],
				TF_OVERLAY_PROGRESS, event);
		}

		// title
		Component title = event.getName();
		int titleWidth = this.minecraft.font.width(title);
		int titleX = graphics.guiWidth() / 2 - titleWidth / 2;
		int titleY = y - 9;
		graphics.text(this.minecraft.font, title, titleX, titleY, -1);
	}

	@Unique
	private void twilightforest$blitBarLayer(GuiGraphicsExtractor graphics, int x, int y, int width,
											 Identifier barSprite, Identifier[] overlaySprites, BossEvent event) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, barSprite,
			TF_BAR_WIDTH, TF_BAR_HEIGHT, 0, 0, x, y, width, TF_BAR_HEIGHT);
		if (event.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED,
				overlaySprites[event.getOverlay().ordinal() - 1],
				TF_BAR_WIDTH, TF_BAR_HEIGHT, 0, 0, x, y, width, TF_BAR_HEIGHT);
		}
	}
}
