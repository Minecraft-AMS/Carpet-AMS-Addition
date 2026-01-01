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

package club.mcams.carpet.mixin.rule.amsUpdateSuppressionCrashFix;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.amsUpdateSuppressionCrashFix.AMS_ThrowableSuppression;
import club.mcams.carpet.helpers.rule.amsUpdateSuppressionCrashFix.UpdateSuppressionException;
import club.mcams.carpet.helpers.rule.amsUpdateSuppressionCrashFix.UpdateSuppressionContext;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft <= 1.18")
@Mixin(World.class)
public abstract class WorldMixin {
    @ModifyArg(
        method = "updateNeighbor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;"
        ),
        index = 0
    )
    private Throwable modifyCrashReportThrowable(Throwable original, @Local(ordinal = 1, argsOnly = true) BlockPos sourcePos) {
        if (!AmsServerSettings.amsUpdateSuppressionCrashFix.equals("false") && UpdateSuppressionException.isUpdateSuppression(original)) {
            World world = (World) (Object) this;
            UpdateSuppressionContext.sendMessageToServer(sourcePos, world, original);
            return new AMS_ThrowableSuppression(UpdateSuppressionContext.suppressionMessageText(sourcePos, world, original).getString());
        } else {
            return original;
        }
    }
}
