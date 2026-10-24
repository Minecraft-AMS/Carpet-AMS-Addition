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
import carpetamsaddition.config.rule.blockChunkLoader.BlockLoaderConfigStore;
import carpetamsaddition.api.command.suggestionProviders.ListSuggestionProvider;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static carpetamsaddition.commands.rule.commandBlockChunkloader.CommandOperations.*;

public class ConditionSubcommands {
    private static final String[] DIRECTIONS = {
        "up", "down", "north", "south", "east", "west", "northeast", "northwest", "southeast", "southwest"
    };

    protected static void register(CommandBuilder command, LoaderType type) {
        CommandArgument<String> group = groupArgument(type);
        CommandArgument<String> direction = choice("direction", Arrays.asList(DIRECTIONS));
        CommandArgument<String> block = Arguments.blockPredicate("block");
        command.route(path(type, "condition", "list")
            .argument(group))
            .executes(c -> showGroup(c, type));
        command.route(path(type, "condition", "clear")
            .argument(group))
            .executes(c -> clearConditions(c, type));
        CommandArgument<Integer> id = conditionIdArgument(type, group);
        command.route(path(type, "condition", "remove")
            .argument(group)
            .argument(id))
            .executes(c -> removeCondition(c, type));
        for (boolean inverted : new boolean[]{false, true}) {
            String operation = inverted ? "not" : "is";
            command.route(path(type, "condition", "add")
                .argument(group).literal("direction")
                .argument(direction)
                .argument(Arguments.integer("distance", 1, 32))
                .literal(operation)
                .argument(block))
                .executes(c -> addDirectionCondition(c, type, inverted));
            command.route(path(type, "condition", "add")
                .argument(group).literal("offset")
                .argument(Arguments.integer("dx", -32, 32))
                .argument(Arguments.integer("dy", -32, 32))
                .argument(Arguments.integer("dz", -32, 32))
                .literal(operation).argument(block))
                .executes(c -> addOffsetCondition(c, type, inverted));
        }
    }

    private static CommandArgument<Integer> conditionIdArgument(LoaderType type, CommandArgument<String> group) {
        return Arguments.suggests(Arguments.integer("id", 1), (context, builder) -> {
            String name = group.read(context);
            BlockLoaderConfig.Group selected = BlockLoaderConfigStore.get(type).groups.get(name);
            List<Integer> ids = new ArrayList<>();

            if (selected != null) {
                for (LoaderCondition condition : selected.conditions) {
                    ids.add(condition.id);
                }
            }

            return ListSuggestionProvider.of(ids).getSuggestions(context, builder);
        });
    }

    private static int clearConditions(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        String name = context.get(Arguments.word("group"));

        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        BlockLoaderConfig.Group group = findGroup(context, config, name);

        if (group == null) {
            return 0;
        }

        group.conditions.clear();

        return save(context, type, config);
    }

    private static int removeCondition(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        String name = context.get(Arguments.word("group"));
        int selectedId = context.get(Arguments.integer("id"));

        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        BlockLoaderConfig.Group group = findGroup(context, config, name);

        if (group == null) {
            return 0;
        }

        if (!group.conditions.removeIf(condition -> condition.id == selectedId)) {
            return invalid(context, "condition id");
        }

        return save(context, type, config);
    }

    private static int addDirectionCondition(CommandExecutionContext context, LoaderType type, boolean inverted) throws CommandSyntaxException {
        String name = context.get(Arguments.word("group"));
        String directionName = context.get(Arguments.word("direction"));
        int distance = context.get(Arguments.integer("distance"));
        String blockValue = context.get(Arguments.blockPredicate("block"));

        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        int[] offset = offset(directionName, distance);

        if (offset == null) {
            return invalid(context, "direction");
        }

        return addCondition(context, type, config, name, offset[0], offset[1], offset[2], inverted, blockValue);
    }

    private static int addOffsetCondition(CommandExecutionContext context, LoaderType type, boolean inverted) throws CommandSyntaxException {
        String name = context.get(Arguments.word("group"));
        int dx = context.get(Arguments.integer("dx"));
        int dy = context.get(Arguments.integer("dy"));
        int dz = context.get(Arguments.integer("dz"));
        String blockValue = context.get(Arguments.blockPredicate("block"));

        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);

        return addCondition(context, type, config, name, dx, dy, dz, inverted, blockValue);
    }

    private static int addCondition(CommandExecutionContext context, LoaderType type, BlockLoaderConfig config, String name, int dx, int dy, int dz, boolean inverted, String block) {
        BlockLoaderConfig.Group group = findGroup(context, config, name);

        if (group == null) {
            return 0;
        }

        LoaderCondition condition = new LoaderCondition();
        condition.id = config.nextId++;
        condition.dx = dx;
        condition.dy = dy;
        condition.dz = dz;
        condition.inverted = inverted;
        condition.block = block;
        group.conditions.add(condition);

        return save(context, type, config);
    }

    private static int[] offset(String direction, int distance) {
        int x = 0, y = 0, z = 0;

        switch (direction) {
            case "up": y = 1; break;
            case "down": y = -1; break;
            case "north": z = -1; break;
            case "south": z = 1; break;
            case "east": x = 1; break;
            case "west": x = -1; break;
            case "northeast": x = 1; z = -1; break;
            case "northwest": x = -1; z = -1; break;
            case "southeast": x = 1; z = 1; break;
            case "southwest": x = -1; z = 1; break;
            default: return null;
        }

        return new int[]{x * distance, y * distance, z * distance};
    }
}
