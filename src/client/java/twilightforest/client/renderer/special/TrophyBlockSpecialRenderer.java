package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3fc;
import twilightforest.client.model.entity.TrophyBlockModel;
import twilightforest.client.renderer.block.TrophyRenderer;
import twilightforest.enums.BossVariant;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Special renderer used for the <em>block</em> rendering path of boss trophies (e.g. when a trophy
 * is picked up / thrown as a {@link net.minecraft.world.entity.livingblock.LivingBlock}).
 *
 * <p>Unlike {@link TrophySpecialRenderer} (which positions the trophy for item displays), this
 * renderer applies no transforms of its own: the {@code SpecialBlockModelWrapper} supplies the
 * ground/wall transformation that matches {@link TrophyRenderer}'s block entity rendering, so the
 * LivingBlock trophy looks identical to the placed block. The {@code wall} flag only controls the
 * tilt angle used by {@link TrophyBlockModel#setupRotationsForTrophy}.
 */
public record TrophyBlockSpecialRenderer(BossVariant variant, boolean wall, Function<BossVariant, TrophyBlockModel> trophy) implements NoDataSpecialModelRenderer {

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		TrophyBlockModel model = this.trophy().apply(this.variant());
		if (model != null) {
			float animation = !Minecraft.getInstance().isPaused() ? (int) (Util.getMillis() / 30) + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks() : 0;
			TrophyRenderer.submitTrophy(this.wall(), model, animation, stack, collector, light, overlay, null, ItemDisplayContext.NONE);
		}
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		TrophyBlockModel model = this.trophy().apply(this.variant());
		if (model != null) {
			PoseStack poseStack = new PoseStack();
			model.setupRotationsForTrophy(0.0F, this.wall() ? 0.5F : 0.0F);
			model.getTrophyRoot().getExtentsForGui(poseStack, output);
		}
	}

	public record Unbaked(BossVariant variant, boolean wall) implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<TrophyBlockSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BossVariant.CODEC.fieldOf("kind").forGetter(TrophyBlockSpecialRenderer.Unbaked::variant),
				Codec.BOOL.optionalFieldOf("wall", false).forGetter(TrophyBlockSpecialRenderer.Unbaked::wall))
			.apply(instance, TrophyBlockSpecialRenderer.Unbaked::new));

		@Override
		public MapCodec<TrophyBlockSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(BakingContext context) {
			Function<BossVariant, TrophyBlockModel> model = Util.memoize(new Function<>() {
				@Override
				public TrophyBlockModel apply(BossVariant variant) {
					return TrophyRenderer.createTrophyModel(context.entityModelSet(), variant);
				}
			});
			return new TrophyBlockSpecialRenderer(this.variant(), this.wall(), model);
		}
	}
}
