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

package carpetamsaddition.network;

import carpetamsaddition.network.payloads.AMS_UnknownPayload;
import carpetamsaddition.network.payloads.core.LazySettingsPayload_S2C;
import carpetamsaddition.network.payloads.debug.RequestClientModVersionPayload_C2S;
import carpetamsaddition.network.payloads.debug.RequestClientModVersionPayload_S2C;
import carpetamsaddition.network.payloads.handshake.HandShakeC2SPayload;
import carpetamsaddition.network.payloads.handshake.HandShakeS2CPayload;
import carpetamsaddition.network.payloads.handshake.RequestHandShakeS2CPayload;
import carpetamsaddition.network.payloads.rule.commandCustomBlockHardness.CustomBlockHardnessPayload_S2C;
import carpetamsaddition.network.payloads.rule.blockHardnessSyncProtocol.BlockHardnessSyncPayload_C2S;
import carpetamsaddition.network.payloads.rule.blockHardnessSyncProtocol.BlockHardnessSyncPayload_S2C;
import carpetamsaddition.network.payloads.rule.commandGetClientPlayerFPS.ClientPlayerFpsPayload_C2S;
import carpetamsaddition.network.payloads.rule.commandGetClientPlayerFPS.ClientPlayerFpsPayload_S2C;
import carpetamsaddition.network.payloads.rule.commandSetPlayerPose.UpdatePlayerPosePayload_S2C;

import net.minecraft.network.FriendlyByteBuf;
//#if MC<12005
//$$ import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
//$$ import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
//#endif

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.concurrent.ConcurrentHashMap;

public class AMS_PayloadManager {
    public static final Map<String, Function<FriendlyByteBuf, AMS_CustomPayload>> PAYLOAD_REGISTRY = new ConcurrentHashMap<>();
    private static final PayloadDispatcher C2S_DISPATCHER = PayloadDispatcherCreator.createC2SDispatcher();
    private static final PayloadDispatcher S2C_DISPATCHER = PayloadDispatcherCreator.createS2CDispatcher();

    public enum PacketId {
        UNKNOWN("unknown"),
        HANDSHAKE_C2S("handshake_c2s"),
        HANDSHAKE_S2C("handshake_s2c"),
        REQUEST_CLIENT_MOD_VERSION_S2C("request_client_mod_version_s2c"),
        REQUEST_CLIENT_MOD_VERSION_C2S("request_client_mod_version_c2s"),
        REQUEST_HANDSHAKE_S2C("request_handshake_s2c"),
        SYNC_CUSTOM_BLOCK_HARDNESS("sync_custom_block_hardness"),
        BLOCK_HARDNESS_SYNC_PROTOCOL_C2S("block_hardness_sync_protocol_c2s"),
        BLOCK_HARDNESS_SYNC_PROTOCOL_S2C("block_hardness_sync_protocol_s2c"),
        CLIENT_PLAYER_FPS_C2S("client_player_fps_c2s"),
        CLIENT_PLAYER_FPS_S2C("client_player_fps_s2c"),
        UPDATE_PLAYER_POSE_S2C("update_player_pose_s2c"),
        LAZY_SETTINGS_S2C("lazy_settings_s2c");

        private final String id;

