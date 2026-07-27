package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;


/**
 * Mixin to add NeoForge-compatible methods to Block.
 */
@Mixin(Block.class)
public class BlockMixin {

    // NeoForge: getToolModifiedState
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, String itemAbility, boolean simulate) {
        return state;
    }

    // NeoForge: onDestroyedByPlayer with extra params
    public BlockState onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
        return state;
    }

    // NeoForge: onCaughtFire
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, Direction face, LivingEntity igniter) {
    }

    // NeoForge: getFriction
    public float getFriction(BlockState state, Level level, BlockPos pos, Entity entity) {
        return state.getBlock().getFriction();
    }

    // NeoForge: hasCorrectToolForDrops with extra params
    public boolean hasCorrectToolForDrops(BlockState state, Level level, BlockPos pos) {
        return state.requiresCorrectToolForDrops();
    }

    // NeoForge: getCloneItemStack with extra params
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack((Block) (Object) this);
    }
}