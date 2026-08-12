package twilightforest.client.event;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Nullable;
import twilightforest.beanification.Autowired;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.OreScannerData;
import twilightforest.config.TFConfig;
import twilightforest.entity.passive.QuestRam;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.item.OreMeterItem;
import twilightforest.util.ComponentAlignment;

import java.text.DecimalFormat;
import java.util.*;

//import org.apache.commons.lang3.tuple.Pair;

public class OverlayHandler {
	private static final Identifier QUESTING_RAM_CHECK_SPRITE = TwilightForestMod.prefix("questing_ram_check");
	private static final Identifier QUESTING_RAM_X_SPRITE = TwilightForestMod.prefix("questing_ram_x");
	private static final Identifier FORTIFICATION_SHIELD_SPRITE = TwilightForestMod.prefix("fortification_shield");
	public static final Map<Long, OreMeterInfoCache> ORE_METER_STAT_CACHE = new HashMap<>();

	// TODO: Port to Fabric - @Autowired(dist = Dist.CLIENT) uses NeoForge's Dist
	@Autowired
	private static QuestingRamCurrentContext questingRamCurrentContext;

	// TODO: Port to Fabric - RegisterGuiLayersEvent is NeoForge-specific
	protected static void registerOverlays(Object event) {
		// Fabric equivalent: Use HudRenderCallback or custom overlay registration
		/*
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, TwilightForestMod.prefix("quest_ram_indicator"), (graphics, partialTicks) -> {
			...
		});
		*/
	}

	private static void renderIndicator(Minecraft minecraft, GuiGraphicsExtractor graphics, Gui gui, Player player, int screenWidth, int screenHeight) {
		if (minecraft.options.getCameraType().isFirstPerson() && (minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || gui.hud.canRenderCrosshairForSpectator(minecraft.hitResult)) && minecraft.crosshairPickEntity instanceof QuestRam ram) {
			ItemStack stack = player.getInventory().getItem(player.getInventory().getSelectedSlot());
			if (!stack.isEmpty()) {
				for (var questEntry : questingRamCurrentContext.getContext().questItems().entrySet()) {
					if (questEntry.getValue().test(stack)) {
						int j = ((screenHeight - 1) / 2) - 11;
						int k = ((screenWidth - 1) / 2) - 3;
						if (!ram.isColorPresent(questEntry.getKey())) {
							graphics.blitSprite(RenderPipelines.GUI_TEXTURED, QUESTING_RAM_X_SPRITE, k, j, 7, 7);
						} else {
							graphics.blitSprite(RenderPipelines.GUI_TEXTURED, QUESTING_RAM_CHECK_SPRITE, k, j, 7, 7);
						}
						break;
					}
				}
			}
		}
	}

