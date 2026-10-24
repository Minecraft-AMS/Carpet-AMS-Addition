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

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 使用列表中的元素提供补全建议
 *
 * <p>每个元素通过 {@link Object#toString()} 转换为建议文本，并按照列表的迭代顺序加入结果</p>
 *
 * @param <E> 列表元素类型
 */
public class ListSuggestionProvider<E> implements SuggestionProvider<CommandSourceStack> {
    /** 用于生成补全建议的元素列表*/
    private final List<E> options;

    /**
     * 创建列表补全提供器
     *
     * @param options 用于生成建议的元素列表
     */
    public ListSuggestionProvider(List<E> options) {
        this.options = options;
    }

    /**
     * 创建列表补全提供器
     *
     * @param options 用于生成建议的元素列表
     * @return 列表补全提供器
     */
    public static ListSuggestionProvider<?> of(List<?> options) {
        return new ListSuggestionProvider<>(options);
    }

    /**
     * 将列表中的全部元素转换为文本并加入补全结果
     *
     * @param context 当前指令上下文
     * @param builder 补全结果构建器
     * @return 异步补全结果
     */
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        options.forEach(option -> builder.suggest(option.toString()));
        return builder.buildFuture();
    }
}
