package twilightforest.client.event;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import twilightforest.beanification.Component;
import twilightforest.beanification.PostConstruct;
//import twilightforest.client.model.block.connected.ConnectedTextureModelLoader; // Commented out - class uses NeoForge UnbakedModelLoader
//import twilightforest.client.model.block.patch.PatchModelLoader;
import twilightforest.client.renderer.entity.layers.IceLayer;
import twilightforest.client.renderer.entity.layers.ShieldLayer;

@Component
public class ClientRegistrationEvents {

	private static boolean optifinePresent = false;

	// TODO: Port to Fabric - IEventBus is NeoForge-specific; use Fabric event system
	@PostConstruct
	private void setup() {
		// TODO: Port to Fabric - Replace NeoForge event bus listeners with Fabric event system
		// bus.addListener(EntityRenderersEvent.AddLayers.class, this::attachRenderLayers);
		// bus.addListener(this::clientSetup);
		// ... (all event registrations need Fabric equivalents)
	}

	// TODO: Port to Fabric - RegisterBlockStateModels is NeoForge-specific; use Fabric block state model registration
	private void registerBlockStateModels(Object event) {
		// TODO: Port to Fabric - event.registerModel() is NeoForge-specific
		/*
		event.registerModel(TwilightForestMod.prefix("giant_block"), UnbakedGiantBlockStateModel.MAP_CODEC);
		event.registerModel(TwilightForestMod.prefix("noise_varying"), UnbakedNoiseVaryingBlockStateModel.MAP_CODEC);
		event.registerModel(TwilightForestMod.prefix("force_field"), UnbakedForceFieldBlockStateModel.MAP_CODEC);
		event.registerModel(TwilightForestMod.prefix("reactor_debris"), UnbakedReactorDebrisBlockStateModel.MAP_CODEC);
		event.registerModel(TwilightForestMod.prefix("royal_rags"), UnbakedRoyalRagsBlockStateModel.MAP_CODEC);
		event.registerModel(TwilightForestMod.prefix("connected_texture_block"), ConnectedTextureBlockStateModel.MAP_CODEC);
		event.registerModel(TwilightForestMod.prefix("patch"), UnbakedPatchBlockStateModel.MAP_CODEC);
		*/
	}

	// TODO: Port to Fabric - RegisterItemModelsEvent is NeoForge-specific
	private void registerItemModels(Object event) {
	}

	// TODO: Port to Fabric - RegisterSpecialModelRendererEvent is NeoForge-specific
	private void registerSpecialModelRenderers(Object event) {
	}

	// TODO: Port to Fabric - ModelEvent.RegisterLoaders and UnbakedModelLoader are NeoForge-specific
	private void registerModelLoaders(Object event) {
	}

	// TODO: Port to Fabric - RegisterConditionalItemModelPropertyEvent is NeoForge-specific
	private void registerConditionalProperties(Object event) {
	}

	// TODO: Port to Fabric - RegisterRangeSelectItemModelPropertyEvent is NeoForge-specific
	private void registerRangeProperties(Object event) {
	}

	// TODO: Port to Fabric - RegisterSelectItemModelPropertyEvent is NeoForge-specific
	private void registerSelectProperties(Object event) {
	}

	// TODO: Port to Fabric - ModelEvent.RegisterStandalone is NeoForge-specific; use Fabric model registration API
	private void registerJarLidModels(Object event) {
		// TODO: Port to Fabric - StandaloneModelKey and related classes are NeoForge-specific
		/*
		for (JarRenderer.LidResource lid : JarRenderer.LID_LOCATION_LIST.get()) {
			StandaloneModelKey<BlockModel> key = lid.createKey();
			JarRenderer.LID_KEYS.put(lid.lid(), key);
			event.register(key, new SimpleUnbakedStandaloneModel<>(
				lid.getModelId(),
				(model, baker, name) -> {
					var part = SimpleModelWrapper.bake(baker, model, BlockModelRotation.IDENTITY);
					BlockStateModel stateModel = new SingleVariant(part);
					return new BlockStateModelWrapper(stateModel, java.util.List.of(), new Matrix4f());
				}
			));
		}
		*/
	}

	// TODO: Port to Fabric - FMLClientSetupEvent is NeoForge-specific; use Fabric entrypoint or mod initializer
	private void clientSetup(Object evt) {
		try {
			Class.forName("net.optifine.Config");
			optifinePresent = true;
		} catch (ClassNotFoundException e) {
			optifinePresent = false;
		}

		// evt.enqueueWork(() -> { Sheets.addWoodType(...); }); - needs Fabric equivalent
	}

