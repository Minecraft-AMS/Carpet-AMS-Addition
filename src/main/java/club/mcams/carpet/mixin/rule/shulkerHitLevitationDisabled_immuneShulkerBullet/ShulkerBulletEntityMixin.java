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

package club.mcams.carpet.mixin.rule.shulkerHitLevitationDisabled_immuneShulkerBullet;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ShulkerBulletEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ShulkerBulletEntity.class)
public abstract class ShulkerBulletEntityMixin {
    @ModifyArg(
        method = "onEntityHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"
        )
    )
    private StatusEffectInstance noLevitation(StatusEffectInstance statusEffectInstance) {
        if (AmsServerSettings.shulkerHitLevitationDisabled || AmsServerSettings.immuneShulkerBullet) {
            return new StatusEffectInstance(StatusEffects.LEVITATION, 0);
        } else {
            return statusEffectInstance;
        }
    }

    @ModifyArg(
        method = "onEntityHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;sidedDamage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
        )
    )
    private float noDamage(float amount) {
        return AmsServerSettings.immuneShulkerBullet ? 0.0F : amount;
    }
}
