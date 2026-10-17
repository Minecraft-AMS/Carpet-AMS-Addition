/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 * Copyright (C) 2026 Fallen_Breath and contributors
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

package carpetamsaddition.mixin.rule.endPortalChunkLoader;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.compat.TicketTypeWrapper;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.portal.PortalInfo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 1.20.5")
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(
        method = "findDimensionEntryPoint",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/portal/PortalInfo;<init>(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;FF)V"
        )
    )
    private void endPortalChunkLoader(CallbackInfoReturnable<PortalInfo> cir, @Local(argsOnly = true) ServerLevel destination, @Local BlockPos targetPos) {
        if (CarpetAMSAdditionSettings.endPortalChunkLoader) {
            TicketTypeWrapper.wrap(TicketType.PORTAL).addTicket(destination, new ChunkPos(targetPos), 3, targetPos);
        }
    }
}
