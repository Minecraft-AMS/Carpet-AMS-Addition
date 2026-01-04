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

package carpetamsaddition.helpers.rule.sendPlayerDeathLocation;

import carpetamsaddition.helpers.FakePlayerHelper;
import carpetamsaddition.fuzz.InvokeFuzzModCommand;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.compat.DimensionWrapper;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class PlayerDeathLocationContext {
    private static final Translator tr = new Translator("rule.sendPlayerDeathLocation");

    public static void sendMessage(MinecraftServer server, ServerPlayer player, Level world) {
        final MutableComponent copyButton = copyButton(player);
        String message = formatMessage(player, world);
        Messenger.sendServerMessage(server, Messenger.c(
            Messenger.f(Messenger.s(message), Layout.RED),
            copyButton,
            InvokeFuzzModCommand.highlightCoordButton(getPlayerPos(player).replace(",", ""))
        ));
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

    private static MutableComponent copyButton(ServerPlayer player) {
        MutableComponent hoverText = Messenger.f(tr.tr("copy"), Layout.YELLOW);
        String copyCoordText = getPlayerPos(player).replace(",", ""); // 1, 0, -24 -> 1 0 -24

        return Messenger.f(Messenger.s(" [C]").setStyle(Messenger.simpleCopyButtonStyle(copyCoordText, hoverText, Layout.YELLOW)), Layout.GREEN, Layout.BOLD);
    }

    // Alex 死亡位置 @ minecraft:overworld -> [ 888, 20, 999 ]
    private static String formatMessage(ServerPlayer player, Level world) {
        String playerName = getPlayerName(player);
        DimensionWrapper dimension = getDimension(world);
        return String.format(
            "%s %s @ %s -> [ %s ]",
            playerName,
            tr.tr("location").getString(),
            dimension,
            getPlayerPos(player)
        );
    }
}
