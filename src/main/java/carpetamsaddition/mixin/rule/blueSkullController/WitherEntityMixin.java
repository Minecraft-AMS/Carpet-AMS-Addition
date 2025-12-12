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

import carpetamsaddition.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = net.minecraft.world.entity.boss.wither.WitherBoss.class, priority = 168)
public abstract class WitherEntityMixin {

    @Shadow
    protected abstract void performRangedAttack(int headIndex, double targetX, double targetY, double targetZ, boolean charged);

    @Inject(method = "performRangedAttack(ILnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    private void shootSkullAt(int headIndex, LivingEntity target, CallbackInfo ci) {
        if (AmsServerSettings.blueSkullController == AmsServerSettings.blueSkullProbability.SURELY) {
            this.performRangedAttack(headIndex, target.getX(), target.getY() + (double)target.getEyeHeight() * 0.5, target.getZ(), true);
            ci.cancel();
        }
    }

    @WrapOperation(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private Difficulty modifyDifficulty(ServerLevel world, Operation<Difficulty> original) {
        if (AmsServerSettings.blueSkullController == AmsServerSettings.blueSkullProbability.NEVER) {
            return Difficulty.EASY;
        } else {
            return original.call(world);
        }
    }
}