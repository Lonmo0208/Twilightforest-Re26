package twilightforest.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFDimensionData;

@Mixin(LightmapRenderStateExtractor.class)
public class TFLightmapStateMixin {

	@Shadow @Final private Minecraft minecraft;

	@Unique
	private static final Identifier TF_DIM_TYPE_ID = TFDimensionData.TWILIGHT_DIM_TYPE.identifier();

	// Lightmap uniform adjustment — only applied inside the Twilight Forest dimension.
	//
	// Strategy:
	//  - only lift the ABSOLUTE MINIMUM for truly pitch-black areas (player should not
	//    be 100% blind in a corner) — otherwise caves and underground mood stays intact.
	//  - brightness floor: 0.04 (≈ level-0 perceived brightness, can barely see shapes)
	//  - ambient boost: 0.06 — tiny nudge so fully-dark corners aren't flat black,
	//    but won't wash out a torch's dark surroundings.
	//  - both only kick in when the original values are *below* that floor (i.e. only
	//    the lowest-end), never dimming already-lit areas.
	@Unique
	private static final float TF_INDOOR_BRIGHTNESS_FLOOR = 0.04F;
	@Unique
	private static final float TF_AMBIENT_BOOST_MAX = 0.06F;

	@Inject(method = "extract", at = @At(value = "TAIL"))
	private void twilightforest$liftDarknessFloorForTF(LightmapRenderState renderState, float partialTicks, CallbackInfo ci) {
		ClientLevel level = this.minecraft.level;
		if (level == null) return;

		// restrict to Twilight Forest dimension type only
		Identifier dimTypeId = level.registryAccess()
			.lookupOrThrow(Registries.DIMENSION_TYPE)
			.getKey(level.dimensionType());
		if (dimTypeId == null || !dimTypeId.equals(TF_DIM_TYPE_ID)) return;

		// 1) brightness — only raise the floor if original is BELOW our very dim threshold;
		//    never clamp well-lit scenes.
		if (renderState.brightness < TF_INDOOR_BRIGHTNESS_FLOOR) {
			renderState.brightness = TF_INDOOR_BRIGHTNESS_FLOOR;
		}

		// 2) ambient color — add a small boost only proportional to how dark the original
		//    ambient already is; bright areas (sunlit outdoors) get zero extra.
		float avgAmbient = (renderState.ambientColor.x() + renderState.ambientColor.y() + renderState.ambientColor.z()) / 3.0F;
		float boostFactor = Math.max(0.0F, 1.0F - avgAmbient / 0.25F); // full boost only when avg < 0.25
		if (boostFactor > 0.0F) {
			float boost = TF_AMBIENT_BOOST_MAX * boostFactor;
			float r = Math.min(1.0F, renderState.ambientColor.x() + boost);
			float g = Math.min(1.0F, renderState.ambientColor.y() + boost);
			float b = Math.min(1.0F, renderState.ambientColor.z() + boost);
			renderState.ambientColor = new Vector3f(r, g, b);
		}
	}
}
