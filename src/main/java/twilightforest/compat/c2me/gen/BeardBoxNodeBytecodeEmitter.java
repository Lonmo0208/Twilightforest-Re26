package twilightforest.compat.c2me.gen;

// Adapted from tf-c2me-compat (MIT); see THIRD_PARTY_NOTICES.md.

import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeEmitter;
import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeGen;
import twilightforest.compat.c2me.ast.BeardBoxNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.InstructionAdapter;

/**
 * JVM bytecode emitter for {@link BeardBoxNode}: the generated method simply
 * calls {@link BeardBoxMath#compute}, which replicates BoxDensityFunction by
 * invoking the same vanilla Beardifier statics (bit-exact).
 */
public final class BeardBoxNodeBytecodeEmitter implements BytecodeEmitter<BeardBoxNode> {

	public static final BeardBoxNodeBytecodeEmitter INSTANCE = new BeardBoxNodeBytecodeEmitter();

	private static final String HELPER = Type.getInternalName(BeardBoxMath.class);
	// (kind, minX, minY, minZ, maxX, maxY, maxZ, x, y, z, minValue, maxValue)D
	private static final String COMPUTE_DESC = "(IIIIIIIIIIDD)D";

	private BeardBoxNodeBytecodeEmitter() {
	}

	@Override
	public void doBytecodeGenSingle(BeardBoxNode node, BytecodeGen.Context context, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
		pushArgs(node, m,
				() -> m.load(1, Type.INT_TYPE),   // x
				() -> m.load(2, Type.INT_TYPE),   // y
				() -> m.load(3, Type.INT_TYPE));  // z
		m.invokestatic(HELPER, "compute", COMPUTE_DESC, false);
		m.areturn(Type.DOUBLE_TYPE);
	}

	@Override
	public void doBytecodeGenMulti(BeardBoxNode node, BytecodeGen.Context context, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
		context.doCountedLoop(m, localVarConsumer, idx -> {
			m.load(1, InstructionAdapter.OBJECT_TYPE); // out array
			m.load(idx, Type.INT_TYPE);                // out index
			pushArgs(node, m,
					() -> loadArrayElem(m, 2, idx),    // x[i]
					() -> loadArrayElem(m, 3, idx),    // y[i]
					() -> loadArrayElem(m, 4, idx));   // z[i]
			m.invokestatic(HELPER, "compute", COMPUTE_DESC, false);
			m.astore(Type.DOUBLE_TYPE);                // out[idx] = result
		});
		m.areturn(Type.VOID_TYPE);
	}

	private interface CoordLoader {
		void load();
	}

	private static void loadArrayElem(InstructionAdapter m, int arrayLocal, int idxLocal) {
		m.load(arrayLocal, InstructionAdapter.OBJECT_TYPE);
		m.load(idxLocal, Type.INT_TYPE);
		m.aload(Type.INT_TYPE);
		m.cast(Type.INT_TYPE, Type.DOUBLE_TYPE);
	}

	private static void pushArgs(BeardBoxNode node, InstructionAdapter m, CoordLoader x, CoordLoader y, CoordLoader z) {
		m.iconst(node.kind.ordinal());
		m.iconst(node.minX);
		m.iconst(node.minY);
		m.iconst(node.minZ);
		m.iconst(node.maxX);
		m.iconst(node.maxY);
		m.iconst(node.maxZ);
		x.load();
		y.load();
		z.load();
		m.dconst(node.minValue);
		m.dconst(node.maxValue);
	}
}
