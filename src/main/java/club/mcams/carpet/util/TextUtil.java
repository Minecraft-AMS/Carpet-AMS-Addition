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

package club.mcams.carpet.util;

import club.mcams.carpet.util.compat.DimensionWrapper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/*
 * Reference: Carpet TIS Addition
 */
//TODO: 暂时先不管这里，能用就行
public class TextUtil {
    public static String tp(Vec3d pos) {
        return String.format("/tp %s %s %s", pos.getX(), pos.getY(), pos.getZ());
    }

    public static String tp(Vec3i pos) {
        return String.format("/tp %d %d %d", pos.getX(), pos.getY(), pos.getZ());
    }

    public static String tp(ChunkPos pos) {
        return String.format("/tp %d ~ %d", pos.x * 16 + 8, pos.z * 16 + 8);
    }

    public static String tp(Vec3d pos, DimensionWrapper dimensionType) {
        return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');
    }

    public static String tp(Vec3i pos, DimensionWrapper dimensionType) {
        return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');
    }

    public static String tp(ChunkPos pos, DimensionWrapper dimensionType) {
        return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');
    }

    public static String tp(Entity entity) {
        if (entity instanceof PlayerEntity) {
            String name = ((PlayerEntity) entity).getGameProfile().getName();
            return String.format("/tp %s", name);
        }
        String uuid = entity.getUuid().toString();
        return String.format("/tp %s", uuid);
    }

    public static String coord(Vec3d pos) {
        return String.format("[%.1f, %.1f, %.1f]", pos.getX(), pos.getY(), pos.getZ());
    }

    public static String coord(Vec3i pos) {
        return String.format("[%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ());
    }

    public static String coord(ChunkPos pos) {
        return String.format("[%d, %d]", pos.x, pos.z);
    }
}
