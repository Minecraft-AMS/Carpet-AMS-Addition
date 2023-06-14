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
import club.mcams.carpet.function.BlockChunkLoader;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
//#if MC>=11900
//$$ import net.minecraft.block.BlockState;
//$$ import net.minecraft.entity.Entity;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
    @Inject(at = @At("HEAD"), method = "playNote")
    private void playNoteMixin(
            //#if MC>=11900
            //$$ Entity entity,
            //#endif
            //#if MC>=11903
            //$$ BlockState blockState,
            //#endif
            World world, BlockPos pos, CallbackInfo info) {
        if (Objects.equals(AmsServerSettings.noteBlockChunkLoader, "note_block") && !world.isClient) {
            ChunkPos chunkPos = new ChunkPos(pos);
            ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.BLOCK_LOADER, chunkPos, 3, chunkPos);
        }
        if (Objects.equals(AmsServerSettings.noteBlockChunkLoader, "bone_block") && !world.isClient) {
            BlockState noteBlock = world.getBlockState(pos.up(1));
            if (Objects.equals(AmsServerSettings.noteBlockChunkLoader, "bone_block") && noteBlock.isOf(Blocks.BONE_BLOCK)) {
                ChunkPos chunkPos = new ChunkPos(pos.up(1));
                ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.BLOCK_LOADER, chunkPos, 3, chunkPos);
            }
        }
        if (Objects.equals(AmsServerSettings.noteBlockChunkLoader, "wither_skeleton_skull") && !world.isClient) {
            BlockState noteBlock = world.getBlockState(pos.up(1));
            if (Objects.equals(AmsServerSettings.noteBlockChunkLoader, "wither_skeleton_skull") && (noteBlock.isOf(Blocks.WITHER_SKELETON_SKULL)) || (noteBlock.isOf(Blocks.WITHER_SKELETON_WALL_SKULL))) {
                ChunkPos chunkPos = new ChunkPos(pos.up(1));
                ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.BLOCK_LOADER, chunkPos, 3, chunkPos);
            }
        }
    }
}