	// TODO: Port to Fabric - RegisterTextureAtlasesEvent is NeoForge-specific
	private void registerAtlases(Object event) {
	}

	// TODO: Port to Fabric - AddClientReloadListenersEvent is NeoForge-specific; use Fabric ResourceManagerHelper
	private void registerClientReloadListeners(Object event) {
		// TODO: Port to Fabric - Use ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(...) instead
		/*
		event.addListener(TwilightForestMod.prefix("texture_generator"), TextureGeneratorReloadListener.INSTANCE);
		event.addListener(TwilightForestMod.prefix("armor_cache"), new TFArmorRenderer.ResourceReloadListener());
		*/
	}

	// TODO: Port to Fabric - RegisterMenuScreensEvent is NeoForge-specific
	private void registerScreens(Object event) {
	}

	// TODO: Port to Fabric - EntityRenderersEvent.RegisterRenderers is NeoForge-specific; use Fabric EntityRendererRegistry
	private void registerEntityRenderers(Object event) {
		// TODO: Port to Fabric - Replace with EntityRendererRegistry.register(entityType, rendererProvider)
		/*
		event.registerEntityRenderer(TFEntities.BOAR.get(), BoarRenderer::new);
		event.registerEntityRenderer(TFEntities.BIGHORN_SHEEP.get(), BighornRenderer::new);
		event.registerEntityRenderer(TFEntities.DEER.get(), DeerRenderer::new);
		event.registerEntityRenderer(TFEntities.REDCAP.get(), RedcapRenderer::new);
		event.registerEntityRenderer(TFEntities.SKELETON_DRUID.get(), SkeletonDruidRenderer::new);
		event.registerEntityRenderer(TFEntities.HOSTILE_WOLF.get(), HostileWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.WRAITH.get(), WraithRenderer::new);
		event.registerEntityRenderer(TFEntities.HYDRA.get(), HydraRenderer::new);
		event.registerEntityRenderer(TFEntities.LICH.get(), LichRenderer::new);
		event.registerEntityRenderer(TFEntities.PENGUIN.get(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN_BABY)), 0.375F, "penguin.png"));
		event.registerEntityRenderer(TFEntities.LICH_MINION.get(), LichMinionRenderer::new);
		event.registerEntityRenderer(TFEntities.LOYAL_ZOMBIE.get(), LoyalZombieRenderer::new);
		event.registerEntityRenderer(TFEntities.TINY_BIRD.get(), TinyBirdRenderer::new);
		event.registerEntityRenderer(TFEntities.SQUIRREL.get(), SquirrelRenderer::new);
		event.registerEntityRenderer(TFEntities.DWARF_RABBIT.get(), BunnyRenderer::new);
		event.registerEntityRenderer(TFEntities.RAVEN.get(), m -> new BirdRenderer<>(m, new RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		event.registerEntityRenderer(TFEntities.QUEST_RAM.get(), QuestRamRenderer::new);
		event.registerEntityRenderer(TFEntities.KOBOLD.get(), KoboldRenderer::new);
		//event.registerEntityRenderer(TFEntities.BOGGARD.get(), m -> new RenderTFBiped<>(m, new BipedModel<>(0), 0.625F, "kobold.png"));
		event.registerEntityRenderer(TFEntities.MOSQUITO_SWARM.get(), MosquitoSwarmRenderer::new);
		event.registerEntityRenderer(TFEntities.DEATH_TOME.get(), DeathTomeRenderer::new);
		event.registerEntityRenderer(TFEntities.MINOTAUR.get(), MinotaurRenderer::new);
		event.registerEntityRenderer(TFEntities.MINOSHROOM.get(), MinoshroomRenderer::new);
		event.registerEntityRenderer(TFEntities.FIRE_BEETLE.get(), FireBeetleRenderer::new);
		event.registerEntityRenderer(TFEntities.SLIME_BEETLE.get(), SlimeBeetleRenderer::new);
		event.registerEntityRenderer(TFEntities.PINCH_BEETLE.get(), PinchBeetleRenderer::new);
		event.registerEntityRenderer(TFEntities.MIST_WOLF.get(), MistWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.CARMINITE_GHASTLING.get(), m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		event.registerEntityRenderer(TFEntities.CARMINITE_GOLEM.get(), CarminiteGolemRenderer::new);
		event.registerEntityRenderer(TFEntities.TOWERWOOD_BORER.get(), TowerwoodBorerRenderer::new);
		event.registerEntityRenderer(TFEntities.CARMINITE_GHASTGUARD.get(), CarminiteGhastRenderer::new);
		event.registerEntityRenderer(TFEntities.UR_GHAST.get(), UrGhastRenderer::new);
		event.registerEntityRenderer(TFEntities.BLOCKCHAIN_GOBLIN.get(), BlockChainGoblinRenderer::new);
		event.registerEntityRenderer(TFEntities.UPPER_GOBLIN_KNIGHT.get(), UpperGoblinKnightRenderer::new);
		event.registerEntityRenderer(TFEntities.LOWER_GOBLIN_KNIGHT.get(), LowerGoblinKnightRenderer::new);
		event.registerEntityRenderer(TFEntities.HELMET_CRAB.get(), HelmetCrabRenderer::new);
		event.registerEntityRenderer(TFEntities.KNIGHT_PHANTOM.get(), KnightPhantomRenderer::new);
		event.registerEntityRenderer(TFEntities.NAGA.get(), NagaRenderer::new);
		event.registerEntityRenderer(TFEntities.SWARM_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.25F, "swarmspider.png", 0.5F));
		event.registerEntityRenderer(TFEntities.KING_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 1.25F, "kingspider.png", 1.9F));
		event.registerEntityRenderer(TFEntities.CARMINITE_BROODLING.get(), m -> new TFSpiderRenderer<>(m, 0.6F, "towerbroodling.png", 0.7F));
		event.registerEntityRenderer(TFEntities.HEDGE_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.8F, "hedgespider.png", 1.0F));
		event.registerEntityRenderer(TFEntities.REDCAP_SAPPER.get(), RedcapSapperRenderer::new);
		event.registerEntityRenderer(TFEntities.MAZE_SLIME.get(), MazeSlimeRenderer::new);
		event.registerEntityRenderer(TFEntities.YETI.get(), YetiRenderer::new);
		event.registerEntityRenderer(TFEntities.PROTECTION_BOX.get(), ProtectionBoxRenderer::new);
		event.registerEntityRenderer(TFEntities.MAGIC_PAINTING.get(), MagicPaintingRenderer::new);
		event.registerEntityRenderer(TFEntities.ALPHA_YETI.get(), AlphaYetiRenderer::new);
		event.registerEntityRenderer(TFEntities.WINTER_WOLF.get(), WinterWolfRenderer::new);
		event.registerEntityRenderer(TFEntities.SNOW_GUARDIAN.get(), SnowGuardianRenderer::new);
		event.registerEntityRenderer(TFEntities.STABLE_ICE_CORE.get(), StableIceCoreRenderer::new);
		event.registerEntityRenderer(TFEntities.UNSTABLE_ICE_CORE.get(), UnstableIceCoreRenderer::new);
		event.registerEntityRenderer(TFEntities.SNOW_QUEEN.get(), SnowQueenRenderer::new);
		event.registerEntityRenderer(TFEntities.TROLL.get(), TrollRenderer::new);
		event.registerEntityRenderer(TFEntities.GIANT_MINER.get(), TFGiantRenderer::new);
		event.registerEntityRenderer(TFEntities.ARMORED_GIANT.get(), TFGiantRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_CRYSTAL.get(), IceCrystalRenderer::new);
		event.registerEntityRenderer(TFEntities.CHAIN_BLOCK.get(), BlockChainRenderer::new);
		event.registerEntityRenderer(TFEntities.CUBE_OF_ANNIHILATION.get(), CubeOfAnnihilationRenderer::new);
		event.registerEntityRenderer(TFEntities.HARBINGER_CUBE.get(), HarbingerCubeRenderer::new);
		event.registerEntityRenderer(TFEntities.ADHERENT.get(), AdherentRenderer::new);
		event.registerEntityRenderer(TFEntities.ROVING_CUBE.get(), RovingCubeRenderer::new);
		event.registerEntityRenderer(TFEntities.RISING_ZOMBIE.get(), RisingZombieRenderer::new);
		event.registerEntityRenderer(TFEntities.PLATEAU_BOSS.get(), NoopRenderer::new);

		// projectiles
		event.registerEntityRenderer(TFEntities.NATURE_BOLT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.LICH_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		event.registerEntityRenderer(TFEntities.WAND_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		event.registerEntityRenderer(TFEntities.LICH_BOMB.get(), c -> new CustomProjectileTextureRenderer(c, Identifier.withDefaultNamespace("textures/item/magma_cream.png"), 1.0F, true, true));
		event.registerEntityRenderer(TFEntities.TOME_BOLT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.HYDRA_MORTAR.get(), HydraMortarRenderer::new);
		event.registerEntityRenderer(TFEntities.SLIME_BLOB.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.MOONWORM_SHOT.get(), MoonwormShotRenderer::new);
		event.registerEntityRenderer(TFEntities.CHARM_EFFECT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_WEP.get(), ThrownWepRenderer::new);
		event.registerEntityRenderer(TFEntities.FALLING_ICE.get(), FallingIceRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_ICE.get(), ThrownIceRenderer::new);
		event.registerEntityRenderer(TFEntities.THROWN_BLOCK.get(), ThrownBlockRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_SNOWBALL.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TFEntities.SLIDER.get(), SlideBlockRenderer::new);
		event.registerEntityRenderer(TFEntities.SEEKER_ARROW.get(), DefaultArrowRenderer::new);
		event.registerEntityRenderer(TFEntities.ICE_ARROW.get(), DefaultArrowRenderer::new);

		// Boats
		event.registerEntityRenderer(TFEntities.TWILIGHT_OAK_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TWILIGHT_OAK_BOAT));
		event.registerEntityRenderer(TFEntities.TWILIGHT_OAK_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TWILIGHT_OAK_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.CANOPY_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.CANOPY_BOAT));
		event.registerEntityRenderer(TFEntities.CANOPY_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.CANOPY_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.MANGROVE_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MANGROVE_BOAT));
		event.registerEntityRenderer(TFEntities.MANGROVE_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MANGROVE_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.DARK_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.DARK_BOAT));
		event.registerEntityRenderer(TFEntities.DARK_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.DARK_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.TIME_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TIME_BOAT));
		event.registerEntityRenderer(TFEntities.TIME_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TIME_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.TRANSFORMATION_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TRANSFORMATION_BOAT));
		event.registerEntityRenderer(TFEntities.TRANSFORMATION_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.TRANSFORMATION_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.MINING_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MINING_BOAT));
		event.registerEntityRenderer(TFEntities.MINING_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.MINING_CHEST_BOAT));
		event.registerEntityRenderer(TFEntities.SORTING_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.SORTING_BOAT));
		event.registerEntityRenderer(TFEntities.SORTING_CHEST_BOAT.get(), context -> new BoatRenderer(context, TFModelLayers.SORTING_CHEST_BOAT));

		// Block Entities
		event.registerBlockEntityRenderer(TFBlockEntities.FIREFLY.get(), FireflyRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.CICADA.get(), CicadaRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.MOONWORM.get(), MoonwormRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TROPHY.get(), TrophyRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TF_CHEST.get(), TFChestRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.TF_TRAPPED_CHEST.get(), TFChestRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.SKULL_CHEST.get(), SkullChestRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.KEEPSAKE_CASKET.get(), KeepsakeCasketRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.SKULL_CANDLE.get(), SkullCandleRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.REACTOR_DEBRIS.get(), ReactorDebrisRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.RED_THREAD.get(), RedThreadRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.CANDELABRA.get(), CandelabraRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.JAR.get(), JarRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.MASON_JAR.get(), JarRenderer.MasonJarRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.OMINOUS_CANDLE.get(), OminousCandleRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.SINISTER_SPAWNER.get(), SinisterSpawnerRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.BRAZIER.get(), BrazierRenderer::new);
		event.registerBlockEntityRenderer(TFBlockEntities.DRYING_RACK.get(), DryingRackRenderer::new);
		*/
	}

