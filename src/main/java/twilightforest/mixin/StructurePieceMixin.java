package twilightforest.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructurePiece.class)
public interface StructurePieceMixin {

	@Accessor("rotation")
	void setPieceRotation(Rotation rotation);

	@Accessor("mirror")
	void setPieceMirror(Mirror mirror);

	@Accessor("orientation")
	void setPieceOrientation(Direction orientation);
}