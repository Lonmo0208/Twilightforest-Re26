package twilightforest.client.renderer;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import twilightforest.TwilightForestMod;

public class TFRenderPipelines {

	private static final BlendFunction SHADOW = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);

	public static final RenderPipeline RED_THREAD = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
		.withLocation(TwilightForestMod.prefix("pipeline/red_thread"))
		.withVertexShader(TwilightForestMod.prefix("pipeline/red_thread"))
		.withFragmentShader(TwilightForestMod.prefix("pipeline/red_thread"))
		.withVertexBinding(0, DefaultVertexFormat.ENTITY)
		.withPrimitiveTopology(PrimitiveTopology.QUADS)
		.withBindGroupLayout(BindGroupLayouts.SAMPLER0)
		.withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
		.withCull(false)
		.build();

	public static final RenderPipeline PROTECTION_BOX = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
		.withLocation("pipeline/energy_swirl")
		.withVertexShader("core/entity")
		.withFragmentShader("core/entity")
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withShaderDefine("EMISSIVE")
		.withShaderDefine("NO_CARDINAL_LIGHTING")
		.withShaderDefine("APPLY_TEXTURE_MATRIX")
		.withBindGroupLayout(BindGroupLayouts.SAMPLER0)
		.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
		.withCull(false)
		.withVertexBinding(0, DefaultVertexFormat.ENTITY)
		.withPrimitiveTopology(PrimitiveTopology.QUADS)
		.withDepthStencilState(DepthStencilState.DEFAULT)
		.build();

	public static final RenderPipeline SHADOW_CLONE = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
		.withLocation("pipeline/entity_translucent_cull")
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withBindGroupLayout(BindGroupLayouts.SAMPLER1)
		.withCull(false)
		.withColorTargetState(new ColorTargetState(SHADOW))
		.build();
}
