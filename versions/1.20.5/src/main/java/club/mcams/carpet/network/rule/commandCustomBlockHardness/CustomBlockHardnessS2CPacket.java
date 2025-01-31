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

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;

import club.mcams.carpet.utils.IdentifierUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.HashMap;
import java.util.Map;

public record CustomBlockHardnessS2CPacket(Map<BlockState, Float> hardnessMap) implements CustomPayload {
    public static final CustomPayload.Id<CustomBlockHardnessS2CPacket> ID = new CustomPayload.Id<>(
        IdentifierUtil.of(AmsServer.compactName, "sync_custom_block_hardness")
    );
    public static final PacketCodec<RegistryByteBuf, CustomBlockHardnessS2CPacket> CODEC = new PacketCodec<>() {
        @Override
        public CustomBlockHardnessS2CPacket decode(RegistryByteBuf buf) {
            int size = buf.readVarInt();
            Map<BlockState, Float> map = new HashMap<>();
            for (int i = 0; i < size; i++) {
                int stateId = buf.readVarInt();
                float hardness = buf.readFloat();
                BlockState state = Block.STATE_IDS.get(stateId);
                map.put(state, hardness);
            }
            return new CustomBlockHardnessS2CPacket(map);
        }

        @Override
        public void encode(RegistryByteBuf buf, CustomBlockHardnessS2CPacket packet) {
            buf.writeVarInt(packet.hardnessMap().size());
            for (Map.Entry<BlockState, Float> entry : packet.hardnessMap().entrySet()) {
                buf.writeVarInt(Block.getRawIdFromState(entry.getKey()));
                buf.writeFloat(entry.getValue());
            }
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void sendToPlayer(ServerPlayerEntity player) {
        CustomBlockHardnessS2CPacket packet = new CustomBlockHardnessS2CPacket(new HashMap<>(CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP));
        ServerPlayNetworking.send(player, packet);
    }
}