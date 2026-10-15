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

package carpetamsaddition.utils;

import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkUtil {
    private static final Set<ServerGamePacketListenerImpl> SUPPORT_CLIENT = ConcurrentHashMap.newKeySet();
    private static final AtomicBoolean SUPPORT_SERVER = new AtomicBoolean(false);
    private static final Map<ServerGamePacketListenerImpl, Set<String>> CLIENT_SUPPORTED_PACKETS = new ConcurrentHashMap<>();
    private static final Set<String> SERVER_SUPPORTED_PACKETS = ConcurrentHashMap.newKeySet();

    public enum SendMode {
        FORCE,
        NEED_SUPPORT
    }

    public static void broadcastDataPack(AMS_CustomPayload payload, SendMode sendMode) {
        MinecraftServerUtil.getOnlinePlayers().forEach(player -> sendS2CPacket(player, payload, sendMode));
    }

    public static void sendS2CPacket(ServerPlayer player, AMS_CustomPayload payload, SendMode sendMode) {
        boolean shouldSend;
        switch (sendMode) {
            case FORCE:
                shouldSend = true;
                break;
            case NEED_SUPPORT:
                shouldSend = isSupportClient(player) && isClientPacketSupported(player, payload.getPacketId());
                break;
            default:
                shouldSend = false;
                break;
        }

        if (shouldSend) {
            payload.sendS2CPacket(player);
        }
    }

    public static void sendC2SPacket(LocalPlayer player, AMS_CustomPayload payload, SendMode sendMode) {
        boolean shouldSend;
        switch (sendMode) {
            case FORCE:
                shouldSend = true;
                break;
            case NEED_SUPPORT:
                shouldSend = getServerSupportState() && isServerPacketSupported(payload.getPacketId());
                break;
            default:
                shouldSend = false;
                break;
        }

        if (shouldSend) {
            payload.sendC2SPacket(player);
        }
    }

    public static boolean isClientPacketSupported(ServerPlayer player, String packetId) {
        Set<String> supportedPackets = CLIENT_SUPPORTED_PACKETS.get(player.connection);
        return supportedPackets != null && supportedPackets.contains(packetId);
    }

    public static boolean isServerPacketSupported(String packetId) {
        return SERVER_SUPPORTED_PACKETS.contains(packetId);
    }

    public static void setClientSupportedPackets(ServerPlayer player, Set<String> packetIds) {
        CLIENT_SUPPORTED_PACKETS.put(player.connection, new HashSet<>(packetIds));
    }

    public static Set<String> getClientSupportedPackets(ServerPlayer player) {
        return CLIENT_SUPPORTED_PACKETS.getOrDefault(player.connection, Collections.emptySet());
    }

    public static void setServerSupportedPackets(Set<String> packetIds) {
        SERVER_SUPPORTED_PACKETS.clear();
        SERVER_SUPPORTED_PACKETS.addAll(packetIds);
    }

    public static Set<String> getServerSupportedPackets() {
        return new HashSet<>(SERVER_SUPPORTED_PACKETS);
    }

    public static Set<String> getLocalSupportedPackets() {
        return new HashSet<>(AMS_PayloadManager.PAYLOAD_REGISTRY.keySet());
    }

    public static void removeClientSupportedPackets(ServerPlayer player) {
        CLIENT_SUPPORTED_PACKETS.remove(player.connection);
    }

    public static void clearAllClientSupportedPackets() {
        CLIENT_SUPPORTED_PACKETS.clear();
    }

    public static boolean isSupportClient(ServerPlayer player) {
        return SUPPORT_CLIENT.contains(player.connection);
    }

    public static void setServerSupport(boolean support) {
        SUPPORT_SERVER.set(support);
    }

    public static boolean getServerSupportState() {
        return SUPPORT_SERVER.get();
    }

    public static Iterator<ServerGamePacketListenerImpl> supportClientSetIterator() {
        return SUPPORT_CLIENT.iterator();
    }

    public static void addSupportClient(ServerPlayer player) {
        SUPPORT_CLIENT.add(player.connection);
    }

    public static void removeSupportClient(ServerPlayer player) {
        SUPPORT_CLIENT.remove(player.connection);
        removeClientSupportedPackets(player);
    }

    public static void clearClientSupport() {
        SUPPORT_CLIENT.clear();
        clearAllClientSupportedPackets();
    }

    public static void executeOnClientThread(Runnable runnable) {
        Optional.of(MinecraftClientUtil.clientIsRunning()).filter(Boolean::booleanValue).ifPresent(b -> MinecraftClientUtil.getCurrentClient().execute(runnable));
    }

    public static void executeOnServerThread(Runnable runnable) {
        Optional.of(MinecraftServerUtil.serverIsRunning()).filter(Boolean::booleanValue).ifPresent(b -> MinecraftServerUtil.getServer().execute(runnable));
    }

    public static void executeOnServerThread(ServerPlayer sender, Runnable runnable) {
        if (sender == null) {
            return;
        }

        MinecraftServer server = MinecraftServerUtil.getServer();
        ServerGamePacketListenerImpl connection = sender.connection;
        if (!MinecraftServerUtil.serverIsRunning(server)) {
            return;
        }

        server.execute(() -> {
            if (!MinecraftServerUtil.serverIsRunning(server)
                || sender.hasDisconnected() || sender.connection != connection
                || server.getPlayerList().getPlayers().stream().noneMatch(player -> player == sender)) {
                return;
            }

            //#if MC>=11904
            if (!connection.isAcceptingMessages()) {
                return;
            }
            //#else
            //$$ if (!connection.connection.isConnected()) {
            //$$     return;
            //$$ }
            //#endif

            runnable.run();
        });
    }

    public static String readBufString(FriendlyByteBuf buf) {
        //#if MC<11700
        //$$ return buf.readUtf(Short.MAX_VALUE);
        //#else
        return buf.readUtf();
        //#endif
    }
}
