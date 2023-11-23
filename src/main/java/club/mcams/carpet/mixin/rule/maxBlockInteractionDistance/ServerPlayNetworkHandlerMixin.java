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

//#if MC>11800
//$$ import org.objectweb.asm.Opcodes;
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import static net.minecraft.server.network.ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE;
//#endif
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @ModifyConstant(
            method = "onPlayerInteractBlock",
            constant = @Constant(doubleValue = 64.0)
    )
    private double getActualReachDistance(final double reachDistance) {
        if (AmsServerSettings.maxBlockInteractionDistance == -1.0) {
            return reachDistance;
        } else {
            return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
        }
    }
    //#if MC>11800
    //$$ @WrapOperation(
    //$$         method = "onPlayerInteractBlock",
    //$$         at = @At(
    //$$         value = "FIELD",
    //$$         target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"
    //$$         )
    //$$ )
    //$$ private double getActualReachDistance(Operation<Double> original) {
    //$$     if (AmsServerSettings.maxBlockInteractionDistance == -1.0) {
    //$$         return original.call();
    //$$     } else {
    //$$         return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
    //$$     }
    //$$ }
    //#endif
}
