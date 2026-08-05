package twilightforest.client;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DynamicUniformStorage;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;

import java.nio.ByteBuffer;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class TFShaders {

	public static RenderPipeline RED_THREAD;
	public static AuroraShaderInstance AURORA;

	public static void registerRenderPipelines() {
		RED_THREAD = RenderPipeline.builder(RenderPipelines.BLOCK_SNIPPET)
			.withLocation(TwilightForestMod.prefix("red_thread/red_thread"))
			.build();

		// Aurora pipeline: TRANSLUCENT blending for fogged sky effect, with depth test enabled
		// so the aurora correctly culls behind solid terrain (buildings, mountains), but
		// depth writes disabled (writeDepth=false) because it is a semi-transparent sky
		// layer and should not prevent subsequent translucent geometry from rendering.
		// This mirrors the official implementation: RenderSystem.enableDepthTest() paired
		// with RenderSystem.depthMask(false)-style behavior for sky effects.
		RenderPipeline auroraPipeline = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
			.withLocation(TwilightForestMod.prefix("aurora/aurora"))
			.withVertexShader(TwilightForestMod.prefix("core/aurora/aurora"))
			.withFragmentShader(TwilightForestMod.prefix("core/aurora/aurora"))
			.withUniform("AuroraSettings", UniformType.UNIFORM_BUFFER)
			.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
			.withCull(false)
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
			.build();
		AURORA = new AuroraShaderInstance(auroraPipeline);
	}

	/**
	 * AuroraSettings uniform buffer structure matching the shader definition:
	 * layout(std140) uniform AuroraSettings {
	 *     int seedContext;
	 *     vec3 positionContext;
	 * };
	 */
	public record AuroraSettings(int seedContext, float posX, float posY, float posZ) implements DynamicUniformStorage.DynamicUniform {
		public static final int SIZE = new Std140SizeCalculator()
			.putInt()      // seedContext: 4 bytes
			.putVec3()     // positionContext: 16 bytes
			.get();

		@Override
		public void write(ByteBuffer buffer) {
			Std140Builder.intoBuffer(buffer)
				.putInt(this.seedContext)
				.putVec3(this.posX, this.posY, this.posZ);
		}
	}

	public static class AuroraShaderInstance {
		private final RenderPipeline pipeline;
		private DynamicUniformStorage<AuroraSettings> auroraUniforms;
		private final RenderSystem.AutoStorageIndexBuffer sequentialBuffer;
		private GpuBuffer vertexBuffer;
		private int vertexCount;
		private int indexCount;
		private boolean initialized;

		public AuroraShaderInstance(RenderPipeline pipeline) {
			this.pipeline = pipeline;
			this.sequentialBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
			this.initialized = false;
		}

		private void ensureInitialized() {
			if (!initialized && RenderSystem.tryGetDevice() != null) {
				this.auroraUniforms = new DynamicUniformStorage<>("TF Aurora UBO", AuroraSettings.SIZE, 2);
				buildQuadBuffer();
				initialized = true;
			}
		}

		private void buildQuadBuffer() {
			try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(
				DefaultVertexFormat.POSITION_COLOR.getVertexSize() * 4)) {
				BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

				bufferBuilder.addVertex(-1F, 1F, 0F).setColor(1F, 1F, 1F, 1F);
				bufferBuilder.addVertex(-1F, -1F, 0F).setColor(1F, 1F, 1F, 1F);
				bufferBuilder.addVertex(1F, -1F, 0F).setColor(1F, 1F, 1F, 1F);
				bufferBuilder.addVertex(1F, 1F, 0F).setColor(1F, 1F, 1F, 1F);

				try (MeshData mesh = bufferBuilder.buildOrThrow()) {
					this.vertexCount = mesh.drawState().vertexCount();
					this.indexCount = mesh.drawState().indexCount();
					this.vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "TF Aurora vertices", 40, mesh.vertexBuffer());
				}
			}
		}

		public void renderAurora(LevelRenderContext context, int seed, float x, float y, float z, float intensity) {
			ensureInitialized();
			if (pipeline == null || !initialized) return;

			Minecraft mc = Minecraft.getInstance();
			GpuTextureView colorTexture = mc.getMainRenderTarget().getColorTextureView();
			GpuTextureView depthTexture = mc.getMainRenderTarget().getDepthTextureView();

			AuroraSettings settings = new AuroraSettings(seed, x, y, z);
			GpuBufferSlice auroraSlice = auroraUniforms.writeUniform(settings);

			// The aurora quad is placed in world space (centered on the camera at y=256),
			// so it must be transformed by the camera's view rotation matrix to appear in the sky.
			// Using an identity matrix here would render it directly in screen space.
			org.joml.Matrix4fStack matrix = RenderSystem.getModelViewStack();
			matrix.pushMatrix();
			matrix.identity();
			matrix.mul(context.levelState().cameraRenderState.viewRotationMatrix);

			GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
				.writeTransform(matrix, new org.joml.Vector4f(1.0F, 1.0F, 1.0F, 1.0F), new org.joml.Vector3f(), new org.joml.Matrix4f());

			try (RenderPass renderPass = RenderSystem.getDevice()
				.createCommandEncoder()
				.createRenderPass(() -> "TF Aurora", colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty())) {
				renderPass.setPipeline(pipeline);
				RenderSystem.bindDefaultUniforms(renderPass);
				renderPass.setUniform("AuroraSettings", auroraSlice);
				renderPass.setUniform("DynamicTransforms", dynamicTransforms);

				updateSkyQuad(context, intensity);

				renderPass.setVertexBuffer(0, vertexBuffer);
				if (indexCount > 0) {
					GpuBuffer indices = sequentialBuffer.getBuffer(indexCount);
					renderPass.setIndexBuffer(indices, sequentialBuffer.type());
					renderPass.drawIndexed(0, 0, indexCount, 1);
				} else {
					renderPass.draw(0, vertexCount);
				}
			}

			matrix.popMatrix();
		}

		private void updateSkyQuad(LevelRenderContext context, float intensity) {
			Vec3 cameraPos = context.levelState().cameraRenderState.pos;
			float y = (float) (256F - cameraPos.y);
			float scale = 2048F * (Minecraft.getInstance().options.renderDistance().get() / 32F);

			try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(
				DefaultVertexFormat.POSITION_COLOR.getVertexSize() * 4)) {
				BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

				bufferBuilder.addVertex(-scale, y, scale).setColor(1F, 1F, 1F, intensity);
				bufferBuilder.addVertex(-scale, y, -scale).setColor(1F, 1F, 1F, intensity);
				bufferBuilder.addVertex(scale, y, -scale).setColor(1F, 1F, 1F, intensity);
				bufferBuilder.addVertex(scale, y, scale).setColor(1F, 1F, 1F, intensity);

				try (MeshData mesh = bufferBuilder.buildOrThrow()) {
					this.vertexCount = mesh.drawState().vertexCount();
					this.indexCount = mesh.drawState().indexCount();
					if (this.vertexBuffer != null) {
						this.vertexBuffer.close();
					}
					this.vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "TF Aurora vertices", 40, mesh.vertexBuffer());
				}
			}
		}

		public void endFrame() {
			if (auroraUniforms != null) {
				auroraUniforms.endFrame();
			}
		}

		public void close() {
			if (vertexBuffer != null) {
				vertexBuffer.close();
			}
			if (auroraUniforms != null) {
				auroraUniforms.close();
			}
		}
	}
}