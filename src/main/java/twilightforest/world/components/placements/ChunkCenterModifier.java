package twilightforest.world.components.placements;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.function.Consumer;

public class ChunkCenterModifier implements PlacementModifier {
	private static final ChunkCenterModifier INSTANCE = new ChunkCenterModifier();
	public static final MapCodec<ChunkCenterModifier> CODEC = MapCodec.unit(() -> INSTANCE);

	public static ChunkCenterModifier center() {
		return INSTANCE;
	}

	@Override
	public void modify(PlacementContext ctx, RandomSource random, BlockPos pos, Consumer<BlockPos> collector) {
		collector.accept(new BlockPos((pos.getX() & 0xfffffff0) + 8, pos.getY(), (pos.getZ() & 0xfffffff0) + 8));
	}

	@Override
	public MapCodec<? extends PlacementModifier> codec() {
		return CODEC;
	}
}
