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

package club.mcams.carpet.helpers.rule.blockChunkLoader;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

public class BlockChunkLoaderHelper {
    private static final String TICKET_NAMESPACE = "carpetamsaddition";

    private static final TicketType NOTE_BLOCK_TICKET_TYPE = registerTicketType(
        String.format("%s:note_block_loader", TICKET_NAMESPACE), BlockChunkLoaderHelper.getLoadTime(), 15
    );

    private static final TicketType PISTON_BLOCK_TICKET_TYPE = registerTicketType(
        String.format("%s:piston_block_loader", TICKET_NAMESPACE), BlockChunkLoaderHelper.getLoadTime(), 15
    );

    private static final TicketType BELL_BLOCK_TICKET_TYPE = registerTicketType(
        String.format("%s:bell_block_loader", TICKET_NAMESPACE), BlockChunkLoaderHelper.getLoadTime(), 15
    );

    public static void addNoteBlockTicket(ServerLevel world, ChunkPos chunkPos) {
        addTicket(world, chunkPos, NOTE_BLOCK_TICKET_TYPE);
    }

    public static void addPistonBlockTicket(ServerLevel world, ChunkPos chunkPos) {
        addTicket(world, chunkPos, PISTON_BLOCK_TICKET_TYPE);
    }

    public static void addBellBlockTicket(ServerLevel world, ChunkPos chunkPos) {
        addTicket(world, chunkPos, BELL_BLOCK_TICKET_TYPE);
    }

    private static void addTicket(ServerLevel world, ChunkPos chunkPos, TicketType ticketType) {
        world.getChunkSource().addTicketWithRadius(ticketType, chunkPos, getLoadRange());
        blockChunkLoaderKeepWorldTickUpdate(world);
    }

    private static void blockChunkLoaderKeepWorldTickUpdate(ServerLevel world) {
        if (AmsServerSettings.blockChunkLoaderKeepWorldTickUpdate) {
            world.resetEmptyTime();
        }
    }

    private static int getLoadTime() {
        return AmsServerSettings.blockChunkLoaderTimeController;
    }

    private static int getLoadRange() {
        return AmsServerSettings.blockChunkLoaderRangeController;
    }

    @SuppressWarnings("SameParameterValue")
    private static TicketType registerTicketType(String id, long expiryTicks, int flags) {
        return Registry.register(BuiltInRegistries.TICKET_TYPE, id, new TicketType(expiryTicks, flags));
    }
}