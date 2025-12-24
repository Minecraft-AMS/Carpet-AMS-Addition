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

package carpetamsaddition.utils;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.mixin.rule.commandCustomCommandPermissionLevel.CommandNodeInvoker;
import carpetamsaddition.commands.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelRegistry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.ChatFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("EnhancedSwitchMigration")
public final class CommandHelper {
    public static final List<String> permissionLevels = Arrays.asList("0", "1", "2", "3", "4");
    private static final Translator tr = new Translator("command.commandHelper");

    private CommandHelper() {}

    @SuppressWarnings("unchecked")
    public static void updateAllCommandPermissions(MinecraftServer server) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandCustomCommandPermissionLevel, "false")) {
            CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
            Commands serverCommandManager = server.getCommands();

            for (CommandNode<CommandSourceStack> node : dispatcher.getRoot().getChildren()) {
                if (node instanceof LiteralCommandNode) {
                    String commandName = ((LiteralCommandNode<?>) node).getLiteral();
                    Predicate<CommandSourceStack> defaultRequirement = CustomCommandPermissionLevelRegistry.DEFAULT_PERMISSION_MAP.get(commandName);
                    if (CustomCommandPermissionLevelRegistry.COMMAND_PERMISSION_MAP.containsKey(commandName)) {
                        int level = CustomCommandPermissionLevelRegistry.COMMAND_PERMISSION_MAP.get(commandName);
                        ((CommandNodeInvoker<CommandSourceStack>) node).setRequirement(source -> hasPermissionLevel(source, level));
                    } else if (defaultRequirement != null) {
                        ((CommandNodeInvoker<CommandSourceStack>) node).setRequirement(defaultRequirement);
                    }
                }
            }

            server.getPlayerList().getPlayers().forEach(serverCommandManager::sendCommands);
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("refresh_cmd_tree"), ChatFormatting.GRAY));
        }
    }

    @SuppressWarnings("unchecked")
    public static void setPermission(MinecraftServer server, String command, int permissionLevel) {
        CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
        CommandNode<CommandSourceStack> target = dispatcher.getRoot().getChild(command);

        if (target != null) {
            ((CommandNodeInvoker<CommandSourceStack>) target).setRequirement(source -> hasPermissionLevel(source, permissionLevel));
        }
    }

    public static boolean canUseCommand(CommandSourceStack source, Object commandLevel) {
        if (commandLevel instanceof Boolean) {
            return (Boolean) commandLevel;
        }

        if (commandLevel instanceof String) {
            final String levelStr = ((String) commandLevel).toLowerCase(Locale.ENGLISH);

            switch (levelStr) {
                case "true": return true;
                case "false": return false;
                case "ops": return hasPermissionLevel(source, 2);
            }

            if (levelStr.length() == 1) {
                char c = levelStr.charAt(0);
                if (c >= '0' && c <= '4') {
                    return hasPermissionLevel(source, c - '0');
                }
            }
        }

        return false;
    }

    public static boolean hasPermissionLevel(CommandSourceStack source, int level) {
        Permission.HasCommandLevel requiredPermission = new Permission.HasCommandLevel(PermissionLevel.byId(level));

        if (level > 4) {
            return false;
        }

        return source.permissions().hasPermission(requiredPermission);
    }
}
