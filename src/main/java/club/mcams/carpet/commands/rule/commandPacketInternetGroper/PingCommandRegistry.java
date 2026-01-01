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

package club.mcams.carpet.commands.rule.commandPacketInternetGroper;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PingCommandRegistry {
    private static final Translator tr = new Translator("command.ping");
    private static final String MSG_HEAD = "<commandPacketInternetGroper>";
    private static final Map<ServerCommandSource, PingThread> PING_THREADS = new HashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("pings")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandPacketInternetGroper))
            .then(argument("targetIpOrDomainName", StringArgumentType.string())
            .then(argument("pingQuantity", IntegerArgumentType.integer())
            .executes(context -> executePing(
                context.getSource(),
                StringArgumentType.getString(context, "targetIpOrDomainName"),
                IntegerArgumentType.getInteger(context, "pingQuantity")
            ))))
            .then(literal("stop").executes(context -> stopPing(context.getSource())))
            .then(literal("help").executes(context -> help(context.getSource())))
        );
    }

    private static int executePing(ServerCommandSource source, String targetIpOrDomainName, int pingQuantity) {
        PingThread thread = PING_THREADS.get(source);

        if (thread != null) {
            thread.setInterrupted();
        }

        PingThread pingThread = new PingThread(pingQuantity, source, targetIpOrDomainName);
        pingThread.start();
        PING_THREADS.put(source, pingThread);
        return 1;
    }

    private static long ping(ServerCommandSource source, String targetIpOrDomainName, boolean isFirstPing) {
        try {
            InetAddress inetAddress = InetAddress.getByName(targetIpOrDomainName);
            if (isFirstPing) {
                Messenger.tell(
                    source, Messenger.s(
                        String.format(
                            "§b%s §ePing §b%s §e[ %s ] §e...",
                            MSG_HEAD, targetIpOrDomainName, inetAddress.getHostAddress()
                        )
                    )
                );
            }

            long startTime = System.currentTimeMillis();
            boolean isReachable = inetAddress.isReachable(5000);
            long endTime = System.currentTimeMillis();

            if (isReachable) {
                long delayTime = endTime - startTime;
                Messenger.tell(
                    source, Messenger.s(
                        String.format(
                            "§b%s §2Replay from §e[ %s ]§2 Time = %dms",
                            MSG_HEAD, inetAddress.getHostAddress(), delayTime
                        )
                    )
                );

                return delayTime;
            } else {
                Messenger.tell(source, Messenger.s(String.format("§b%s §4Request time out.", MSG_HEAD)));
                return 0;
            }
        } catch (IOException e) {
            AmsServer.LOGGER.error("[commandPacketInternetGroper] An error occurred while performing ping operation");
            return 0;
        }
    }

    private static int stopPing(ServerCommandSource source) {
        String stopPing = tr.tr("stop_ping").getString();
        String activePingIsNull = tr.tr("active_ping_is_null").getString();
        PingThread pingThread = PING_THREADS.get(source);
        if (pingThread != null) {
            pingThread.setInterrupted();
            Messenger.tell(source, Messenger.s(String.format("§b%s §4%s", MSG_HEAD, stopPing)));
        } else {
            Messenger.tell(source, Messenger.s(String.format("§b%s §4%s", MSG_HEAD, activePingIsNull)));
        }
        return 1;
    }

    private static int help(ServerCommandSource source) {
        MutableText pingHelp = tr.tr("help.ping");
        MutableText stopHelp = tr.tr("help.stop");
        Messenger.tell(source, Messenger.f(Messenger.c(pingHelp, Messenger.endl(), stopHelp), Layout.GRAY));
        return 1;
    }

    private static void sendFinishMessage(ServerCommandSource source, int totalPings, int successfulPings, int failedPings, long averageDelay) {
        Messenger.tell(
            source, Messenger.s(
                String.format(
                    "§b%s §aSent = %d, Received = %d, Lost = %d, Average delay = %dms",
                    MSG_HEAD, totalPings, successfulPings, failedPings, averageDelay
                )
            )
        );
    }

    private static class PingThread extends Thread {
        private volatile boolean interrupted = false;
        private final int pingQuantity;
        private final ServerCommandSource source;
        private final String targetIpOrDomainName;

        private PingThread(int pingQuantity, ServerCommandSource source, String targetIpOrDomainName) {
            this.pingQuantity = pingQuantity;
            this.source = source;
            this.targetIpOrDomainName = targetIpOrDomainName;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            int successfulPings = 0;
            int failedPings = 0;
            long totalDelay = 0;

            try {
                boolean isFirstPing = true;
                for (int i = 0; i < pingQuantity; i++) {
                    if (interrupted){
                        return;
                    }

                    long delay = ping(source, targetIpOrDomainName, isFirstPing);

                    if (delay >= 0) {
                        successfulPings++;
                        totalDelay += delay;
                    } else {
                        failedPings++;
                    }

                    isFirstPing = false;
                    Thread.sleep(1000);
                }

                sendFinishMessage(source, pingQuantity, successfulPings, failedPings, successfulPings > 0 ? totalDelay / successfulPings : 0);
            } catch (InterruptedException ignored) {
            } finally {
                PING_THREADS.remove(source);
            }
        }

        private void setInterrupted(){
            this.interrupted = true;
        }
    }
}
