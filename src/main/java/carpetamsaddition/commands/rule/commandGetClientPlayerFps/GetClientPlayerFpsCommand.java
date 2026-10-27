/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

package carpetamsaddition.commands.rule.commandGetClientPlayerFps;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.Arguments;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.*;
import carpetamsaddition.network.payloads.rule.commandGetClientPlayerFPS.ClientPlayerFpsPayload_S2C;

import carpetamsaddition.utils.messenger.Messenger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GetClientPlayerFpsCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.getClientPlayerFps");
    private static final Map<ServerGamePacketListenerImpl, CommandSourceStack> pendingQueries = new ConcurrentHashMap<>();

    @Override
    public void define(CommandBuilder command) {
        command.name("getClientPlayerFps")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandGetClientPlayerFps)
            .route(Arguments.player("player")).executes(c -> requestFps(c.get(Arguments.player("player")), c.source()))
            .route("help").executes(c -> help(c.source()));
    }

    private static int requestFps(ServerPlayer targetPlayer, CommandSourceStack source) {
        pendingQueries.put(targetPlayer.connection, source);
        NetworkUtil.sendS2CPacket(targetPlayer, ClientPlayerFpsPayload_S2C.create(), NetworkUtil.SendMode.NEED_SUPPORT);
        return 1;
    }

    public static void sendFpsResult(ServerPlayer player, int fps) {
        CommandSourceStack source = pendingQueries.remove(player.connection);
        if (source != null && !PlayerUtil.isFakePlayer(player)) {
            Messenger.tell(source, Messenger.f(tr.tr("feedback", PlayerUtil.getName(player), String.valueOf(fps)), Layout.GREEN));
        }
    }

    public static void clearPlayerQueries(ServerPlayer player) {
        pendingQueries.entrySet().removeIf(
            entry -> entry.getKey() == player.connection ||
            entry.getValue().getEntity() instanceof ServerPlayer && ((ServerPlayer) entry.getValue().getEntity()).connection == player.connection
        );
    }

    public static void clearQueries() {
        pendingQueries.clear();
    }

    private static int help(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(tr.tr("help"), Layout.GRAY));
        return 1;
    }
}
