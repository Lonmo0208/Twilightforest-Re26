package twilightforest.config;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import twilightforest.TwilightForestMod;
import twilightforest.network.SyncUncraftingTableConfigPacket;

public final class ConfigSetup {

	private static TFCommonConfig COMMON_CONFIG;
	private static TFClientConfig CLIENT_CONFIG;
	private static ModConfigSpec COMMON_SPEC;
	private static ModConfigSpec CLIENT_SPEC;
	private static MinecraftServer currentServer;

	public static void init() {
		// Build config specs
		{
			ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
			COMMON_CONFIG = new TFCommonConfig(builder);
			COMMON_SPEC = builder.build();
		}
		{
			ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
			CLIENT_CONFIG = new TFClientConfig(builder);
			CLIENT_SPEC = builder.build();
		}

		// Register configs with ForgeConfigAPIPort (auto-loads config files)
		ConfigRegistry.INSTANCE.register(TwilightForestMod.ID, ModConfig.Type.COMMON, COMMON_SPEC);
		ConfigRegistry.INSTANCE.register(TwilightForestMod.ID, ModConfig.Type.CLIENT, CLIENT_SPEC);

		// Immediately rebake config values after registration in case loading events already fired
		TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		TFConfig.rebakeClientOptions(CLIENT_CONFIG);

		// Track server instance for config sync
		ServerLifecycleEvents.SERVER_STARTED.register(server -> currentServer = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> currentServer = null);

		// Register config load/reload event handlers
		ModConfigEvents.loading(TwilightForestMod.ID).register(config -> {
			if (config.getSpec() == COMMON_SPEC) {
				TFConfig.rebakeCommonOptions(COMMON_CONFIG);
			} else if (config.getSpec() == CLIENT_SPEC) {
				TFConfig.rebakeClientOptions(CLIENT_CONFIG);
			}
		});

		ModConfigEvents.reloading(TwilightForestMod.ID).register(config -> {
			if (config.getSpec() == COMMON_SPEC) {
				TFConfig.rebakeCommonOptions(COMMON_CONFIG);
				// Sync updated config to all online players
				if (currentServer != null) {
					for (var player : currentServer.getPlayerList().getPlayers()) {
						syncUncraftingConfig(player);
					}
				}
			} else if (config.getSpec() == CLIENT_SPEC) {
				TFConfig.rebakeClientOptions(CLIENT_CONFIG);
			}
		});

		ModConfigEvents.unloading(TwilightForestMod.ID).register(config -> {
		});

		// Register uncrafting config sync on player login
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			syncUncraftingConfig(handler.getPlayer());
		});
	}

	public static TFCommonConfig getCommonConfig() {
		return COMMON_CONFIG;
	}

	public static TFClientConfig getClientConfig() {
		return CLIENT_CONFIG;
	}

	public static ModConfigSpec getCommonSpec() {
		return COMMON_SPEC;
	}

	public static ModConfigSpec getClientSpec() {
		return CLIENT_SPEC;
	}

	public static void syncUncraftingConfig(ServerPlayer player) {
		MinecraftServer server = player.level().getServer();
		if (server != null && server.isDedicatedServer()) {
			ServerPlayNetworking.send(player, new SyncUncraftingTableConfigPacket(
				COMMON_CONFIG.UNCRAFTING_STUFFS.uncraftingXpCostMultiplier.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.repairingXpCostMultiplier.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.allowShapelessUncrafting.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.disableIngredientSwitching.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingOnly.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.disableEntireTable.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingRecipes.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.reverseRecipeBlacklist.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get(),
				COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.get()));
		}
	}
}
