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

package club.mcams.carpet.mixin.rule.commandCustomBlockHardness;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PistonBlock.class, priority = 16888)
public abstract class PistonBlockMixin {
    @WrapOperation(
        method = "isMovable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;getHardness(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F"
        )
    )
    private static float noPush(BlockState blockState, BlockView blockView, BlockPos blockPos, Operation<Float> original) {
        if (!AmsServerSettings.commandCustomBlockHardness.equals("false") ) {
            Block block = blockView.getBlockState(blockPos).getBlock();
            Float defaultHardness = CustomBlockHardnessCommandRegistry.DEFAULT_HARDNESS_MAP.get(block);
            if (defaultHardness != null && defaultHardness == -1.0F) {
                return -1.0F;
            }
        }
        return original.call(blockState, blockView, blockPos);
    }
}
