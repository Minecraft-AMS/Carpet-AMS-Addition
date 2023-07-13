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

package club.mcams.carpet.mixin.rule.blueSkullController;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = net.minecraft.entity.boss.WitherEntity.class, priority = 168)
public abstract class WitherEntityMixin {
    @ModifyConstant(
            //#if MC>=11700
            method = "shootSkullAt(ILnet/minecraft/entity/LivingEntity;)V",
            //#else
            //$$ method = "shootSkullAt",
            //#endif
            constant = @Constant(floatValue = 0.001F)
    )
    private float shootSkull(float constant) {
        if(AmsServerSettings.blueSkullController == AmsServerSettings.blueSkullProbability.SURELY) {
            return 1F;
        } else {
            return constant;
        }
    }

    @Redirect(
            method = "mobTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getDifficulty()Lnet/minecraft/world/Difficulty;"
            )
    )
    private Difficulty modifyDifficulty(World instance) {
        if(AmsServerSettings.blueSkullController == AmsServerSettings.blueSkullProbability.NEVER) {
            return Difficulty.EASY;
        } else {
            return instance.getDifficulty();
        }
    }
}

