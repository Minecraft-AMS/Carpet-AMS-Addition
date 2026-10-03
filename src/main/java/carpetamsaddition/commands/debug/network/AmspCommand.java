/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.commands.debug.network;

import carpetamsaddition.CarpetAMSAdditionMod;
import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.Arguments;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.api.command.CommandPath;
import carpetamsaddition.network.payloads.debug.RequestClientModVersionPayload_S2C;
import carpetamsaddition.network.payloads.handshake.RequestHandShakeS2CPayload;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.*;
import carpetamsaddition.utils.messenger.Messenger;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AmspCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.amsp");
    public static final Map<UUID, String> clientModVersion = new ConcurrentHashMap<>();

    @Override
    public void define(CommandBuilder command) {
        command.name("amsp")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandAmspDebug)
            .route(CommandPath.path().literal("show").literal("supportClientList"))
            .executes(c -> showSupportClientList(c.source()))

            .route(CommandPath.path().literal("show").literal("serverSupportStatus"))
            .executes(c -> showServerSupportStatus(c.source()))

            .route(CommandPath.path().literal("show").literal("clientModVersion").argument(Arguments.player("player")))
            .executes(c -> showClientModVersion(c.source(), c.get(Arguments.player("player"))))

            .route(CommandPath.path().literal("show").literal("serverModVersion"))
            .executes(c -> showServerModVersion(c.source()))

            .route(CommandPath.path().literal("deny").literal("clientConnection").argument(Arguments.player("player")))
            .executes(c -> denyClientConnection(c.source(), c.get(Arguments.player("player"))))

            .route(CommandPath.path().literal("deny").literal("all"))
            .executes(c -> denyAllClientConnections(c.source()))

            .route(CommandPath.path().literal("set").literal("serverSupport").argument(Arguments.bool("boolean")))
            .executes(c -> setServerSupport(c.source(), c.get(Arguments.bool("boolean"))))

            .route(CommandPath.path().literal("request").literal("handshake"))
            .executes(c -> requestHandShake(c.source()))

            .route(CommandPath.path().literal("request").literal("handshake").argument(Arguments.players("players")))
            .executes(c -> requestHandShake(c.source(), c.get(Arguments.players("players"))));
    }

    private static int showSupportClientList(CommandSourceStack source) {
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
            MutableComponent text = Messenger.f(Messenger.s(strUuid + " - " + playerName), Layout.AQUA);
            Messenger.tell(source, text);
        }

        return 1;
    }

    private static int showServerSupportStatus(CommandSourceStack source) {
        Layout formatting = NetworkUtil.getServerSupportState() ? Layout.GREEN : Layout.RED;
        Messenger.tell(source, Messenger.f(tr.tr("server_support_status", String.valueOf(NetworkUtil.getServerSupportState())), formatting));
        return 1;
    }

    private static int showClientModVersion(CommandSourceStack source, ServerPlayer targetPlayer) {
        UUID playerUuid = targetPlayer.getUUID();
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
                    } else if (retryCount[0] < maxRetries) {
                        retryCount[0]++;
                        Messenger.tell(source, Messenger.f(tr.tr("request_client_version", String.valueOf(retryCount[0])), Layout.YELLOW));
                        NetworkUtil.sendS2CPacket(targetPlayer, RequestClientModVersionPayload_S2C.create(playerUuid), NetworkUtil.SendMode.NEED_SUPPORT);
                        CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(this);
                    } else {
                        Messenger.tell(source, Messenger.f(tr.tr("client_mod_version_failed_feedback", String.valueOf(maxRetries)), Layout.RED));
                    }
                });
            }
        };

        CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(checkAndRetry);
        Messenger.tell(source, Messenger.f(tr.tr("get_client_version_waiting"), Layout.GREEN));
        return 1;
    }

    private static int showServerModVersion(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(tr.tr("server_mod_version_feedback", CarpetAMSAdditionServer.fancyName, CarpetAMSAdditionMod.getVersion()), Layout.AQUA));
        return 1;
    }

    private static int denyClientConnection(CommandSourceStack source, ServerPlayer targetPlayer) {
        NetworkUtil.removeSupportClient(targetPlayer.getUUID());
        Messenger.tell(source, Messenger.f(tr.tr("deny_client_feedback", PlayerUtil.getName(targetPlayer.getUUID())), Layout.LIGHT_PURPLE));
        return 1;
    }

    private static int denyAllClientConnections(CommandSourceStack source) {
        NetworkUtil.clearClientSupport();
        Messenger.tell(source, Messenger.f(tr.tr("deny_all_client_feedback"), Layout.LIGHT_PURPLE));
        return 1;
    }

    private static int setServerSupport(CommandSourceStack source, Boolean support) {
        NetworkUtil.setServerSupport(support);
        Messenger.tell(source, Messenger.f(tr.tr("set_server_support_feedback", String.valueOf(NetworkUtil.getServerSupportState())), Layout.GREEN));
        return 1;
    }

    private static int requestHandShake(CommandSourceStack source) {
        return requestHandShake(source, MinecraftServerUtil.getOnlinePlayers());
    }

    private static int requestHandShake(CommandSourceStack source, Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            NetworkUtil.sendS2CPacket(player, RequestHandShakeS2CPayload.create(), NetworkUtil.SendMode.FORCE);
            Messenger.tell(source, Messenger.f(tr.tr("request_handshake_feedback", PlayerUtil.getName(player)), Layout.GREEN));
        }

        return 1;
    }
}
