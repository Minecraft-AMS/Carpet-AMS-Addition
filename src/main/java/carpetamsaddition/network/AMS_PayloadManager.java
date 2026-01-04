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
import carpetamsaddition.network.payloads.core.StaticSettingsPayload_S2C;
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

import java.util.Map;
import java.util.function.Function;
import java.util.concurrent.ConcurrentHashMap;

public class AMS_PayloadManager {
    protected static final Map<String, Function<FriendlyByteBuf, AMS_CustomPayload>> PAYLOAD_REGISTRY = new ConcurrentHashMap<>();
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
        STATIC_SETTINGS_S2C("static_settings_s2c");

        private final String id;

        PacketId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /*
     * Register Payload Handlers
     */
    // C2S
    private static void registerC2SHandlers(PayloadHandlerChain chain) {
        chain.addHandlerFor(HandShakeC2SPayload.class, HandShakeC2SPayload::handle);
        chain.addHandlerFor(AMS_UnknownPayload.class, AMS_UnknownPayload::handle);
        chain.addHandlerFor(ClientPlayerFpsPayload_C2S.class, ClientPlayerFpsPayload_C2S::handle);
        chain.addHandlerFor(RequestClientModVersionPayload_C2S.class, RequestClientModVersionPayload_C2S::handle);
    }

    // S2C
    private static void registerS2CHandlers(PayloadHandlerChain chain) {
        chain.addHandlerFor(HandShakeS2CPayload.class, HandShakeS2CPayload::handle);
        chain.addHandlerFor(RequestHandShakeS2CPayload.class, RequestHandShakeS2CPayload::handle);
        chain.addHandlerFor(CustomBlockHardnessPayload_S2C.class, CustomBlockHardnessPayload_S2C::handle);
        chain.addHandlerFor(AMS_UnknownPayload.class, AMS_UnknownPayload::handle);
        chain.addHandlerFor(ClientPlayerFpsPayload_S2C.class, ClientPlayerFpsPayload_S2C::handle);
        chain.addHandlerFor(UpdatePlayerPosePayload_S2C.class,  UpdatePlayerPosePayload_S2C::handle);
        chain.addHandlerFor(RequestClientModVersionPayload_S2C.class, RequestClientModVersionPayload_S2C::handle);
        chain.addHandlerFor(StaticSettingsPayload_S2C.class, StaticSettingsPayload_S2C::handle);

    }

    /*
     * Register Payloads
     */
    // C2S
    public static void registerC2SPayloads() {
        HandShakeC2SPayload.register();
        AMS_UnknownPayload.register();
        ClientPlayerFpsPayload_C2S.register();
        RequestClientModVersionPayload_C2S.register();
    }

    // S2C
    public static void registerS2CPayloads() {
        HandShakeS2CPayload.register();
        RequestHandShakeS2CPayload.register();
        CustomBlockHardnessPayload_S2C.register();
        AMS_UnknownPayload.register();
        ClientPlayerFpsPayload_S2C.register();
        UpdatePlayerPosePayload_S2C.register();
        RequestClientModVersionPayload_S2C.register();
        StaticSettingsPayload_S2C.register();
    }

    public static void register(String packetId, Function<FriendlyByteBuf, AMS_CustomPayload> constructor) {
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
