package club.mcams.carpet.mixin.translations;

import carpet.logging.HUDController;

import club.mcams.carpet.translations.AMSTranslations;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
//#if MC>=11900
//$$ import net.minecraft.text.Text;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HUDController.class)
public abstract class HUDControllerMixin {
    //#if MC>=11900
    //$$     @ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, remap = false)
    //$$   private static Text applyAMSTranslationToHudLoggerMessage(Text hudMessage, /* parent method parameters -> */ ServerPlayerEntity player, Text hudMessage_) {
    //$$        if (player != null) {
    //$$            hudMessage = AMSTranslations.translate((MutableText) hudMessage, player);
    //$$        }
    //$$        return hudMessage;
    //$$    }
    //#else
    @ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, remap = false)
    private static BaseText applyAMSTranslationToHudLoggerMessage(BaseText hudMessage, /* parent method parameters -> */ ServerPlayerEntity player, BaseText hudMessage_) {
        if (player != null) {
            hudMessage = AMSTranslations.translate(hudMessage, player);
        }
        return hudMessage;
    }
    //#endif
}
