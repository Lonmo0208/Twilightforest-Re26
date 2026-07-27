package twilightforest.entity.boss;

import net.minecraft.world.level.Level;
import twilightforest.init.TFEntities;

public class HydraSmallPart extends HydraPart {

	public HydraSmallPart(Hydra hydra, float w, float h) {
		super(hydra, TFEntities.HYDRA_SMALL_PART.get(), hydra.level(), w, h);
	}
}
