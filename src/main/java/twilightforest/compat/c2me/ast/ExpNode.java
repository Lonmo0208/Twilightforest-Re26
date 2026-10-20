package twilightforest.compat.c2me.ast;

// Adapted from tf-c2me-compat (MIT); see THIRD_PARTY_NOTICES.md.

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.unary.AbstractUnaryNode;

/**
 * e^x — the missing transcendental node in dfc's AST. Needed to express
 * Twilight Forest's TanhHillFunction (which mixes tanh and exp terms)
 * in compiled density functions, including OpenCL kernels.
 */
public class ExpNode extends AbstractUnaryNode {

	public ExpNode(AstNode operand) {
		super(operand);
	}

	@Override
	protected AstNode newInstance(AstNode operand) {
		return new ExpNode(operand);
	}
}
