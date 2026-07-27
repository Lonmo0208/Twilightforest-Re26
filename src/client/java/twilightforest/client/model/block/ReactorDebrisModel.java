package twilightforest.client.model.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import twilightforest.block.entity.ReactorDebrisBlockEntity;
import twilightforest.client.renderer.block.ReactorDebrisRenderer;

import java.util.List;

// TODO: Port to Fabric - DynamicBlockStateModel is NeoForge-specific
public class ReactorDebrisModel implements BlockStateModel {

	private final BlockStateModel wrappedModel;

	public ReactorDebrisModel(BlockStateModel wrappedModel) {
		this.wrappedModel = wrappedModel;
	}

	// TODO: Port to Fabric - level-aware collectParts was from NeoForge DynamicBlockStateModel
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		// Determine the particle texture from the block entity
		Identifier textureForParticle = ReactorDebrisBlockEntity.DEFAULT_TEXTURE;
		if (level.getBlockEntity(pos) instanceof ReactorDebrisBlockEntity reactorDebrisBlockEntity
			&& level instanceof ClientLevel clientLevel) {
			textureForParticle = reactorDebrisBlockEntity.textures[clientLevel.getRandom().nextInt(reactorDebrisBlockEntity.textures.length)];
		}
		Material.Baked particleMaterial = new Material.Baked(ReactorDebrisRenderer.getSprite(textureForParticle), false);

		// Delegate to the wrapped model
		this.wrappedModel.collectParts(random, parts);
	}

	@Override
	@Deprecated
	public void collectParts(RandomSource random, @NotNull List<BlockStateModelPart> parts) {
		this.wrappedModel.collectParts(random, parts);
	}

	// TODO: Port to Fabric - level-aware particleMaterial was from NeoForge DynamicBlockStateModel
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.wrappedModel.particleMaterial();
	}

	@Override
	@Deprecated
	public Material.Baked particleMaterial() {
		return this.wrappedModel.particleMaterial();
	}

	// TODO: Port to Fabric - level-aware materialFlags was from NeoForge DynamicBlockStateModel; Fabric version uses 4 args with RandomSource
	public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.wrappedModel.materialFlags();
	}

	@Override
	@Deprecated
	public int materialFlags() {
		return this.wrappedModel.materialFlags();
	}
}
