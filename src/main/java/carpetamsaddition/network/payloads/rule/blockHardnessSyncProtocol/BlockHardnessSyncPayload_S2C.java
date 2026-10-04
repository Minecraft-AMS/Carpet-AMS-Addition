/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

package carpetamsaddition.network.payloads.rule.blockHardnessSyncProtocol;

import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;
import carpetamsaddition.helpers.rule.blockHardnessSyncProtocol.BlockHardnessSyncCache;
import carpetamsaddition.utils.NetworkUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHardnessSyncPayload_S2C extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.BLOCK_HARDNESS_SYNC_PROTOCOL_S2C.getId();
    private final BlockState state;
    private final float hardness;

    private BlockHardnessSyncPayload_S2C(BlockState state, float hardness) {
        super(ID);
        this.state = state;
        this.hardness = hardness;
    }

    public BlockHardnessSyncPayload_S2C(FriendlyByteBuf buf) {
        super(ID);
        this.state = Block.BLOCK_STATE_REGISTRY.byId(buf.readVarInt());
        this.hardness = buf.readFloat();
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        buf.writeVarInt(Block.getId(this.state));
        buf.writeFloat(this.hardness);
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnClientThread(() -> {
            if (this.state != null) {
                BlockHardnessSyncCache.put(this.state, this.hardness);
            }
        });
    }

    public static BlockHardnessSyncPayload_S2C create(BlockState state, float hardness) {
        return new BlockHardnessSyncPayload_S2C(state, hardness);
    }
}
