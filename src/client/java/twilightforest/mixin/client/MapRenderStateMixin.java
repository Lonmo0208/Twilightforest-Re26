package twilightforest.mixin.client;

import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MapRenderState.class)
public class MapRenderStateMixin {

	@Unique
	public MapItemSavedData twilightforest$mapData;

	@Unique
	public boolean twilightforest$isTFMap;
}