package twilightforest.client;

import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DynamicGpuDataStorage;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.TwilightForestMod;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class TFShaders {

	private static final Logger LOGGER = LoggerFactory.getLogger("twilightforest");

	public static AuroraShaderInstance AURORA;

	public static void registerRenderPipelines() {
		// Aurora pipeline: TRANSLUCENT blending for fogged sky effect, with depth test enabled
		// so the aurora correctly culls behind solid terrain (buildings, mountains), but
		// depth writes disabled (writeDepth=false) because it is a semi-transparent sky
		// layer and should not prevent subsequent translucent geometry from rendering.
		// This mirrors the official implementation: RenderSystem.enableDepthTest() paired
		// with RenderSystem.depthMask(false)-style behavior for sky effects.
		RenderPipeline auroraPipeline = RenderPipeline.builder()
			.withLocation(TwilightForestMod.prefix("aurora/aurora"))
			.withVertexShader(TwilightForestMod.prefix("core/aurora/aurora"))
			.withFragmentShader(TwilightForestMod.prefix("core/aurora/aurora"))
			// Declare every uniform buffer the aurora shaders reference (via their vanilla
			// #include files) so the pipeline's bind group layout maps them to the GLSL UBOs.
			// Without these, the compiled pipeline would have no bindings for Globals/Fog/
			// Projection/DynamicTransforms and the sky would not render correctly.
			.withBindGroupLayout(BindGroupLayout.builder()
				.withUniform("Projection", UniformType.UNIFORM_BUFFER)
				.withUniform("Fog", UniformType.UNIFORM_BUFFER)
				.withUniform("Globals", UniformType.UNIFORM_BUFFER)
				.withUniform("Lighting", UniformType.UNIFORM_BUFFER)
				.withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
				.withUniform("AuroraSettings", UniformType.UNIFORM_BUFFER)
				.build())
			.withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
			.withPrimitiveTopology(PrimitiveTopology.QUADS)
			.withCull(false)
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			// 26.3 uses a reversed-Z depth buffer (near=1, far=0), so the depth test that
			// keeps a far-away sky quad behind solid terrain is GREATER_THAN_OR_EQUAL
			// (match vanilla DepthStencilState.DEFAULT). LESS_THAN_OR_EQUAL inverts the
			// test and lets the aurora overwrite buildings/terrain.
			.withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
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
	public record AuroraSettings(int seedContext, float posX, float posY, float posZ) implements DynamicGpuDataStorage.DynamicGpuData {
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
		private DynamicGpuDataStorage<AuroraSettings> auroraUniforms;
		private final RenderSystem.AutoStorageIndexBuffer sequentialBuffer;
		private GpuBuffer vertexBuffer;
		private int vertexCount;
		private int indexCount;
		private boolean initialized;
		private CompiledRenderPipeline compiledPipeline;

		public AuroraShaderInstance(RenderPipeline pipeline) {
			this.pipeline = pipeline;
			this.sequentialBuffer = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
			this.initialized = false;
		}

		/**
		 * Compiles the aurora pipeline into a {@link CompiledRenderPipeline} using a ShaderSource
		 * that loads our mod's GLSL files from the resource manager.
		 *
		 * <p>We must compile the pipeline ourselves instead of relying on
		 * {@code RenderSystem.getCompiledPipeline()}: the vanilla PipelineCache used by RenderSystem
		 * is backed by the vanilla ShaderManager's shader-source table, which has no entry for
		 * {@code twilightforest:core/aurora/aurora}. Asking it to compile our pipeline therefore
		 * yields {@code null} and throws "Failed to find or load pipeline".</p>
		 *
		 * <p>Compiling here is idempotent and thread-safe on the render thread: if the pipeline was
		 * already compiled it is simply reused.</p>
		 */
		private void ensureCompiled() {
			if (compiledPipeline != null && !compiledPipeline.isClosed()) {
				return;
			}
			Minecraft mc = Minecraft.getInstance();
			if (mc == null) return;
			ResourceProvider resources = mc.getResourceManager();
			if (resources == null || RenderSystem.tryGetDevice() == null) return;

			ShaderSource shaderSource = (id, type) -> readShader(resources, id, type);
			try {
				this.compiledPipeline = RenderSystem.getDevice().compilePipeline(pipeline, shaderSource);
			} catch (RuntimeException e) {
				LOGGER.error("Failed to compile aurora render pipeline {}", pipeline.getLocation(), e);
				this.compiledPipeline = null;
			}
		}

		private static String readShader(ResourceProvider resources, Identifier id, ShaderType type) {
			// Top-level shaders are keyed by their logical id (e.g. twilightforest:core/aurora/aurora) and
			// must be converted to the on-disk location (…/shaders/core/aurora/aurora.vsh). Include files
			// requested by the GLSL #include preprocessor (e.g. minecraft:shaders/include/fog.glsl) arrive
			// with a null ShaderType and are already full resource locations.
			Identifier location = type != null ? type.idConverter().idToFile(id) : id;
			try (BufferedReader reader = new BufferedReader(resources.getResourceOrThrow(location).openAsReader())) {
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append('\n');
				}
				return sb.toString();
			} catch (IOException | RuntimeException e) {
				LOGGER.error("Couldn't preload {} shader {}: {}", type, id, e);
				return null;
			}
		}

		private void ensureInitialized() {
			if (!initialized && RenderSystem.tryGetDevice() != null) {
				this.auroraUniforms = new DynamicGpuDataStorage<>("TF Aurora UBO", AuroraSettings.SIZE, GpuBuffer.USAGE_UNIFORM, 2);
				buildQuadBuffer();
				initialized = true;
			}
		}

		private void buildQuadBuffer() {
			try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(
				DefaultVertexFormat.POSITION_COLOR.getVertexSize() * 4)) {
				BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_COLOR);

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

			ensureCompiled();
			if (compiledPipeline == null || compiledPipeline.isClosed()) return;

			Minecraft mc = Minecraft.getInstance();
			GpuTextureView colorTexture = mc.gameRenderer.mainRenderTarget().getColorTextureView();
			GpuTextureView depthTexture = mc.gameRenderer.mainRenderTarget().getDepthTextureView();

			AuroraSettings settings = new AuroraSettings(seed, x, y, z);
			GpuBufferSlice auroraSlice = auroraUniforms.writeData(settings);

			// The aurora quad is placed relative to the camera (y = 256 - cameraY), so it only
			// needs the camera's view *rotation* to orient it in front of the viewer.
			//
			// IMPORTANT: build this matrix in a local copy instead of mutating the global
			// RenderSystem ModelView stack. That stack is shared with GUI item rendering; if we
			// push/identity/mul and the frame aborts before popMatrix (or a frame skips our
			// renderer), the leaked matrix would rotate every item in the inventory.
			org.joml.Matrix4f modelView = new org.joml.Matrix4f(context.levelState().cameraRenderState.viewRotationMatrix);

			GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
				.writeTransform(modelView, new org.joml.Vector4f(1.0F, 1.0F, 1.0F, 1.0F), new org.joml.Vector3f(), new org.joml.Matrix4f());

			try (RenderPass renderPass = RenderSystem.getDevice()
				.createCommandEncoder()
				.createRenderPass(() -> "TF Aurora", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty())) {
				renderPass.setPipeline(compiledPipeline);
				RenderSystem.bindDefaultUniforms(renderPass);
				renderPass.setUniform("AuroraSettings", auroraSlice);
				renderPass.setUniform("DynamicTransforms", dynamicTransforms);

				updateSkyQuad(context, intensity);

				renderPass.setVertexBuffer(0, vertexBuffer.slice());
				if (indexCount > 0) {
					GpuBuffer indices = sequentialBuffer.getBuffer(indexCount);
					renderPass.setIndexBuffer(indices, sequentialBuffer.type());
					renderPass.drawIndexed(indexCount, 1, 0, 0, 0);
				} else {
					renderPass.draw(vertexCount, 1, 0, 0);
				}
			}
		}

		private void updateSkyQuad(LevelRenderContext context, float intensity) {
			Vec3 cameraPos = context.levelState().cameraRenderState.pos;
			float y = (float) (256F - cameraPos.y);
			float scale = 2048F * (Minecraft.getInstance().options.renderDistance().get() / 32F);

			try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(
				DefaultVertexFormat.POSITION_COLOR.getVertexSize() * 4)) {
				BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_COLOR);

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
			if (compiledPipeline != null) {
				compiledPipeline.close();
				compiledPipeline = null;
			}
			if (vertexBuffer != null) {
				vertexBuffer.close();
			}
			if (auroraUniforms != null) {
				auroraUniforms.close();
			}
		}
	}
}