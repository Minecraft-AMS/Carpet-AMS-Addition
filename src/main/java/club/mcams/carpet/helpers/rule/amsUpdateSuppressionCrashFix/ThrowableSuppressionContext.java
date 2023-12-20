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

package club.mcams.carpet.helpers.rule.amsUpdateSuppressionCrashFix;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.compat.DimensionWrapper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThrowableSuppressionContext {

    private static String getSuppressionPos(BlockPos pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    private static DimensionWrapper getSuppressionDimension(World world) {
        return DimensionWrapper.of(world);
    }

    public static String suppressionMessageText(BlockPos pos, World world) {
        final String minecraftStyle = "§c§o";
        return minecraftStyle + "\n" +
                "Update Suppression in: \n" +
                "Location: " + getSuppressionPos(pos) + "\n" +
                "Dimension: " + getSuppressionDimension(world) + "\n";
    }

    public static void sendMessageToServer(BlockPos pos, World world) {
        Messenger.sendServerMessage(AmsServer.minecraftServer, suppressionMessageText(pos, world));
    }
}
