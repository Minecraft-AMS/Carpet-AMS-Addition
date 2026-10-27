/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.commands.rule.commandBlockChunkloader;

import carpetamsaddition.api.command.*;
import carpetamsaddition.config.rule.blockChunkLoader.BlockLoaderConfig;
import carpetamsaddition.config.rule.blockChunkLoader.BlockLoaderConfigStore;
import carpetamsaddition.helpers.rule.blockChunkLoader.LoaderType;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.Objects;

import static carpetamsaddition.commands.rule.commandBlockChunkloader.CommandOperations.*;

public class GroupSubcommands {
    protected static void register(CommandBuilder command, LoaderType type) {
        CommandArgument<String> group = groupArgument(type);
        CommandArgument<String> mode = choice("mode", Arrays.asList("all", "any"));
        command.route(path(type, "group", "list"))
            .executes(c -> show(c, type));
        command.route(path(type, "group", "show")
            .argument(group))
            .executes(c -> showGroup(c, type));
        command.route(path(type, "group", "create")
            .argument(Arguments.word("name")))
            .executes(c -> createGroup(c, type));
        command.route(path(type, "group", "remove")
            .argument(group))
            .executes(c -> removeGroup(c, type));
        command.route(path(type, "group", "clear"))
            .executes(c -> clearGroups(c, type));
        command.route(path(type, "group", "mode")
            .argument(group)
            .argument(mode))
            .executes(c -> setGroupMode(c, type));
        command.route(path(type, "group", "enabled")
            .argument(group)
            .argument(Arguments.bool("enabled")))
            .executes(c -> setGroupEnabled(c, type));
    }

    private static int createGroup(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        String name = context.get(Arguments.word("name"));

        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        if (!name.matches("[a-zA-Z0-9_-]{1,32}") || config.groups.containsKey(name)) {
            return invalid(context, "group name");
        }

        config.groups.put(name, new BlockLoaderConfig.Group());

        return save(context, type, config);
    }

    private static int removeGroup(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        String name = context.get(Arguments.word("group"));

        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        if (findGroup(context, config, name) == null) {
            return 0;
        }

        config.groups.remove(name);

        return save(context, type, config);
    }

    private static int clearGroups(CommandExecutionContext context, LoaderType type) {
        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        config.groups.clear();
        config.groups.put("default", new BlockLoaderConfig.Group());

        return save(context, type, config);
    }

    private static int setGroupEnabled(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        String name = context.get(Arguments.word("group"));
        boolean value = context.get(Arguments.bool("enabled"));

        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        BlockLoaderConfig.Group group = findGroup(context, config, name);

        if (group == null) {
            return 0;
        }

        group.enabled = value;

        return save(context, type, config);
    }

    private static int setGroupMode(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        String name = context.get(Arguments.word("group"));
        String value = context.get(Arguments.word("mode"));

        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        if (!Objects.equals(value, "all") && !Objects.equals(value, "any")) {
            return invalid(context, "mode");
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        BlockLoaderConfig.Group group = findGroup(context, config, name);

        if (group == null) {
            return 0;
        }

        group.any = Objects.equals(value, "any");

        return save(context, type, config);
    }
}
