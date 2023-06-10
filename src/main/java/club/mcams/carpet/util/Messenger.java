package club.mcams.carpet.util;
//#if MC >= 11600 && MC < 11900
//$$ import net.minecraft.util.Util;
//#endif
//#if MC >= 11900
//$$ import net.minecraft.text.Text;
//#endif

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;

public final class Messenger {
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

    private static void __tell(ServerCommandSource source, BaseText text, boolean broadcastToOps) {
        source.sendFeedback(
                //#if MC >= 12000
                //$$ () ->
                //#endif
                text, broadcastToOps
        );
    }

    public static void tell(ServerCommandSource source, BaseText text, boolean broadcastToOps)
    {
        __tell(source, text, broadcastToOps);
    }

    public static void tell(ServerCommandSource source, BaseText text) {
        tell(source, text, false);
    }
}
