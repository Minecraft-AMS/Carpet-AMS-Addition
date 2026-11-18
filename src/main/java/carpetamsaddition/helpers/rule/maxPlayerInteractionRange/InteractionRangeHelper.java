/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

package carpetamsaddition.helpers.rule.maxPlayerInteractionRange;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.MathUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import top.byteeeee.annotationtoolbox.annotation.GameVersion;

public final class InteractionRangeHelper {
    private InteractionRangeHelper() {}

    public static double blockRange() {
        return CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange;
    }

    public static double entityRange() {
        return CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange;
    }

    //#if MC<12006
    @GameVersion(version = "Minecraft < 1.20.6")
    @SuppressWarnings("unused")
    public static double aabbDistanceSqr(Vec3 eye, double centerX, double centerY, double centerZ) {
        return eye.distanceToSqr(
            Mth.clamp(eye.x(), centerX - 0.5D, centerX + 0.5D),
            Mth.clamp(eye.y(), centerY - 0.5D, centerY + 0.5D),
            Mth.clamp(eye.z(), centerZ - 0.5D, centerZ + 0.5D)
        );
    }

    @GameVersion(version = "Minecraft < 1.20.6")
    @SuppressWarnings("unused")
    public static boolean withinBlockRange(Player player, BlockPos pos) {
        double range = blockRange();

        if (range == -1.0D) {
            return true;
        }

        Vec3 eye = player.getEyePosition(1.0F);

        return aabbDistanceSqr(eye, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) < MathUtil.square(range);
    }
    //#endif
}
