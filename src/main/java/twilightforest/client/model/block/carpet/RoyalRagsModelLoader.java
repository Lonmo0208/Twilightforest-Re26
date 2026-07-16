package twilightforest.client.model.block.carpet;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

public final class RoyalRagsModelLoader implements UnbakedModelLoader<UnbakedRoyalRagsModel> {
	public static final RoyalRagsModelLoader INSTANCE = new RoyalRagsModelLoader();

	private RoyalRagsModelLoader() {
	}

	@Override
	public UnbakedRoyalRagsModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
		return new UnbakedRoyalRagsModel(StandardModelParameters.parse(object, deserializationContext));
	}
}
