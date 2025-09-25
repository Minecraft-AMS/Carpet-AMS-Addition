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
import club.mcams.carpet.utils.Noop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.20.5 && Minecraft < 1.21")
@Mixin(Entity.class)
public abstract class EntityMixin {
    @WrapOperation(
        method = "getTeleportTarget",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerChunkManager;addTicket(Lnet/minecraft/server/world/ChunkTicketType;Lnet/minecraft/util/math/ChunkPos;ILjava/lang/Object;)V"
        )
    )
    private static void endPortalChunkLoadDisabled(
        ServerChunkManager chunkManager, ChunkTicketType<ServerWorld> chunkTicketType,
        ChunkPos chunkPos, int radius, Object obj, Operation<Void> original,
        @Local BlockPos blockPos
    ) {
        if (AmsServerSettings.enderPortalChunkLoadDisabled && blockPos.equals(ServerWorld.END_SPAWN_POS)) {
            Noop.noop();
        } else {
            original.call(chunkManager, chunkTicketType, chunkPos, radius, obj);
        }
    }
}
