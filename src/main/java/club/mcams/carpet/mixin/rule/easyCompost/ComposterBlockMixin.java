/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.easyCompost;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComposterBlock.class)
public abstract class ComposterBlockMixin {
    @Inject(method = "addToComposter", at = @At("HEAD"), cancellable = true)
    private static void addToComposter(BlockState state, WorldAccess world, BlockPos pos, ItemStack item, CallbackInfoReturnable<BlockState> cir) {
        if (AmsServerSettings.easyCompost) {
            int level = state.get(ComposterBlock.LEVEL);
            int newLevel = Math.min(7, level + 1);
            BlockState updatedState = state.with(ComposterBlock.LEVEL, newLevel);
            world.setBlockState(pos, updatedState, 3);
            cir.setReturnValue(updatedState);
        }
    }
}
