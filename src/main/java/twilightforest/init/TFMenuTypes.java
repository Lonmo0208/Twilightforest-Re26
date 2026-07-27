package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import twilightforest.TwilightForestMod;
import twilightforest.inventory.UncraftingMenu;

public class TFMenuTypes {

	public static final MenuType<UncraftingMenu> UNCRAFTING = new MenuType<>(UncraftingMenu::fromNetwork, FeatureFlags.REGISTRY.allFlags());

	public static void init() {
		Registry.register(BuiltInRegistries.MENU, TwilightForestMod.prefix("uncrafting"), UNCRAFTING);
	}
}
