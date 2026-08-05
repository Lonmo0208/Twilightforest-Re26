package twilightforest.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.fabricmc.fabric.api.client.model.loading.v1.UnbakedModelDeserializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.AtlasRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.monster.silverfish.SilverfishModel;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.world.entity.Entity;
import twilightforest.TwilightForestMod;
import twilightforest.client.event.ColorHandler;
import twilightforest.client.event.ClientGameEvents;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.*;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.client.model.block.UnbakedReactorDebrisBlockStateModel;
import twilightforest.client.model.block.aurorablock.UnbakedNoiseVaryingBlockStateModel;
import twilightforest.client.model.block.carpet.UnbakedRoyalRagsBlockStateModel;
import twilightforest.client.model.block.connected.ConnectedTextureBlockStateModel;
import twilightforest.client.model.block.forcefield.ForceFieldModelLoader;
import twilightforest.client.model.block.forcefield.UnbakedForceFieldBlockStateModel;
import twilightforest.client.model.block.giantblock.UnbakedGiantBlockStateModel;
import twilightforest.client.model.block.patch.PatchModelLoader;
import twilightforest.client.model.block.patch.UnbakedPatchBlockStateModel;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.item.TravellersGearItemModel;
import twilightforest.client.model.item.TrollsteinnModel;
import twilightforest.client.particle.*;
import twilightforest.client.properties.Experiment115Type;
import twilightforest.client.properties.MoonwormQueenPulse;
import twilightforest.client.properties.OreMeterFlash;
import twilightforest.client.properties.PotionFlaskDamage;
import twilightforest.client.properties.PotionFlaskDosage;
import twilightforest.client.renderer.armor.TFArmorRenderer;
import twilightforest.client.renderer.block.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.special.*;
import twilightforest.client.renderer.tooltip.ItemDisplayTooltipComponent;
import twilightforest.client.renderer.tooltip.PotionFlaskTooltipComponent;
import twilightforest.client.renderer.tooltip.TravellersBeltTooltipComponent;
import twilightforest.init.*;
import twilightforest.item.PotionFlaskItem;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersGogglesItem;
import twilightforest.network.*;
import twilightforest.network.client.*;
import twilightforest.network.UpdateTFMultipartPacket;
import twilightforest.util.TFEntityExtensions;
import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;

import java.util.Set;

