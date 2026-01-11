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

package club.mcams.carpet.helpers;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class SoundEffectHelper {
    public static void playFlipSound(PlayerEntity player, World world) {
        if (AmsServerSettings.flippinCactusSoundEffect != 0) {
            float volume, pitch;
            switch (AmsServerSettings.flippinCactusSoundEffect) {
                case 1:
                    volume = 1.0F;
                    pitch = 0.95F;
                    world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, volume, pitch);
                    break;
                case 2:
                    volume = 1.0F;
                    pitch = 2.5F;
                    world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, volume, pitch);
                    break;
                case 3:
                    volume = 1.0F;
                    pitch = 0.75F;
                    world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, volume, pitch);
                    break;
                case 4:
                    volume = 1.0F;
                    pitch = 1.0F;
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_VILLAGER_AMBIENT, SoundCategory.BLOCKS, volume, pitch);
                    break;
                case 5:
                    volume = 1.0F;
                    pitch = 1.0F;
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, volume, pitch);
                    break;
            }
        }
    }
}
