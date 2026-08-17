package twilightforest.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.TFCavesCarver;

import net.minecraft.core.Registry;

//this was all put into 1 class because it seems like a waste to have it in 2
public class TFCaveCarvers {

	public static final TFCavesCarver TF_CAVES = new TFCavesCarver(
		0.15F,
		UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)),
		UniformFloat.of(0.7F, 1.4F),
		UniformFloat.of(0.8F, 1.3F),
		UniformFloat.of(-1.0F, -0.4F),
		UniformFloat.of(0.1F, 0.9F),
		false,
		BlockStateProvider.simple(Blocks.DIRT)
	);
	public static final TFCavesCarver HIGHLAND_CAVES = new TFCavesCarver(
		0.15F,
		UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)),
		UniformFloat.of(0.7F, 1.4F),
		UniformFloat.of(0.8F, 1.3F),
		UniformFloat.of(-1.0F, -0.4F),
		UniformFloat.of(0.1F, 0.9F),
		true,
		BlockStateProvider.simple(TFBlocks.TROLLSTEINN)
	);

	public static final ResourceKey<WorldCarver> TFCAVES_CONFIGURED = registerKey("tf_caves");
	public static final ResourceKey<WorldCarver> HIGHLANDCAVES_CONFIGURED = registerKey("highland_caves");

	private static ResourceKey<WorldCarver> registerKey(String name) {
		return ResourceKey.create(Registries.CARVER, TwilightForestMod.prefix(name));
	}

	public static void init() {
		// 26.3: register the carver type codecs into the CARVER_TYPE registry, which the
		// worldgen/carver/*.json data entries then reference by type name.
		Registry.register(BuiltInRegistries.CARVER_TYPE, TwilightForestMod.prefix("tf_caves"), TF_CAVES.codec());
		Registry.register(BuiltInRegistries.CARVER_TYPE, TwilightForestMod.prefix("highland_caves"), HIGHLAND_CAVES.codec());
	}

	public static void bootstrap(BootstrapContext<WorldCarver> context) {
		context.register(TFCAVES_CONFIGURED, TF_CAVES);
		context.register(HIGHLANDCAVES_CONFIGURED, HIGHLAND_CAVES);
	}
}
