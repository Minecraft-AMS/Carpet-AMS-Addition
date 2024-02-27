/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package club.mcams.carpet.commands.rule.commandPing;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Colors;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.compat.LiteralTextUtil;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class pingCommandRegistry {
    private static final Translator translator = new Translator("command.ping");
    private static final String MSG_HEAD = "<commandPing>";
    private static final Map<PlayerEntity, Thread> PING_THREADS = new HashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("ping")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandPacketInternetGroper))
            .then(argument("targetIpOrDomainName", StringArgumentType.string())
            .then(argument("pingQuantity", IntegerArgumentType.integer())
            .executes(context -> executePing(
                context.getSource().getPlayer(),
                StringArgumentType.getString(context, "targetIpOrDomainName"),
                IntegerArgumentType.getInteger(context, "pingQuantity")
            ))))
            .then(literal("stop").executes(context -> stopPing(context.getSource().getPlayer())))
            .then(literal("help").executes(context -> help(context.getSource().getPlayer())))
        );
    }

    private static int executePing(PlayerEntity player, String targetIpOrDomainName, int pingQuantity) {
        Thread pingThread = new Thread(() -> {
            int successfulPings = 0;
            int failedPings = 0;
            long totalDelay = 0;
            try {
                boolean isFirstPing = true;
                for (int i = 0; i < pingQuantity; i++) {
                    long delay = ping(player, targetIpOrDomainName, isFirstPing);
                    if (delay >= 0) {
                        successfulPings++;
                        totalDelay += delay;
                    } else {
                        failedPings++;
                    }
                    isFirstPing = false;
                    Thread.sleep(1000);
                }
                sendFinishMessage(player, pingQuantity, successfulPings, failedPings, successfulPings > 0 ? totalDelay / successfulPings : 0);
            } catch (InterruptedException ignored) {}
        });
        pingThread.start();
        PING_THREADS.put(player, pingThread);
        return 1;
    }

    private static long ping(PlayerEntity player, String targetIpOrDomainName, boolean isFirstPing) {
        try {
            InetAddress inetAddress = InetAddress.getByName(targetIpOrDomainName);
            if (isFirstPing) {
                player.sendMessage(
                    LiteralTextUtil.compatText(
                        String.format(
                            "§b%s §ePing §b%s §e[ %s ] §e...",
                            MSG_HEAD, targetIpOrDomainName, inetAddress.getHostAddress()
                        )
                    ),
                    false
                );
            }
            long startTime = System.currentTimeMillis();
            boolean isReachable = inetAddress.isReachable(5000);
            long endTime = System.currentTimeMillis();
            if (isReachable) {
                long delayTime = endTime - startTime;
                player.sendMessage(
                    LiteralTextUtil.compatText(
                        String.format(
                            "§b%s §2Replay from §e[ %s ]§2 Time = %dms",
                            MSG_HEAD, inetAddress.getHostAddress(), delayTime
                        )
                    ),
                    false
                );
                return delayTime;
            } else {
                player.sendMessage(LiteralTextUtil.compatText(String.format("§b%s §4Request time out.", MSG_HEAD)), false);
                return -1;
            }
        } catch (IOException e) {
            AmsServer.LOGGER.error("[commandPing] An error occurred while performing ping operation");
            return -1;
        }
    }

    private static int stopPing(PlayerEntity player) {
        String stopPing = translator.tr("stop_ping").getString();
        String activePingIsNull = translator.tr("active_ping_is_null").getString();
        Thread pingThread = PING_THREADS.get(player);
        if (pingThread != null && pingThread.isAlive()) {
            pingThread.interrupt();
            player.sendMessage(LiteralTextUtil.compatText(String.format("§b%s §4%s", MSG_HEAD, stopPing)), false);
        } else {
            player.sendMessage(LiteralTextUtil.compatText(String.format("§b%s §4%s", MSG_HEAD, activePingIsNull)), false);
        }
        return 1;
    }

    private static int help(PlayerEntity player) {
        String pingHelp = translator.tr("help.ping").getString();
        String stopHelp = translator.tr("help.stop").getString();
        player.sendMessage(LiteralTextUtil.createColoredText(pingHelp + "\n" + stopHelp, Colors.GRAY), false);
        return 1;
    }

    private static void sendFinishMessage(PlayerEntity player, int totalPings, int successfulPings, int failedPings, long averageDelay) {
        player.sendMessage(
            LiteralTextUtil.compatText(
                String.format(
                    "§b%s §aSent = %d, Received = %d, Lost = %d, Average delay = %dms",
                    MSG_HEAD, totalPings, successfulPings, failedPings, averageDelay
                )
            ),
            false
        );
    }
}
