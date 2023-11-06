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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = net.minecraft.entity.boss.WitherEntity.class, priority = 168)
public abstract class WitherEntityMixin extends HostileEntity {
    protected WitherEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract void shootSkullAt(int headIndex, double targetX, double targetY, double targetZ, boolean charged);

    //#if MC>=11700
    @Inject(method = "shootSkullAt(ILnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    //#else
    //$$ @Inject(method = "method_6878", at = @At("HEAD"), cancellable = true)
    //#endif
    private void shootSkullAt(int headIndex, LivingEntity target, CallbackInfo ci) {
        if(AmsServerSettings.blueSkullController == AmsServerSettings.blueSkullProbability.SURELY) {
            this.shootSkullAt(headIndex, target.getX(), target.getY() + (double)target.getStandingEyeHeight() * 0.5, target.getZ(), true);
            ci.cancel();
        }
    }

    @WrapOperation(
            method = "mobTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getDifficulty()Lnet/minecraft/world/Difficulty;"
            )
    )
    private Difficulty modifyDifficulty(World instance, Operation<Difficulty> original) {
        if(AmsServerSettings.blueSkullController == AmsServerSettings.blueSkullProbability.NEVER) {
            return Difficulty.EASY;
        } else {
            return original.call(instance);
        }
    }
}