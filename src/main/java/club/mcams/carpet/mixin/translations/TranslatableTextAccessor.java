package club.mcams.carpet.mixin.translations;

import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;
//#if MC<11800
//$$ import net.minecraft.text.Text;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
//#if MC<11800
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//#endif

//#if MC<11800
//$$ import java.util.List;
//#endif
import java.util.function.Consumer;

@Mixin(TranslatableText.class)
public interface TranslatableTextAccessor {
    //#if MC>=11800
    @Invoker
    void invokeForEachPart(String translation, Consumer<StringVisitable> partsConsumer);
    //#else
    //$$ @Accessor
    //$$ List<StringVisitable> getTranslations();
    //$$ @Invoker
    //$$ void invokeSetTranslation(String translation);
    //#endif
}
