package twilightforest.world.registration.surface_rules;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFBlocks;

public class TFSurfaceRules {
	private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
	private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
	private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
	private static final SurfaceRules.RuleSource PODZOL = makeStateRule(Blocks.PODZOL);
	private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
	private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
	private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
	private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
	private static final SurfaceRules.RuleSource SNOW = makeStateRule(Blocks.SNOW_BLOCK);
	private static final SurfaceRules.RuleSource WEATHERED_DEADROCK = makeStateRule(TFBlocks.WEATHERED_DEADROCK);
	private static final SurfaceRules.RuleSource CRACKED_DEADROCK = makeStateRule(TFBlocks.CRACKED_DEADROCK);
	private static final SurfaceRules.RuleSource DEADROCK = makeStateRule(TFBlocks.DEADROCK);

	private static SurfaceRules.RuleSource makeStateRule(Block block) {
		return SurfaceRules.state(block.defaultBlockState());
	}

	public static SurfaceRules.RuleSource tfSurface(HolderGetter<Biome> biomes) {
		SurfaceRules.RuleSource bedrockLayer = SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK);

		return SurfaceRules.sequence(
			bedrockLayer,
			highlandsSurface(biomes),
			deadrockSurface(biomes),
			snowyForestSurface(biomes),
			glacierSurface(biomes),
			overworldLikeFloor(biomes)
		);
	}

	@NotNull
	private static SurfaceRules.RuleSource highlandsSurface(HolderGetter<Biome> biomes) {
		// Make sure it's not a block under the water level
		SurfaceRules.RuleSource podzolFloor = SurfaceRules.sequence(
			SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), PODZOL),
			DIRT
		);

		//highlands has a noise-based mixture of podzol and coarse dirt
		SurfaceRules.RuleSource highlandsSoil = SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, CaveSurface.FLOOR), SurfaceRules.sequence(
			//mix coarse dirt and podzol with noise
			SurfaceRules.ifTrue(surfaceNoiseAbove(2.25D), COARSE_DIRT),
			SurfaceRules.ifTrue(surfaceNoiseAbove(-2.25D), podzolFloor)
		));

		//check if we're in the highlands
		return SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, TFBiomes.HIGHLANDS), highlandsSoil);
	}

	@NotNull
	private static SurfaceRules.RuleSource deadrockSurface(HolderGetter<Biome> biomes) {
		//thornlands/plateau has no caves and deadrock instead of stone
		SurfaceRules.RuleSource deadrockTerrain = SurfaceRules.sequence(
			SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, CaveSurface.FLOOR), WEATHERED_DEADROCK),
			SurfaceRules.ifTrue(
				SurfaceRules.waterStartCheck(-6, -1),
				SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, true, CaveSurface.FLOOR), CRACKED_DEADROCK)
			),
			DEADROCK
		);

		//check if we're in the deadrock biomes
		return SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU), deadrockTerrain);
	}

	@NotNull
	private static SurfaceRules.RuleSource snowyForestSurface(HolderGetter<Biome> biomes) {
		// Make sure it's not a block under the water level
		SurfaceRules.RuleSource snowFloor = SurfaceRules.sequence(
			SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), SNOW),
			DIRT
		);

		SurfaceRules.RuleSource snowySoil = SurfaceRules.sequence(
			SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, CaveSurface.FLOOR), snowFloor),
			SurfaceRules.ifTrue(
				SurfaceRules.waterStartCheck(-6, -1),
				SurfaceRules.ifTrue(
					SurfaceRules.stoneDepthCheck(0, true, CaveSurface.FLOOR),
					SurfaceRules.ifTrue(SurfaceRules.verticalGradient("snowy_dirt", VerticalAnchor.absolute(0), VerticalAnchor.absolute(-3)), DIRT)
				)
			)
		);

		//check if we're in the snowy forest
		return SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, TFBiomes.SNOWY_FOREST), snowySoil);
	}

	@NotNull
	private static SurfaceRules.RuleSource glacierSurface(HolderGetter<Biome> biomes) {
		//glacier has gravel for a few layers, then stone. All blanketed under 30+ blocks of ice
		SurfaceRules.RuleSource surfaceUnderPermafrost = SurfaceRules.sequence(
			//surface and under is gravel
			SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, CaveSurface.FLOOR), GRAVEL),
			SurfaceRules.ifTrue(
				SurfaceRules.waterStartCheck(-6, -1),
				SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, true, CaveSurface.FLOOR), GRAVEL)
			)
		);

		//check if we're in the glacier biome
		return SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, TFBiomes.GLACIER), surfaceUnderPermafrost);
	}

	@NotNull
	private static SurfaceRules.RuleSource overworldLikeFloor(HolderGetter<Biome> biomes) {
		//lakes and rivers get sand
		SurfaceRules.RuleSource riverLakeBeds = SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, TFBiomes.LAKE, TFBiomes.STREAM), SurfaceRules.sequence(
			SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, CaveSurface.CEILING), SANDSTONE),
			SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), GRASS_BLOCK),
			SAND
		));

		//make sure the swamps always get grass, they had weird stone patches sometimes
		SurfaceRules.RuleSource swampBeds = SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, TFBiomes.SWAMP, TFBiomes.FIRE_SWAMP), SurfaceRules.sequence(
			SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), GRASS_BLOCK),
			DIRT
		));

		//check if we're above ground, so hollow hills dont have grassy floors
		SurfaceRules.RuleSource grassAboveSeaLevel = SurfaceRules.ifTrue(SurfaceRules.yStartCheck(VerticalAnchor.absolute(-4), 1), GRASS_BLOCK);
		//make everything else grass
		SurfaceRules.RuleSource grassSurface = SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), grassAboveSeaLevel);

		//if we're around the area hollow hill floors are, check if we're underwater. If so place some dirt.
		//This fixes streams having weird stone patches
		SurfaceRules.RuleSource underwaterSurface = SurfaceRules.ifTrue(
			SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(-4), 1)),
			SurfaceRules.ifTrue(
				SurfaceRules.not(SurfaceRules.waterBlockCheck(-1, 0)),
				DIRT
			)
		);

		// Twilight Forest's surface is based off the normal overworld surface
		SurfaceRules.RuleSource onFloor = SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, CaveSurface.FLOOR), SurfaceRules.sequence(
			riverLakeBeds,
			swampBeds,
			grassSurface,
			underwaterSurface
		));

		//dirt goes under the grass of course!
		SurfaceRules.RuleSource underFloor = SurfaceRules.ifTrue(
			SurfaceRules.waterStartCheck(-6, -1),
			//check if we're above ground, so hollow hills dont have dirt floors
			SurfaceRules.ifTrue(
				SurfaceRules.yStartCheck(VerticalAnchor.absolute(-4), 1),
				SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, true, CaveSurface.FLOOR), DIRT)
			)
		);

		return SurfaceRules.sequence(onFloor, underFloor);
	}

	private static SurfaceRules.ConditionSource surfaceNoiseAbove(double p_194809_) {
		return SurfaceRules.noiseCondition2d(Noises.SURFACE, p_194809_ / 8.25D, Double.MAX_VALUE);
	}
}
