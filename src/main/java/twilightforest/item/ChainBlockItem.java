package twilightforest.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;
import twilightforest.entity.projectile.ChainBlock;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;
import twilightforest.tags.TFBlockTags;

import java.util.UUID;

public class ChainBlockItem extends Item {

	public ChainBlockItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (!level.isClientSide() && stack.get(TFDataComponents.THROWN_PROJECTILE) != null && this.getThrownEntity(level, stack) == null) {
			stack.remove(TFDataComponents.THROWN_PROJECTILE);
		}
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.get(TFDataComponents.THROWN_PROJECTILE) != null || !level.getWorldBorder().isWithinBounds(player.blockPosition()))
			return InteractionResult.PASS;

		player.playSound(TFSounds.BLOCK_AND_CHAIN_FIRED, 0.5F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F));

		if (!level.isClientSide()) {
			ChainBlock launchedBlock = new ChainBlock(TFEntities.CHAIN_BLOCK.get(), level, player, hand, stack);
			level.addFreshEntity(launchedBlock);
			stack.set(TFDataComponents.THROWN_PROJECTILE, launchedBlock.getUUID());
		}

		player.startUsingItem(hand);
		return InteractionResult.SUCCESS;
	}

	@Nullable
	private ChainBlock getThrownEntity(Level level, ItemStack stack) {
		if (level instanceof ServerLevel server) {
			UUID id = stack.get(TFDataComponents.THROWN_PROJECTILE);
			if (id != null) {
				Entity e = server.getEntity(id);
				if (e instanceof ChainBlock) {
					return (ChainBlock) e;
				}
			}
		}

		return null;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BLOCK;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		// don't treat the chain block as a harvesting tool unless it is thrown
		if (stack.get(TFDataComponents.THROWN_PROJECTILE) == null || !state.is(TFBlockTags.MINEABLE_WITH_BLOCK_AND_CHAIN)) return false;
		int destruction = getDestructionLevel(stack);
		if (destruction <= 0) return false;
		// the destruction enchant grants a harvest tier: 1 = wood, 2 = stone, 3+ = iron
		// this mirrors vanilla's incorrect_for_<tier>_tool tags (denied blocks first)
		if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) return false;
		if (destruction < 3 && state.is(BlockTags.NEEDS_IRON_TOOL)) return false;
		if (destruction < 2 && state.is(BlockTags.NEEDS_STONE_TOOL)) return false;
		return true;
	}

	public static int getDestructionLevel(ItemStack stack) {
		ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
		for (Holder<Enchantment> holder : enchantments.keySet()) {
			if (holder.is(TFEnchantments.DESTRUCTION)) {
				return enchantments.getLevel(holder);
			}
		}
		return 0;
	}
}