	// TODO: Port to Fabric - EntityRenderersEvent.RegisterLayerDefinitions is NeoForge-specific; use Fabric EntityModelLayerRegistry
	private void registerLayerDefinitions(Object event) {
		// TODO: Port to Fabric - Use EntityModelLayerRegistry.registerModelLayer() instead
		/*
		event.registerLayerDefinition(TFModelLayers.ARCTIC_ARMOR_INNER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.ARCTIC_ARMOR_OUTER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.FIERY_ARMOR_INNER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.FIERY_ARMOR_OUTER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.TRAVELLERS_ARMOR_HELMET, () -> LayerDefinition.create(TravellersGearModels.addGogglePieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.295F), false), 64, 32)); // TODO: reduce to 0.25F (+ dx?) without z-fighting in the player's inventory view
		event.registerLayerDefinition(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.295F), true), 64, 32));
		event.registerLayerDefinition(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, () -> TravellersWingsModel.createLayer(0.25F));
		event.registerLayerDefinition(TFModelLayers.TRAVELLERS_ARMOR_BOOTS, () -> LayerDefinition.create(TravellersGearModels.addBootPieces(new CubeDeformation(0.5F)), 64, 32));
		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_ARMOR_INNER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_ARMOR_OUTER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.PHANTOM_ARMOR_INNER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.PHANTOM_ARMOR_OUTER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.YETI_ARMOR_INNER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(TFModelLayers.YETI_ARMOR_OUTER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));

		event.registerLayerDefinition(TFModelLayers.ALPHA_YETI_TROPHY, AlphaYetiModel::createTrophy);
		event.registerLayerDefinition(TFModelLayers.HYDRA_TROPHY, HydraHeadModel::create);
		event.registerLayerDefinition(TFModelLayers.KNIGHT_PHANTOM_TROPHY, KnightPhantomModel::createTrophy);
		event.registerLayerDefinition(TFModelLayers.LICH_TROPHY, LichModel::create);
		event.registerLayerDefinition(TFModelLayers.MINOSHROOM_TROPHY, MinoshroomModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA_TROPHY, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.QUEST_RAM_TROPHY, QuestRamModel::create);
		event.registerLayerDefinition(TFModelLayers.SNOW_QUEEN_TROPHY, SnowQueenModel::create);
		event.registerLayerDefinition(TFModelLayers.UR_GHAST_TROPHY, UrGhastModel::create);

		event.registerLayerDefinition(TFModelLayers.ADHERENT, AdherentModel::create);
		event.registerLayerDefinition(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create);
		event.registerLayerDefinition(TFModelLayers.ARMORED_GIANT, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP, BighornModel::create);
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP_BABY, () -> BighornModel.create().apply(BighornModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP_WOOL, SheepFurModel::createFurLayer);
		event.registerLayerDefinition(TFModelLayers.BIGHORN_SHEEP_BABY_WOOL, () -> SheepFurModel.createFurLayer().apply(new BabyModelTransform(false, 8.0F, 6.0F, Set.of("head"))));
		event.registerLayerDefinition(TFModelLayers.BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::create);
		event.registerLayerDefinition(TFModelLayers.BOAR, BoarModel::create);
		event.registerLayerDefinition(TFModelLayers.BOAR_BABY, () -> BoarModel.create().apply(BoarModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(TFModelLayers.BUNNY, BunnyModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.CHAIN, ChainModel::create);
		event.registerLayerDefinition(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		event.registerLayerDefinition(TFModelLayers.DEER, DeerModel::create);
		event.registerLayerDefinition(TFModelLayers.DEER_BABY, () -> DeerModel.create().apply(DeerModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(TFModelLayers.FIRE_BEETLE, FireBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		event.registerLayerDefinition(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.HELMET_CRAB, HelmetCrabModel::create);
		event.registerLayerDefinition(TFModelLayers.HOSTILE_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.HYDRA_HEAD, HydraHeadModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA, HydraModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create);
		event.registerLayerDefinition(TFModelLayers.HYDRA_NECK, HydraNeckModel::create);
		event.registerLayerDefinition(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create);
		event.registerLayerDefinition(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create);
		event.registerLayerDefinition(TFModelLayers.KOBOLD, KoboldModel::create);
		event.registerLayerDefinition(TFModelLayers.LICH_MINION, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.LICH, LichModel::create);
		event.registerLayerDefinition(TFModelLayers.LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::create);
		event.registerLayerDefinition(TFModelLayers.LOYAL_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
		event.registerLayerDefinition(TFModelLayers.MINOSHROOM, MinoshroomModel::create);
		event.registerLayerDefinition(TFModelLayers.MINOTAUR, MinotaurModel::create);
		event.registerLayerDefinition(TFModelLayers.MIST_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NAGA_BODY, NagaModel::create);
		event.registerLayerDefinition(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		event.registerLayerDefinition(TFModelLayers.PENGUIN, PenguinModel::create);
		event.registerLayerDefinition(TFModelLayers.PENGUIN_BABY, () -> PenguinModel.create().apply(PenguinModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(TFModelLayers.PINCH_BEETLE, PinchBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.PROTECTION_BOX, () -> LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
		event.registerLayerDefinition(TFModelLayers.QUEST_RAM, QuestRamModel::create);
		event.registerLayerDefinition(TFModelLayers.RAVEN, RavenModel::create);
		event.registerLayerDefinition(TFModelLayers.REDCAP, RedcapModel::create);
		event.registerLayerDefinition(TFModelLayers.REDCAP_ARMOR_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.7F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.REDCAP_ARMOR_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.65F), 0.7F), 64, 32));
		event.registerLayerDefinition(TFModelLayers.RISING_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		event.registerLayerDefinition(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create);
		event.registerLayerDefinition(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create);
		event.registerLayerDefinition(TFModelLayers.SLIME_BEETLE, SlimeBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.SLIME_BEETLE_TAIL, SlimeBeetleModel::create);
		event.registerLayerDefinition(TFModelLayers.SNOW_QUEEN, SnowQueenModel::create);
		event.registerLayerDefinition(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create);
		event.registerLayerDefinition(TFModelLayers.SQUIRREL, SquirrelModel::create);
		event.registerLayerDefinition(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create);
		event.registerLayerDefinition(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
		event.registerLayerDefinition(TFModelLayers.TINY_BIRD, TinyBirdModel::create);
		event.registerLayerDefinition(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
		event.registerLayerDefinition(TFModelLayers.TROLL, TrollModel::create);
		event.registerLayerDefinition(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
		event.registerLayerDefinition(TFModelLayers.UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::create);
		event.registerLayerDefinition(TFModelLayers.UR_GHAST, UrGhastModel::create);
		event.registerLayerDefinition(TFModelLayers.WINTER_WOLF, () -> LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32));
		event.registerLayerDefinition(TFModelLayers.WRAITH, WraithModel::create);
		event.registerLayerDefinition(TFModelLayers.YETI, YetiModel::create);

		event.registerLayerDefinition(TFModelLayers.CICADA, CicadaModel::create);
		event.registerLayerDefinition(TFModelLayers.FIREFLY, FireflyModel::create);
		event.registerLayerDefinition(TFModelLayers.KEEPSAKE_CASKET, () -> KeepsakeCasketModel.create(true));
		event.registerLayerDefinition(TFModelLayers.SKULL_CHEST, () -> KeepsakeCasketModel.create(false));
		event.registerLayerDefinition(TFModelLayers.MOONWORM, MoonwormModel::create);
		event.registerLayerDefinition(TFModelLayers.BRAZIER, BrazierModel::create);

		event.registerLayerDefinition(TFModelLayers.RED_THREAD, RedThreadModel::create);

		event.registerLayerDefinition(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);

		// Boats
		event.registerLayerDefinition(TFModelLayers.TWILIGHT_OAK_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.TWILIGHT_OAK_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.CANOPY_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.CANOPY_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.MANGROVE_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.MANGROVE_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.DARK_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.DARK_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.TIME_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.TIME_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.TRANSFORMATION_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.TRANSFORMATION_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.MINING_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.MINING_CHEST_BOAT, BoatModel::createChestBoatModel);
		event.registerLayerDefinition(TFModelLayers.SORTING_BOAT, BoatModel::createBoatModel);
		event.registerLayerDefinition(TFModelLayers.SORTING_CHEST_BOAT, BoatModel::createChestBoatModel);
		*/
	}

