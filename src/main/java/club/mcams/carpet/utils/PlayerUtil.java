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

package club.mcams.carpet.utils;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
//#if MC>=12109
//$$ import net.minecraft.server.PlayerConfigEntry;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class PlayerUtil {
    public static String getName(PlayerEntity player) {
        return player.getGameProfile().getName();
    }

    public static String getName(UUID uuid) {
        ServerPlayerEntity player = getServerPlayerEntity(uuid);
        return getName(player);
    }

    public static ServerPlayerEntity getServerPlayerEntity(UUID uuid) {
        return MinecraftServerUtil.getServer().getPlayerManager().getPlayer(uuid);
    }

    @SuppressWarnings("unused")
    public static ServerPlayerEntity getServerPlayerEntity(String name) {
        return MinecraftServerUtil.getServer().getPlayerManager().getPlayer(name);
    }

    @SuppressWarnings("unused")
    public static Boolean isInWhitelist(PlayerEntity player) {
        return MinecraftServerUtil.getServer().getPlayerManager().getWhitelist().isAllowed(
            //#if MC>=12109
            //$$ player.getPlayerConfigEntry()
            //#else
            player.getGameProfile()
            //#endif
        );
    }

    public static Boolean isInWhitelist(
        //#if MC>=12109
        //$$ PlayerConfigEntry gameProfile
        //#else
        GameProfile gameProfile
        //#endif
    ) {
        return MinecraftServerUtil.getServer().getPlayerManager().getWhitelist().isAllowed(gameProfile);
    }
}
