package twilightforest.compat.c2me;

// Adapted from tf-c2me-compat (MIT); see THIRD_PARTY_NOTICES.md.

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.compat.c2me.bind.TfDfBindings;

import java.nio.file.Path;

/**
 * Registers Twilight Forest's custom density functions into C2ME's dfc compiler when C2ME is
 * installed. Loaded only from the guarded hook in {@code TwilightForestMod}.
 * <p>
 * Portions adapted from tf-c2me-compat (MIT) by Lonmo/lbw14514; see THIRD_PARTY_NOTICES.md.
 */
public final class CompatInit {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompatInit.class);

	private CompatInit() {
	}

	public static void init(Path configDir) {
		try {
			TfDfBindings.register();
			LOGGER.info("[TF-C2ME-Compat] Twilight Forest <-> C2ME dfc bindings active (JVM + OpenCL)");
		} catch (Throwable t) {
			// Never hard-fail the game over an optimization mod; C2ME will
			// simply fall back to DelegateNode evaluation without us.
			LOGGER.error("[TF-C2ME-Compat] Failed to register dfc bindings; TF will fall back to C2ME's default handling", t);
		}
		// C2ME's OpenCL module is a separate optional mod; this is a no-op without it.
		OpenclFallbackEnabler.enable(configDir);
	}
}
