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
import carpetamsaddition.utils.compat.DimensionWrapper;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.Objects;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin {
    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    private NaturalSpawner.SpawnState lastSpawnState;

    @Inject(
        //#if MC>=260300
        //$$ method = "tickChunks(Lnet/minecraft/util/profiling/ProfilerFiller;)V",
        //#elseif MC>=12105
        method = "tickChunks(Lnet/minecraft/util/profiling/ProfilerFiller;J)V",
        //#else
        //$$ method = "tickChunks()V",
        //#endif
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/server/level/ServerChunkCache;lastSpawnState:Lnet/minecraft/world/level/NaturalSpawner$SpawnState;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void modifyMonsterCount(CallbackInfo ci) {
        if (ams$isFakePeaceEnabled()) {
            SpawnStateAccessor state = (SpawnStateAccessor) this.lastSpawnState;
            int limit = MobCategory.MONSTER.getMaxInstancesPerChunk() * state.ams$getSpawnableChunkCount() / NaturalSpawnerAccessor.ams$getMagicNumber() + 1;
            Object2IntOpenHashMap<MobCategory> counts = state.ams$getMobCategoryCounts();
            counts.put(MobCategory.MONSTER, Math.max(counts.getInt(MobCategory.MONSTER), limit));
        }
    }

    @Unique
    private boolean ams$isFakePeaceEnabled() {
        String option = CarpetAMSAdditionSettings.fakePeace;

        if (Objects.equals(option, "false")) {
            return false;
        }

        if (Objects.equals(option, "true")) {
            return true;
        }

        String dimension = DimensionWrapper.of(this.level).getIdentifierString();

        return Arrays.asList(option.split(",")).contains(dimension);
    }
}
