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

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
//#if MC>=12005
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
//#else
//$$ import io.netty.buffer.Unpooled;
//$$ import net.minecraft.network.FriendlyByteBuf;
//$$ import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
//$$ import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
//#endif

public class AMS_PayloadSender {
    protected static void s2c(AMS_CustomPayload payload, ServerPlayer player) {
        //#if MC>=12005
        player.connection.send(new ClientboundCustomPayloadPacket(payload));
        //#else
        //$$ FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        //$$ payload.write(buf);
        //$$ player.connection.send(new ClientboundCustomPayloadPacket(AMS_CustomPayload.CHANNEL_ID, buf));
        //#endif
    }

    protected static void c2s(AMS_CustomPayload payload, LocalPlayer player) {
        //#if MC>=12005
        player.connection.send(new ServerboundCustomPayloadPacket(payload));
        //#else
        //$$ FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        //$$ payload.write(buf);
        //$$ player.connection.send(new ServerboundCustomPayloadPacket(AMS_CustomPayload.CHANNEL_ID, buf));
        //#endif
    }
}
