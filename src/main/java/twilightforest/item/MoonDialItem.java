package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.MoonPhase;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFDimension;

import java.util.function.Consumer;

public class MoonDialItem extends Item {
	private static final String[] PHASE_TRANSLATION_KEYS = new String[]{
		"item.twilightforest.moon_dial.phase_0",
		"item.twilightforest.moon_dial.phase_1",
		"item.twilightforest.moon_dial.phase_2",
		"item.twilightforest.moon_dial.phase_3",
		"item.twilightforest.moon_dial.phase_4",
		"item.twilightforest.moon_dial.phase_5",
		"item.twilightforest.moon_dial.phase_6",
		"item.twilightforest.moon_dial.phase_7"
	};
	private static final String PHASE_UNKNOWN = "item.twilightforest.moon_dial.phase_unknown";
	private static final String PHASE_UNKNOWN_FOOLS = "item.twilightforest.moon_dial.phase_unknown_fools";

	public static int CLIENT_PHASE = -1;

	public MoonDialItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (!level.isClientSide() && slot == EquipmentSlot.MAINHAND && owner instanceof Player player) {
			int phase = calculatePhase(level);
			int prev = stack.getDamageValue();
			if (phase != prev) {
				stack.setDamageValue(phase);
				player.getCooldowns().addCooldown(stack, 20);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		int phase = CLIENT_PHASE >= 0 ? CLIENT_PHASE : stack.getDamageValue();
		if (phase >= 0 && phase < PHASE_TRANSLATION_KEYS.length) {
			builder.accept(Component.translatable(PHASE_TRANSLATION_KEYS[phase]).withStyle(ChatFormatting.GRAY));
		} else if (phase == -1) {
			builder.accept(Component.translatable(PHASE_UNKNOWN).withStyle(ChatFormatting.GRAY));
		} else if (phase == 404) {
			builder.accept(Component.translatable(PHASE_UNKNOWN_FOOLS).withStyle(ChatFormatting.RED));
		}
	}

	private int calculatePhase(ServerLevel level) {
		if (level.dimensionTypeRegistration().is(Identifier.fromNamespaceAndPath("minecraft", "the_end"))) {
			return 404;
		}
		if (level.dimensionType().hasFixedTime() && !level.dimension().equals(TFDimension.DIMENSION_KEY)) {
			return -1;
		}
		MoonPhase phase = level.environmentAttributes().getDimensionValue(EnvironmentAttributes.MOON_PHASE);
		if (phase != null) {
			return phase.index();
		}
		long dayTime = level.getGameTime();
		return (int) ((dayTime / 24000L) % 8L);
	}
}
