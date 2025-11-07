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

package club.mcams.carpet.commands.rule.commandGetClientPlayerFps;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.FakePlayerHelper;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.NetworkUtil;
import club.mcams.carpet.utils.PlayerUtil;
import club.mcams.carpet.network.payloads.rule.commandGetClientPlayerFPS.ClientPlayerFpsPayload_S2C;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.util.Formatting;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GetClientPlayerFpsRegistry {
    private static final Translator tr = new Translator("command.getClientPlayerFps");
    private static final Map<UUID, ServerCommandSource> pendingQueries = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("getClientPlayerFps")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetClientPlayerFps))
            .then(CommandManager.argument("player", EntityArgumentType.player())
            .executes(ctx -> requestFps(EntityArgumentType.getPlayer(ctx, "player"), ctx.getSource())))
            .then(CommandManager.literal("help")
            .executes(ctx -> help(ctx.getSource())))
        );
    }

    private static int requestFps(ServerPlayerEntity targetPlayer, ServerCommandSource source) {
        pendingQueries.put(targetPlayer.getUuid(), source);
        NetworkUtil.sendS2CPacketIfSupport(targetPlayer, ClientPlayerFpsPayload_S2C.create(targetPlayer.getUuid()));
        return 1;
    }

    public static void sendFpsResult(UUID playerUuid, int fps) {
        ServerCommandSource source = pendingQueries.remove(playerUuid);
        if (source != null) {
            ServerPlayerEntity player = PlayerUtil.getServerPlayerEntity(playerUuid);
            if (!FakePlayerHelper.isFakePlayer(player) && player != null) {
                Messenger.tell(source, Messenger.formatting(tr.tr("feedback", PlayerUtil.getName(player), String.valueOf(fps)), Formatting.GREEN));
            }
        }
    }

    private static int help(ServerCommandSource source) {
        Messenger.tell(source, Messenger.formatting(tr.tr("help"), Formatting.GRAY));
        return 1;
    }
}
