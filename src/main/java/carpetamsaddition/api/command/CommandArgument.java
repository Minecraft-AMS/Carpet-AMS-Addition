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

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.commands.CommandSourceStack;

/**
 * 类型化的指令参数描述
 *
 * <p>参数对象同时保存参数名称、底层 Brigadier 参数类型和取值逻辑
 * 读取操作按参数名进行，因此路由和执行器可以分别调用参数工厂，无需复用同一个对象</p>
 *
 * @param <T> 参数解析后的 Java 类型
 */
public interface CommandArgument<T> {
    /**
     * @return 参数在指令树中的名称
     */
    String getName();

    /**
     * 为当前 Minecraft 版本创建底层 Brigadier 参数类型
     *
     * @param context 指令注册上下文，可用于取得版本相关的注册表上下文
     * @return Brigadier 参数类型
     */
    ArgumentType<?> createType(CommandRegistrationContext context);

    /**
     * 从 Brigadier 上下文读取已经解析的参数值
     *
     * @param context Brigadier 指令上下文
     * @return 类型化参数值
     * @throws CommandSyntaxException 参数解析失败时抛出
     */
    T read(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;

    /**
     * 返回参数的补全建议器
     *
     * @return 补全建议器；没有自定义建议时返回 {@code null}
     */
    default SuggestionProvider<CommandSourceStack> suggestions() {
        return null;
    }
}
