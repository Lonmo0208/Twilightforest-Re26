package twilightforest.config;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import twilightforest.network.SyncUncraftingTableConfigPacket;

public final class ConfigSetup {

	static final FabricModConfigSpec CLIENT_SPEC;
	static final FabricModConfigSpec COMMON_SPEC;
	static final TFClientConfig CLIENT_CONFIG;
	static final TFCommonConfig COMMON_CONFIG;

	static {
		{
			FabricModConfigSpec spec = new FabricModConfigSpec("twilightforest-common");
			COMMON_CONFIG = new TFCommonConfig(new FabricModConfigSpec.Builder(spec));
			COMMON_SPEC = spec;
		}
		{
			FabricModConfigSpec spec = new FabricModConfigSpec("twilightforest-client");
			CLIENT_CONFIG = new TFClientConfig(new FabricModConfigSpec.Builder(spec));
			CLIENT_SPEC = spec;
		}
	}

	public static void loadConfigs() {
		COMMON_SPEC.load(FabricLoader.getInstance().getConfigDir());
		TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		CLIENT_SPEC.load(FabricLoader.getInstance().getConfigDir());
		TFConfig.rebakeClientOptions(CLIENT_CONFIG);
	}

	public static void reloadConfigs() {
		COMMON_SPEC.load(FabricLoader.getInstance().getConfigDir());
		TFConfig.rebakeCommonOptions(COMMON_CONFIG);
		CLIENT_SPEC.load(FabricLoader.getInstance().getConfigDir());
		TFConfig.rebakeClientOptions(CLIENT_CONFIG);
	}

	//sends uncrafting settings to a player on a server when they log in. This prevents desyncs when the configs dont match up between the player and the server.
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