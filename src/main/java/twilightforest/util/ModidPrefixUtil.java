package twilightforest.util;

import tamaized.beanification.Component;
import twilightforest.TwilightForestMod;

@Component
public class ModidPrefixUtil {

	public String stringPrefix(String suffix) {
		return TwilightForestMod.ID.concat(":").concat(suffix);
	}

}
