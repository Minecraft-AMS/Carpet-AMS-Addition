/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.api.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * 指令执行回调
 *
 * <p>与 Brigadier 的执行器等价，但接收经过封装的 {@link CommandExecutionContext}</p>
 */
@FunctionalInterface
public interface CommandExecutor {
    /**
     * 执行指令逻辑
     *
     * @param context 当前指令执行上下文
     * @return 指令结果值，通常使用 {@link CommandResult}
     * @throws CommandSyntaxException 当来源或参数不满足执行要求时抛出
     */
    int execute(CommandExecutionContext context) throws CommandSyntaxException;
}
