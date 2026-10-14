/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.helpers.rule.blockChunkLoader;

import carpetamsaddition.config.rule.blockChunkLoader.BlockLoaderConfigStore;
import carpetamsaddition.config.rule.blockChunkLoader.BlockLoaderConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.Objects;

public final class BlockChunkLoaderManager {
    private BlockChunkLoaderManager() {}

    public static boolean matches(LoaderType type, ServerLevel world, BlockPos origin) {
        BlockLoaderConfig config = BlockLoaderConfigStore.get(type);
        return !Objects.equals("false", type.permission()) && config.enabled && config.matches(world, origin);
    }

    public static void trigger(LoaderType type, ServerLevel world, BlockPos origin, BlockPos center) {
        if (!matches(type, world, origin)) {
            return;
        }

        BlockLoaderConfig config = BlockLoaderConfigStore.get(type);
        type.ticketType().addTicket(world, center, config.ticketRadius());
    }
}
