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

/**
 * 一条已经声明完成的指令路由
 *
 * <p>通过该对象为路由绑定执行器，绑定完成后会返回所属的 {@link CommandBuilder} 以便继续声明其他平级路由</p>
 */
public final class CommandRoute {
    private final CommandNodeSpec node;

    CommandRoute(CommandNodeSpec node) {
        this.node = node;
    }

    /**
     * 为当前路由绑定执行器。
     *
     * @param executor 指令执行回调
     * @return 当前指令的构建器
     * @throws IllegalStateException 当前路由已经绑定执行器时抛出
     */
    public CommandBuilder executes(CommandExecutor executor) {
        if (this.node.executor != null) {
            throw new IllegalStateException("Command route already has an executor: " + this.node.path());
        }

        this.node.executor = executor;

        return this.node.owner;
    }
}
