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

package club.mcams.carpet.utils;

import club.mcams.carpet.network.AMS_CustomPayload;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkUtil {
    private static final Set<UUID> SUPPORT_CLIENT = ConcurrentHashMap.newKeySet();
    private static final AtomicBoolean SUPPORT_SERVER = new AtomicBoolean(false);

    public static void broadcastDataPack(MinecraftServer server, AMS_CustomPayload payload) {
        server.getPlayerManager().getPlayerList().forEach(player -> sendS2CPacketIfSupport(player, payload));
    }

    public static void forcedBroadcastDataPack(MinecraftServer server, AMS_CustomPayload payload) {
        server.getPlayerManager().getPlayerList().forEach(payload::sendS2CPacket);
    }

    public static void sendS2CPacketIfSupport(ServerPlayerEntity player, AMS_CustomPayload payload) {
        if (isSupportClient(player.getUuid())) {
            payload.sendS2CPacket(player);
        }
    }

    public static void sendC2SPacketIfSupport(ClientPlayerEntity player, AMS_CustomPayload payload) {
        if (isSupportServer()) {
            payload.sendC2SPacket(player);
        }
    }

    public static void sendS2CPacket(ServerPlayerEntity player, AMS_CustomPayload payload) {
        payload.sendS2CPacket(player);
    }

    public static void sendC2SPacket(ClientPlayerEntity player, AMS_CustomPayload payload) {
        payload.sendC2SPacket(player);
    }

    public static boolean isSupportClient(UUID uuid) {
        return SUPPORT_CLIENT.contains(uuid);
    }

    public static boolean isSupportServer() {
        return SUPPORT_SERVER.get();
    }

    public static void setServerSupport(boolean support) {
        SUPPORT_SERVER.set(support);
    }

    public static Boolean getServerSupport() {
        return SUPPORT_SERVER.get();
    }

    public static Set<UUID> getSupportClientSet() {
        return SUPPORT_CLIENT;
    }

    public static void addSupportClient(UUID uuid) {
        SUPPORT_CLIENT.add(uuid);
    }

    public static void removeSupportClient(UUID uuid) {
        SUPPORT_CLIENT.remove(uuid);
    }

    public static void clearClientSupport() {
        SUPPORT_CLIENT.clear();
    }

    public static void executeOnClientThread(Runnable runnable) {
        if (MinecraftClientUtil.clientIsRunning()) {
            MinecraftClientUtil.getCurrentClient().execute(runnable);
        }
    }

    public static void executeOnServerThread(Runnable runnable) {
        if (MinecraftServerUtil.serverIsRunning()) {
            MinecraftServerUtil.getServer().execute(runnable);
        }
    }
}
