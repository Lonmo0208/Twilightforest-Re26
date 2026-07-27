package twilightforest.init;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import twilightforest.TwilightForestMod;
import net.minecraft.core.Registry;

public class TFPOITypes {

	public static final PoiType GHAST_TRAP = new PoiType(ImmutableSet.copyOf(TFBlocks.GHAST_TRAP.getStateDefinition().getPossibleStates()), 0, 1);

	public static void init() {
		Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, TwilightForestMod.prefix("ghast_trap"), GHAST_TRAP);
	}

}