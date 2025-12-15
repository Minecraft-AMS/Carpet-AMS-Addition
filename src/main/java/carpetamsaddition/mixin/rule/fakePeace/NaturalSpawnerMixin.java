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

package carpetamsaddition.mixin.rule.fakePeace;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.Noop;
import carpetamsaddition.utils.compat.DimensionWrapper;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {
    @WrapOperation(
        method = "spawnForChunk",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/NaturalSpawner;spawnCategoryForChunk(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V"
        )
    )
    private static void allowsSpawning(MobCategory group, ServerLevel serverWorld, LevelChunk chunk, NaturalSpawner.SpawnPredicate checker, NaturalSpawner.AfterSpawnCallback runner, Operation<Void> original) {
        if (!Objects.equals(CarpetAMSAdditionSettings.fakePeace, "false") && group.equals(MobCategory.MONSTER)) {
            DimensionWrapper worldDimension = DimensionWrapper.of(serverWorld);
            Set<String> dimensionCP = new HashSet<>(Arrays.asList(CarpetAMSAdditionSettings.fakePeace.split(",")));
            if (dimensionCP.contains(worldDimension.getIdentifierString()) || Objects.equals(CarpetAMSAdditionSettings.fakePeace, "true")) {
                Noop.noop();
            } else {
                original.call(group, serverWorld, chunk, checker, runner);
            }
        } else {
            original.call(group, serverWorld, chunk, checker, runner);
        }
    }
}
