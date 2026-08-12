package twilightforest.client.event;


import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.GraphicsPreset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.block.CloudBlock;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.config.TFConfig;
import twilightforest.util.Vec2i;

import java.util.ArrayList;
import java.util.List;

public class CloudEvents {
	private static final List<PrecipitationRenderHelper> RENDER_HELPER = new ArrayList<>();

	record PrecipitationRenderHelper(BlockPos cloudPos, Biome.Precipitation precipitation, float precipitationLevel, int rainOnY) {

	}

	// TODO: Port to Fabric - ClientTickEvent is NeoForge-specific
	//protected static void tickWeatherEffects(ClientTickEvent.Post event) {
	protected static void tickWeatherEffects() {
		Minecraft mc = Minecraft.getInstance();

		if (!mc.isPaused()) {
			if (mc.level != null && TFConfig.getClientCloudBlockPrecipitationDistance() > 0) { // Semi vanilla copy of the weather tick, but made to work with cloud blocks instead
				Vec3 vec3 = mc.gameRenderer.mainCamera().position();
				if (mc.level.getGameTime() % 10L == 0L) {
					RENDER_HELPER.clear();

					double camX = vec3.x();
					double camY = vec3.y();
					double camZ = vec3.z();

					int floorX = Mth.floor(camX);
					int floorY = Mth.floor(camY);
					int floorZ = Mth.floor(camZ);

					int renderDistance = Minecraft.getInstance().options.graphicsPreset().get() == GraphicsPreset.FANCY ? 10 : 5;
					int precipitationDistance = TFConfig.getClientCloudBlockPrecipitationDistance();
					BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

					for (int roofZ = floorZ - renderDistance; roofZ <= floorZ + renderDistance; ++roofZ) {
						for (int roofX = floorX - renderDistance; roofX <= floorX + renderDistance; ++roofX) {
							int lastBadYLevel = Integer.MIN_VALUE;
							for (int roofY = floorY - renderDistance; roofY < floorY + precipitationDistance + renderDistance; roofY++) {
								boolean skipLoop = roofY == lastBadYLevel + 1; // Cloud can't rain if there is an invalid blockState right below it, so might as well skip the loop
								pos.set(roofX, roofY, roofZ);
								if (Heightmap.Types.MOTION_BLOCKING.isOpaque().test(mc.level.getBlockState(pos)))
									lastBadYLevel = roofY; // Check if we skip next loop
								if (skipLoop) continue;

								if (mc.level.getBlockState(pos).getBlock() instanceof CloudBlock cloudBlock) {
									Pair<Biome.Precipitation, Float> precipitationRainLevelPair = cloudBlock.getCurrentPrecipitation(pos, mc.level, mc.level.getRainLevel(1.0F));
									if (precipitationRainLevelPair.getLeft() == Biome.Precipitation.NONE)
										continue; // No rain no gain

									int highestRainyBlock = roofY;
									for (int y = roofY - 1; y > roofY - precipitationDistance; y--) {
										if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(mc.level.getBlockState(pos.atY(y))))
											highestRainyBlock = y;
										else break;
									}
									if (highestRainyBlock == roofY) continue;

									RENDER_HELPER.add(new PrecipitationRenderHelper(pos.immutable(), precipitationRainLevelPair.getLeft(), precipitationRainLevelPair.getRight(), highestRainyBlock));
								}
							}
						}
					}
				}

				if (!RENDER_HELPER.isEmpty()) {
					RandomSource randomsource = RandomSource.create(mc.level.getGameTime() * 312987231L);
					BlockPos particlePos = null;
					int particleCount = 100 / (mc.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

					boolean yetToMakeASound = true;
					BlockPos camPos = BlockPos.containing(vec3);

					List<Vec2i> particleChecks = new ArrayList<>();
					for (int i = 0; i < particleCount; ++i) {
						particleChecks.add(new Vec2i(randomsource.nextInt(21) - 10 + camPos.getX(), randomsource.nextInt(21) - 10 + camPos.getZ()));
					}

					for (PrecipitationRenderHelper helper : RENDER_HELPER) {
						if (helper.precipitation() == Biome.Precipitation.RAIN) {
							for (Vec2i vec2 : particleChecks) {
								if (vec2.x == helper.cloudPos().getX() && vec2.z == helper.cloudPos().getZ()) {
									BlockPos highestRainyPos = helper.cloudPos().atY(helper.rainOnY());
									if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(mc.level.getBlockState(highestRainyPos.below())))
										continue;

									// TODO: 26.1.2 - rainSoundTime is private, weather sound logic needs rework
									// Old code using mc.levelRenderer.rainSoundTime has been removed
									if (yetToMakeASound && particlePos != null && randomsource.nextInt(3) < 0) {
										if (particlePos.getY() > camPos.getY() + 1 && mc.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, camPos).getY() > Mth.floor((float) camPos.getY())) {
											mc.level.playLocalSound(particlePos, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
										} else {
											mc.level.playLocalSound(particlePos, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
										}
										yetToMakeASound = false;
									}

									if (highestRainyPos.getY() > mc.level.getMinY() && highestRainyPos.getY() <= camPos.getY() + 10 && highestRainyPos.getY() >= camPos.getY() - 10) {
										particlePos = highestRainyPos.below();
										if (mc.options.particles().get() == ParticleStatus.MINIMAL) break;

										double particleX = randomsource.nextDouble();
										double particleZ = randomsource.nextDouble();
										BlockState blockstate = mc.level.getBlockState(particlePos);
										FluidState fluidstate = mc.level.getFluidState(particlePos);
										VoxelShape voxelshape = blockstate.getCollisionShape(mc.level, particlePos);
										double voxelMax = voxelshape.max(Direction.Axis.Y, particleX, particleZ);
										double fluidMax = fluidstate.getHeight(mc.level, particlePos);
										double particleY = Math.max(voxelMax, fluidMax);
										ParticleOptions particleoptions = !fluidstate.is(FluidTags.LAVA) && !blockstate.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockstate) ? ParticleTypes.RAIN : ParticleTypes.SMOKE;
										mc.level.addParticle(particleoptions, (double) particlePos.getX() + particleX, (double) particlePos.getY() + particleY, (double) particlePos.getZ() + particleZ, 0.0D, 0.0D, 0.0D);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	// TODO: Port to Fabric - RenderLevelStageEvent is NeoForge-specific; entire method body depends on it
	/*
	protected static void renderPrecipitation(RenderLevelStageEvent event) {
		if (event instanceof RenderLevelStageEvent.AfterTranslucentParticles && TFConfig.getClientCloudBlockPrecipitationDistance() > 0 && !RENDER_HELPER.isEmpty()) {
			...
		}
	}
	*/
	protected static void renderPrecipitation() {
	}
}
