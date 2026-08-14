package twilightforest.mixin.client;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.block.BuiltInBlockModels;
import net.minecraft.client.renderer.block.model.SpecialBlockModelWrapper;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.TwilightForestMod;
import twilightforest.block.TrophyBlock;
import twilightforest.block.TrophyWallBlock;
import twilightforest.client.renderer.block.TrophyRenderer;
import twilightforest.client.renderer.special.BrazierSpecialRenderer;
import twilightforest.client.renderer.special.KeepsakeCasketSpecialRenderer;
import twilightforest.client.renderer.special.SkullChestSpecialRenderer;
import twilightforest.client.renderer.special.TrophyBlockSpecialRenderer;
import twilightforest.enums.BossVariant;
import twilightforest.init.TFBlocks;

import java.util.Optional;

/**
 * Registers all twilight containers (chests, trapped chests, skull chest and keepsake casket) into
 * {@link BuiltInBlockModels}, exactly like vanilla chests.
 *
 * <p>This makes containers use the <em>block</em> rendering path with the chest special renderer
 * instead of the item rendering path. When a container becomes a
 * {@link net.minecraft.world.entity.livingblock.LivingBlock}, {@code LivingBlockRenderer} resolves
 * the block state model through {@code BlockModelResolver}, which picks up these built-in models and
 * renders a full-size chest with the correct facing, size and collision box.
 *
 * <p>Fabric API 26w14a (0.145.2) does not yet ship {@code BuiltInBlockModelsCallback}, so we hook
 * the vanilla builder directly (equivalent to NeoForge's {@code createTFChest} registration).
 */
@Mixin(BuiltInBlockModels.class)
public abstract class BuiltInBlockModelsMixin {

	@Inject(method = "addDefaults", at = @At("TAIL"))
	private static void tf$addContainerModels(BuiltInBlockModels.Builder builder, CallbackInfo ci) {
		// Normal chests
		registerChest(builder, TFBlocks.TWILIGHT_OAK_CHEST, "twilight_oak", "normal");
		registerChest(builder, TFBlocks.CANOPY_CHEST, "canopy", "normal");
		registerChest(builder, TFBlocks.MANGROVE_CHEST, "mangrove", "normal");
		registerChest(builder, TFBlocks.DARK_CHEST, "darkwood", "normal");
		registerChest(builder, TFBlocks.TIME_CHEST, "time", "normal");
		registerChest(builder, TFBlocks.TRANSFORMATION_CHEST, "transformation", "normal");
		registerChest(builder, TFBlocks.MINING_CHEST, "mining", "normal");
		registerChest(builder, TFBlocks.SORTING_CHEST, "sorting", "normal");

		// Trapped chests
		registerChest(builder, TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, "twilight_oak", "trapped");
		registerChest(builder, TFBlocks.CANOPY_TRAPPED_CHEST, "canopy", "trapped");
		registerChest(builder, TFBlocks.MANGROVE_TRAPPED_CHEST, "mangrove", "trapped");
		registerChest(builder, TFBlocks.DARK_TRAPPED_CHEST, "darkwood", "trapped");
		registerChest(builder, TFBlocks.TIME_TRAPPED_CHEST, "time", "trapped");
		registerChest(builder, TFBlocks.TRANSFORMATION_TRAPPED_CHEST, "transformation", "trapped");
		registerChest(builder, TFBlocks.MINING_TRAPPED_CHEST, "mining", "trapped");
		registerChest(builder, TFBlocks.SORTING_TRAPPED_CHEST, "sorting", "trapped");

		// Skull chest and keepsake casket (render with their own special renderers)
		registerFacingBlock(builder, TFBlocks.SKULL_CHEST, new SkullChestSpecialRenderer.Unbaked());
		registerFacingBlock(builder, TFBlocks.KEEPSAKE_CASKET, new KeepsakeCasketSpecialRenderer.Unbaked());

		// Boss trophies (ground + wall) and the brazier, so they render through the block path
		registerTrophy(builder, TFBlocks.NAGA_TROPHY, BossVariant.NAGA);
		registerTrophy(builder, TFBlocks.LICH_TROPHY, BossVariant.LICH);
		registerTrophy(builder, TFBlocks.HYDRA_TROPHY, BossVariant.HYDRA);
		registerTrophy(builder, TFBlocks.UR_GHAST_TROPHY, BossVariant.UR_GHAST);
		registerTrophy(builder, TFBlocks.KNIGHT_PHANTOM_TROPHY, BossVariant.KNIGHT_PHANTOM);
		registerTrophy(builder, TFBlocks.SNOW_QUEEN_TROPHY, BossVariant.SNOW_QUEEN);
		registerTrophy(builder, TFBlocks.MINOSHROOM_TROPHY, BossVariant.MINOSHROOM);
		registerTrophy(builder, TFBlocks.ALPHA_YETI_TROPHY, BossVariant.ALPHA_YETI);
		registerTrophy(builder, TFBlocks.QUEST_RAM_TROPHY, BossVariant.QUEST_RAM);

		registerWallTrophy(builder, TFBlocks.NAGA_WALL_TROPHY, BossVariant.NAGA);
		registerWallTrophy(builder, TFBlocks.LICH_WALL_TROPHY, BossVariant.LICH);
		registerWallTrophy(builder, TFBlocks.HYDRA_WALL_TROPHY, BossVariant.HYDRA);
		registerWallTrophy(builder, TFBlocks.UR_GHAST_WALL_TROPHY, BossVariant.UR_GHAST);
		registerWallTrophy(builder, TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY, BossVariant.KNIGHT_PHANTOM);
		registerWallTrophy(builder, TFBlocks.SNOW_QUEEN_WALL_TROPHY, BossVariant.SNOW_QUEEN);
		registerWallTrophy(builder, TFBlocks.MINOSHROOM_WALL_TROPHY, BossVariant.MINOSHROOM);
		registerWallTrophy(builder, TFBlocks.ALPHA_YETI_WALL_TROPHY, BossVariant.ALPHA_YETI);
		registerWallTrophy(builder, TFBlocks.QUEST_RAM_WALL_TROPHY, BossVariant.QUEST_RAM);

		builder.put((colors, state) -> new SpecialBlockModelWrapper.Unbaked<>(
			new BrazierSpecialRenderer.Unbaked(),
			Optional.empty()
		), TFBlocks.BRAZIER);
	}

