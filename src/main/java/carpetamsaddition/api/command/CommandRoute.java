/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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
