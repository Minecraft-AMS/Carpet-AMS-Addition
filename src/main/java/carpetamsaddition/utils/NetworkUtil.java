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

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.messenger.Messenger;

import net.minecraft.client.player.LocalPlayer;
//#if MC>=12005
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
//#else
//$$ import carpetamsaddition.mixin.network.CustomPayloadC2SPacketAccessor;
//$$ import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
//$$ import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
//#endif
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkUtil {
    private static final Translator tr = new Translator("network");
    private static final Set<ServerGamePacketListenerImpl> SUPPORT_CLIENT = ConcurrentHashMap.newKeySet();
    private static final AtomicBoolean SUPPORT_SERVER = new AtomicBoolean(false);
    private static final Map<ServerGamePacketListenerImpl, Set<String>> CLIENT_SUPPORTED_PACKETS = new ConcurrentHashMap<>();
    private static final Set<String> SERVER_SUPPORTED_PACKETS = ConcurrentHashMap.newKeySet();
    private static final Set<ServerGamePacketListenerImpl> PROTOCOL_WARNED_CONNECTIONS = ConcurrentHashMap.newKeySet();

    public enum SendMode {
        FORCE,
        NEED_SUPPORT
    }

    // S2C
    public static Identifier getChannelId(ClientboundCustomPayloadPacket packet) {
        //#if MC>=12005
        return packet.payload().type().id();
        //#else
        //$$ return packet.getIdentifier();
        //#endif
    }

    // C2S
    public static Identifier getChannelId(ServerboundCustomPayloadPacket packet) {
        //#if MC>=12005
        return packet.payload().type().id();
        //#else
        //$$ return ((CustomPayloadC2SPacketAccessor) packet).getIdentifier();
        //#endif
    }

    // S2C
    public static AMS_CustomPayload getAmsPayload(ClientboundCustomPayloadPacket packet) {
        //#if MC>=12005
        CustomPacketPayload payload = packet.payload();
        return payload instanceof AMS_CustomPayload ? (AMS_CustomPayload) payload : null;
        //#else
        //$$ return AMS_PayloadManager.decodeS2CPacket(packet);
        //#endif
    }

    // C2S
    public static AMS_CustomPayload getAmsPayload(ServerboundCustomPayloadPacket packet) {
        //#if MC>=12005
        CustomPacketPayload payload = packet.payload();
        return payload instanceof AMS_CustomPayload ? (AMS_CustomPayload) payload : null;
        //#else
        //$$ return AMS_PayloadManager.decodeC2SPacket(packet);
        //#endif
    }

    // S2C
    @GameVersion(version = "Minecraft < 1.20.5", desc = "packet.getData() 返回 FriendlyByteBuf，release() 释放当前持有的引用，避免缓冲区资源泄漏")
    public static void releasePayload(@SuppressWarnings("unused") ClientboundCustomPayloadPacket packet) {
        //#if MC<12005
        //$$ packet.getData().release();
        //#endif
    }

    public static void broadcastDataPack(AMS_CustomPayload payload, SendMode sendMode) {
        MinecraftServerUtil.getOnlinePlayers().forEach(player -> sendS2CPacket(player, payload, sendMode));
    }

    @SuppressWarnings("EnhancedSwitchMigration")
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

    @SuppressWarnings("EnhancedSwitchMigration")
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

    public static void clearProtocolWarning(ServerPlayer player) {
        PROTOCOL_WARNED_CONNECTIONS.remove(player.connection);
    }

    public static void clearProtocolWarnings() {
        PROTOCOL_WARNED_CONNECTIONS.clear();
    }

    public static void sendIncompatibleProtocolWarn(Identifier channelId, ServerPlayer player) {
        EntityUtil.getEntityServer(player).execute(() -> {
            if (CarpetAMSAdditionSettings.incompatibleNetworkProtocolWarningDisabled) {
                return;
            }

            if (player.hasDisconnected() || !PROTOCOL_WARNED_CONNECTIONS.add(player.connection)) {
                return;
            }

            Messenger.tell(player, Messenger.c(
                Messenger.f(tr.tr("incompatible_protocol", channelId.toString(), AMS_CustomPayload.CHANNEL_ID_STRING), Layout.RED),
                Messenger.endl(),
                Messenger.f(tr.tr("incompatible_protocol_hint"), Layout.YELLOW)
            ));
        });
    }
}
