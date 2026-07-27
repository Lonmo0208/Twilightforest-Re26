package twilightforest.client.renderer.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.util.EnumMap;
import java.util.Map;

public class TFChestRenderer<T extends ChestBlockEntity> extends ChestRenderer<T> {
	public static final Map<Block, EnumMap<ChestType, SpriteId>> MATERIALS;

	static {
		ImmutableMap.Builder<Block, EnumMap<ChestType, SpriteId>> builder = ImmutableMap.builder();

		builder.put(TFBlocks.TWILIGHT_OAK_CHEST, chestMaterial("twilight_oak", "normal"));
		builder.put(TFBlocks.CANOPY_CHEST, chestMaterial("canopy", "normal"));
		builder.put(TFBlocks.MANGROVE_CHEST, chestMaterial("mangrove", "normal"));
		builder.put(TFBlocks.DARK_CHEST, chestMaterial("darkwood", "normal"));
		builder.put(TFBlocks.TIME_CHEST, chestMaterial("time", "normal"));
		builder.put(TFBlocks.TRANSFORMATION_CHEST, chestMaterial("transformation", "normal"));
		builder.put(TFBlocks.MINING_CHEST, chestMaterial("mining", "normal"));
		builder.put(TFBlocks.SORTING_CHEST, chestMaterial("sorting", "normal"));

		builder.put(TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, chestMaterial("twilight_oak", "trapped"));
		builder.put(TFBlocks.CANOPY_TRAPPED_CHEST, chestMaterial("canopy", "trapped"));
		builder.put(TFBlocks.MANGROVE_TRAPPED_CHEST, chestMaterial("mangrove", "trapped"));
		builder.put(TFBlocks.DARK_TRAPPED_CHEST, chestMaterial("darkwood", "trapped"));
		builder.put(TFBlocks.TIME_TRAPPED_CHEST, chestMaterial("time", "trapped"));
		builder.put(TFBlocks.TRANSFORMATION_TRAPPED_CHEST, chestMaterial("transformation", "trapped"));
		builder.put(TFBlocks.MINING_TRAPPED_CHEST, chestMaterial("mining", "trapped"));
		builder.put(TFBlocks.SORTING_TRAPPED_CHEST, chestMaterial("sorting", "trapped"));

		MATERIALS = builder.build();
	}

	// Store the block type during extraction so submit() can look up the correct sprite
	private Block currentBlock;
	private final SpriteGetter sprites;
	private final MultiblockChestResources<ChestModel> models;

	public TFChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
		this.sprites = context.sprites();
		this.models = ChestRenderer.LAYERS.map(layer -> new ChestModel(context.bakeLayer(layer)));
	}

	@Override
	public void extractRenderState(T blockEntity, ChestRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		this.currentBlock = blockEntity.getBlockState().getBlock();
	}

	@Override
	public void submit(ChestRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		Block block = this.currentBlock;
		SpriteId customSprite = block != null ? getCustomSprite(block, state) : null;
		if (customSprite != null) {
			poseStack.pushPose();
			poseStack.mulPose(ChestRenderer.modelTransformation(state.facing));
			float open = state.open;
			open = 1.0F - open;
			open = 1.0F - open * open * open;
			ChestModel model = this.models.select(state.type);
			submitNodeCollector.submitModel(
				model, open, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, customSprite, this.sprites, 0, state.breakProgress
			);
			poseStack.popPose();
		} else {
			super.submit(state, poseStack, submitNodeCollector, camera);
		}
	}

	@Nullable
	private SpriteId getCustomSprite(Block block, ChestRenderState renderState) {
		EnumMap<ChestType, SpriteId> map = MATERIALS.get(block);
		return map != null ? map.get(renderState.type) : null;
	}

	private static EnumMap<ChestType, SpriteId> chestMaterial(String type, String suffix) {
		EnumMap<ChestType, SpriteId> map = new EnumMap<>(ChestType.class);
		map.put(ChestType.SINGLE, Sheets.CHEST_MAPPER.apply(TwilightForestMod.prefix(type + "/" + suffix)));
		map.put(ChestType.LEFT, Sheets.CHEST_MAPPER.apply(TwilightForestMod.prefix(type + "/" + suffix + "_left")));
		map.put(ChestType.RIGHT, Sheets.CHEST_MAPPER.apply(TwilightForestMod.prefix(type + "/" + suffix + "_right")));
		return map;
	}
}
