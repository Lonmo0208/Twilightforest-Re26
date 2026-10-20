package twilightforest.compat.c2me.ast;

// Adapted from tf-c2me-compat (MIT); see THIRD_PARTY_NOTICES.md.

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.AstTransformer;

/**
 * Compiled representation of Twilight Forest's {@code BoxDensityFunction}
 * (structure beard/bury adjustment over an axis-aligned box), which mirrors
 * vanilla Beardifier semantics. All parameters are constants baked at binding
 * time; coordinates come from the kernel/codegen context, so this node has no
 * AstNode children.
 */
public final class BeardBoxNode implements AstNode {

	public enum Kind {
		NONE, BURY, BEARD_THIN, BEARD_BOX, ENCAPSULATE
	}

	public final Kind kind;
	public final int minX, minY, minZ, maxX, maxY, maxZ;
	public final double minValue, maxValue;

	public BeardBoxNode(Kind kind, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, double minValue, double maxValue) {
		this.kind = kind;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	public AstNode[] getChildren() {
		return new AstNode[0];
	}

	@Override
	public AstNode transform(AstTransformer transformer) {
		return this;
	}

	@Override
	public boolean relaxedEquals(AstNode other) {
		if (!(other instanceof BeardBoxNode that)) return false;
		return this.kind == that.kind
				&& this.minX == that.minX && this.minY == that.minY && this.minZ == that.minZ
				&& this.maxX == that.maxX && this.maxY == that.maxY && this.maxZ == that.maxZ
				&& this.minValue == that.minValue && this.maxValue == that.maxValue;
	}

	@Override
	public int relaxedHashCode() {
		int result = kind.hashCode();
		result = 31 * result + minX;
		result = 31 * result + minY;
		result = 31 * result + minZ;
		result = 31 * result + maxX;
		result = 31 * result + maxY;
		result = 31 * result + maxZ;
		result = 31 * result + java.lang.Double.hashCode(minValue);
		result = 31 * result + java.lang.Double.hashCode(maxValue);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof BeardBoxNode that && this.relaxedEquals(that);
	}

	@Override
	public int hashCode() {
		return relaxedHashCode();
	}
}
