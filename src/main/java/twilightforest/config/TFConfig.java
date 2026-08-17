package twilightforest.config;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.util.PlayerHelper;

import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.*;

public class TFConfig {

	public static final String CONFIG_ID = "config." + TwilightForestMod.ID + ".";
	@Nullable
	private static Identifier portalLockingAdvancement;
	private static final List<Holder<Biome>> VALID_AURORA_BIOMES = new ArrayList<>();
	public static final List<GameProfile> GAME_PROFILES = new ArrayList<>();

	public static boolean silentCicadas = false;
	public static boolean silentCicadasOnHead = false;
	public static boolean firstPersonEffects = true;
	public static boolean rotateTrophyHeadsGui = true;
	public static boolean disableOptifineNagScreen = false;
	public static boolean disableLockedBiomeToasts = false;
	public static boolean showQuestRamCrosshairIndicator = true;
	public static boolean showFortificationShieldIndicator = true;
	public static boolean showFortificationShieldIndicatorInCreative = false;
	private static int clientCloudBlockPrecipitationDistance = 32;
	public static boolean prettifyOreMeterGui = true;
	public static boolean spawnCharmAnimationAsTotem = false;
	public static boolean manualTravellersWingsGradualGlideDefault = true;
	public static boolean firstPersonGloveOverlay = true;
	public static boolean firstPersonShieldScepterRenderer = false;
	public static RainbowLeavesStyle rainbowLeavesStyle = RainbowLeavesStyle.VANILLA_GRID;

	public static int itemDisplayXOffs = 4;
	public static int itemDisplayYOffs = 4;
	public static double itemDisplayScale = 1.0D;
	public static boolean clock24HourFormat = use24HourTimeDefault();

	public static boolean casketUUIDLocking = false;
	public static boolean disableSkullCandles = false;
	public static boolean defaultItemEnchants = true;
	public static boolean bossDropChests = true;
	public static MultiplayerFightAdjuster multiplayerFightAdjuster = MultiplayerFightAdjuster.NONE;
	public static int commonCloudBlockPrecipitationDistance = 32;

	// -- Dimension --
	public static boolean newPlayersSpawnInTF = false;
	public static boolean portalForNewPlayerSpawn = true;

	// -- Portal --
	public static String originDimension = Level.OVERWORLD.identifier().toString();
	public static boolean allowPortalsInOtherDimensions = false;
	public static PermissionLevel portalCreationPermission = PermissionLevel.ALL;
	public static boolean disablePortalCreation = false;
	public static boolean checkPortalPlacement = true;
	public static boolean destructivePortalLightning = true;
	public static boolean shouldReturnPortalBeUsable = true;
	public static int maxPortalSize = 64;

	// -- Uncrafting Table --
	public static double uncraftingXpCostMultiplier = 1.0D;
	public static double repairingXpCostMultiplier = 1.0D;
	public static boolean allowShapelessUncrafting = false;
	public static boolean disableIngredientSwitching = false;
	public static List<? extends String> disableUncraftingRecipes = new ArrayList<>();
	public static boolean reverseRecipeBlacklist = false;
	public static List<? extends String> blacklistedUncraftingModIds = new ArrayList<>();
	public static boolean flipUncraftingModIdList = false;
	public static boolean disableUncraftingOnly = false;
	public static boolean disableEntireTable = false;
	public static boolean allowScepterUncrafting = false;

	// -- Magic Trees --
	public static boolean disableTimeCore = false;
	public static int timeCoreRange = 16;
	public static boolean disableTransformationCore = false;
	public static int transformationCoreRange = 16;
	public static boolean disableMiningCore = false;
	public static int miningCoreRange = 16;
	public static boolean disableSortingCore = false;
	public static int sortingCoreRange = 16;

	// -- Shield Parrying --
	public static boolean parryNonTwilightAttacks = false;
	public static int shieldParryTicks = 40;

	public static int getClientCloudBlockPrecipitationDistance() {
		return clientCloudBlockPrecipitationDistance == -1 ? commonCloudBlockPrecipitationDistance : clientCloudBlockPrecipitationDistance;
	}

