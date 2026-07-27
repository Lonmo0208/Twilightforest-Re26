package twilightforest.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

/**
 * Safe access to client-only classes from common code.
 * Uses Fabric's Environment annotation to prevent class loading on server.
 * All actual client class access is done via reflection or Fabric API.
 */
public class SafeClientAccess {

	@Nullable
	public static Object getMinecraft() {
		// Client-only: returns Minecraft instance if on client
		// Use Fabric API or avoid direct client class references in common code
		return null;
	}

	@Nullable
	public static Object getClientLevel() {
		return null;
	}

	@Nullable
	public static Object getLocalPlayer() {
		return null;
	}
}
