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

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin {
    @Inject(method = "triggerEvent", at = @At("HEAD"))
    private void onSyncedBlockEvent(BlockState state, Level level, BlockPos pos, int i, int j, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(CarpetAMSAdditionSettings.pistonBlockChunkLoader, "false")) {
            handleChunkLoading(state, level, pos);
        }
    }

    @Unique
    private void handleChunkLoading(BlockState state, Level world, BlockPos pos) {
        if (!WorldUtil.isClient(world)) {
            Direction direction = state.getValue(DirectionalBlock.FACING);
            BlockState pistonBlockUp = world.getBlockState(pos.above(1));
            BlockState pistonBlockDown = world.getBlockState(pos.below(1));
            ChunkPos chunkPos = new ChunkPos(pos.relative(direction).getX(), pos.relative(direction).getZ());
            if (optionIsBoneBlockOrAll()) {
                loadChunkIfMatch(world, chunkPos, pistonBlockUp, Blocks.BONE_BLOCK);
            }
            if (optionIsBedRockOrAll()) {
                loadChunkIfMatch(world, chunkPos, pistonBlockDown, Blocks.BEDROCK);
            }
        }
    }

    @Unique
    private void loadChunkIfMatch(Level world, ChunkPos chunkPos, BlockState blockState, Block... blocks) {
        for (Block block : blocks) {
            if (blockState.getBlock() == block) {
                BlockChunkLoaderHelper.addPistonBlockTicket((ServerLevel) world, chunkPos);
                break;
            }
        }
    }

    @Unique
    private boolean optionIsBoneBlockOrAll() {
        return (Objects.equals(CarpetAMSAdditionSettings.pistonBlockChunkLoader, "bone_block") || Objects.equals(CarpetAMSAdditionSettings.pistonBlockChunkLoader, "all"));
    }

    @Unique
    private boolean optionIsBedRockOrAll() {
        return (Objects.equals(CarpetAMSAdditionSettings.pistonBlockChunkLoader, "bedrock") || Objects.equals(CarpetAMSAdditionSettings.pistonBlockChunkLoader, "all"));
    }
}
