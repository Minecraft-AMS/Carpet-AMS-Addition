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

package club.mcams.carpet.mixin.rule.fasterMovement;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.EntityUtil;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @SuppressWarnings("EnhancedSwitchMigration")
    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    private void getMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (!Objects.equals(AmsServerSettings.fasterMovement, "VANILLA")) {
            PlayerEntity player = (PlayerEntity)(Object)this;
            World world = EntityUtil.getEntityWorld(player);
            if (
                (AmsServerSettings.fasterMovementController == AmsServerSettings.fasterMovementDimension.END && world.getRegistryKey() == World.END) ||
                (AmsServerSettings.fasterMovementController == AmsServerSettings.fasterMovementDimension.NETHER && world.getRegistryKey() == World.NETHER) ||
                (AmsServerSettings.fasterMovementController == AmsServerSettings.fasterMovementDimension.OVERWORLD  && world.getRegistryKey() == World.OVERWORLD) ||
                (AmsServerSettings.fasterMovementController == AmsServerSettings.fasterMovementDimension.ALL)
            ) {
                float speed = (float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                switch (AmsServerSettings.fasterMovement) {
                    case "Ⅰ":
                        speed = 0.2F;
                        break;
                    case "Ⅱ":
                        speed = 0.3F;
                        break;
                    case "Ⅲ":
                        speed = 0.4F;
                        break;
                    case "Ⅳ":
                        speed = 0.5F;
                        break;
                    case "Ⅴ":
                        speed = 0.6F;
                }
                cir.setReturnValue(speed);
            }
        }
    }
}
