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

package carpetamsaddition.mixin.rule.commandCustomMovableBlock;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.commands.rule.commandCustomMovableBlock.CustomMovableBlockCommandRegistry;
import carpetamsaddition.utils.RegexTools;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin {
    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private static void MovableBlocks(BlockState state, Level world, BlockPos blockPos, Direction direction, boolean canBreak, Direction pistonDir, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandCustomMovableBlock, "false") && CustomMovableBlockCommandRegistry.CUSTOM_MOVABLE_BLOCKS.contains(RegexTools.getBlockRegisterName(state))) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            boolean isBottomY = blockPos.getY() == world.getMinY();
            boolean isTopY = blockPos.getY() == world.getMaxY();
            if (!(blockEntity instanceof RandomizableContainerBlockEntity)) {
                if (direction == Direction.DOWN && isBottomY) {
                    cir.setReturnValue(false);
                } else if (direction == Direction.UP && isTopY) {
                    cir.setReturnValue(false);
                } else {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
