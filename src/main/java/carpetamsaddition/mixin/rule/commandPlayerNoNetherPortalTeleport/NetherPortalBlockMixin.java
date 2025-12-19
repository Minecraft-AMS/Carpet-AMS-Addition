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

package carpetamsaddition.mixin.rule.commandPlayerNoNetherPortalTeleport;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.commands.rule.commandPlayerNoNetherPortalTeleport.PlayerNoNetherPortalTeleportRegistry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.NetherPortalBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {
    @SuppressWarnings("SimplifiableConditionalExpression")
    @WrapOperation(
        method = "entityInside",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;canUsePortal(Z)Z"
        )
    )
    private boolean preventPlayerTeleport(Entity entity, boolean canUsePortals, Operation<Boolean> original) {
        return shouldPreventTeleport(entity) ? false : original.call(entity, canUsePortals);
    }

    @Unique
    private boolean shouldPreventTeleport(Entity entity) {
        if (Objects.equals(CarpetAMSAdditionSettings.commandPlayerNoNetherPortalTeleport, "false")) {
            return false;
        }

        if (!(entity instanceof Player)) {
            return false;
        }

        return PlayerNoNetherPortalTeleportRegistry.isGlobalMode || PlayerNoNetherPortalTeleportRegistry.NO_NETHER_PORTAL_TELEPORT_SET.contains(entity);
    }
}
