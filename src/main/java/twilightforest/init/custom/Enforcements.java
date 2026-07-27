package twilightforest.init.custom;

import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.util.TriConsumer;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFMobEffects;
import twilightforest.init.TFSounds;
import twilightforest.util.Enforcement;
import twilightforest.util.Restriction;

public class Enforcements {

	public static final Enforcement DARKNESS = new Enforcement(new TriConsumer<>() {
		@Override
		public void accept(Player player, ServerLevel level, Restriction restriction) {
			if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
				player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, (int) restriction.multiplier(), false, true));
			}
		}
	});

	public static final Enforcement HUNGER = new Enforcement(new TriConsumer<>() {
		@Override
		public void accept(Player player, ServerLevel level, Restriction restriction) {
			if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
				MobEffectInstance currentHunger = player.getEffect(MobEffects.HUNGER);
				int hungerLevel = currentHunger != null ? currentHunger.getAmplifier() + (int) restriction.multiplier() : (int) restriction.multiplier();
				player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, hungerLevel, false, true));
			}
		}
	});

	public static final Enforcement FIRE = new Enforcement(new TriConsumer<>() {
		@Override
		public void accept(Player player, ServerLevel level, Restriction restriction) {
			if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
				player.igniteForSeconds((int) restriction.multiplier());
			}
		}
	});

	public static final Enforcement FROST = new Enforcement(new TriConsumer<>() {
		@Override
		public void accept(Player player, ServerLevel level, Restriction restriction) {
			if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
				player.addEffect(new MobEffectInstance(TFMobEffects.FROSTY, 100, (int) restriction.multiplier(), false, true));
			}
		}
	});

	public static final Enforcement ACID_RAIN = new Enforcement(new TriConsumer<>() {
		@Override
		public void accept(Player player, ServerLevel level, Restriction restriction) {
			if (player.tickCount % 5 == 0 && level.tickRateManager().runsNormally()) {
				if (player.hurtServer(level, TFDamageTypes.getDamageSource(level, TFDamageTypes.ACID_RAIN), restriction.multiplier())) {
					level.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.ACID_RAIN_BURNS, SoundSource.PLAYERS, 1.0F, 1.0F);
				}
			}
		}
	});

	public static void init() {
		Registry.register(TFRegistries.ENFORCEMENT, TwilightForestMod.prefix("darkness"), DARKNESS);
		Registry.register(TFRegistries.ENFORCEMENT, TwilightForestMod.prefix("hunger"), HUNGER);
		Registry.register(TFRegistries.ENFORCEMENT, TwilightForestMod.prefix("fire"), FIRE);
		Registry.register(TFRegistries.ENFORCEMENT, TwilightForestMod.prefix("frost"), FROST);
		Registry.register(TFRegistries.ENFORCEMENT, TwilightForestMod.prefix("acid_rain"), ACID_RAIN);
	}
}