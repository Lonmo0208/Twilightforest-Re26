package twilightforest.client.model.block.aurorablock;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.client.model.block.LevelAwareBlockStateModel;
import twilightforest.util.SimplexNoiseHelper;

import java.util.List;
import java.util.function.Predicate;

// TODO: Port to Fabric - DynamicBlockStateModel is NeoForge-specific
public class NoiseVaryingModel implements BlockStateModel, LevelAwareBlockStateModel {
	private final BlockStateModel[] variants;

	public NoiseVaryingModel(BlockStateModel[] variants) {
		this.variants = variants;
	}

	@Override
	public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
		this.variants[0].collectParts(random, parts);
	}

	// Fabric (FRAPI) world rendering entry point - pick the variant for the block's position
	@Override
	public void emitQuads(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, Predicate<Direction> cullTest) {
		this.chooseVariant(pos).emitQuads(emitter, level, pos, state, random, cullTest);
	}

	@Override
	public Material.Baked particleMaterial() {
		return this.variants[0].particleMaterial();
	}

	@Override
	public @MaterialFlags int materialFlags() {
		return this.variants[0].materialFlags();
	}

	// TODO: Port to Fabric - level-aware collectParts was from NeoForge DynamicBlockStateModel
	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		this.chooseVariant(pos).collectParts(random, parts);
	}

	// TODO: Port to Fabric - level-aware particleMaterial was from NeoForge DynamicBlockStateModel
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.chooseVariant(pos).particleMaterial();
	}

	// TODO: Port to Fabric - level-aware materialFlags was from NeoForge DynamicBlockStateModel; Fabric version uses 4 args with RandomSource
	public @MaterialFlags int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.chooseVariant(pos).materialFlags();
	}

	private BlockStateModel chooseVariant(BlockPos pos) {
		return this.variants[SimplexNoiseHelper.calcVariant(pos, this.variants.length)];
	}

}
