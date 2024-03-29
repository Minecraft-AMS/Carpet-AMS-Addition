/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.helpers.rule.BlockChunkLoader;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import java.util.Comparator;

public class BlockChunkLoaderHelper {
    public static void loadChunk(ServerWorld world, ChunkPos chunkPos) {
        addTicket(world, chunkPos);
        if (AmsServerSettings.blockChunkLoaderKeepWorldTickUpdate) {
            resetIdleTimeout(world);
        }
    }

    private static void addTicket(ServerWorld world, ChunkPos chunkPos) {
        int loadTime = AmsServerSettings.blockChunkLoaderTimeController; // Default: 300gt
        int loadRange = AmsServerSettings.blockChunkLoaderRangeController; // Default: 3x3
        ChunkTicketType<ChunkPos> blockLoader =
            ChunkTicketType.create(
                "block_loader",
                Comparator.comparingLong(ChunkPos::toLong),
                loadTime
            );
        world.getChunkManager().addTicket(blockLoader, chunkPos, loadRange, chunkPos);
    }

    private static void resetIdleTimeout(ServerWorld world) {
        world.resetIdleTimeout();
    }
}
