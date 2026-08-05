package twilightforest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.beanification.BeanContext;
import twilightforest.beanification.Configurable;
import twilightforest.config.ConfigSetup;
import twilightforest.command.TFCommand;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.events.RegistrationEvents;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.entity.passive.TinyBirdVariant;
import twilightforest.entity.passive.quest.QuestReloadListener;
import twilightforest.init.*;
import twilightforest.init.custom.*;
import twilightforest.loot.TFLootTables;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.network.TFNetwork;
import twilightforest.util.Restriction;
import twilightforest.util.TFRemapper;
import twilightforest.util.WorldUtil;
import twilightforest.util.woods.WoodPalette;
import twilightforest.util.datamaps.DataMapReloadListener;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.BiomeDensitySource;
import twilightforest.world.components.speleothem.StalactiteReloadListener;
import twilightforest.world.components.structures.StructureSpeleothemConfig;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

import java.util.Locale;

@Configurable
public final class TwilightForestMod implements ModInitializer {

	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/entity/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";

	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	static {
		BeanContext.init(ID);
	}

	@Override
	public void onInitialize() {

		// Initialize config system with ForgeConfigAPIPort
		ConfigSetup.init();

		// Register server lifecycle listener early to ensure WorldUtil.currentServer is always set
		WorldUtil.registerServerLifecycle();

		// Initialize migrated registration classes
		TFBlocks.init();
		TFItems.init();
		TFEntities.init();

		// Vanilla-compatible init registrations (flower pots, flammability, jar lids, etc.)
		BeanContext.inject(RegistrationEvents.class).init();

		// Register entity attributes with Fabric
		for (var entry : TFEntities.ATTRIBUTES.entrySet()) {
			@SuppressWarnings("unchecked")
			var entityType = (net.minecraft.world.entity.EntityType<? extends LivingEntity>) entry.getKey().get();
			AttributeSupplier supplier = entry.getValue().get().build();
			FabricDefaultAttributeRegistry.register(entityType, supplier);
		}

		TFBlockEntities.init();
		TFSounds.init();
		TFJukeboxSongs.init();
		TFCreativeTabs.init();
		TFAttributes.init();
		TFFeatures.init();
		TFGameRules.init();
		TFMenuTypes.init();
		TFMobEffects.init();
		TFParticleType.init();
		TFStats.init();
		TFStructurePieceTypes.init();
		TFStructureTypes.init();
		TFAdvancements.init();
		TFCaveCarvers.init();
		TFConsumeEffects.init();
		// Trigger class loading for static registration of loot functions, conditions, and modifiers
		TFLoot.init();
		TFLootModifiers.init();
		TFLootTables.init();
		TFDataComponents.init();
		TFDataSerializers.init();
		TFDensityFunctions.init();
		TFEnchantmentEffects.init();
		TFFeatureModifiers.init();
		TFMapDecorations.init();
		TFPOITypes.init();
		TFRecipes.init();
		TFStructurePlacementTypes.init();
		TFStructureProcessors.init();
		BiomeLayerTypes.init();
		ChunkBlanketProcessors.init();
		Enforcements.init();
		ItemDisplays.init();
		TemplateMarkerHandlers.init();
		TravellersModifierTypes.init();

		// Register network payload types and server-side handlers
		TFNetwork.registerPayloadTypes();
		TFNetwork.registerServerHandlers();

		// Register datapack registries for Fabric
		DynamicRegistries.register(TFRegistries.Keys.WOOD_PALETTES, WoodPalette.CODEC);
		DynamicRegistries.register(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack.DISPATCH_CODEC);
		DynamicRegistries.register(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.RESTRICTIONS, Restriction.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariant.CODEC);
		DynamicRegistries.register(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfig.CODEC);
		DynamicRegistries.register(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, ChunkBlanketProcessors.DISPATCH_CODEC);
		DynamicRegistries.register(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER, TemplateMarkerHandlers.DISPATCH_CODEC);
		DynamicRegistries.register(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TemplateMarkerHandlerList.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DwarfRabbitVariant.DIRECT_CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.TINY_BIRD_VARIANT, TinyBirdVariant.DIRECT_CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TravellersModifier.CODEC);

		// Register TFBiomeProvider codec in the biome_source registry for datagen serialization
		Registry.register(BuiltInRegistries.BIOME_SOURCE, prefix("twilight_biomes"), TFBiomeProvider.TF_CODEC);

		TFRemapper.addRegistryAliases();

		// Register TF commands via Fabric CommandRegistrationCallback
		TFCommand tfCommand = BeanContext.inject(TFCommand.class);
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> {
			tfCommand.register(dispatcher, buildContext);
		});

		// Register resource reload listeners (Fabric equivalent of NeoForge AddServerReloadListenersEvent)
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(StalactiteReloadListener.INSTANCE);
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(StructureTemplateDefinitions.INSTANCE);
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new QuestReloadListener());
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new DataMapReloadListener());
		// TravellersModifiersManager cache invalidation is handled via @PostConstruct in TravellersModifiersManager

		// Manually load data maps on server start to ensure they're available before world generation.
		// Use SERVER_STARTED (not SERVER_STARTING) to ensure the resource manager is fully initialized.
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			DataMapReloadListener.loadFromResourceManager(server.getResourceManager());
		});
	}

	public static Identifier prefix(String name) {
		return Identifier.fromNamespaceAndPath(ID, name.toLowerCase(Locale.ROOT));
	}

	public static Identifier getModelTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, MODEL_DIR + name);
	}

	public static Identifier getGuiTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, GUI_DIR + name);
	}

	public static Identifier getEnvTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, ENVIRO_DIR + name);
	}
}