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

import carpetamsaddition.AmsServerSettings;
import carpetamsaddition.helpers.rule.amsUpdateSuppressionCrashFix.UpdateSuppressionException;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @WrapOperation(
        method = "tickChildren",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;tick(Ljava/util/function/BooleanSupplier;)V"
        )
    )
    private void tickWorlds(ServerLevel serverWorld, BooleanSupplier shouldKeepTicking, Operation<Void> original) {
        if (!Objects.equals(AmsServerSettings.amsUpdateSuppressionCrashFix, "false")) {
            try {
                original.call(serverWorld, shouldKeepTicking);
            } catch (Throwable throwable) {
                boolean isSuppression = UpdateSuppressionException.isUpdateSuppression(throwable);
                boolean isSuppressionCause = throwable.getCause() != null && UpdateSuppressionException.isUpdateSuppression(throwable.getCause());
                if (!isSuppression && !isSuppressionCause) {
                    throw throwable;
                }
            }
        } else {
            original.call(serverWorld, shouldKeepTicking);
        }
    }
}
