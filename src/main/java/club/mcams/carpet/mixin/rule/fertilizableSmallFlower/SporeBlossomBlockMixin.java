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

package club.mcams.carpet.mixin.rule.fertilizableSmallFlower;

//#if MC>=11700
import top.byteeeee.annotationtoolbox.annotation.GameVersion;
//#if MC>=11900
//$$ import net.minecraft.world.WorldView;
//$$ import net.minecraft.util.math.random.Random;
//#else
import java.util.Random;
import net.minecraft.world.BlockView;
//#endif
import club.mcams.carpet.AmsServerSettings;
import net.minecraft.block.SporeBlossomBlock;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import static net.minecraft.block.Block.dropStack;
//#else
//$$ import club.mcams.carpet.utils.compat.DummyClass;
//#endif

import org.spongepowered.asm.mixin.Mixin;

//#if MC>=11700
@GameVersion(version = "Minecraft >= 1.17")
@Mixin(SporeBlossomBlock.class)
public abstract class SporeBlossomBlockMixin implements Fertilizable {
//#else
//$$ @Mixin(DummyClass.class)
//$$ public abstract class SporeBlossomBlockMixin {
//#endif

//#if MC>=11700

    @Override
    //#if MC>=12002
    //$$ public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
    //#elseif MC>=11900
    //$$ public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
    //#else
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        //#endif
        return AmsServerSettings.fertilizableSmallFlower;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return AmsServerSettings.fertilizableSmallFlower;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (AmsServerSettings.fertilizableSmallFlower) {
            dropStack(world, pos, new ItemStack((ItemConvertible) this));
        }
    }

//#endif
}
