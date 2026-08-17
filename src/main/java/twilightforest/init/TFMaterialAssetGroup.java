package twilightforest.init;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.Map;

// TODO-263: MaterialAssetGroup class removed, use placeholder Object for now
public class TFMaterialAssetGroup {

	public static final Object IRONWOOD = create("twilightforest_ironwood");
	public static final Object STEELEAF = create("twilightforest_steeleaf");
	public static final Object KNIGHTMETAL = create("twilightforest_knightmetal");
	public static final Object FIERY = create("twilightforest_fiery");
	public static final Object NAGA_SCALE = create("twilightforest_naga_scale");
	public static final Object CARMINITE = create("twilightforest_carminite");

	public static Object create(String base) {
		return null; // TODO-263: MaterialAssetGroup removed
	}

	public static Object create(String base, Map<ResourceKey<EquipmentAsset>, String> overrides) {
		return null; // TODO-263: MaterialAssetGroup removed
	}
}
