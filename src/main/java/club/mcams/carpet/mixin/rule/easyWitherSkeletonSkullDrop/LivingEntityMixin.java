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

package club.mcams.carpet.mixin.rule.easyWitherSkeletonSkullDrop;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.item.Items;
//#if MC>=12102
//$$ import net.minecraft.server.world.ServerWorld;
//$$ import club.mcams.carpet.utils.EntityUtil;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "onDeath", at = @At("TAIL"))
    private void dropSkull(CallbackInfo ci) {
        if (AmsServerSettings.easyWitherSkeletonSkullDrop) {
            LivingEntity entity = (LivingEntity) (Object) this;
            if (entity instanceof WitherSkeletonEntity) {
                //#if MC>=12102
                //$$ entity.dropItem((ServerWorld) EntityUtil.getEntityWorld(entity), Items.WITHER_SKELETON_SKULL);
                //#else
                entity.dropItem(Items.WITHER_SKELETON_SKULL, 1);
                //#endif
            }
        }
    }
}
