package twilightforest.init.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.util.TriConsumer;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFMobEffects;
import twilightforest.init.TFSounds;
import twilightforest.util.Enforcement;
import twilightforest.util.Restriction;

public class Enforcements {

	public static final DeferredRegister<Enforcement> ENFORCEMENTS = DeferredRegister.create(TFRegistries.Keys.ENFORCEMENT, TwilightForestMod.ID);

	public static final DeferredHolder<Enforcement, Enforcement> DARKNESS = ENFORCEMENTS.register("darkness", () -> new Enforcement(new TriConsumer<>() {
		@Override
		public void accept(Player player, ServerLevel level, Restriction restriction) {
			if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
				player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, (int) restriction.multiplier(), false, true));
			}
		}
	}));

	public static final DeferredHolder<Enforcement, Enforcement> HUNGER = ENFORCEMENTS.register("hunger", () -> new Enforcement(new TriConsumer<>() {
		@Override
		public void accept(Player player, ServerLevel level, Restriction restriction) {
			if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
				MobEffectInstance currentHunger = player.getEffect(MobEffects.HUNGER);
				int hungerLevel = currentHunger != null ? currentHunger.getAmplifier() + (int) restriction.multiplier() : (int) restriction.multiplier();
				player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, hungerLevel, false, true));
			}
		}
	}));

	public static final DeferredHolder<Enforcement, Enforcement> FIRE = ENFORCEMENTS.register("fire", () -> new Enforcement(new TriConsumer<>() {
		@Override
		public void accept(Player player, ServerLevel level, Restriction restriction) {
			if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
				player.igniteForSeconds((int) restriction.multiplier());
			}
		}
	}));

	public static final DeferredHolder<Enforcement, Enforcement> FROST = ENFORCEMENTS.register("frost", () -> new Enforcement(new TriConsumer<>() {
		@Override
		public void accept(Player player, ServerLevel level, Restriction restriction) {
			if (player.tickCount % 60 == 0 && level.tickRateManager().runsNormally()) {
				player.addEffect(new MobEffectInstance(TFMobEffects.FROSTY, 100, (int) restriction.multiplier(), false, true));
			}
		}
	}));

	public static final DeferredHolder<Enforcement, Enforcement> ACID_RAIN = ENFORCEMENTS.register("acid_rain", () -> new Enforcement(new TriConsumer<>() {
		@Override
		public void accept(Player player, ServerLevel level, Restriction restriction) {
			if (player.tickCount % 5 == 0 && level.tickRateManager().runsNormally()) {
				if (player.hurtServer(level, TFDamageTypes.getDamageSource(level, TFDamageTypes.ACID_RAIN), restriction.multiplier())) {
					level.playSound(null, player.getX(), player.getY(), player.getZ(), TFSounds.ACID_RAIN_BURNS.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
				}
			}
		}
	}));
}
