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

package carpetamsaddition.mixin.rule.amsUpdateSuppressionCrashFix;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.amsUpdateSuppressionCrashFix.AMS_ThrowableSuppression;
import carpetamsaddition.helpers.rule.amsUpdateSuppressionCrashFix.UpdateSuppressionContext;
import carpetamsaddition.helpers.rule.amsUpdateSuppressionCrashFix.UpdateSuppressionException;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.redstone.NeighborUpdater;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@SuppressWarnings("InjectLocalCaptureCanBeReplacedWithLocal")
@Mixin(NeighborUpdater.class)
public interface NeighborUpdaterMixin {
    @Inject(
       method = "executeUpdate",
       at = @At(
           value = "INVOKE",
           target = "Lnet/minecraft/CrashReport;forThrowable(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;"
       ),
       locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void tryNeighborUpdate(Level world, BlockState state, BlockPos pos, Block sourceBlock, Orientation orientation, boolean notify, CallbackInfo ci, Throwable throwable) {
        if (!Objects.equals(CarpetAMSAdditionSettings.amsUpdateSuppressionCrashFix, "false") && UpdateSuppressionException.isUpdateSuppression(throwable)) {
            UpdateSuppressionContext.sendMessageToServer(pos, world, throwable);
            throw new AMS_ThrowableSuppression(UpdateSuppressionContext.suppressionMessageText(pos, world, throwable));
        }
    }
}
