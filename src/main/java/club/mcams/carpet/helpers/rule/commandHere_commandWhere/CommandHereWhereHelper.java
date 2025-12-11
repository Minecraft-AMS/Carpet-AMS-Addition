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

package club.mcams.carpet.helpers.rule.commandHere_commandWhere;

import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.CommandSourceStack;

public class CommandHereWhereHelper {
    public static int[] getPos(CommandSourceStack source) {
        int x = (int) source.getPosition().x();
        int y = (int) source.getPosition().y();
        int z = (int) source.getPosition().z();
        return new int[]{x, y, z};
    }

    public static int[] getPos(Player player) {
        int x = (int) player.getX();
        int y = (int) player.getY();
        int z = (int) player.getZ();
        return new int[]{x, y, z};
    }
}
