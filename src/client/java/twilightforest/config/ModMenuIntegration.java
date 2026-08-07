package twilightforest.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import twilightforest.TwilightForestMod;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigurationScreen(TwilightForestMod.ID, parent);
    }
}