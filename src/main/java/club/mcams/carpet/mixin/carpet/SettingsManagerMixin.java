package club.mcams.carpet.mixin.carpet;

import carpet.settings.SettingsManager;
import carpet.utils.Messenger;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerMod;

import net.minecraft.server.command.ServerCommandSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static carpet.utils.Translations.tr;

@Mixin(SettingsManager.class)
public abstract class SettingsManagerMixin {
    @SuppressWarnings("DefaultAnnotationParam")
    @Inject(
            method = "listAllSettings",
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=ui.version",  // after printed fabric-carpet version
                            ordinal = 0
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lcarpet/settings/SettingsManager;getCategories()Ljava/lang/Iterable;",
                    ordinal = 0
            ),
            remap = false
    )
    private void printVersion(ServerCommandSource source, CallbackInfoReturnable<Integer> cir) {
        Messenger.m(source,
                String.format("g %s ", AmsServer.fancyName),
                String.format("g %s: ", tr("ui.version",  "version")),
                String.format("g %s", AmsServerMod.getVersion())
        );
    }
}
