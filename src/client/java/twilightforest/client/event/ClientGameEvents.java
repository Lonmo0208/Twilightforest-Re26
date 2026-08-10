package twilightforest.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.beanification.Autowired;
import twilightforest.beanification.PostConstruct;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.GrowingBeanstalkBlockEntity;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.TFShaders;
import twilightforest.client.renderer.entity.MagicPaintingRenderer;
import twilightforest.config.TFConfig;
import twilightforest.events.HostileMountEvents;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFDimension;
import twilightforest.init.TFItems;
import twilightforest.item.MoonDialItem;
import twilightforest.util.HolderMatcher;

import java.util.HashSet;
import java.util.List;

@twilightforest.beanification.Component
public class ClientGameEvents {
	private final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);
	private final MutableComponent WIP_TEXT = Component.translatable("misc.twilightforest.wip").withStyle(ChatFormatting.RED);
	private final MutableComponent EMPERORS_CLOTH_TOOLTIP = Component.translatable("item.twilightforest.emperors_cloth.desc").withStyle(ChatFormatting.GRAY);

	private boolean firstTitleScreenShown = false;

	public static int time = 0;
	private float shakeIntensity = 0.0F;

	private int aurora = 0;
	private int lastAurora = 0;

	private final HolderMatcher holderMatcher;

	// Default constructor for Beanification
	public ClientGameEvents(HolderMatcher holderMatcher) {
		this.holderMatcher = holderMatcher;
	}

	// Convenience constructor
	public ClientGameEvents() {
		this.holderMatcher = new HolderMatcher();
	}

	// TODO: Port to Fabric - NeoForge.EVENT_BUS and all NeoForge event types need Fabric equivalents
	@PostConstruct
	private void setup() {
		// All event registrations are NeoForge-specific and need to be ported to Fabric
		// NeoForge.EVENT_BUS.addListener(this::addCustomTooltips);
		// NeoForge.EVENT_BUS.addListener(this::clientTick);
		// NeoForge.EVENT_BUS.addListener(this::customizeSplashes);
		// NeoForge.EVENT_BUS.addListener(this::clearEntityRenderUtilMap);
		// NeoForge.EVENT_BUS.addListener(this::handleGameBootup);
		// NeoForge.EVENT_BUS.addListener(this::killVignette);
		// NeoForge.EVENT_BUS.addListener(this::removeHostileMountHealth);
		// NeoForge.EVENT_BUS.addListener(RenderLevelStageEvent.AfterTranslucentParticles.class, this::renderAurora);
		// NeoForge.EVENT_BUS.addListener(this::renderCustomBossbars);
		// NeoForge.EVENT_BUS.addListener(this::renderGiantBlockOutlines);
		// NeoForge.EVENT_BUS.addListener(this::setMusicInDimension);
		// NeoForge.EVENT_BUS.addListener(this::shakeCamera);
		// NeoForge.EVENT_BUS.addListener(this::translateBookContents);
		// NeoForge.EVENT_BUS.addListener(this::updateBowFOV);
		// NeoForge.EVENT_BUS.addListener(RenderLevelStageEvent.AfterTranslucentParticles.class, CloudEvents::renderPrecipitation);
		// NeoForge.EVENT_BUS.addListener(CloudEvents::tickWeatherEffects);
		// NeoForge.EVENT_BUS.addListener(FogHandler::renderFog);
		// NeoForge.EVENT_BUS.addListener(FogHandler::unloadFog);
		// NeoForge.EVENT_BUS.addListener(LockedBiomeToastHandler::tickLockedToastLogic);
		// NeoForge.EVENT_BUS.addListener(TFSkyRenderer::extractLevelRender);
	}

	// TODO: Port to Fabric - ScreenEvent.Init.Post is NeoForge-specific
	/*
	private void handleGameBootup(ScreenEvent.Init.Post event) {
		...
	}
	*/

	// TODO: Port to Fabric - ScreenEvent.Init.Post is NeoForge-specific
	/*
	private void customizeSplashes(ScreenEvent.Init.Post event) {
		...
	}
	*/

	// TODO: Port to Fabric - ScreenEvent.Closing is NeoForge-specific
	/*
	private void clearEntityRenderUtilMap(ScreenEvent.Closing event) {
		...
	}
	*/

	// TODO: Port to Fabric - SelectMusicEvent is NeoForge-specific
	/*
	private void setMusicInDimension(SelectMusicEvent event) {
		...
	}
	*/

	// TODO: Port to Fabric - RenderGuiLayerEvent.Pre and VanillaGuiLayers are NeoForge-specific
	/*
	private void removeHostileMountHealth(RenderGuiLayerEvent.Pre event) {
		...
	}
	*/

	// Fabric version of aurora rendering - called from TwilightForestClient via LevelRenderEvents.END_MAIN
	public void renderAurora(LevelRenderContext context) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return;

		if ((aurora > 0 || lastAurora > 0) && TFShaders.AURORA != null) {
			Vec3 pos = context.levelState().cameraRenderState.pos;
			float intensity = (Mth.lerp(0F, lastAurora, aurora)) / 60F * 0.5F;
			int seed = Mth.abs((int) mc.level.getBiomeManager().biomeZoomSeed);

			// Use the new render pipeline with proper uniform passing
			TFShaders.AURORA.renderAurora(context, seed, (float) pos.x, (float) pos.y, (float) pos.z, intensity);
		}
	}

	// Public getter for aurora intensity
	public int getAuroraIntensity() {
		return aurora;
	}

	// TODO: Port to Fabric - RenderFrameEvent.Pre is NeoForge-specific
	private void killVignette() {
		Minecraft minecraft = Minecraft.getInstance();
		// only fire if we're in the twilight forest
		if (minecraft.level != null && TFDimension.DIMENSION_KEY.equals(minecraft.level.dimension())) {
			minecraft.gui.vignetteBrightness = 0.0F;
		}

		if (minecraft.player != null && HostileMountEvents.isRidingUnfriendly(minecraft.player)) {
			minecraft.gui.setOverlayMessage(Component.empty(), false);
		}
	}

	// Fabric version - called from TwilightForestClient via ClientTickEvents.END_CLIENT_TICK
	public void clientTick() {
		Minecraft mc = Minecraft.getInstance();

		if (!mc.isPaused()) {
			time++;

			lastAurora = aurora;
			if (mc.level != null && mc.getCameraEntity() != null && !TFConfig.getValidAuroraBiomes(mc.level.registryAccess()).isEmpty()) {
				RegistryAccess access = mc.level.registryAccess();
				Holder<Biome> biome = mc.level.getBiome(mc.getCameraEntity().blockPosition());
				if (TFConfig.getValidAuroraBiomes(access).stream().anyMatch(c -> holderMatcher.match(c, biome)))
					aurora++;
				else
					aurora--;
				aurora = Mth.clamp(aurora, 0, 60);
			} else {
				aurora = 0;
			}

			BugModelAnimationHelper.animate();

			if (mc.level != null) {
				var flashState = mc.level.endFlashState();
				if (flashState != null && flashState.flashStartedThisTick()) {
					MagicPaintingRenderer.lastLightning = mc.level.getGameTime();
				}

				if (TFConfig.firstPersonEffects && mc.player != null) {
					HashSet<ChunkPos> chunksInRange = new HashSet<>();
					for (int x = -16; x <= 16; x += 16) {
						for (int z = -16; z <= 16; z += 16) {
							chunksInRange.add(new ChunkPos((int) (mc.player.getX() + x) >> 4, (int) (mc.player.getZ() + z) >> 4));
						}
					}
					for (ChunkPos pos : chunksInRange) {
						if (mc.level.getChunk(pos.x(), pos.z(), ChunkStatus.FULL, false) != null) {
							List<BlockEntity> beanstalksInChunk = mc.level.getChunk(pos.x(), pos.z()).getBlockEntities().values().stream()
								.filter(blockEntity -> blockEntity instanceof GrowingBeanstalkBlockEntity beanstalkBlock && beanstalkBlock.isBeanstalkRumbling())
								.toList();
							if (!beanstalksInChunk.isEmpty()) {
								BlockEntity beanstalk = beanstalksInChunk.getFirst();
								Player player = mc.player;
								shakeIntensity = (float) (1.0F - mc.player.distanceToSqr(Vec3.atCenterOf(beanstalk.getBlockPos())) / Math.pow(16, 2));
								if (shakeIntensity > 0) {
									player.setYRot(player.getYRot() + (player.getRandom().nextFloat() - 0.5F) * shakeIntensity);
									player.setXRot(player.getXRot() + (player.getRandom().nextFloat() * 2.5F - 1.25F) * shakeIntensity);
									shakeIntensity = 0.0F;
									break;
								}
							}
						}
					}
				}
			}

			if (mc.player != null) {
				fixTranslatableBookNames(mc.player);
				updateMoonDialPhases(mc);
			}
		}
	}

	private void fixTranslatableBookNames(Player player) {
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			fixSingleBookName(stack);
		}
	}

	private void fixSingleBookName(ItemStack stack) {
		if (stack.getItem() instanceof WrittenBookItem && stack.has(TFDataComponents.TRANSLATABLE_BOOK) && stack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
			WrittenBookContent content = stack.get(DataComponents.WRITTEN_BOOK_CONTENT);
			if (content != null) {
				String rawTitle = content.title().raw();
				if (rawTitle.startsWith(TwilightForestMod.ID + ".book.") && !rawTitle.endsWith(".author")) {
					Component currentName = stack.get(DataComponents.CUSTOM_NAME);
					if (currentName == null || currentName.getString().equals(rawTitle)) {
						stack.set(DataComponents.CUSTOM_NAME, Component.translatable(rawTitle));
					}
				}
			}
		}
	}

	private void updateMoonDialPhases(Minecraft mc) {
		if (mc.level == null) return;
		Player player = mc.player;
		if (player == null) return;

		var level = mc.level;
		int phaseIndex;
		if (level.dimensionTypeRegistration().is(Identifier.fromNamespaceAndPath("minecraft", "the_end"))) {
			phaseIndex = 404;
		} else if (level.dimensionType().hasFixedTime() && !level.dimension().equals(TFDimension.DIMENSION_KEY)) {
			phaseIndex = -1;
		} else {
			MoonPhase phase = level.environmentAttributes().getDimensionValue(EnvironmentAttributes.MOON_PHASE);
			if (phase != null) {
				phaseIndex = phase.index();
			} else {
				long dayTime = level.getGameTime();
				phaseIndex = (int) ((dayTime / 24000L) % 8L);
			}
		}

		MoonDialItem.CLIENT_PHASE = phaseIndex;

		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (stack.is(TFItems.MOON_DIAL)) {
				int currentDamage = stack.getDamageValue();
				if (currentDamage != phaseIndex) {
					stack.setDamageValue(phaseIndex);
				}
			}
		}
	}

	// TODO: Port to Fabric - ViewportEvent.ComputeCameraAngles is NeoForge-specific
	private void shakeCamera() {
		if (TFConfig.firstPersonEffects && !Minecraft.getInstance().isPaused() && shakeIntensity > 0 && Minecraft.getInstance().player != null) {
			// event.setYaw/... needs porting
			shakeIntensity = 0F;
		}
	}

	// TODO: Port to Fabric - ItemTooltipEvent is NeoForge-specific; use Fabric tooltip callback
	/*
	private void addCustomTooltips(ItemTooltipEvent event) {
		...
	}
	*/

	// TODO: Port to Fabric - ComputeFovModifierEvent is NeoForge-specific
	/*
	private void updateBowFOV(ComputeFovModifierEvent event) {
		...
	}
	*/

	// TODO: Port to Fabric - ItemTooltipEvent is NeoForge-specific
	/*
	private void translateBookContents(ItemTooltipEvent event) {
		...
	}
	*/

	// TODO: Port to Fabric - ExtractBlockOutlineRenderStateEvent and CustomBlockOutlineRenderer are NeoForge-specific
	/*
	private void renderGiantBlockOutlines(ExtractBlockOutlineRenderStateEvent event) {
		...
	}
	*/

	// TODO: Port to Fabric - CustomizeGuiOverlayEvent.BossEventProgress is NeoForge-specific
	/*
	private void renderCustomBossbars(CustomizeGuiOverlayEvent.BossEventProgress event) {
		...
	}
	*/
}
