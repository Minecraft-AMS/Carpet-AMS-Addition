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

package club.mcams.carpet.mixin.rule.mitePearl;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
//#if MC>=12102
//$$ import net.minecraft.entity.SpawnReason;
//#endif
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin extends ThrownItemEntity {
    public EnderPearlEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
        method = "onCollision",
        at = @At(
            value = "INVOKE",
            //#if MC<11900
            target = "Ljava/util/Random;nextFloat()F"
            //#else
            //$$ target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"
            //#endif
        )
    )
    private void onCollision(CallbackInfo ci) {
        if (AmsServerSettings.mitePearl) {
            Entity entity = this.getOwner();
            //#if MC>=12102
            //$$ EndermiteEntity endermiteEntity = EntityType.ENDERMITE.create(this.getWorld(), SpawnReason.EVENT);
            //#elseif MC>=11900
            //$$ EndermiteEntity endermiteEntity = EntityType.ENDERMITE.create(this.getWorld());
            //#else
            EndermiteEntity endermiteEntity = EntityType.ENDERMITE.create(this.world);
            //#endif
            if (entity instanceof ServerPlayerEntity && endermiteEntity != null) {
                //#if MC>11700
                endermiteEntity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                //#else
                //$$ endermiteEntity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.yaw, entity.pitch);
                //#endif
                //#if MC>=11900
                //$$ this.getWorld().spawnEntity(endermiteEntity);
                //#else
                this.world.spawnEntity(endermiteEntity);
                //#endif
            }
        }
    }
}
