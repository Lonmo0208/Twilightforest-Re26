package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import twilightforest.item.MagicMapItem;
import twilightforest.item.mapdata.TFMagicMapData;

public class MapDisplay implements ItemDisplay {

	private static final Identifier MAP_BACKGROUND_CHECKERBOARD = Identifier.withDefaultNamespace("textures/map/map_background_checkerboard.png");
	private static final int MAP_SIZE = 100;
	private static final int BG_SIZE = 110;
	private static final int PAD = (BG_SIZE - MAP_SIZE) / 2;
	private static final int BOTTOM_PADDING = 6;
	private static final int MAP_TEXTURE_SIZE = 128;

	@Override
	public void render(ItemStack item, GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		MapId mapid = item.get(DataComponents.MAP_ID);
		if (mapid == null) return;

		// Use MagicMapItem.getData for magic maps, MapItem.getSavedData for vanilla maps
		MapItemSavedData data;
		if (item.getItem() instanceof MagicMapItem) {
			TFMagicMapData magicData = MagicMapItem.getData(item, minecraft.level);
			if (magicData == null) return;
			data = magicData;
		} else {
			data = MapItem.getSavedData(item, minecraft.level);
			if (data == null) return;
		}

		int startX = Math.max(widestWidgetWidth / 2 - BG_SIZE / 2, 0);
		int startY = 0;

		graphics.blit(MAP_BACKGROUND_CHECKERBOARD, startX, startY, startX + BG_SIZE, startY + BG_SIZE, 0.0F, 1.0F, 0.0F, 1.0F);

		minecraft.getMapTextureManager().update(mapid, data);
		MapRenderState mapRenderState = new MapRenderState();
		minecraft.getMapRenderer().extractRenderState(mapid, data, mapRenderState);

		float mapScale = (float) MAP_SIZE / MAP_TEXTURE_SIZE;

		graphics.pose().pushMatrix();
		graphics.pose().translate(startX + PAD, startY + PAD);
		graphics.pose().scale(mapScale, mapScale);

		if (mapRenderState.texture != null) {
			AbstractTexture texture = minecraft.getTextureManager().getTexture(mapRenderState.texture);
			graphics.blit(
				texture.getTextureView(),
				texture.getSampler(),
				0, 0, MAP_TEXTURE_SIZE, MAP_TEXTURE_SIZE,
				0.0F, 1.0F, 0.0F, 1.0F
			);
		}

		for (MapRenderState.MapDecorationRenderState decoration : mapRenderState.decorations) {
			this.renderDecoration(decoration, graphics, minecraft);
		}

		graphics.pose().popMatrix();
	}

	private void renderDecoration(MapRenderState.MapDecorationRenderState decoration, GuiGraphicsExtractor graphics, Minecraft minecraft) {
		TextureAtlasSprite atlasSprite = decoration.atlasSprite;
		if (atlasSprite == null) return;

		boolean isPlayerIcon = atlasSprite.contents().name().getPath().contains("player");

		graphics.pose().pushMatrix();
		graphics.pose().translate(decoration.x / 2.0F + 64.0F, decoration.y / 2.0F + 64.0F);

		float rot = (float) (Math.PI / 180.0F) * decoration.rot * 360.0F / 16.0F;
		if (!isPlayerIcon) {
			rot += (float) Math.PI;
		}
		graphics.pose().rotate(rot);

		graphics.pose().scale(4.0F, 4.0F);
		graphics.pose().translate(-0.125F, 0.125F);

		AbstractTexture decorationTexture = minecraft.getTextureManager().getTexture(atlasSprite.atlasLocation());
		graphics.blit(
			decorationTexture.getTextureView(),
			decorationTexture.getSampler(),
			-1, -1, 1, 1,
			atlasSprite.getU0(), atlasSprite.getU1(),
			atlasSprite.getV1(), atlasSprite.getV0()
		);

		graphics.pose().popMatrix();
	}

	@Override
	public DisplayPosition displayPosition() {
		return DisplayPosition.TOP;
	}

	@Override
	public Bounds getWidgetSize(ItemStack item, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		return new Bounds(Math.max(widestWidgetWidth / 2 - BG_SIZE / 2, 0), 0, BG_SIZE, BG_SIZE + BOTTOM_PADDING);
	}
}
