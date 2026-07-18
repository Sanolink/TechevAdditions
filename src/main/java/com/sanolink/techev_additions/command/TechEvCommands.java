package com.sanolink.techev_additions.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sanolink.techev_additions.progression.TechEvProgression;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;

public class TechEvCommands {

    private static final String[] KNOWN_STAGES = {TechEvProgression.NO_TREE_PUNCHING_BYPASS};

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("techev")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("stage")

                                .then(Commands.literal("add")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .then(stageArgument()
                                                        .executes(context -> apply(context, true)))))

                                .then(Commands.literal("remove")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .then(stageArgument()
                                                        .executes(context -> apply(context, false)))))

                                .then(Commands.literal("list")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .executes(TechEvCommands::list))))
        );
    }

    private static com.mojang.brigadier.builder.RequiredArgumentBuilder<CommandSourceStack, String> stageArgument() {
        return Commands.argument("stage", StringArgumentType.word())
                .suggests((context, builder) -> SharedSuggestionProvider.suggest(KNOWN_STAGES, builder));
    }

    private static int apply(CommandContext<CommandSourceStack> context, boolean grant) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        String stage = StringArgumentType.getString(context, "stage");

        int changed = 0;
        for (ServerPlayer player : targets) {
            boolean applied = grant
                    ? TechEvProgression.grantStage(player, stage)
                    : TechEvProgression.revokeStage(player, stage);

            if (applied) {
                changed++;
            }
        }

        int count = changed;
        context.getSource().sendSuccess(Component.literal(
                (grant ? "Granted stage '" : "Revoked stage '") + stage + "' for " + count + " player(s)."), true);

        return count;
    }

    private static int list(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");

        for (ServerPlayer player : targets) {
            String stages = player.getTags().isEmpty() ? "(none)" : String.join(", ", player.getTags());
            context.getSource().sendSuccess(
                    Component.literal(player.getGameProfile().getName() + ": " + stages), false);
        }

        return targets.size();
    }
}
