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

package carpetamsaddition.mixin.rule.shulkerHitLevitationDisabled_immuneShulkerBullet;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.Noop;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.phys.EntityHitResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ShulkerBullet.class)
public abstract class ShulkerBulletMixin {
    @ModifyArg(
        method = "onHitEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/effect/MobEffectInstance;<init>(Lnet/minecraft/core/Holder;I)V"
        )
    )
    private int noLevitation(int effectDuration) {
        return CarpetAMSAdditionSettings.shulkerHitLevitationDisabled ? 0 : effectDuration;
    }

    @WrapMethod(method = "onHitEntity")
    private void immuneShulkerBullet(EntityHitResult hitResult, Operation<Void> original) {
        if (CarpetAMSAdditionSettings.immuneShulkerBullet) {
            Noop.noop();
        } else {
            original.call(hitResult);
        }
    }
}
