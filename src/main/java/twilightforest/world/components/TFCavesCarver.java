package twilightforest.world.components;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.CarverOutput;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

//Framework taken from CaveWorldCarver, everything worth knowing is documented for easier changes in the future
public class TFCavesCarver implements WorldCarver {
	private final boolean isHighlands;
	private final BlockStateProvider wallBlocks;
	private final float probability;
	private final HeightProvider y;
	private final FloatProvider horizontalRadiusMultiplier;
	private final FloatProvider verticalRadiusMultiplier;
	private final FloatProvider floorLevel;
	private final FloatProvider yScale;

	public TFCavesCarver(float probability, HeightProvider y, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel, FloatProvider yScale, boolean isHighlands, BlockStateProvider wallBlocks) {
		this.probability = probability;
		this.y = y;
		this.horizontalRadiusMultiplier = horizontalRadiusMultiplier;
		this.verticalRadiusMultiplier = verticalRadiusMultiplier;
		this.floorLevel = floorLevel;
		this.yScale = yScale;
		this.wallBlocks = wallBlocks;
		this.isHighlands = isHighlands;
	}

	@Override
	public MapCodec<? extends WorldCarver> codec() {
		return MapCodec.unit(this);
	}

	@Override
	public boolean isStartChunk(RandomSource rand) {
		// Highland caves instead spawn when the troll caves structure is nearby, and with special location ruls
		return this.isHighlands || rand.nextFloat() <= this.probability;
	}

	@Override
	public boolean carve(WorldGenerationContext ctx, RandomSource random, ChunkPos pos1, ChunkPos pos2, CarverOutput output) {
		if (this.isHighlands && (Mth.clamp(LegacyLandmarkPlacements.manhattanDistanceFromLandmarkCenter(pos2.x(), pos2.z()), 0, 0b11) & 0b1) == 1)
			return false; // If highlands, enforces a binary grid (diagonal range of 4 chunks) of possible placements around the structure center, with center being one of the zero tiles

		int i = SectionPos.sectionToBlockCoord(this.getRange() * 2 - 1);

		// If highlands, only roll chance to generate even 1 cave. Otherwise, limited caves spawn for regular TF underground
		int caveCount = this.isHighlands ? random.nextInt(2) : random.nextInt(this.getCaveBound());

		for (int caveIndex = 0; caveIndex < caveCount; ++caveIndex) {
			double x = pos2.getBlockX(random.nextInt(16));
			double y = this.y.sample(random, ctx);
			double z = pos2.getBlockZ(random.nextInt(16));
			double horiz = this.horizontalRadiusMultiplier.sample(random);
			double vert = this.verticalRadiusMultiplier.sample(random);
			double floor = this.floorLevel.sample(random);
			CarveSkipChecker checker = (dX, dY, dZ, yPos) -> shouldSkip(dX, dY, dZ, floor);
			int tunnelCount = 1;
			if (this.isHighlands || random.nextInt(4) == 0) {
				double horizToVertRatio = this.yScale.sample(random);
				float radius = 1.0F + random.nextFloat() * 6.0F;
				this.createRoom(pos1, x, y, z, radius, horizToVertRatio, output, checker);
				tunnelCount += random.nextInt(4);
			}

			for (int tunnelIndex = 0; tunnelIndex < tunnelCount; ++tunnelIndex) {
				float randomRadian = random.nextFloat() * ((float) Math.PI * 2F);
				float randomPitch = (random.nextFloat() - 0.5F) / 4.0F;
				float thickness = this.getThickness(random);
				int branchCount = i - random.nextInt(i / 4);

				this.createTunnel(pos1, random.nextLong(), x, y, z, horiz, vert, thickness, randomRadian, randomPitch, 0, branchCount, this.getYScale(), output, checker);
			}
		}

		return true;
	}

	protected int getCaveBound() {
		return 4;
	}

	protected float getThickness(RandomSource rand) {
		float f = rand.nextFloat() * 2.0F + rand.nextFloat();
		if (rand.nextInt(10) == 0) {
			f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
		}

		return f;
	}

	protected double getYScale() {
		return 1.0D;
	}

	protected void createRoom(ChunkPos pos, double posX, double posY, double posZ, float radius, double horizToVertRatio, CarverOutput output, CarveSkipChecker checker) {
		double d0 = 1.5D + (double) (Mth.sin(((float) Math.PI / 2F)) * radius);
		double d1 = d0 * horizToVertRatio;
		WorldCarver.carveEllipsoid(pos, posX, posY, posZ, d0, d1, output, checker);
	}

	protected void createTunnel(ChunkPos pos, long seed, double posX, double posY, double posZ, double horizMult, double vertMult, float thickness, float yaw, float pitch, int branchIndex, int branchCount, double horizToVertRatio, CarverOutput output, CarveSkipChecker checker) {
		RandomSource random = RandomSource.create(seed);
		int i = random.nextInt(branchCount / 2) + branchCount / 4;
		boolean flag = random.nextInt(6) == 0;
		float f = 0.0F;
		float f1 = 0.0F;

		for (int j = branchIndex; j < branchCount; ++j) {
			double horizontalRadius = 1.5D + (double) (Mth.sin((float) Math.PI * (float) j / (float) branchCount) * thickness);
			double verticalRadius = horizontalRadius * horizToVertRatio;
			float f2 = Mth.cos(pitch);
			posX += Mth.cos(yaw) * f2;

			float yShift = Mth.sin(pitch);
			// If posY nears bedrock, "slow" its descent if marching downwards
			posY += yShift > 0 || posY + yShift > output.minY() + 10 ? yShift : yShift * 0.25f;

			posZ += Mth.sin(yaw) * f2;
			pitch = pitch * (flag ? 0.92F : 0.7F);
			pitch = pitch + f1 * 0.1F;
			yaw += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (j == i && thickness > 1.0F) {
				this.createTunnel(pos, random.nextLong(), posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw - ((float) Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, output, checker);
				this.createTunnel(pos, random.nextLong(), posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw + ((float) Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, output, checker);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!WorldCarver.canReach(pos, posX, posZ, j, branchCount, thickness)) {
					return;
				}

				// Additional size-boosting to make wider & taller spherical rooms
				boolean shouldEnlargeSphere = posY > output.minY() + 12 && random.nextInt(48) == 0;
				float sizeMultiplier = shouldEnlargeSphere
					? random.nextFloat() * random.nextFloat() * 2f + 1
					: 1;

				double sphereHRadius = Math.min(horizontalRadius * horizMult * sizeMultiplier, 10);
				double sphereVRadius = verticalRadius * vertMult * sizeMultiplier;
				// If side-boosting is applied, then squish the sphere's edge-steeped floor into a dish
				double sphereVRadiusLimited = shouldEnlargeSphere ? Math.min(sphereVRadius, sphereHRadius * 0.65f) : sphereVRadius;

				WorldCarver.carveEllipsoid(pos, posX, posY, posZ, sphereHRadius, sphereVRadiusLimited, output, checker);
			}
		}
	}

	private static boolean shouldSkip(double posX, double posY, double posZ, double minY) {
		if (posY <= minY) {
			return true;
		} else {
			return posX * posX + posY * posY + posZ * posZ >= 1.0D;
		}
	}
}
