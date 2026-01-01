package club.mcams.carpet.mixin.translations;

import carpet.logging.HUDController;

import club.mcams.carpet.translations.AMSTranslations;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.19")
@Mixin(HUDController.class)
public abstract class HUDControllerMixin {
    @ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, remap = false, name = "arg1")
    private static Text applyAMSTranslationToHudLoggerMessage(Text hudMessage, ServerPlayerEntity player, Text hudMessage_) {
        if (player != null) {
            hudMessage = AMSTranslations.translate((MutableText) hudMessage, player);
        }
        return hudMessage;
    }
}
