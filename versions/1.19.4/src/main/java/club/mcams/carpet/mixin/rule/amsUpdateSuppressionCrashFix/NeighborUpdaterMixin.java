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
import club.mcams.carpet.helpers.rule.amsUpdateSuppressionCrashFix.ThrowableSuppression;
import club.mcams.carpet.helpers.rule.amsUpdateSuppressionCrashFix.UpdateSuppressionContext;
import club.mcams.carpet.helpers.rule.amsUpdateSuppressionCrashFix.UpdateSuppressionException;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
//#if MC>=12102
//$$ import net.minecraft.world.block.WireOrientation;
//#endif
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.NeighborUpdater;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.19")
@Mixin(NeighborUpdater.class)
public interface NeighborUpdaterMixin {
    @Inject(
       method = "tryNeighborUpdate",
       at = @At(
           value = "INVOKE",
           target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;"
       ),
       locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void tryNeighborUpdate(
        World world,
        BlockState state,
        BlockPos pos,
        Block sourceBlock,
        //#if MC>=12102
        //$$ WireOrientation orientation,
        //#else
        BlockPos sourcePos,
        //#endif
        boolean notify,
        CallbackInfo ci,
        Throwable throwable
    ) {
        if (AmsServerSettings.amsUpdateSuppressionCrashFix && UpdateSuppressionException.isUpdateSuppression(throwable)) {
            UpdateSuppressionContext.sendMessageToServer(pos, world, throwable);
           throw new ThrowableSuppression(UpdateSuppressionContext.suppressionMessageText(pos, world, throwable));
        }
    }
}
