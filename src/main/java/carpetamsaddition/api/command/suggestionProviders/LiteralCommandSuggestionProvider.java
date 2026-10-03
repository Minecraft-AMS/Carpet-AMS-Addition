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

package carpetamsaddition.api.command.suggestionProviders;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.commands.CommandSourceStack;

import java.util.concurrent.CompletableFuture;

/**
 * 提供指令根节点下所有一级指令名称的补全建议
 *
 * <p>该提供器适用于需要输入已注册指令名称的参数，例如自定义指令权限配置</p>
 */
public class LiteralCommandSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    /**
     * 从当前 Brigadier 指令树的根节点收集一级子节点名称
     *
     * @param context 当前指令上下文
     * @param builder 补全结果构建器
     * @return 异步补全结果
     */
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        context.getRootNode().getChildren().forEach(node -> {
            if (node != null && node.getName() != null) {
                builder.suggest(node.getName());
            }
        });

        return builder.buildFuture();
    }
}
