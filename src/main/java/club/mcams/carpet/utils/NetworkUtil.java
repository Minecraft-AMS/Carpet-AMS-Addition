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

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.network.AMS_CustomPayload;
import club.mcams.carpet.network.AMS_PayloadManager;
import club.mcams.carpet.settings.AmsRuleCategory;
import club.mcams.carpet.settings.Rule;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkUtil {
    private static final Set<UUID> SUPPORT_CLIENT = ConcurrentHashMap.newKeySet();
    private static final AtomicBoolean SUPPORT_SERVER = new AtomicBoolean(false);
    public static final Set<String> AMS_NETWORK_RULE_NAMES = new LinkedHashSet<>();
    private static final Map<UUID, Set<String>> CLIENT_SUPPORTED_PACKETS = new ConcurrentHashMap<>();
    private static final Set<String> SERVER_SUPPORTED_PACKETS = ConcurrentHashMap.newKeySet();

    public enum SendMode {
        FORCE,
        NEED_SUPPORT
    }

    public static void broadcastDataPack(AMS_CustomPayload payload, SendMode sendMode) {
        MinecraftServerUtil.getOnlinePlayers().forEach(player -> sendS2CPacket(player, payload, sendMode));
    }

    public static void sendS2CPacket(ServerPlayerEntity player, AMS_CustomPayload payload, SendMode sendMode) {
        boolean shouldSend;
        switch (sendMode) {
            case FORCE:
                shouldSend = true;
                break;
            case NEED_SUPPORT:
                shouldSend = isSupportClient(player.getUuid()) && isClientPacketSupported(player.getUuid(), payload.getPacketId());
                break;
            default:
                shouldSend = false;
                break;
        }

        if (shouldSend) {
            payload.sendS2CPacket(player);
        }
    }

    public static void sendC2SPacket(ClientPlayerEntity player, AMS_CustomPayload payload, SendMode sendMode) {
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

    public static boolean isClientPacketSupported(UUID clientUuid, String packetId) {
        Set<String> supportedPackets = CLIENT_SUPPORTED_PACKETS.get(clientUuid);
        return supportedPackets != null && supportedPackets.contains(packetId);
    }

    public static boolean isServerPacketSupported(String packetId) {
        return SERVER_SUPPORTED_PACKETS.contains(packetId);
    }

    public static void setClientSupportedPackets(UUID clientUuid, Set<String> packetIds) {
        CLIENT_SUPPORTED_PACKETS.put(clientUuid, new HashSet<>(packetIds));
    }

    public static Set<String> getClientSupportedPackets(UUID clientUuid) {
        return CLIENT_SUPPORTED_PACKETS.getOrDefault(clientUuid, Collections.emptySet());
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

    public static void removeClientSupportedPackets(UUID clientUuid) {
        CLIENT_SUPPORTED_PACKETS.remove(clientUuid);
    }

    public static void clearAllClientSupportedPackets() {
        CLIENT_SUPPORTED_PACKETS.clear();
    }

    public static boolean isSupportClient(UUID uuid) {
        return SUPPORT_CLIENT.contains(uuid);
    }

    public static void setServerSupport(boolean support) {
        SUPPORT_SERVER.set(support);
    }

    public static boolean getServerSupportState() {
        return SUPPORT_SERVER.get();
    }

    public static Iterator<UUID> supportClientSetIterator() {
        return SUPPORT_CLIENT.iterator();
    }

    public static void addSupportClient(UUID uuid) {
        SUPPORT_CLIENT.add(uuid);
    }

    public static void removeSupportClient(UUID uuid) {
        SUPPORT_CLIENT.remove(uuid);
        removeClientSupportedPackets(uuid);
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

    // 兼容 Minecraft 1.16.5
    public static String readBufString(PacketByteBuf buf) {
        //#if MC<11700
        return buf.readString(Short.MAX_VALUE);
        //#else
        //$$ return buf.readString();
        //#endif
    }

    public static void collectAmsNetworkRuleNames() {
        for (Field field : AmsServerSettings.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Rule.class)) {
                Rule ruleAnnotation = field.getAnnotation(Rule.class);
                for (String category : ruleAnnotation.categories()) {
                    if (category.equals(AmsRuleCategory.AMS_NETWORK)) {
                        AMS_NETWORK_RULE_NAMES.add(field.getName());
                        break;
                    }
                }
            }
        }
    }
}
