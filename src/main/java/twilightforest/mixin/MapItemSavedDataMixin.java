package twilightforest.mixin;

import net.minecraft.world.level.saveddata.maps.MapBanner;
import net.minecraft.world.level.saveddata.maps.MapFrame;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MapItemSavedData.class)
public interface MapItemSavedDataMixin {

	@Accessor("frameMarkers")
	Map<String, MapFrame> getFrameMarkers();

	@Accessor("bannerMarkers")
	Map<String, MapBanner> getBannerMarkers();

	@Accessor("trackedDecorationCount")
	int getTrackedDecorationCount();

	@Accessor("trackedDecorationCount")
	void setTrackedDecorationCount(int value);
}