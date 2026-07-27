package twilightforest.client.model.block.giantblock;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
// TODO: Port to Fabric - BlockStateModel imports for Fabric compatibility
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialFlags;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.GiantBlock;

import java.util.List;

// TODO: Port to Fabric - DynamicBlockStateModel is NeoForge-specific
public class GiantBlockModel implements BlockStateModel {

	private final BlockStateModel[] voxels;

	public GiantBlockModel(BlockStateModel[] voxels) {
		this.voxels = voxels;
	}

	// TODO: Port to Fabric - level-aware collectParts was from NeoForge DynamicBlockStateModel
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		this.voxels[GiantBlock.packCoords(pos)].collectParts(random, parts);
	}

	@Override
	public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
		this.voxels[0].collectParts(random, output);
	}

	// TODO: Port to Fabric - level-aware particleMaterial was from NeoForge DynamicBlockStateModel
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.voxels[GiantBlock.packCoords(pos)].particleMaterial();
	}

	@Override
	public Material.Baked particleMaterial() {
		return this.voxels[0].particleMaterial();
	}

	// TODO: Port to Fabric - level-aware materialFlags was from NeoForge DynamicBlockStateModel; Fabric version uses 4 args with RandomSource
	public @MaterialFlags int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.voxels[GiantBlock.packCoords(pos)].materialFlags();
	}

	@Override
	public @MaterialFlags int materialFlags() {
		return this.voxels[0].materialFlags();
	}
}
