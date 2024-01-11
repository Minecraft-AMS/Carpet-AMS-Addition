/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package club.mcams.carpet.utils.compat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class SendSystemMessageUtil {
    public static void serverSend(MinecraftServer server, Text text) {
        //#if MC<11900
        server.sendSystemMessage(text, Util.NIL_UUID);
        //#else
        //$$ server.sendMessage(text);
        //#endif
    }

    public static void playerSend(PlayerEntity player, Text text) {
        //#if MC<11900
        player.sendSystemMessage(text, Util.NIL_UUID);
        //#else
        //$$ player.sendMessage(text);
        //#endif
    }
}
