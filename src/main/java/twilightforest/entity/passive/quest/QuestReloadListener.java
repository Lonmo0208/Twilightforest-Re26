package twilightforest.entity.passive.quest;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;

import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;

import java.util.Map;

public class QuestReloadListener extends SimpleJsonResourceReloadListener<JsonElement> implements IdentifiableResourceReloadListener {

	private static final QuestingRamCurrentContext questingRamCurrentContext = QuestingRamCurrentContext.SHARED;

	public QuestReloadListener() {
		super(ExtraCodecs.JSON, FileToIdConverter.json("twilight/quests"));
	}

	@Override
	public Identifier getFabricId() {
		return Identifier.fromNamespaceAndPath(TwilightForestMod.ID, "quests");
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
		boolean found = false;
		RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
		for (var entry : object.entrySet()) {
			if (entry.getKey().getPath().endsWith("/questing_ram")) {
				questingRamCurrentContext.setContext(QuestingRamContext.CODEC.parse(ops, entry.getValue()).getOrThrow(RuntimeException::new));
				TwilightForestMod.LOGGER.debug("Questing Ram quest set by mod {}", entry.getKey().getNamespace());
				found = true;
			}
		}

		if (!found) {
			TwilightForestMod.LOGGER.error("Questing Ram quest file not found. Defaulting to fallback");
			questingRamCurrentContext.setContext(QuestingRamContext.FALLBACK);
		}
	}
}
