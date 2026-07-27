package twilightforest.world.components.chunkgenerators;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

/**
 * A Beardifier wrapper that also applies custom density functions from CustomDensitySource structures.
 * This allows structures like Hollow Hill, Hydra Lair, and Aurora Palace to shape terrain
 * using their own density functions (弧形山, glacier, etc.) on top of the standard beardifier carving.
 */
public class CustomDensityBeardifier implements DensityFunctions.BeardifierOrMarker {
	private final Beardifier delegate;
	private final ObjectList<DensityFunction> customDensities;

	public CustomDensityBeardifier(Beardifier delegate, ObjectList<DensityFunction> customDensities) {
		this.delegate = delegate;
		this.customDensities = customDensities;
	}

	@Override
	public double compute(FunctionContext context) {
		double original = this.delegate.compute(context);
		if (this.customDensities.isEmpty()) {
			return original;
		}
		double addedDensity = 0;
		for (int i = 0; i < this.customDensities.size(); i++) {
			addedDensity += this.customDensities.get(i).compute(context);
		}
		return original + addedDensity;
	}

	@Override
	public void fillArray(double[] output, ContextProvider contextProvider) {
		contextProvider.fillAllDirectly(output, this);
	}

	@Override
	public double minValue() {
		return this.delegate.minValue();
	}

	@Override
	public double maxValue() {
		return this.delegate.maxValue();
	}
}