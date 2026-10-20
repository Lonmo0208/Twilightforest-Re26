package twilightforest.compat.c2me.bind;

// Adapted from tf-c2me-compat (MIT); see THIRD_PARTY_NOTICES.md.

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.McToAst;
import com.ishland.c2me.opts.dfc.common.ast.binary.AddNode;
import com.ishland.c2me.opts.dfc.common.ast.binary.DivNode;
import com.ishland.c2me.opts.dfc.common.ast.binary.MaxNode;
import com.ishland.c2me.opts.dfc.common.ast.binary.MinNode;
import com.ishland.c2me.opts.dfc.common.ast.binary.MulNode;
import com.ishland.c2me.opts.dfc.common.ast.misc.ConstantNode;
import com.ishland.c2me.opts.dfc.common.ast.misc.CoordinateNode;
import com.ishland.c2me.opts.dfc.common.ast.misc.RangeChoiceNode;
import com.ishland.c2me.opts.dfc.common.ast.unary.AbsNode;
import com.ishland.c2me.opts.dfc.common.ast.unary.CosNode;
import com.ishland.c2me.opts.dfc.common.ast.unary.SqrtNode;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLCGenData;
import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeGenRegistry;
import twilightforest.compat.c2me.ast.BeardBoxNode;
import twilightforest.compat.c2me.ast.ExpNode;
import twilightforest.compat.c2me.gen.BeardBoxNodeBytecodeEmitter;
import twilightforest.compat.c2me.gen.BeardBoxNodeOpenCLCEmitter;
import twilightforest.compat.c2me.gen.ExpNodeBytecodeEmitter;
import twilightforest.compat.c2me.gen.ExpNodeOpenCLCEmitter;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import twilightforest.world.components.chunkgenerators.AbsoluteDifferenceFunction;
import twilightforest.world.components.chunkgenerators.BoxDensityFunction;
import twilightforest.world.components.chunkgenerators.FocusedDensityFunction;
import twilightforest.world.components.chunkgenerators.HollowHillFunction;
import twilightforest.world.components.chunkgenerators.SqrtDensityFunction;
import twilightforest.world.components.chunkgenerators.TanhHillFunction;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps Twilight Forest's custom DensityFunctions onto C2ME dfc's AST so they
 * compile to JVM bytecode and OpenCL C instead of falling back to DelegateNode
 * (which crashes OpenCL kernel generation).
 *
 * <p>All formulas below mirror the TF implementations 1:1 in double precision.
 * Branch conditions that depend on the record's constant fields are resolved at
 * emitter time. Dynamic branches become {@link RangeChoiceNode} guards; the
 * boundaries use a large negative sentinel (-1.0e18) as "negative infinity".
 * Measure-zero boundary points may take the other branch vs. vanilla's strict
 * comparisons, which has no observable effect on terrain.
 *
 * <p>Not bound (falls back to DelegateNode):
 * <ul>
 *   <li>{@code BoxDensityFunction} — replicates vanilla's BEARD_KERNEL table
 *       lookup; needs a dedicated custom node + emitters (planned).</li>
 *   <li>{@code NoiseDensityRouter} / {@code ChunkCachedNoiseDensityRouter} —
 *       samples the biome registry per column; inherently un-representable in
 *       a stateless GPU kernel.</li>
 * </ul>
 */
public final class TfDfBindings {

	private static final Logger LOGGER = LoggerFactory.getLogger(TfDfBindings.class);

	/** "Negative infinity" sentinel for RangeChoice lower bounds. */
	private static final double NEG_INF = -1.0e18;

	private static final float PERPENDICULAR_BIAS = 1.3F;

	private TfDfBindings() {
	}

	private static AstNode c(double v) {
		return new ConstantNode(v);
	}

	private static AstNode add(AstNode a, AstNode b) {
		return new AddNode(a, b);
	}

	private static AstNode sub(AstNode a, AstNode b) {
		return new AddNode(a, new MulNode(b, c(-1.0)));
	}

	private static AstNode mul(AstNode a, AstNode b) {
		return new MulNode(a, b);
	}

	private static AstNode div(AstNode a, AstNode b) {
		return new DivNode(a, b);
	}

	private static AstNode min(AstNode a, AstNode b) {
		return new MinNode(a, b);
	}

