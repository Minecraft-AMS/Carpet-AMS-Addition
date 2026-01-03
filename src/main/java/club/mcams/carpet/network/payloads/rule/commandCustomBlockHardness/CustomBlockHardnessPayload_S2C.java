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

package club.mcams.carpet.network.payloads.rule.commandCustomBlockHardness;

import club.mcams.carpet.utils.NetworkUtil;
import club.mcams.carpet.utils.PacketByteBufExtras;
import club.mcams.carpet.network.AMS_PayloadManager;
import club.mcams.carpet.network.AMS_CustomPayload;
import club.mcams.carpet.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;

import java.util.HashMap;
import java.util.Map;

public class CustomBlockHardnessPayload_S2C extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.SYNC_CUSTOM_BLOCK_HARDNESS.getId();
    private final Map<BlockState, Float> hardnessMap;

    private CustomBlockHardnessPayload_S2C(PacketByteBuf buf) {
        super(ID);
        this.hardnessMap = PacketByteBufExtras.readMap(buf, b -> Block.STATE_IDS.get(b.readVarInt()), PacketByteBuf::readFloat);
    }

    private CustomBlockHardnessPayload_S2C(Map<BlockState, Float> hardnessMap) {
        super(ID);
        this.hardnessMap = new HashMap<>(hardnessMap);
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        PacketByteBufExtras.writeMap(buf, this.hardnessMap, (b, state) -> b.writeVarInt(Block.getRawIdFromState(state)), PacketByteBuf::writeFloat);
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnClientThread(() -> {
            CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.clear();
            CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.putAll(this.hardnessMap);
        });
    }

    public static void register() {
        AMS_PayloadManager.register(ID, CustomBlockHardnessPayload_S2C::new);
    }

    public static CustomBlockHardnessPayload_S2C create(Map<BlockState, Float> hardnessMap) {
        return new CustomBlockHardnessPayload_S2C(hardnessMap);
    }
}
