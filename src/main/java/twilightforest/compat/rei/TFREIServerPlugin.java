package twilightforest.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.forge.REIPluginCommon;
import twilightforest.TwilightForestMod;
import twilightforest.compat.rei.categories.REICrumbleHornCategory;
import twilightforest.compat.rei.categories.REIDryingCategory;
import twilightforest.compat.rei.categories.REIOminousFireCategory;
import twilightforest.compat.rei.categories.REITransformationPowderCategory;
import twilightforest.compat.rei.displays.*;
import twilightforest.compat.rei.fillers.MoonwormQueenRepairFiller;
import twilightforest.compat.rei.fillers.REITravellersGearModifierRecipeFiller;

@REIPluginCommon
public class TFREIServerPlugin implements REICommonPlugin {
	public static final CategoryIdentifier<REIUncraftingDisplay> UNCRAFTING = CategoryIdentifier.of(TwilightForestMod.ID, "uncrafting");

	@Override
	public void registerDisplays(ServerDisplayRegistry registry) {
		new REITravellersGearModifierRecipeFiller().registerDisplays(registry);
		new MoonwormQueenRepairFiller().registerDisplays(registry);
	}

	@Override
	public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
		registry.register(REICrumbleHornCategory.CRUMBLE_HORN.getIdentifier(), REICrumbleHornDisplay.SERIALIZER);
		registry.register(REIDryingCategory.DRYING.getIdentifier(), REIDryingDisplay.SERIALIZER);
		registry.register(REITransformationPowderCategory.TRANSFORMATION.getIdentifier(), REITransformationPowderDisplay.SERIALIZER);
		registry.register(REIOminousFireCategory.OMINOUS_FIRE.getIdentifier(), REIOminousFireDisplay.SERIALIZER);
		registry.register(UNCRAFTING.getIdentifier(), REIUncraftingDisplay.SERIALIZER);
	}
}