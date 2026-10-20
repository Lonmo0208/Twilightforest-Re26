package twilightforest.compat.c2me.gen;

// Adapted from tf-c2me-compat (MIT); see THIRD_PARTY_NOTICES.md.

import twilightforest.compat.c2me.ast.BeardBoxNode;
import net.minecraft.world.level.levelgen.Beardifier;

/**
 * Java-side reference implementation of TF's BoxDensityFunction.compute,
 * replicated 1:1 (calls the same vanilla Beardifier statics). Used verbatim by
 * the JVM bytecode emitter; the OpenCL emitter emits an equivalent inlined C
 * version of this logic.
 */
public final class BeardBoxMath {

	private BeardBoxMath() {
	}

	public static double compute(BeardBoxNode node, int blockX, int blockY, int blockZ) {
		int xDist = Math.max(0, Math.max(node.minX - blockX, blockX - node.maxX));
		int zDist = Math.max(0, Math.max(node.minZ - blockZ, blockZ - node.maxZ));

		int distAboveBottom = blockY - node.minY;
		int yDist = switch (node.kind) {
			case BURY, BEARD_THIN -> distAboveBottom;
			case BEARD_BOX, ENCAPSULATE -> Math.max(0, Math.max(node.minY - blockY, blockY - node.maxY));
			default -> 0;
		};

		double densityValue = switch (node.kind) {
			case BURY -> Beardifier.getBuryContribution(xDist, yDist * 0.5, zDist);
			case BEARD_THIN, BEARD_BOX -> Beardifier.getBeardContribution(xDist, yDist, zDist, distAboveBottom) * 0.8;
			case ENCAPSULATE -> Beardifier.getBuryContribution(xDist * 0.5, yDist * 0.5, zDist * 0.5) * 0.8;
			default -> 0;
		};

		return Math.clamp(densityValue, node.minValue, node.maxValue);
	}
}
