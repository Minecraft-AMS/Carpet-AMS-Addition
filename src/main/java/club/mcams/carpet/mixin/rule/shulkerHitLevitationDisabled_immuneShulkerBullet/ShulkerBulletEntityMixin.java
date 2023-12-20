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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBulletEntity.class)
public abstract class ShulkerBulletEntityMixin extends Entity {
    public ShulkerBulletEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(
            method = "onEntityHit",
            at = @At(
                    value = "INVOKE",
                    //#if MC<11700
                    //$$ target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"
                    //#else
                    target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"
                    //#endif
            )
    )
    //#if MC<11700
    //$$ private boolean onEntityHit1(LivingEntity instance, StatusEffectInstance effect, Operation<Boolean> original) {
    //$$ return !AmsServerSettings.shulkerHitLevitationDisabled && original.call(instance, effect);
    //#else
    private boolean onEntityHit1(LivingEntity instance, StatusEffectInstance effect, Entity source, Operation<Boolean> original) {
        return !AmsServerSettings.shulkerHitLevitationDisabled && original.call(instance, effect, source);
    //#endif
    }

    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void onEntityHit2(EntityHitResult entityHitResult, CallbackInfo ci) {
        if(AmsServerSettings.immuneShulkerBullet && (entityHitResult.getEntity() instanceof PlayerEntity)) {
            ci.cancel();
        }
    }
}
