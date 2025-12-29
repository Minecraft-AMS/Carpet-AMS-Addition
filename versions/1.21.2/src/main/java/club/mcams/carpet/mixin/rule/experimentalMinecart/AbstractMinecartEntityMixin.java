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

package club.mcams.carpet.mixin.rule.experimentalMinecart;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.21.2")
@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin {
    @Unique
    private boolean isExperimentalMinecart$AMS = false;

    @ModifyReturnValue(method = "areMinecartImprovementsEnabled", at = @At("RETURN"))
    private static boolean setExMinecartEnabled(boolean original) {
        return AmsServerSettings.minecartImprovementsEnabled || original;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void checkAndUpdateController(CallbackInfo ci) {
        if (AmsServerSettings.minecartImprovementsEnabled != isExperimentalMinecart$AMS) {
            AbstractMinecartEntity minecart = (AbstractMinecartEntity) (Object) this;
            isExperimentalMinecart$AMS = AmsServerSettings.minecartImprovementsEnabled;
            ExperimentalMinecartController experimentalMinecartController = new ExperimentalMinecartController(minecart);
            DefaultMinecartController defaultMinecartController = new DefaultMinecartController(minecart);
            if (AmsServerSettings.minecartImprovementsEnabled) {
                ((AbstractMinecartEntityAccessor) minecart).setController(experimentalMinecartController);
            } else {
                ((AbstractMinecartEntityAccessor) minecart).setController(defaultMinecartController);
            }
        }
    }
}
