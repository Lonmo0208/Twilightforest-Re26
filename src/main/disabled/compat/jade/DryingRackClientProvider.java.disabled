package twilightforest.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;

public enum DryingRackClientProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		CompoundTag data = accessor.getServerData();
		if (data.contains("progress")) {
			int progress = data.getInt("progress").orElse(0);
			int total = data.getInt("total").orElse(0);
			tooltip.add(JadeUI.text(Component.translatable("jade.drying_rack.remaining", RecipeViewerConstants.getDryingTime(total - progress))));
		}
	}

	@Override
	public Identifier getUid() {
		return TwilightForestMod.prefix("drying_rack");
	}
}