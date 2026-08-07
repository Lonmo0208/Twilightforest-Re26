package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HostileWolfModel;
import twilightforest.client.state.entity.HostileWolfRenderState;
import twilightforest.entity.monster.HostileWolf;

public class HostileWolfRenderer extends MobRenderer<HostileWolf, HostileWolfRenderState, HostileWolfModel<HostileWolfRenderState>> {

	public HostileWolfRenderer(EntityRendererProvider.Context context) {
		super(context, new HostileWolfModel<>(context.bakeLayer(TFModelLayers.HOSTILE_WOLF)), 0.5F);
	}

	@Override
	public HostileWolfRenderState createRenderState() {
		return new HostileWolfRenderState();
	}

	@Override
	public void extractRenderState(HostileWolf entity, HostileWolfRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.tailAngle = entity.getTailAngle();
		state.texture = entity.getTexture();
		state.healthPercent = entity.getHealth() / entity.getMaxHealth();
	}

	@Override
	public Identifier getTextureLocation(HostileWolfRenderState state) {
		return state.texture;
	}
}
