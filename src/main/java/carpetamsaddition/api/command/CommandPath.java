/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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
