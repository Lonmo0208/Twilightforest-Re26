package twilightforest.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twilightforest.beanification.Component;

/**
 * Update URL interceptor - no-op in Fabric as there is no equivalent runtime update JSON URL override mechanism.
 * In NeoForge, this redirected the update JSON URL to a custom endpoint. Fabric's update checking is
 * handled via fabric.mod.json's updateUrl field instead.
 */
@Component
public class ModUpdateURLInterceptor {

	private final Logger logger = LogManager.getLogger();

	public ModUpdateURLInterceptor() {
		// No-op: Fabric does not have an equivalent runtime update JSON URL override mechanism.
		// The update URL is specified statically in fabric.mod.json.
		logger.debug("ModUpdateURLInterceptor is not applicable in Fabric; update URL is specified in fabric.mod.json");
	}

}
