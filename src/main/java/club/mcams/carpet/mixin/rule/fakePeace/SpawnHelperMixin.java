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

package club.mcams.carpet.mixin.rule.fakePeace;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.compat.DimensionWrapper;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {
    @WrapOperation(
        method = "spawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/SpawnHelper;spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V"
        )
    )
    private static void allowsSpawning(SpawnGroup group, ServerWorld serverWorld, WorldChunk chunk, SpawnHelper.Checker checker, SpawnHelper.Runner runner, Operation<Void> original) {
        if (!Objects.equals(AmsServerSettings.fakePeace, "false") && group.equals(SpawnGroup.MONSTER)) {
            DimensionWrapper worldDimension = DimensionWrapper.of(serverWorld);
            Set<String> dimensionCP = new HashSet<>(Arrays.asList(AmsServerSettings.fakePeace.split(",")));
            if (dimensionCP.contains(worldDimension.getIdentifierString()) || Objects.equals(AmsServerSettings.fakePeace, "true")) {
                SpawnHelper.spawnEntitiesInChunk(null, serverWorld, chunk, null, null);
            } else {
                original.call(group, serverWorld, chunk, checker, runner);
            }
        } else {
            original.call(group, serverWorld, chunk, checker, runner);
        }
    }
}
