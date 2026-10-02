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

package carpetamsaddition.helpers.rule.blockChunkLoader;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Comparator;

@GameVersion(version = "Minecraft < 1.21.5")
public class BlockChunkLoaderHelper {
    public static TicketType<ChunkPos> NOTE_BLOCK_TICKET_TYPE;
    public static TicketType<ChunkPos> PISTON_BLOCK_TICKET_TYPE;
    public static TicketType<ChunkPos> BELL_BLOCK_TICKET_TYPE;

    public static void addNoteBlockTicket(ServerLevel world, BlockPos pos) {
        addTicket(world, new ChunkPos(pos), NOTE_BLOCK_TICKET_TYPE);
    }

    public static void addPistonBlockTicket(ServerLevel world, BlockPos pos) {
        addTicket(world, new ChunkPos(pos), PISTON_BLOCK_TICKET_TYPE);
    }

    public static void addBellBlockTicket(ServerLevel world, BlockPos pos) {
        addTicket(world, new ChunkPos(pos), BELL_BLOCK_TICKET_TYPE);
    }

    private static void addTicket(ServerLevel world, ChunkPos chunkPos, TicketType<ChunkPos> ticketType) {
        ServerChunkCache chunkCache = world.getChunkSource();
        int loadRange = getLoadRange();
        chunkCache.addRegionTicket(ticketType, chunkPos, loadRange, chunkPos);

        if (CarpetAMSAdditionSettings.blockChunkLoaderKeepWorldTickUpdate) {
            world.resetEmptyTime();
        }
    }

    public static int getLoadTime() {
        return CarpetAMSAdditionSettings.blockChunkLoaderTimeController;
    }

    private static int getLoadRange() {
        return CarpetAMSAdditionSettings.blockChunkLoaderRangeController;
    }

    public static TicketType<ChunkPos> registerTicketType(String id, int flag) {
        return TicketType.create(id, Comparator.comparingLong(ChunkPos::toLong), getLoadTime());
    }
}
