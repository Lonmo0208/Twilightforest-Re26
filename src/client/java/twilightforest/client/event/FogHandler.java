package twilightforest.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBiomes;

import org.jetbrains.annotations.Nullable;

public class FogHandler {

	private static boolean SKY_CHUNK_LOADED = false;

	private static float SKY_FAR = 0.0F;
	private static float SKY_NEAR = 0.0F;

	private static boolean TERRAIN_CHUNK_LOADED = false;

	private static float TERRAIN_FAR = 0.0F;
	private static float TERRAIN_NEAR = 0.0F;

	// TODO: Port to Fabric - ViewportEvent.RenderFog is NeoForge-specific
	// protected static void renderFog(ViewportEvent.RenderFog event) {
	//     ...
	// }
	protected static void renderFog() {
	}

	// TODO: Port to Fabric - LevelEvent.Unload is NeoForge-specific
	// protected static void unloadFog(LevelEvent.Unload event) {
	protected static void unloadFog() {
		SKY_CHUNK_LOADED = false;
		TERRAIN_CHUNK_LOADED = false;
	}

	private static boolean isSpooky(@Nullable ClientLevel level, @Nullable LocalPlayer player) {
		return level != null && player != null && level.getBiome(player.blockPosition()).is(TFBiomes.SPOOKY_FOREST);
	}
}
