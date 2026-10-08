/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

package carpetamsaddition.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * 相对中心位置的方块条件
 */
public final class BlockCondition {
    private final BiPredicate<BlockGetter, BlockPos> predicate;

    private BlockCondition(BiPredicate<BlockGetter, BlockPos> predicate) {
        this.predicate = predicate;
    }

    /** 检查相对中心坐标偏移处的方块状态 */
    public static BlockCondition at(int x, int y, int z, Predicate<BlockState> predicate) {
        return new BlockCondition((world, origin) -> predicate.test(world.getBlockState(origin.offset(x, y, z))));
    }

    /** 检查指定世界方向和距离处的方块状态 */
    public static BlockCondition at(Direction direction, int distance, Predicate<BlockState> predicate) {
        return at(direction.getStepX() * distance, direction.getStepY() * distance, direction.getStepZ() * distance, predicate);
    }

    /** 检查相邻位置是否是给定方块中的任意一种 */
    public static BlockCondition matchingBlocks(Direction direction, Block... blocks) {
        return matchingBlocks(direction, 1, blocks);
    }

    /** 检查指定方向和距离处是否是给定方块中的任意一种 */
    public static BlockCondition matchingBlocks(Direction direction, int distance, Block... blocks) {
        return at(direction, distance, blocksPredicate(blocks));
    }

    /** 检查坐标偏移处是否是给定方块中的任意一种 */
    public static BlockCondition matchingBlocks(int x, int y, int z, Block... blocks) {
        return at(x, y, z, blocksPredicate(blocks));
    }

    private static Predicate<BlockState> blocksPredicate(Block[] blocks) {
        Block[] copy = blocks.clone();

        return state -> {
            for (Block block : copy) {
                if (state.is(block)) {
                    return true;
                }
            }

            return false;
        };
    }

    /** 所有条件均满足才匹配 */
    public static BlockCondition allOf(BlockCondition... conditions) {
        BlockCondition[] copy = copyConditions(conditions);
        return new BlockCondition((world, origin) -> {
            for (BlockCondition condition : copy) {
                if (!condition.predicate.test(world, origin)) {
                    return false;
                }
            }

            return true;
        });
    }

    /** 任意条件满足即匹配 */
    public static BlockCondition anyOf(BlockCondition... conditions) {
        BlockCondition[] copy = copyConditions(conditions);
        return new BlockCondition((world, origin) -> {
            for (BlockCondition condition : copy) {
                if (condition.predicate.test(world, origin)) {
                    return true;
                }
            }

            return false;
        });
    }

    private static BlockCondition[] copyConditions(BlockCondition[] conditions) {
        return conditions.clone();
    }

    /** @return 返回当前条件的否定条件 */
    public BlockCondition not() {
        return new BlockCondition((world, origin) -> !this.predicate.test(world, origin));
    }

    /** 使用当前世界状态检查条件 */
    public boolean matches(BlockGetter world, BlockPos blockPos) {
        return this.predicate.test(world, blockPos);
    }
}
