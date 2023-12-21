/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.utils;

import club.mcams.carpet.mixin.translations.StyleAccessor;
import club.mcams.carpet.utils.compat.LiteralTextUtil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import static club.mcams.carpet.AmsServer.LOGGER;

public class Messenger {
    // Compound Text
    public static BaseText c(Object... fields) {
        //#if MC>=11900
        //$$ return (MutableText) carpet.utils.Messenger.c(fields);
        //#else
        return carpet.utils.Messenger.c(fields);
        //#endif
    }

    // Simple Text
    public static BaseText s(Object text) {
        return LiteralTextUtil.compatText(text.toString());
    }

    // Simple Text with carpet style
    public static BaseText s(Object text, String carpetStyle) {
        return formatting(s(text), carpetStyle);
    }

    // Simple Text with formatting
    public static BaseText s(Object text, Formatting textFormatting) {
        return formatting(s(text), textFormatting);
    }

    // Translation Text
    public static BaseText tr(String key, Object... args) {
        //#if MC>=11900
        //$$ return Text.translatable(key, args);
        //#else
        return new TranslatableText(key, args);
        //#endif
    }

    public static BaseText copy(BaseText text) {
        return (BaseText) text.shallowCopy();
    }

    private static void __tell(ServerCommandSource source, BaseText text, boolean broadcastToOps) {
        //#if MC>=12000
        //$$ source.sendFeedback(() -> text, broadcastToOps);
        //#else
        source.sendFeedback(text, broadcastToOps);
        //#endif
    }

    public static void tell(ServerCommandSource source, BaseText text, boolean broadcastToOps) {
        __tell(source, text, broadcastToOps);
    }

    public static void tell(ServerCommandSource source, BaseText text) {
        tell(source, text, false);
    }


    public static BaseText formatting(BaseText text, Formatting... formattings) {
        text.formatted(formattings);
        return text;
    }

    public static BaseText formatting(BaseText text, String carpetStyle) {
        Style textStyle = text.getStyle();
        StyleAccessor parsedStyle = (StyleAccessor) parseCarpetStyle(carpetStyle);
        textStyle =  textStyle.withColor(parsedStyle.getColorField());
        textStyle = textStyle.withBold(parsedStyle.getBoldField());
        textStyle = textStyle.withItalic(parsedStyle.getItalicField());
        ((StyleAccessor) textStyle).setUnderlinedField(parsedStyle.getUnderlineField());
        ((StyleAccessor) textStyle).setStrikethroughField(parsedStyle.getStrikethroughField());
        ((StyleAccessor) textStyle).setObfuscatedField(parsedStyle.getObfuscatedField());
        return style(text, textStyle);
    }

    public static BaseText style(BaseText text, Style style) {
        text.setStyle(style);
        return text;
    }

    public static Style parseCarpetStyle(String style) {
        return carpet.utils.Messenger.parseStyle(style);
    }

    public static void sendServerMessage(MinecraftServer server, String message) {
        BaseText text = c("gi "+ message);
        if (server == null) {
            LOGGER.error("Message not delivered: " + message);
        } else {
            //#if MC<11900
            server.sendSystemMessage(LiteralTextUtil.compatText(message), Util.NIL_UUID);
            //#else
            //$$ server.sendMessage(LiteralTextUtil.compatText(message));
            //#endif
            for (PlayerEntity entityplayer : server.getPlayerManager().getPlayerList()) {
                //#if MC<11900
                entityplayer.sendSystemMessage(text, Util.NIL_UUID);
                //#else
                //$$ entityplayer.sendMessage(text);
                //#endif
            }
        }
    }
}
