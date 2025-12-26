/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

package carpetamsaddition.mixin.rule.itemAntiExplosion;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.Noop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ServerExplosion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin {
    @WrapOperation(
        method = "hurtEntities",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;push(Lnet/minecraft/world/phys/Vec3;)V"
        )
    )
    private void onSetVelocity(Entity entity, Vec3 velocity, Operation<Void> original) {
        if (Objects.equals(CarpetAMSAdditionSettings.itemAntiExplosion, "no_blast_wave") && entity instanceof ItemEntity) {
            Noop.noop();
        } else {
            original.call(entity, velocity);
        }
    }
}
