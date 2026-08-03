package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.NaturalSpawner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.events.EntityEvents;

/**
 * Replaces the biome spawn list with the structure-controlled spawn list while the
 * candidate position is inside a Twilight Forest controlled-spawns structure
 * (Tower, Lich Tower, Dark Tower, ...). This is the Fabric equivalent of the
 * NeoForge PotentialSpawns event that EntityEvents#structureSpecialSpawns listens to.
 */
@Mixin(NaturalSpawner.class)
public class NaturalSpawnerMixin {

	@Inject(
		method = "mobsAt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Holder;)Lnet/minecraft/util/random/WeightedList;",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void tf$structureSpawnList(ServerLevel level, StructureManager structureManager, ChunkGenerator generator, MobCategory mobCategory, BlockPos pos, @Nullable Holder<Biome> biome, CallbackInfoReturnable<WeightedList<MobSpawnSettings.SpawnerData>> cir) {
		WeightedList<MobSpawnSettings.SpawnerData> structureSpawns = EntityEvents.getPotentialStructureSpawns(structureManager, mobCategory, pos);
		if (structureSpawns != null && !structureSpawns.isEmpty()) {
			cir.setReturnValue(structureSpawns);
		}
	}
}
