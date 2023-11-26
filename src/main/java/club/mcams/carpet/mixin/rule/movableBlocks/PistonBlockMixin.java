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

package club.mcams.carpet.mixin.rule.movableBlocks;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin {
    @Inject(method = "isMovable", at = @At("HEAD"), cancellable = true)
    //#if MC<11700
    //$$ private static void MovableBlocks(BlockState state, World world, BlockPos blockPos, Direction direction, boolean canBreak, Direction pistonDir, CallbackInfoReturnable<Boolean> cir) {
    //#else
    private static void MovableBlocks(BlockState state, World world, BlockPos pos, Direction direction, boolean canBreak, Direction pistonDir, CallbackInfoReturnable<Boolean> cir) {
        //#endif
        if (
            (AmsServerSettings.movableEnderChest && state.isOf(Blocks.ENDER_CHEST)) ||
            (AmsServerSettings.movableEndPortalFrame && state.isOf(Blocks.END_PORTAL_FRAME)) ||
            (AmsServerSettings.movableObsidian && state.isOf(Blocks.OBSIDIAN)) ||
            (AmsServerSettings.movableCryingObsidian && state.isOf(Blocks.CRYING_OBSIDIAN)) ||
            (AmsServerSettings.movableBedRock && state.isOf(Blocks.BEDROCK)) ||
            (AmsServerSettings.movableEnchantingTable && state.isOf(Blocks.ENCHANTING_TABLE)) ||
            (AmsServerSettings.movableBeacon && state.isOf(Blocks.BEACON)) ||
            (AmsServerSettings.movableAnvil && state.isOf(Blocks.ANVIL))
            //#if MC>=11900
            //$$ || (AmsServerSettings.movableReinforcedDeepslate && state.isOf(Blocks.REINFORCED_DEEPSLATE))
            //$$ || (AmsServerSettings.movableSculkCatalyst && state.isOf(Blocks.SCULK_CATALYST))
            //$$ || (AmsServerSettings.movableSculkSensor && state.isOf(Blocks.SCULK_SENSOR))
            //$$ || (AmsServerSettings.movableSculkShrieker && state.isOf(Blocks.SCULK_SHRIEKER))
            //#endif
            //#if MC>=12000
            //$$ || (AmsServerSettings.movableCalibratedSculkSensor && state.isOf(Blocks.CALIBRATED_SCULK_SENSOR))
            //#endif
        ) {
            //#if MC<11700
            //$$ if (direction == Direction.DOWN && blockPos.getY() == 0) {
            //#else
            if (direction == Direction.DOWN && pos.getY() == world.getBottomY()) {
                //#endif
                cir.setReturnValue(false);
                //#if MC<11700
                //$$ } else if (direction == Direction.UP && blockPos.getY() == world.getHeight() - 1) {
                //#else
            } else if (direction == Direction.UP && pos.getY() == world.getTopY() - 1) {
                //#endif
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(true);
            }
        }
    }
}
