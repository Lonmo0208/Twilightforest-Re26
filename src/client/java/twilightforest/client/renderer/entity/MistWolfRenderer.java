package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MistWolfModel;
import twilightforest.client.state.entity.HostileWolfRenderState;
import twilightforest.entity.monster.MistWolf;

public class MistWolfRenderer extends MobRenderer<MistWolf, HostileWolfRenderState, MistWolfModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("mistwolf.png");

	public MistWolfRenderer(EntityRendererProvider.Context context) {
		super(context, new MistWolfModel(context.bakeLayer(TFModelLayers.HOSTILE_WOLF)), 1.0F);
	}

	@Override
	protected void scale(HostileWolfRenderState state, PoseStack stack) {
		float wolfScale = 1.9F;
		stack.scale(wolfScale, wolfScale, wolfScale);
	}

	@Override
	protected int getModelTint(HostileWolfRenderState state) {
		float misty = Math.min(1.0F, state.brightness * 3.0F + 0.25F);
		float smoky = Math.min(1.0F, state.brightness * 2.0F + 0.6F);
		return ARGB.colorFromFloat(smoky, misty, misty, misty);
	}

	@Override
	public HostileWolfRenderState createRenderState() {
		return new HostileWolfRenderState();
	}

	@Override
	public void extractRenderState(MistWolf entity, HostileWolfRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.brightness = entity.level().getMaxLocalRawBrightness(entity.blockPosition());
		state.tailAngle = entity.getTailAngle();
		state.healthPercent = entity.getHealth() / entity.getMaxHealth();
	}

	@Override
	public Identifier getTextureLocation(HostileWolfRenderState state) {
		return TEXTURE;
	}
}
