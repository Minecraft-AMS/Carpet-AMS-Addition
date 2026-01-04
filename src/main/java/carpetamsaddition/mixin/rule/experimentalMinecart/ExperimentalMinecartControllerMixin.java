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

package carpetamsaddition.mixin.rule.experimentalMinecart;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NewMinecartBehavior.class)
public abstract class ExperimentalMinecartControllerMixin implements MinecartControllerAccessor {
    @ModifyReturnValue(method = "getMaxSpeed", at = @At("RETURN"))
    private double setMaxSpeed(double original) {
        if (CarpetAMSAdditionSettings.experimentalMinecartSpeed != -1.0D && CarpetAMSAdditionSettings.experimentalMinecartEnabled) {
            return CarpetAMSAdditionSettings.experimentalMinecartSpeed * (this.getMinecart().isInWater() ? (double)0.5F : (double)1.0F) / (double)20.0F;
        } else {
            return original;
        }
    }
}
