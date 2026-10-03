/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.api.command;

import net.minecraft.commands.CommandSourceStack;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * AMS 指令声明构建器
 *
 * <p>构建器先生成与 Brigadier 无关的指令描述树，再由内部编译器统一转换为 Brigadier 节点</p>
 */
public final class CommandBuilder {
    private final CommandNodeSpec root = CommandNodeSpec.root(this);
    private String name;
    private Predicate<CommandSourceStack> requirement = source -> true;

    /**
     * 设置根指令名称，每条指令只能设置一次
     *
     * @param name 不包含斜杠的根指令名称
     * @return 当前构建器
     */
    public CommandBuilder name(String name) {
        if (this.name != null) {
            throw new IllegalStateException("Command name has already been set to " + this.name);
        }

        this.name = Objects.requireNonNull(name, "name");

        return this;
    }

    /**
     * 使用 Carpet Rule 的动态值控制指令权限
     *
     * <p>规则值会在每次权限判断时读取，支持 {@code true}、{@code false}、{@code ops} 和 {@code 0-4} 权限等级</p>
     *
     * @param rule 提供 Carpet Rule 当前值的函数
     * @return 当前构建器
     */
    public CommandBuilder carpetRule(Supplier<?> rule) {
        Objects.requireNonNull(rule, "rule");
        return this.requires(source -> CommandHelper.canUseCommand(source, rule.get()));
    }

    /**
     * 设置固定命令权限等级
     *
     * @param level Minecraft 命令权限等级
     * @return 当前构建器
     */
    public CommandBuilder permissionLevel(int level) {
        return this.requires(source -> CommandHelper.hasPermissionLevel(source, level));
    }

    /**
     * 允许所有来源使用该指令
     *
     * @return 当前构建器
     */
    public CommandBuilder allowAll() {
        return this.requires(source -> true);
    }

    /**
     * 设置自定义权限判断器
     *
     * @param requirement 接收指令来源的权限判断器
     * @return 当前构建器
     */
    public CommandBuilder requires(Predicate<CommandSourceStack> requirement) {
        this.requirement = Objects.requireNonNull(requirement, "requirement");
        return this;
    }

    /**
     * 为根指令绑定执行器
     *
     * @param executor 指令执行回调
     * @return 当前构建器
     */
    public CommandBuilder executes(CommandExecutor executor) {
        this.route().executes(executor);
        return this;
    }

    /**
     * 声明一条只包含参数节点的路由
     *
     * @param arguments 按顺序排列的参数
     * @return 等待绑定执行器的路由
     */
    public CommandRoute route(CommandArgument<?>... arguments) {
        return this.append(this.root, arguments);
    }

    /**
     * 声明以一个字面量开头，后接零个或多个参数的路由
     *
     * @param literal 子指令字面量
     * @param arguments 后续参数
     * @return 等待绑定执行器的路由
     */
    public CommandRoute route(String literal, CommandArgument<?>... arguments) {
        return this.append(this.root.literal(literal), arguments);
    }

    /**
     * 使用完整路径声明复杂路由
     *
     * @param path 可混合多个字面量与参数的路径
     * @return 等待绑定执行器的路由
     */
    public CommandRoute route(CommandPath path) {
        CommandNodeSpec node = this.root;

        for (Object segment : Objects.requireNonNull(path, "path").segments()) {
            if (segment instanceof String) {
                node = node.literal((String) segment);
            } else {
                node = node.argument((CommandArgument<?>) segment);
            }
        }

        return new CommandRoute(node);
    }

    private CommandRoute append(CommandNodeSpec start, CommandArgument<?>... arguments) {
        CommandNodeSpec node = start;
        for (CommandArgument<?> argument : arguments) {
            node = node.argument(Objects.requireNonNull(argument, "argument"));
        }
        return new CommandRoute(node);
    }

    String getName() {
        return this.name == null ? "<unnamed>" : this.name;
    }

    public String requireName() {
        if (this.name == null || this.name.isEmpty()) {
            throw new IllegalStateException("Command name must be set");
        }
        return this.name;
    }

    Predicate<CommandSourceStack> requirement() {
        return this.requirement;
    }

    CommandNodeSpec root() {
        return this.root;
    }
}
