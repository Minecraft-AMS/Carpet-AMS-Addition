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

package club.mcams.carpet.mixin.rule.maxPlayerInteractionRange;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.maxPlayerInteractionDistance_maxClientInteractionReachDistance.MaxInteractionDistanceMathHelper;

//#if MC>11800
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//$$ import org.spongepowered.asm.mixin.injection.At;
//#endif

import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.server.network.ServerPlayerInteractionManager;

import org.spongepowered.asm.mixin.Mixin;

@GameVersion(version = "Minecraft < 1.20.5")
@Mixin(value = ServerPlayerInteractionManager.class, priority = 168)
public abstract class ServerPlayerInteractionManagerMixin {
    //#if MC<11900
    @ModifyConstant(
            method = "processBlockBreakingAction",
            require = 0,
            allow = 1,
            constant = @Constant(doubleValue = 36.0)
    )
    private double processBlockBreakingAction(final double constant) {
        if (AmsServerSettings.maxPlayerBlockInteractionRange != -1.0D) {
            return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance(AmsServerSettings.maxPlayerBlockInteractionRange);
        } else {
            return constant;
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
    //$$     if (AmsServerSettings.maxPlayerBlockInteractionRange != -1.0D) {
    //$$         return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance(AmsServerSettings.maxPlayerBlockInteractionRange);
    //$$     } else {
    //$$         return original.call();
    //$$     }
    //$$ }
    //#endif
}