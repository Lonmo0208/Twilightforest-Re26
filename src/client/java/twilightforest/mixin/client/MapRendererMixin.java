package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.renderer.map.ConqueredMapIconRenderer;
import twilightforest.client.renderer.map.MagicMapPlayerIconRenderer;
import twilightforest.item.mapdata.TFMagicMapData;

@Mixin(MapRenderer.class)
public abstract class MapRendererMixin {

	@Shadow
	@Final
	private TextureAtlas decorationSprites;

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

	@Inject(method = "extractRenderState", at = @At("RETURN"))
	private void tf$storeMapData(MapId mapId, MapItemSavedData mapData, MapRenderState mapRenderState, CallbackInfo ci) {
		try {
			TF_MAP_DATA_FIELD.set(mapRenderState, mapData);
			TF_IS_TF_MAP_FIELD.set(mapRenderState, mapData instanceof TFMagicMapData);
		} catch (IllegalAccessException ignored) {
		}
	}

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void tf$renderMagicMap(MapRenderState mapRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, boolean showOnlyFrame, int lightCoords, CallbackInfo ci) {
		boolean isTFMap;
		MapItemSavedData mapData;
		try {
			isTFMap = TF_IS_TF_MAP_FIELD.getBoolean(mapRenderState);
			mapData = (MapItemSavedData) TF_MAP_DATA_FIELD.get(mapRenderState);
		} catch (IllegalAccessException e) {
			return;
		}

		if (!isTFMap || mapData == null) {
			return;
		}

		TFMagicMapData magicMapData = (TFMagicMapData) mapData;

		// Render the map texture
		if (mapRenderState.texture != null) {
			submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(mapRenderState.texture), (pose, buffer) -> {
				buffer.addVertex(pose, 0.0F, 128.0F, -0.01F).setColor(-1).setUv(0.0F, 1.0F).setLight(lightCoords);
				buffer.addVertex(pose, 128.0F, 128.0F, -0.01F).setColor(-1).setUv(1.0F, 1.0F).setLight(lightCoords);
				buffer.addVertex(pose, 128.0F, 0.0F, -0.01F).setColor(-1).setUv(1.0F, 0.0F).setLight(lightCoords);
				buffer.addVertex(pose, 0.0F, 0.0F, -0.01F).setColor(-1).setUv(0.0F, 0.0F).setLight(lightCoords);
			});
		}

		// Render decorations
		MagicMapPlayerIconRenderer playerIconRenderer = new MagicMapPlayerIconRenderer();
		ConqueredMapIconRenderer conqueredRenderer = new ConqueredMapIconRenderer();

		int count = 0;
		for (MapRenderState.MapDecorationRenderState decoration : mapRenderState.decorations) {
			if (!showOnlyFrame || decoration.renderOnFrame) {
				// Check if this is a player icon (handled by MagicMapPlayerIconRenderer)
				boolean isPlayerIcon = decoration.atlasSprite != null
					&& decoration.atlasSprite.contents().name().getPath().contains("player");

				if (isPlayerIcon) {
					playerIconRenderer.render(decoration, poseStack, submitNodeCollector, mapRenderState, this.decorationSprites, showOnlyFrame, lightCoords, count);
				} else {
					// Render vanilla-style decoration
					this.tf$renderVanillaDecoration(decoration, poseStack, submitNodeCollector, lightCoords, count);
				}

				// Apply conquered overlay for TF structures
				conqueredRenderer.render(decoration, poseStack, submitNodeCollector, mapRenderState, this.decorationSprites, showOnlyFrame, lightCoords, count);

				// Render decoration name
				this.tf$renderDecorationName(decoration, poseStack, submitNodeCollector, lightCoords);

				count++;
			}
		}

		ci.cancel();
	}

	@Unique
	private void tf$renderVanillaDecoration(MapRenderState.MapDecorationRenderState decoration, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int index) {
		poseStack.pushPose();
		poseStack.translate(decoration.x / 2.0F + 64.0F, decoration.y / 2.0F + 64.0F, -0.02F);
		poseStack.mulPose(new org.joml.Matrix4f().rotation(Axis.ZP.rotationDegrees(decoration.rot * 360.0F / 16.0F)));
		poseStack.scale(4.0F, 4.0F, 3.0F);
		poseStack.translate(-0.125F, 0.125F, 0.0F);
		TextureAtlasSprite atlasSprite = decoration.atlasSprite;
		if (atlasSprite != null) {
			float z = index * -0.001F;
			submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(atlasSprite.atlasLocation()), (pose, buffer) -> {
				buffer.addVertex(pose, -1.0F, 1.0F, z).setColor(-1).setUv(atlasSprite.getU0(), atlasSprite.getV1()).setLight(lightCoords);
				buffer.addVertex(pose, 1.0F, 1.0F, z).setColor(-1).setUv(atlasSprite.getU1(), atlasSprite.getV1()).setLight(lightCoords);
				buffer.addVertex(pose, 1.0F, -1.0F, z).setColor(-1).setUv(atlasSprite.getU1(), atlasSprite.getV0()).setLight(lightCoords);
				buffer.addVertex(pose, -1.0F, -1.0F, z).setColor(-1).setUv(atlasSprite.getU0(), atlasSprite.getV0()).setLight(lightCoords);
			});
		}
		poseStack.popPose();
	}

	@Unique
	private void tf$renderDecorationName(MapRenderState.MapDecorationRenderState decoration, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
		if (decoration.name == null) {
			return;
		}

		Font font = Minecraft.getInstance().font;
		float width = font.width(decoration.name);
		float scale = Mth.clamp(25.0F / width, 0.0F, 6.0F / 9.0F);
		poseStack.pushPose();
		poseStack.translate(decoration.x / 2.0F + 64.0F - width * scale / 2.0F, decoration.y / 2.0F + 64.0F + 4.0F, -0.025F);
		poseStack.scale(scale, scale, -1.0F);
		poseStack.translate(0.0F, 0.0F, 0.1F);
		submitNodeCollector.order(1)
			.submitText(
				poseStack, 0.0F, 0.0F, decoration.name.getVisualOrderText(), false, Font.DisplayMode.NORMAL, lightCoords, -1, Integer.MIN_VALUE, 0
			);
		poseStack.popPose();
	}
}