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

public class BlockChunkLoaderHelper {
    private static TicketType NOTE_BLOCK_TICKET_TYPE;
    private static TicketType PISTON_BLOCK_TICKET_TYPE;
    private static TicketType BELL_BLOCK_TICKET_TYPE;

    public static void addNoteBlockTicket(ServerLevel world, BlockPos blockPos) {
        addTicket(world, ChunkPos.containing(blockPos), NOTE_BLOCK_TICKET_TYPE);
    }

    public static void addPistonBlockTicket(ServerLevel world, BlockPos blockPos) {
        addTicket(world, ChunkPos.containing(blockPos), PISTON_BLOCK_TICKET_TYPE);
    }

    public static void addBellBlockTicket(ServerLevel world, BlockPos blockPos) {
        addTicket(world, ChunkPos.containing(blockPos), BELL_BLOCK_TICKET_TYPE);
    }

    private static void addTicket(ServerLevel world, ChunkPos chunkPos, TicketType ticketType) {
        ServerChunkCache chunkCache = world.getChunkSource();
        int loadRange = getLoadRange();
        chunkCache.addTicketWithRadius(ticketType, chunkPos, loadRange);
        if (CarpetAMSAdditionSettings.blockChunkLoaderKeepWorldTickUpdate) {
            world.resetEmptyTime();
        }
    }

    private static int getLoadTime() {
        return CarpetAMSAdditionSettings.blockChunkLoaderTimeController;
    }

    private static int getLoadRange() {
        return CarpetAMSAdditionSettings.blockChunkLoaderRangeController;
    }

    @SuppressWarnings("SameParameterValue")
    private static TicketType registerTicketType(String id, int flags) {
        return TicketType.register(id, getLoadTime(), flags);
    }

    public static void registerTicketTypeToMinecraft() {
        NOTE_BLOCK_TICKET_TYPE = registerTicketType("note_block_loader", 15);
        PISTON_BLOCK_TICKET_TYPE = registerTicketType("piston_block_loader", 15);
        BELL_BLOCK_TICKET_TYPE = registerTicketType("bell_block_loader", 15);
    }
}
