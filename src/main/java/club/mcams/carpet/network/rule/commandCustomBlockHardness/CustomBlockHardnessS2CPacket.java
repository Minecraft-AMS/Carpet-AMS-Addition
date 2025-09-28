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

package club.mcams.carpet.network.rule.commandCustomBlockHardness;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;
import club.mcams.carpet.utils.IdentifierUtil;

import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.block.Block;

import java.util.Map;
import java.util.Objects;

import io.netty.buffer.Unpooled;

public class CustomBlockHardnessS2CPacket {
    public static final Identifier ID = IdentifierUtil.of("ams_server", "sync_custom_block_hardness");

    public static void decode(PacketByteBuf buf, Map<BlockState, Float> map) {
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            BlockState state = Block.STATE_IDS.get(buf.readVarInt());
            float hardness = buf.readFloat();
            map.put(state, hardness);
        }
    }

    private static void encode(PacketByteBuf buf) {
        buf.writeVarInt(CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.size());
        for (Map.Entry<BlockState, Float> entry : CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.entrySet()) {
            buf.writeVarInt(Block.getRawIdFromState(entry.getKey()));
            buf.writeFloat(entry.getValue());
        }
    }

    public static void sendToPlayer(ServerPlayerEntity player) {
        if (!Objects.equals(AmsServerSettings.commandCustomBlockHardness, "false")) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            encode(buf);
            CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(ID, buf);
            player.networkHandler.sendPacket(packet);
        }
    }
}
