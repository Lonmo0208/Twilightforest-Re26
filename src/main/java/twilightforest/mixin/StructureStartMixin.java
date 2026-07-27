package twilightforest.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.world.components.structures.util.ConqueredStructureStart;

@Mixin(StructureStart.class)
public abstract class StructureStartMixin implements ConqueredStructureStart {

	@Unique
	private boolean conquered = false;

	@Unique
	private int startY = 0;

	@Override
	public boolean isConquered() {
		return this.conquered;
	}

	@Override
	public void setConquered(boolean flag, LevelAccessor level) {
		if (this.conquered != flag) {
			ChunkPos chunkPos = ((StructureStart) (Object) this).getChunkPos();
			level.getChunk(chunkPos.x(), chunkPos.z()).markUnsaved();
		}
		this.conquered = flag;
	}

	@Override
	public void loadFromTag(CompoundTag nbt) {
		this.conquered = nbt.getBooleanOr("conquered", false);
		this.startY = nbt.getIntOr("knight_y", 0);
	}

	@Override
	public void setStartY(int startY) {
		this.startY = startY;
	}

	@Override
	public int getStartY() {
		return this.startY;
	}

	@Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
	private void tf$modifyBoundingBox(CallbackInfoReturnable<BoundingBox> cir) {
		if (this.startY > 0) {
			BoundingBox original = cir.getReturnValue();
			cir.setReturnValue(new BoundingBox(original.minX(), original.minY(), original.minZ(), original.maxX(), Math.min(original.maxY(), this.startY), original.maxZ()));
		}
	}

	@Inject(method = "createTag", at = @At("RETURN"))
	private void tf$addConquerData(StructurePieceSerializationContext context, ChunkPos chunkPos, CallbackInfoReturnable<CompoundTag> cir) {
		CompoundTag tag = cir.getReturnValue();
		if (tag.contains("id") && !"INVALID".equals(tag.getString("id"))) {
			tag.putBoolean("conquered", this.conquered);
			if (this.startY > 0) {
				tag.putInt("knight_y", this.startY);
			}
		}
	}

	@Inject(method = "loadStaticStart", at = @At("RETURN"))
	private static void tf$loadConquerData(StructurePieceSerializationContext context, CompoundTag tag, long seed, CallbackInfoReturnable<StructureStart> cir) {
		StructureStart start = cir.getReturnValue();
		if (start != null && start != StructureStart.INVALID_START) {
			((ConqueredStructureStart) (Object) start).loadFromTag(tag);
		}
	}
}