	// TODO: Port to Fabric - RegisterParticleProvidersEvent is NeoForge-specific; use Fabric ParticleFactoryRegistry
	private void registerParticleFactories(Object event) {
		// TODO: Port to Fabric - Use ParticleFactoryRegistry.getInstance().register() instead
		/*
		event.registerSpriteSet(TFParticleType.LARGE_FLAME.get(), LargeFlameParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.LEAF_RUNE.get(), LeafRuneParticle.Factory::new);
		event.registerSpecial(TFParticleType.BOSS_TEAR.get(), new GhastTearParticle.Factory());
		event.registerSpriteSet(TFParticleType.GHAST_TRAP.get(), GhastTrapParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.PROTECTION.get(), ProtectionParticle.Factory::new); //probably not a good idea, but worth a shot
		event.registerSpriteSet(TFParticleType.SNOW.get(), SnowParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.SNOW_GUARDIAN.get(), SnowGuardianParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.SNOW_WARNING.get(), SnowWarningParticle.SimpleFactory::new);
		event.registerSpriteSet(TFParticleType.EXTENDED_SNOW_WARNING.get(), SnowWarningParticle.ExtendedFactory::new);
		event.registerSpriteSet(TFParticleType.ICE_BEAM.get(), IceBeamParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.ANNIHILATE.get(), AnnihilateParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.PERFECT_DODGE.get(), PerfectDodgeParticle.Provider::new);
		event.registerSpriteSet(TFParticleType.DOUBLE_JUMP.get(), DoubleJumpParticle.Provider::new);
		event.registerSpriteSet(TFParticleType.HUGE_SMOKE.get(), SmokeScaleParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.FIREFLY.get(), FireflyParticle.StationaryProvider::new);
		event.registerSpriteSet(TFParticleType.WANDERING_FIREFLY.get(), FireflyParticle.WanderingProvider::new);
		event.registerSpriteSet(TFParticleType.PARTICLE_SPAWNER_FIREFLY.get(), FireflyParticle.ParticleSpawnerProvider::new);
		event.registerSpriteSet(TFParticleType.FALLEN_LEAF.get(), LeafParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.DIM_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		event.registerSpriteSet(TFParticleType.OMINOUS_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		event.registerSpriteSet(TFParticleType.SORTING_PARTICLE.get(), SortingParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.TRANSFORMATION_PARTICLE.get(), TransformationParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.LOG_CORE_PARTICLE.get(), LogCoreParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.CLOUD_PUFF.get(), CloudPuffParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.DRYING_RACK.get(), DryingRackParticle.Provider::new);
		event.registerSpriteSet(TFParticleType.MAGIC_EFFECT.get(), MagicEffectParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.ANGRY_LICH.get(), AngryLichParticle.Factory::new);
		event.registerSpriteSet(TFParticleType.TWILIGHT_ORB.get(), (SpriteSet sprite) -> new CustomTextureParticle.Factory(sprite, true));
		event.registerSpriteSet(TFParticleType.SHIELD_BREAK.get(), CustomTextureParticle.ShieldBreak::new);
		*/
	}

