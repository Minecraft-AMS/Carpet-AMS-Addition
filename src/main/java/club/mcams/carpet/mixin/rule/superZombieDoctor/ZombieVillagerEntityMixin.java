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

package club.mcams.carpet.mixin.rule.superZombieDoctor;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.mob.ZombieVillagerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieVillagerEntity.class)
public abstract class ZombieVillagerEntityMixin implements ZombieVillagerEntityAccessor {
    @Inject(method = "tick", at = @At("HEAD"))
    private void modifyConversionTime(CallbackInfo ci) {
        if (AmsServerSettings.superZombieDoctor) {
            ZombieVillagerEntity zombieVillagerEntity = (ZombieVillagerEntity) (Object) this;
            if (zombieVillagerEntity.isConverting()) {
                this.setConversionTimer(0);
            }
        }
    }
}
