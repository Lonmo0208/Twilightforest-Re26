package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ClockDisplay implements ItemDisplay {
	private static final long INITIAL_DAY_OFFSET_IN_TICKS = 6000L;
	private static final long MINECRAFT_DAY_LENGTH_IN_TICKS = 24000L;
	private static final long REAL_LIFE_DAY_LENGTH_IN_SECONDS = 86400L;
	private static final DateTimeFormatter FORMAT_24 = DateTimeFormatter.ofPattern("HH:mm");
	private static final DateTimeFormatter FORMAT_12 = DateTimeFormatter.ofPattern("hh:mm a");
	private static final int FACE_SCALE = 2;
	private static final int FACE_SIZE = 8 * FACE_SCALE;
	private static final int PAD = 4;
	private static final NavigableMap<Long, TimeFrame> TIME_FRAMES = new TreeMap<>(Map.ofEntries(
		Map.entry(0L, TimeFrame.SUNRISE),
		Map.entry(501L, TimeFrame.DAY),
		Map.entry(12500L, TimeFrame.SUNSET),
		Map.entry(14000L, TimeFrame.NIGHT),
		Map.entry(22000L, TimeFrame.SUNRISE)
	));

	private static long getMinecraftClockTimeInTicks(long ticks) {
		return (ticks + INITIAL_DAY_OFFSET_IN_TICKS) % MINECRAFT_DAY_LENGTH_IN_TICKS;
	}

	private static Component getGameTime(Level level, boolean use24HourFormat) {
		if (!level.dimensionType().hasSkyLight())
			return Component.translatable("travellers_gear.modifier.twilightforest.item_display.clock.unknown");

		long rawTime = level.getDefaultClockTime();
		long ticksOfDay = getMinecraftClockTimeInTicks(rawTime);
		long secondsOfDay = (ticksOfDay * REAL_LIFE_DAY_LENGTH_IN_SECONDS) / MINECRAFT_DAY_LENGTH_IN_TICKS;
		LocalTime localTime = LocalTime.ofSecondOfDay(secondsOfDay);
		return Component.literal(localTime.format(use24HourFormat ? FORMAT_24 : FORMAT_12));
	}

	@Override
	public void render(ItemStack item, GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		Component textComponent = this.getText(minecraft);
		int textWidth = minecraft.font.width(textComponent);
		boolean showClockFace = minecraft.level.dimensionType().hasSkyLight();
		int contentWidth = textWidth + (showClockFace ? FACE_SIZE + PAD : 0);
		int contentHeight = Math.max(minecraft.font.lineHeight, showClockFace ? FACE_SIZE : 0);
		int widgetX = Math.max(0, (widestWidgetWidth / 2) - (contentWidth / 2));
		int widgetY = 0;

		int faceX = widgetX;
		int textX = showClockFace ? faceX + FACE_SIZE + PAD : widgetX;
		int textY = widgetY + (contentHeight - minecraft.font.lineHeight) / 2;

		if (showClockFace) {
			int k = this.getFrameForTime(minecraft.level.getDefaultClockTime()).frame;
			int xRow = k % 2;
			int yRow = k / 2 % 2;
			float u0 = xRow * 8 / 16.0F;
			float u1 = (xRow * 8 + 8) / 16.0F;
			float v0 = yRow * 8 / 16.0F;
			float v1 = (yRow * 8 + 8) / 16.0F;
			int faceY = widgetY + (contentHeight - FACE_SIZE) / 2;
			graphics.blit(
				TwilightForestMod.getGuiTexture("time.png"),
				faceX, faceY, faceX + FACE_SIZE, faceY + FACE_SIZE,
				u0, u1, v0, v1
			);
		}
		graphics.text(minecraft.font, textComponent, textX, textY, 0xFFFFFFFF);
	}

	private TimeFrame getFrameForTime(long dayTimeInTicks) {
		long time = dayTimeInTicks % MINECRAFT_DAY_LENGTH_IN_TICKS;
		return TIME_FRAMES.floorEntry(time).getValue();
	}

	@Override
	public Bounds getWidgetSize(ItemStack item, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		int textWidth = minecraft.font.width(this.getText(minecraft));
		boolean showClockFace = minecraft.level.dimensionType().hasSkyLight();
		int contentWidth = textWidth + (showClockFace ? FACE_SIZE + PAD : 0);
		int contentHeight = Math.max(minecraft.font.lineHeight, showClockFace ? FACE_SIZE : 0);
		int startX = Math.max(0, (widestWidgetWidth / 2) - (contentWidth / 2));
		return new Bounds(startX, 0, contentWidth, contentHeight);
	}

	private Component getText(Minecraft minecraft) {
		return getGameTime(minecraft.level, TFConfig.clock24HourFormat);
	}

	private enum TimeFrame {
		SUNRISE(0),
		DAY(1),
		SUNSET(2),
		NIGHT(3);

		final int frame;

		TimeFrame(int frame) {
			this.frame = frame;
		}
	}
}