	// TODO: Port to Fabric - RegisterClientExtensionsEvent and IClientBlockExtensions are NeoForge-specific
	private void registerClientExtensions(Object event) {
		/*
			@Override
			public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
				if (level.getRandom().nextBoolean() && target instanceof BlockHitResult hitResult) { // No clue why the parameter isn't blockHitResult, this should be always true, but we check just in case
					BlockPos pos = hitResult.getBlockPos();
					BlockState blockstate = level.getBlockState(pos);
					if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
						Direction side = hitResult.getDirection();

						int posX = pos.getX();
						int posY = pos.getY();
						int posZ = pos.getZ();

						AABB aabb = blockstate.getShape(level, pos).bounds();
						double x = (double) posX + level.getRandom().nextDouble() * (aabb.maxX - aabb.minX - (double) 0.2F) + (double) 0.1F + aabb.minX;
						double y = (double) posY + level.getRandom().nextDouble() * (aabb.maxY - aabb.minY - (double) 0.2F) + (double) 0.1F + aabb.minY;
						double z = (double) posZ + level.getRandom().nextDouble() * (aabb.maxZ - aabb.minZ - (double) 0.2F) + (double) 0.1F + aabb.minZ;

						if (side == Direction.DOWN) y = (double) posY + aabb.minY - (double) 0.1F;
						if (side == Direction.UP) y = (double) posY + aabb.maxY + (double) 0.1F;

						if (side == Direction.NORTH) z = (double) posZ + aabb.minZ - (double) 0.1F;
						if (side == Direction.SOUTH) z = (double) posZ + aabb.maxZ + (double) 0.1F;

						if (side == Direction.WEST) x = (double) posX + aabb.minX - (double) 0.1F;
						if (side == Direction.EAST) x = (double) posX + aabb.maxX + (double) 0.1F;

						Particle particle = Minecraft.getInstance().particleEngine.createParticle(TFParticleType.CLOUD_PUFF.get(), x, y, z, (double) side.getStepX() * 0.01D, (double) side.getStepY() * 0.01D, (double) side.getStepZ() * 0.01D);
						if (particle == null) return true;
						manager.add(particle);
					}
				}
				return true;
			}

			@Override
			public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager) {
				state.getShape(level, pos).forAllBoxes((boxX, boxY, boxZ, boxX1, boxY1, boxZ1) -> {
					double xSize = Math.min(1.0D, boxX1 - boxX);
					double ySize = Math.min(1.0D, boxY1 - boxY);
					double zSize = Math.min(1.0D, boxZ1 - boxZ);

					int xMax = Math.max(2, Mth.ceil(xSize / 0.25D));
					int yMax = Math.max(2, Mth.ceil(ySize / 0.25D));
					int zMax = Math.max(2, Mth.ceil(zSize / 0.25D));

					for (int xSlice = 0; xSlice < xMax; ++xSlice) {
						if (level.getRandom().nextInt(3) == 1) continue;
						for (int ySlice = 0; ySlice < yMax; ++ySlice) {
							if (level.getRandom().nextInt(3) == 1) continue;
							for (int zSlice = 0; zSlice < zMax; ++zSlice) {
								if (level.getRandom().nextInt(3) == 1) continue;

								double speedX = ((double) xSlice + 0.5D) / (double) xMax;
								double speedY = ((double) ySlice + 0.5D) / (double) yMax;
								double speedZ = ((double) zSlice + 0.5D) / (double) zMax;

								double x = speedX * xSize + boxX;
								double y = speedY * ySize + boxY;
								double z = speedZ * zSize + boxZ;

								speedX = (speedX - 0.5D) * 0.05D;
								speedY = (speedY - 0.5D) * 0.05D;
								speedZ = (speedZ - 0.5D) * 0.05D;

								Particle particle = Minecraft.getInstance().particleEngine.createParticle(TFParticleType.CLOUD_PUFF.get(), (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, speedX, speedY, speedZ);
								if (particle == null) return;
								manager.add(particle);
							}
						}
					}
				});
				return true;
			}
		}, TFBlocks.WISPY_CLOUD.get(), TFBlocks.RAINY_CLOUD.get(), TFBlocks.SNOWY_CLOUD.get(), TFBlocks.FLUFFY_CLOUD.get());

		event.registerItem(
			new ArcticArmorRenderer(),
			TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get());
		event.registerItem(
			new TFSimpleArmorRenderer(FieryArmorModel::new, TFModelLayers.FIERY_ARMOR_INNER, TFModelLayers.FIERY_ARMOR_OUTER),
			TFItems.FIERY_HELMET.get(), TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get()
		);
		event.registerItem(
			new TravellersArmorRenderer(),
			TFItems.TRAVELLERS_GOGGLES.get(), TFItems.TRAVELLERS_VEST.get(), TFItems.TRAVELLERS_GLOVES.get(), TFItems.TRAVELLERS_WINGS.get(), TFItems.TRAVELLERS_BELT.get(), TFItems.TRAVELLERS_BOOTS.get()
		);
		event.registerItem(
			new TFSimpleArmorRenderer(TFArmorModel::new, TFModelLayers.KNIGHTMETAL_ARMOR_INNER, TFModelLayers.KNIGHTMETAL_ARMOR_OUTER),
			TFItems.KNIGHTMETAL_HELMET.get(), TFItems.KNIGHTMETAL_CHESTPLATE.get(), TFItems.KNIGHTMETAL_LEGGINGS.get(), TFItems.KNIGHTMETAL_BOOTS.get()
		);
		event.registerItem(
			new TFSimpleArmorRenderer(TFArmorModel::new, TFModelLayers.PHANTOM_ARMOR_INNER, TFModelLayers.PHANTOM_ARMOR_OUTER),
			TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get()
		);
		event.registerItem(
			new TFSimpleArmorRenderer(YetiArmorModel::new, TFModelLayers.YETI_ARMOR_INNER, TFModelLayers.YETI_ARMOR_OUTER),
			TFItems.YETI_HELMET.get(), TFItems.YETI_CHESTPLATE.get(), TFItems.YETI_LEGGINGS.get(), TFItems.YETI_BOOTS.get()
		);
		*/
	}

