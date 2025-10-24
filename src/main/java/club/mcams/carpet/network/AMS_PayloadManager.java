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

package club.mcams.carpet.network;

import club.mcams.carpet.network.payloads.AMS_UnknownPayload;
import club.mcams.carpet.network.payloads.handshake.HandShakeC2SPayload;
import club.mcams.carpet.network.payloads.handshake.HandShakeS2CPayload;
import club.mcams.carpet.network.payloads.handshake.RequestHandShakeS2CPayload;
import club.mcams.carpet.network.payloads.rule.commandCustomBlockHardness.CustomBlockHardnessPayload;
import club.mcams.carpet.network.payloads.rule.commandGetClientPlayerFPS.ClientPlayerFpsPayload_C2S;
import club.mcams.carpet.network.payloads.rule.commandGetClientPlayerFPS.ClientPlayerFpsPayload_S2C;

import net.minecraft.network.PacketByteBuf;
//#if MC<12005
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
//#endif

import java.util.Map;
import java.util.function.Function;
import java.util.concurrent.ConcurrentHashMap;

public class AMS_PayloadManager {
    protected static final Map<String, Function<PacketByteBuf, AMS_CustomPayload>> PAYLOAD_REGISTRY = new ConcurrentHashMap<>();
    private static final PayloadHandlerChain C2S_HANDLER_CHAIN = PayloadHandlerChainCreator.createC2SHandlerChain();
    private static final PayloadHandlerChain S2C_HANDLER_CHAIN = PayloadHandlerChainCreator.createS2CHandlerChain();

    public enum PacketId {
        UNKNOWN("unknown"),
        HANDSHAKE_C2S("handshake_c2s"),
        HANDSHAKE_S2C("handshake_s2c"),
        REQUEST_HANDSHAKE_S2C("request_handshake_s2c"),
        SYNC_CUSTOM_BLOCK_HARDNESS("sync_custom_block_hardness"),
        CLIENT_PLAYER_FPS_C2S("client_player_fps_c2s"),
        CLIENT_PLAYER_FPS_S2C("client_player_fps_s2c");

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
    }

    // S2C
    private static void registerS2CHandlers(PayloadHandlerChain chain) {
        chain.addHandlerFor(HandShakeS2CPayload.class, HandShakeS2CPayload::handle);
        chain.addHandlerFor(RequestHandShakeS2CPayload.class, RequestHandShakeS2CPayload::handle);
        chain.addHandlerFor(CustomBlockHardnessPayload.class, CustomBlockHardnessPayload::handle);
        chain.addHandlerFor(AMS_UnknownPayload.class, AMS_UnknownPayload::handle);
        chain.addHandlerFor(ClientPlayerFpsPayload_S2C.class, ClientPlayerFpsPayload_S2C::handle);
    }

    /*
     * Register Payloads
     */
    // C2S
    public static void registerC2SPayloads() {
        HandShakeC2SPayload.register();
        AMS_UnknownPayload.register();
        ClientPlayerFpsPayload_C2S.register();
    }

    // S2C
    public static void registerS2CPayloads() {
        HandShakeS2CPayload.register();
        RequestHandShakeS2CPayload.register();
        CustomBlockHardnessPayload.register();
        AMS_UnknownPayload.register();
        ClientPlayerFpsPayload_S2C.register();
    }

    //#if MC<12005
    public static AMS_CustomPayload C2S_decodePacket(CustomPayloadC2SPacket packet) {
        return AMS_PayloadCodec.decode(packet);
    }

    public static AMS_CustomPayload S2C_decodePacket(CustomPayloadS2CPacket packet) {
        return AMS_PayloadCodec.decode(packet);
    }
    //#endif

    public static void register(String packetId, Function<PacketByteBuf, AMS_CustomPayload> constructor) {
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
