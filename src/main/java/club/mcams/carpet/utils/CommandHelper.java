/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.utils;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.commands.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelRegistry;
import club.mcams.carpet.mixin.rule.commandCustomCommandPermissionLevel.CommandNodeInvoker;
import club.mcams.carpet.translations.Translator;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
//#if MC>=12111
//$$ import net.minecraft.command.permission.PermissionPredicate;
//$$ import net.minecraft.command.permission.Permission;
//$$ import net.minecraft.command.permission.PermissionLevel;
//#endif

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("EnhancedSwitchMigration")
public final class CommandHelper {
    public static final List<String> permissionLevels = Arrays.asList("0", "1", "2", "3", "4");
    private static final Translator translator = new Translator("command.commandHelper");

    private CommandHelper() {}

    @SuppressWarnings("unchecked")
    public static void updateAllCommandPermissions(MinecraftServer server) {
        if (!Objects.equals(AmsServerSettings.commandCustomCommandPermissionLevel, "false")) {
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
            CommandManager serverCommandManager = server.getCommandManager();
            for (CommandNode<ServerCommandSource> node : dispatcher.getRoot().getChildren()) {
                if (node instanceof LiteralCommandNode) {
                    String commandName = ((LiteralCommandNode<?>) node).getLiteral();
                    Predicate<ServerCommandSource> defaultRequirement = CustomCommandPermissionLevelRegistry.DEFAULT_PERMISSION_MAP.get(commandName);
                    if (CustomCommandPermissionLevelRegistry.COMMAND_PERMISSION_MAP.containsKey(commandName)) {
                        int level = CustomCommandPermissionLevelRegistry.COMMAND_PERMISSION_MAP.get(commandName);
                        //#if MC>=12111
                        //$$ Predicate<ServerCommandSource> permissionPredicate = createCompatPermissionPredicate(level);
                        //$$ ((CommandNodeInvoker<ServerCommandSource>) node).setRequirement(permissionPredicate);
                        //#else
                        ((CommandNodeInvoker<ServerCommandSource>) node).setRequirement(source -> source.hasPermissionLevel(level));
                        //#endif
                    } else if (defaultRequirement != null) {
                        ((CommandNodeInvoker<ServerCommandSource>) node).setRequirement(defaultRequirement);
                    }
                }
            }
            server.getPlayerManager().getPlayerList().forEach(serverCommandManager::sendCommandTree);
            Messenger.sendServerMessage(server, Messenger.s("§o§7Server: " + translator.tr("refresh_cmd_tree").getString()));
        }
    }

    @SuppressWarnings("unchecked")
    public static void setPermission(MinecraftServer server, String command, int permissionLevel) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
        CommandNode<ServerCommandSource> target = dispatcher.getRoot().getChild(command);
        if (target != null) {
            //#if MC>=12111
            //$$ Predicate<ServerCommandSource> permissionPredicate = createCompatPermissionPredicate(permissionLevel);
            //$$ ((CommandNodeInvoker<ServerCommandSource>) target).setRequirement(permissionPredicate);
            //#else
            ((CommandNodeInvoker<ServerCommandSource>) target).setRequirement(source -> source.hasPermissionLevel(permissionLevel));
            //#endif
        }
    }

    public static boolean canUseCommand(ServerCommandSource source, Object commandLevel) {
        if (commandLevel instanceof Boolean) {
            return (Boolean) commandLevel;
        }

        if (commandLevel instanceof String) {
            final String levelStr = ((String) commandLevel).toLowerCase(Locale.ENGLISH);
            switch (levelStr) {
                case "true": return true;
                case "false": return false;
                //#if MC>=12111
                //$$ case "ops": return hasPermissionLevel(source, 2);
                //#else
                case "ops": return source.hasPermissionLevel(2);
                //#endif
            }
            if (levelStr.length() == 1) {
                char c = levelStr.charAt(0);
                if (c >= '0' && c <= '4') {
                    //#if MC>=12111
                    //$$ return hasPermissionLevel(source, c - '0');
                    //#else
                    return source.hasPermissionLevel(c - '0');
                    //#endif
                }
            }
        }

        return false;
    }

    //#if MC>=12111
    //$$ public static Predicate<ServerCommandSource> createCompatPermissionPredicate(int level) {
    //$$     return source -> hasPermissionLevel(source, level);
    //$$ }
    //$$
    //$$ public static boolean hasPermissionLevel(ServerCommandSource source, int level) {
    //$$    Permission.Level requiredPermission = createPermissionLevel(level);
    //$$
    //$$    if (level > 4) {
    //$$        return false;
    //$$    }
    //$$
    //$$    return source.getPermissions().hasPermission(requiredPermission);
    //$$ }
    //$$
    //$$ public static Permission.Level createPermissionLevel(int level) {
    //$$     PermissionLevel permissionLevel = PermissionLevel.fromLevel(level);
    //$$     return new Permission.Level(permissionLevel);
    //$$ }
    //#endif
}
