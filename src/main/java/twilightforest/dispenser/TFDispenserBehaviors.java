package twilightforest.dispenser;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DispenseBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import twilightforest.TwilightForestMod;

import java.util.List;

/**
 * Dispense behaviors are data-driven since 26.4-snapshot-9: every item carries a
 * {@code minecraft:dispense_behavior} component pointing at a list in the
 * {@code <namespace>:dispense_behavior} datapack registry. Custom behavior instances are
 * referenced from those JSON lists by their registered behavior type.
 */
public class TFDispenserBehaviors {

	public static final ResourceKey<List<DispenseBehavior>> MOONWORM_QUEEN = key("moonworm_queen");
	public static final ResourceKey<List<DispenseBehavior>> TWILIGHT_SCEPTER = key("twilight_scepter");
	public static final ResourceKey<List<DispenseBehavior>> FEATHER_FAN = key("feather_fan");
	public static final ResourceKey<List<DispenseBehavior>> CRUMBLE_HORN = key("crumble_horn");
	public static final ResourceKey<List<DispenseBehavior>> TRANSFORMATION_POWDER = key("transformation_powder");
	/**
	 * Equip-only list without the vanilla {@code drop_item} fallback, matching the old
	 * OptionalDispenseItemBehavior semantics for trophies, skull candles and bugs.
	 */
	public static final ResourceKey<List<DispenseBehavior>> EQUIP_ONLY = key("equip_only");

	private static ResourceKey<List<DispenseBehavior>> key(String name) {
		return ResourceKey.create(Registries.DISPENSE_BEHAVIOR, TwilightForestMod.prefix(name));
	}

	public static void init() {
		register("moonworm_shot", MoonwormShotDispenseBehavior.CODEC);
		register("twilight_wand_bolt", TwilightWandBoltDispenseBehavior.CODEC);
		register("feather_fan", FeatherFanDispenseBehavior.CODEC);
		register("crumble_horn", CrumbleDispenseBehavior.CODEC);
		register("transformation_powder", TransformationDispenseBehavior.CODEC);
		register("candle", CandleDispenseBehavior.CODEC);
	}

	private static void register(String name, MapCodec<? extends DispenseBehavior> codec) {
		Registry.register(BuiltInRegistries.DISPENSE_BEHAVIOR_TYPE, TwilightForestMod.prefix(name), codec);
	}
}
