package twilightforest.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin {

	@Shadow
	private Matrix3x2fStack pose;

	@Shadow
	private GuiRenderState guiRenderState;

	@Shadow
	private GuiGraphicsExtractor.ScissorStack scissorStack;

	@Unique
	private static final Field TF_MAP_DATA_FIELD;
	@Unique
	private static final Field TF_IS_TF_MAP_FIELD;

	static {
		try {
			TF_MAP_DATA_FIELD = MapRenderState.class.getField("twilightforest$mapData");
			TF_MAP_DATA_FIELD.setAccessible(true);
			TF_IS_TF_MAP_FIELD = MapRenderState.class.getField("twilightforest$isTFMap");
			TF_IS_TF_MAP_FIELD.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Failed to find TF map fields on MapRenderState", e);
		}
	}

	@Inject(method = "map", at = @At("HEAD"), cancellable = true)
	private void tf$renderMapGui(MapRenderState mapRenderState, CallbackInfo ci) {
		boolean isTFMap;
		MapItemSavedData mapData;
		try {
			isTFMap = TF_IS_TF_MAP_FIELD.getBoolean(mapRenderState);
			mapData = (MapItemSavedData) TF_MAP_DATA_FIELD.get(mapRenderState);
		} catch (IllegalAccessException e) {
			return;
		}

		// Only override for TF maps; for vanilla maps, we still want to show player icons
		if (!isTFMap) {
			// For vanilla maps, let vanilla code handle terrain rendering,
			// but we need to add player icons which are filtered by renderOnFrame
			// We'll do this in a separate injection
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();

		// Render the map background
		if (mapRenderState.texture != null) {
			AbstractTexture texture = minecraft.getTextureManager().getTexture(mapRenderState.texture);
			this.innerBlit(
				RenderPipelines.GUI_TEXTURED,
				texture.getTextureView(),
				texture.getSampler(),
				0, 0, 128, 128,
				0.0F, 1.0F, 0.0F, 1.0F, -1
			);
		}

		// Render decorations - show all including player icons
		for (MapRenderState.MapDecorationRenderState decoration : mapRenderState.decorations) {
			this.tf$renderDecoration(decoration, minecraft);
		}

		ci.cancel();
	}

	@Inject(method = "map", at = @At("TAIL"))
	private void tf$addPlayerIconsToVanillaMap(MapRenderState mapRenderState, CallbackInfo ci) {
		// Check if this is a TF map (already fully rendered)
		boolean isTFMap;
		try {
			isTFMap = TF_IS_TF_MAP_FIELD.getBoolean(mapRenderState);
		} catch (IllegalAccessException e) {
			return;
		}

		if (isTFMap) {
			return; // Already handled above
		}

		// For vanilla maps, add player icons that were filtered by renderOnFrame
		Minecraft minecraft = Minecraft.getInstance();
		for (MapRenderState.MapDecorationRenderState decoration : mapRenderState.decorations) {
			if (!decoration.renderOnFrame) {
				// This is likely a player icon - render it
				this.tf$renderDecoration(decoration, minecraft);
			}
		}
	}

	@Unique
	private void tf$renderDecoration(MapRenderState.MapDecorationRenderState decoration, Minecraft minecraft) {
		boolean isPlayerIcon = decoration.atlasSprite != null
			&& decoration.atlasSprite.contents().name().getPath().contains("player");

		this.pose.pushMatrix();
		this.pose.translate(decoration.x / 2.0F + 64.0F, decoration.y / 2.0F + 64.0F);

		// Player icons get 180° flip (matching MagicMapPlayerIconRenderer)
		float rot = (float) (Math.PI / 180.0F) * decoration.rot * 360.0F / 16.0F;
		if (isPlayerIcon) {
			rot += (float) Math.PI;
		}
		this.pose.rotate(rot);

		this.pose.scale(4.0F, 4.0F);
		this.pose.translate(-0.125F, 0.125F);

		TextureAtlasSprite atlasSprite = decoration.atlasSprite;
		if (atlasSprite != null) {
			AbstractTexture decorationTexture = minecraft.getTextureManager().getTexture(atlasSprite.atlasLocation());
			// Flip V coordinates for player icons (matching MagicMapPlayerIconRenderer's UV flipping)
			// This ensures the player icon appears correctly oriented
			this.innerBlit(
				RenderPipelines.GUI_TEXTURED,
				decorationTexture.getTextureView(),
				decorationTexture.getSampler(),
				-1, -1, 1, 1,
				atlasSprite.getU0(), atlasSprite.getU1(),
				atlasSprite.getV1(), atlasSprite.getV0(),
				-1
			);
		}
		this.pose.popMatrix();

		// Render decoration name
		if (decoration.name != null) {
			Font font = minecraft.font;
			float width = font.width(decoration.name);
			float labelScale = Mth.clamp(25.0F / width, 0.0F, 6.0F / 9.0F);
			this.pose.pushMatrix();
			this.pose.translate(
				decoration.x / 2.0F + 64.0F - width * labelScale / 2.0F,
				decoration.y / 2.0F + 64.0F + 4.0F
			);
			this.pose.scale(labelScale, labelScale);
			this.guiRenderState.addText(
				new GuiTextRenderState(
					font,
					decoration.name.getVisualOrderText(),
					new Matrix3x2f(this.pose),
					0, 0, -1, Integer.MIN_VALUE, false, false,
					this.scissorStack.peek()
				)
			);
			this.pose.popMatrix();
		}
	}

	@Shadow
	private void innerBlit(
		RenderPipeline renderPipeline,
		GpuTextureView textureView,
		GpuSampler textureSampler,
		int x0, int y0, int x1, int y1,
		float u0, float u1, float v0, float v1,
		int color
	) {
	}
}