	@Nullable
	public static Identifier getPortalLockingAdvancement(Player player) {
		//only run assigning logic if the config has an advancement set and the RL is null
		var commonConfig = ConfigSetup.getCommonConfig();
		if (portalLockingAdvancement == null && commonConfig != null && !commonConfig.PORTAL.portalAdvancementLock.get().isEmpty()) {

			Identifier lock = Identifier.tryParse(commonConfig.PORTAL.portalAdvancementLock.get());
			if (lock == null || PlayerHelper.getAdvancement(player, lock) == null) {
				//if the RL is not a valid advancement fail us
				TwilightForestMod.LOGGER.error("The portal locking advancement is not a valid advancement! Setting to null!");
				commonConfig.PORTAL.portalAdvancementLock.set("");
			} else {
				portalLockingAdvancement = Identifier.tryParse(commonConfig.PORTAL.portalAdvancementLock.get());
			}
		}
		//always return the RL, even if its null. We can use this to run logic less often
		return portalLockingAdvancement;
	}

	//Forge's biome registry doesn't contain biomes done via datapacks, so we have to use registryaccess
	public static List<Holder<Biome>> getValidAuroraBiomes(RegistryAccess access) {
		var clientConfig = ConfigSetup.getClientConfig();
		if (VALID_AURORA_BIOMES.isEmpty() && clientConfig != null && !clientConfig.auroraBiomes.get().isEmpty()) {
			clientConfig.auroraBiomes.get().forEach(s -> {
				Optional<Holder<Biome>> holder = Optional.ofNullable(Identifier.tryParse(s)).flatMap(key -> access.lookupOrThrow(Registries.BIOME).get(key));
				if (holder.isEmpty()) {
					TwilightForestMod.LOGGER.warn("Biome {} in Twilight Forest's validAuroraBiomes config option is not a valid biome. Skipping!", s);
				} else {
					VALID_AURORA_BIOMES.add(holder.get());
				}
			});
		}
		return VALID_AURORA_BIOMES;
	}

	protected static void rebakeCommonOptions(TFCommonConfig config) {
		casketUUIDLocking = config.casketUUIDLocking.get();
		disableSkullCandles = config.disableSkullCandles.get();
		defaultItemEnchants = config.defaultItemEnchants.get();
		bossDropChests = config.bossDropChests.get();
		commonCloudBlockPrecipitationDistance = config.cloudBlockPrecipitationDistance.get();
		multiplayerFightAdjuster = config.multiplayerFightAdjuster.get();

		//Dimension
		newPlayersSpawnInTF = config.DIMENSION.newPlayersSpawnInTF.get();
		portalForNewPlayerSpawn = config.DIMENSION.portalForNewPlayerSpawn.get();

		//Portal
		originDimension = config.PORTAL.originDimension.get();
		allowPortalsInOtherDimensions = config.PORTAL.allowPortalsInOtherDimensions.get();
		portalCreationPermission = config.PORTAL.portalCreationPermission.get();
		disablePortalCreation = config.PORTAL.disablePortalCreation.get();
		checkPortalPlacement = config.PORTAL.checkPortalPlacement.get();
		destructivePortalLightning = config.PORTAL.destructivePortalLightning.get();
		shouldReturnPortalBeUsable = config.PORTAL.shouldReturnPortalBeUsable.get();
		maxPortalSize = config.PORTAL.maxPortalSize.get();

		//Uncrafting Table
		uncraftingXpCostMultiplier = config.UNCRAFTING_STUFFS.uncraftingXpCostMultiplier.get();
		repairingXpCostMultiplier = config.UNCRAFTING_STUFFS.repairingXpCostMultiplier.get();
		allowShapelessUncrafting = config.UNCRAFTING_STUFFS.allowShapelessUncrafting.get();
		disableIngredientSwitching = config.UNCRAFTING_STUFFS.disableIngredientSwitching.get();
		disableUncraftingOnly = config.UNCRAFTING_STUFFS.disableUncraftingOnly.get();
		disableEntireTable = config.UNCRAFTING_STUFFS.disableEntireTable.get();
		disableUncraftingRecipes = config.UNCRAFTING_STUFFS.disableUncraftingRecipes.get();
		reverseRecipeBlacklist = config.UNCRAFTING_STUFFS.reverseRecipeBlacklist.get();
		blacklistedUncraftingModIds = config.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get();
		flipUncraftingModIdList = config.UNCRAFTING_STUFFS.flipUncraftingModIdList.get();
		allowScepterUncrafting = config.UNCRAFTING_STUFFS.allowScepterUncrafting.get();

		// Tree Cores
		disableTimeCore = config.MAGIC_TREES.timeRange.get() <= 0;
		timeCoreRange = config.MAGIC_TREES.timeRange.get();
		disableTransformationCore = config.MAGIC_TREES.transformationRange.get() <= 0;
		transformationCoreRange = config.MAGIC_TREES.transformationRange.get();
		disableMiningCore = config.MAGIC_TREES.miningRange.get() <= 0;
		miningCoreRange = config.MAGIC_TREES.miningRange.get();
		disableSortingCore = config.MAGIC_TREES.sortingRange.get() <= 0;
		sortingCoreRange = config.MAGIC_TREES.sortingRange.get();

		//Parrying
		parryNonTwilightAttacks = config.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get();
		shieldParryTicks = config.SHIELD_INTERACTIONS.shieldParryTicks.get();

		//sets cached portal locking advancement to null just in case it changed
		portalLockingAdvancement = null;
	}

