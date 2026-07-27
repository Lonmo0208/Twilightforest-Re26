package twilightforest.init;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import twilightforest.TwilightForestMod;

public class TFGameRules {

	public static final GameRule<Boolean> ENFORCED_PROGRESSION_RULE = new GameRule<>(GameRuleCategory.UPDATES, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, b -> b ? 1 : 0, true, FeatureFlagSet.of());
	public static final GameRule<Integer> TF_PORTAL_DEFAULT_DELAY = new GameRule<>(GameRuleCategory.PLAYER, GameRuleType.INT, IntegerArgumentType.integer(0, Integer.MAX_VALUE), GameRuleTypeVisitor::visitInteger, Codec.INT, i -> i, 60, FeatureFlagSet.of());
	public static final GameRule<Integer> TF_PORTAL_CREATIVE_DELAY = new GameRule<>(GameRuleCategory.PLAYER, GameRuleType.INT, IntegerArgumentType.integer(0, Integer.MAX_VALUE), GameRuleTypeVisitor::visitInteger, Codec.INT, i -> i, 0, FeatureFlagSet.of());

	public static void init() {
		Registry.register(BuiltInRegistries.GAME_RULE, TwilightForestMod.prefix("twilightforest_enforced_progression"), ENFORCED_PROGRESSION_RULE);
		Registry.register(BuiltInRegistries.GAME_RULE, TwilightForestMod.prefix("players_twilight_portal_default_delay"), TF_PORTAL_DEFAULT_DELAY);
		Registry.register(BuiltInRegistries.GAME_RULE, TwilightForestMod.prefix("players_twilight_portal_creative_delay"), TF_PORTAL_CREATIVE_DELAY);
	}
}
