package twilightforest.network.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.config.TFConfig;
import twilightforest.network.SyncUncraftingTableConfigPacket;

public class SyncUncraftingTableConfigPacketClientHandler {

	public static void handle(SyncUncraftingTableConfigPacket message, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			TFConfig.uncraftingXpCostMultiplier = message.uncraftingMultiplier();
			TFConfig.repairingXpCostMultiplier = message.repairingMultiplier();
			TFConfig.allowShapelessUncrafting = message.allowShapeless();
			TFConfig.disableIngredientSwitching = message.disableIngredientSwitching();
			TFConfig.disableUncraftingOnly = message.disabledUncrafting();
			TFConfig.disableEntireTable = message.disabledTable();
			TFConfig.disableUncraftingRecipes = message.disabledRecipes();
			TFConfig.reverseRecipeBlacklist = message.flipRecipeList();
			TFConfig.blacklistedUncraftingModIds = message.disabledModids();
			TFConfig.flipUncraftingModIdList = message.flipModidList();
		});
	}
}
