package twilightforest.client;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.TFRenderPipelines;

public class TFShaders {

	public static PositionAwareShaderInstance AURORA;

	public static void registerRenderPipelines(RegisterRenderPipelinesEvent event) {
		RenderPipeline auroraPipeline = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
			.withLocation(TwilightForestMod.prefix("aurora/aurora"))
			.withBindGroupLayout(BindGroupLayout.builder()
				.withUniform("TFRenderParameters", UniformType.UNIFORM_BUFFER)
				.build())
			.withVertexShader(TwilightForestMod.prefix("core/aurora/aurora"))
			.withFragmentShader(TwilightForestMod.prefix("core/aurora/aurora"))
			.withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
			.withPrimitiveTopology(PrimitiveTopology.QUADS)
			.withCull(false)
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			.build();
		event.registerPipeline(auroraPipeline);
		AURORA = new PositionAwareShaderInstance(auroraPipeline);
		event.registerPipeline(TFRenderPipelines.GIANT_BLOCK_LINES);
		event.registerPipeline(TFRenderPipelines.RED_THREAD);
		event.registerPipeline(TFRenderPipelines.PROTECTION_BOX);
		event.registerPipeline(TFRenderPipelines.SHADOW_CLONE);
	}

	@Nullable
	private static RenderPipeline activePipeline;

	@Nullable
	public static RenderPipeline getActivePipeline() {
		return activePipeline;
	}

	static void setActivePipeline(@Nullable RenderPipeline pipeline) {
		activePipeline = pipeline;
	}

	public static class BindableShaderInstance {

		protected final RenderPipeline pipeline;
		@Nullable
		private RenderPipeline last;

		public BindableShaderInstance(RenderPipeline pipeline) {
			this.pipeline = pipeline;
		}

		RenderPipeline getPipeline() {
			return this.pipeline;
		}

		public final void bind(@Nullable Runnable exec) {
			this.last = activePipeline;
			activePipeline = this.pipeline;
			if (exec != null)
				exec.run();
		}

		public final void runThenClear(Runnable exec) {
			exec.run();
			activePipeline = this.last;
			this.last = null;
		}

		public final void invokeThenClear(@Nullable Runnable execBind, Runnable execPost) {
			bind(execBind);
			runThenClear(execPost);
		}

		public final void invokeThenClear(Runnable execPost) {
			invokeThenClear(null, execPost);
		}

		public final void invokeThenEndTesselator(@Nullable Runnable execBind, BufferBuilder builder) {
			invokeThenClear(execBind, () -> {
				builder.buildOrThrow();
			});
		}

		public final void invokeThenEndTesselator(BufferBuilder builder) {
			invokeThenClear(() -> {
				builder.buildOrThrow();
			});
		}

	}

	public static class PositionAwareShaderInstance extends BindableShaderInstance {

		private int seedValue;
		private float posX;
		private float posY;
		private float posZ;

		public PositionAwareShaderInstance(RenderPipeline pipeline) {
			super(pipeline);
		}

		public final void setValue(int seed, float x, float y, float z) {
			this.seedValue = seed;
			this.posX = x;
			this.posY = y;
			this.posZ = z;
		}

		public final void setValueBindApply(int seed, float x, float y, float z) {
			bind(() -> setValue(seed, x, y, z));
		}

		public final void reset() {
			setValue(0, 0, 0, 0);
		}

		public final void resetClear() {
			runThenClear(this::reset);
		}

		public final void invokeThenClear(int seed, float x, float y, float z, Runnable exec) {
			setValueBindApply(seed, x, y, z);
			exec.run();
			resetClear();
		}

		public final void invokeThenEndTesselator(int seed, float x, float y, float z, BufferBuilder builder) {
			invokeThenClear(seed, x, y, z, () -> {
				builder.buildOrThrow();
			});
		}

	}

}
