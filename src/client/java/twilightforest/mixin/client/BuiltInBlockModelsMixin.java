package twilightforest.mixin.client;

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
import twilightforest.client.renderer.special.KeepsakeCasketSpecialRenderer;
import twilightforest.client.renderer.special.SkullChestSpecialRenderer;
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
}
