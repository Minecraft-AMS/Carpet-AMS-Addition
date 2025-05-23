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

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.commands.suggestionProviders.ListSuggestionProvider;
import club.mcams.carpet.commands.suggestionProviders.LiteralCommandSuggestionProvider;
import club.mcams.carpet.commands.suggestionProviders.SetSuggestionProvider;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.config.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class CustomCommandPermissionLevelRegistry {
    private static final Translator translator = new Translator("command.customCommandPermissionLevel");
    private static final String MSG_HEAD = "<customCommandPermissionLevel> ";
    public static final Map<String, Integer> COMMAND_PERMISSION_MAP = new ConcurrentHashMap<>();
    public static final Map<String, Predicate<ServerCommandSource>> DEFAULT_PERMISSION_MAP = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
        CommandManager.literal("customCommandPermissionLevel")
        .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCustomCommandPermissionLevel))
        .then(CommandManager.literal("set")
        .then(CommandManager.argument("command", StringArgumentType.string())
        .suggests(new LiteralCommandSuggestionProvider())
        .then(CommandManager.argument("permissionLevel", IntegerArgumentType.integer())
        .suggests(ListSuggestionProvider.of(CommandHelper.permissionLevels))
        .executes(context -> set(
            context.getSource().getServer(),
            context.getSource().getPlayer(),
            StringArgumentType.getString(context, "command"),
            IntegerArgumentType.getInteger(context, "permissionLevel")
        )))))
        .then(CommandManager.literal("remove")
        .then(CommandManager.argument("command", StringArgumentType.string())
        .suggests(SetSuggestionProvider.of(COMMAND_PERMISSION_MAP.keySet()))
        .executes(context -> remove(
            context.getSource().getServer(),
            context.getSource().getPlayer(),
            StringArgumentType.getString(context, "command")
        ))))
        .then(CommandManager.literal("removeAll")
        .executes(context -> removeAll(context.getSource().getServer(), context.getSource().getPlayer())))
        .then(CommandManager.literal("refresh")
        .executes(context -> refreshCommandTree(context.getSource().getServer())))
        .then(CommandManager.literal("list")
        .executes(context -> list(context.getSource().getPlayer())))
        .then(CommandManager.literal("help")
        .executes(context -> help(context.getSource().getPlayer()))));
    }

    private static int set(MinecraftServer server, ServerPlayerEntity player, String command, int permissionLevel) {
        if (Objects.equals(command, "customCommandPermissionLevel")) {
            player.sendMessage(
                Messenger.s(MSG_HEAD + translator.tr("cant_modify_self").getString())
                .formatted(Formatting.ITALIC, Formatting.RED), false
            );
            return 0;
        }
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
        saveToJson();
        CommandHelper.setPermission(server, command, permissionLevel);
        CommandHelper.updateAllCommandPermissions(server);
        return 1;
    }

    private static int remove(MinecraftServer server, PlayerEntity player, String command) {
        if (COMMAND_PERMISSION_MAP.containsKey(command)) {
            COMMAND_PERMISSION_MAP.remove(command);
            saveToJson();
            player.sendMessage(
                Messenger.s(
                    String.format("%s- %s", MSG_HEAD, command)
                ).formatted(Formatting.RED, Formatting.ITALIC), false
            );
            CommandHelper.updateAllCommandPermissions(server);
        } else {
            player.sendMessage(
                Messenger.s(
                    MSG_HEAD + command + translator.tr("not_found").getString()
                ).formatted(Formatting.RED, Formatting.ITALIC), false
            );
        }
        return 1;
    }

    private static int removeAll(MinecraftServer server, ServerPlayerEntity player) {
        if (!COMMAND_PERMISSION_MAP.isEmpty()) {
            COMMAND_PERMISSION_MAP.clear();
            saveToJson();
        }
        player.sendMessage(
            Messenger.s(
                MSG_HEAD + translator.tr("removeAll").getString()
            ).formatted(Formatting.RED, Formatting.ITALIC), false
        );
        CommandHelper.updateAllCommandPermissions(server);
        return 1;
    }

    private static int refreshCommandTree(MinecraftServer server) {
        CommandHelper.updateAllCommandPermissions(server);
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
            int permissionLevel = entry.getValue();
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
        final String refreshHelp = translator.tr("help.refresh").getString();
        final String listHelp = translator.tr("help.list").getString();
        player.sendMessage(
            Messenger.s(
                setHelp + "\n" + removeHelp + "\n" + removeAllHelp + "\n" + listHelp + "\n" + refreshHelp
            ).formatted(Formatting.GRAY), false
        );
        return 1;
    }

    private static void saveToJson() {
        CustomCommandPermissionLevelConfig.getInstance().saveToJson(COMMAND_PERMISSION_MAP);
    }
}
