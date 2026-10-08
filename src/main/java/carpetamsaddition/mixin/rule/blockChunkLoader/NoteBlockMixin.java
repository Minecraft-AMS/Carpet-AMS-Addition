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
import carpetamsaddition.utils.BlockCondition;
import carpetamsaddition.utils.WorldUtil;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = NoteBlock.class, priority = 168)
public abstract class NoteBlockMixin {
    @Unique
    private static final BlockCondition ams$boneBlockCondition = BlockCondition.at(Direction.UP, 1, state -> state.is(Blocks.BONE_BLOCK));
    @Unique
    private static final BlockCondition ams$witherSkeletonSkullCondition = BlockCondition.at(Direction.UP, 1, state -> state.is(Blocks.WITHER_SKELETON_SKULL) || state.is(Blocks.WITHER_SKELETON_WALL_SKULL));

    @ModifyExpressionValue(
        method = "playNote",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"
        )
    )
    private boolean allowBoneBlock(boolean original, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos blockPos) {
        if (Objects.equals(CarpetAMSAdditionSettings.noteBlockChunkLoader, "bone_block") && ams$boneBlockCondition.matches(level, blockPos)) {
            return true;
        }

        return original;
    }

    @Inject(method = "triggerEvent", at = @At("HEAD"), cancellable = true)
    private void onPlayNote(BlockState blockState, Level level, BlockPos blockPos, int i, int j, CallbackInfoReturnable<Boolean> cir) {
        if (WorldUtil.isClient(level) || Objects.equals(CarpetAMSAdditionSettings.noteBlockChunkLoader, "false")) {
            return;
        }

        BlockCondition condition;
        switch (CarpetAMSAdditionSettings.noteBlockChunkLoader) {
            case "note_block":
                BlockChunkLoaderHelper.addNoteBlockTicket((ServerLevel) level, blockPos);
                return;
            case "bone_block":
                condition = ams$boneBlockCondition;
                break;
            case "wither_skeleton_skull":
                condition = ams$witherSkeletonSkullCondition;
                break;
            default:
                return;
        }

        if (condition.matches(level, blockPos)) {
            BlockChunkLoaderHelper.addNoteBlockTicket((ServerLevel) level, blockPos);
            if (Objects.equals(CarpetAMSAdditionSettings.noteBlockChunkLoader, "bone_block")) {
                cir.setReturnValue(false);
            }
        }
    }
}
