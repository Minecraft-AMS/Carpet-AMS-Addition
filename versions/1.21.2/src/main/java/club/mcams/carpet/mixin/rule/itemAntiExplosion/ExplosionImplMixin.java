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

package club.mcams.carpet.mixin.rule.itemAntiExplosion;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.Noop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.ExplosionImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Objects;

@GameVersion(version = "Minecraft >= 1.21.2")
@Mixin(ExplosionImpl.class)
public abstract class ExplosionImplMixin {
    @WrapOperation(
        method = "damageEntities",
        at = @At(
            value = "INVOKE",
            //#if MC>=12104
            //$$ target = "Lnet/minecraft/entity/Entity;addVelocity(Lnet/minecraft/util/math/Vec3d;)V"
            //#else
            target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"
            //#endif
        )
    )
    private void onSetVelocity(Entity entity, Vec3d velocity, Operation<Void> original) {
        if (Objects.equals(AmsServerSettings.itemAntiExplosion, "no_blast_wave") && entity instanceof ItemEntity) {
            Noop.noop();
        } else {
            original.call(entity, velocity);
        }
    }
}
