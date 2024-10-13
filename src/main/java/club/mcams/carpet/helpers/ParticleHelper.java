/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package club.mcams.carpet.helpers;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class ParticleHelper {
    public static void spawnParticles(ServerWorld world, ParticleEffect particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
        world.spawnParticles(particle, x, y, z, count, deltaX, deltaY, deltaZ, speed);
    }

    public static void spawnShulkerGolemParticles(ServerWorld serverWorld, BlockPos pos) {
        spawnParticles(serverWorld, ParticleTypes.DRAGON_BREATH, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1688, 0.8, 0.8, 0.8, 0.0168);
    }
}
