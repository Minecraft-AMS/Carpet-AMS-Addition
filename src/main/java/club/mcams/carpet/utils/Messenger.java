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

import club.mcams.carpet.utils.MessageTextEventUtils.ClickEventUtil;
import club.mcams.carpet.utils.MessageTextEventUtils.HoverEventUtil;
import club.mcams.carpet.utils.compat.MessengerCompatFactory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.text.*;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Messenger {
    public static BaseText c(Object... fields) {
        return MessengerCompatFactory.CarpetCompoundText(fields);
    }

    public static BaseText s(Object text) {
        return MessengerCompatFactory.LiteralText(text.toString());
    }

    @NotNull
    public static BaseText f(BaseText text, Layout... formattings) {
        Formatting[] chatFormattings = new Formatting[formattings.length];

        for (int i = 0; i < formattings.length; i++) {
            chatFormattings[i] = formattings[i].getFormatting();
        }

        return (BaseText) text.formatted(chatFormattings);
    }

    public static BaseText tr(String key, Object... args) {
        return MessengerCompatFactory.TranslatableText(key, args);
    }

    @NotNull
    public static BaseText copy(BaseText text) {
        return (BaseText) text.shallowCopy();
    }

    private static void __tell(ServerCommandSource source, BaseText text, boolean broadcastToOps) {
        MessengerCompatFactory.sendFeedBack(source, text, broadcastToOps);
    }

    public static void tell(ServerCommandSource source, BaseText text, boolean broadcastToOps) {
        __tell(source, text, broadcastToOps);
    }

    public static void tell(ServerCommandSource source, BaseText text) {
        tell(source, text, false);
    }

    public static void tell(PlayerEntity player, BaseText text, Boolean overlay) {
        player.sendMessage(text, overlay);
    }

    public static void tell(ServerPlayerEntity player, BaseText text, Boolean overlay) {
        player.sendMessage(text, overlay);
    }

    @NotNull
    public static BaseText endl() {
        return Messenger.s("\n");
    }

    public static BaseText sline() {
        return Messenger.s("-----------------------------------");
    }

    public static BaseText dline() {
        return Messenger.s("===================================");
    }

    public static void sendServerMessage(MinecraftServer server, BaseText text) {
        Objects.requireNonNull(server, "Server is null, message not delivered !");
        MessengerCompatFactory.sendSystemMessage(server, text);
        MinecraftServerUtil.getOnlinePlayers().forEach(player -> tell(player, text, false));
    }

    @NotNull
    public static Style simpleCmdButtonStyle(String command, BaseText hoverText, Layout... hoverTextFormattings) {
        return emptyStyle()
            .withClickEvent(ClickEventUtil.event(ClickEventUtil.RUN_COMMAND, command))
            .withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, f(s(hoverText.getString()), hoverTextFormattings)));
    }

    @NotNull
    public static Style simpleCopyButtonStyle(String copyText, BaseText hoverText, Layout... hoverTextFormattings) {
        return emptyStyle()
            .withClickEvent(ClickEventUtil.event(ClickEventUtil.COPY_TO_CLIPBOARD, copyText))
            .withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, f(s(hoverText.getString()), hoverTextFormattings)));
    }

    private static Style emptyStyle() {
        return Style.EMPTY;
    }
}
