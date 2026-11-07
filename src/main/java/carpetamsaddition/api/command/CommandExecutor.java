/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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
     * @throws CommandSyntaxException 当来源或参数不满足执行要求时抛出
     */
    int execute(CommandExecutionContext context) throws CommandSyntaxException;
}
