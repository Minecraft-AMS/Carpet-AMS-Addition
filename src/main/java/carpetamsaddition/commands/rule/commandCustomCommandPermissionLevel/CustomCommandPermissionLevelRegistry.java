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

package carpetamsaddition.commands.rule.commandCustomCommandPermissionLevel;

import carpetamsaddition.AmsServerSettings;
import carpetamsaddition.commands.suggestionProviders.ListSuggestionProvider;
import carpetamsaddition.commands.suggestionProviders.LiteralCommandSuggestionProvider;
import carpetamsaddition.commands.suggestionProviders.SetSuggestionProvider;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.config.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.ChatFormatting;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class CustomCommandPermissionLevelRegistry {
    private static final Translator translator = new Translator("command.customCommandPermissionLevel");
    private static final String MSG_HEAD = "<customCommandPermissionLevel> ";
    public static final Map<String, Integer> COMMAND_PERMISSION_MAP = new ConcurrentHashMap<>();
    public static final Map<String, Predicate<CommandSourceStack>> DEFAULT_PERMISSION_MAP = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
        Commands.literal("customCommandPermissionLevel")
        .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCustomCommandPermissionLevel))
        .then(Commands.literal("set")
        .then(Commands.argument("command", StringArgumentType.string())
        .suggests(new LiteralCommandSuggestionProvider())
        .then(Commands.argument("permissionLevel", IntegerArgumentType.integer())
        .suggests(ListSuggestionProvider.of(CommandHelper.permissionLevels))
        .executes(context -> set(
            context.getSource().getServer(),
            context.getSource().getPlayerOrException(),
            StringArgumentType.getString(context, "command"),
            IntegerArgumentType.getInteger(context, "permissionLevel")
        )))))
        .then(Commands.literal("remove")
        .then(Commands.argument("command", StringArgumentType.string())
        .suggests(SetSuggestionProvider.of(COMMAND_PERMISSION_MAP.keySet()))
        .executes(context -> remove(
            context.getSource().getServer(),
            context.getSource().getPlayerOrException(),
            StringArgumentType.getString(context, "command")
        ))))
        .then(Commands.literal("removeAll")
        .executes(context -> removeAll(context.getSource().getServer(), context.getSource().getPlayerOrException())))
        .then(Commands.literal("refresh")
        .executes(context -> refreshCommandTree(context.getSource().getServer())))
        .then(Commands.literal("list")
        .executes(context -> list(context.getSource().getPlayerOrException())))
        .then(Commands.literal("help")
        .executes(context -> help(context.getSource().getPlayerOrException()))));
    }

    private static int set(MinecraftServer server, ServerPlayer player, String command, int permissionLevel) {
        if (Objects.equals(command, "customCommandPermissionLevel")) {
            player.displayClientMessage(
                Messenger.s(MSG_HEAD + translator.tr("cant_modify_self").getString())
                .withStyle(ChatFormatting.ITALIC, ChatFormatting.RED), false
            );
            return 0;
        }
        if (COMMAND_PERMISSION_MAP.containsKey(command)) {
            int oldPermissionLevel = COMMAND_PERMISSION_MAP.get(command);
            player.displayClientMessage(
                Messenger.s(
                    String.format("%s%s %d -> %d", MSG_HEAD, command, oldPermissionLevel, permissionLevel)
                ).withStyle(ChatFormatting.GREEN), false
            );
        } else {
            player.displayClientMessage(
                Messenger.s(
                    String.format("%s+ %s/%d", MSG_HEAD, command, permissionLevel)
                ).withStyle(ChatFormatting.GREEN), false
            );
        }
        COMMAND_PERMISSION_MAP.put(command, permissionLevel);
        saveToJson();
        CommandHelper.setPermission(server, command, permissionLevel);
        CommandHelper.updateAllCommandPermissions(server);
        return 1;
    }

    private static int remove(MinecraftServer server, Player player, String command) {
        if (COMMAND_PERMISSION_MAP.containsKey(command)) {
            COMMAND_PERMISSION_MAP.remove(command);
            saveToJson();
            player.displayClientMessage(
                Messenger.s(
                    String.format("%s- %s", MSG_HEAD, command)
                ).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC), false
            );
            CommandHelper.updateAllCommandPermissions(server);
        } else {
            player.displayClientMessage(
                Messenger.s(
                    MSG_HEAD + command + translator.tr("not_found").getString()
                ).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC), false
            );
        }
        return 1;
    }

    private static int removeAll(MinecraftServer server, ServerPlayer player) {
        if (!COMMAND_PERMISSION_MAP.isEmpty()) {
            COMMAND_PERMISSION_MAP.clear();
            saveToJson();
        }
        player.displayClientMessage(
            Messenger.s(
                MSG_HEAD + translator.tr("removeAll").getString()
            ).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC), false
        );
        CommandHelper.updateAllCommandPermissions(server);
        return 1;
    }

    private static int refreshCommandTree(MinecraftServer server) {
        CommandHelper.updateAllCommandPermissions(server);
        return 1;
    }

    private static int list(Player player) {
        final String LINE = "------------------------------------";
        player.displayClientMessage(
            Messenger.s(
                translator.tr("list").getString() + "\n" + LINE
            ).withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD), false
        );
        for (Map.Entry<String, Integer> entry : COMMAND_PERMISSION_MAP.entrySet()) {
            String command = entry.getKey();
            int permissionLevel = entry.getValue();
            player.displayClientMessage(
                Messenger.s(command + " -> " + permissionLevel).withStyle(ChatFormatting.DARK_AQUA),
                false
            );
        }
        return 1;
    }

    private static int help(Player player) {
        final String setHelp = translator.tr("help.set").getString();
        final String removeHelp = translator.tr("help.remove").getString();
        final String removeAllHelp = translator.tr("help.removeAll").getString();
        final String refreshHelp = translator.tr("help.refresh").getString();
        final String listHelp = translator.tr("help.list").getString();
        player.displayClientMessage(
            Messenger.s(
                setHelp + "\n" + removeHelp + "\n" + removeAllHelp + "\n" + listHelp + "\n" + refreshHelp
            ).withStyle(ChatFormatting.GRAY), false
        );
        return 1;
    }

    private static void saveToJson() {
        CustomCommandPermissionLevelConfig.getInstance().saveToJson(COMMAND_PERMISSION_MAP);
    }
}
