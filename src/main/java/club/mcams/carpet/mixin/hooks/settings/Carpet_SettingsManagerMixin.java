package club.mcams.carpet.mixin.hooks.settings;

import carpet.settings.ParsedRule;
import carpet.settings.SettingsManager;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.server.command.ServerCommandSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SettingsManager.class)
public abstract class Carpet_SettingsManagerMixin {
    @Shadow
    protected abstract int setDefault(ServerCommandSource source, ParsedRule<?> rule, String value);

    @Inject(method = "setRule", at = @At("RETURN"))
    private void alwaysSetDefaultRule(ServerCommandSource source, ParsedRule<?> rule, String value, CallbackInfoReturnable<Integer> cir) {
        if (
            //#if MC>=11900
            //$$ AmsServerSettings.MUST_SET_DEFAULT_RULES.contains(rule.name())
            //#else
            AmsServerSettings.MUST_SET_DEFAULT_RULES.contains(rule.name)
            //#endif
        ) {
            this.setDefault(source, rule, value);
        }
    }
}
