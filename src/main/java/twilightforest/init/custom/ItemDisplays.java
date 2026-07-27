package twilightforest.init.custom;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;

import java.util.Optional;
import java.util.function.Supplier;

public class ItemDisplays {

	private static Supplier<?> reflect(String className) {
		return () -> {
			try {
				return Class.forName(className).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Failed to create display: " + className, e);
			}
		};
	}

	public static final ItemDisplayType MAP = new ItemDisplayType(stack -> stack.getItem() instanceof MapItem, reflect("twilightforest.client.overlay.display.MapDisplay"), Optional.of(TwilightForestMod.prefix("textures/item/map_display.png")));
	public static final ItemDisplayType COMPASS = new ItemDisplayType(stack -> stack.is(Items.COMPASS), reflect("twilightforest.client.overlay.display.CompassDisplay"), Optional.of(TwilightForestMod.prefix("textures/item/compass_display.png")));
	public static final ItemDisplayType CLOCK = new ItemDisplayType(stack -> stack.is(Items.CLOCK), reflect("twilightforest.client.overlay.display.ClockDisplay"), Optional.of(TwilightForestMod.prefix("textures/item/clock_display.png")));
	public static final ItemDisplayType MOON_DIAL = new ItemDisplayType(stack -> stack.is(TFItems.MOON_DIAL), reflect("twilightforest.client.overlay.display.MoonDialDisplay"), Optional.of(TwilightForestMod.prefix("textures/item/moon_dial_display.png")));

	public static void init() {
		Registry.register(TFRegistries.ITEM_DISPLAY_TYPE, TwilightForestMod.prefix("map"), MAP);
		Registry.register(TFRegistries.ITEM_DISPLAY_TYPE, TwilightForestMod.prefix("compass"), COMPASS);
		Registry.register(TFRegistries.ITEM_DISPLAY_TYPE, TwilightForestMod.prefix("clock"), CLOCK);
		Registry.register(TFRegistries.ITEM_DISPLAY_TYPE, TwilightForestMod.prefix("moon_dial"), MOON_DIAL);
	}
}