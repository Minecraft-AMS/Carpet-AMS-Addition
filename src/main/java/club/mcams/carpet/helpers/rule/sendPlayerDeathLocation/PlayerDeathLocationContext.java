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
import club.mcams.carpet.carpetorgaddition.InvokeOrgCommand;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.MessageTextEventUtils.ClickEventUtil;
import club.mcams.carpet.utils.MessageTextEventUtils.HoverEventUtil;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.compat.DimensionWrapper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class PlayerDeathLocationContext {
    private static final Translator translator = new Translator("rule.sendPlayerDeathLocation");

    public static void sendMessage(MinecraftServer server, ServerPlayerEntity player, World world) {
        final Text copyButton = copyButton(player);
        String message = formatMessage(player, world);
        Messenger.sendServerMessage(
            server, Messenger.s(message).formatted(Formatting.RED)
            .append(copyButton)
            .append(InvokeOrgCommand.highlightPosButton(getPlayerPos(player).replace(",", "")))
        );
    }

    public static void realPlayerSendMessage(MinecraftServer server, ServerPlayerEntity player, World world) {
        if (!FakePlayerHelper.isFakePlayer(player)) {
            sendMessage(server, player, world);
        }
    }

    public static void fakePlayerSendMessage(MinecraftServer server, ServerPlayerEntity player, World world) {
        if (FakePlayerHelper.isFakePlayer(player)) {
            sendMessage(server, player, world);
        }
    }

    private static String getPlayerName(ServerPlayerEntity player) {
        return player.getName().getString();
    }

    private static DimensionWrapper getDimension(World world) {
        return DimensionWrapper.of(world);
    }

    private static String getPlayerPos(ServerPlayerEntity player) {
        return player.getBlockPos().getX() + ", " + player.getBlockPos().getY() + ", " + player.getBlockPos().getZ();
    }

    private static Text copyButton(ServerPlayerEntity player) {
        Text hoverText = Messenger.s(translator.tr("copy")).formatted(Formatting.YELLOW);
        String copyCoordText = getPlayerPos(player).replace(",", ""); // 1, 0, -24 -> 1 0 -24

        return
            Messenger.s(" [C]").setStyle(
            Style.EMPTY.withColor(Formatting.GREEN).withBold(true).
            withClickEvent(ClickEventUtil.event(ClickEventUtil.COPY_TO_CLIPBOARD, copyCoordText)).
            withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, hoverText))
        );
    }

    // Alex 死亡位置 @ minecraft:overworld -> [ 888, 20, 999 ]
    private static String formatMessage(ServerPlayerEntity player, World world) {
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
