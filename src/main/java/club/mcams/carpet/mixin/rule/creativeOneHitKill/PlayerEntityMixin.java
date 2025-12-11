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

package club.mcams.carpet.mixin.rule.creativeOneHitKill;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.EntityUtil;
import club.mcams.carpet.utils.WorldUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.function.Function;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements EntityAccessorAndInvoker, PlayerEntityAccessorAndInvoker {
    @Inject(method = "attack", at = @At("HEAD"))
    private void attack(Entity target, CallbackInfo ci) {
        if (AmsServerSettings.creativeOneHitKill && !WorldUtil.isClient(this.getWorld()) && this.getPlayerAbilities().creativeMode && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) {
            Function<Boolean, Runnable> actionInstaKill = isSneaking -> isSneaking ? () -> aoeAttack(target) : () -> instaKill(target);
            actionInstaKill.apply(invokerIsSneaking()).run();
        }
    }

    @Unique
    private void instaKill(Entity target) {
        if (target instanceof EnderDragonPart) {
            Arrays.stream(((EnderDragonPart) target).owner.getBodyParts()).forEach(Entity -> target.kill((ServerWorld) EntityUtil.getEntityWorld(target)));
            ((EnderDragonPart) target).owner.kill((ServerWorld) EntityUtil.getEntityWorld(target));
        } else {
            target.kill((ServerWorld) EntityUtil.getEntityWorld(target));
        }
        playCritSoundEffect(this.getWorld(), this.invokerGetX(), this.invokerGetY(), this.invokerGetZ(), this.invokeGetSoundCategory());
    }

    @Unique
    private void aoeAttack(Entity target) {
        for (Entity entity : this.getWorld().getNonSpectatingEntities(Entity.class, target.getBoundingBox().expand(2.0D, 0.50D, 2.0D))) {
            if (entity.isAttackable() && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(entity)) {
                instaKill(entity);
            }
        }
        playSweepSoundEffect(this.getWorld(), this.invokerGetX(), this.invokerGetY(), this.invokerGetZ(), this.invokeGetSoundCategory());
    }

    @Unique
    private void playSweepSoundEffect(World world, double x, double y, double z, SoundCategory soundCategory) {
        world.playSound(null, x, y, z, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, soundCategory, 1.0F, 1.0F);
    }

    @Unique
    private void playCritSoundEffect(World world, double x, double y, double z, SoundCategory soundCategory) {
        world.playSound(null, x, y, z, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, soundCategory, 1.0F, 1.0F);
    }
}
