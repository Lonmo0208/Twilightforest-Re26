package twilightforest.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import twilightforest.client.model.entity.DeathTomeModel;
import twilightforest.potions.FrostedEffect;

public class IceLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
	private final RandomSource random = RandomSource.create();

	public IceLayer(RenderLayerParent<S, M> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector submitNodeCollector, int light, S state, float netHeadYaw, float headPitch) {
		if (!state.isFullyFrozen) return;
		double count = -1.0D;
		// Use a slowly-changing seed (update every 5 ticks / 0.25s) to avoid violent per-frame
		// position/rotation jitter of ice cubes. The original code used ageInTicks * 1000 which
		// changed the seed every frame, causing visible twitching.
		int id = (int)(state.ageInTicks / 5);

		this.random.setSeed(id * id * 3121L + id * 45238971L);

		int numCubes = (int) (state.boundingBoxHeight / 0.4F) + (int) (count / FrostedEffect.FROST_MULTIPLIER) + 1;

		float specialOffset = this.getParentModel() instanceof DeathTomeModel ? 1.0F : 0.0F;

		for (int i = 0; i < numCubes; i++) {
			stack.pushPose();
			float dx = ((this.random.nextFloat() * (state.boundingBoxWidth * 2.0F)) - state.boundingBoxWidth) * 0.1F;
			float dy = Math.max(1.5F - (this.random.nextFloat()) * (state.boundingBoxHeight - specialOffset), -0.1F) - specialOffset;
			float dz = ((this.random.nextFloat() * (state.boundingBoxWidth * 2.0F)) - state.boundingBoxWidth) * 0.1F;
			stack.translate(dx, dy, dz);
			stack.scale(0.5F, 0.5F, 0.5F);
			stack.mulPose(Axis.XP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.mulPose(Axis.YP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.mulPose(Axis.ZP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.translate(-0.5F, -0.5F, -0.5F);

			BlockModelRenderState iceModel = new BlockModelRenderState();
			new BlockModelResolver(Minecraft.getInstance().getModelManager()).update(iceModel, Blocks.ICE.defaultBlockState(), BlockDisplayContext.create());
			iceModel.submit(stack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, 0);
			stack.popPose();
		}
	}
}
