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

package club.mcams.carpet.commands.debug.network;

import club.mcams.carpet.utils.*;
import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerMod;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.network.payloads.handshake.RequestHandShakeS2CPayload;
import club.mcams.carpet.network.payloads.debug.RequestClientModVersionPayload_S2C;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.BaseText;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AmspCommandRegistry {
    private static final Translator tr = new Translator("command.amsp");
    public static final Map<UUID, String> clientModVersion = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("amsp")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandAmspDebug))
            // show
            .then(CommandManager.literal("show")
            .then(CommandManager.literal("supportClientList")
            .executes(ctx -> showSupportClientList(ctx.getSource())))
            .then(CommandManager.literal("serverSupportStatus")
            .executes(ctx -> showServerSupportStatus(ctx.getSource())))
            .then(CommandManager.literal("clientModVersion")
            .then(CommandManager.argument("player", EntityArgumentType.player())
            .executes(ctx -> showClientModVersion(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player")))))
            .then(CommandManager.literal("serverModVersion")
            .executes(ctx -> showServerModVersion(ctx.getSource()))))

            // deny
            .then(CommandManager.literal("deny")
            .then(CommandManager.literal("clientConnection")
            .then(CommandManager.argument("player", EntityArgumentType.player())
            .executes(ctx -> denyClientConnection(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player")))))
            .then(CommandManager.literal("all")
            .executes(ctx -> denyAllClientConnections(ctx.getSource()))))

            // set
            .then(CommandManager.literal("set")
            .then(CommandManager.literal("serverSupport")
            .then(CommandManager.argument("boolean", BoolArgumentType.bool())
            .executes(ctx -> setServerSupport(ctx.getSource(), BoolArgumentType.getBool(ctx, "boolean"))))))

            // request
            .then(CommandManager.literal("request")
            .then(CommandManager.literal("handshake")
            .executes(ctx -> requestHandShake(ctx.getSource()))
            .then(CommandManager.argument("players", EntityArgumentType.players())
            .executes(ctx -> requestHandShake(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "players"))))))
        );
    }

    private static int showSupportClientList(ServerCommandSource source) {
        Iterator<UUID> iterator = NetworkUtil.supportClientSetIterator();

        if (!iterator.hasNext()) {
            Messenger.tell(source, Messenger.f(tr.tr("support_client_set_is_none"), Layout.YELLOW));
            return 0;
        }

        Messenger.tell(source, Messenger.f(tr.tr("support_client_list_title"), Layout.AQUA));

        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            String strUuid = uuid.toString();
            String playerName = PlayerUtil.getName(uuid);
            BaseText text = Messenger.f(Messenger.s(strUuid + " - " + playerName), Layout.AQUA);
            Messenger.tell(source, text);
        }

        return 1;
    }

    private static int showServerSupportStatus(ServerCommandSource source) {
        Layout formatting = NetworkUtil.getServerSupport() ? Layout.GREEN : Layout.RED;
        Messenger.tell(source, Messenger.f(tr.tr("server_support_status", String.valueOf(NetworkUtil.getServerSupport())), formatting));
        return 1;
    }

    private static int showClientModVersion(ServerCommandSource source, ServerPlayerEntity targetPlayer) {
        UUID playerUuid = targetPlayer.getUuid();

        clientModVersion.clear();

        NetworkUtil.sendS2CPacket(targetPlayer, RequestClientModVersionPayload_S2C.create(playerUuid), NetworkUtil.SendMode.NEED_SUPPORT);

        final int[] retryCount = {0};
        final int maxRetries = 5;

        Runnable checkAndRetry = new Runnable() {
            @Override
            public void run() {
                NetworkUtil.executeOnServerThread(() -> {
                    String version = clientModVersion.get(playerUuid);
                    if (version != null) {
                        Messenger.tell(source, Messenger.f(tr.tr("client_mod_version_success_feedback", PlayerUtil.getName(targetPlayer), version), Layout.AQUA));
                        clientModVersion.remove(playerUuid);
                    } else {
                        if (retryCount[0] < maxRetries) {
                            retryCount[0]++;
                            Messenger.tell(source, Messenger.f(tr.tr("request_client_version", String.valueOf(retryCount[0])), Layout.YELLOW));
                            NetworkUtil.sendS2CPacket(targetPlayer, RequestClientModVersionPayload_S2C.create(playerUuid), NetworkUtil.SendMode.NEED_SUPPORT);
                            CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(this);
                        } else {
                            Messenger.tell(source, Messenger.f(tr.tr("client_mod_version_failed_feedback", String.valueOf(maxRetries)), Layout.RED));
                        }
                    }
                });
            }
        };

        CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(checkAndRetry);

        Messenger.tell(source, Messenger.f(tr.tr("get_client_version_waiting"), Layout.GREEN));
        return 1;
    }

    private static int showServerModVersion(ServerCommandSource source) {
        Messenger.tell(source, Messenger.f(tr.tr("server_mod_version_feedback", AmsServer.fancyName, AmsServerMod.getVersion()), Layout.AQUA));
        return 1;
    }

    private static int denyClientConnection(ServerCommandSource source, ServerPlayerEntity targetPlayer) {
        NetworkUtil.removeSupportClient(targetPlayer.getUuid());
        Messenger.tell(source, Messenger.f(tr.tr("deny_client_feedback", PlayerUtil.getName(targetPlayer.getUuid())), Layout.LIGHT_PURPLE));
        return 1;
    }

    private static int denyAllClientConnections(ServerCommandSource source) {
        NetworkUtil.clearClientSupport();
        Messenger.tell(source, Messenger.f(tr.tr("deny_all_client_feedback"), Layout.LIGHT_PURPLE));
        return 1;
    }

    private static int setServerSupport(ServerCommandSource source, Boolean support) {
        NetworkUtil.setServerSupport(support);
        Messenger.tell(source, Messenger.f(tr.tr("set_server_support_feedback", String.valueOf(NetworkUtil.getServerSupport())), Layout.GREEN));
        return 1;
    }

    private static int requestHandShake(ServerCommandSource source) {
        return requestHandShake(source, MinecraftServerUtil.getOnlinePlayers());
    }

    private static int requestHandShake(ServerCommandSource source, Collection<ServerPlayerEntity> players) {
        for (ServerPlayerEntity player : players) {
            NetworkUtil.sendS2CPacket(player, RequestHandShakeS2CPayload.create(), NetworkUtil.SendMode.FORCE);
            Messenger.tell(source, Messenger.f(tr.tr("request_handshake_feedback", PlayerUtil.getName(player)), Layout.GREEN));
        }

        return 1;
    }
}
