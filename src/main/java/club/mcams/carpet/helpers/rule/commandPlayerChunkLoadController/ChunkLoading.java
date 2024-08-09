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

package club.mcams.carpet.helpers.rule.commandPlayerChunkLoadController;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChunkLoading {
    public static final Map<String, Integer> unloadingPlayers = new HashMap<>();

    public static void setPlayerLoading(ServerPlayerEntity player, boolean loading) {
        if (loading) {
            unloadingPlayers.remove(player.getName().getString());
        } else {
            unloadingPlayers.put(player.getName().getString(), ChunkLoadingLevel.NONE.level());
        }
    }

    public static void setUnloadingPlayerLoadingLevel(ServerPlayerEntity player, ChunkLoadingLevel loadingLevel) {
        unloadingPlayers.put(player.getName().getString(), loadingLevel.level());
    }

    public static boolean isPlayerUnLoading(ServerPlayerEntity player) {
        return unloadingPlayers.get(player.getName().getString()) != null;
    }

    public static int getSimulationLevel(ServerPlayerEntity player) {
        return unloadingPlayers.get(player.getName().getString());
    }
}