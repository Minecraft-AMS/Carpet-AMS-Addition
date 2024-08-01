/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.commands.rule.commandCustomCommandPermissionLevel;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.commands.suggestionProviders.ListSuggestionProvider;
import club.mcams.carpet.commands.suggestionProviders.LiteralCommandSuggestionProvider;
import club.mcams.carpet.mixin.rule.commandCustomCommandPermissionLevel.CommandNodeInvoker;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.config.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

public class CustomCommandPermissionLevelRegistry {
    private static final Translator translator = new Translator("command.customCommandPermissionLevel");
    private static final String MSG_HEAD = "<customCommandPermissionLevel> ";
    public static final Map<String, Integer> COMMAND_PERMISSION_MAP = new HashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
        CommandManager.literal("customCommandPermissionLevel")
        .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCustomCommandPermissionLevel))
        .then(CommandManager.literal("set")
        .then(CommandManager.argument("command", StringArgumentType.string()).suggests(new LiteralCommandSuggestionProvider())
        .then(CommandManager.argument("permissionLevel", IntegerArgumentType.integer()).suggests(ListSuggestionProvider.of(CommandHelper.permissionLevels))
        .executes(context -> set(
            context.getSource().getServer(),
            context.getSource().getPlayer(),
            StringArgumentType.getString(context, "command"),
            IntegerArgumentType.getInteger(context, "permissionLevel")
        )))))
        .then(CommandManager.literal("remove")
        .then(CommandManager.argument("command", StringArgumentType.string()).suggests(ListSuggestionProvider.of(COMMAND_PERMISSION_MAP.keySet().stream().toList()))
        .executes(context -> remove(
            context.getSource().getServer(),
            context.getSource().getPlayer(),
            StringArgumentType.getString(context, "command")
        ))))
        .then(CommandManager.literal("removeAll")
        .executes(context -> removeAll(context.getSource().getServer(), context.getSource().getPlayer())))
        .then(CommandManager.literal("list")
        .executes(context -> list(context.getSource().getPlayer())))
        .then(CommandManager.literal("help")
        .executes(context -> help(context.getSource().getPlayer()))));
    }

    private static int set(MinecraftServer server, PlayerEntity player, String command, int permissionLevel) {
        if (COMMAND_PERMISSION_MAP.containsKey(command)) {
            int oldPermissionLevel = COMMAND_PERMISSION_MAP.get(command);
            player.sendMessage(
                Messenger.s(
                    String.format("%s%s %d -> %d", MSG_HEAD, command, oldPermissionLevel, permissionLevel)
                ).formatted(Formatting.GREEN), false
            );
        } else {
            player.sendMessage(
                Messenger.s(
                    String.format("%s+ %s/%d", MSG_HEAD, command, permissionLevel)
                ).formatted(Formatting.GREEN), false
            );
        }
        COMMAND_PERMISSION_MAP.put(command, permissionLevel);
        saveToJson(server);
        setPermission(server,player, command, permissionLevel);
        return 1;
    }

    private static void setPermission(MinecraftServer server, PlayerEntity player, String command, int permissionLevel) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

        CommandNode<ServerCommandSource> target = dispatcher.getRoot().getChild(command);

        ((CommandNodeInvoker<ServerCommandSource>)target).setRequirement(source -> source.hasPermissionLevel(permissionLevel));

        refreshCommandNodeTree();
    }

    private static int remove(MinecraftServer server, PlayerEntity player, String command) {
        if (COMMAND_PERMISSION_MAP.containsKey(command)) {
            COMMAND_PERMISSION_MAP.remove(command);
            saveToJson(server);
            player.sendMessage(
                Messenger.s(
                    String.format("%s- %s", MSG_HEAD, command)
                ).formatted(Formatting.RED, Formatting.ITALIC), false
            );
            refreshCommandNodeTree();
        } else {
            player.sendMessage(
                Messenger.s(
                    MSG_HEAD + command + translator.tr("not_found").getString()
                ).formatted(Formatting.RED, Formatting.ITALIC), false
            );
        }
        return 1;
    }

    private static int removeAll(MinecraftServer server, PlayerEntity player) {
        if (!COMMAND_PERMISSION_MAP.isEmpty()) {
            COMMAND_PERMISSION_MAP.clear();
            saveToJson(server);
            refreshCommandNodeTree();
        }
        player.sendMessage(
                Messenger.s(
                        MSG_HEAD + translator.tr("removeAll").getString()
                ).formatted(Formatting.RED, Formatting.ITALIC), false
        );
        return 1;
    }

    private static int list(PlayerEntity player) {
        final String LINE = "------------------------------------";
        player.sendMessage(
            Messenger.s(
                translator.tr("list").getString() + "\n" + LINE
            ).formatted(Formatting.AQUA, Formatting.BOLD), false
        );
        for (Map.Entry<String, Integer> entry : COMMAND_PERMISSION_MAP.entrySet()) {
            String command = entry.getKey();
            float permissionLevel = entry.getValue();
            player.sendMessage(
                Messenger.s(command + " -> " + permissionLevel).formatted(Formatting.DARK_AQUA),
                false
            );
        }
        return 1;
    }

    private static int help(PlayerEntity player) {
        final String setHelp = translator.tr("help.set").getString();
        final String removeHelp = translator.tr("help.remove").getString();
        final String removeAllHelp = translator.tr("help.removeAll").getString();
        final String listHelp = translator.tr("help.list").getString();
        player.sendMessage(
            Messenger.s(
                setHelp + "\n" + removeHelp + "\n" + removeAllHelp + "\n" + listHelp
            ).formatted(Formatting.GRAY), false
        );
        return 1;
    }

    private static void saveToJson(MinecraftServer server) {
        final String CONFIG_PATH = CustomCommandPermissionLevelConfig.getPath(server);
        CustomCommandPermissionLevelConfig.saveToJson(COMMAND_PERMISSION_MAP, CONFIG_PATH);
    }

    private static void refreshCommandNodeTree() {
        CommandHelper.notifyPlayersCommandsChanged(AmsServer.minecraftServer);
    }
}
