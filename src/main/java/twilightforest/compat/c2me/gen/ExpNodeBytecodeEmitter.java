package twilightforest.compat.c2me.gen;

// Adapted from tf-c2me-compat (MIT); see THIRD_PARTY_NOTICES.md.

import com.ishland.c2me.opts.dfc.common.ast.unary.AbstractUnaryNode;
import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeEmitter;
import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeGen;
import com.ishland.c2me.opts.dfc.common.gen.meta.ValuesMethodDefF64;
import twilightforest.compat.c2me.ast.ExpNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.InstructionAdapter;

/**
 * JVM bytecode emitter for {@link ExpNode}, mirroring dfc's
 * AbstractGenericUnaryNodeBytecodeEmitter pattern.
 */
public final class ExpNodeBytecodeEmitter implements BytecodeEmitter<ExpNode> {

	public static final ExpNodeBytecodeEmitter INSTANCE = new ExpNodeBytecodeEmitter();

	private ExpNodeBytecodeEmitter() {
	}

	@Override
	public void doBytecodeGenSingle(ExpNode node, BytecodeGen.Context context, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
		ValuesMethodDefF64 operandMethod = context.newSingleMethodF64(node.operand);
		context.callDelegateSingle(m, operandMethod);
		m.invokestatic(
				Type.getInternalName(Math.class),
				"exp",
				Type.getMethodDescriptor(Type.DOUBLE_TYPE, Type.DOUBLE_TYPE),
				false
		);
		m.areturn(Type.DOUBLE_TYPE);
	}

	@Override
	public void doBytecodeGenMulti(ExpNode node, BytecodeGen.Context context, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
		ValuesMethodDefF64 operandMethod = context.newMultiMethodF64(node.operand);
		context.callDelegateMulti(m, operandMethod);
		context.doCountedLoop(m, localVarConsumer, idx -> {
			m.load(1, InstructionAdapter.OBJECT_TYPE);
			m.load(idx, Type.INT_TYPE);
			m.dup2();
			m.aload(Type.DOUBLE_TYPE);
			m.invokestatic(
					Type.getInternalName(Math.class),
					"exp",
					Type.getMethodDescriptor(Type.DOUBLE_TYPE, Type.DOUBLE_TYPE),
					false
			);
			m.astore(Type.DOUBLE_TYPE);
		});
		m.areturn(Type.VOID_TYPE);
	}
}
