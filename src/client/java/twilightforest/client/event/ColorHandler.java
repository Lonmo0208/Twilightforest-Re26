package twilightforest.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.block.CastleDoorBlock;
import twilightforest.block.ClimbableHollowLogBlock;
import twilightforest.client.properties.PotionFlaskTintSource;
import twilightforest.client.properties.SpawnEggTintSource;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.util.ColorUtil;
import twilightforest.util.SimplexNoiseHelper;

import java.util.List;

public class ColorHandler {


	private static int auroraColor(BlockPos pos) {
		return ColorUtil.hsvToRGB(
			SimplexNoiseHelper.rippleFractalNoise(2, 128.0f, pos != null ? pos.above(128) : BlockPos.ZERO, 0.37f, 0.67f, 1.5f),
			1.0f, 1.0f);
	}

	private static int auroraVariantColor(BlockPos pos) {
		float hue = SimplexNoiseHelper.rippleFractalNoise(2, 128.0f, new BlockPos(pos.getX(), 128 - pos.getY(), pos.getZ()), 0.37f, 0.67f, 1.5f);
		int normalColor = ColorUtil.hsvToRGB(hue, 1.0F, 1.0F);
		int red = (normalColor >> 16) & 255;
		int green = (normalColor >> 8) & 255;
		int blue = normalColor & 255;
		float[] hsb = ColorUtil.rgbToHSV(red, green, blue);
		return ColorUtil.hsvToRGB(hsb[0], hsb[1] * 0.5F, Math.min(hsb[2] + 0.4F, 0.9F));
	}


