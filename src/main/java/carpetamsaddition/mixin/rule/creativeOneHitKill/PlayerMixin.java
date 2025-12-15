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

package carpetamsaddition.mixin.rule.creativeOneHitKill;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.EntityUtil;
import carpetamsaddition.utils.WorldUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.function.Function;

@Mixin(Player.class)
public abstract class PlayerMixin implements EntityAccessorAndInvoker, PlayerAccessorAndInvoker {
    @Inject(method = "attack", at = @At("HEAD"))
    private void attack(Entity target, CallbackInfo ci) {
        if (CarpetAMSAdditionSettings.creativeOneHitKill && !WorldUtil.isClient(this.getLevel()) && this.getPlayerAbilities().instabuild && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            Function<Boolean, Runnable> actionInstaKill = isSneaking -> isSneaking ? () -> aoeAttack(target) : () -> instaKill(target);
            actionInstaKill.apply(invokerIsSneaking()).run();
        }
    }

    @Unique
    private void instaKill(Entity target) {
        if (target instanceof EnderDragonPart) {
            Arrays.stream(((EnderDragonPart) target).parentMob.getSubEntities()).forEach(Entity -> target.kill((ServerLevel) EntityUtil.getEntityWorld(target)));
            ((EnderDragonPart) target).parentMob.kill((ServerLevel) EntityUtil.getEntityWorld(target));
        } else {
            target.kill((ServerLevel) EntityUtil.getEntityWorld(target));
        }
        playCritSoundEffect(this.getLevel(), this.invokerGetX(), this.invokerGetY(), this.invokerGetZ(), this.invokeGetSoundSource());
    }

    @Unique
    private void aoeAttack(Entity target) {
        for (Entity entity : this.getLevel().getEntitiesOfClass(Entity.class, target.getBoundingBox().inflate(2.0D, 0.50D, 2.0D))) {
            if (entity.isAttackable() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)) {
                instaKill(entity);
            }
        }
        playSweepSoundEffect(this.getLevel(), this.invokerGetX(), this.invokerGetY(), this.invokerGetZ(), this.invokeGetSoundSource());
    }

    @Unique
    private void playSweepSoundEffect(Level world, double x, double y, double z, SoundSource soundCategory) {
        world.playSound(null, x, y, z, SoundEvents.PLAYER_ATTACK_SWEEP, soundCategory, 1.0F, 1.0F);
    }

    @Unique
    private void playCritSoundEffect(Level world, double x, double y, double z, SoundSource soundCategory) {
        world.playSound(null, x, y, z, SoundEvents.PLAYER_ATTACK_CRIT, soundCategory, 1.0F, 1.0F);
    }
}
