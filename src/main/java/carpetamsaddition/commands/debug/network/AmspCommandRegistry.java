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

package carpetamsaddition.commands.debug.network;

import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.CarpetAMSAdditionMod;
import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.network.payloads.handshake.RequestHandShakeS2CPayload;
import carpetamsaddition.network.payloads.debug.RequestClientModVersionPayload_S2C;
import carpetamsaddition.utils.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;

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

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("amsp")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandAmspDebug))
            // show
            .then(Commands.literal("show")
            .then(Commands.literal("supportClientList")
            .executes(ctx -> showSupportClientList(ctx.getSource())))
            .then(Commands.literal("serverSupportStatus")
            .executes(ctx -> showServerSupportStatus(ctx.getSource())))
            .then(Commands.literal("clientModVersion")
            .then(Commands.argument("player", EntityArgument.player())
            .executes(ctx -> showClientModVersion(ctx.getSource(), EntityArgument.getPlayer(ctx, "player")))))
            .then(Commands.literal("serverModVersion")
            .executes(ctx -> showServerModVersion(ctx.getSource()))))

            // deny
            .then(Commands.literal("deny")
            .then(Commands.literal("clientConnection")
            .then(Commands.argument("player", EntityArgument.player())
            .executes(ctx -> denyClientConnection(ctx.getSource(), EntityArgument.getPlayer(ctx, "player")))))
            .then(Commands.literal("all")
            .executes(ctx -> denyAllClientConnections(ctx.getSource()))))

            // set
            .then(Commands.literal("set")
            .then(Commands.literal("serverSupport")
            .then(Commands.argument("boolean", BoolArgumentType.bool())
            .executes(ctx -> setServerSupport(ctx.getSource(), BoolArgumentType.getBool(ctx, "boolean"))))))

            // request
            .then(Commands.literal("request")
            .then(Commands.literal("handshake")
            .executes(ctx -> requestHandShake(ctx.getSource()))
            .then(Commands.argument("players", EntityArgument.players())
            .executes(ctx -> requestHandShake(ctx.getSource(), EntityArgument.getPlayers(ctx, "players"))))))
        );
    }

    private static int showSupportClientList(CommandSourceStack source) {
        Iterator<UUID> iterator = NetworkUtil.getSupportClientSet().iterator();

        if (!iterator.hasNext()) {
            Messenger.tell(source, Messenger.f(tr.tr("support_client_set_is_none"), ChatFormatting.YELLOW));
            return 0;
        }

        Messenger.tell(source, Messenger.f(tr.tr("support_client_list_title"), ChatFormatting.AQUA));

        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            String strUuid = uuid.toString();
            String playerName = PlayerUtil.getName(uuid);
            MutableComponent text = Messenger.f(Messenger.s(strUuid + " - " + playerName), ChatFormatting.AQUA);
            Messenger.tell(source, text);
        }

        return 1;
    }

    private static int showServerSupportStatus(CommandSourceStack source) {
        ChatFormatting formatting = NetworkUtil.getServerSupport() ? ChatFormatting.GREEN : ChatFormatting.RED;
        Messenger.tell(source, Messenger.f(tr.tr("server_support_status", String.valueOf(NetworkUtil.getServerSupport())), formatting));
        return 1;
    }

    private static int showClientModVersion(CommandSourceStack source, ServerPlayer targetPlayer) {
        UUID playerUuid = targetPlayer.getUUID();

        clientModVersion.clear();

        NetworkUtil.sendS2CPacketIfSupport(targetPlayer, RequestClientModVersionPayload_S2C.create(playerUuid));

        final int[] retryCount = {0};
        final int maxRetries = 5;

        Runnable checkAndRetry = new Runnable() {
            @Override
            public void run() {
                NetworkUtil.executeOnServerThread(() -> {
                    String version = clientModVersion.get(playerUuid);
                    if (version != null) {
                        Messenger.tell(source, Messenger.f(tr.tr("client_mod_version_success_feedback", PlayerUtil.getName(targetPlayer), version), ChatFormatting.AQUA));
                        clientModVersion.remove(playerUuid);
                    } else {
                        if (retryCount[0] < maxRetries) {
                            retryCount[0]++;
                            Messenger.tell(source, Messenger.f(tr.tr("request_client_version", String.valueOf(retryCount[0])), ChatFormatting.YELLOW));
                            NetworkUtil.sendS2CPacketIfSupport(targetPlayer, RequestClientModVersionPayload_S2C.create(playerUuid));
                            CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(this);
                        } else {
                            Messenger.tell(source, Messenger.f(tr.tr("client_mod_version_failed_feedback", String.valueOf(maxRetries)), ChatFormatting.RED));
                        }
                    }
                });
            }
        };

        CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(checkAndRetry);

        Messenger.tell(source, Messenger.f(tr.tr("get_client_version_waiting"), ChatFormatting.GREEN));
        return 1;
    }

    private static int showServerModVersion(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(tr.tr("server_mod_version_feedback", CarpetAMSAdditionServer.fancyName, CarpetAMSAdditionMod.getVersion()), ChatFormatting.AQUA));
        return 1;
    }

    private static int denyClientConnection(CommandSourceStack source, ServerPlayer targetPlayer) {
        NetworkUtil.removeSupportClient(targetPlayer.getUUID());
        Messenger.tell(source, Messenger.f(tr.tr("deny_client_feedback", PlayerUtil.getName(targetPlayer.getUUID())), ChatFormatting.LIGHT_PURPLE));
        return 1;
    }

    private static int denyAllClientConnections(CommandSourceStack source) {
        NetworkUtil.clearClientSupport();
        Messenger.tell(source, Messenger.f(tr.tr("deny_all_client_feedback"), ChatFormatting.LIGHT_PURPLE));
        return 1;
    }

    private static int setServerSupport(CommandSourceStack source, Boolean support) {
        NetworkUtil.setServerSupport(support);
        Messenger.tell(source, Messenger.f(tr.tr("set_server_support_feedback", String.valueOf(NetworkUtil.getServerSupport())), ChatFormatting.GREEN));
        return 1;
    }

    private static int requestHandShake(CommandSourceStack source) {
        return requestHandShake(source, MinecraftServerUtil.getOnlinePlayers());
    }

    private static int requestHandShake(CommandSourceStack source, Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            NetworkUtil.sendS2CPacket(player, RequestHandShakeS2CPayload.create());
            Messenger.tell(source, Messenger.f(tr.tr("request_handshake_feedback", PlayerUtil.getName(player)), ChatFormatting.GREEN));
        }

        return 1;
    }
}
