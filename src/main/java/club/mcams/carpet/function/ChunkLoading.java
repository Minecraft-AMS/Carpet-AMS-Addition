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

package club.mcams.carpet.function;

import carpet.CarpetServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class ChunkLoading {
    public static Map<String, Boolean> onlinePlayerMap = new HashMap<>();

    public static void setPlayerInteraction(String playerName, boolean b, boolean online) {
        if (playerFromName(playerName) == null) return;
        if (online) {
            onlinePlayerMap.put(playerName, b);
        }
    }

    public static void onPlayerConnect(PlayerEntity player) {
        String playerName = player.getName().getString();
        setPlayerInteraction(playerName, true, true);
    }

    public static void onPlayerDisconnect(PlayerEntity player) {
        String playerName = player.getName().getString();
        if (onlinePlayerMap.containsKey(playerName)) {
            setPlayerInteraction(playerName, true, false);
            onlinePlayerMap.remove(playerName);
        }
    }

    protected static ServerPlayerEntity playerFromName(String name) {
        return CarpetServer.minecraft_server.getPlayerManager().getPlayer(name);
    }
}