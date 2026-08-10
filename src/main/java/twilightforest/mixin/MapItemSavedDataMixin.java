package twilightforest.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapBanner;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapFrame;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
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

	@Accessor("decorations")
	Map<String, MapDecoration> getDecorationsField();

	@Accessor("centerX")
	int getCenterX();

	@Accessor("centerZ")
	int getCenterZ();

	@Accessor("scale")
	byte getScale();

	@Accessor("dimension")
	ResourceKey<Level> getDimension();

	@Accessor("trackingPosition")
	boolean isTrackingPosition();

	@Accessor("unlimitedTracking")
	boolean isUnlimitedTracking();

	@Accessor("carriedByPlayers")
	Map<Player, MapItemSavedData.HoldingPlayer> getCarriedByPlayers();

	@Accessor("carriedBy")
	java.util.List<MapItemSavedData.HoldingPlayer> getCarriedBy();

	@Invoker("setDecorationsDirty")
	void tf$setDecorationsDirty();
}