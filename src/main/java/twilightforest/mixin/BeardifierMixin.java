package twilightforest.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.TwilightForestMod;
import twilightforest.asmhooks.WorldgenHooks;
import twilightforest.world.components.structures.CustomDensitySource;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@Mixin(Beardifier.class)
public class BeardifierMixin {

	@Unique
	private static final Map<Beardifier, ObjectList<DensityFunction>> CUSTOM_DENSITIES = Collections.synchronizedMap(new WeakHashMap<>());

	@Unique
	private static int debugCounter = 0;

	@Inject(method = "forStructuresInChunk", at = @At("RETURN"), cancellable = true)
	private static void tf$addPieceBeardifierModifiers(StructureManager structureManager, ChunkPos chunkPos, CallbackInfoReturnable<Beardifier> cir) {
		Beardifier original = cir.getReturnValue();

		ObjectList<DensityFunction> customDensities = new ObjectArrayList<>(10);
		for (StructureStart start : structureManager.startsForStructure(chunkPos, s -> s instanceof CustomDensitySource)) {
			if (start.getStructure() instanceof CustomDensitySource customDensitySource) {
				DensityFunction terraformer = customDensitySource.getStructureTerraformer(chunkPos, start);
				customDensities.add(terraformer);
				if (debugCounter++ % 100 == 0) {
					TwilightForestMod.LOGGER.info("TF-BeardifierMixin: Added custom density from {} for chunk ({},{})",
						start.getStructure().getClass().getSimpleName(), chunkPos.x(), chunkPos.z());
				}
			}
		}

		Beardifier result = WorldgenHooks.addPieceBeardifierModifiers(structureManager, chunkPos, original);

		if (!customDensities.isEmpty()) {
			CUSTOM_DENSITIES.put(result, customDensities);
		}

		cir.setReturnValue(result);
	}

	@Inject(method = "compute", at = @At("RETURN"), cancellable = true)
	private void tf$addCustomDensity(DensityFunction.FunctionContext context, CallbackInfoReturnable<Double> cir) {
		ObjectList<DensityFunction> densities = CUSTOM_DENSITIES.get(this);
		if (densities != null && !densities.isEmpty()) {
			double original = cir.getReturnValue();
			double added = 0;
			for (int i = 0; i < densities.size(); i++) {
				added += densities.get(i).compute(context);
			}
			if (debugCounter++ % 100 == 0) {
				TwilightForestMod.LOGGER.info("TF-BeardifierMixin: Custom density applied: original={}, added={}, result={}",
					original, added, original + added);
			}
			cir.setReturnValue(original + added);
		}
	}

	/**
	 * When the Beardifier has no affectedBox (e.g., EMPTY instance), fillArray fills with 0.0
	 * and never calls compute(), so our custom densities are never applied.
	 * This injection forces fillArray to use the default DensityFunctions.BeardifierOrMarker
	 * implementation which calls compute() for each element, ensuring custom densities take effect.
	 */
	@Inject(method = "fillArray", at = @At("HEAD"), cancellable = true)
	private void tf$fillArrayWithCustomDensity(double[] output, DensityFunction.ContextProvider contextProvider, CallbackInfo ci) {
		ObjectList<DensityFunction> densities = CUSTOM_DENSITIES.get(this);
		if (densities != null && !densities.isEmpty()) {
			contextProvider.fillAllDirectly(output, (DensityFunction) (Object) this);
			ci.cancel();
		}
	}
}