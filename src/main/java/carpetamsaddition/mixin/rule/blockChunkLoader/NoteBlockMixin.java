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

package carpetamsaddition.mixin.rule.blockChunkLoader;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.blockChunkLoader.BlockChunkLoaderHelper;
import carpetamsaddition.utils.WorldUtil;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
    @Inject(method = "playNote", at = @At("HEAD"))
    private void playNoteMixin(Entity entity, BlockState blockState, Level world, BlockPos pos, CallbackInfo info) {
        if (!Objects.equals(CarpetAMSAdditionSettings.noteBlockChunkLoader, "false")) {
            handleChunkLoading(world, pos);
        }
    }

    @Unique
    private void handleChunkLoading(Level world, BlockPos pos) {
        if (!WorldUtil.isClient(world)) {
            ChunkPos chunkPos = new ChunkPos(pos);
            BlockState noteBlockUp = world.getBlockState(pos.above(1));
            if (Objects.equals(CarpetAMSAdditionSettings.noteBlockChunkLoader, "note_block")) {
                BlockChunkLoaderHelper.addNoteBlockTicket((ServerLevel) world, chunkPos);
            } else if (Objects.equals(CarpetAMSAdditionSettings.noteBlockChunkLoader, "bone_block")) {
                loadChunkIfMatch(world, chunkPos, noteBlockUp, Blocks.BONE_BLOCK);
            } else if (Objects.equals(CarpetAMSAdditionSettings.noteBlockChunkLoader, "wither_skeleton_skull")) {
                loadChunkIfMatch(world, chunkPos, noteBlockUp, Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL);
            }
        }
    }

    @Unique
    private void loadChunkIfMatch(Level world, ChunkPos chunkPos, BlockState blockState, Block... blocks) {
        for (Block block : blocks) {
            if (blockState.getBlock() == block) {
                BlockChunkLoaderHelper.addNoteBlockTicket((ServerLevel) world, chunkPos);
                break;
            }
        }
    }
}
