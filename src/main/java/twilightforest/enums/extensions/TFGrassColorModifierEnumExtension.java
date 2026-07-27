package twilightforest.enums.extensions;

import net.minecraft.world.level.biome.BiomeSpecialEffects;
import twilightforest.beanification.Component;
import twilightforest.TFEnumExtensions;

@Component
public class TFGrassColorModifierEnumExtension {

	/**
	 * {@link TFEnumExtensions#GrassColorModifier_ENCHANTED_FOREST(int, Class)}
	 */
	public final BiomeSpecialEffects.GrassColorModifier ENCHANTED_FOREST = BiomeSpecialEffects.GrassColorModifier.NONE; // Fabric: use vanilla NONE (has grassColorOverride)

	/**
	 * {@link TFEnumExtensions#GrassColorModifier_SWAMP(int, Class)}
	 */
	public final BiomeSpecialEffects.GrassColorModifier SWAMP = BiomeSpecialEffects.GrassColorModifier.SWAMP; // Fabric: use vanilla SWAMP

	/**
	 * {@link TFEnumExtensions#GrassColorModifier_DARK_FOREST(int, Class)}
	 */
	public final BiomeSpecialEffects.GrassColorModifier DARK_FOREST = BiomeSpecialEffects.GrassColorModifier.DARK_FOREST; // Fabric: use vanilla DARK_FOREST

	/**
	 * {@link TFEnumExtensions#GrassColorModifier_DARK_FOREST_CENTER(int, Class)}
	 */
	public final BiomeSpecialEffects.GrassColorModifier DARK_FOREST_CENTER = BiomeSpecialEffects.GrassColorModifier.DARK_FOREST; // Fabric: closest vanilla equivalent

	/**
	 * {@link TFEnumExtensions#GrassColorModifier_SPOOKY_FOREST(int, Class)}
	 */
	public final BiomeSpecialEffects.GrassColorModifier SPOOKY_FOREST = BiomeSpecialEffects.GrassColorModifier.NONE; // Fabric: use vanilla NONE (has grassColorOverride)

}
