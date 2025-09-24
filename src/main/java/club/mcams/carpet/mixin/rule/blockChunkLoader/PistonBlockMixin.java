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
import club.mcams.carpet.helpers.rule.blockChunkLoader.BlockChunkLoaderHelper;
import club.mcams.carpet.utils.WorldUtil;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin {
    @Inject(method = "onSyncedBlockEvent", at = @At("HEAD"))
    private void onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "false")) {
            handleChunkLoading(state, world, pos);
        }
    }

    @Unique
    private void handleChunkLoading(BlockState state, World world, BlockPos pos) {
        if (!WorldUtil.isClient(world)) {
            Direction direction = state.get(FacingBlock.FACING);
            BlockState pistonBlockUp = world.getBlockState(pos.up(1));
            BlockState pistonBlockDown = world.getBlockState(pos.down(1));
            ChunkPos chunkPos = new ChunkPos(pos.offset(direction));
            if (optionIsBoneBlockOrAll()) {
                loadChunkIfMatch(world, chunkPos, pistonBlockUp, Blocks.BONE_BLOCK);
            }
            if (optionIsBedRockOrAll()) {
                loadChunkIfMatch(world, chunkPos, pistonBlockDown, Blocks.BEDROCK);
            }
        }
    }

    @Unique
    private void loadChunkIfMatch(World world, ChunkPos chunkPos, BlockState blockState, Block... blocks) {
        for (Block block : blocks) {
            if (blockState.getBlock() == block) {
                BlockChunkLoaderHelper.addPistonBlockTicket((ServerWorld) world, chunkPos);
                break;
            }
        }
    }

    @Unique
    private boolean optionIsBoneBlockOrAll() {
        return (Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "bone_block") || Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "all"));
    }

    @Unique
    private boolean optionIsBedRockOrAll() {
        return (Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "bedrock") || Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "all"));
    }
}
