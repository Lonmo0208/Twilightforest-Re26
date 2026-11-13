package twilightforest.command;

import com.mojang.brigadier.Command;
import net.minecraft.commands.arguments.BooleanArgument;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.commands.arguments.IntegerArgument;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandPermissionLevels;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.beanification.Component;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.init.TFDataAttachments;

@Component
public class ShieldCommand {

	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("shield")
			.requires(Commands.hasPermission(CommandPermissionLevels.LEVEL_GAMEMASTERS))
			.then(Commands.argument("target", EntityArgument.entity())
				.then(Commands.literal("set")
					.then(Commands.argument("amount", IntegerArgument.integer())
						.executes(ctx -> set(EntityArgument.getEntity(ctx, "target"), IntegerArgument.getInteger(ctx, "amount"), true))
						.then(Commands.argument("temp", BooleanArgument.bool())
							.executes(ctx -> set(EntityArgument.getEntity(ctx, "target"), IntegerArgument.getInteger(ctx, "amount"), BooleanArgument.getBool(ctx, "temp"))))))
				.then(Commands.literal("add")
					.then(Commands.argument("amount", IntegerArgument.integer())
						.executes(ctx -> add(EntityArgument.getEntity(ctx, "target"), IntegerArgument.getInteger(ctx, "amount"), true))
						.then(Commands.argument("temp", BooleanArgument.bool())
							.executes(ctx -> add(EntityArgument.getEntity(ctx, "target"), IntegerArgument.getInteger(ctx, "amount"), BooleanArgument.getBool(ctx, "temp")))))));
	}

	private FortificationShieldAttachment getOrCreateAttachment(LivingEntity living) {
		return TFDataAttachments.getOrCreate(living, TFDataAttachments.FORTIFICATION_SHIELDS, twilightforest.components.entity.FortificationShieldAttachment::new);
	}

	private int add(Entity e, int num, boolean temporary) {
		if (e instanceof LivingEntity living) {
			getOrCreateAttachment(living).addShields(living, num, temporary);
		}
		return Command.SINGLE_SUCCESS;
	}

	private int set(Entity e, int num, boolean temporary) {
		if (e instanceof LivingEntity living) {
			getOrCreateAttachment(living).setShields(living, num, temporary);
		}
		return Command.SINGLE_SUCCESS;
	}
}
