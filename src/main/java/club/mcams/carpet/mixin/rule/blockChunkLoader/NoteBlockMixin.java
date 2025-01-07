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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
//#if MC>=11900
//$$ import net.minecraft.block.BlockState;
//$$ import net.minecraft.entity.Entity;
//#endif
import net.minecraft.server.world.ServerWorld;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
    @Inject(method = "playNote", at = @At("HEAD"))
    private void playNoteMixin(
        //#if MC>=11900
        //$$ Entity entity,
        //$$ BlockState blockState,
        //#endif
        World world, BlockPos pos, CallbackInfo info
    ) {
        if (!Objects.equals(AmsServerSettings.noteBlockChunkLoader, "false")) {
            handleChunkLoading(world, pos);
        }
    }

    @Unique
    private void handleChunkLoading(World world, BlockPos pos) {
        if (!world.isClient) {
            ChunkPos chunkPos = new ChunkPos(pos);
            BlockState noteBlockUp = world.getBlockState(pos.up(1));
            if (Objects.equals(AmsServerSettings.noteBlockChunkLoader, "note_block")) {
                BlockChunkLoaderHelper.addNoteBlockTicket((ServerWorld) world, chunkPos);
            } else if (Objects.equals(AmsServerSettings.noteBlockChunkLoader, "bone_block")) {
                loadChunkIfMatch(world, chunkPos, noteBlockUp, Blocks.BONE_BLOCK);
            } else if (Objects.equals(AmsServerSettings.noteBlockChunkLoader, "wither_skeleton_skull")) {
                loadChunkIfMatch(world, chunkPos, noteBlockUp, Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL);
            }
        }
    }

    @Unique
    private void loadChunkIfMatch(World world, ChunkPos chunkPos, BlockState blockState, Block... blocks) {
        for (Block block : blocks) {
            if (blockState.getBlock() == block) {
                BlockChunkLoaderHelper.addNoteBlockTicket((ServerWorld) world, chunkPos);
                break;
            }
        }
    }
}
