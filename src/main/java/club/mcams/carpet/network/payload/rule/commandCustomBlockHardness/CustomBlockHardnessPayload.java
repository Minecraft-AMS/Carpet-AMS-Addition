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

package club.mcams.carpet.network.payload.rule.commandCustomBlockHardness;

import club.mcams.carpet.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;
import club.mcams.carpet.network.payload.AMS_CustomPayload;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;

import java.util.HashMap;
import java.util.Map;

public class CustomBlockHardnessPayload extends AMS_CustomPayload {
    public static final String PACKET_ID = "sync_custom_block_hardness";

    private final Map<BlockState, Float> hardnessMap;

    private CustomBlockHardnessPayload(PacketByteBuf buf) {
        super(PACKET_ID);
        this.hardnessMap = decode(buf);
    }

    private CustomBlockHardnessPayload(Map<BlockState, Float> hardnessMap) {
        super(PACKET_ID);
        this.hardnessMap = new HashMap<>(hardnessMap);
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        encode(buf, this.hardnessMap);
    }

    private static void encode(PacketByteBuf buf, Map<BlockState, Float> map) {
        buf.writeVarInt(map.size());
        for (Map.Entry<BlockState, Float> entry : map.entrySet()) {
            buf.writeVarInt(Block.getRawIdFromState(entry.getKey()));
            buf.writeFloat(entry.getValue());
        }
    }

    private static Map<BlockState, Float> decode(PacketByteBuf buf) {
        int size = buf.readVarInt();
        Map<BlockState, Float> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            int stateId = buf.readVarInt();
            float hardness = buf.readFloat();
            BlockState state = Block.STATE_IDS.get(stateId);
            map.put(state, hardness);
        }
        return map;
    }

    public static void register() {
        AMS_CustomPayload.register(PACKET_ID, CustomBlockHardnessPayload::new);
    }

    public static CustomBlockHardnessPayload create(Map<BlockState, Float> hardnessMap) {
        return new CustomBlockHardnessPayload(hardnessMap);
    }

    @Override
    public void handle() {
        CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.clear();
        CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.putAll(this.hardnessMap);
    }
}
