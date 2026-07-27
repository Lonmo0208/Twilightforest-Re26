package twilightforest.mixin;

import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.item.mapdata.TFMagicMapData;

/**
 * Redirects {@link MapItem#getSavedData(MapId, Level)} to also look up TFMagicMapData
 * when the vanilla map data lookup returns null.
 * This is necessary because TFMagicMapData is stored under a custom key
 * (twilightforest:magicmap_&lt;id&gt;) instead of the vanilla key (map_&lt;id&gt;).
 */
@Mixin(MapItem.class)
public class MapItemMixin {

	@Inject(method = "getSavedData(Lnet/minecraft/world/level/saveddata/maps/MapId;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;", at = @At("RETURN"), cancellable = true)
	private static void tf$getSavedData(MapId id, Level level, CallbackInfoReturnable<MapItemSavedData> cir) {
		if (id != null) {
			// Always prefer TFMagicMapData over vanilla MapItemSavedData.
			// The vanilla ClientboundMapItemDataPacket handler stores data under the vanilla key (map_<id>),
			// but TFMagicMapData is stored under a custom key (twilightforest:magicmap_<id>).
			// We must override the vanilla data even when it's not null, because the vanilla data
			// lacks the conqueredStructures and other TF-specific fields needed for rendering.
			TFMagicMapData magicData = TFMagicMapData.getMagicMapData(level, id);
			if (magicData != null) {
				cir.setReturnValue(magicData);
			}
		}
	}
}