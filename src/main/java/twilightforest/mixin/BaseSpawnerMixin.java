package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BaseSpawner.class)
public interface BaseSpawnerMixin {

	@Accessor("spawnDelay")
	int getSpawnDelay();

	@Accessor("spawnDelay")
	void setSpawnDelay(int value);

	@Accessor("spawnRange")
	int getSpawnRange();

	@Accessor("spawnRange")
	void setSpawnRange(int value);

	@Accessor("spin")
	void setSpin(double value);

	@Invoker("delay")
	void invokeDelay(Level level, BlockPos pos);

	@Invoker("isNearPlayer")
	boolean invokeIsNearPlayer(Level level, BlockPos pos);

	@Invoker("getOrCreateNextSpawnData")
	SpawnData invokeGetOrCreateNextSpawnData(Level level, RandomSource random, BlockPos pos);
}