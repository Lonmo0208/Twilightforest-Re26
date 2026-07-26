package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.core.Direction;
import org.joml.Vector3fc;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.KeepsakeCasketModel;
import twilightforest.client.renderer.block.KeepsakeCasketRenderer;

import java.util.function.Consumer;

public record KeepsakeCasketSpecialRenderer(KeepsakeCasketModel model, float openness) implements NoDataSpecialModelRenderer {

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		stack.translate(0.5F, 0.0F, 0.5F);
		stack.mulPose(Direction.NORTH.getRotation());
		stack.mulPose(Axis.XP.rotationDegrees(90.0F));
		collector.submitModel(this.model(), this.openness(), stack, KeepsakeCasketRenderer.getTextureLocation(0), light, overlay, outlineColor, null);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.mulPose(Direction.NORTH.getRotation());
		poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		this.model.root().getExtentsForGui(poseStack, output);
	}

	public record Unbaked(float openness) implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<KeepsakeCasketSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(KeepsakeCasketSpecialRenderer.Unbaked::openness))
			.apply(instance, KeepsakeCasketSpecialRenderer.Unbaked::new));

		public Unbaked() {
			this(0.0F);
		}

		@Override
		public MapCodec<KeepsakeCasketSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public KeepsakeCasketSpecialRenderer bake(BakingContext context) {
			KeepsakeCasketModel model = new KeepsakeCasketModel(context.entityModelSet().bakeLayer(TFModelLayers.KEEPSAKE_CASKET));
			return new KeepsakeCasketSpecialRenderer(model, this.openness);
		}
	}
}
