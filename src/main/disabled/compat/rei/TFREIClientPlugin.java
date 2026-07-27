package twilightforest.compat.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.filtering.FilteringRuleTypeRegistry;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.entry.renderer.EntryRendererRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.client.UncraftingScreen;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.categories.*;
import twilightforest.compat.rei.displays.REICrumbleHornDisplay;
import twilightforest.compat.rei.displays.REIOminousFireDisplay;
import twilightforest.compat.rei.displays.REITransformationPowderDisplay;
import twilightforest.compat.rei.entries.BlockStateEntryDefinition;
import twilightforest.compat.rei.entries.EntityEntryDefinition;
import twilightforest.compat.rei.filter.HideItemFilterType;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.tags.TFItemTags;

import java.util.Map;
import java.util.WeakHashMap;

@SuppressWarnings("UnstableApiUsage")
@REIPluginClient
public class TFREIClientPlugin implements REIClientPlugin {

	public static final EntityEntryDefinition ENTITY_DEFINITION = new EntityEntryDefinition();
	public static final BlockStateEntryDefinition BLOCKSTATE_DEFINITION = new BlockStateEntryDefinition();
	public Map<EntryStack<Entity>, EntryRenderer<Entity>> RENDER_CACHE = new WeakHashMap<>();

	static {
		FilteringRuleTypeRegistry.getInstance().register(TwilightForestMod.prefix("filter"), HideItemFilterType.INSTANCE);
	}

	@Override
	public void registerCategories(CategoryRegistry registry) {
		if (!TFConfig.disableEntireTable) {
			registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(TFBlocks.UNCRAFTING_TABLE));
			registry.addWorkstations(TFREIServerPlugin.UNCRAFTING, EntryStacks.of(TFBlocks.UNCRAFTING_TABLE));
		}
		registry.addWorkstations(REICrumbleHornCategory.CRUMBLE_HORN, EntryStacks.of(TFItems.CRUMBLE_HORN));
		registry.addWorkstations(REITransformationPowderCategory.TRANSFORMATION, EntryStacks.of(TFItems.TRANSFORMATION_POWDER));
		registry.addWorkstations(REIOminousFireCategory.OMINOUS_FIRE, EntryStacks.of(TFItems.EXANIMATE_ESSENCE));
		registry.addWorkstations(REIDryingCategory.DRYING, EntryIngredients.ofItemTag(TFItemTags.DRYING_RACKS));

		if (!TFConfig.disableEntireTable) {
			registry.add(new REIUncraftingCategory());
		}
		registry.add(new REICrumbleHornCategory());
		registry.add(new REITransformationPowderCategory());
		registry.add(new REIOminousFireCategory());
		registry.add(new REIDryingCategory());
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		RecipeViewerConstants.getCrumbleHornRecipes().forEach(info -> registry.add(REICrumbleHornDisplay.of(info.getFirst(), info.getSecond())));
		RecipeViewerConstants.getTransformationPowderRecipes().forEach(info -> registry.add(REITransformationPowderDisplay.of(info)));
		RecipeViewerConstants.getOminousFireRecipes().forEach(info -> registry.add(REIOminousFireDisplay.of(info)));
	}

	@Override
	public void registerScreens(ScreenRegistry registry) {
		if (!TFConfig.disableEntireTable) {
			registry.registerClickArea(screen -> new Rectangle(34, 33, 27, 20), UncraftingScreen.class, TFREIServerPlugin.UNCRAFTING);
			registry.registerClickArea(screen -> new Rectangle(115, 33, 27, 20), UncraftingScreen.class, BuiltinPlugin.CRAFTING);
		}
	}

	@Override
	public void registerEntryRenderers(EntryRendererRegistry registry) {
		RENDER_CACHE.clear();

		registry.register(EntityEntryDefinition.ENTITY_TYPE, (entry, last) -> {
			if (entry.getValue() instanceof ItemEntity) {
				return RENDER_CACHE.computeIfAbsent(entry, stack -> new EntityEntryDefinition.ItemEntityRenderer());
			}

			return last;
		});
	}

	public static Entity createItemEntity(ItemStack stack) {
		ItemEntity entity = new ItemEntity(EntityType.ITEM, Minecraft.getInstance().level);
		entity.setItem(stack);
		return entity;
	}
}
