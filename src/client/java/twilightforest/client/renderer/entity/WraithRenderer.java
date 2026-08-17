package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.WraithModel;
import twilightforest.client.state.entity.WraithRenderState;
import twilightforest.entity.monster.Wraith;

public class WraithRenderer extends HumanoidMobRenderer<Wraith, WraithRenderState, WraithModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("ghost.png");

	public WraithRenderer(EntityRendererProvider.Context context) {
		super(context, new WraithModel(context.bakeLayer(TFModelLayers.WRAITH)), 0.5F);
	}

	@Override
	protected int getModelTint(WraithRenderState state) {
		return ARGB.colorFromFloat(0.6F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public WraithRenderState createRenderState() {
		return new WraithRenderState();
	}

	@Override
	public void extractRenderState(Wraith entity, WraithRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.attackTime = entity.getSwingAnimation(partialTick);
	}

	@Override
	public Identifier getTextureLocation(WraithRenderState state) {
		return TEXTURE;
	}
}
