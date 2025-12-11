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
import club.mcams.carpet.utils.RegexTools;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.parrot.ShoulderRidingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Parrot.class)
public abstract class ParrotEntityMixin extends ShoulderRidingEntity {
    protected ParrotEntityMixin(EntityType<? extends @NotNull ShoulderRidingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    protected void initGoals(CallbackInfo ci) {
        if (!Objects.equals(AmsServerSettings.breedableParrots, "none")) {
            this.targetSelector.addGoal(1, new BreedGoal(this, 1.0F));
        }
    }

    @Inject(method = "isFood", at = @At("HEAD"), cancellable = true)
    private void isBreedingItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.breedableParrots, "none")) {
            String item = RegexTools.getItemRegisterName(stack.getItem().toString());
            if (Objects.equals(AmsServerSettings.breedableParrots, item)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "canMate", at = @At("HEAD"), cancellable = true)
    private void canBreedWith(Animal animalEntity, CallbackInfoReturnable<Boolean> cir) {
        if (
            !Objects.equals(AmsServerSettings.breedableParrots, "none") &&
            animalEntity instanceof Parrot
        ) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getBreedOffspring", at = @At("HEAD"), cancellable = true)
    private void createChild(ServerLevel world, AgeableMob entity, CallbackInfoReturnable<AgeableMob> cir) {
        if (!Objects.equals(AmsServerSettings.breedableParrots, "none")) {
            AgeableMob child = EntityType.PARROT.create(world, EntitySpawnReason.BREEDING);
            if (child != null) {
                child.setBaby(true);
            }
            cir.setReturnValue(child);
        }
    }
}
