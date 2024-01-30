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

package club.mcams.carpet.mixin.rule.blockChunkLoader;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.BlockChunkLoader.BlockChunkLoaderHelper;

import net.minecraft.block.BellBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BellBlock.class)
public abstract class BellBlockMixin {
    @Inject(
        //#if MC>=11700
        method = "ring(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
        //#else
        //$$ method = "ring(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
        //#endif
        at = @At("HEAD")
    )
    private void ring(
        //#if MC>=11700
        Entity entity,
        //#endif
        World world, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir
    ) {
        if (AmsServerSettings.bellBlockChunkLoader && !world.isClient) {
            ChunkPos chunkPos = new ChunkPos(pos);
            BlockChunkLoaderHelper.loadChunk((ServerWorld) world, chunkPos);
        }
    }
}
