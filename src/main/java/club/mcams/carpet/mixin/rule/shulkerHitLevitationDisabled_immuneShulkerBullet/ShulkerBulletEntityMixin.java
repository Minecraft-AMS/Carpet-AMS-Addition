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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void shulkerHitLevitationDisable(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (AmsServerSettings.shulkerHitLevitationDisabled && (entityHitResult.getEntity() instanceof PlayerEntity)) {
            Entity entity = entityHitResult.getEntity();
            LivingEntity entity1024 = (LivingEntity) entity;
            //#if MC<=11800
            boolean damage = entity.damage(DamageSource.mobProjectile(this, entity1024).setProjectile(), 4.0F);
            //#else
            //$$ boolean damage = entity.damage(this.getDamageSources().mobProjectile(this, entity1024), 4.0F);
            //#endif
            if (damage) {
                ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 0));
            }
            ci.cancel();
        }
    }

    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void immuneShulkerBullet(EntityHitResult entityHitResult, CallbackInfo ci) {
        if(AmsServerSettings.immuneShulkerBullet && (entityHitResult.getEntity() instanceof PlayerEntity)) {
            ci.cancel();
        }
    }
}
