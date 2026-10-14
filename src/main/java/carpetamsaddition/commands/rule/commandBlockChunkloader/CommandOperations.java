/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.commands.rule.commandBlockChunkloader;

import carpetamsaddition.api.command.*;
import carpetamsaddition.config.rule.blockChunkLoader.BlockLoaderConfig;
import carpetamsaddition.helpers.rule.blockChunkLoader.LoaderCondition;
import carpetamsaddition.helpers.rule.blockChunkLoader.LoaderType;
import carpetamsaddition.api.command.suggestionProviders.ListSuggestionProvider;
import carpetamsaddition.api.command.suggestionProviders.SetSuggestionProvider;
import carpetamsaddition.config.rule.blockChunkLoader.BlockLoaderConfigStore;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.messenger.Messenger;
import carpetamsaddition.utils.Layout;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class CommandOperations {
    protected static final Translator tr = new Translator("command.blockChunkloader");

    protected static CommandArgument<String> groupArgument(LoaderType type) {
        return Arguments.suggests(Arguments.word("group"), (context, builder) ->
            SetSuggestionProvider.of(BlockLoaderConfigStore.get(type).groups.keySet()).getSuggestions(context, builder)
        );
    }

    protected static CommandArgument<String> choice(String name, List<String> values) {
        return Arguments.suggests(Arguments.word(name), ListSuggestionProvider.of(values));
    }

    protected static CommandPath path(LoaderType type, String... literals) {
        CommandPath path = CommandPath.path().literal(type.id);

        for (String literal : literals) {
            path.literal(literal);
        }

        return path;
    }

    protected static boolean canUseCommand(CommandSourceStack source, LoaderType type) {
        return CommandHelper.canUseCommand(source, type.permission());
    }

    protected static BlockLoaderConfig.Group findGroup(CommandExecutionContext context, BlockLoaderConfig config, String name) {
        BlockLoaderConfig.Group group = config.groups.get(name);

        if (group == null) {
            invalid(context, "group: " + name);
        }

        return group;
    }

    protected static int save(CommandExecutionContext context, LoaderType type, BlockLoaderConfig config) {
        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        try {
            config.renumberConditions();
            BlockLoaderConfigStore.commit(type, config);
            Messenger.tell(context.source(), tr.tr("saved", type.id));
            return 1;
        } catch (Exception e) {
            return invalid(context, e.getMessage());
        }
    }

    protected static int show(CommandExecutionContext context, LoaderType type) {
        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.get(type);
        Messenger.tell(context.source(), tr.tr("show", type.id, config.enabled, config.time, config.range));

        for (String group : config.groups.keySet()) {
            showGroup(context, type, group);
        }

        return 1;
    }

    protected static int showGroup(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        return showGroup(context, type, context.get(Arguments.word("group")));
    }

    private static int showGroup(CommandExecutionContext context, LoaderType type, String name) {
        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig.Group group = BlockLoaderConfigStore.get(type).groups.get(name);

        if (group == null) {
            Messenger.tell(context.source(), tr.tr("missing_group", name));
            return 0;
        }

        sendGroupMessage(context, group, Messenger.c(
            tr.tr("group", name),
            tr.tr(group.enabled ? "group_enabled" : "group_disabled"),
            tr.tr("group_mode"),
            tr.tr(group.any ? "any" : "all"),
            tr.tr("group_details", group.conditions.size())
        ));

        for (LoaderCondition condition : group.conditions) {
            sendGroupMessage(context, group, Messenger.c(
                tr.tr("condition", condition.id, condition.dx, condition.dy, condition.dz),
                tr.tr(condition.inverted ? "not" : "is"),
                Messenger.s(" " + condition.block)
            ));
        }

        return 1;
    }

    protected static int invalid(CommandExecutionContext context, String reason) {
        Messenger.tell(context.source(), tr.tr("invalid", reason));
        return 0;
    }

    protected static void sendGroupMessage(CommandExecutionContext context, BlockLoaderConfig.Group group, MutableComponent message) {
        if (!group.enabled) {
            Messenger.f(message, Layout.GRAY);
        }

        Messenger.tell(context.source(), message);
    }

    protected static int deny(CommandExecutionContext context) {
        Messenger.tell(context.source(), tr.tr("denied"));
        return 0;
    }
}
