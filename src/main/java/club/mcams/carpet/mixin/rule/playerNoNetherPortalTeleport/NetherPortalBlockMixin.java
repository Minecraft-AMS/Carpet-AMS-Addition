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

package club.mcams.carpet.mixin.rule.playerNoNetherPortalTeleport;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.NetherPortalBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {
    @WrapOperation(
        method = "onEntityCollision",
        at = @At(
            value = "INVOKE",
            //#if MC>=12100
            //$$ target = "Lnet/minecraft/entity/Entity;canUsePortals(Z)Z"
            //#else
            target = "Lnet/minecraft/entity/Entity;canUsePortals()Z"
            //#endif
        )
    )
    private boolean onEntityCollision(
        Entity entity,
        //#if MC>=12100
        //$$ boolean canUsePortals,
        //#endif
        Operation<Boolean> original
    ) {
        if (AmsServerSettings.playerNoNetherPortalTeleport && entity instanceof PlayerEntity) {
            return false;
        } else {
            //#if MC>=12100
            //$$ return original.call(entity, canUsePortals);
            //#else
            return original.call(entity);
            //#endif
        }
    }
}