	/**
	 * Registers a twilight chest as a built-in block model that dispatches on {@link ChestBlock.FACING}
	 * and {@link ChestBlock.TYPE}, mirroring the vanilla chest registration.
	 */
	private static void registerChest(BuiltInBlockModels.Builder builder, Block block, String wood, String suffix) {
		MultiblockChestResources<Identifier> textures = new MultiblockChestResources<>(
			TwilightForestMod.prefix(wood + "/" + suffix),
			TwilightForestMod.prefix(wood + "/" + suffix + "_left"),
			TwilightForestMod.prefix(wood + "/" + suffix + "_right")
		);
		builder.put((colors, state) -> {
			Direction facing = state.getValue(ChestBlock.FACING);
			ChestType type = state.hasProperty(ChestBlock.TYPE) ? state.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
			return new SpecialBlockModelWrapper.Unbaked<>(
				new ChestSpecialRenderer.Unbaked(textures.select(type), type),
				Optional.of(ChestRenderer.modelTransformation(facing))
			);
		}, block);
	}

	/**
	 * Registers a block (skull chest / keepsake casket) as a built-in block model that dispatches on its
	 * horizontal facing property, using an existing {@link SpecialModelRenderer.Unbaked}.
	 */
	private static void registerFacingBlock(BuiltInBlockModels.Builder builder, Block block, SpecialModelRenderer.Unbaked<?> special) {
		builder.put((colors, state) -> {
			Direction facing = state.hasProperty(ChestBlock.FACING) ? state.getValue(ChestBlock.FACING) : Direction.NORTH;
			return new SpecialBlockModelWrapper.Unbaked<>(special, Optional.of(ChestRenderer.modelTransformation(facing)));
		}, block);
	}

	/**
	 * Registers a free-standing boss trophy as a built-in block model, dispatching on
	 * {@link TrophyBlock#ROTATION} like vanilla mob heads, so that LivingBlock trophies render
	 * identically to the placed block.
	 */
	private static void registerTrophy(BuiltInBlockModels.Builder builder, TrophyBlock block, BossVariant variant) {
		builder.put((colors, state) -> {
			int rotation = state.getValue(TrophyBlock.ROTATION);
			return new SpecialBlockModelWrapper.Unbaked<>(
				new TrophyBlockSpecialRenderer.Unbaked(variant, false),
				Optional.of(TrophyRenderer.createGroundTransformation(rotation))
			);
		}, block);
	}

	/**
	 * Registers a wall-mounted boss trophy as a built-in block model, dispatching on
	 * {@link TrophyWallBlock#FACING} like vanilla wall heads. The UR Ghast trophy uses the unmounted
	 * wall transformation, matching {@link TrophyRenderer}'s block entity rendering.
	 */
	private static void registerWallTrophy(BuiltInBlockModels.Builder builder, TrophyWallBlock block, BossVariant variant) {
		builder.put((colors, state) -> {
			Direction facing = state.getValue(TrophyWallBlock.FACING);
			Transformation transformation = variant == BossVariant.UR_GHAST
				? TrophyRenderer.createUnmountedWallTransformation(facing)
				: TrophyRenderer.createWallTransformation(facing);
			return new SpecialBlockModelWrapper.Unbaked<>(
				new TrophyBlockSpecialRenderer.Unbaked(variant, true),
				Optional.of(transformation)
			);
		}, block);
	}
}
