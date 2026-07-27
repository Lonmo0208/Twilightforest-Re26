package twilightforest.util;

import twilightforest.beanification.Component;
import twilightforest.TwilightForestMod;

@Component
public class ModidPrefixUtil {

	public String stringPrefix(String suffix) {
		return TwilightForestMod.ID.concat(":").concat(suffix);
	}

}
