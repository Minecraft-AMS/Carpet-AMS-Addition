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
import carpetamsaddition.utils.NetworkUtil;
import carpetamsaddition.utils.PlayerUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class BlockHardnessSyncPayload_C2S extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.BLOCK_HARDNESS_SYNC_PROTOCOL_C2S.getId();
    private final UUID playerUuid;
    private final BlockPos pos;
    private final float clientHardness;

    public BlockHardnessSyncPayload_C2S(UUID playerUuid, BlockPos pos, float clientHardness) {
        super(ID);
        this.playerUuid = playerUuid;
        this.pos = pos;
        this.clientHardness = clientHardness;
    }

    public BlockHardnessSyncPayload_C2S(FriendlyByteBuf buf) {
        super(ID);
        this.playerUuid = buf.readUUID();
        this.pos = buf.readBlockPos();
        this.clientHardness = buf.readFloat();
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerUuid);
        buf.writeBlockPos(this.pos);
        buf.writeFloat(this.clientHardness);
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnServerThread(() -> {
            ServerPlayer player = PlayerUtil.getServerPlayerEntity(this.playerUuid);
            //#if MC>=11802
            BlockGetter level = ((Player) player).level();
            //#else
            //$$ BlockGetter level = player.level;
            //#endif
            BlockState state = level.getBlockState(this.pos);
            float serverHardness = state.getDestroySpeed(level, this.pos);
            if (Float.compare(this.clientHardness, serverHardness) != 0) {
                NetworkUtil.sendS2CPacket(player, BlockHardnessSyncPayload_S2C.create(state, serverHardness), NetworkUtil.SendMode.NEED_SUPPORT);
            }
        });
    }

    public static BlockHardnessSyncPayload_C2S create(UUID playerUuid, BlockPos pos, float clientHardness) {
        return new BlockHardnessSyncPayload_C2S(playerUuid, pos, clientHardness);
    }
}
