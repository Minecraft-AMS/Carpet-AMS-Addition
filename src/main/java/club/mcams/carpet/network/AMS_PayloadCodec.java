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
import club.mcams.carpet.utils.NetworkUtil;

import net.minecraft.network.PacketByteBuf;

//#if MC<12005
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import club.mcams.carpet.mixin.network.CustomPayloadC2SPacketAccessor;
import club.mcams.carpet.mixin.network.CustomPayloadS2CPacketAccessor;
//#endif

import java.util.function.Function;

public class AMS_PayloadCodec {
    //#if MC<12005
    protected static AMS_CustomPayload decode(CustomPayloadS2CPacket packet) {
        return decodePayload(((CustomPayloadS2CPacketAccessor) packet).getData());
    }

    protected static AMS_CustomPayload decode(CustomPayloadC2SPacket packet) {
        return decodePayload(((CustomPayloadC2SPacketAccessor) packet).getData());
    }
    //#endif

    protected static AMS_CustomPayload decodePayload(PacketByteBuf buf) {
        String packetId = NetworkUtil.readBufString(buf);
        Function<PacketByteBuf, AMS_CustomPayload> constructor = AMS_PayloadManager.PAYLOAD_REGISTRY.get(packetId);
        return constructor != null ? constructor.apply(buf) : AMS_UnknownPayload.create();
    }
}