	private static AstNode max(AstNode a, AstNode b) {
		return new MaxNode(a, b);
	}

	/** input < upperBound ? whenInRange : whenOutOfRange (inclusive lower sentinel). */
	private static AstNode ltGuard(AstNode input, double upperExclusive, AstNode whenInRange, AstNode whenOutOfRange) {
		return new RangeChoiceNode(input, NEG_INF, upperExclusive, whenInRange, whenOutOfRange);
	}

	public static void register() {
		// ExpNode: the transcendental TF needs, in both code generators.
		BytecodeGenRegistry.REGISTRY.registerExactMatch(ExpNode.class, ExpNodeBytecodeEmitter.INSTANCE);
		OpenCLCGenData.REGISTRY.registerExactMatch(ExpNode.class, ExpNodeOpenCLCEmitter.INSTANCE);

		// BeardBoxNode: compiled TF BoxDensityFunction (structure beards/bury),
		// JVM path bit-exact via vanilla statics, CL path fully inlined.
		BytecodeGenRegistry.REGISTRY.registerExactMatch(BeardBoxNode.class, BeardBoxNodeBytecodeEmitter.INSTANCE);
		OpenCLCGenData.REGISTRY.registerExactMatch(BeardBoxNode.class, BeardBoxNodeOpenCLCEmitter.INSTANCE);

		// ---- SqrtDensityFunction: sqrt(input) ----
		McToAst.REGISTRY.registerExactMatch(SqrtDensityFunction.class, f -> new SqrtNode(McToAst.toAst(f.input())));

		// ---- AbsoluteDifferenceFunction.Min: min(min(|x-cx|, |z-cz|), max) ----
		McToAst.REGISTRY.registerExactMatch(AbsoluteDifferenceFunction.Min.class, f -> min(
				min(absDiffX(adCenterX(f)), absDiffZ(adCenterZ(f))),
				c(adMax(f))
		));

		// ---- AbsoluteDifferenceFunction.Max: min(max(|x-cx|, |z-cz|), max) ----
		McToAst.REGISTRY.registerExactMatch(AbsoluteDifferenceFunction.Max.class, f -> min(
				max(absDiffX(adCenterX(f)), absDiffZ(adCenterZ(f))),
				c(adMax(f))
		));

		// ---- FocusedDensityFunction: clampedMap(dist3d, 0, radius, near, far) ----
		McToAst.REGISTRY.registerExactMatch(FocusedDensityFunction.class, f -> {
			AstNode dX = sub(CoordinateNode.AXIS_X, c(f.centerX()));
			AstNode dY = sub(CoordinateNode.AXIS_Y, c(f.bottomY()));
			AstNode dZ = sub(CoordinateNode.AXIS_Z, c(f.centerZ()));
			AstNode dist = new SqrtNode(add(add(mul(dX, dX), mul(dY, dY)), mul(dZ, dZ)));
			AstNode t = max(c(0.0), min(div(dist, c(f.radius())), c(1.0)));
			return add(c(f.nearValue()), mul(t, sub(c(f.farValue()), c(f.nearValue()))));
		});

		// ---- HollowHillFunction ----
		McToAst.REGISTRY.registerExactMatch(HollowHillFunction.class, f -> {
			AstNode dX = sub(CoordinateNode.AXIS_X, c(f.centerX()));
			AstNode dY = sub(CoordinateNode.AXIS_Y, c(f.bottomY()));
			AstNode dZ = sub(CoordinateNode.AXIS_Z, c(f.centerZ()));
			AstNode dist = new SqrtNode(add(mul(dX, dX), mul(dZ, dZ)));
			// height = cos(dist / radius * PI) * radius * 0.3333333334
			AstNode height = mul(new CosNode(mul(div(dist, c(f.radius())), c(Math.PI))), mul(c(f.radius()), c(0.3333333334)));
			// normalizedDist = clamp(dist / |radius|, 0, 1); >= 1 -> 0
			AstNode normalizedDist = max(c(0.0), min(div(dist, c(Math.abs(f.radius()))), c(1.0)));
			AstNode value = max(c(-1.0), min(sub(mul(height, c(f.heightScale())), dY), c(1.0)));
			return ltGuard(normalizedDist, 1.0, value, c(0.0));
		});

		// ---- TanhHillFunction ----
		McToAst.REGISTRY.registerExactMatch(TanhHillFunction.class, f -> {
			AstNode dX = sub(CoordinateNode.AXIS_X, c(f.centerX()));
			AstNode dY = sub(CoordinateNode.AXIS_Y, c(f.bottomY()));
			AstNode dZ = sub(CoordinateNode.AXIS_Z, c(f.centerZ()));
			// Make mounds more parallel to the trunk axis in shape
			AstNode dXe = f.isXOriented() ? dX : mul(dX, c(PERPENDICULAR_BIAS));
			AstNode dZe = f.isXOriented() ? mul(dZ, c(PERPENDICULAR_BIAS)) : dZ;
			AstNode perpendicular = f.isXOriented() ? dZe : dXe;
			if (!f.isOnRightSide()) {
				perpendicular = mul(perpendicular, c(-1.0));
			}
			// if (perpendicularDist < 0) return -1;
			AstNode body = bodyForTanhHill(f, dXe, dZe, dY);
			return ltGuard(perpendicular, 0.0, c(-1.0), body);
		});

		// ---- BoxDensityFunction: compiled structure beard/bury box ----
		McToAst.REGISTRY.registerExactMatch(BoxDensityFunction.class, f -> new BeardBoxNode(
				kindOf(adAdjust(f)),
				(int) BF_MIN_X.get(f), (int) BF_MIN_Y.get(f), (int) BF_MIN_Z.get(f),
				(int) BF_MAX_X.get(f), (int) BF_MAX_Y.get(f), (int) BF_MAX_Z.get(f),
				(double) BF_MIN_VALUE.get(f), (double) BF_MAX_VALUE.get(f)
		));

		LOGGER.info("[TF-C2ME-Compat] Registered Twilight Forest density function bindings into C2ME dfc");
	}