        PacketId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /*
     * Register Payloads
     */
    public static void registerPayloads() {
        // C2S
        registerPayload(PacketId.HANDSHAKE_C2S.getId(), HandShakeC2SPayload::new);
        registerPayload(PacketId.CLIENT_PLAYER_FPS_C2S.getId(), ClientPlayerFpsPayload_C2S::new);
        registerPayload(PacketId.REQUEST_CLIENT_MOD_VERSION_C2S.getId(), RequestClientModVersionPayload_C2S::new);
        registerPayload(PacketId.BLOCK_HARDNESS_SYNC_PROTOCOL_C2S.getId(), BlockHardnessSyncPayload_C2S::new);

        // S2C
        registerPayload(PacketId.HANDSHAKE_S2C.getId(), HandShakeS2CPayload::new);
        registerPayload(PacketId.REQUEST_HANDSHAKE_S2C.getId(), buf -> new RequestHandShakeS2CPayload());
        registerPayload(PacketId.SYNC_CUSTOM_BLOCK_HARDNESS.getId(), CustomBlockHardnessPayload_S2C::new);
        registerPayload(PacketId.BLOCK_HARDNESS_SYNC_PROTOCOL_S2C.getId(), BlockHardnessSyncPayload_S2C::new);
        registerPayload(PacketId.CLIENT_PLAYER_FPS_S2C.getId(), buf -> new ClientPlayerFpsPayload_S2C());
        registerPayload(PacketId.UPDATE_PLAYER_POSE_S2C.getId(), UpdatePlayerPosePayload_S2C::new);
        registerPayload(PacketId.REQUEST_CLIENT_MOD_VERSION_S2C.getId(), buf -> new RequestClientModVersionPayload_S2C());
        registerPayload(PacketId.LAZY_SETTINGS_S2C.getId(), LazySettingsPayload_S2C::new);

        // Both
        registerPayload(PacketId.UNKNOWN.getId(), buf -> new AMS_UnknownPayload());
    }

    /*
     * Register Payload Handlers
     */
    // C2S
    private static void registerC2SHandlers(@NotNull PayloadDispatcher dispatcher) {
        dispatcher.register(AMS_UnknownPayload.class, (payload, sender) -> payload.handle());
        dispatcher.register(HandShakeC2SPayload.class, (payload, sender) -> payload.handle(sender));
        dispatcher.register(ClientPlayerFpsPayload_C2S.class, (payload, sender) -> payload.handle(sender));
        dispatcher.register(RequestClientModVersionPayload_C2S.class, (payload, sender) -> payload.handle(sender));
        dispatcher.register(BlockHardnessSyncPayload_C2S.class, (payload, sender) -> payload.handle(sender));
    }

    // S2C
    private static void registerS2CHandlers(@NotNull PayloadDispatcher dispatcher) {
        dispatcher.register(AMS_UnknownPayload.class, payload -> payload.handle());
        dispatcher.register(HandShakeS2CPayload.class, payload -> payload.handle());
        dispatcher.register(RequestHandShakeS2CPayload.class, payload -> payload.handle());
        dispatcher.register(CustomBlockHardnessPayload_S2C.class, payload -> payload.handle());
        dispatcher.register(BlockHardnessSyncPayload_S2C.class, payload -> payload.handle());
        dispatcher.register(ClientPlayerFpsPayload_S2C.class, payload -> payload.handle());
        dispatcher.register(UpdatePlayerPosePayload_S2C.class,  payload -> payload.handle());
        dispatcher.register(RequestClientModVersionPayload_S2C.class, payload -> payload.handle());
        dispatcher.register(LazySettingsPayload_S2C.class, payload -> payload.handle());
    }

    private static void registerPayload(String packetId, Function<FriendlyByteBuf, AMS_CustomPayload> constructor) {
        PAYLOAD_REGISTRY.put(packetId, constructor);
    }

    //#if MC<12005
    //$$ public static AMS_CustomPayload decodeC2SPacket(ServerboundCustomPayloadPacket packet) {
    //$$     return AMS_PayloadCodec.decode(packet);
    //$$ }

    //$$ public static AMS_CustomPayload decodeS2CPacket(ClientboundCustomPayloadPacket packet) {
    //$$     return AMS_PayloadCodec.decode(packet);
    //$$ }
    //#endif

    public static class DispatcherGetter {
        public static PayloadDispatcher getC2SDispatcher() {
            return C2S_DISPATCHER;
        }

        public static PayloadDispatcher getS2CDispatcher() {
            return S2C_DISPATCHER;
        }
    }

    private static class PayloadDispatcherCreator {
        private static PayloadDispatcher createC2SDispatcher() {
            PayloadDispatcher dispatcher = new PayloadDispatcher();
            registerC2SHandlers(dispatcher);
            return dispatcher;
        }

        private static PayloadDispatcher createS2CDispatcher() {
            PayloadDispatcher dispatcher = new PayloadDispatcher();
            registerS2CHandlers(dispatcher);
            return dispatcher;
        }
    }
}
