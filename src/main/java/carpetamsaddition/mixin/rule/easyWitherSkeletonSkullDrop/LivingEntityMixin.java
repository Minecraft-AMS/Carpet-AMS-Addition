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

package carpetamsaddition.mixin.rule.easyWitherSkeletonSkullDrop;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.EntityUtil;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "die", at = @At("TAIL"))
    private void dropSkull(CallbackInfo ci) {
        if (CarpetAMSAdditionSettings.easyWitherSkeletonSkullDrop) {
            LivingEntity entity = (LivingEntity) (Object) this;
            if (entity instanceof WitherSkeleton && !EntityUtil.getEntityWorld(entity).isClientSide()) {
                entity.spawnAtLocation((ServerLevel) EntityUtil.getEntityWorld(entity), Items.WITHER_SKELETON_SKULL);
            }
        }
    }
}
