package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.world.components.chunkgenerators.*;
import twilightforest.world.components.layer.BiomeDensitySource;
import net.minecraft.core.Registry;

@SuppressWarnings("unused")
public class TFDensityFunctions {

	public static final MapCodec<TerrainDensityRouter> BIOME_DRIVEN_TERRAIN = TerrainDensityRouter.CODEC;
	public static final MapCodec<NoiseDensityRouter> BIOME_DRIVEN_NOISE = NoiseDensityRouter.CODEC;
	public static final MapCodec<FocusedDensityFunction> FOCUSED = FocusedDensityFunction.CODEC;
	public static final MapCodec<HollowHillFunction> HOLLOW_HILL = HollowHillFunction.CODEC;
	public static final MapCodec<AbsoluteDifferenceFunction.Min> COORD_MIN = AbsoluteDifferenceFunction.Min.CODEC;
	public static final MapCodec<AbsoluteDifferenceFunction.Max> COORD_MAX = AbsoluteDifferenceFunction.Max.CODEC;
	public static final MapCodec<SqrtDensityFunction> SQRT = SqrtDensityFunction.CODEC;

	public static final ResourceKey<DensityFunction> BIOME_TERRAIN_RAW = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("raw_biome_terrain"));
	public static final ResourceKey<DensityFunction> BIOME_NOISE_RAW = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("raw_biome_noise"));
	public static final ResourceKey<DensityFunction> FORESTED_TERRAIN = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("forested_terrain"));
	public static final ResourceKey<DensityFunction> SKYLIGHT_TERRAIN = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("skylight_terrain"));

	public static void init() {
		Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, TwilightForestMod.prefix("biome_driven_terrain"), BIOME_DRIVEN_TERRAIN);
		Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, TwilightForestMod.prefix("biome_driven_noise"), BIOME_DRIVEN_NOISE);
		Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, TwilightForestMod.prefix("focused"), FOCUSED);
		Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, TwilightForestMod.prefix("hollow_hill"), HOLLOW_HILL);
		Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, TwilightForestMod.prefix("coord_min"), COORD_MIN);
		Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, TwilightForestMod.prefix("coord_max"), COORD_MAX);
		Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, TwilightForestMod.prefix("sqrt"), SQRT);
	}

	public static void bootstrap(BootstrapContext<DensityFunction> context) {
		Holder.Reference<BiomeDensitySource> biomeGrid = context.lookup(TFRegistries.Keys.BIOME_TERRAIN_DATA).getOrThrow(BiomeLayerStack.BIOME_GRID);
		DensityFunction referencedBiomeDensity = makeBiomeDensityRaw(context, biomeGrid);
		DensityFunction ambientTerrainNoise = makeAmbientNoise2D(context);
		DensityFunction referencedNoiseDensity = makeStreamDensityRaw(context, biomeGrid);

		makeForestedTerrain(context, referencedBiomeDensity, ambientTerrainNoise, referencedNoiseDensity);
		makeSkylightTerrain(context, referencedBiomeDensity, ambientTerrainNoise);
	}

	@NotNull
	private static DensityFunction makeBiomeDensityRaw(BootstrapContext<DensityFunction> context, Holder.Reference<BiomeDensitySource> biomeGrid) {
		DensityFunction rawBiomeDensityReferenced = new TerrainDensityRouter(
			biomeGrid,
			-31,
			64,
			1,
			DensityFunctions.constant(8),
			DensityFunctions.constant(-1.25F)
		);

		return new DensityFunctions.HolderHolder(context.register(BIOME_TERRAIN_RAW, rawBiomeDensityReferenced));
	}

	@NotNull
	private static DensityFunction makeAmbientNoise2D(BootstrapContext<DensityFunction> context) {
		HolderGetter<NormalNoise> noiseLookup = context.lookup(Registries.NOISE);
		Holder.Reference<NormalNoise> surfaceParams = noiseLookup.getOrThrow(Noises.SURFACE);
		Holder.Reference<NormalNoise> ridgeParams = noiseLookup.getOrThrow(Noises.RIDGE);

		DensityFunction noiseInterpolator = mulAddHalf(DensityFunctions.noise(surfaceParams, 1, 0));
		DensityFunction wideNoise = mulAddHalf(DensityFunctions.noise(ridgeParams, 1, 0));
		DensityFunction thinNoise = mulAddHalf(DensityFunctions.noise(ridgeParams, 4, 0));

		DensityFunction jitteredNoise = DensityFunctions.lerp(
			noiseInterpolator.clamp(0, 1),
			wideNoise,
			thinNoise
		);

		return DensityFunctions.cache(jitteredNoise);
	}

	@NotNull
	private static DensityFunction makeStreamDensityRaw(BootstrapContext<DensityFunction> context, Holder.Reference<BiomeDensitySource> biomeGrid) {
		DensityFunction rawStreamDensityReferenced = new NoiseDensityRouter(
			biomeGrid,
			-31,
			64,
			1
		);

		return new DensityFunctions.HolderHolder(context.register(BIOME_NOISE_RAW, rawStreamDensityReferenced));
	}

	@NotNull
	private static DensityFunction mulAddHalf(DensityFunction input) {
		return DensityFunctions.add(
			DensityFunctions.constant(0.5F),
			DensityFunctions.mul(
				DensityFunctions.constant(0.5F),
				input
			)
		);
	}

	private static void makeForestedTerrain(BootstrapContext<DensityFunction> context, DensityFunction rawBiomeDensity, DensityFunction ambientTerrainNoise, DensityFunction rawNoiseDensity) {
		DensityFunction biomedLandscape = DensityFunctions.mul(
			DensityFunctions.constant(1 / 6f),
			DensityFunctions.add(
				rawBiomeDensity,
				DensityFunctions.yClampedGradient(-31, 256, 31, -256)
			)
		);

		DensityFunction finalDensity = DensityFunctions.add(
			biomedLandscape,
			DensityFunctions.mul(
				rawNoiseDensity,
				DensityFunctions.interpolated(
					DensityFunctions.max(
						DensityFunctions.zero(),
						ambientTerrainNoise
					),
					4,
					4
				)
			)
		);

		context.register(FORESTED_TERRAIN, finalDensity.clamp(-0.1F, 0.5F));
	}

	private static void makeSkylightTerrain(BootstrapContext<DensityFunction> context, DensityFunction rawBiomeDensity, DensityFunction ambientTerrainNoise) {
		DensityFunction skyIslandNoise = DensityFunctions.add(
			DensityFunctions.constant(-0.5F),
			DensityFunctions.mul(
				DensityFunctions.add(
					DensityFunctions.constant(-0.5F),
					ambientTerrainNoise
				),
				DensityFunctions.constant(5)
			)
		);

		DensityFunction biomeDensity = DensityFunctions.mul(
			DensityFunctions.constant(-0.25F),
			DensityFunctions.mul(DensityFunctions.add(
				rawBiomeDensity,
				DensityFunctions.yClampedGradient(-31, 256, 31, -256)
			), DensityFunctions.constant(-1)).halfNegative().abs()
		);

		DensityFunction finalDensity = DensityFunctions.add(
			new SqrtDensityFunction(
				DensityFunctions.interpolated(skyIslandNoise, 4, 4).clamp(0, 2)
			),
			biomeDensity
		);

		context.register(SKYLIGHT_TERRAIN, finalDensity.clamp(-0.1F, 0.5F));
	}
}
