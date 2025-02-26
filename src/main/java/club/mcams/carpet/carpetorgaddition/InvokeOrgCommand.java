package club.mcams.carpet.carpetorgaddition;

import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.MessageTextEventUtils.ClickEventUtil;
import club.mcams.carpet.utils.MessageTextEventUtils.HoverEventUtil;
import club.mcams.carpet.utils.Messenger;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class InvokeOrgCommand {
    public static Text highlightPosButton(String posText) {
        final Text hoverText = new Translator("org").tr("command.highlightPosButtonHoverText").formatted(Formatting.YELLOW);
        //#if MC>=12103
        return
            Messenger.s(" [+H]").setStyle(
                Style.EMPTY.withColor(Formatting.YELLOW).withBold(true).
                withClickEvent(ClickEventUtil.event(ClickEventUtil.RUN_COMMAND, "/highlight " + posText.replace(",", "") + " continue")).
                withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, hoverText))
            );
        //#else
        //$$ return Messenger.s("");
        //#endif
    }
}
