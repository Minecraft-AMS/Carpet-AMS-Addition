/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.breedableParrots;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.EntityType;
//#if MC>=12102
//$$ import net.minecraft.entity.SpawnReason;
//#endif
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ParrotEntity.class)
public abstract class ParrotEntityMixin extends TameableShoulderEntity {
    protected ParrotEntityMixin(EntityType<? extends TameableShoulderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    protected void initGoals(CallbackInfo ci) {
        if (!Objects.equals(AmsServerSettings.breedableParrots, "none")) {
            this.targetSelector.add(1, new AnimalMateGoal(this, 1.0F));
        }
    }

    @Inject(method = "isBreedingItem", at = @At("HEAD"), cancellable = true)
    private void isBreedingItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.breedableParrots, "none")) {
            String item = stack.getItem().toString();
            if (Objects.equals(AmsServerSettings.breedableParrots, item)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "canBreedWith", at = @At("HEAD"), cancellable = true)
    private void canBreedWith(AnimalEntity other, CallbackInfoReturnable<Boolean> cir) {
        if (
            !Objects.equals(AmsServerSettings.breedableParrots, "none") &&
            other instanceof ParrotEntity
        ) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "createChild", at = @At("HEAD"), cancellable = true)
    private void createChild(ServerWorld world, PassiveEntity entity, CallbackInfoReturnable<PassiveEntity> cir) {
        if (!Objects.equals(AmsServerSettings.breedableParrots, "none")) {
            //#if MC>=12102
            //$$ PassiveEntity child = EntityType.PARROT.create(world, SpawnReason.BREEDING);
            //#else
            PassiveEntity child = EntityType.PARROT.create(world);
            //#endif
            if (child != null) {
                child.setBaby(true);
            }
            cir.setReturnValue(child);
        }
    }
}
