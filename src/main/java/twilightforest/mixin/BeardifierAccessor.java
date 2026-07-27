package twilightforest.mixin;

import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(Beardifier.class)
public interface BeardifierAccessor {

	@Accessor("pieces")
	List<Beardifier.Rigid> tf$getPieces();

	@Accessor("junctions")
	List<JigsawJunction> tf$getJunctions();

	@Accessor("affectedBox")
	BoundingBox tf$getAffectedBox();

	@Invoker("<init>")
	static Beardifier tf$create(List<Beardifier.Rigid> pieces, List<JigsawJunction> junctions, BoundingBox affectedBox) {
		throw new AssertionError();
	}
}