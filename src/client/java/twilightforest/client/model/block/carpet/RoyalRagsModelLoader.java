package twilightforest.client.model.block.carpet;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
// TODO: Port to Fabric - StandardModelParameters and UnbakedModelLoader are NeoForge-specific
// import net.neoforged.neoforge.client.model.StandardModelParameters;
// import net.neoforged.neoforge.client.model.UnbakedModelLoader;

// TODO: Port to Fabric - Previously implemented UnbakedModelLoader (NeoForge-specific)
public final class RoyalRagsModelLoader {
	public static final RoyalRagsModelLoader INSTANCE = new RoyalRagsModelLoader();

	private RoyalRagsModelLoader() {
	}

	// TODO: Port to Fabric - read() overrides UnbakedModelLoader; StandardModelParameters is NeoForge-specific
	public UnbakedRoyalRagsModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
		// return new UnbakedRoyalRagsModel(StandardModelParameters.parse(object, deserializationContext));
		return new UnbakedRoyalRagsModel();
	}
}
