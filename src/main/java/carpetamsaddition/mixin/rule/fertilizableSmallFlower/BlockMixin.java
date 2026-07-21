/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package carpetamsaddition.mixin.rule.fertilizableSmallFlower;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.SporeBlossomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlowerBlock.class)
public abstract class BlockMixin implements BonemealableBlock{
    @Override
    public boolean isValidBonemealTarget(
        LevelReader reader, BlockPos pos, BlockState state
        //#if MC>=260300
        //$$ , BonemealSource bonemealSource
        //#endif
    ) {
        if (CarpetAMSAdditionSettings.fertilizableSmallFlower && (state.getBlock() instanceof FlowerBlock || state.getBlock() instanceof SporeBlossomBlock)) {
            return true;
        }

        //#if MC>=260300
        //$$ return this.isValidBonemealTarget(reader, pos, state, bonemealSource);
        //#else
        return this.isValidBonemealTarget(reader, pos, state);
        //#endif
    }

    @Override
    public boolean isBonemealSuccess(
        Level level, RandomSource randomSource, BlockPos pos, BlockState state
        //#if MC>=260300
        //$$ , BonemealSource bonemealSource
        //#endif
    ) {
        if (CarpetAMSAdditionSettings.fertilizableSmallFlower && (state.getBlock() instanceof FlowerBlock || state.getBlock() instanceof SporeBlossomBlock)) {
            return true;
        }
        //#if MC>=260300
        //$$ return this.isBonemealSuccess(level, randomSource, pos, state, bonemealSource);
        //#else
        return this.isBonemealSuccess(level, randomSource, pos, state);
        //#endif
    }

    @Override
    public void performBonemeal(
        ServerLevel level, RandomSource randomSource, BlockPos pos, BlockState state
        //#if MC>=260300
        //$$ , BonemealSource bonemealSource
        //#endif
    ) {
        if (CarpetAMSAdditionSettings.fertilizableSmallFlower && (state.getBlock() instanceof FlowerBlock || state.getBlock() instanceof SporeBlossomBlock)) {
            Block.popResource(level, pos, new ItemStack(state.getBlock(), 1));
        } else {
            //#if MC>=260300
            //$$ this.performBonemeal(level, randomSource, pos, state, bonemealSource);
            //#else
            this.performBonemeal(level, randomSource, pos, state);
            //#endif
        }
    }
}