public class TwilightForestClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Inform Forge Config API Port that our mod should display its config button
		// in Mod Menu. ConfigScreenFactoryRegistry is queried by FCAP's ModMenuApiImpl
		// via getProvidedConfigScreenFactories(). For each mod id, FCAP will provide
		// a single tabbed config screen containing CLIENT / COMMON / SERVER sub-tabs
		// for every ModConfig registered to that mod id. We register empty factories
		// for each known config file name, so the ModMenuApiImpl sees our mod id
		// when iterating registered factories and includes us in the returned map.
		// NOTE: The BiFunction is intentionally a no-op returning null, which
		// triggers FCAP's fallback (building the standard tabbed screen automatically).
		ConfigScreenFactoryRegistry.INSTANCE.register(TwilightForestMod.ID + "-client",
			(fileName, parent) -> null);
		ConfigScreenFactoryRegistry.INSTANCE.register(TwilightForestMod.ID + "-common",
			(fileName, parent) -> null);
		ClientProxyInitializer.init();
		TFShaders.registerRenderPipelines();
		registerCustomModelTypes();
		registerAtlases();
		registerTooltipComponents();
		registerModelLayers();
		registerEntityRenderers();
		registerBlockEntityRenderers();
		registerScreens();
		registerParticleFactories();
		registerBlockColors();
		registerItemColors();

		// Register armor renderers
		TFArmorRenderer.bootstrap();

		// Register boat texture generator
		ResourceLoader.get(net.minecraft.server.packs.PackType.CLIENT_RESOURCES)
			.registerReloadListener(TwilightForestMod.prefix("texture_generator"), TextureGeneratorReloadListener.INSTANCE);

		// Register client-side packet handlers (payload types already registered in TwilightForestMod)
		registerClientHandlers();

		// Clear the client-side feather-fan flag when the player lands. Fabric attachments
		// are not synced when they change, so the client must maintain this flag locally to
		// prevent the Peacock Feather Fan from granting an unlimited mid-air jump.
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				var player = client.player;
				if (((TFEntityExtensions) player).twilightforest$getData(TFDataAttachments.FEATHER_FAN)
					&& (player.onGround() || player.isSwimming() || player.isInWater())) {
					((TFEntityExtensions) player).twilightforest$setData(TFDataAttachments.FEATHER_FAN, false);
				}
			}
		});

		// Register aurora rendering for glacier biomes.
		// BEFORE_TRANSLUCENT_TERRAIN fires after the opaque terrain pass (which writes the
		// depth buffer), so the aurora can be depth-tested against solid geometry and
		// correctly appear behind buildings / terrain. Equivalent to NeoForge AFTER_WEATHER.
		ClientGameEvents clientGameEvents = new ClientGameEvents();
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			clientGameEvents.clientTick();
		});
		LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register(clientGameEvents::renderAurora);
	}

	private void registerAtlases() {
		// Register custom magic_paintings texture atlas
		AtlasRegistry.register(new AtlasManager.AtlasConfig(
			MagicPaintingAtlasInfo.ATLAS_LOCATION,
			MagicPaintingAtlasInfo.ATLAS_INFO_LOCATION,
			false
		));
	}

	private void registerCustomModelTypes() {
		// ItemModel.Unbaked types
		ItemModels.ID_MAPPER.put(TwilightForestMod.prefix("trollsteinn"), TrollsteinnModel.Unbaked.MAP_CODEC);
		ItemModels.ID_MAPPER.put(TwilightForestMod.prefix("travellers_gear"), TravellersGearItemModel.Unbaked.MAP_CODEC);

		// UnbakedModel deserializers (Fabric equivalent of NeoForge's ModelEvent.RegisterLoaders)
		UnbakedModelDeserializer.register(TwilightForestMod.prefix("force_field"), (json, context) -> ForceFieldModelLoader.INSTANCE.read(json, context));
		UnbakedModelDeserializer.register(TwilightForestMod.prefix("patch"), (json, context) -> PatchModelLoader.INSTANCE.read(json, context));

		// CustomUnbakedBlockStateModel types (Fabric equivalent of NeoForge's RegisterBlockStateModels)
		CustomUnbakedBlockStateModel.register(TwilightForestMod.prefix("noise_varying"), UnbakedNoiseVaryingBlockStateModel.MAP_CODEC);
		CustomUnbakedBlockStateModel.register(TwilightForestMod.prefix("force_field"), UnbakedForceFieldBlockStateModel.MAP_CODEC);
		CustomUnbakedBlockStateModel.register(TwilightForestMod.prefix("connected_texture_block"), ConnectedTextureBlockStateModel.MAP_CODEC);
		CustomUnbakedBlockStateModel.register(TwilightForestMod.prefix("royal_rags"), UnbakedRoyalRagsBlockStateModel.MAP_CODEC);
		CustomUnbakedBlockStateModel.register(TwilightForestMod.prefix("giant_block"), UnbakedGiantBlockStateModel.MAP_CODEC);
		CustomUnbakedBlockStateModel.register(TwilightForestMod.prefix("patch"), UnbakedPatchBlockStateModel.MAP_CODEC);
		CustomUnbakedBlockStateModel.register(TwilightForestMod.prefix("reactor_debris"), UnbakedReactorDebrisBlockStateModel.MAP_CODEC);

		// SpecialModelRenderer.Unbaked types
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("brazier"), BrazierSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("block/candelabra"), CandelabraSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("item/cicada"), CicadaSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("item/firefly"), FireflySpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("item/keepsake_casket"), KeepsakeCasketSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("block/mason_jar"), MasonJarSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("item/moonworm"), MoonwormSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("item/mystic_crown"), MysticCrownSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("item/skull_chest"), SkullChestSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("item/template_skull_candle"), SkullCandleSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("item/template_trophy"), TrophySpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderers.ID_MAPPER.put(TwilightForestMod.prefix("knightmetal_shield"), KnightmetalShieldSpecialRenderer.Unbaked.MAP_CODEC);

		// RangeSelectItemModelProperty types (numeric)
		RangeSelectItemModelProperties.ID_MAPPER.put(TwilightForestMod.prefix("potion_flask_damage"), PotionFlaskDamage.TYPE);
		RangeSelectItemModelProperties.ID_MAPPER.put(TwilightForestMod.prefix("potion_flask_dosage"), PotionFlaskDosage.TYPE);

		// SelectItemModelProperty types
		SelectItemModelProperties.ID_MAPPER.put(TwilightForestMod.prefix("experiment_115_variant"), Experiment115Type.TYPE);

		// ConditionalItemModelProperty types
		ConditionalItemModelProperties.ID_MAPPER.put(TwilightForestMod.prefix("ore_meter_flash"), OreMeterFlash.TYPE);
		ConditionalItemModelProperties.ID_MAPPER.put(TwilightForestMod.prefix("moonworm_queen_pulse"), MoonwormQueenPulse.TYPE);
	}

	private void registerTooltipComponents() {
		ClientTooltipComponentCallback.EVENT.register(data -> {
			if (data instanceof TravellersGogglesItem.Tooltip tooltip) {
				return new ItemDisplayTooltipComponent(tooltip);
			}
			if (data instanceof TravellersArmorBeltItem.Tooltip tooltip) {
				return new TravellersBeltTooltipComponent(tooltip);
			}
			if (data instanceof PotionFlaskItem.Tooltip tooltip) {
				return new PotionFlaskTooltipComponent(tooltip);
			}
			return null;
		});
	}

	@SuppressWarnings("unchecked")
	private void registerModelLayers() {
		// Armor
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_INNER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_OUTER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_INNER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_OUTER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_HELMET, () -> LayerDefinition.create(TravellersGearModels.addGogglePieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.295F), false), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.295F), true), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, () -> TravellersWingsModel.createLayer(0.25F));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_BOOTS, () -> LayerDefinition.create(TravellersGearModels.addBootPieces(new CubeDeformation(0.5F)), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_INNER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_OUTER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_INNER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_OUTER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_INNER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_OUTER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));

		// Trophies
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI_TROPHY, AlphaYetiModel::createTrophy);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_TROPHY, HydraHeadModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM_TROPHY, KnightPhantomModel::createTrophy);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_TROPHY, LichModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM_TROPHY, MinoshroomModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_TROPHY, NagaModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM_TROPHY, QuestRamModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN_TROPHY, SnowQueenModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST_TROPHY, UrGhastModel::create);

		// Entities
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ADHERENT, AdherentModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ARMORED_GIANT, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP, BighornModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP_BABY, () -> BighornModel.create().apply(BighornModel.BABY_TRANSFORMER));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP_WOOL, SheepFurModel::createFurLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP_BABY_WOOL, () -> SheepFurModel.createFurLayer().apply(new BabyModelTransform(false, 8.0F, 6.0F, Set.of("head"))));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BOAR, BoarModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BOAR_BABY, () -> BoarModel.create().apply(BoarModel.BABY_TRANSFORMER));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BUNNY, BunnyModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN, ChainModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DEER, DeerModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DEER_BABY, () -> DeerModel.create().apply(DeerModel.BABY_TRANSFORMER));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIRE_BEETLE, FireBeetleModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HELMET_CRAB, HelmetCrabModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HOSTILE_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_HEAD, HydraHeadModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA, HydraModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_NECK, HydraNeckModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KOBOLD, KoboldModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_MINION, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LICH, LichModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.LOYAL_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM, MinoshroomModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINOTAUR, MinotaurModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MIST_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA, NagaModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_BODY, NagaModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PENGUIN, PenguinModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PENGUIN_BABY, () -> PenguinModel.create().apply(PenguinModel.BABY_TRANSFORMER));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PINCH_BEETLE, PinchBeetleModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.PROTECTION_BOX, () -> LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM, QuestRamModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.RAVEN, RavenModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP, RedcapModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.7F), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.65F), 0.7F), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.RISING_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE, SlimeBeetleModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE_TAIL, SlimeBeetleModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN, SnowQueenModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SQUIRREL, SquirrelModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TINY_BIRD, TinyBirdModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TROLL, TrollModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST, UrGhastModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.WINTER_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.WRAITH, WraithModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.YETI, YetiModel::create);

		// Block entities
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CICADA, CicadaModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.FIREFLY, FireflyModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KEEPSAKE_CASKET, () -> KeepsakeCasketModel.create(true));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SKULL_CHEST, () -> KeepsakeCasketModel.create(false));
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MOONWORM, MoonwormModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.BRAZIER, BrazierModel::create);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.RED_THREAD, RedThreadModel::create);

		// Shield
		ModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);

		// Boats
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TWILIGHT_OAK_BOAT, BoatModel::createBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TWILIGHT_OAK_CHEST_BOAT, BoatModel::createChestBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CANOPY_BOAT, BoatModel::createBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.CANOPY_CHEST_BOAT, BoatModel::createChestBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MANGROVE_BOAT, BoatModel::createBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MANGROVE_CHEST_BOAT, BoatModel::createChestBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DARK_BOAT, BoatModel::createBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.DARK_CHEST_BOAT, BoatModel::createChestBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TIME_BOAT, BoatModel::createBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TIME_CHEST_BOAT, BoatModel::createChestBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRANSFORMATION_BOAT, BoatModel::createBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.TRANSFORMATION_CHEST_BOAT, BoatModel::createChestBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINING_BOAT, BoatModel::createBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.MINING_CHEST_BOAT, BoatModel::createChestBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SORTING_BOAT, BoatModel::createBoatModel);
		ModelLayerRegistry.registerModelLayer(TFModelLayers.SORTING_CHEST_BOAT, BoatModel::createChestBoatModel);
	}

	private void registerEntityRenderers() {
		// Living entities
		EntityRenderers.register(TFEntities.BOAR.get(), BoarRenderer::new);
		EntityRenderers.register(TFEntities.BIGHORN_SHEEP.get(), BighornRenderer::new);
		EntityRenderers.register(TFEntities.DEER.get(), DeerRenderer::new);
		EntityRenderers.register(TFEntities.REDCAP.get(), RedcapRenderer::new);
		EntityRenderers.register(TFEntities.SKELETON_DRUID.get(), SkeletonDruidRenderer::new);
		EntityRenderers.register(TFEntities.HOSTILE_WOLF.get(), HostileWolfRenderer::new);
		EntityRenderers.register(TFEntities.WRAITH.get(), WraithRenderer::new);
		EntityRenderers.register(TFEntities.HYDRA.get(), HydraRenderer::new);
		@SuppressWarnings({"unchecked", "rawtypes"})
		EntityRendererProvider<Entity> hydraHeadProvider = ctx -> (EntityRenderer) new HydraHeadRenderer(ctx, new HydraHeadModel(ctx.bakeLayer(TFModelLayers.HYDRA_HEAD)));
		EntityRenderers.register(TFEntities.HYDRA_HEAD.get(), hydraHeadProvider);
		@SuppressWarnings({"unchecked", "rawtypes"})
		EntityRendererProvider<Entity> hydraNeckProvider = ctx -> (EntityRenderer) new HydraNeckRenderer(ctx, new HydraNeckModel(ctx.bakeLayer(TFModelLayers.HYDRA_NECK)));
		EntityRenderers.register(TFEntities.HYDRA_NECK.get(), hydraNeckProvider);
		EntityRenderers.register(TFEntities.LICH.get(), LichRenderer::new);
		EntityRenderers.register(TFEntities.PENGUIN.get(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN_BABY)), 0.375F, "penguin.png"));
		EntityRenderers.register(TFEntities.LICH_MINION.get(), LichMinionRenderer::new);
		EntityRenderers.register(TFEntities.LOYAL_ZOMBIE.get(), LoyalZombieRenderer::new);
		EntityRenderers.register(TFEntities.TINY_BIRD.get(), TinyBirdRenderer::new);
		EntityRenderers.register(TFEntities.SQUIRREL.get(), SquirrelRenderer::new);
		EntityRenderers.register(TFEntities.DWARF_RABBIT.get(), BunnyRenderer::new);
		EntityRenderers.register(TFEntities.RAVEN.get(), m -> new BirdRenderer<>(m, new RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		EntityRenderers.register(TFEntities.QUEST_RAM.get(), QuestRamRenderer::new);
		EntityRenderers.register(TFEntities.KOBOLD.get(), KoboldRenderer::new);
		EntityRenderers.register(TFEntities.MOSQUITO_SWARM.get(), MosquitoSwarmRenderer::new);
		EntityRenderers.register(TFEntities.DEATH_TOME.get(), DeathTomeRenderer::new);
		EntityRenderers.register(TFEntities.MINOTAUR.get(), MinotaurRenderer::new);
		EntityRenderers.register(TFEntities.MINOSHROOM.get(), MinoshroomRenderer::new);
		EntityRenderers.register(TFEntities.FIRE_BEETLE.get(), FireBeetleRenderer::new);
		EntityRenderers.register(TFEntities.SLIME_BEETLE.get(), SlimeBeetleRenderer::new);
		EntityRenderers.register(TFEntities.PINCH_BEETLE.get(), PinchBeetleRenderer::new);
		EntityRenderers.register(TFEntities.MIST_WOLF.get(), MistWolfRenderer::new);
		EntityRenderers.register(TFEntities.CARMINITE_GHASTLING.get(), m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		EntityRenderers.register(TFEntities.CARMINITE_GOLEM.get(), CarminiteGolemRenderer::new);
		EntityRenderers.register(TFEntities.TOWERWOOD_BORER.get(), TowerwoodBorerRenderer::new);
		EntityRenderers.register(TFEntities.CARMINITE_GHASTGUARD.get(), CarminiteGhastRenderer::new);
		EntityRenderers.register(TFEntities.UR_GHAST.get(), UrGhastRenderer::new);
		EntityRenderers.register(TFEntities.BLOCKCHAIN_GOBLIN.get(), BlockChainGoblinRenderer::new);
		EntityRenderers.register(TFEntities.UPPER_GOBLIN_KNIGHT.get(), UpperGoblinKnightRenderer::new);
		EntityRenderers.register(TFEntities.LOWER_GOBLIN_KNIGHT.get(), LowerGoblinKnightRenderer::new);
		EntityRenderers.register(TFEntities.HELMET_CRAB.get(), HelmetCrabRenderer::new);
		EntityRenderers.register(TFEntities.KNIGHT_PHANTOM.get(), KnightPhantomRenderer::new);
		EntityRenderers.register(TFEntities.NAGA.get(), NagaRenderer::new);
		@SuppressWarnings({"unchecked", "rawtypes"})
		EntityRendererProvider<Entity> nagaSegmentProvider = ctx -> (EntityRenderer) new NagaSegmentRenderer(ctx, new NagaModel<>(ctx.bakeLayer(TFModelLayers.NAGA_BODY)));
		EntityRenderers.register(TFEntities.NAGA_SEGMENT.get(), nagaSegmentProvider);
		EntityRenderers.register(TFEntities.SWARM_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.25F, "swarmspider.png", 0.5F));
		EntityRenderers.register(TFEntities.KING_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 1.25F, "kingspider.png", 1.9F));
		EntityRenderers.register(TFEntities.CARMINITE_BROODLING.get(), m -> new TFSpiderRenderer<>(m, 0.6F, "towerbroodling.png", 0.7F));
		EntityRenderers.register(TFEntities.HEDGE_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.8F, "hedgespider.png", 1.0F));
		EntityRenderers.register(TFEntities.REDCAP_SAPPER.get(), RedcapSapperRenderer::new);
		EntityRenderers.register(TFEntities.MAZE_SLIME.get(), MazeSlimeRenderer::new);
		EntityRenderers.register(TFEntities.YETI.get(), YetiRenderer::new);
		EntityRenderers.register(TFEntities.PROTECTION_BOX.get(), ProtectionBoxRenderer::new);
		EntityRenderers.register(TFEntities.MAGIC_PAINTING.get(), MagicPaintingRenderer::new);
		EntityRenderers.register(TFEntities.ALPHA_YETI.get(), AlphaYetiRenderer::new);
		EntityRenderers.register(TFEntities.WINTER_WOLF.get(), WinterWolfRenderer::new);
		EntityRenderers.register(TFEntities.SNOW_GUARDIAN.get(), SnowGuardianRenderer::new);
		EntityRenderers.register(TFEntities.STABLE_ICE_CORE.get(), StableIceCoreRenderer::new);
		EntityRenderers.register(TFEntities.UNSTABLE_ICE_CORE.get(), UnstableIceCoreRenderer::new);
		EntityRenderers.register(TFEntities.SNOW_QUEEN.get(), SnowQueenRenderer::new);
		@SuppressWarnings({"unchecked", "rawtypes"})
		EntityRendererProvider<Entity> iceShieldProvider = ctx -> (EntityRenderer) new SnowQueenIceShieldRenderer(ctx);
		EntityRenderers.register(TFEntities.SNOW_QUEEN_ICE_SHIELD.get(), iceShieldProvider);
		EntityRenderers.register(TFEntities.TROLL.get(), TrollRenderer::new);
		EntityRenderers.register(TFEntities.GIANT_MINER.get(), TFGiantRenderer::new);
		EntityRenderers.register(TFEntities.ARMORED_GIANT.get(), TFGiantRenderer::new);
		EntityRenderers.register(TFEntities.ICE_CRYSTAL.get(), IceCrystalRenderer::new);
		EntityRenderers.register(TFEntities.CHAIN_BLOCK.get(), BlockChainRenderer::new);
		EntityRenderers.register(TFEntities.CUBE_OF_ANNIHILATION.get(), CubeOfAnnihilationRenderer::new);
		EntityRenderers.register(TFEntities.HARBINGER_CUBE.get(), HarbingerCubeRenderer::new);
		EntityRenderers.register(TFEntities.ADHERENT.get(), AdherentRenderer::new);
		EntityRenderers.register(TFEntities.ROVING_CUBE.get(), RovingCubeRenderer::new);
		EntityRenderers.register(TFEntities.RISING_ZOMBIE.get(), RisingZombieRenderer::new);
		EntityRenderers.register(TFEntities.PLATEAU_BOSS.get(), NoopRenderer::new);

		// Projectiles
		EntityRenderers.register(TFEntities.NATURE_BOLT.get(), ThrownItemRenderer::new);
		EntityRenderers.register(TFEntities.LICH_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		EntityRenderers.register(TFEntities.WAND_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		EntityRenderers.register(TFEntities.LICH_BOMB.get(), c -> new CustomProjectileTextureRenderer(c, net.minecraft.resources.Identifier.withDefaultNamespace("textures/item/magma_cream.png"), 1.0F, true, true));
		EntityRenderers.register(TFEntities.TOME_BOLT.get(), ThrownItemRenderer::new);
		EntityRenderers.register(TFEntities.HYDRA_MORTAR.get(), HydraMortarRenderer::new);
		EntityRenderers.register(TFEntities.SLIME_BLOB.get(), ThrownItemRenderer::new);
		EntityRenderers.register(TFEntities.MOONWORM_SHOT.get(), MoonwormShotRenderer::new);
		EntityRenderers.register(TFEntities.CHARM_EFFECT.get(), ThrownItemRenderer::new);
		EntityRenderers.register(TFEntities.THROWN_WEP.get(), ThrownWepRenderer::new);
		EntityRenderers.register(TFEntities.FALLING_ICE.get(), FallingIceRenderer::new);
		EntityRenderers.register(TFEntities.THROWN_ICE.get(), ThrownIceRenderer::new);
		EntityRenderers.register(TFEntities.THROWN_BLOCK.get(), ThrownBlockRenderer::new);
		EntityRenderers.register(TFEntities.ICE_SNOWBALL.get(), ThrownItemRenderer::new);
		EntityRenderers.register(TFEntities.SLIDER.get(), SlideBlockRenderer::new);
		EntityRenderers.register(TFEntities.SEEKER_ARROW.get(), DefaultArrowRenderer::new);
		EntityRenderers.register(TFEntities.ICE_ARROW.get(), DefaultArrowRenderer::new);

		// Boats
		EntityRenderers.register(TFEntities.TWILIGHT_OAK_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TWILIGHT_OAK_BOAT));
		EntityRenderers.register(TFEntities.TWILIGHT_OAK_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TWILIGHT_OAK_CHEST_BOAT));
		EntityRenderers.register(TFEntities.CANOPY_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.CANOPY_BOAT));
		EntityRenderers.register(TFEntities.CANOPY_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.CANOPY_CHEST_BOAT));
		EntityRenderers.register(TFEntities.MANGROVE_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MANGROVE_BOAT));
		EntityRenderers.register(TFEntities.MANGROVE_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MANGROVE_CHEST_BOAT));
		EntityRenderers.register(TFEntities.DARK_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.DARK_BOAT));
		EntityRenderers.register(TFEntities.DARK_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.DARK_CHEST_BOAT));
		EntityRenderers.register(TFEntities.TIME_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TIME_BOAT));
		EntityRenderers.register(TFEntities.TIME_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TIME_CHEST_BOAT));
		EntityRenderers.register(TFEntities.TRANSFORMATION_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TRANSFORMATION_BOAT));
		EntityRenderers.register(TFEntities.TRANSFORMATION_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TRANSFORMATION_CHEST_BOAT));
		EntityRenderers.register(TFEntities.MINING_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MINING_BOAT));
		EntityRenderers.register(TFEntities.MINING_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MINING_CHEST_BOAT));
		EntityRenderers.register(TFEntities.SORTING_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.SORTING_BOAT));
		EntityRenderers.register(TFEntities.SORTING_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.SORTING_CHEST_BOAT));
	}

	private void registerBlockEntityRenderers() {
		BlockEntityRenderers.register(TFBlockEntities.FIREFLY, FireflyRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.CICADA, CicadaRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.MOONWORM, MoonwormRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.TROPHY, TrophyRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.TF_CHEST, TFChestRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.TF_TRAPPED_CHEST, TFChestRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.SKULL_CHEST, SkullChestRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.KEEPSAKE_CASKET, KeepsakeCasketRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.SKULL_CANDLE, SkullCandleRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.REACTOR_DEBRIS, ReactorDebrisRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.RED_THREAD, RedThreadRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.CANDELABRA, CandelabraRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.JAR, JarRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.MASON_JAR, JarRenderer.MasonJarRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.OMINOUS_CANDLE, OminousCandleRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.SINISTER_SPAWNER, SinisterSpawnerRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.BRAZIER, BrazierRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.DRYING_RACK, DryingRackRenderer::new);
	}

	private void registerScreens() {
		MenuScreens.register(TFMenuTypes.UNCRAFTING, UncraftingScreen::new);
	}

	private void registerParticleFactories() {
		ParticleProviderRegistry registry = ParticleProviderRegistry.getInstance();
		registry.register(TFParticleType.LARGE_FLAME, LargeFlameParticle.Factory::new);
		registry.register(TFParticleType.LEAF_RUNE, LeafRuneParticle.Factory::new);
		registry.register(TFParticleType.BOSS_TEAR, (net.fabricmc.fabric.api.client.particle.v1.FabricSpriteSet sprite) -> new GhastTearParticle.Factory());
		registry.register(TFParticleType.GHAST_TRAP, GhastTrapParticle.Factory::new);
		registry.register(TFParticleType.PROTECTION, ProtectionParticle.Factory::new);
		registry.register(TFParticleType.SNOW, SnowParticle.Factory::new);
		registry.register(TFParticleType.SNOW_GUARDIAN, SnowGuardianParticle.Factory::new);
		registry.register(TFParticleType.SNOW_WARNING, SnowWarningParticle.SimpleFactory::new);
		registry.register(TFParticleType.EXTENDED_SNOW_WARNING, SnowWarningParticle.ExtendedFactory::new);
		registry.register(TFParticleType.ICE_BEAM, IceBeamParticle.Factory::new);
		registry.register(TFParticleType.ANNIHILATE, AnnihilateParticle.Factory::new);
		registry.register(TFParticleType.PERFECT_DODGE, PerfectDodgeParticle.Provider::new);
		registry.register(TFParticleType.DOUBLE_JUMP, DoubleJumpParticle.Provider::new);
		registry.register(TFParticleType.HUGE_SMOKE, SmokeScaleParticle.Factory::new);
		registry.register(TFParticleType.FIREFLY, FireflyParticle.StationaryProvider::new);
		registry.register(TFParticleType.WANDERING_FIREFLY, FireflyParticle.WanderingProvider::new);
		registry.register(TFParticleType.PARTICLE_SPAWNER_FIREFLY, FireflyParticle.ParticleSpawnerProvider::new);
		registry.register(TFParticleType.FALLEN_LEAF, LeafParticle.Factory::new);
		registry.register(TFParticleType.DIM_FLAME, FlameParticle.SmallFlameProvider::new);
		registry.register(TFParticleType.OMINOUS_FLAME, FlameParticle.SmallFlameProvider::new);
		registry.register(TFParticleType.SORTING_PARTICLE, SortingParticle.Factory::new);
		registry.register(TFParticleType.TRANSFORMATION_PARTICLE, TransformationParticle.Factory::new);
		registry.register(TFParticleType.LOG_CORE_PARTICLE, LogCoreParticle.Factory::new);
		registry.register(TFParticleType.CLOUD_PUFF, CloudPuffParticle.Factory::new);
		registry.register(TFParticleType.DRYING_RACK, DryingRackParticle.Provider::new);
		registry.register(TFParticleType.MAGIC_EFFECT, MagicEffectParticle.Factory::new);
		registry.register(TFParticleType.ANGRY_LICH, AngryLichParticle.Factory::new);
		registry.register(TFParticleType.TWILIGHT_ORB, sprite -> new CustomTextureParticle.Factory(sprite, true));
		registry.register(TFParticleType.SHIELD_BREAK, CustomTextureParticle.ShieldBreak::new);
	}

	private void registerClientHandlers() {
		ClientPlayNetworking.registerGlobalReceiver(AreaProtectionPacket.TYPE, AreaProtectionPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(CreateMovingCicadaSoundPacket.TYPE, CreateMovingCicadaSoundPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(EnforceProgressionStatusPacket.TYPE, EnforceProgressionStatusPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(MagicMapPacket.TYPE, MagicMapPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(MazeMapPacket.TYPE, MazeMapPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(MissingAdvancementToastPacket.TYPE, MissingAdvancementToastPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(MovePlayerPacket.TYPE, MovePlayerPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(ParticlePacket.TYPE, ParticlePacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnCharmPacket.TYPE, SpawnCharmPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(SpawnFallenLeafFromPacket.TYPE, SpawnFallenLeafFromPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(StructureProtectionPacket.TYPE, StructureProtectionPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncUncraftingTableConfigPacket.TYPE, SyncUncraftingTableConfigPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(UpdateThrownPacket.TYPE, UpdateThrownPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(LifedrainParticlePacket.TYPE, LifedrainParticlePacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(UpdateDeathTimePacket.TYPE, UpdateDeathTimePacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacketClientHandler::handleAdd);
		ClientPlayNetworking.registerGlobalReceiver(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacketClientHandler::handleUpdateStyle);
		ClientPlayNetworking.registerGlobalReceiver(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncQuestsPacket.TYPE, SyncQuestsPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(TravellersWingsStatePacket.TYPE, TravellersWingsStatePacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(UpdateTFMultipartPacket.TYPE, UpdateTFMultipartPacketClientHandler::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncUncraftingCostsPacket.TYPE, SyncUncraftingCostsPacketClientHandler::handle);

		// Bidirectional packets - client side
		ClientPlayNetworking.registerGlobalReceiver(GogglesZoomPacket.TYPE, (packet, context) ->
			context.client().execute(() -> {
				var player = context.player();
				if (player != null) {
					player.setAttached(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, packet.isUsingZoom());
				}
			}));
		ClientPlayNetworking.registerGlobalReceiver(GradualGlidePacket.TYPE, (packet, context) ->
			context.client().execute(() -> {
				var player = context.player();
				if (player != null) {
					player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, packet.isGraduallyGliding());
				}
			}));
	}

	private void registerBlockColors() {
		ColorHandler.registerBlockColors();
	}

	private void registerItemColors() {
		ColorHandler.registerItemColors();
	}
}