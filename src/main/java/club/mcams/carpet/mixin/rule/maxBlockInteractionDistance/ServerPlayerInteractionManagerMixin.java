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

package club.mcams.carpet.mixin.rule.maxBlockInteractionDistance;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.maxBlockInteractionDistance.MaxInteractionDistanceMathHelper;

import net.minecraft.server.network.ServerPlayerInteractionManager;

//#if MC>11800
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//$$ import org.spongepowered.asm.mixin.injection.At;
//#endif

//#if MC<11900
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
//#endif
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
    //#if MC<11900
    @ModifyConstant(
            method = "processBlockBreakingAction",
            require = 1,
            allow = 1,
            constant = @Constant(doubleValue = 36.0D)
    )
    private double getActualReachDistance(final double reachDistance) {
        if (AmsServerSettings.maxBlockInteractionDistance != -1.0D) {
            return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
        } else {
            return reachDistance;
        }
    }
    //#else
    //$$ @WrapOperation(
    //$$         method = "processBlockBreakingAction",
    //$$         at = @At(
    //$$                 value = "FIELD",
    //$$                 target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"
    //$$         )
    //$$ )
    //$$ private double getActualReachDistance(Operation<Double> original) {
    //$$     if (AmsServerSettings.maxBlockInteractionDistance != -1.0D) {
    //$$         return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
    //$$     } else {
    //$$         return original.call();
    //$$     }
    //$$ }
    //#endif
}

