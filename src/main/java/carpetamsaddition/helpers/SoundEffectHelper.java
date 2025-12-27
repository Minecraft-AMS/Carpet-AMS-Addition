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

package carpetamsaddition.helpers;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SoundEffectHelper {
    public static void playFlipSound(Player player, Level world) {
        if (CarpetAMSAdditionSettings.flippinCactusSoundEffect != 0) {
            float volume, pitch;
            switch (CarpetAMSAdditionSettings.flippinCactusSoundEffect) {
                case 1:
                    volume = 1.0F;
                    pitch = 0.95F;
                    world.playSound(null, player.blockPosition(), SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, volume, pitch);
                    break;
                case 2:
                    volume = 1.0F;
                    pitch = 2.5F;
                    world.playSound(null, player.blockPosition(), SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, volume, pitch);
                    break;
                case 3:
                    volume = 1.0F;
                    pitch = 0.75F;
                    world.playSound(null, player.blockPosition(), SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, volume, pitch);
                    break;
                case 4:
                    volume = 1.0F;
                    pitch = 1.0F;
                    world.playSound(null, player.blockPosition(), SoundEvents.VILLAGER_AMBIENT, SoundSource.BLOCKS, volume, pitch);
                    break;
                case 5:
                    volume = 1.0F;
                    pitch = 1.0F;
                    world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, volume, pitch);
                    break;
            }
        }
    }
}
