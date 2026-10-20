package twilightforest.compat.c2me.gen;

// Adapted from tf-c2me-compat (MIT); see THIRD_PARTY_NOTICES.md.

import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLCEmitter;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLCGenFunctionContext;
import twilightforest.compat.c2me.ast.BeardBoxNode;

/**
 * OpenCL C emitter for {@link BeardBoxNode}: fully inlines the box-beard math
 * (C2ME offers no hook to inject kernel helper functions), including a copy of
 * vanilla's BEARD_KERNEL — a pure exp() falloff table regenerated here with
 * the exact same formula — as kernel const data.
 */
public final class BeardBoxNodeOpenCLCEmitter implements OpenCLCEmitter<BeardBoxNode> {

	public static final BeardBoxNodeOpenCLCEmitter INSTANCE = new BeardBoxNodeOpenCLCEmitter();

	/**
	 * Bit-exact replica of vanilla's BEARD_KERNEL (vanilla lambda$static$0):
	 * fs[a*576 + b*24 + c] = computeBeardContribution(b-12, c-12, a-12)
	 *   where computeBeardContribution(x,y,z) = exp(-lengthSquared(x, y+0.5, z)/16).
	 */
	private static final float[] BEARD_KERNEL = new float[24 * 24 * 24];

	static {
		for (int a = 0; a < 24; a++) {
			for (int b = 0; b < 24; b++) {
				for (int c = 0; c < 24; c++) {
					int x = b - 12;
					int y = c - 12;
					int z = a - 12;
					double e = x * x + (y + 0.5) * (y + 0.5) + z * z;
					BEARD_KERNEL[a * 576 + b * 24 + c] = (float) Math.exp(-e / 16.0);
				}
			}
		}
	}

	private BeardBoxNodeOpenCLCEmitter() {
	}

	@Override
	public String doCLGen(BeardBoxNode node, OpenCLCGenFunctionContext context, String storeTo) {
		int tableOffset = context.getGlobalContext().allocGlobalConstDataObject(BEARD_KERNEL);

		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		sb.append("    const int xDist = max(0, max(").append(node.minX).append(" - ctx.x, ctx.x - ").append(node.maxX).append("));\n");
		sb.append("    const int zDist = max(0, max(").append(node.minZ).append(" - ctx.z, ctx.z - ").append(node.maxZ).append("));\n");
		sb.append("    const int distAboveBottom = ctx.y - ").append(node.minY).append(";\n");
		sb.append("    int yDist;\n");
		switch (node.kind) {
			case BURY, BEARD_THIN -> sb.append("    yDist = distAboveBottom;\n");
			case BEARD_BOX, ENCAPSULATE -> sb.append("    yDist = max(0, max(").append(node.minY).append(" - ctx.y, ctx.y - ").append(node.maxY).append("));\n");
			default -> sb.append("    yDist = 0;\n");
		}
		sb.append("    double densityValue;\n");
		switch (node.kind) {
			case BURY -> sb
					.append("    {\n")
					.append("        const double len = sqrt((double)xDist * (double)xDist + (double)yDist * (double)yDist * 0.25 + (double)zDist * (double)zDist);\n")
					.append("        densityValue = clamp(1.0 - len / 6.0, 0.0, 1.0);\n")
					.append("    }\n");
			case BEARD_THIN, BEARD_BOX -> {
				sb
						.append("    double beardValue;\n")
						.append("    {\n")
						.append("        const int i = xDist + 12; const int j = yDist + 12; const int k = zDist + 12;\n")
						.append("        if (i < 0 || i >= 24 || j < 0 || j >= 24 || k < 0 || k >= 24) {\n")
						.append("            beardValue = 0.0;\n")
						.append("        } else {\n")
						.append("            const double d = (double)distAboveBottom + 0.5;\n")
						.append("            const double e = (double)(xDist * xDist) + d * d + (double)(zDist * zDist);\n")
						// fastInvSqrt(-e/2), Quake bit hack, same magic constant as Mth.fastInvSqrt
						.append("            const double arg = -e / 2.0;\n")
						.append("            const double dHalf = 0.5 * arg;\n")
						.append("            double u = as_double(6910469410427058090L - (as_long(arg) >> 1));\n")
						.append("            u *= 1.5 - dHalf * u * u;\n")
						.append("            const double f = (-d) * u / 2.0;\n")
						.append("            global const float * restrict tf_kernel = ptr_shift_global(ctx.const_data, ").append(tableOffset).append(");\n")
						.append("            beardValue = f * (double)tf_kernel[k * 576 + i * 24 + j];\n")
						.append("        }\n")
						.append("    }\n")
						.append("    densityValue = beardValue * 0.8;\n");
			}
			case ENCAPSULATE -> sb
					.append("    {\n")
					.append("        const double len = sqrt((double)(xDist * xDist) * 0.25 + (double)(yDist * yDist) * 0.25 + (double)(zDist * zDist) * 0.25);\n")
					.append("        densityValue = clamp(1.0 - len / 6.0, 0.0, 1.0) * 0.8;\n")
					.append("    }\n");
			default -> sb.append("    densityValue = 0.0;\n");
		}
		sb.append("    ").append(storeTo).append(" = clamp(densityValue, ").append(node.minValue).append(", ").append(node.maxValue).append(");\n");
		sb.append("}\n");
		return sb.toString();
	}
}