	private static BeardBoxNode.Kind kindOf(TerrainAdjustment adjustment) {
		return switch (adjustment) {
			case BURY -> BeardBoxNode.Kind.BURY;
			case BEARD_THIN -> BeardBoxNode.Kind.BEARD_THIN;
			case BEARD_BOX -> BeardBoxNode.Kind.BEARD_BOX;
			case ENCAPSULATE -> BeardBoxNode.Kind.ENCAPSULATE;
			default -> BeardBoxNode.Kind.NONE;
		};
	}

	private static AstNode bodyForTanhHill(TanhHillFunction f, AstNode dX, AstNode dZ, AstNode dY) {
		// if (dY > 10 || dY < -10) return -1;
		AstNode inYRange = bodyForTanhHill0(f, dX, dZ, dY);
		return ltGuard(mul(dY, c(-1.0)), 10.0,
				ltGuard(dY, 10.0, inYRange, c(-1.0)),
				c(-1.0));
	}

	private static AstNode bodyForTanhHill0(TanhHillFunction f, AstNode dX, AstNode dZ, AstNode dY) {
		AstNode distSquared = add(mul(dX, dX), mul(dZ, dZ));
		// if (distSquared > 9 * radius^2) return -1;
		AstNode inRadius = hillHeight(f, dX, dZ, distSquared, dY);
		return ltGuard(distSquared, 9.0 * f.radius() * f.radius(), inRadius, c(-1.0));
	}

