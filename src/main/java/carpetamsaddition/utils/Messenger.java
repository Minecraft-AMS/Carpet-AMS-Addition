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

package carpetamsaddition.utils;

import carpetamsaddition.utils.MessageTextEventUtils.ClickEventUtil;
import carpetamsaddition.utils.MessageTextEventUtils.HoverEventUtil;
import carpetamsaddition.utils.compat.MessengerCompatFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Messenger {
    // Compound Text
    public static MutableComponent c(Object... fields) {
        return MessengerCompatFactory.CarpetCompoundText(fields);
    }

    // Simple Text
    public static MutableComponent s(Object text) {
        return MessengerCompatFactory.LiteralText(text.toString());
    }

    // Simple Text with formatting
    public static MutableComponent s(Object text, ChatFormatting textFormatting) {
        return formatting(s(text), textFormatting);
    }

    // Translation Text
    public static MutableComponent tr(String key, Object... args) {
        return MessengerCompatFactory.TranslatableText(key, args);
    }

    @NotNull
    public static MutableComponent copy(MutableComponent text) {
        return text.copy();
    }

    private static void __tell(CommandSourceStack source, MutableComponent text, boolean broadcastToOps) {
        MessengerCompatFactory.sendFeedBack(source, text, broadcastToOps);
    }

    public static void tell(CommandSourceStack source, MutableComponent text, boolean broadcastToOps) {
        __tell(source, text, broadcastToOps);
    }

    public static void tell(CommandSourceStack source, MutableComponent text) {
        tell(source, text, false);
    }

    @NotNull
    public static Component endl() {
        return Messenger.s("\n");
    }

    public static Component sline() {
        return Messenger.s("-------------------------------");
    }

    public static Component dline() {
        return Messenger.s("===============================");
    }

    @NotNull
    public static MutableComponent formatting(MutableComponent text, ChatFormatting... formattings) {
        text.withStyle(formattings);
        return text;
    }

    public static void sendServerMessage(MinecraftServer server, Component text) {
        Objects.requireNonNull(server, "Server is null, message not delivered !");
        MessengerCompatFactory.sendSystemMessage(server, text);
        MinecraftServerUtil.getOnlinePlayers().forEach(player -> MessengerCompatFactory.sendSystemMessage(player, text));
    }

    @NotNull
    public static Style simpleCmdButtonStyle(String command, Component hoverText, ChatFormatting... hoverTextFormattings) {
        return
            emptyStyle()
            .withClickEvent(ClickEventUtil.event(ClickEventUtil.RUN_COMMAND, command))
            .withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, Messenger.s(hoverText.getString()).withStyle(hoverTextFormattings)));
    }

    @NotNull
    public static Style simpleCopyButtonStyle(String text, Component hoverText, ChatFormatting... hoverTextFormattings) {
        return
            emptyStyle()
            .withClickEvent(ClickEventUtil.event(ClickEventUtil.COPY_TO_CLIPBOARD, text))
            .withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, Messenger.s(hoverText.getString()).withStyle(hoverTextFormattings)));
    }

    private static Style emptyStyle() {
        return Style.EMPTY;
    }
}
