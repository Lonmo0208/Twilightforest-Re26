package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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
			.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
			.then(Commands.argument("target", EntityArgument.entity())
				.then(Commands.literal("set")
					.then(Commands.argument("amount", IntegerArgumentType.integer())
						.executes(ctx -> set(EntityArgument.getEntity(ctx, "target"), IntegerArgumentType.getInteger(ctx, "amount"), true))
						.then(Commands.argument("temp", BoolArgumentType.bool())
							.executes(ctx -> set(EntityArgument.getEntity(ctx, "target"), IntegerArgumentType.getInteger(ctx, "amount"), BoolArgumentType.getBool(ctx, "temp"))))))
				.then(Commands.literal("add")
					.then(Commands.argument("amount", IntegerArgumentType.integer())
						.executes(ctx -> add(EntityArgument.getEntity(ctx, "target"), IntegerArgumentType.getInteger(ctx, "amount"), true))
						.then(Commands.argument("temp", BoolArgumentType.bool())
							.executes(ctx -> add(EntityArgument.getEntity(ctx, "target"), IntegerArgumentType.getInteger(ctx, "amount"), BoolArgumentType.getBool(ctx, "temp")))))));
	}

	private FortificationShieldAttachment getOrCreateAttachment(LivingEntity living) {
		FortificationShieldAttachment attachment = living.getAttached(TFDataAttachments.FORTIFICATION_SHIELDS);
		if (attachment == null) {
			attachment = new FortificationShieldAttachment();
			living.setAttached(TFDataAttachments.FORTIFICATION_SHIELDS, attachment);
		}
		return attachment;
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
