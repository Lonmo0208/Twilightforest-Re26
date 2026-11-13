package twilightforest.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.suggestions.ServerOnlyCustomSuggestion;
import net.minecraft.commands.CommandPermissionLevels;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.commands.arguments.LootTableArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.beanification.Component;
import twilightforest.block.entity.spawner.SinisterSpawnerBlockEntity;

@Component
public class SinisterSpawnerCommand {
	public static final net.minecraft.util.context.ContextKeySet LOOT_TABLE_KEY_SET = net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ALL_PARAMS;

	// 26.4-snapshot-8: suggestions must be CustomSuggestion instances (raw brigadier lambdas are rejected)
	public static final ServerOnlyCustomSuggestion SUGGEST_LOOT_TABLE = (server, context, builder) -> {
		var lootTables = server.registryAccess().lookupOrThrow(Registries.LOOT_TABLE).listElements().map(holder -> holder.key().identifier());

		String remaining = builder.getRemaining().toLowerCase(java.util.Locale.ROOT);
		lootTables.forEach(id -> { String s = id.toString(); if (s.toLowerCase(java.util.Locale.ROOT).startsWith(remaining)) builder.suggest(s); });
		return builder.buildFuture();
	};

	public LiteralArgumentBuilder<CommandSourceStack> register(CommandBuildContext buildContext) {
		return Commands.literal("sinister_spawner")
			.requires(Commands.hasPermission(CommandPermissionLevels.LEVEL_GAMEMASTERS))
			.then(Commands.literal("add_particle").then(Commands.argument("particle", ParticleArgument.particle()).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::addParticle))))
			.then(Commands.literal("remove_particle").then(Commands.argument("particle", ParticleArgument.particle()).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::removeParticle))))
			.then(Commands.literal("set_loot").then(Commands.argument("loot", LootTableArgument.lootTable(LOOT_TABLE_KEY_SET)).suggests(SUGGEST_LOOT_TABLE.toBrigadier()).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::setLootTable))))
			.then(Commands.literal("clear_loot").then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::clearLootTable)))
			;
	}

	private int addParticle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ParticleOptions options = ParticleArgument.getParticle(context, "particle");
		BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");

		if (context.getSource().getLevel().getBlockEntity(pos) instanceof SinisterSpawnerBlockEntity entity) {
			if (entity.addParticle(options, true))
				return 1;
		}

		return 0;
	}

	private int removeParticle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ParticleOptions options = ParticleArgument.getParticle(context, "particle");
		BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");

		if (context.getSource().getLevel().getBlockEntity(pos) instanceof SinisterSpawnerBlockEntity entity) {
			if (entity.removeParticle(options, true))
				return 1;
		}

		return 0;
	}

	private int setLootTable(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
		Holder<LootTable> loot = LootTableArgument.getLootTable(context, "loot").validateAndGet(LOOT_TABLE_KEY_SET);

		if (context.getSource().getLevel().getBlockEntity(pos) instanceof SinisterSpawnerBlockEntity entity)
			if (entity.setLootTable(loot.unwrapKey().orElse(null)))
				return 1;

		return 0;
	}

	private int clearLootTable(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");

		if (context.getSource().getLevel().getBlockEntity(pos) instanceof SinisterSpawnerBlockEntity entity)
			if (entity.setLootTable(null))
				return 1;

		return 0;
	}
}
