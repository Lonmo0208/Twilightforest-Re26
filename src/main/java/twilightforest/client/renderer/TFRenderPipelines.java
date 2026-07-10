package twilightforest.client.renderer;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import net.minecraft.client.renderer.RenderPipelines;

import twilightforest.TwilightForestMod;

public class TFRenderPipelines {

	private static final BlendFunction SHADOW = BlendFunction.TRANSLUCENT;

	public static final RenderPipeline GIANT_BLOCK_LINES = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
		.withLocation(TwilightForestMod.prefix("pipeline/giant_block_lines"))
		.withVertexShader(TwilightForestMod.prefix("core/giant_block_lines/giant_block_lines"))
		.withFragmentShader(TwilightForestMod.prefix("core/giant_block_lines/giant_block_lines"))
		.withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
		.withPrimitiveTopology(PrimitiveTopology.LINES)
		.withCull(false)
		.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
		.build();

	public static final RenderPipeline RED_THREAD = RenderPipeline.builder(RenderPipelines.BLOCK_SNIPPET)
		.withLocation(TwilightForestMod.prefix("pipeline/red_thread"))
		.withVertexBinding(0, DefaultVertexFormat.BLOCK)
		.withPrimitiveTopology(PrimitiveTopology.QUADS)
		.withCull(true)
		.withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
		.build();

	public static final RenderPipeline PROTECTION_BOX = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
		.withLocation(TwilightForestMod.prefix("pipeline/energy_swirl"))
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withShaderDefine("EMISSIVE")
		.withShaderDefine("NO_CARDINAL_LIGHTING")
		.withShaderDefine("APPLY_TEXTURE_MATRIX")
		.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
		.withCull(false)
		.withVertexBinding(0, DefaultVertexFormat.ENTITY)
		.withPrimitiveTopology(PrimitiveTopology.QUADS)
		.withDepthStencilState(DepthStencilState.DEFAULT)
		.build();

	public static final RenderPipeline SHADOW_CLONE = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
		.withLocation(TwilightForestMod.prefix("pipeline/entity_translucent_cull"))
		.withShaderDefine("ALPHA_CUTOUT", 0.1F)
		.withShaderDefine("EMISSIVE")
		.withCull(false)
		.withColorTargetState(new ColorTargetState(SHADOW))
		.build();
}
