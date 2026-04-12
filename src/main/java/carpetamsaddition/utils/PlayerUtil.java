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
//#if MC>=12111
import net.minecraft.server.players.NameAndId;
//#else
//$$ import com.mojang.authlib.GameProfile;
//#endif
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class PlayerUtil {
    public static String getName(Player player) {
        //#if MC>=12111
        return player.getGameProfile().name();
        //#else
        //$$ return player.getGameProfile().getName();
        //#endif
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

    public static UUID getPlayerUUID(Player player) {
        return player.getUUID();
    }

    @SuppressWarnings("unused")
    public static Boolean isInWhitelist(Player player) {
        return MinecraftServerUtil.getServer().getPlayerList().getWhiteList().isWhiteListed(
            //#if MC>=12111
            player.nameAndId()
            //#else
            //$$ player.getGameProfile()
            //#endif
        );
    }

    public static Boolean isInWhitelist(
        //#if MC>=12111
        NameAndId gameProfile
        //#else
        //$$ GameProfile gameProfile
        //#endif
    ) {
        return MinecraftServerUtil.getServer().getPlayerList().getWhiteList().isWhiteListed(gameProfile);
    }
}
