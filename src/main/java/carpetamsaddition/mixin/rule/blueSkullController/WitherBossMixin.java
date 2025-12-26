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

package carpetamsaddition.mixin.rule.blueSkullController;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.Difficulty;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.wither.WitherBoss;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = WitherBoss.class, priority = 168)
public abstract class WitherBossMixin {
    @ModifyVariable(method = "performRangedAttack(IDDDZ)V", at = @At("HEAD"), argsOnly = true)
    private boolean shootSkullAt(boolean blueSkull) {
        return CarpetAMSAdditionSettings.blueSkullController.equals(CarpetAMSAdditionSettings.blueSkullProbability.SURELY) || blueSkull;
    }

    @WrapOperation(
        method = "customServerAiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;getDifficulty()Lnet/minecraft/world/Difficulty;"
        )
    )
    private Difficulty modifyDifficulty(ServerLevel world, Operation<Difficulty> original) {
        if (CarpetAMSAdditionSettings.blueSkullController.equals(CarpetAMSAdditionSettings.blueSkullProbability.NEVER)) {
            return Difficulty.EASY;
        } else {
            return original.call(world);
        }
    }
}
