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

package carpetamsaddition.helpers.rule.commandPlayerChunkLoadController;

import carpetamsaddition.utils.MinecraftServerUtil;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class ChunkLoading {
    public static Map<String, Boolean> onlinePlayerMap = new HashMap<>();

    public static void setPlayerInteraction(String playerName, boolean b, boolean online) {
        if (playerFromName(playerName) == null) {
            return;
        }

        if (online) {
            onlinePlayerMap.put(playerName, b);
        }
    }

    protected static ServerPlayer playerFromName(String name) {
        return MinecraftServerUtil.getServer().getPlayerList().getPlayerByName(name);
    }
}