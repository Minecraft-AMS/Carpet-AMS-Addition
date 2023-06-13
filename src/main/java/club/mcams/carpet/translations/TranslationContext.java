package club.mcams.carpet.translations;

import net.minecraft.text.BaseText;

public class TranslationContext {
    private final Translator translator;

    protected TranslationContext(Translator translator) {
        this.translator = translator;
    }

    protected TranslationContext(String translationPath) {
        this(new Translator(translationPath));
    }

    public Translator getTranslator() {
        return translator;
    }

    protected BaseText tr(String key, Object... args) {
        return this.translator.tr(key, args);
    }
}
