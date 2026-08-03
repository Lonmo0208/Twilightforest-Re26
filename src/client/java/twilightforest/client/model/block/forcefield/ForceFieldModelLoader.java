package twilightforest.client.model.block.forcefield;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.model.block.forcefield.ForceFieldModel.ExtraDirection;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

// TODO: Port to Fabric - Previously implemented UnbakedModelLoader (NeoForge-specific)
public class ForceFieldModelLoader {
	public static final ForceFieldModelLoader INSTANCE = new ForceFieldModelLoader();

	// TODO: Port to Fabric - read() overrides UnbakedModelLoader
	@SuppressWarnings("ConstantConditions")
	public UnbakedForceFieldModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {

		Map<CuboidModelElement, Condition> elementsAndConditions = new IdentityHashMap<>();

		if (json.has("elements")) {
			for (JsonElement jsonElement : GsonHelper.getAsJsonArray(json, "elements")) {
				ExtraDirection direction = null;
				boolean b = false;
				List<ExtraDirection> parents = new ArrayList<>();

				if (jsonElement instanceof JsonObject element) {
					if (element.get("condition") instanceof JsonObject condition) {
						direction = ForceFieldModel.ExtraDirection.byName(GsonHelper.getAsString(condition, "direction", "up"));
						b = GsonHelper.getAsBoolean(condition, "if", true);
						for (JsonElement parentElement : GsonHelper.getAsJsonArray(condition, "parents")) {
							parents.add(ForceFieldModel.ExtraDirection.byName(parentElement.getAsString()));
						}
					}
				}
				elementsAndConditions.put(context.deserialize(jsonElement, CuboidModelElement.class), new Condition(direction, b, parents));
			}
		}

		// Parse the texture map ("pane"/"particle") so baked materials can be resolved at bake time
		TextureSlots.Data textureSlots = json.has("textures")
			? TextureSlots.parseTextureMap(GsonHelper.getAsJsonObject(json, "textures"))
			: TextureSlots.Data.EMPTY;

		// TODO: Port to Fabric - StandardModelParameters.parse is NeoForge-specific
		return new UnbakedForceFieldModel(elementsAndConditions, textureSlots);
	}

	public record Condition(@Nullable ExtraDirection direction, boolean b, List<ExtraDirection> parents) {

	}
}
