package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import twilightforest.TwilightForestMod;

import java.util.function.Function;

public class TFParticleType {

	public static final SimpleParticleType LARGE_FLAME = new SimpleParticleType(false);
	public static final SimpleParticleType LEAF_RUNE = new SimpleParticleType(false);
	public static final SimpleParticleType BOSS_TEAR = new SimpleParticleType(false);
	public static final SimpleParticleType GHAST_TRAP = new SimpleParticleType(false);
	public static final SimpleParticleType PROTECTION = new SimpleParticleType(true);
	public static final SimpleParticleType SNOW = new SimpleParticleType(false);
	public static final SimpleParticleType SNOW_WARNING = new SimpleParticleType(false);
	public static final SimpleParticleType EXTENDED_SNOW_WARNING = new SimpleParticleType(false);
	public static final SimpleParticleType SNOW_GUARDIAN = new SimpleParticleType(false);
	public static final SimpleParticleType ICE_BEAM = new SimpleParticleType(false);
	public static final SimpleParticleType ANNIHILATE = new SimpleParticleType(false);
	public static final SimpleParticleType PERFECT_DODGE = new SimpleParticleType(false);
	public static final SimpleParticleType DOUBLE_JUMP = new SimpleParticleType(true);
	public static final SimpleParticleType HUGE_SMOKE = new SimpleParticleType(false);
	public static final SimpleParticleType FIREFLY = new SimpleParticleType(false);
	public static final SimpleParticleType WANDERING_FIREFLY = new SimpleParticleType(false);
	public static final SimpleParticleType PARTICLE_SPAWNER_FIREFLY = new SimpleParticleType(false);
	public static final ParticleType<ColorParticleOption> FALLEN_LEAF = new ParticleType<ColorParticleOption>(false) {
		@Override
		public MapCodec<ColorParticleOption> codec() {
			return ColorParticleOption.codec(this);
		}

		@Override
		public StreamCodec<? super RegistryFriendlyByteBuf, ColorParticleOption> streamCodec() {
			return ColorParticleOption.streamCodec(this);
		}
	};
	public static final SimpleParticleType DIM_FLAME = new SimpleParticleType(false);
	public static final SimpleParticleType OMINOUS_FLAME = new SimpleParticleType(false);
	public static final SimpleParticleType SORTING_PARTICLE = new SimpleParticleType(false);
	public static final SimpleParticleType TRANSFORMATION_PARTICLE = new SimpleParticleType(false);
	public static final SimpleParticleType LOG_CORE_PARTICLE = new SimpleParticleType(false);
	public static final SimpleParticleType CLOUD_PUFF = new SimpleParticleType(false);
	public static final ParticleType<ColorParticleOption> MAGIC_EFFECT = new ParticleType<ColorParticleOption>(false) {
		@Override
		public MapCodec<ColorParticleOption> codec() {
			return ColorParticleOption.codec(this);
		}

		@Override
		public StreamCodec<? super RegistryFriendlyByteBuf, ColorParticleOption> streamCodec() {
			return ColorParticleOption.streamCodec(this);
		}
	};
	public static final SimpleParticleType ANGRY_LICH = new SimpleParticleType(false);
	public static final SimpleParticleType TWILIGHT_ORB = new SimpleParticleType(false);
	public static final SimpleParticleType SHIELD_BREAK = new SimpleParticleType(false);
	public static final SimpleParticleType DRYING_RACK = new SimpleParticleType(false);

	public static void init() {
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("large_flame"), LARGE_FLAME);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("leaf_rune"), LEAF_RUNE);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("boss_tear"), BOSS_TEAR);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("ghast_trap"), GHAST_TRAP);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("protection"), PROTECTION);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("snow"), SNOW);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("snow_warning"), SNOW_WARNING);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("extended_snow_warning"), EXTENDED_SNOW_WARNING);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("snow_guardian"), SNOW_GUARDIAN);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("ice_beam"), ICE_BEAM);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("annihilate"), ANNIHILATE);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("perfect_dodge"), PERFECT_DODGE);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("double_jump"), DOUBLE_JUMP);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("huge_smoke"), HUGE_SMOKE);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("firefly"), FIREFLY);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("wandering_firefly"), WANDERING_FIREFLY);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("particle_spawner_firefly"), PARTICLE_SPAWNER_FIREFLY);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("fallen_leaf"), FALLEN_LEAF);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("dim_flame"), DIM_FLAME);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("ominous_flame"), OMINOUS_FLAME);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("sorting_particle"), SORTING_PARTICLE);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("transformation_particle"), TRANSFORMATION_PARTICLE);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("log_core_particle"), LOG_CORE_PARTICLE);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("cloud_puff"), CLOUD_PUFF);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("magic_effect"), MAGIC_EFFECT);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("angry_lich"), ANGRY_LICH);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("twilight_orb"), TWILIGHT_ORB);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("shield_break"), SHIELD_BREAK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, TwilightForestMod.prefix("drying_rack"), DRYING_RACK);
	}

}
