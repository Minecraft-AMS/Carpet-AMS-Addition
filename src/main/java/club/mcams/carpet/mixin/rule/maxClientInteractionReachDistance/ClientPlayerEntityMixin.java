/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.maxClientInteractionReachDistance;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.player.LocalPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LocalPlayer.class, priority = 1688)
public abstract class ClientPlayerEntityMixin {
    @WrapOperation(
        method = "raycastHitResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;blockInteractionRange()D"
        )
    )
    private double modifyBlockInteractionRange(LocalPlayer player, Operation<Double> original) {
        if (AmsServerSettings.maxClientInteractionReachDistance != -1.0D) {
            return AmsServerSettings.maxClientInteractionReachDistance;
        } else {
            return original.call(player);
        }
    }

    @WrapOperation(
        method = "raycastHitResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;entityInteractionRange()D"
        )
    )
    private double modifyEntityInteractionRange(LocalPlayer player, Operation<Double> original) {
        if (AmsServerSettings.maxClientInteractionReachDistance != -1.0D) {
            return AmsServerSettings.maxClientInteractionReachDistance;
        } else {
            return original.call(player);
        }
    }
}
