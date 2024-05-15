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

package club.mcams.carpet.mixin.rule.extinguishedCampfire_campfireSmokeParticleDisabled;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin {
    @ModifyReturnValue(method = "getPlacementState", at = @At("RETURN"))
    private BlockState setPlacementState(BlockState original) {
        if (AmsServerSettings.extinguishedCampfire && original != null) {
            return original.with(CampfireBlock.LIT, false);
        } else {
            return original;
        }
    }

    @Inject(method = "spawnSmokeParticle", at = @At("HEAD"), cancellable = true)
    private static void noSpawnSmokeParticle(CallbackInfo ci) {
        if (AmsServerSettings.campfireSmokeParticleDisabled) {
           ci.cancel();
        }
    }
}
