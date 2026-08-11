package twilightforest.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * In Fabric 26.1.2, the vanilla SIGN/HANGING_SIGN block entity types are created with
 * an immutable Set.of() of valid blocks. The Twilight Forest sign blocks are not present
 * in that set, so placing them throws "Invalid block entity" in BlockEntity#validateBlockState.
 *
 * The original NeoForge registration (addBlockEntityTypes -> event.modify) is NeoForge-only
 * dead code. This mixin makes isValid() accept any sign block for the matching sign type.
 */
@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {

	@Inject(
		method = "isValid",
		at = @At("HEAD"),
		cancellable = true
	)
	private void twilightforest$acceptTwilightSigns(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		BlockEntityType<?> self = (BlockEntityType<?>) (Object) this;
		Block block = state.getBlock();

		if (self == BlockEntityType.SIGN) {
			if (block instanceof StandingSignBlock || block instanceof WallSignBlock) {
				cir.setReturnValue(true);
			}
		} else if (self == BlockEntityType.HANGING_SIGN) {
			if (block instanceof CeilingHangingSignBlock || block instanceof WallHangingSignBlock) {
				cir.setReturnValue(true);
			}
		}
	}
}