	// TODO: Port to Fabric - RegisterMapDecorationRenderersEvent is NeoForge-specific
	public static void registerMapDecorators(Object event) {
	}

	// TODO: Port to Fabric - EntityRenderersEvent.AddLayers is NeoForge-specific; use Fabric event system
	@SuppressWarnings({"unchecked", "rawtypes"})
	private void attachRenderLayers(Object event) {
		// TODO: Port to Fabric - Replace with Fabric entity renderer layer attachment
		/*
		BakedMultiPartRenderers.bakeMultiPartRenderers(event.getContext());
		for (EntityType<?> type : event.getEntityTypes()) {
			var renderer = event.getRenderer(type);
			if (renderer instanceof LivingEntityRenderer living) {
				attachRenderLayers(living);
			}
		}

		event.getSkins().forEach(renderer -> {
			AvatarRenderer<AbstractClientPlayer> skin = event.getPlayerRenderer(renderer);
			attachRenderLayers(Objects.requireNonNull(skin));
		});
		*/
	}

	private <T extends LivingEntityRenderState, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<?, T, M> renderer) {
		renderer.addLayer(new ShieldLayer<>(renderer));
		renderer.addLayer(new IceLayer<>(renderer));
	}

	// TODO: Port to Fabric - RegisterCustomEnvironmentEffectRendererEvent is NeoForge-specific
	private void registerCustomEnvironmentEffects(Object event) {
	}

	public static boolean isOptifinePresent() {
		return optifinePresent;
	}
}
