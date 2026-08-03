package twilightforest.client.model.block.patch;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.util.GsonHelper;

public final class PatchModelLoader {
	public static final PatchModelLoader INSTANCE = new PatchModelLoader();

	private PatchModelLoader() {
	}

	public UnbakedPatchModel read(JsonObject object, JsonDeserializationContext deserializationContext) throws JsonParseException {
		boolean shaggify = GsonHelper.getAsBoolean(object, "shaggify", false);
		TextureSlots.Data textureSlots = object.has("textures") ? TextureSlots.parseTextureMap(GsonHelper.getAsJsonObject(object, "textures")) : TextureSlots.Data.EMPTY;
		return new UnbakedPatchModel(shaggify, textureSlots);
	}
}
