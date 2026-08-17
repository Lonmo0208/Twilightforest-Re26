package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.chunk.ChunkGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.util.landmarks.LandmarkUtil;

import java.util.Optional;

public class UndergroundPlantFeature implements Feature {

	public static final MapCodec<UndergroundPlantFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC.fieldOf("state").forGetter(f -> f.state)
	).apply(instance, UndergroundPlantFeature::new));

	private final BlockState state;
	int maxCount;
	boolean spawnInStructure;

	public UndergroundPlantFeature(BlockState state, int maxCount, boolean spawnInStructure) {
		this.state = state;
		this.maxCount = maxCount;
		this.spawnInStructure = spawnInStructure;
	}

	public UndergroundPlantFeature(BlockState state, int maxCount) {
		this(state, maxCount, false);
	}

	public UndergroundPlantFeature(BlockState state) {
		this(state, Integer.MAX_VALUE, false);
	}

	public UndergroundPlantFeature(BlockState state, boolean spawnInStructure) {
		this(state, Integer.MAX_VALUE, spawnInStructure);
	}

	public UndergroundPlantFeature() {
		this(Blocks.AIR.defaultBlockState(), 4, false);
	}

	@Override
	public MapCodec<? extends Feature> codec() {
		// 26.3: 每个注册的 feature 类型需要独立的 codec 对象，否则 MappedRegistry(IdentityHashMap) 会报重复值
		// 该 codec 用于从 JSON 反序列化 feature 配置（仅含 state 字段）
		return RecordCodecBuilder.<UndergroundPlantFeature>mapCodec(instance -> instance.group(
			BlockState.CODEC.fieldOf("state").forGetter(f -> f.state)
		).apply(instance, UndergroundPlantFeature::new));
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
		BlockPos origin = pos;
		int x = origin.getX();
		int z = origin.getZ();
		int placed = 0;

		for (int y = origin.getY(); y > level.getMinY(); y--) {
			if (placed >= maxCount)
				break;

			BlockPos pos1 = new BlockPos(x, y, z);
			if (!level.isEmptyBlock(pos1) || random.nextInt(6) == 0) {
				x = origin.getX() + random.nextInt(4) - random.nextInt(4);
				z = origin.getZ() + random.nextInt(4) - random.nextInt(4);
				continue;
			}

			BlockState state = this.state;
			Optional<StructureStart> structureStart = LandmarkUtil.locateNearestLandmarkStart(level, SectionPos.blockToSectionCoord(pos1.getX()), SectionPos.blockToSectionCoord(pos1.getZ()));
			if (state.is(TFBlocks.TROLLVIDR) && random.nextInt(10) == 0)
				state = TFBlocks.UNRIPE_TROLLBER.defaultBlockState();
			if (state.canSurvive(level, pos1) && (spawnInStructure || structureStart.isEmpty() || !structureStart.get().getBoundingBox().isInside(pos1))) {
				level.setBlock(pos1, state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
				placed++;
			}
		}
		return placed > 0;
	}
}
