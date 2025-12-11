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

package club.mcams.carpet.mixin.rule.endPortalChunkLoadDisabled;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.EndPortalBlock;
import net.minecraft.world.TeleportTarget;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.21")
@Mixin(EndPortalBlock.class)
public abstract class EndPortalBlockMixin {
    @WrapOperation(
        method = "createTeleportTarget",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/TeleportTarget$PostDimensionTransition;then(Lnet/minecraft/world/TeleportTarget$PostDimensionTransition;)Lnet/minecraft/world/TeleportTarget$PostDimensionTransition;"
        )
    )
    private TeleportTarget.PostDimensionTransition endPortalChunkLoadDisabled(TeleportTarget.PostDimensionTransition instance, TeleportTarget.PostDimensionTransition next, Operation<TeleportTarget.PostDimensionTransition> original) {
        if (AmsServerSettings.endPortalChunkLoadDisabled && next.equals(TeleportTarget.ADD_PORTAL_CHUNK_TICKET)) {
            return entity -> {};
        } else {
            return original.call(instance, next);
        }
    }
}
