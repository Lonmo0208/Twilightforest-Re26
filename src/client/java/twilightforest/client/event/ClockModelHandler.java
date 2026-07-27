package twilightforest.client.event;

import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDimension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ClockModelHandler {

	// TODO: Port to Fabric - ModelEvent.ModifyBakingResult is NeoForge-specific; use Fabric model modification API
	/*
    @SuppressWarnings("unchecked")
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        ...
    }
    */
    public static void onModifyBakingResult() {
    }
}
