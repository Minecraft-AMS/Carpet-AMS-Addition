/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package carpetamsaddition.mixin.rule.flippinCactusExtras;

import carpet.CarpetSettings;
import carpet.helpers.BlockRotator;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.SoundEffectHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("IfCanBeSwitch")
@Mixin(BlockRotator.class)
public abstract class Carpet_BlockRotatorMixin {
    @Inject(method = "flipBlock", at = @At("HEAD"), cancellable = true)
    private static void flipCactusExtras(BlockState state, Level world, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetAMSAdditionSettings.flippinCactusExtras && CarpetSettings.flippinCactus) {
            Block block = state.getBlock();
            BlockPos pos = hit.getBlockPos();
            BlockState newState = null;

            if (block instanceof BarrelBlock) {
                Direction currentFacing = state.getValue(BarrelBlock.FACING);
                Direction newFacing = currentFacing.getOpposite();
                newState = state.setValue(BarrelBlock.FACING, newFacing);
            } else if (block instanceof CrafterBlock) {
                newState = state.rotate(Rotation.CLOCKWISE_180);
            } else if (block instanceof ChiseledBookShelfBlock) {
                Direction currentFacing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                Direction newFacing = currentFacing.getOpposite();
                newState = state.setValue(BlockStateProperties.HORIZONTAL_FACING, newFacing);
            } else if (block instanceof ShelfBlock) {
                Direction currentFacing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                Direction newFacing = switch (currentFacing) {
                    case NORTH -> Direction.EAST;
                    case EAST -> Direction.SOUTH;
                    case SOUTH -> Direction.WEST;
                    case WEST -> Direction.NORTH;
                    default -> currentFacing.getOpposite();
                };
                newState = state.setValue(BlockStateProperties.HORIZONTAL_FACING, newFacing);
            }

            if (newState != null) {
                world.setBlock(pos, newState, Block.UPDATE_CLIENTS | 1024);
                world.setBlocksDirty(pos, state, newState);
                SoundEffectHelper.playFlipSound(player, world);
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
    }
}