	protected static void rebakeClientOptions(TFClientConfig config) {
		reloadGiantSkins(config);
		VALID_AURORA_BIOMES.clear();
		silentCicadas = config.silentCicadas.get();
		silentCicadasOnHead = config.silentCicadasOnHead.get();
		firstPersonEffects = config.firstPersonEffects.get();
		rotateTrophyHeadsGui = config.rotateTrophyHeadsGui.get();
		disableOptifineNagScreen = config.disableOptifineNagScreen.get();
		disableLockedBiomeToasts = config.disableLockedBiomeToasts.get();
		showFortificationShieldIndicator = config.showFortificationShieldIndicator.get();
		showFortificationShieldIndicatorInCreative = config.showFortificationShieldIndicatorInCreative.get();
		showQuestRamCrosshairIndicator = config.showQuestRamCrosshairIndicator.get();
		clientCloudBlockPrecipitationDistance = config.cloudBlockPrecipitationDistance.get();
		prettifyOreMeterGui = config.prettifyOreMeterGui.get();
		spawnCharmAnimationAsTotem = config.spawnCharmAnimationAsTotem.get();
		manualTravellersWingsGradualGlideDefault = config.manualTravellersWingsGradualGlide.get();
		firstPersonGloveOverlay = config.firstPersonGloveOverlay.get();
		firstPersonShieldScepterRenderer = config.firstPersonShieldScepterRenderer.get();

		itemDisplayXOffs = config.ITEM_DISPLAY.screenOffsetX.get();
		itemDisplayYOffs = config.ITEM_DISPLAY.screenOffsetY.get();
		itemDisplayScale = config.ITEM_DISPLAY.screenScale.get();
		clock24HourFormat = config.ITEM_DISPLAY.twentyFourHourFormat.get();
		rainbowLeavesStyle = config.rainbowLeavesStyle.get();
	}

	private static void reloadGiantSkins(TFClientConfig config) {
		if (!config.giantSkinUUIDs.get().isEmpty()) {
			new Thread() {
				@Override
				public void run() {
					GAME_PROFILES.clear();
					// ===== 26.3 TODO: YggdrasilAuthenticationService / MinecraftSessionService / ProfileResult 已从 MC 依赖中删除 =====
					// ===== 如需恢复 skin fetch，请改为使用 com.mojang.authlib 独立库或 Minecraft 服务端 GameProfileRepository API =====
					TwilightForestMod.LOGGER.warn("Skin fetching for giant mobs is temporarily disabled in 26.3 snapshot; skipping {} UUID entries.", config.giantSkinUUIDs.get().size());
					super.run();
				}
			}.start();
		}
	}

	public enum MultiplayerFightAdjuster {
		NONE(false, false),
		MORE_LOOT(true, false),
		MORE_HEALTH(false, true),
		MORE_LOOT_AND_HEALTH(true, true);

		private final boolean moreLoot;
		private final boolean moreHealth;

		MultiplayerFightAdjuster(boolean loot, boolean health) {
			this.moreLoot = loot;
			this.moreHealth = health;
		}

		public boolean adjustsLootRolls() {
			return this.moreLoot;
		}

		public boolean adjustsHealth() {
			return this.moreHealth;
		}

		public Component getTranslatedName() {
			return Component.translatable(CONFIG_ID + "multiplayer_fight_adjuster." + this.name().toLowerCase(Locale.ROOT));
		}
	}

	public enum RainbowLeavesStyle {
		VANILLA_GRID,
		HSV_SMOOTH;

		public Component getTranslatedName() {
			return Component.translatable(CONFIG_ID + "rainbow_leaves_style." + this.name().toLowerCase(Locale.ROOT));
		}
	}

	public static boolean use24HourTimeDefault() {
		try {
			String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, FormatStyle.SHORT, IsoChronology.INSTANCE, Locale.getDefault());
			return !pattern.contains("a");  // "a" is used to display AM / PM.
		} catch (Throwable throwable) {
			return true;
		}
	}
}
