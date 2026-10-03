/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.api.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * 指令执行期间使用的便捷上下文
 *
 * <p>该类封装 Brigadier 上下文，并提供服务器、玩家及类型化参数的快捷访问方法</p>
 */
public final class CommandExecutionContext {
    private final CommandContext<CommandSourceStack> context;

    CommandExecutionContext(CommandContext<CommandSourceStack> context) {
        this.context = context;
    }

    /** @return 当前指令来源 */
    public CommandSourceStack source() {
        return this.context.getSource();
    }

    /** @return 当前 Minecraft 服务器 */
    public MinecraftServer server() {
        return this.source().getServer();
    }

    /**
     * 获取执行指令的玩家
     *
     * @return 执行玩家
     * @throws CommandSyntaxException 指令来源不是玩家时抛出
     */
    public ServerPlayer player() throws CommandSyntaxException {
        return this.source().getPlayerOrException();
    }

    /**
     * 读取类型化参数值。
     *
     * @param argument 要读取的参数描述，只需与路由中的参数名称和类型一致
     * @param <T> 参数值类型
     * @return 已解析参数值
     * @throws CommandSyntaxException 参数读取失败时抛出
     */
    public <T> T get(CommandArgument<T> argument) throws CommandSyntaxException {
        return argument.read(this.context);
    }

    /**
     * 获取底层 Brigadier 上下文，用于兼容尚未封装的特殊逻辑
     *
     * @return 原始 Brigadier 上下文
     */
    public CommandContext<CommandSourceStack> rawContext() {
        return this.context;
    }
}
