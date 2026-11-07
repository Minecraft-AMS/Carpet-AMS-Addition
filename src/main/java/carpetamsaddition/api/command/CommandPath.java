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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 可组合的复杂指令路径
 *
 * <p>用于表达 {@code literal -> argument -> literal -> argument} 等无法通过
 * {@link CommandBuilder#route(String, CommandArgument[])} 简写表示的结构。</p>
 */
public final class CommandPath {
    private CommandPath() {}

    private final List<Object> segments = new ArrayList<>();

    /**
     * 创建空路径
     *
     * @return 新的路径构建对象
     */
    public static CommandPath path() {
        return new CommandPath();
    }

    /**
     * 在路径末尾添加字面量节点
     *
     * @param literal 字面量内容
     * @return 当前路径
     */
    public CommandPath literal(String literal) {
        this.segments.add(Objects.requireNonNull(literal, "literal"));
        return this;
    }

    /**
     * 在路径末尾添加参数节点
     *
     * @param argument 类型化参数
     * @return 当前路径
     */
    public CommandPath argument(CommandArgument<?> argument) {
        this.segments.add(Objects.requireNonNull(argument, "argument"));
        return this;
    }

    List<Object> segments() {
        return Collections.unmodifiableList(this.segments);
    }
}