	private static void renderShieldCount(GuiGraphicsExtractor graphics, Gui gui, int screenWidth, int screenHeight, int shieldCount) {
		int overlayOffset = 0;
		for (int i = 0; i < Math.min(shieldCount, 10); i++) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FORTIFICATION_SHIELD_SPRITE, screenWidth / 2 - 91 + (i * 8), screenHeight - 39 + overlayOffset, 9, 9);
			overlayOffset += 10;
		}
	}

	private static void renderOreMeterStats(GuiGraphicsExtractor graphics, Player player) {
		if (player.isHolding(stack -> stack.is(TFItems.ORE_METER))) {
			InteractionHand handToUse = player.getItemInHand(InteractionHand.MAIN_HAND).is(TFItems.ORE_METER) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
			ItemStack selectedMeter = player.getItemInHand(handToUse);
			if (OreMeterItem.isLoading(selectedMeter)) {
				int dots = (OreMeterItem.getLoadProgress(selectedMeter) / 5) % 3;
				Component component = Component.translatable("misc.twilightforest.ore_meter_loading");
				for (int i = 0; i <= dots; i++) {
					component = component.copy().append(".");
				}
				graphics.fill(0, 0, 56, 16, 0x9b000000);
				graphics.text(Minecraft.getInstance().font, component, 4, 4, 16777215, false);
			} else {
				OreScannerData oreScannerData = selectedMeter.get(TFDataComponents.ORE_DATA);

				if (oreScannerData == null) return;

				long identifier = oreScannerData.universalId();
				if (identifier != 0L && !ORE_METER_STAT_CACHE.containsKey(identifier)) {
					initTooltips(identifier, selectedMeter.getOrDefault(TFDataComponents.ORE_RANGE, 1), oreScannerData);
				}

				if (ORE_METER_STAT_CACHE.containsKey(identifier)) {
					OreMeterInfoCache info = ORE_METER_STAT_CACHE.get(identifier);

					if (info != null) {
						info.renderData(graphics);
					}
				}
			}
		}
	}

	private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	private static void initTooltips(long id, int range, OreScannerData data) {
		ChunkPos pos = data.scannedChunk();
		int totalScanned = data.totalScannedBlocks();

		List<Component> headerRowTexts = ImmutableList.of(
			Component.translatable("misc.twilightforest.ore_meter_range", range, pos.x(), pos.z()),
			Component.translatable("misc.twilightforest.ore_meter_total", totalScanned)
		);

		ArrayList<ComponentColumn> columns = new ArrayList<>();

		List<Map.Entry<String, Integer>> scanData = data.counts().entrySet().stream()
			.sorted(Comparator.comparing(Map.Entry::getValue)) // Sort by quantity
			.toList(); // Make sorted immutable list

		if (TFConfig.prettifyOreMeterGui) {
			ComponentColumn padding = ComponentColumn.padding(1);
			List<Integer> counts = scanData.stream().map(Map.Entry::getValue).toList();

			columns.add(nameColumn(scanData.stream().map(Map.Entry::getKey).toList()));
			columns.add(padding);
			columns.add(dashColumn(scanData.size()));
			columns.add(padding);
			columns.add(countColumn(counts));
			columns.add(padding);
			columns.add(ratioColumn(totalScanned, counts));
		} else {
			columns.add(withoutPrettyPrinting(totalScanned, scanData));
		}

		ORE_METER_STAT_CACHE.put(id, OreMeterInfoCache.build(headerRowTexts, columns));
	}

	private static ComponentColumn withoutPrettyPrinting(int totalScanned, List<Map.Entry<String, Integer>> entries) {
		List<Component> tooltips = new ArrayList<>();

		for (Map.Entry<String, Integer> entry : entries) {
			String percentage = FORMAT.format(entry.getValue() * 100.0F / totalScanned);
			Component formattedEntry = Component.translatable(entry.getKey())
				.append(Component.literal(" "))
				.append(Component.translatable("misc.twilightforest.ore_meter_separator"))
				.append(Component.literal(" " + entry.getValue() + " "))
				.append(Component.translatable("misc.twilightforest.ore_meter_ratio", percentage));

			tooltips.add(formattedEntry);
		}

		return ComponentColumn.build(tooltips, ComponentAlignment.LEFT);
	}

	private static ComponentColumn nameColumn(List<String> oreNameKeys) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();

		toList.add(Component.translatable("misc.twilightforest.ore_meter_header_block").withStyle(ChatFormatting.GRAY));

		for (String oreNameKey : oreNameKeys) {
			MutableComponent translatable = Component.translatable(oreNameKey);
			toList.add(translatable);
		}

		return ComponentColumn.build(toList.build(), ComponentAlignment.LEFT);
	}

	private static ComponentColumn dashColumn(int size) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();

		toList.add(Component.empty());

		MutableComponent dash = Component.translatable("misc.twilightforest.ore_meter_separator");
		for (int i = 0; i < size; i++)
			toList.add(dash);

		return ComponentColumn.build(toList.build(), ComponentAlignment.CENTER);
	}

	private static ComponentColumn countColumn(List<Integer> oreCounts) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();

		toList.add(Component.translatable("misc.twilightforest.ore_meter_header_count").withStyle(ChatFormatting.GRAY));

		oreCounts.stream().mapToInt(count -> count).mapToObj(count -> Component.literal(String.valueOf(count))).forEach(toList::add);

		return ComponentColumn.build(toList.build(), ComponentAlignment.RIGHT);
	}

	private static ComponentColumn ratioColumn(int totalScanned, List<Integer> oreCounts) {
		ImmutableList.Builder<Component> toList = ImmutableList.builder();

		toList.add(Component.translatable("misc.twilightforest.ore_meter_header_ratio").withStyle(ChatFormatting.GRAY));

		for (int count : oreCounts) {
			var percentage = FORMAT.format(count * 100.0F / totalScanned);
			toList.add(Component.translatable("misc.twilightforest.ore_meter_ratio", percentage));
		}

		return ComponentColumn.build(toList.build(), ComponentAlignment.RIGHT);
	}

	public record ComponentColumn(List<? extends Component> textRows, int maxPixelWidth,
								  ComponentAlignment textAlignment) {
		public static ComponentColumn build(List<? extends Component> rowTexts, ComponentAlignment textAlignment) {
			int maxColumnPixelWidth = rowTexts.stream().mapToInt(c -> Minecraft.getInstance().font.width(c)).max().orElse(0);
			return new ComponentColumn(rowTexts, maxColumnPixelWidth, textAlignment);
		}

		public static ComponentColumn padding(int forcedExtraMaxWidthBySpaces) {
			return new ComponentColumn(List.of(), forcedExtraMaxWidthBySpaces * Minecraft.getInstance().font.width(" "), ComponentAlignment.LEFT);
		}

		private int renderColumn(GuiGraphicsExtractor graphics, ComponentColumn column, int xOff, int yOff, int verticalTextPixelsAdvance) {
			for (Component rowText : column.textRows) {
				int textPixelWidth = Minecraft.getInstance().font.width(rowText);
				int textXPos = xOff + this.textAlignment.getTextOffset(textPixelWidth, this.maxPixelWidth);
				graphics.text(Minecraft.getInstance().font, rowText, textXPos, yOff, 0x00_ff_ff_ff, false);
				yOff += verticalTextPixelsAdvance;
			}

			return column.maxPixelWidth;
		}
	}

	public record OreMeterInfoCache(int totalPixelWidth, int totalRowCount, List<Component> headerRows, List<ComponentColumn> textColumns) {
		public static OreMeterInfoCache build(List<Component> headers, List<ComponentColumn> columns) {
			// All these widths are measured in pixels, used for dealing with the font
			int summedColumnMaxWidths = columns.stream().mapToInt(ComponentColumn::maxPixelWidth).sum();
			int maxHeaderWidth = headers.stream().mapToInt(c -> Minecraft.getInstance().font.width(c)).max().orElse(0);

			int maxPixelWidth = Math.max(summedColumnMaxWidths, maxHeaderWidth);

			// Not measured in pixels, instead goes by element count - How many total rows of text will be shown in the GUI
			int totalRowCount = headers.size() + columns.stream().mapToInt(column -> column.textRows.size()).max().orElse(0);

			return new OreMeterInfoCache(maxPixelWidth, totalRowCount, ImmutableList.copyOf(headers), ImmutableList.copyOf(columns));
		}

		public void renderData(GuiGraphicsExtractor graphics) {
			int verticalTextPixelsAdvance = Minecraft.getInstance().font.lineHeight + 1;

			graphics.fill(0, 0, this.totalPixelWidth + 8, this.totalRowCount * verticalTextPixelsAdvance + 6, 0x9b_00_00_00);

			int xOff = 4;
			int yOff = 4;

			for (Component headerRowText : this.headerRows) {
				graphics.text(Minecraft.getInstance().font, headerRowText, xOff, yOff, 0x00_ff_ff_ff, false);
				yOff += verticalTextPixelsAdvance;
			}

			for (ComponentColumn column : this.textColumns) {
				xOff += column.renderColumn(graphics, column, xOff, yOff, verticalTextPixelsAdvance);
			}
		}
	}

	@Nullable
	private static Player getCameraPlayer() {
		return Minecraft.getInstance().getCameraEntity() instanceof Player player ? player : null;
	}
}
