package club.mcams.carpet.mixin.translations;

import club.mcams.carpet.translations.AMSTranslations;
import club.mcams.carpet.translations.TranslationConstants;

import net.minecraft.text.TranslatableText;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * From Carpet TIS Addition
 */
@Mixin(TranslatableText.class)
public abstract class TranslatableTextMixin {
    @Shadow
    @Final
    private String key;

    @ModifyArg(
            method = "updateTranslations",
            at = @At(
                    value = "INVOKE",
                    //#if MC<11800
                    //$$ target = "Lnet/minecraft/text/TranslatableText;setTranslation(Ljava/lang/String;)V"
                    //#else
                    target = "Lnet/minecraft/text/TranslatableText;forEachPart(Ljava/lang/String;Ljava/util/function/Consumer;)V"
                    //#endif
            )
    )
    private String applyAMSTranslation(String vanillaTranslatedFormattingString) {
        if (this.key.startsWith(TranslationConstants.TRANSLATION_KEY_PREFIX) && vanillaTranslatedFormattingString.equals(this.key)) {
            String amsTranslated = AMSTranslations.translateKeyToFormattedString(AMSTranslations.getServerLanguage(), this.key);
            if (amsTranslated != null) {
                return amsTranslated;
            }
        }
        return vanillaTranslatedFormattingString;
    }
}
