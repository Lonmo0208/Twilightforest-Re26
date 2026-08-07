package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.state.entity.HostileWolfRenderState;

public class WinterWolfRenderer extends HostileWolfRenderer {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("winterwolf.png");

	public WinterWolfRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 1.0F;
	}

	@Override
	protected void scale(HostileWolfRenderState state, PoseStack stack) {
		stack.scale(1.9F, 1.9F, 1.9F);
	}

	@Override
	public Identifier getTextureLocation(HostileWolfRenderState state) {
		return TEXTURE;
	}
}
