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

package carpetamsaddition.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class PlayerUtil {
    public static String getName(Player player) {
        return player.getGameProfile().name();
    }

    public static String getName(UUID uuid) {
        ServerPlayer player = getServerPlayerEntity(uuid);
        return getName(player);
    }

    public static ServerPlayer getServerPlayerEntity(UUID uuid) {
        return MinecraftServerUtil.getServer().getPlayerList().getPlayer(uuid);
    }

    @SuppressWarnings("unused")
    public static ServerPlayer getServerPlayerEntity(String name) {
        return MinecraftServerUtil.getServer().getPlayerList().getPlayerByName(name);
    }

    @SuppressWarnings("unused")
    public static Boolean isInWhitelist(Player player) {
        return MinecraftServerUtil.getServer().getPlayerList().getWhiteList().isWhiteListed(player.nameAndId());
    }

    public static Boolean isInWhitelist(NameAndId gameProfile) {
        return MinecraftServerUtil.getServer().getPlayerList().getWhiteList().isWhiteListed(gameProfile);
    }
}
