package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import twilightforest.tags.TFBlockTags;

import java.util.Optional;

@SuppressWarnings("deprecation")
public class TFSmallLakeFeature implements Feature {
	private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

	public static final MapCodec<TFSmallLakeFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("fluid").forGetter(f -> f.fluid),
		BlockStateProvider.CODEC.optionalFieldOf("barrier").forGetter(f -> Optional.ofNullable(f.barrier)),
		BlockStateProvider.CODEC.optionalFieldOf("ice").forGetter(f -> Optional.ofNullable(f.ice))
	).apply(instance, TFSmallLakeFeature::new));

	private final BlockStateProvider fluid;
	private final @Nullable BlockStateProvider barrier;
	private final @Nullable BlockStateProvider ice;

	public TFSmallLakeFeature(BlockStateProvider fluid, @Nullable BlockStateProvider barrier, @Nullable BlockStateProvider ice) {
		this.fluid = fluid;
		this.barrier = barrier;
		this.ice = ice;
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Vanilla does this shit too
	private TFSmallLakeFeature(BlockStateProvider fluid, Optional<BlockStateProvider> barrier, Optional<BlockStateProvider> ice) {
		this(fluid, barrier.orElse(null), ice.orElse(null));
	}

	public TFSmallLakeFeature() {
		this(BlockStateProvider.simple(Blocks.WATER), (BlockStateProvider) null, (BlockStateProvider) null);
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		return CODEC;
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
		BlockPos blockpos = pos;
		WorldGenLevel worldgenlevel = level;
		RandomSource randomsource = random;

		if (blockpos.getY() <= worldgenlevel.getMinY() + 4) {
			return false;
		} else {
			blockpos = blockpos.below(4);
			boolean[] booleans = new boolean[2048];
			int i = randomsource.nextInt(4) + 4;

			for (int j = 0; j < i; j++) {
				double d0 = randomsource.nextDouble() * 6.0 + 3.0;
				double d1 = randomsource.nextDouble() * 4.0 + 2.0;
				double d2 = randomsource.nextDouble() * 6.0 + 3.0;
				double d3 = randomsource.nextDouble() * (16.0 - d0 - 2.0) + 1.0 + d0 / 2.0;
				double d4 = randomsource.nextDouble() * (8.0 - d1 - 4.0) + 2.0 + d1 / 2.0;
				double d5 = randomsource.nextDouble() * (16.0 - d2 - 2.0) + 1.0 + d2 / 2.0;

				for (int l = 1; l < 15; l++) {
					for (int i1 = 1; i1 < 15; i1++) {
						for (int j1 = 1; j1 < 7; j1++) {
							double d6 = ((double)l - d3) / (d0 / 2.0);
							double d7 = ((double)j1 - d4) / (d1 / 2.0);
							double d8 = ((double)i1 - d5) / (d2 / 2.0);
							double d9 = d6 * d6 + d7 * d7 + d8 * d8;
							if (d9 < 1.0) {
								booleans[(l * 16 + i1) * 8 + j1] = true;
							}
						}
					}
				}
			}

			BlockState fluidState = this.fluid.getState(worldgenlevel, randomsource, blockpos);

			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					for (int y = 0; y < 8; y++) {
						boolean flag = !booleans[(x * 16 + z) * 8 + y]
							&& (
							x < 15 && booleans[((x + 1) * 16 + z) * 8 + y]
								|| x > 0 && booleans[((x - 1) * 16 + z) * 8 + y]
								|| z < 15 && booleans[(x * 16 + z + 1) * 8 + y]
								|| z > 0 && booleans[(x * 16 + (z - 1)) * 8 + y]
								|| y < 7 && booleans[(x * 16 + z) * 8 + y + 1]
								|| y > 0 && booleans[(x * 16 + z) * 8 + (y - 1)]
						);
						if (flag) {
							BlockState blockstate3 = worldgenlevel.getBlockState(blockpos.offset(x, y, z));
							if (y >= 4 && blockstate3.liquid()) {
								return false;
							}

							if (y < 4 && !blockstate3.isSolid() && worldgenlevel.getBlockState(blockpos.offset(x, y, z)) != fluidState) {
								return false;
							}
						}
					}
				}
			}

			BlockState iceState = this.ice != null ? this.ice.getState(worldgenlevel, randomsource, blockpos) : null;

			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					for (int y = 0; y < 8; y++) {
						if (booleans[(x * 16 + z) * 8 + y]) {
							BlockPos offset = blockpos.offset(x, y, z);
							if (!(worldgenlevel.getBlockState(offset).is(TFBlockTags.SMALL_LAKES_DONT_REPLACE) ||
								worldgenlevel.getBlockState(offset.above()).is(TFBlockTags.SMALL_LAKES_DONT_REPLACE))) {
								if (y >= 4) {
									worldgenlevel.setBlock(offset, AIR, Block.UPDATE_CLIENTS);
                                    worldgenlevel.scheduleTick(offset, AIR.getBlock(), 0);
                                    this.markAboveForPostProcessing(worldgenlevel, offset);
									continue;
                                }

								if (y == 3 && iceState != null) {
									worldgenlevel.setBlock(offset, iceState, Block.UPDATE_CLIENTS);
									continue;
								}

								worldgenlevel.setBlock(offset, fluidState, Block.UPDATE_CLIENTS);
                            }
						}
					}
				}
			}

			if (this.barrier != null) {
				BlockState barrierState = this.barrier.getState(worldgenlevel, randomsource, blockpos);
				if (!barrierState.isAir()) {
					for (int x = 0; x < 16; x++) {
						for (int z = 0; z < 16; z++) {
							for (int y = 0; y < 8; y++) {
								boolean flag2 = !booleans[(x * 16 + z) * 8 + y]
									&& (
									x < 15 && booleans[((x + 1) * 16 + z) * 8 + y]
										|| x > 0 && booleans[((x - 1) * 16 + z) * 8 + y]
										|| z < 15 && booleans[(x * 16 + z + 1) * 8 + y]
										|| z > 0 && booleans[(x * 16 + (z - 1)) * 8 + y]
										|| y < 7 && booleans[(x * 16 + z) * 8 + y + 1]
										|| y > 0 && booleans[(x * 16 + z) * 8 + (y - 1)]
								);
								if (flag2 && (y < 4 || randomsource.nextInt(2) != 0)) {
									BlockState blockstate = worldgenlevel.getBlockState(blockpos.offset(x, y, z));
									if (blockstate.isSolid() && !blockstate.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
										BlockPos blockpos3 = blockpos.offset(x, y, z);
										worldgenlevel.setBlock(blockpos3, barrierState, Block.UPDATE_CLIENTS);
										this.markAboveForPostProcessing(worldgenlevel, blockpos3);
									}
								}
							}
						}
					}
				}
			}

			return true;
		}
	}
}
