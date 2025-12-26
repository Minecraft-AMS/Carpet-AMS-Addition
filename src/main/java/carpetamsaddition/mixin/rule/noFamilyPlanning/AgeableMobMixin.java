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

package carpetamsaddition.mixin.rule.noFamilyPlanning;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin extends PathfinderMob {
    protected AgeableMobMixin(EntityType<? extends @NotNull PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    @Final
    private static EntityDataAccessor<@NotNull Boolean> DATA_BABY_ID;

    @ModifyReturnValue(method = "getAge", at = @At("RETURN"))
    private int getBreedingAge(int original) {
        if (CarpetAMSAdditionSettings.noFamilyPlanning && !this.entityData.get(DATA_BABY_ID)) {
            return 0;
        } else {
            return original;
        }
    }
}