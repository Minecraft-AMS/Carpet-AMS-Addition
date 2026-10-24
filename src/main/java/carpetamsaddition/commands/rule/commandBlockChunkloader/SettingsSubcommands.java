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
import carpetamsaddition.utils.messenger.Messenger;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;

import static carpetamsaddition.commands.rule.commandBlockChunkloader.CommandOperations.*;

public class SettingsSubcommands {
    protected static void register(CommandBuilder command, LoaderType type) {
        CommandArgument<String> range = choice("range", BlockLoaderConfig.RANGES);
        command.route(path(type, "show"))
            .executes(c -> show(c, type));
        command.route(path(type, "enabled")
            .argument(Arguments.bool("enabled")))
            .executes(c -> setEnabled(c, type));
        command.route(path(type, "time")
            .argument(Arguments.integer("time", 1, BlockLoaderConfig.MAX_TIME)))
            .executes(c -> setTime(c, type));
        command.route(path(type, "range")
            .argument(range))
            .executes(c -> setRange(c, type));
        command.route(path(type, "reset"))
            .executes(c -> reset(c, type));
        command.route(path(type, "test")
            .argument(Arguments.blockPos("pos")))
            .executes(c -> test(c, type));
    }

    private static int setEnabled(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        boolean value = context.get(Arguments.bool("enabled"));

        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        config.enabled = value;

        return save(context, type, config);
    }

    private static int setTime(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        int value = context.get(Arguments.integer("time"));

        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        config.time = value;

        return save(context, type, config);
    }

    private static int setRange(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        String value = context.get(Arguments.word("range"));
        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        if (!BlockLoaderConfig.RANGES.contains(value)) {
            return invalid(context, "range");
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.copy(type);
        config.range = value;

        return save(context, type, config);
    }

    private static int reset(CommandExecutionContext context, LoaderType type) {
        return save(context, type, new BlockLoaderConfig());
    }

    private static int test(CommandExecutionContext context, LoaderType type) throws CommandSyntaxException {
        if (!canUseCommand(context.source(), type)) {
            return deny(context);
        }

        ServerLevel world = context.source().getLevel();
        BlockPos origin = context.get(Arguments.blockPos("pos"));

        if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(origin.getX()), SectionPos.blockToSectionCoord(origin.getZ()))) {
            Messenger.tell(context.source(), tr.tr("unloaded"));
            return 0;
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.get(type);

        for (Map.Entry<String, BlockLoaderConfig.Group> entry : config.groups.entrySet()) {
            if (!entry.getValue().enabled) {
                sendGroupMessage(context, entry.getValue(), Messenger.c(
                    tr.tr("test_group", entry.getKey()),
                    tr.tr("group_disabled")
                ));
                continue;
            }
            if (entry.getValue().conditions.isEmpty()) {
                continue;
            }

            Messenger.tell(context.source(), Messenger.c(
                tr.tr("test_group", entry.getKey()),
                resultText(entry.getValue().evaluate(world, origin))
            ));

            for (LoaderCondition condition : entry.getValue().conditions) {
                Messenger.tell(context.source(), Messenger.c(
                    tr.tr("test_condition", condition.id),
                    resultText(condition.evaluate(world, origin))
                ));
            }
        }

        Messenger.tell(context.source(), tr.tr("test_result", config.matches(world, origin)));

        return 1;
    }

    private static MutableComponent resultText(LoaderCondition.Result result) {
        return tr.tr(result.name().toLowerCase(java.util.Locale.ROOT));
    }
}
