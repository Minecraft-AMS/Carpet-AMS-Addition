/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.helpers.rule.sendPlayerDeathLocation;

import club.mcams.carpet.helpers.FakePlayerHelper;
import club.mcams.carpet.fuzz.InvokeFuzzModCommand;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.MessageTextEventUtils.ClickEventUtil;
import club.mcams.carpet.utils.MessageTextEventUtils.HoverEventUtil;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.compat.DimensionWrapper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

public class PlayerDeathLocationContext {
    private static final Translator translator = new Translator("rule.sendPlayerDeathLocation");

    public static void sendMessage(MinecraftServer server, ServerPlayer player, Level world) {
        final Component copyButton = copyButton(player);
        String message = formatMessage(player, world);
        Messenger.sendServerMessage(
            server, Messenger.s(message).withStyle(ChatFormatting.RED)
            .append(copyButton)
            .append(InvokeFuzzModCommand.highlightCoordButton(getPlayerPos(player).replace(",", "")))
        );
    }

    public static void realPlayerSendMessage(MinecraftServer server, ServerPlayer player, Level world) {
        if (!FakePlayerHelper.isFakePlayer(player)) {
            sendMessage(server, player, world);
        }
    }

    public static void fakePlayerSendMessage(MinecraftServer server, ServerPlayer player, Level world) {
        if (FakePlayerHelper.isFakePlayer(player)) {
            sendMessage(server, player, world);
        }
    }

    private static String getPlayerName(ServerPlayer player) {
        return player.getName().getString();
    }

    private static DimensionWrapper getDimension(Level world) {
        return DimensionWrapper.of(world);
    }

    private static String getPlayerPos(ServerPlayer player) {
        return player.blockPosition().getX() + ", " + player.blockPosition().getY() + ", " + player.blockPosition().getZ();
    }

    private static Component copyButton(ServerPlayer player) {
        Component hoverText = Messenger.s(translator.tr("copy")).withStyle(ChatFormatting.YELLOW);
        String copyCoordText = getPlayerPos(player).replace(",", ""); // 1, 0, -24 -> 1 0 -24

        return
            Messenger.s(" [C]").setStyle(
            Style.EMPTY.withColor(ChatFormatting.GREEN).withBold(true).
            withClickEvent(ClickEventUtil.event(ClickEventUtil.COPY_TO_CLIPBOARD, copyCoordText)).
            withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, hoverText))
        );
    }

    // Alex 死亡位置 @ minecraft:overworld -> [ 888, 20, 999 ]
    private static String formatMessage(ServerPlayer player, Level world) {
        String playerName = getPlayerName(player);
        DimensionWrapper dimension = getDimension(world);
        return String.format(
            "%s %s @ %s -> [ %s ]",
            playerName,
            translator.tr("location").getString(),
            dimension,
            getPlayerPos(player)
        );
    }
}
