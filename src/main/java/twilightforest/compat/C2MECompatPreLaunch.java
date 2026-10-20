package twilightforest.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * C2ME's OpenCL acceleration module compiles the noise router of every dimension to OpenCL.
 * The Twilight Forest dimension uses biome-driven density functions that cannot be expressed
 * as an OpenCL kernel, so its codegen fails and, with the module's default configuration, the
 * whole server crashes while loading the dimension.
 * <p>
 * Setting C2ME's documented system-property override before its config is initialized makes
 * the module fall back to vanilla (CPU) generation for the dimensions it cannot compile,
 * while the vanilla dimensions keep using OpenCL.
 */
public final class C2MECompatPreLaunch implements PreLaunchEntrypoint {

	private static final Logger LOGGER = LoggerFactory.getLogger("TwilightForest/C2ME-OCL");

	private static final String OPENCL_MOD_ID = "c2me-opts-accel-opencl";
	private static final String FALLBACK_PROPERTY = "c2me.base.config.override.openclAccel.allowIncompatibilityFallback";

	@Override
	public void onPreLaunch() {
		if (!FabricLoader.getInstance().isModLoaded(OPENCL_MOD_ID)) {
			return;
		}
		if (System.getProperty(FALLBACK_PROPERTY) != null) {
			return;
		}

		System.setProperty(FALLBACK_PROPERTY, "true");
		LOGGER.info("C2ME-OCL detected: enabling its incompatibility fallback. The Twilight Forest dimension generates on the CPU because its biome-driven density functions cannot be compiled to OpenCL.");
	}
}
