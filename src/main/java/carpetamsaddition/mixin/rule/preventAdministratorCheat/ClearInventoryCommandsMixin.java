package carpetamsaddition.mixin.rule.preventAdministratorCheat;

import carpetamsaddition.helpers.rule.preventAdministratorCheat.PermissionHelper;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ClearInventoryCommands;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(ClearInventoryCommands.class)
public abstract class ClearInventoryCommandsMixin {
    @ModifyArg(
        method = "register",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
            remap = false
        ),
        require = 1,
        allow = 2
    )
    private static Predicate<CommandSourceStack> preventCheat(Predicate<CommandSourceStack> predicate) {
        return source -> predicate.test(source) && PermissionHelper.canCheat(source);
    }
}
