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
import club.mcams.carpet.helpers.rule.BlockChunkLoader.BlockChunkLoader;

import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin {
    @Inject(method = "onSyncedBlockEvent", at = @At("HEAD"))
    private void onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        if((Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "bone_block") || Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "all")) && !world.isClient) {
            Direction direction = state.get(FacingBlock.FACING);
            BlockState pistonBlock = world.getBlockState(pos.up(1));
            if ((Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "bone_block") || Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "all")) && pistonBlock.isOf(Blocks.BONE_BLOCK)) {
                ChunkPos chunkPos = new ChunkPos(pos.offset(direction));
                ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.BLOCK_LOADER, chunkPos, 3, chunkPos);
            }
        }

        if((Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "bedrock") || Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "all")) && !world.isClient) {
            Direction direction = state.get(FacingBlock.FACING);
            BlockState pistonBlock = world.getBlockState(pos.down(1));
            if ((Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "bedrock") || Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "all")) && pistonBlock.isOf(Blocks.BEDROCK)) {
                ChunkPos chunkPos = new ChunkPos(pos.offset(direction));
                ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.BLOCK_LOADER, chunkPos, 3, chunkPos);
            }
        }
    }
}
