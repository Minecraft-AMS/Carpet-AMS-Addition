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

package club.mcams.carpet.mixin.rule.maxPlayerInteractionDistance;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.maxPlayerInteractionDistance_maxClientInteractionReachDistance.MaxInteractionDistanceMathHelper;

//#if MC>11800
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.objectweb.asm.Opcodes;
//#endif

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = ServerPlayNetworkHandler.class, priority = 168)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @ModifyConstant(
            method = "onPlayerInteractBlock",
            require = 0,
            allow = 1,
            constant = @Constant(doubleValue = 64.0)
    )
    private double onPlayerInteractBlock1(double constant) {
        if (AmsServerSettings.maxPlayerInteractionDistance != -1.0D) {
            return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
        } else {
            return constant;
        }
    }

    //#if MC<11900
    @ModifyConstant(
            method = "onPlayerInteractEntity",
            require = 0,
            allow = 2,
            constant = @Constant(doubleValue = 36.0)
    )
    private double onPlayerInteractEntity(double constant) {
        if (AmsServerSettings.maxPlayerInteractionDistance != -1.0D) {
            return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
        } else {
            return constant;
        }
    }
    //#endif

    //#if MC>11800
    //$$ @WrapOperation(
    //$$          method = "onPlayerInteractBlock",
    //$$          at = @At(
    //$$                  value = "FIELD",
    //$$                  target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D",
    //$$                  opcode = Opcodes.GETSTATIC
    //$$          )
    //$$  )
    //$$  private double onPlayerInteractBlock2(Operation<Double> original) {
    //$$      if (AmsServerSettings.maxPlayerInteractionDistance != -1.0D) {
    //$$          return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
    //$$      } else {
    //$$          return original.call();
    //$$      }
    //$$  }
    //$$
    //$$ @WrapOperation(
    //$$         method = "onPlayerInteractEntity",
    //$$         at = @At(
    //$$                 value = "FIELD",
    //$$                 target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D",
    //$$                 opcode = Opcodes.GETSTATIC
    //$$         )
    //$$ )
    //$$ private double onPlayerInteractEntity(Operation<Double> original) {
    //$$     if (AmsServerSettings.maxPlayerInteractionDistance != -1.0D) {
    //$$         return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
    //$$     } else {
    //$$         return original.call();
    //$$     }
    //$$ }
    //#endif
}