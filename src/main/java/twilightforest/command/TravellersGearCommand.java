package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.entity.FakePlayer;
import twilightforest.TFRegistries;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.modifiers.InsertableTravellersModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiable;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.function.Function;

@twilightforest.beanification.Component
public class TravellersGearCommand {

	private static final DynamicCommandExceptionType ERROR_INVALID_MODIFIER = new DynamicCommandExceptionType(p_304101_ -> Component.translatableEscape("commands.tffeature.invalid_modifier", p_304101_));
	private final SimpleCommandExceptionType ERROR_NOT_RUN_BY_PLAYER = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.not_player"));
	private final SimpleCommandExceptionType ERROR_NOT_HOLDING_GEAR = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.not_travellers_gear"));
	private final SimpleCommandExceptionType ERROR_TOO_MANY_MODIFIERS = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.too_many_modifiers"));
	private final Function<Component, SimpleCommandExceptionType> ERROR_NO_MODIFIER = component -> new SimpleCommandExceptionType(Component.translatable("commands.tffeature.no_modifier", component));
	private final Function<Component, SimpleCommandExceptionType> ERROR_HAS_MODIFIER = component -> new SimpleCommandExceptionType(Component.translatable("commands.tffeature.has_modifier", component));
	private final Function<Component, SimpleCommandExceptionType> ERROR_WRONG_SLOT = component -> new SimpleCommandExceptionType(Component.translatable("commands.tffeature.wrong_modifier_slot", component));
	private final SimpleCommandExceptionType ERROR_ABILITY = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.ability_modifier"));

	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("travellers_gear").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
			.then(Commands.literal("give_set")
				.executes(context -> this.giveMaxedSet(context.getSource())))
			.then(Commands.literal("add_modifier")
				.then(Commands.argument("modifier", ResourceKeyArgument.key(TFRegistries.Keys.TRAVELLERS_MODIFIERS))
					.executes(context -> this.addModifier(context.getSource(), ResourceKeyArgument.resolveKey(context, "modifier", TFRegistries.Keys.TRAVELLERS_MODIFIERS, ERROR_INVALID_MODIFIER)))))
				.then(Commands.literal("remove_modifier")
					.then(Commands.argument("modifier", ResourceKeyArgument.key(TFRegistries.Keys.TRAVELLERS_MODIFIERS))
						.executes(context -> this.removeModifier(context.getSource(), ResourceKeyArgument.resolveKey(context, "modifier", TFRegistries.Keys.TRAVELLERS_MODIFIERS, ERROR_INVALID_MODIFIER)))));
	}

	private int giveMaxedSet(CommandSourceStack source) throws CommandSyntaxException {
		if (!(source.getEntity() instanceof Player player) || player instanceof FakePlayer) throw ERROR_NOT_RUN_BY_PLAYER.create();
		var registries = source.registryAccess();

		
		ItemStack goggles = new ItemStack(TFItems.TRAVELLERS_GOGGLES);
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER);
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.AQUATIC_AGILITY_MODIFIER);
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.RED_THREAD_VISION_MODIFIER);
		TravellersModifiersManager.addModifier(registries, goggles, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER);

		
		ItemStack vest = new ItemStack(TFItems.TRAVELLERS_VEST);
		vest.set(TFDataComponents.TRAVELLERS_HAS_GLOVES, Unit.INSTANCE);
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.STEALTH_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.ARROW_MAGNETISM_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.EFFICIENT_EATER_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.PERFECT_DODGE_MODIFIER);
		TravellersModifiersManager.addModifier(registries, vest, TravellersModifiersManager.HASTE_MODIFIER);

		
		ItemStack wings = new ItemStack(TFItems.TRAVELLERS_WINGS);
		wings.set(TFDataComponents.TRAVELLERS_HAS_BELT, Unit.INSTANCE);
		wings.set(TFDataComponents.SWAP_HOTBAR_ABILITY, Unit.INSTANCE);
		wings.set(DataComponents.CONTAINER, TravellersArmorBeltItem.DEFAULT_EMPTY_BELT_CONTAINER);
		TravellersModifiersManager.addModifier(registries, wings, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, wings, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, wings, TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER);
		TravellersModifiersManager.addModifier(registries, wings, TravellersModifiersManager.AGILE_RANGER_MODIFIER);
		TravellersModifiersManager.addModifier(registries, wings, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER);
		TravellersModifiersManager.addModifier(registries, wings, TravellersModifiersManager.SIDESTEP_MODIFIER);

		
		ItemStack boots = new ItemStack(TFItems.TRAVELLERS_BOOTS);
		TravellersModifiersManager.addModifier(registries, boots, TravellersModifiersManager.AUTO_REPAIR_MODIFIER);
		TravellersModifiersManager.addModifier(registries, boots, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER);
		TravellersModifiersManager.addModifier(registries, boots, TravellersModifiersManager.SLIMY_SOLES_MODIFIER);
		TravellersModifiersManager.addModifier(registries, boots, TravellersModifiersManager.UNRESTRAINED_MODIFIER);
		TravellersModifiersManager.addModifier(registries, boots, TravellersModifiersManager.WATER_WALK_MODIFIER);

		giveOrDrop(player, goggles);
		giveOrDrop(player, vest);
		giveOrDrop(player, wings);
		giveOrDrop(player, boots);

		source.sendSuccess(() -> Component.translatable("twilightforest.command.travellers_gear.give_set.success"), true);
		return Command.SINGLE_SUCCESS;
	}

	private static void giveOrDrop(Player player, ItemStack stack) {
		if (!player.getInventory().add(stack)) {
			player.drop(stack, false);
		}
	}

	private int addModifier(CommandSourceStack source, Holder.Reference<TravellersModifier> modifier) throws CommandSyntaxException {
		Context ctx = validate(source, modifier);
		if (TravellersModifiersManager.countInsertableModifiers(source.registryAccess(), ctx.stack()) >= ctx.item().getModifierSlots()) throw ERROR_TOO_MANY_MODIFIERS.create();
		if (TravellersModifiersManager.hasTravellersModifier(source.registryAccess(), ctx.stack(), modifier.key())) throw ERROR_HAS_MODIFIER.apply(ctx.modKey()).create();
		if (!modifier.value().group().test(ctx.player().getEquipmentSlotForItem(ctx.stack()))) throw ERROR_WRONG_SLOT.apply(ctx.modKey()).create();

		TravellersModifiersManager.addModifier(source.registryAccess(), ctx.stack(), modifier.key());
		source.sendSuccess(() -> Component.translatable("commands.tffeature.added_modifier", ctx.modKey(), ctx.stack().getHoverName()), true);
		return Command.SINGLE_SUCCESS;
	}

	private int removeModifier(CommandSourceStack source, Holder.Reference<TravellersModifier> modifier) throws CommandSyntaxException {
		Context ctx = validate(source, modifier);
		if (!TravellersModifiersManager.hasTravellersModifier(source.registryAccess(), ctx.stack(), modifier.key())) throw ERROR_NO_MODIFIER.apply(ctx.modKey()).create();

		((InsertableTravellersModifier) modifier.value()).removeModifier(ctx.stack());
		source.sendSuccess(() -> Component.translatable("commands.tffeature.removed_modifier", ctx.modKey(), ctx.stack().getHoverName()), true);
		return Command.SINGLE_SUCCESS;
	}

	private Context validate(CommandSourceStack source, Holder.Reference<TravellersModifier> modifier) throws CommandSyntaxException {
		if (!(source.getEntity() instanceof Player player) || player instanceof FakePlayer) throw ERROR_NOT_RUN_BY_PLAYER.create();
		if (!(player.getMainHandItem().getItem() instanceof TravellersModifiable armor)) throw ERROR_NOT_HOLDING_GEAR.create();
		if (modifier.value().isAbility()) throw ERROR_ABILITY.create();
		Component modKey = TravellersModifiersManager.getModifierTooltipComponent(modifier);
		return new Context(player, player.getMainHandItem(), armor, modKey);
	}

	private record Context(Player player, ItemStack stack, TravellersModifiable item, Component modKey) {}
}
