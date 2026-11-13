package twilightforest.init;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.commands.arguments.BooleanArgument;
import net.minecraft.commands.arguments.IntegerArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import twilightforest.TwilightForestMod;

public class TFGameRules {

	// 26.4-snapshot-8: GameRule's argument slot takes a SerializableArgument.ContextFree now, and the
	// value codec must carry the allowed range so /gamerule validation matches the parse bounds
	private static final MinMaxBounds.Bounds<Integer> PORTAL_DELAY_BOUNDS = MinMaxBounds.Bounds.between(0, Integer.MAX_VALUE);

	public static final GameRule<Boolean> ENFORCED_PROGRESSION_RULE = new GameRule<>(GameRuleCategory.UPDATES, GameRuleType.BOOL, BooleanArgument.INSTANCE, GameRuleTypeVisitor::visitBoolean, Codec.BOOL, b -> b ? 1 : 0, true, FeatureFlagSet.of());
	public static final GameRule<Integer> TF_PORTAL_DEFAULT_DELAY = new GameRule<>(GameRuleCategory.PLAYER, GameRuleType.INT, new IntegerArgument(PORTAL_DELAY_BOUNDS), GameRuleTypeVisitor::visitInteger, ExtraCodecs.inRange(Codec.INT, PORTAL_DELAY_BOUNDS.asRange()), i -> i, 60, FeatureFlagSet.of());
	public static final GameRule<Integer> TF_PORTAL_CREATIVE_DELAY = new GameRule<>(GameRuleCategory.PLAYER, GameRuleType.INT, new IntegerArgument(PORTAL_DELAY_BOUNDS), GameRuleTypeVisitor::visitInteger, ExtraCodecs.inRange(Codec.INT, PORTAL_DELAY_BOUNDS.asRange()), i -> i, 0, FeatureFlagSet.of());

	public static void init() {
		Registry.register(BuiltInRegistries.GAME_RULE, TwilightForestMod.prefix("twilightforest_enforced_progression"), ENFORCED_PROGRESSION_RULE);
		Registry.register(BuiltInRegistries.GAME_RULE, TwilightForestMod.prefix("players_twilight_portal_default_delay"), TF_PORTAL_DEFAULT_DELAY);
		Registry.register(BuiltInRegistries.GAME_RULE, TwilightForestMod.prefix("players_twilight_portal_creative_delay"), TF_PORTAL_CREATIVE_DELAY);
	}
}
