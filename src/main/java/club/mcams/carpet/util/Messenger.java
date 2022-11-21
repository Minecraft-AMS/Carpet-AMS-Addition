package club.mcams.carpet.util;
//#if MC >= 11600 && MC < 11900
//$$ import net.minecraft.util.Util;
//#endif

import net.minecraft.text.*;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class Messenger {
    public static
        //#if MC >= 11900
        //$$ TextContent
        //#else
    BaseText
    //#endif
    getTextContent(BaseText text) {
        //#if MC >= 11900
        //$$ return text.getContent();
        //#else
        return text;
        //#endif
    }

    /*
     * ----------------------------
     *    Text Factories - Basic
     * ----------------------------
     */

    // Compound Text in carpet style
    public static BaseText c(Object... fields) {
        return
                //#if MC >= 11900
                //$$ (MutableText)
                //#endif
                carpet.utils.Messenger.c(fields);
    }

    // Simple Text
    public static BaseText s(Object text) {
        return
                //#if MC >= 11900
                //$$ Text.literal
                //#else
                new LiteralText
                        //#endif
                        (text.toString());
    }
}
