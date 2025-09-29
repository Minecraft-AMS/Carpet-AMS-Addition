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

import club.mcams.carpet.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;
import club.mcams.carpet.network.BaseCustomPayload;
import club.mcams.carpet.utils.IdentifierUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
//#if MC>=12005
//$$ import net.minecraft.network.packet.CustomPayload;
//#else
import club.mcams.carpet.network.CustomPayload;
//#endif
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

//#if MC>=12005
//$$ import net.minecraft.network.codec.PacketCodec;
//$$ import net.minecraft.network.packet.CustomPayload;
//$$ import io.netty.buffer.Unpooled;
//#endif

public class CustomBlockHardnessPayload extends BaseCustomPayload implements CustomPayload {
    public static final Identifier ID = IdentifierUtil.of("carpetamsaddition", "sync_custom_block_hardness");

    //#if MC>=12005
    //$$ public static final CustomPayload.Id<CustomBlockHardnessPayload> ID_ = new CustomPayload.Id<>(ID);
    //$$ public static final PacketCodec<PacketByteBuf, CustomBlockHardnessPayload> CODEC = PacketCodec.of(CustomBlockHardnessPayload::write, CustomBlockHardnessPayload::new);
    //#endif

    private final Map<BlockState, Float> hardnessMap;

    public CustomBlockHardnessPayload(Map<BlockState, Float> hardnessMap) {
        super(ID);
        this.hardnessMap = new HashMap<>(hardnessMap);
    }

    //#if MC>=12005
    //$$ public CustomBlockHardnessPayload(PacketByteBuf buf) {
    //$$     super(ID);
    //$$     this.hardnessMap = decode(buf);
    //$$ }
    //#endif

    public void write(PacketByteBuf buf) {
        encode(buf, this.hardnessMap);
    }

    //#if MC>=12005
    //$$ public Id<? extends CustomPayload> getId() {
    //$$     return ID_;
    //$$ }
    //#endif

    //#if MC>=12005
    //$$ public Map<BlockState, Float> getHardnessMap() {
    //$$     return hardnessMap;
    //$$ }
    //#endif

    public static void encode(PacketByteBuf buf, Map<BlockState, Float> map) {
        buf.writeVarInt(map.size());
        for (Map.Entry<BlockState, Float> entry : map.entrySet()) {
            buf.writeVarInt(Block.getRawIdFromState(entry.getKey()));
            buf.writeFloat(entry.getValue());
        }
    }

    public static Map<BlockState, Float> decode(PacketByteBuf buf) {
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

    public static void handleCustomBlockHardnessPacket(Object packet) {
        Map<BlockState, Float> hardnessMap;
        //#if MC>=12005
        //$$ hardnessMap = ((CustomBlockHardnessPayload) packet).getHardnessMap();
        //#else
        PacketByteBuf buf = ((CustomPayloadS2CPacket) packet).getData();
        hardnessMap = CustomBlockHardnessPayload.decode(buf);
        //#endif
        CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.clear();
        CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.putAll(hardnessMap);
    }
}