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

package club.mcams.carpet.mixin.rule.maxPlayerInteractionRange;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Objects;

@GameVersion(version = "Minecraft >= 1.20.5")
@Mixin(value = PlayerEntity.class, priority = 1688)
public abstract class PlayerEntityMixin {
    @WrapOperation(
        method = "canInteractWithBlockAt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getBlockInteractionRange()D"
        )
    )
    private double canInteractWithBlockAt(PlayerEntity player, Operation<Double> original) {
        if (AmsServerSettings.maxPlayerBlockInteractionRange != -1.0D && Objects.equals(AmsServerSettings.maxPlayerBlockInteractionRangeScope, "server")) {
            return AmsServerSettings.maxPlayerBlockInteractionRange;
        } else {
            return original.call(player);
        }
    }

    @WrapOperation(
        method = "canInteractWithEntityIn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getEntityInteractionRange()D"
        )
    )
    private double canInteractWithEntityAt(PlayerEntity player, Operation<Double> original) {
        if (AmsServerSettings.maxPlayerEntityInteractionRange != -1.0D && Objects.equals(AmsServerSettings.maxPlayerEntityInteractionRangeScope, "server")) {
            return AmsServerSettings.maxPlayerEntityInteractionRange;
        } else {
            return original.call(player);
        }
    }
}
