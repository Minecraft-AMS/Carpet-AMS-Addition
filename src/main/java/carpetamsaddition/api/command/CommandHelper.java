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

package carpetamsaddition.api.command;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.mixin.rule.commandCustomCommandPermissionLevel.CommandNodeInvoker;
import carpetamsaddition.commands.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelCommand;

import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.MinecraftServerUtil;
import carpetamsaddition.utils.messenger.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
//#if MC>=12111
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
//#endif

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 命令工具类，提供命令权限相关的静态辅助方法
 */
public final class CommandHelper {
    public static final List<String> permissionLevels = Arrays.asList("0", "1", "2", "3", "4");
    private static final Translator tr = new Translator("command.commandHelper");

    private CommandHelper() {}

    /**
     * 批量更新服务器所有命令的权限要求。
     * 仅当规则 {@code commandCustomCommandPermissionLevel} 未设置为 "false" 时生效：
     * 遍历根命令节点的所有子命令，若该命令在自定义权限映射中，则应用自定义权限等级；
     * 否则回退到默认权限要求。更新完成后向在线玩家同步命令树，并广播刷新完成消息。
     */
    @SuppressWarnings("unchecked")
    public static void updateAllCommandPermissions(MinecraftServer server) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandCustomCommandPermissionLevel, "false")) {
            CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
            Commands serverCommandManager = server.getCommands();

            for (CommandNode<CommandSourceStack> node : dispatcher.getRoot().getChildren()) {
                if (node instanceof LiteralCommandNode) {
                    String commandName = ((LiteralCommandNode<?>) node).getLiteral();
                    Predicate<CommandSourceStack> defaultRequirement = CustomCommandPermissionLevelCommand.DEFAULT_PERMISSION_MAP.get(commandName);
                    if (CustomCommandPermissionLevelCommand.COMMAND_PERMISSION_MAP.containsKey(commandName)) {
                        int level = CustomCommandPermissionLevelCommand.COMMAND_PERMISSION_MAP.get(commandName);
                        ((CommandNodeInvoker<CommandSourceStack>) node).setRequirement(source -> hasPermissionLevel(source, level));
                    } else if (defaultRequirement != null) {
                        ((CommandNodeInvoker<CommandSourceStack>) node).setRequirement(defaultRequirement);
                    }
                }
            }

            MinecraftServerUtil.getOnlinePlayers().forEach(serverCommandManager::sendCommands);
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("refresh_cmd_tree"), Layout.GRAY), true);
        }
    }

    /**
     * 为指定命令设置新的权限等级要求。
     * 若目标命令不存在则静默忽略，存在则用给定的权限等级重写其权限判定。
     */
    @SuppressWarnings("unchecked")
    public static void setPermission(MinecraftServer server, String command, int permissionLevel) {
        CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
        CommandNode<CommandSourceStack> target = dispatcher.getRoot().getChild(command);

        if (target != null) {
            ((CommandNodeInvoker<CommandSourceStack>) target).setRequirement(source -> hasPermissionLevel(source, permissionLevel));
        }
    }

    /**
     * 判断命令来源是否有权使用命令。
     * 兼容三种配置形态：布尔值直接作为结果，字符串支持 "true" / "false" / "ops"（等价 2 级权限）
     * 以及单个字符 "0"~"4"（对应权限等级），其余情况一律视为无权。
     */
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

    /**
     * 按权限等级检查命令来源是否具备对应权限
     */
    public static boolean hasPermissionLevel(CommandSourceStack source, int level) {
        //#if MC>=12111
        Permission.HasCommandLevel requiredPermission = new Permission.HasCommandLevel(PermissionLevel.byId(level));

        if (level > 4) {
            return false;
        }

        return source.permissions().hasPermission(requiredPermission);
        //#else
        //$$ return source.hasPermission(level);
        //#endif
    }
}
