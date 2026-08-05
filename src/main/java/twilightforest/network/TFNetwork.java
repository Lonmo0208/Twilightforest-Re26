package twilightforest.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/**
 * Centralized networking setup for Twilight Forest.
 * Called from both server and client initializers.
 */
public class TFNetwork {

	/**
	 * Register all payload types with PayloadTypeRegistry.
	 * Must be called on both server and client sides.
	 */
	public static void registerPayloadTypes() {
		// === Server-to-Client (S2C) packets ===
		var s2c = PayloadTypeRegistry.clientboundPlay();
		s2c.register(AreaProtectionPacket.TYPE, AreaProtectionPacket.STREAM_CODEC);
		s2c.register(CreateMovingCicadaSoundPacket.TYPE, CreateMovingCicadaSoundPacket.STREAM_CODEC);
		s2c.register(EnforceProgressionStatusPacket.TYPE, EnforceProgressionStatusPacket.STREAM_CODEC);
		s2c.register(MagicMapPacket.TYPE, MagicMapPacket.STREAM_CODEC);
		s2c.register(MazeMapPacket.TYPE, MazeMapPacket.STREAM_CODEC);
		s2c.register(MissingAdvancementToastPacket.TYPE, MissingAdvancementToastPacket.STREAM_CODEC);
		s2c.register(MovePlayerPacket.TYPE, MovePlayerPacket.STREAM_CODEC);
		s2c.register(ParticlePacket.TYPE, ParticlePacket.STREAM_CODEC);
		s2c.register(SpawnCharmPacket.TYPE, SpawnCharmPacket.STREAM_CODEC);
		s2c.register(SpawnFallenLeafFromPacket.TYPE, SpawnFallenLeafFromPacket.STREAM_CODEC);
		s2c.register(StructureProtectionPacket.TYPE, StructureProtectionPacket.STREAM_CODEC);
		s2c.register(SyncUncraftingTableConfigPacket.TYPE, SyncUncraftingTableConfigPacket.STREAM_CODEC);
		s2c.register(UpdateTFMultipartPacket.TYPE, UpdateTFMultipartPacket.STREAM_CODEC);
		s2c.register(UpdateThrownPacket.TYPE, UpdateThrownPacket.STREAM_CODEC);
		s2c.register(LifedrainParticlePacket.TYPE, LifedrainParticlePacket.STREAM_CODEC);
		s2c.register(UpdateDeathTimePacket.TYPE, UpdateDeathTimePacket.STREAM_CODEC);
		s2c.register(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacket.AddTFBossBarPacket.STREAM_CODEC);
		s2c.register(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacket.UpdateTFBossBarStylePacket.STREAM_CODEC);
		s2c.register(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacket.STREAM_CODEC);
		s2c.register(SyncQuestsPacket.TYPE, SyncQuestsPacket.STREAM_CODEC);
		s2c.register(TravellersWingsStatePacket.TYPE, TravellersWingsStatePacket.STREAM_CODEC);
		s2c.register(SyncUncraftingCostsPacket.TYPE, SyncUncraftingCostsPacket.STREAM_CODEC);

		// Bidirectional packets (S2C side)
		s2c.register(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC);
		s2c.register(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC);

		// === Client-to-Server (C2S) packets ===
		var c2s = PayloadTypeRegistry.serverboundPlay();
		c2s.register(PerformDoubleJumpPacket.TYPE, PerformDoubleJumpPacket.STREAM_CODEC);
		c2s.register(SwapHotbarPacket.TYPE, SwapHotbarPacket.STREAM_CODEC);
		c2s.register(PerformSidestepPacket.TYPE, PerformSidestepPacket.STREAM_CODEC);
		c2s.register(CycleMapSlotPacket.TYPE, CycleMapSlotPacket.STREAM_CODEC);
		c2s.register(UncraftingGuiPacket.TYPE, UncraftingGuiPacket.STREAM_CODEC);
		c2s.register(WipeOreMeterPacket.TYPE, WipeOreMeterPacket.STREAM_CODEC);

		// Bidirectional packets (C2S side)
		c2s.register(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC);
		c2s.register(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC);
	}

	/**
	 * Register server-side packet handlers.
	 * Must be called from the server initializer.
	 */
	public static void registerServerHandlers() {
		ServerPlayNetworking.registerGlobalReceiver(PerformDoubleJumpPacket.TYPE, PerformDoubleJumpPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(SwapHotbarPacket.TYPE, SwapHotbarPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(PerformSidestepPacket.TYPE, PerformSidestepPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(CycleMapSlotPacket.TYPE, CycleMapSlotPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(UncraftingGuiPacket.TYPE, UncraftingGuiPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(WipeOreMeterPacket.TYPE, WipeOreMeterPacket::handle);

		// Bidirectional packets - server side
		ServerPlayNetworking.registerGlobalReceiver(GogglesZoomPacket.TYPE, GogglesZoomPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(GradualGlidePacket.TYPE, GradualGlidePacket::handle);
	}
}