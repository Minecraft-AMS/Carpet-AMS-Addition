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
import carpetamsaddition.network.payloads.rule.commandGetClientPlayerFPS.ClientPlayerFpsPayload_C2S;
import carpetamsaddition.network.payloads.rule.commandGetClientPlayerFPS.ClientPlayerFpsPayload_S2C;
import carpetamsaddition.network.payloads.rule.commandSetPlayerPose.UpdatePlayerPosePayload_S2C;

import net.minecraft.network.FriendlyByteBuf;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.concurrent.ConcurrentHashMap;

public class AMS_PayloadManager {
    public static final Map<String, Function<FriendlyByteBuf, AMS_CustomPayload>> PAYLOAD_REGISTRY = new ConcurrentHashMap<>();
    private static final PayloadHandlerChain C2S_HANDLER_CHAIN = PayloadHandlerChainCreator.createC2SHandlerChain();
    private static final PayloadHandlerChain S2C_HANDLER_CHAIN = PayloadHandlerChainCreator.createS2CHandlerChain();

    public enum PacketId {
        UNKNOWN("unknown"),
        HANDSHAKE_C2S("handshake_c2s"),
        HANDSHAKE_S2C("handshake_s2c"),
        REQUEST_CLIENT_MOD_VERSION_S2C("request_client_mod_version_s2c"),
        REQUEST_CLIENT_MOD_VERSION_C2S("request_client_mod_version_c2s"),
        REQUEST_HANDSHAKE_S2C("request_handshake_s2c"),
        SYNC_CUSTOM_BLOCK_HARDNESS("sync_custom_block_hardness"),
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

        // S2C
        registerPayload(PacketId.HANDSHAKE_S2C.getId(), HandShakeS2CPayload::new);
        registerPayload(PacketId.REQUEST_HANDSHAKE_S2C.getId(), _ -> new RequestHandShakeS2CPayload());
        registerPayload(PacketId.SYNC_CUSTOM_BLOCK_HARDNESS.getId(), CustomBlockHardnessPayload_S2C::new);
        registerPayload(PacketId.CLIENT_PLAYER_FPS_S2C.getId(), ClientPlayerFpsPayload_S2C::new);
        registerPayload(PacketId.UPDATE_PLAYER_POSE_S2C.getId(), UpdatePlayerPosePayload_S2C::new);
        registerPayload(PacketId.REQUEST_CLIENT_MOD_VERSION_S2C.getId(), RequestClientModVersionPayload_S2C::new);
        registerPayload(PacketId.LAZY_SETTINGS_S2C.getId(), LazySettingsPayload_S2C::new);

        // Both
        registerPayload(PacketId.UNKNOWN.getId(), _ -> new AMS_UnknownPayload());
    }

    /*
     * Register Payload Handlers
     */
    // C2S
    private static void registerC2SHandlers(@NotNull PayloadHandlerChain chain) {
        chain.put(HandShakeC2SPayload.class, HandShakeC2SPayload::handle);
        chain.put(AMS_UnknownPayload.class, AMS_UnknownPayload::handle);
        chain.put(ClientPlayerFpsPayload_C2S.class, ClientPlayerFpsPayload_C2S::handle);
        chain.put(RequestClientModVersionPayload_C2S.class, RequestClientModVersionPayload_C2S::handle);
    }

    // S2C
    private static void registerS2CHandlers(@NotNull PayloadHandlerChain chain) {
        chain.put(HandShakeS2CPayload.class, HandShakeS2CPayload::handle);
        chain.put(RequestHandShakeS2CPayload.class, RequestHandShakeS2CPayload::handle);
        chain.put(CustomBlockHardnessPayload_S2C.class, CustomBlockHardnessPayload_S2C::handle);
        chain.put(AMS_UnknownPayload.class, AMS_UnknownPayload::handle);
        chain.put(ClientPlayerFpsPayload_S2C.class, ClientPlayerFpsPayload_S2C::handle);
        chain.put(UpdatePlayerPosePayload_S2C.class,  UpdatePlayerPosePayload_S2C::handle);
        chain.put(RequestClientModVersionPayload_S2C.class, RequestClientModVersionPayload_S2C::handle);
        chain.put(LazySettingsPayload_S2C.class, LazySettingsPayload_S2C::handle);
    }

    private static void registerPayload(String packetId, Function<FriendlyByteBuf, AMS_CustomPayload> constructor) {
        PAYLOAD_REGISTRY.put(packetId, constructor);
    }

    public static class HandlerChainGetter {
        public static PayloadHandlerChain getC2SHandlerChain() {
            return C2S_HANDLER_CHAIN;
        }

        public static PayloadHandlerChain getS2CHandlerChain() {
            return S2C_HANDLER_CHAIN;
        }
    }

    private static class PayloadHandlerChainCreator {
        private static PayloadHandlerChain createC2SHandlerChain() {
            PayloadHandlerChain chain = new PayloadHandlerChain();
            registerC2SHandlers(chain);
            return chain;
        }

        private static PayloadHandlerChain createS2CHandlerChain() {
            PayloadHandlerChain chain = new PayloadHandlerChain();
            registerS2CHandlers(chain);
            return chain;
        }
    }
}
