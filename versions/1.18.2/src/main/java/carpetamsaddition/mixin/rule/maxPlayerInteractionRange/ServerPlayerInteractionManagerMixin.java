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

package carpetamsaddition.mixin.rule.maxPlayerInteractionRange;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.util.Mth;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 1.19.4")
@Mixin(value = ServerPlayerGameMode.class, priority = 168)
public abstract class ServerPlayerInteractionManagerMixin {
    @ModifyExpressionValue(
        method = "handleBlockBreakAction",
        at = @At(
            value = "CONSTANT",
            args = "doubleValue=36.0D"
        )
    )
    private double modifyBlockBreakDistance(double constant) {
        if (CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange != -1.0D) {
            return CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange * CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange;
        } else {
            return constant;
        }
    }
}
