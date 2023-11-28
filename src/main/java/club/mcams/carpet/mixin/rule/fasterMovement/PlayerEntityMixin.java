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

package club.mcams.carpet.mixin.rule.fasterMovement;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    public void getMovementSpeed(CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        World world = player.getEntityWorld();
        if (
            (AmsServerSettings.fasterMovementController == AmsServerSettings.fasterMovementDimension.END && world.getRegistryKey() == World.END) ||
            (AmsServerSettings.fasterMovementController == AmsServerSettings.fasterMovementDimension.NETHER && world.getRegistryKey() == World.NETHER) ||
            (AmsServerSettings.fasterMovementController == AmsServerSettings.fasterMovementDimension.OVERWORLD  && world.getRegistryKey() == World.OVERWORLD) ||
            (AmsServerSettings.fasterMovementController == AmsServerSettings.fasterMovementDimension.ALL)
        ) {
            if (Objects.equals(AmsServerSettings.fasterMovement, "Ⅰ")) {
                cir.setReturnValue(0.2F);
            } else if (Objects.equals(AmsServerSettings.fasterMovement, "Ⅱ")) {
                cir.setReturnValue(0.3F);
            } else if (Objects.equals(AmsServerSettings.fasterMovement, "Ⅲ")) {
                cir.setReturnValue(0.4F);
            } else if (Objects.equals(AmsServerSettings.fasterMovement, "Ⅳ")) {
                cir.setReturnValue(0.5F);
            } else if (Objects.equals(AmsServerSettings.fasterMovement, "Ⅴ")) {
                cir.setReturnValue(0.6F);
            }
        }
    }
}
