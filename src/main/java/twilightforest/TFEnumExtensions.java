package twilightforest;

import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import twilightforest.init.TFSounds;
import twilightforest.util.ModidPrefixUtil;

/**
 * Enum extension helper for providing constructor parameters to NeoForge's enum extension system.
 * 
 * Note: In Minecraft 26.1.2, the following enums have changed:
 * - Rarity constructor: (int, String, ChatFormatting) - no longer uses UnaryOperator&lt;Style&gt;
 * - GrassColorModifier: now an abstract enum with (String) constructor - ColorModifier interface removed
 * - DamageEffects constructor: (String, SoundEvent)
 * - ItemDisplayContext constructor: (int, String) - third parameter removed
 * 
 * For Fabric port, enum extensions via enumextensions.json are not supported.
 * These methods serve as documentation of what parameters are needed for NeoForge,
 * and the enum extension classes now use Mixin-based approaches instead.
 */
@SuppressWarnings("unused") // Referenced by enumextender.json
public class TFEnumExtensions {

	private static final ModidPrefixUtil modidPrefixUtil = new ModidPrefixUtil(); // Enum extensions run before the bean context loads

	/**
	 * {@link net.minecraft.world.damagesource.DamageEffects}<p/>
	 * Constructor: DamageEffects(String id, SoundEvent sound)
	 */
	public static Object DamageEffects_PINCH(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> modidPrefixUtil.stringPrefix("pinch");
			case 1 -> TFSounds.PINCH_BEETLE_ATTACK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.item.Rarity}<p/>
	 * Constructor: Rarity(int id, String name, ChatFormatting color)
	 */
	public static Object Rarity_TWILIGHT(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> -1;
			case 1 -> modidPrefixUtil.stringPrefix("twilight");
			case 2 -> ChatFormatting.DARK_GREEN;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier}<p/>
	 * In Minecraft 26.1.2, GrassColorModifier is an abstract enum with:
	 * - Constructor: GrassColorModifier(String name)
	 * - Abstract method: modifyColor(double x, double z, int baseColor)
	 * 
	 * NeoForge's enum extension can no longer create instances via simple constructor params
	 * because the ColorModifier functional interface was removed.
	 * This method is kept for reference only.
	 */
	public static Object GrassColorModifier_ENCHANTED_FOREST(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> modidPrefixUtil.stringPrefix("enchanted_forest");
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier}<p/>
	 */
	public static Object GrassColorModifier_SWAMP(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> modidPrefixUtil.stringPrefix("swamp");
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier}<p/>
	 */
	public static Object GrassColorModifier_DARK_FOREST(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> modidPrefixUtil.stringPrefix("dark_forest");
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier}<p/>
	 */
	public static Object GrassColorModifier_DARK_FOREST_CENTER(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> modidPrefixUtil.stringPrefix("dark_forest_center");
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier}<p/>
	 */
	public static Object GrassColorModifier_SPOOKY_FOREST(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> modidPrefixUtil.stringPrefix("spooky_forest");
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.item.ItemDisplayContext}<p/>
	 * Constructor: ItemDisplayContext(int id, String name)
	 * Note: In 26.1.2, third parameter was removed.
	 * This is kept for NeoForge compatibility reference.
	 */
	public static Object ItemDisplayContext_JARRED(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> -1;
			case 1 -> modidPrefixUtil.stringPrefix("jarred");
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}
}
