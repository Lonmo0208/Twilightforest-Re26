package twilightforest.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.init.TFDimensionData;

@Mixin(net.minecraft.client.renderer.Lightmap.class)
public class TFLightmapMixin {

	@Unique
	private static final Identifier TF_DIM_TYPE_ID = TFDimensionData.TWILIGHT_DIM_TYPE.identifier();

	// Minimum brightness for low-light/blocked areas when inside the Twilight Forest dimension.
	// Level-0 (fully dark corners) is lifted to ~12% perceived brightness, which keeps interiors
	// readable without affecting well-lit areas (level 10+) at all.
	@Unique
	private static final float TF_LOW_LIGHT_MIN_BRIGHTNESS = 0.12F;

	@Inject(method = "getBrightness", at = @At(value = "RETURN"), cancellable = true)
	private static void twilightforest$liftLowLightBrightnessForTF(net.minecraft.world.level.dimension.DimensionType dimensionType, int level, CallbackInfoReturnable<Float> cir) {
		ClientLevel level_ = Minecraft.getInstance().level;
		if (level_ == null) return;

		Identifier dimTypeId = level_.registryAccess()
			.lookupOrThrow(Registries.DIMENSION_TYPE)
			.getKey(level_.dimensionType());
		if (dimTypeId == null || !dimTypeId.equals(TF_DIM_TYPE_ID)) return;

		float original = cir.getReturnValueF();
		float minBright = TF_LOW_LIGHT_MIN_BRIGHTNESS;
		// Only lift up to level <= 6. Above that, the vanilla curve already provides plenty of light
		// so we don't mess with the gradient that the player likes outdoors.
		if (level <= 6 && original < minBright) {
			float smoothed = Mth.lerp((level / 6.0F), minBright, original);
			cir.setReturnValue(smoothed);
		}
	}
}
