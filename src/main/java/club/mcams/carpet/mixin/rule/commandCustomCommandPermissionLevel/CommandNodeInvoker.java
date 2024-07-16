package club.mcams.carpet.mixin.rule.commandCustomCommandPermissionLevel;

import com.mojang.brigadier.tree.CommandNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Predicate;

@Mixin(CommandNode.class)
public interface CommandNodeInvoker<S> {
    @Accessor("requirement")
    void setRequirement(Predicate<S> requirement);
}
