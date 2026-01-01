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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkUtil {
    private static final Set<UUID> SUPPORT_CLIENT = ConcurrentHashMap.newKeySet();
    private static final AtomicBoolean SUPPORT_SERVER = new AtomicBoolean(false);

    public enum SendMode {
        FORCE,
        NEED_SUPPORT
    }

    public static void broadcastDataPack(AMS_CustomPayload payload, SendMode sendMode) {
        MinecraftServerUtil.getOnlinePlayers().forEach(player -> sendS2CPacket(player, payload, sendMode));
    }

    @SuppressWarnings("EnhancedSwitchMigration")
    public static void sendS2CPacket(ServerPlayerEntity player, AMS_CustomPayload payload, SendMode sendMode) {
        boolean shouldSend;
        switch (sendMode) {
            case FORCE:
                shouldSend = true;
                break;
            case NEED_SUPPORT:
                shouldSend = isSupportClient(player.getUuid());
                break;
            default:
                shouldSend = false;
                break;
        }

        if (shouldSend) {
            payload.sendS2CPacket(player);
        }
    }

    @SuppressWarnings("EnhancedSwitchMigration")
    public static void sendC2SPacket(ClientPlayerEntity player, AMS_CustomPayload payload, SendMode sendMode) {
        boolean shouldSend;
        switch (sendMode) {
            case FORCE:
                shouldSend = true;
                break;
            case NEED_SUPPORT:
                shouldSend = isSupportServer();
                break;
            default:
                shouldSend = false;
                break;
        }

        if (shouldSend) {
            payload.sendC2SPacket(player);
        }
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
        Optional.of(MinecraftClientUtil.clientIsRunning()).filter(Boolean::booleanValue).ifPresent(b -> MinecraftClientUtil.getCurrentClient().execute(runnable));
    }

    public static void executeOnServerThread(Runnable runnable) {
        Optional.of(MinecraftServerUtil.serverIsRunning()).filter(Boolean::booleanValue).ifPresent(b -> MinecraftServerUtil.getServer().execute(runnable));
    }

    // 兼容 Minecraft 1.16.5
    public static String readBufString(PacketByteBuf buf) {
        //#if MC<11700
        return buf.readString(Short.MAX_VALUE);
        //#else
        //$$ return buf.readString();
        //#endif
    }
}
