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

package carpetamsaddition.helpers.rule.largeShulkerBox;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.mixin.rule.largeShulkerBox.ChunkMapAccessor;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

// 兼容锂
public final class LargeShulkerBoxHelper {
    private static boolean lastEnabled;

    private LargeShulkerBoxHelper() {}

    public static void tick(MinecraftServer server) {
        boolean enabled = CarpetAMSAdditionSettings.largeShulkerBox;

        if (lastEnabled == enabled) {
            return;
        }

        lastEnabled = enabled;

        for (ServerLevel level : server.getAllLevels()) {
            ServerChunkCache chunks = level.getChunkSource();
            long[] loadedChunks = ((ChunkMapAccessor) chunks.chunkMap).ams$getVisibleChunkMap().keySet().toLongArray();
            for (long packedPos : loadedChunks) {
                LevelChunk chunk = chunks.getChunkNow(ChunkPos.getX(packedPos), ChunkPos.getZ(packedPos));

                if (chunk == null) {
                    continue;
                }

                for (BlockEntity blockEntity : chunk.getBlockEntities().values().toArray(new BlockEntity[0])) {
                    if (blockEntity instanceof ShulkerBoxBlockEntity && !blockEntity.isRemoved()) {
                        ((ShulkerBoxBlockEntity) blockEntity).getContainerSize();
                        blockEntity.setChanged();
                    }
                }
            }
        }
    }
}
