/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.api.command;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 指令描述树中的内部节点
 *
 * <p>相同父节点下具有相同名称的字面量或参数会被合并，用于组合多条共享前缀的路由</p>
 */
final class CommandNodeSpec {
    final CommandBuilder owner;
    final CommandNodeSpec parent;
    final String literal;
    final CommandArgument<?> argument;
    final Map<String, CommandNodeSpec> children = new LinkedHashMap<>();
    CommandExecutor executor;

    private CommandNodeSpec(CommandBuilder owner, CommandNodeSpec parent, String literal, CommandArgument<?> argument) {
        this.owner = owner;
        this.parent = parent;
        this.literal = literal;
        this.argument = argument;
    }

    static CommandNodeSpec root(CommandBuilder owner) {
        return new CommandNodeSpec(owner, null, null, null);
    }

    CommandNodeSpec literal(String name) {
        return this.child("literal:" + name, name, null);
    }

    CommandNodeSpec argument(CommandArgument<?> value) {
        return this.child("argument:" + value.getName(), null, value);
    }

    private CommandNodeSpec child(String key, String literal, CommandArgument<?> argument) {
        CommandNodeSpec existing = this.children.get(key);

        if (existing != null) {
            return existing;
        }

        CommandNodeSpec child = new CommandNodeSpec(this.owner, this, literal, argument);
        this.children.put(key, child);

        return child;
    }

    String path() {
        if (this.parent == null) {
            return "/" + this.owner.getName();
        }

        String name = this.literal != null ? this.literal : "<" + this.argument.getName() + ">";
        return this.parent.path() + " " + name;
    }
}
