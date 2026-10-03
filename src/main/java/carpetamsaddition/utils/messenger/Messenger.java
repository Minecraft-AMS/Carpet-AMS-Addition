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

package carpetamsaddition.utils.messenger;

import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.messageTextEventUtils.ClickEventUtil;
import carpetamsaddition.utils.messageTextEventUtils.HoverEventUtil;
import carpetamsaddition.utils.MinecraftServerUtil;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Messenger {
    public static MutableComponent c(Object... fields) {
        return MessengerCompatFactory.carpetCompoundText(fields);
    }

    public static MutableComponent s(Object text) {
        return MessengerCompatFactory.literalText(text.toString());
    }

    @NotNull
    public static MutableComponent f(MutableComponent text, Layout... formattings) {
        ChatFormatting[] chatFormattings = new ChatFormatting[formattings.length];

        for (int i = 0; i < formattings.length; i++) {
            chatFormattings[i] = formattings[i].getFormatting();
        }

        text.withStyle(chatFormattings);
        return text;
    }

    public static MutableComponent tr(String key, Object... args) {
        return MessengerCompatFactory.translatableText(key, args);
    }

    @NotNull
    public static MutableComponent copy(MutableComponent text) {
        return MessengerCompatFactory.copy(text);
    }

    @NotNull
    public static MutableComponent style(MutableComponent text, Style style) {
        text.setStyle(style);
        return text;
    }

    private static void __tell(CommandSourceStack source, MutableComponent text, boolean broadcastToOps) {
        MessengerCompatFactory.sendFeedback(source, text, broadcastToOps);
    }

    public static void tell(CommandSourceStack source, MutableComponent text, boolean broadcastToOps) {
        __tell(source, text, broadcastToOps);
    }

    public static void tell(CommandSourceStack source, MutableComponent text) {
        tell(source, text, false);
    }

    public static void tell(ServerPlayer player, MutableComponent text, Boolean overlay) {
        MessengerCompatFactory.sendPlayerMessage(player, text, overlay);
    }

    public static void tell(ServerPlayer player, MutableComponent text) {
        MessengerCompatFactory.sendPlayerMessage(player, text, false);
    }

    public static void tell(Player player, MutableComponent text) {
        MessengerCompatFactory.sendPlayerMessage(player, text);
    }

    @NotNull
    public static MutableComponent endl() {
        return Messenger.s("\n");
    }

    public static MutableComponent sline() {
        return Messenger.s("-----------------------------------");
    }

    public static MutableComponent dline() {
        return Messenger.s("===================================");
    }

    public static void sendServerMessage(MinecraftServer server, MutableComponent text) {
        sendServerMessage(server, text, false);
    }

    public static void sendServerMessage(MinecraftServer server, MutableComponent text, boolean onlyToPlayer) {
        Objects.requireNonNull(server, "Server is null, message not delivered !");

        if (!onlyToPlayer) {
            MessengerCompatFactory.sendServerMessage(server, text);
        }

        MinecraftServerUtil.getOnlinePlayers().forEach(player -> tell(player, text));
    }

    @NotNull
    public static Style simpleCmdButtonStyle(String command, MutableComponent hoverText, Layout... hoverTextFormattings) {
        return emptyStyle()
            .withClickEvent(ClickEventUtil.event(ClickEventUtil.RUN_COMMAND, command))
            .withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, f(hoverText, hoverTextFormattings)));
    }

    @NotNull
    public static Style simpleCopyButtonStyle(String copyText, MutableComponent hoverText, Layout... hoverTextFormattings) {
        return emptyStyle()
            .withClickEvent(ClickEventUtil.event(ClickEventUtil.COPY_TO_CLIPBOARD, copyText))
            .withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, f(hoverText, hoverTextFormattings)));
    }

    private static Style emptyStyle() {
        return Style.EMPTY;
    }
}
