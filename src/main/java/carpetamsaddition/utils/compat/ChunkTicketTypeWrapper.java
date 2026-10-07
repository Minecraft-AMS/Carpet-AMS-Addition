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

package carpetamsaddition.utils.compat;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.Comparator;

/**
 * 区块加载票的便捷封装，以区块坐标作为旧版票据的 key。
 * 同一区块、同一类型、同一等级重复添加时刷新票据，不区分区块内的方块坐标。
 * 1.21.5 及之后由原版按区块、类型和等级合并，key 不再参与区分。
 * 1.16.5 至 26.3 各目标版本的加载票能力及转换限制见 {@link TicketTypeWrapper}。
 */
public final class ChunkTicketTypeWrapper {
    private final TicketTypeWrapper<ChunkPos> ticketType;

    private ChunkTicketTypeWrapper(TicketTypeWrapper<ChunkPos> ticketType) {
        this.ticketType = ticketType;
    }

    public static ChunkTicketTypeWrapper register(String id, long timeoutTicks, int flags) {
        Comparator<ChunkPos> comparator = Comparator.comparingLong(
            //#if MC>=260000
            //$$ ChunkPos::pack
            //#else
            ChunkPos::toLong
            //#endif
        );
        return new ChunkTicketTypeWrapper(TicketTypeWrapper.register(id, timeoutTicks, flags, comparator));
    }

    public Object getNativeType() {
        return this.ticketType.getNativeType();
    }

    private static ChunkPos toChunkPos(BlockPos blockPos) {
        //#if MC>=260000
        //$$ return ChunkPos.containing(blockPos);
        //#else
        return new ChunkPos(blockPos);
        //#endif
    }

    public void addTicket(ServerLevel world, BlockPos blockPos, int radius) {
        addTicket(world, toChunkPos(blockPos), radius);

        if (CarpetAMSAdditionSettings.blockChunkLoaderKeepWorldTickUpdate) {
            world.resetEmptyTime();
        }
    }

    public void addTicket(ServerLevel world, ChunkPos chunkPos, int radius) {
        this.ticketType.addTicket(world, chunkPos, radius, chunkPos);

        if (CarpetAMSAdditionSettings.blockChunkLoaderKeepWorldTickUpdate) {
            world.resetEmptyTime();
        }
    }
}
