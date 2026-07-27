package twilightforest.client.model.block.carpet;

// TODO: Port to Fabric - CustomLoaderBuilder is NeoForge-specific
// import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import twilightforest.TwilightForestMod;

// TODO: Port to Fabric - Previously extended CustomLoaderBuilder (NeoForge-specific)
public class RoyalRagsBuilder {

	public RoyalRagsBuilder() {
		// super(TwilightForestMod.prefix("royal_rags"), false);
	}

	public static RoyalRagsBuilder begin() {
		return new RoyalRagsBuilder();
	}

	// @Override
	// public CustomLoaderBuilder copyInternal() {
	// 	return new RoyalRagsBuilder();
	// }
}
