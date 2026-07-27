package twilightforest.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.LevelResource;
import twilightforest.command.MapBiomesCommand;
import twilightforest.init.TFBiomes;
import twilightforest.util.ColorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class BiomeMapGenerator {

	private final DecimalFormat numberFormat = new DecimalFormat("#.00");
	private final HashMap<Identifier, MapBiomesCommand.BiomeMapColor> BIOME2COLOR = new HashMap<>();

	private void init() {
		BIOME2COLOR.put(TFBiomes.STREAM.identifier(), new MapBiomesCommand.BiomeMapColor(0, 0, 255));
		BIOME2COLOR.put(TFBiomes.LAKE.identifier(), new MapBiomesCommand.BiomeMapColor(0, 0, 255));
		BIOME2COLOR.put(TFBiomes.CLEARING.identifier(), new MapBiomesCommand.BiomeMapColor(132, 245, 130));
		BIOME2COLOR.put(TFBiomes.OAK_SAVANNAH.identifier(), new MapBiomesCommand.BiomeMapColor(239, 245, 130));
		BIOME2COLOR.put(TFBiomes.FOREST.identifier(), new MapBiomesCommand.BiomeMapColor(0, 255, 0));
		BIOME2COLOR.put(TFBiomes.DENSE_FOREST.identifier(), new MapBiomesCommand.BiomeMapColor(0, 170, 0));
		BIOME2COLOR.put(TFBiomes.FIREFLY_FOREST.identifier(), new MapBiomesCommand.BiomeMapColor(88, 252, 102));
		BIOME2COLOR.put(TFBiomes.ENCHANTED_FOREST.identifier(), new MapBiomesCommand.BiomeMapColor(0, 255, 255));
		BIOME2COLOR.put(TFBiomes.SPOOKY_FOREST.identifier(), new MapBiomesCommand.BiomeMapColor(119, 0, 255));
		BIOME2COLOR.put(TFBiomes.MUSHROOM_FOREST.identifier(), new MapBiomesCommand.BiomeMapColor(204, 0, 139));
		BIOME2COLOR.put(TFBiomes.DENSE_MUSHROOM_FOREST.identifier(), new MapBiomesCommand.BiomeMapColor(184, 48, 184));
		BIOME2COLOR.put(TFBiomes.SWAMP.identifier(), new MapBiomesCommand.BiomeMapColor(0, 204, 187));
		BIOME2COLOR.put(TFBiomes.FIRE_SWAMP.identifier(), new MapBiomesCommand.BiomeMapColor(140, 0, 0));
		BIOME2COLOR.put(TFBiomes.DARK_FOREST.identifier(), new MapBiomesCommand.BiomeMapColor(25, 61, 13));
		BIOME2COLOR.put(TFBiomes.DARK_FOREST_CENTER.identifier(), new MapBiomesCommand.BiomeMapColor(157, 79, 0));
		BIOME2COLOR.put(TFBiomes.SNOWY_FOREST.identifier(), new MapBiomesCommand.BiomeMapColor(255, 255, 255));
		BIOME2COLOR.put(TFBiomes.GLACIER.identifier(), new MapBiomesCommand.BiomeMapColor(130, 191, 245));
		BIOME2COLOR.put(TFBiomes.HIGHLANDS.identifier(), new MapBiomesCommand.BiomeMapColor(100, 65, 0));
		BIOME2COLOR.put(TFBiomes.THORNLANDS.identifier(), new MapBiomesCommand.BiomeMapColor(128, 100, 90));
		BIOME2COLOR.put(TFBiomes.FINAL_PLATEAU.identifier(), new MapBiomesCommand.BiomeMapColor(128, 128, 128));
	}

	public int createMap(CommandSourceStack source, int width, int height, boolean showBiomePercents) {
		if (BIOME2COLOR.isEmpty()) {
			init();
		}

		Map<Holder<Biome>, Integer> biomeCount = new HashMap<>();
		NativeImage img = new NativeImage(width, height, false);

		int progressUpdate = img.getHeight() / 8;

		for (int x = 0; x < img.getHeight(); x++) {
			for (int z = 0; z < img.getWidth(); z++) {
				ServerLevel level = source.getLevel();
				Holder<Biome> b = level.getNoiseBiome(x - (img.getWidth() / 2), 0, z - (img.getHeight() / 2));
				Identifier key = level.registryAccess().lookupOrThrow(Registries.BIOME).getKey(b.value());
				MapBiomesCommand.BiomeMapColor color = BIOME2COLOR.get(key);

				if (color == null) {
					int colorInt = MapBiomesCommand.getBiomeColor(b);

					if (colorInt == 0)
						colorInt = b.value().getGrassColor(0, 0);

					BIOME2COLOR.put(key, color = new MapBiomesCommand.BiomeMapColor(colorInt | 0xFF000000));
				}

				if (!biomeCount.containsKey(b)) {
					biomeCount.put(b, 0);
				} else {
					biomeCount.put(b, biomeCount.get(b) + 1);
				}

				img.setPixelABGR(x, z, ColorUtil.argbToABGR(color.getARGB()));
			}

			if (x % progressUpdate == 0) {
				int finalX = x;
				double percentComplete = (double) finalX / img.getHeight() * 100;
				String percentDisplay = this.numberFormat.format(percentComplete);
				source.sendSuccess(() -> Component.translatable("commands.tffeature.biomepng.progress", percentDisplay), false);
			}
		}

		if (showBiomePercents) {
			source.sendSuccess(() -> Component.translatable("commands.tffeature.biomepng.counts_header", width, height), false);
			int totalCount = biomeCount.values().stream().mapToInt(i -> i).sum();
			biomeCount.forEach((biome, integer) -> source.sendSuccess(() -> Component.literal(
					source.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).getKey(biome.value()).toString())
				.append(": " + (integer) + ChatFormatting.GRAY + " (" + numberFormat.format(((double) integer / totalCount) * 100) + "%)"), false));
		}

		int startX = Mth.floor(source.getPosition().x()) - (img.getWidth() / 2);
		int startZ = Mth.floor(source.getPosition().z()) - (img.getHeight() / 2);
		Path path = source.getLevel().getServer().getWorldPath(LevelResource.GENERATED_DIR).resolve("biomemaps").resolve(String.valueOf(source.getLevel().getSeed())).resolve("biome_map-" + source.getLevel().getSeed() + "-(" + startX + "." + startZ + ")-(" + (startX + width) + "." + (startZ + height) + ").png").normalize();
		try {
			if (!Files.exists(path)) {
				Files.createDirectories(path.getParent());
				img.writeToFile(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
			source.sendFailure(Component.translatable("commands.tffeature.biomepng.save_failed"));
			return 0;
		}

		source.sendSuccess(() -> Component.translatable("commands.tffeature.biomepng.save_success"), false);

		return 1;
	}
}