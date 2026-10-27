package twilightforest.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.DrinkFromFlaskTrigger;
import twilightforest.init.TFDataAttachments;

import java.net.URI;

/**
 * Mixin for {@link PlayerAdvancements} that replaces NeoForge
 * {@code AdvancementEvent.AdvancementEarnEvent} handlers:
 * <ul>
 *   <li>{@code alertPlayerCastleIsWIP} - warns player when completing the progression end advancement</li>
 *   <li>{@code resetFlaskLogic} - resets flask doses when a DrinkFromFlaskTrigger advancement is earned</li>
 * </ul>
 */
@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {

	@Shadow
	private ServerPlayer player;

	@Inject(method = "award", at = @At("HEAD"))
	private void tf$onAdvancementAward(AdvancementHolder advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
		if (advancement.id().equals(TwilightForestMod.prefix("progression_end"))) {
			player.sendSystemMessage(Component.translatable("gui.twilightforest.progression_end.message",
				Component.translatable("gui.twilightforest.progression_end.discord")
					.withStyle(style -> style.withColor(ChatFormatting.BLUE)
						.applyFormat(ChatFormatting.UNDERLINE)
						.withClickEvent(new ClickEvent.OpenUrl(URI.create("https://discord.experiment115.com/"))))));
		}

		for (var criteria : advancement.value().criteria().entrySet()) {
			if (criteria.getValue().trigger() instanceof DrinkFromFlaskTrigger) {
				TFDataAttachments.getOrCreate(player, TFDataAttachments.FLASK_DOSES, twilightforest.components.entity.PotionFlaskTrackingAttachment::new).resetDoses();
				break;
			}
		}
	}
}