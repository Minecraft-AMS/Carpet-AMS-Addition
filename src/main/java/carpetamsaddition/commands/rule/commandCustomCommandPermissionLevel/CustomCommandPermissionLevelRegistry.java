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

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.commands.suggestionProviders.ListSuggestionProvider;
import carpetamsaddition.commands.suggestionProviders.LiteralCommandSuggestionProvider;
import carpetamsaddition.commands.suggestionProviders.SetSuggestionProvider;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.config.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class CustomCommandPermissionLevelRegistry {
    private static final Translator tr = new Translator("command.customCommandPermissionLevel");
    public static final Map<String, Integer> COMMAND_PERMISSION_MAP = new ConcurrentHashMap<>();
    public static final Map<String, Predicate<CommandSourceStack>> DEFAULT_PERMISSION_MAP = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
        Commands.literal("customCommandPermissionLevel")
        .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandCustomCommandPermissionLevel))

        // set
        .then(Commands.literal("set")
        .then(Commands.argument("command", StringArgumentType.string())
        .suggests(new LiteralCommandSuggestionProvider())
        .then(Commands.argument("permissionLevel", IntegerArgumentType.integer())
        .suggests(ListSuggestionProvider.of(CommandHelper.permissionLevels))
        .executes(context -> set(
            context.getSource(),
            context.getSource().getServer(),
            StringArgumentType.getString(context, "command"),
            IntegerArgumentType.getInteger(context, "permissionLevel")
        )))))

        // remove
        .then(Commands.literal("remove")
        .then(Commands.argument("command", StringArgumentType.string())
        .suggests(SetSuggestionProvider.of(COMMAND_PERMISSION_MAP.keySet()))
        .executes(context -> remove(
            context.getSource(),
            context.getSource().getServer(),
            StringArgumentType.getString(context, "command")
        ))))

        // remove all
        .then(Commands.literal("removeAll")
        .executes(context -> removeAll(context.getSource(), context.getSource().getServer())))

        // refresh
        .then(Commands.literal("refresh")
        .executes(context -> refreshCommandTree(context.getSource().getServer())))

        // list
        .then(Commands.literal("list")
        .executes(context -> list(context.getSource())))

        // help
        .then(Commands.literal("help")
        .executes(context -> help(context.getSource()))));
    }

    private static int set(CommandSourceStack source, MinecraftServer server, String command, int permissionLevel) {
        if (Objects.equals(command, "customCommandPermissionLevel")) {
            Messenger.tell(source, Messenger.f(tr.tr("cant_modify_self"), Layout.ITALIC, Layout.RED));
            return 0;
        }

        if (COMMAND_PERMISSION_MAP.containsKey(command)) {
            int oldPermissionLevel = COMMAND_PERMISSION_MAP.get(command);
            Messenger.tell(source, Messenger.f(tr.tr("modify_set", command, oldPermissionLevel, permissionLevel), Layout.GREEN));
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("set", command, permissionLevel), Layout.GREEN));
        }

        COMMAND_PERMISSION_MAP.put(command, permissionLevel);
        saveToJson();
        CommandHelper.setPermission(server, command, permissionLevel);
        CommandHelper.updateAllCommandPermissions(server);

        return 1;
    }

    private static int remove(CommandSourceStack source, MinecraftServer server, String command) {
        if (COMMAND_PERMISSION_MAP.containsKey(command)) {
            COMMAND_PERMISSION_MAP.remove(command);
            saveToJson();
            Messenger.tell(source, Messenger.f(tr.tr("remove", command), Layout.ITALIC, Layout.RED));
            CommandHelper.updateAllCommandPermissions(server);
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("not_found", command), Layout.ITALIC, Layout.RED));
        }

        return 1;
    }

    private static int removeAll(CommandSourceStack source, MinecraftServer server) {
        if (!COMMAND_PERMISSION_MAP.isEmpty()) {
            COMMAND_PERMISSION_MAP.clear();
            saveToJson();
        }

        Messenger.tell(source, Messenger.f(tr.tr("removeAll"), Layout.ITALIC, Layout.RED));
        CommandHelper.updateAllCommandPermissions(server);

        return 1;
    }

    private static int refreshCommandTree(MinecraftServer server) {
        CommandHelper.updateAllCommandPermissions(server);
        return 1;
    }

    private static int list(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(
            Messenger.c(
                tr.tr("list_title"),
                Messenger.endl(),
                Messenger.dline()
            ), Layout.DARK_AQUA, Layout.BOLD
        ));

        for (Map.Entry<String, Integer> entry : COMMAND_PERMISSION_MAP.entrySet()) {
            String command = entry.getKey();
            int permissionLevel = entry.getValue();
            Messenger.tell(source, Messenger.f(Messenger.s(String.format("%s -> %s", command, permissionLevel)), Layout.DARK_AQUA));
        }

        return 1;
    }

    private static int help(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(
            Messenger.c(
                tr.tr("help.set"), Messenger.endl(),
                tr.tr("help.remove"), Messenger.endl(),
                tr.tr("help.removeAll"), Messenger.endl(),
                tr.tr("help.refresh"), Messenger.endl(),
                tr.tr("help.list"), Messenger.endl()
            ), Layout.GRAY
        ));

        return 1;
    }

    private static void saveToJson() {
        CustomCommandPermissionLevelConfig.getInstance().saveToJson(COMMAND_PERMISSION_MAP);
    }
}
