package club.mcams.carpet.commands.commandNodes;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.function.Predicate;

public class ExtensiveLiteralCommandNode<S> extends LiteralCommandNode<S> {
    private final Predicate<S> command_requirement;

    public ExtensiveLiteralCommandNode(String literal, Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks) {
        super(literal, command, requirement, redirect, modifier, forks);
    }
}
