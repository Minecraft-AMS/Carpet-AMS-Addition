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

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
//#if MC<12005
import net.minecraft.network.PacketByteBuf;
import io.netty.buffer.Unpooled;
//#endif
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

public class AMS_PayloadSender {
    protected static void s2c(AMS_CustomPayload payload, ServerPlayerEntity player) {
        //#if MC>=12005
        //$$ player.networkHandler.sendPacket(new CustomPayloadS2CPacket(payload));
        //#else
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        payload.write(buf);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(AMS_CustomPayload.CHANNEL_ID, buf);
        player.networkHandler.sendPacket(packet);
        //#endif
    }

    protected static void c2s(AMS_CustomPayload payload, ClientPlayerEntity player) {
        //#if MC>=12005
        //$$ player.networkHandler.sendPacket(new CustomPayloadC2SPacket(payload));
        //#else
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        payload.write(buf);
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(AMS_CustomPayload.CHANNEL_ID, buf);
        player.networkHandler.sendPacket(packet);
        //#endif
    }
}
