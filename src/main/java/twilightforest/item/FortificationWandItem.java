package twilightforest.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.init.TFDataAttachments;

public class FortificationWandItem extends ScepterItem {

	public FortificationWandItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult performScepterAction(Level level, ItemStack stack, Player player, InteractionHand hand) {
		if (!level.isClientSide()) {
			var attachment = TFDataAttachments.getOrCreate(player, TFDataAttachments.FORTIFICATION_SHIELDS, twilightforest.components.entity.FortificationShieldAttachment::new);
			if (attachment != null) {
				attachment.setShields(player, 5, true);
			}
		}
		player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 1.0F);

		if (!player.isCreative()) {
			player.getCooldowns().addCooldown(stack, 1200);
		}
		return InteractionResult.SUCCESS;
	}
}