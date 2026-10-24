/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.api.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

/**
 * 将 AMS 指令描述树编译为 Brigadier 节点的内部适配器
 */
public final class BrigadierCommandCompiler {
    private BrigadierCommandCompiler() {}

    /**
     * 编译并注册一条指令
     */
    public static void register(CommandBuilder definition, CommandRegistrationContext context) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(definition.requireName());
        root.requires(definition.requirement());
        attach(root, definition.root(), context);
        context.dispatcher().register(root);
    }

    private static void attach(ArgumentBuilder<CommandSourceStack, ?> builder, CommandNodeSpec node, CommandRegistrationContext context) {
        if (node.executor != null) {
            builder.executes(wrap(node.executor));
        }

        for (CommandNodeSpec child : node.children.values()) {
            ArgumentBuilder<CommandSourceStack, ?> childBuilder = createBuilder(child, context);
            attach(childBuilder, child, context);
            builder.then(childBuilder);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static ArgumentBuilder<CommandSourceStack, ?> createBuilder(
        CommandNodeSpec node,
        CommandRegistrationContext context
    ) {
        if (node.literal != null) {
            return Commands.literal(node.literal);
        }

        ArgumentType type = node.argument.createType(context);
        RequiredArgumentBuilder<CommandSourceStack, ?> builder = Commands.argument(node.argument.getName(), type);

        if (node.argument.suggestions() != null) {
            builder.suggests(node.argument.suggestions());
        }

        return builder;
    }

    private static Command<CommandSourceStack> wrap(CommandExecutor executor) {
        return context -> executor.execute(new CommandExecutionContext(context));
    }
}
