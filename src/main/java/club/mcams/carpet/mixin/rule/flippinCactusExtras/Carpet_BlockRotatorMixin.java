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

package club.mcams.carpet.mixin.rule.flippinCactusExtras;

import carpet.CarpetSettings;
import carpet.helpers.BlockRotator;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.SoundEffectHelper;

import net.minecraft.block.*;
//#if MC>=12006
//$$ import net.minecraft.util.BlockRotation;
//$$ import net.minecraft.block.CrafterBlock;
//#endif
import net.minecraft.entity.player.PlayerEntity;
//#if MC>=11900
//$$ import net.minecraft.state.property.Properties;
//#endif
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRotator.class)
public abstract class Carpet_BlockRotatorMixin {
    @Inject(method = "flip_block", at = @At("HEAD"), cancellable = true)
    private static void flipCactusExtras(BlockState state, World world, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<Boolean> cir) {
        if (AmsServerSettings.flippinCactusExtras && CarpetSettings.flippinCactus) {
            Block block = state.getBlock();
            BlockPos pos = hit.getBlockPos();
            BlockState newState = null;

            if (block instanceof BarrelBlock) {
                Direction currentFacing = state.get(BarrelBlock.FACING);
                Direction newFacing = currentFacing.getOpposite();
                newState = state.with(BarrelBlock.FACING, newFacing);
            }

            //#if MC>=12006
            //$$ else if (block instanceof CrafterBlock) {
            //$$     newState = state.rotate(BlockRotation.CLOCKWISE_180);
            //$$ }
            //#endif

            //#if MC>=11900
            //$$ else if (block instanceof ChiseledBookshelfBlock) {
            //$$     Direction currentFacing = state.get(Properties.HORIZONTAL_FACING);
            //$$     Direction newFacing = currentFacing.getOpposite();
            //$$     newState = state.with(Properties.HORIZONTAL_FACING, newFacing);
            //$$ }
            //#endif

            //#if MC>=12109
            //$$ else if (block instanceof ShelfBlock) {
            //$$     Direction currentFacing = state.get(Properties.HORIZONTAL_FACING);
            //$$     Direction newFacing = switch (currentFacing) {
            //$$         case NORTH -> Direction.EAST;
            //$$         case EAST -> Direction.SOUTH;
            //$$         case SOUTH -> Direction.WEST;
            //$$         case WEST -> Direction.NORTH;
            //$$         default -> currentFacing.getOpposite();
            //$$     };
            //$$     newState = state.with(Properties.HORIZONTAL_FACING, newFacing);
            //$$ }
            //#endif

            if (newState != null) {
                world.setBlockState(pos, newState, 2 | 1024);
                world.scheduleBlockRerenderIfNeeded(pos, state, newState);
                SoundEffectHelper.playFlipSound(player, world);
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
    }
}