	private static AstNode hillHeight(TanhHillFunction f, AstNode dX, AstNode dZ, AstNode distSquared, AstNode dY) {
		AstNode dist = new SqrtNode(distSquared);
		// cosPhi = dX / dist; sinPhi = dZ / dist  (dist > 0 guaranteed by the
		// radius guard except at the exact center, where the ratio is 0/0 —
		// vanilla computes the same 0/0 in float and discards it via heightScale
		// comparison; the kernel yields NaN->false identically in IEEE)
		AstNode cosPhi = div(dX, dist);
		AstNode sinPhi = div(dZ, dist);
		AstNode cos2Phi = sub(mul(cosPhi, cosPhi), mul(sinPhi, sinPhi));
		AstNode sin2Phi = mul(mul(cosPhi, sinPhi), c(2.0));
		// dist /= (1 + (sin2Phi * cosBias - cos2Phi * sinBias) / 3)
		AstNode biased = div(dist,
				add(c(1.0), div(sub(mul(sin2Phi, c(f.cosAngleBiasDirection())), mul(cos2Phi, c(f.sinAngleBiasDirection()))), c(3.0))));
		// getHeight(dist):
		//   distOverR = dist / radius; t = 4 - 4 * distOverR
		AstNode t = sub(c(4.0), div(mul(biased, c(4.0)), c(f.radius())));
		//   height = (e^t - 1) / (e^t + 1)  [= tanh(t / 2)]
		AstNode expT = new ExpNode(t);
		AstNode height = div(sub(expT, c(1.0)), add(expT, c(1.0)));
		//   beardArg = -dist * radius * 0.01; factor = beardArg < -10 ? 1 : 1 - e^beardArg
		AstNode beardArg = mul(mul(biased, c(-f.radius())), c(0.01));
		AstNode factor = ltGuard(beardArg, -10.0, c(1.0), sub(c(1.0), new ExpNode(beardArg)));
		height = sub(height, mul(biased, factor));
		// return heightScale * (height + 1) > dY ? 1 : -1;
		AstNode test = sub(mul(c(f.heightScale()), add(height, c(1.0))), dY);
		return ltGuard(mul(test, c(-1.0)), 0.0, c(1.0), c(-1.0));
	}

	// AbsoluteDifference helpers need the protected fields via reflection.
	private static final VarHandle AD_MAX;
	private static final VarHandle AD_CENTER_X;
	private static final VarHandle AD_CENTER_Z;

	// BoxDensityFunction fields are private; read them once via VarHandles.
	private static final VarHandle BF_MIN_X, BF_MIN_Y, BF_MIN_Z, BF_MAX_X, BF_MAX_Y, BF_MAX_Z, BF_MIN_VALUE, BF_MAX_VALUE, BF_ADJUSTMENT;

	static {
		try {
			MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(AbsoluteDifferenceFunction.class, MethodHandles.lookup());
			AD_MAX = lookup.findVarHandle(AbsoluteDifferenceFunction.class, "max", double.class);
			AD_CENTER_X = lookup.findVarHandle(AbsoluteDifferenceFunction.class, "centerX", double.class);
			AD_CENTER_Z = lookup.findVarHandle(AbsoluteDifferenceFunction.class, "centerZ", double.class);

			MethodHandles.Lookup bfLookup = MethodHandles.privateLookupIn(BoxDensityFunction.class, MethodHandles.lookup());
			BF_MIN_X = bfLookup.findVarHandle(BoxDensityFunction.class, "minX", int.class);
			BF_MIN_Y = bfLookup.findVarHandle(BoxDensityFunction.class, "minY", int.class);
			BF_MIN_Z = bfLookup.findVarHandle(BoxDensityFunction.class, "minZ", int.class);
			BF_MAX_X = bfLookup.findVarHandle(BoxDensityFunction.class, "maxX", int.class);
			BF_MAX_Y = bfLookup.findVarHandle(BoxDensityFunction.class, "maxY", int.class);
			BF_MAX_Z = bfLookup.findVarHandle(BoxDensityFunction.class, "maxZ", int.class);
			BF_MIN_VALUE = bfLookup.findVarHandle(BoxDensityFunction.class, "minValue", double.class);
			BF_MAX_VALUE = bfLookup.findVarHandle(BoxDensityFunction.class, "maxValue", double.class);
			BF_ADJUSTMENT = bfLookup.findVarHandle(BoxDensityFunction.class, "terrainAdjustment", TerrainAdjustment.class);
		} catch (ReflectiveOperationException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private static TerrainAdjustment adAdjust(Object f) {
		return (TerrainAdjustment) BF_ADJUSTMENT.get(f);
	}

	private static double adMax(Object f) {
		return (double) AD_MAX.get(f);
	}

	private static double adCenterX(Object f) {
		return (double) AD_CENTER_X.get(f);
	}

	private static double adCenterZ(Object f) {
		return (double) AD_CENTER_Z.get(f);
	}

	private static AstNode absDiffX(double centerX) {
		return new AbsNode(sub(CoordinateNode.AXIS_X, c(centerX)));
	}

	private static AstNode absDiffZ(double centerZ) {
		return new AbsNode(sub(CoordinateNode.AXIS_Z, c(centerZ)));
	}
}