	private static int averageFoliageColor(BlockAndTintGetter level, BlockPos pos) {
		int red = 0, green = 0, blue = 0;
		for (int dz = -1; dz <= 1; dz++) {
			for (int dx = -1; dx <= 1; dx++) {
				int color = BiomeColors.getAverageFoliageColor(level, pos.offset(dx, 0, dz));
				red += (color & 0xFF0000) >> 16;
				green += (color & 0xFF00) >> 8;
				blue += color & 0xFF;
			}
		}
		return 0xFF000000 | (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
	}

	private static int canopyColor(int normalColor) {
		return 0xFF000000 | (((normalColor & 0xFEFEFE) + 0x469A66) / 2);
	}

	private static int mangroveColor(int normalColor) {
		return 0xFF000000 | (((normalColor & 0xFEFEFE) + 0xC0E694) / 2);
	}


	private static int timeLeafColor(BlockPos pos) {
		int fade = pos.getX() * 16 + pos.getY() * 16 + pos.getZ() * 16;
		if ((fade & 256) != 0) fade = 255 - (fade & 255);
		fade &= 255;
		float spring = (255 - fade) / 255F;
		float fall = fade / 255F;
		int red = (int) (spring * 106 + fall * 251);
		int green = (int) (spring * 156 + fall * 108);
		int blue = (int) (spring * 23 + fall * 27);
		return 0xFF000000 | red << 16 | green << 8 | blue;
	}

	private static int transLeafColor(BlockPos pos) {
		int fade = pos.getX() * 27 + pos.getY() * 63 + pos.getZ() * 39;
		if ((fade & 256) != 0) fade = 255 - (fade & 255);
		fade &= 255;
		float spring = (255 - fade) / 255F;
		float fall = fade / 255F;
		int red = (int) (spring * 108 + fall * 96);
		int green = (int) (spring * 204 + fall * 107);
		int blue = (int) (spring * 234 + fall * 121);
		return 0xFF000000 | red << 16 | green << 8 | blue;
	}

	private static int mineLeafColor(BlockPos pos) {
		int fade = pos.getX() * 31 + pos.getY() * 33 + pos.getZ() * 32;
		if ((fade & 256) != 0) fade = 255 - (fade & 255);
		fade &= 255;
		float spring = (255 - fade) / 255F;
		float fall = fade / 255F;
		int red = (int) (spring * 252 + fall * 237);
		int green = (int) (spring * 241 + fall * 172);
		int blue = (int) (spring * 68 + fall * 9);
		return 0xFF000000 | red << 16 | green << 8 | blue;
	}

	private static int sortLeafColor(BlockPos pos) {
		int fade = pos.getX() * 63 + pos.getY() * 63 + pos.getZ() * 63;
		if ((fade & 256) != 0) fade = 255 - (fade & 255);
		fade &= 255;
		float spring = (255 - fade) / 255F;
		float fall = fade / 255F;
		int red = (int) (spring * 54 + fall * 168);
		int green = (int) (spring * 76 + fall * 199);
		int blue = (int) (spring * 3 + fall * 43);
		return 0xFF000000 | red << 16 | green << 8 | blue;
	}

	private static int rainbowLeafColor(BlockPos pos) {
		int red = pos.getX() * 32 + pos.getY() * 16;
		if ((red & 256) != 0) red = 255 - (red & 255);
		red &= 255;
		int green = pos.getY() * 32 + pos.getZ() * 16;
		if ((green & 256) != 0) green = 255 - (green & 255);
		green ^= 255;
		int blue = pos.getX() * 16 + pos.getZ() * 32;
		if ((blue & 256) != 0) blue = 255 - (blue & 255);
		blue &= 255;
		return 0xFF000000 | red << 16 | green << 8 | blue;
	}


	private static int castleDoorColor(int baseColor, BlockState state) {
		if (state.getBlock() instanceof CastleDoorBlock && state.getValue(CastleDoorBlock.ACTIVE) && !state.getValue(CastleDoorBlock.VANISHED))
			return baseColor ^ 0xFFFFFF;
		return baseColor;
	}


	private static BlockTintSource simpleTint(int defaultColor, java.util.function.BiFunction<BlockAndTintGetter, BlockPos, Integer> worldColor) {
		return new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return defaultColor;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return worldColor.apply(level, pos);
			}
		};
	}

	private static BlockTintSource simpleTint(int defaultColor) {
		return new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return defaultColor;
			}
		};
	}

	private static BlockTintSource whiteTint() {
		// 0xFFFFFFFF (opaque white) - tint colors are ARGB in 26.1.2 and get multiplied with the
		// vertex alpha; a bare 0xFFFFFF would have zero alpha and render the block fully transparent.
		return simpleTint(0xFFFFFFFF);
	}

	// ======================================================================
	// registerBlockColors
	// ======================================================================

	public static void registerBlockColors() {
		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFFFFFFFF;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return auroraColor(pos);
			}
		}), TFBlocks.AURORA_BLOCK);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFFFFFFFF;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return auroraVariantColor(pos);
			}
		}), TFBlocks.AURORA_PILLAR, TFBlocks.AURORA_SLAB, TFBlocks.AURORALIZED_GLASS);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | FoliageColor.FOLIAGE_DEFAULT;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return averageFoliageColor(level, pos);
			}
		}), TFBlocks.DARK_LEAVES, TFBlocks.HARDENED_DARK_LEAVES, TFBlocks.GIANT_LEAVES);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF48B518;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return averageFoliageColor(level, pos);
			}
		}), TFBlocks.TWILIGHT_OAK_LEAVES);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0x609860;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return canopyColor(averageFoliageColor(level, pos));
			}
		}), TFBlocks.CANOPY_LEAVES);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0x80A755;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return mangroveColor(averageFoliageColor(level, pos));
			}
		}), TFBlocks.MANGROVE_LEAVES);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | 106 << 16 | 156 << 8 | 23;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return timeLeafColor(pos);
			}
		}), TFBlocks.TIME_LEAVES);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | 108 << 16 | 204 << 8 | 234;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return transLeafColor(pos);
			}
		}), TFBlocks.TRANSFORMATION_LEAVES);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | 252 << 16 | 241 << 8 | 68;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return mineLeafColor(pos);
			}
		}), TFBlocks.MINING_LEAVES);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | 54 << 16 | 76 << 8 | 3;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return sortLeafColor(pos);
			}
		}), TFBlocks.SORTING_LEAVES);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF48B518;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return rainbowLeafColor(pos);
			}
		}), TFBlocks.RAINBOW_OAK_LEAVES);

		BlockColorRegistry.register(List.of(simpleTint(FoliageColor.FOLIAGE_EVERGREEN)),
			TFBlocks.BEANSTALK_LEAVES, TFBlocks.THORN_LEAVES);

		// Note: FoliageColor has no getDefaultColor() method (unlike GrassColor). The vanilla
		// foliage() source uses FOLIAGE_DEFAULT (= -12012264 = 0xFF48B518) which has full
		// alpha already baked in, so this is the correct constant fallback.
		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return FoliageColor.FOLIAGE_DEFAULT;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return BiomeColors.getAverageFoliageColor(level, pos);
			}
		}), TFBlocks.FALLEN_LEAVES);

		// In 26.1.2, GrassColor.getDefaultColor() / BiomeColors.getAverageGrassColor() already
		// return proper ARGB values with alpha set (colormap PNG pixels are stored as ARGB with
		// alpha = 0xFF; fallback default = -65281 = 0xFFFF00FF which also has full alpha).
		// Explicitly OR-ing with 0xFF000000 is redundant and can mask misbehavior, so match
		// the vanilla grass() implementation exactly.
		BlockTintSource fiddleheadTint = new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return GrassColor.getDefaultColor();
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return BiomeColors.getAverageGrassColor(level, pos);
			}
		};
		BlockColorRegistry.register(List.of(fiddleheadTint),
			TFBlocks.FIDDLEHEAD, TFBlocks.POTTED_FIDDLEHEAD);

		// Same grass tint as above, shared with many foliage-derived blocks. Use vanilla form.
		BlockTintSource grassTintSource = new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return GrassColor.getDefaultColor();
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return BiomeColors.getAverageGrassColor(level, pos);
			}
		};
		BlockColorRegistry.register(List.of(whiteTint(), grassTintSource),
			TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL,
			TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL,
			TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL,
			TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL,
			TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL,
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL,
			TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL,
			TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL,
			TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL);

		BlockTintSource climbableGrassTintSource = new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | GrassColor.get(0.5D, 1.0D);
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				if (state.hasProperty(ClimbableHollowLogBlock.VARIANT) && state.getValue(ClimbableHollowLogBlock.VARIANT) == HollowLogVariants.Climbable.VINE) {
					return 0xFF000000 | BiomeColors.getAverageFoliageColor(level, pos);
				}
				return 0xFFFFFFFF;
			}
		};
		BlockColorRegistry.register(List.of(whiteTint(), climbableGrassTintSource),
			TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE, TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE,
			TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE, TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE,
			TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE, TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE,
			TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE, TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE,
			TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE,
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE,
			TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE,
			TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE,
			TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE);

		BlockColorRegistry.register(List.of(grassTintSource),
			TFBlocks.SMOKER, TFBlocks.FIRE_JET);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 7455580;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return 2129968;
			}
		}), TFBlocks.HUGE_LILY_PAD);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return -1;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				float f = SimplexNoiseHelper.rippleFractalNoise(2, 32.0f, pos, 0.4f, 1.0f, 2f);
				return ColorUtil.hsvToRGB(0.1f, 1f - f, (f + 2f) / 3f);
			}
		}), TFBlocks.TOWERWOOD, TFBlocks.CRACKED_TOWERWOOD, TFBlocks.INFESTED_TOWERWOOD, TFBlocks.MOSSY_TOWERWOOD);

		// Note: the ARGB tint must keep an opaque alpha (0xFF000000), otherwise
		// ModelBlockRenderer.multiplyColor() would multiply by a zero alpha and
		// render the rune layer fully transparent.
		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFFFF00FF;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return castleDoorColor(0xFFFF00FF, state);
			}
		}), TFBlocks.PINK_CASTLE_RUNE_BRICK, TFBlocks.PINK_CASTLE_DOOR);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF00FFFF;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return castleDoorColor(0xFF00FFFF, state);
			}
		}), TFBlocks.BLUE_CASTLE_RUNE_BRICK, TFBlocks.BLUE_CASTLE_DOOR);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFFFFFF00;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return castleDoorColor(0xFFFFFF00, state);
			}
		}), TFBlocks.YELLOW_CASTLE_RUNE_BRICK, TFBlocks.YELLOW_CASTLE_DOOR);

		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF4B0082;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return castleDoorColor(0xFF4B0082, state);
			}
		}), TFBlocks.VIOLET_CASTLE_RUNE_BRICK, TFBlocks.VIOLET_CASTLE_DOOR);

		BlockColorRegistry.register(List.of(simpleTint(0xFF5C1074)), TFBlocks.VIOLET_FORCE_FIELD);
		BlockColorRegistry.register(List.of(simpleTint(0xFFFA057E)), TFBlocks.PINK_FORCE_FIELD);
		BlockColorRegistry.register(List.of(simpleTint(0xFFFF5B02)), TFBlocks.ORANGE_FORCE_FIELD);
		BlockColorRegistry.register(List.of(simpleTint(0xFF89E701)), TFBlocks.GREEN_FORCE_FIELD);
		BlockColorRegistry.register(List.of(simpleTint(0xFF0DDEFF)), TFBlocks.BLUE_FORCE_FIELD);

		BlockColorRegistry.register(List.of(grassTintSource),
			TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE, TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE,
			TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE);
	}

	// ======================================================================
	// registerItemColors
	// ======================================================================

	public static void registerItemColors() {
		ItemTintSources.ID_MAPPER.put(TwilightForestMod.prefix("potion_flask"), PotionFlaskTintSource.TYPE);
		ItemTintSources.ID_MAPPER.put(TwilightForestMod.prefix("spawn_egg_primary"), SpawnEggTintSource.Primary.MAP_CODEC);
		ItemTintSources.ID_MAPPER.put(TwilightForestMod.prefix("spawn_egg_secondary"), SpawnEggTintSource.Secondary.MAP_CODEC);
	}
}
